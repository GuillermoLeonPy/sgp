create or replace function f_determinate_sale_invoice_stamping_number
(
pid_sale_invoice_stamping   			bigint,
p_user_requester 		   			varchar
)
returns numeric as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_determinate_invoice_stamping_number';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vrecord					record;
		b1_error_message			text;
		b1_error_message_hint		text;
		v_invoice_next_number_to_use	varchar;
	begin
		select numeration.id,numeration.end_number,numeration.next_number_to_use, 
		stamping.sale_invoice_stamping_number,
		stamping.effective_beginning_date,
		stamping.effective_end_date
		into vrecord
		from
		pms_sale_invoice_stamping stamping
		left outer join
		pms_sale_invoice_stamping_numeration numeration
		on 
		stamping.id = numeration.id_sale_invoice_stamping
		and numeration.is_active = 'S'
		where
		stamping.id = pid_sale_invoice_stamping;
		
		if vrecord.id is null then
			b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.determinate.sale.invoice.stamping.number.no.stamping.numeration.available.to.use.error'||''||'#-numeric-#'||vrecord.sale_invoice_stamping_number||''||'#-date-#'||to_char(vrecord.effective_beginning_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vrecord.effective_end_date,'DD/MM/YYYY HH24:MI:SS')||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';				
		elsif vrecord.end_number = vrecord.next_number_to_use then
			update pms_sale_invoice_stamping_numeration
			set
				last_modif_date = now(),
				last_modif_user = p_user_requester,
				is_active = null
			where 
				id = vrecord.id;

			/* activate the next numeration */
			declare
				vid			bigint;
			begin
				select min(id) into vid from pms_sale_invoice_stamping_numeration
				where id_sale_invoice_stamping = pid_sale_invoice_stamping
				and id > vrecord.id;

				if vid is not null then
					update pms_sale_invoice_stamping_numeration
					set 
						last_modif_date = now(),
						last_modif_user = p_user_requester,
						is_active = 'S'
					where 
						id = vid;
				end if;
			end;
		else
			update pms_sale_invoice_stamping_numeration
			set
				last_modif_date = now(),
				last_modif_user = p_user_requester,
				next_number_to_use = next_number_to_use + 1
			where 
				id = vrecord.id;
		end if;
		return vrecord.next_number_to_use;
	end;		

	
	EXCEPTION
		WHEN SQLSTATE 'P9989' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
