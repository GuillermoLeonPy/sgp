/*44-UC-ANU-FAC-201610151448*/

update pms_order
set status = 'application.common.status.invoiced',
previous_status = null
where id = 8;

update pms_sale_invoice
set status = 'application.common.status.payment.in.progress'
where id = 41;




create or replace function p_annul_sale_invoice
(
pid_sale_invoice	 				bigint,
plast_modif_user	 		   			varchar
)
returns void as
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
	RAISE INFO 'executing p_annul_sale_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vsale_invoice_record			record;
		vorder_record					record;
	begin
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = pid_sale_invoice;

		if vsale_invoice_record.status != 'application.common.status.pending' 
		and vsale_invoice_record.status != 'application.common.status.payment.in.progress' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.annul.sale.invoice.sale.invoice.has.canceled.payments.error'||''||vsale_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		update pms_sale_invoice
				set status = 'application.common.status.annulled',
				previous_status = vsale_invoice_record.status,
				previous_total_amount = null,
				previous_value_added_amount = null,
				previous_total_tax_amount = null,
				previous_exempt_amount = null,
				previous_value_added_tax_10_amount = null,
				previous_value_added_tax_5_amount = null,
				previous_tax_10_amount = null,
				previous_tax_5_amount = null,
				annulment_date = now(),
				last_modif_user = plast_modif_user,
				last_modif_date = now()
		where id = vsale_invoice_record.id;

		--swith the sale invoice items to the pending status
		declare
			vcursor CURSOR FOR
			select sii.* from pms_sale_invoice_item sii where 
			sii.id_sale_invoice = vsale_invoice_record.id and sii.status != 'application.common.status.discarded'
			order by sii.id asc;
		begin
			for vsale_invoice_item_record in vcursor loop
				update pms_sale_invoice_item
					set status = 'application.common.status.annulled',
					previous_status = vsale_invoice_item_record.status,
					previous_exempt_unit_price_amount = null,
					previous_value_added_tax_10_unit_price_amount = null,
					previous_value_added_tax_5_unit_price_amount = null,
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vsale_invoice_item_record.id;
			end loop;
		end;
			

		update pms_sale_invoice_payment
				set status = 'application.common.status.annulled',
				annulment_date = now(),
				last_modif_user = plast_modif_user,
				last_modif_date = now()
		where 
		id_sale_invoice = vsale_invoice_record.id
		and (status = 'application.common.status.pending' or status = 'application.common.status.in.progress');

		--update the order
		select ord.* into vorder_record from pms_order ord where ord.id = vsale_invoice_record.id_order;
		
		update pms_order
			set status = 'application.common.status.annulled',
			previous_status = vorder_record.status,
			previous_amount = null,
			previous_exempt_amount = null,
			previous_value_added_tax_10_amount = null,
			previous_value_added_tax_5_amount = null,
			last_modif_user = plast_modif_user,
			last_modif_date = now()
		where id = vorder_record.id;
		
		--swith the order items to the annulled status
		declare
			vcursor CURSOR FOR
			select oi.* from pms_order_item oi where oi.id_order = vorder_record.id order by oi.id asc;
		begin
			for vorder_item_record in vcursor loop
				update pms_order_item
					set status = 'application.common.status.annulled',
					previous_status = vorder_item_record.status,
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vorder_item_record.id;					
			end loop;
		end;
		
	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



/* ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** */
/* ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** */
/* ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** */
/* ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** ***** */


create table pms_credit_note_stamping_numeration(
id	   		 		   			bigint,
start_number						numeric(7,0) not null,
end_number						numeric(7,0) not null,
next_number_to_use					numeric(7,0) not null,
id_sale_invoice_stamping				bigint not null,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp,
is_active 						varchar(1)
);

--ALTER TABLE pms_credit_note_stamping_numeration ADD id_sale_invoice_stamping	bigint not null;
--ALTER TABLE pms_credit_note_stamping_numeration ADD next_number_to_use numeric(7,0) not null;
--ALTER TABLE pms_credit_note_stamping_numeration ADD is_active varchar(1);
ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT chk_is_active CHECK (is_active in ('S'));
ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT
pms_credit_note_stamping_numeration_uk_01 UNIQUE (is_active);


ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT pms_credit_note_stamping_numeration_id_pk PRIMARY KEY (id);

ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT 
chk_end_number CHECK (end_number > start_number);

ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT 
chk_start_number CHECK (start_number > 0);

ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT 
chk_next_number_to_use_01 CHECK (next_number_to_use >= start_number);

ALTER TABLE pms_credit_note_stamping_numeration ADD CONSTRAINT 
chk_next_number_to_use_02 CHECK (next_number_to_use <= end_number);

ALTER TABLE pms_credit_note_stamping_numeration 
ADD constraint pms_credit_note_stamping_numeration_fk_01
FOREIGN KEY (id_sale_invoice_stamping) REFERENCES pms_sale_invoice_stamping (id);

create sequence pms_credit_note_stamping_numeration_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_credit_note_stamping_numeration.id;

--delete from pms_credit_note_stamping_numeration
begin transaction;
insert into pms_credit_note_stamping_numeration
values
(nextval('pms_credit_note_stamping_numeration_id_sq'),
44000,44078,44000,
1,
NULL,now(),null,null,'S'
);

/* ***************************** ********************************** ********************************* */
/* OBSERVACION: EN POSTGRE SI NO SE ESPECIFICA EL NOMBRE DE COLUMNA ********************************* */
/* EN INSTRUCCION INSERT NO SE REQUIERE ESPECIFICAR TODAS LAS COLUMNAS ****************************** */
/* ***************************** ********************************** ********************************* */

insert into pms_credit_note_stamping_numeration
values
(nextval('pms_credit_note_stamping_numeration_id_sq'),
44000,44078,44000,
1,
NULL,now())


commit;


select f_determinate_credit_note_stamping_number_id(1);
select f_determinate_credit_note_stamping_number(1,'xxx');

create or replace function f_determinate_credit_note_stamping_number
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
	RAISE INFO 'executing f_determinate_credit_note_stamping_number';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vrecord					record;
		b1_error_message			text;
		b1_error_message_hint		text;
		v_credit_note_next_number_to_use	varchar;
	begin
		select numeration.id,numeration.end_number,numeration.next_number_to_use, 
		stamping.sale_invoice_stamping_number,
		stamping.effective_beginning_date,
		stamping.effective_end_date
		into vrecord
		from
		pms_sale_invoice_stamping stamping
		left outer join
		pms_credit_note_stamping_numeration numeration
		on 
		stamping.id = numeration.id_sale_invoice_stamping
		and numeration.is_active = 'S'
		where
		stamping.id = pid_sale_invoice_stamping;

		
		if vrecord.id is null then
			b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.determinate.credit.note.stamping.number.no.stamping.numeration.available.to.use.error'||''||'#-numeric-#'||vrecord.sale_invoice_stamping_number||''||'#-date-#'||to_char(vrecord.effective_beginning_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vrecord.effective_end_date,'DD/MM/YYYY HH24:MI:SS')||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';				
		elsif vrecord.end_number = vrecord.next_number_to_use then
			update pms_credit_note_stamping_numeration
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
				select min(id) into vid from pms_credit_note_stamping_numeration
				where id_sale_invoice_stamping = pid_sale_invoice_stamping
				and id > vrecord.id;

				if vid is not null then
					update pms_credit_note_stamping_numeration
					set 
						last_modif_date = now(),
						last_modif_user = p_user_requester,
						is_active = 'S'
					where 
						id = vid;
				end if;
			end;
		else
			update pms_credit_note_stamping_numeration
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


--select f_determinate_sale_invoice_stamping_number_id(1);
create or replace function f_determinate_credit_note_stamping_number_id
(
pid_sale_invoice_stamping   			bigint
)
returns bigint as
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
	RAISE INFO 'executing f_determinate_credit_note_stamping_number_id';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vrecord			record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select numeration.id,numeration.end_number,numeration.next_number_to_use, 
		stamping.sale_invoice_stamping_number,
		stamping.effective_beginning_date,
		stamping.effective_end_date
		into vrecord
		from
		pms_sale_invoice_stamping stamping
		left outer join
		pms_credit_note_stamping_numeration numeration
		on 
		stamping.id = numeration.id_sale_invoice_stamping
		and numeration.is_active = 'S'
		where
		stamping.id = pid_sale_invoice_stamping;		

		if vrecord.id is null then
			b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.determinate.credit.note.stamping.number.no.stamping.numeration.available.to.use.error'||''||'#-numeric-#'||vrecord.sale_invoice_stamping_number||''||'#-date-#'||to_char(vrecord.effective_beginning_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vrecord.effective_end_date,'DD/MM/YYYY HH24:MI:SS')||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;

		return vrecord.id;
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