/*50-REGENERAR-PAGOS-POR-FACTURA-201610191142*/
update pms_sale_invoice_payment 
set status = 'application.common.status.in.progress',
annulment_date = null,
last_modif_date = now()
where id = 10;

ALTER TABLE pms_sale_invoice_payment DROP CONSTRAINT
pms_sale_invoice_payment_uk_01



delete from pms_sale_invoice_payment where id = 18

select now()

create or replace function p_regenerate_sale_invoice_payment
(
pid_sale_invoice	 				bigint,
pcreation_user	 		   			varchar
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
	RAISE INFO 'executing p_regenerate_sale_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	
	declare
		vsale_invoice_record			record;
		vfunction_call_fake_return		varchar;
	begin
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = pid_sale_invoice;

		if vsale_invoice_record.status != 'application.common.status.payment.in.progress'
		and vsale_invoice_record.previous_status != 'application.common.status.revision' then
			declare
				vrecord				record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.re.generate.sale.invoice.payment.sale.invoice.not.in.required.previous.status.error'||''||vsale_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
			
		--anull current payments registries
		update pms_sale_invoice_payment
			set status = 'application.common.status.annulled',
				annulment_date = now(),
				last_modif_user = pcreation_user,
				last_modif_date = now()
			where id_sale_invoice = vsale_invoice_record.id 
			and status = 'application.common.status.pending'
			or status = 'application.common.status.in.progress';

		/* only sale invoice with emission_date = today can regenerate payments */
		if date_trunc('day',vsale_invoice_record.emission_date) != date_trunc('day',now()) then
			declare
				vrecord				record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.re.generate.sale.invoice.payment.sale.invoice.not.issued.today.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		PERFORM p_i_pms_sale_invoice_payment(vsale_invoice_record.id,pcreation_user);	
	end;

	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
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









/* ************************ DE AQUI EN ADELANTE DESCARTADO ****************************************** */
/* ************************ DE AQUI EN ADELANTE DESCARTADO ****************************************** */
/* ************************ DE AQUI EN ADELANTE DESCARTADO ****************************************** */
/* ************************ DE AQUI EN ADELANTE DESCARTADO ****************************************** */
/* ************************ DE AQUI EN ADELANTE DESCARTADO ****************************************** */

create or replace function p_regenerate_sale_invoice_payment
(
pid_sale_invoice	 				bigint,
pcreation_user	 		   			varchar
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
	RAISE INFO 'executing p_regenerate_sale_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	
	declare
		vsale_invoice_record			record;
	begin
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = pid_sale_invoice;

		if si.previous_status != 'application.common.status.revision' then
			declare
				vrecord				record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.re.generate.sale.invoice.payment.sale.invoice.not.in.required.previous.status.error'||''||vsale_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		
		declare
			vcursor cursor for
			select sip.* from pms_sale_invoice_payment where sip.id_sale_invoice = vsale_invoice_record.id;
			v_canceled_payments_amount		pms_sale_invoice_payment.amount%type;
			v_pending_payments_amount		pms_sale_invoice_payment.amount%type;
			v_sale_invoice_balance			pms_sale_invoice_payment.sale_invoice_balance%type;
			vid_credit_note				pms_credit_note.id%type;
			vid_sale_invoice_payment			pms_sale_invoice_payment.id%type;
			vpayment_number				pms_sale_invoice_payment.payment_number%type;
			vpayment_amount				pms_sale_invoice_payment.amount%type;
			vpending_credit_note_amount		pms_credit_note.total_amount%type;
			vpending_payments_count			bigint;
		begin
			v_canceled_payments_amount := 0; 
			v_pending_payments_amount := 0;
			vpayment_number := 0;
			vcancelled_payments_count := 0;
			--determinate the v_canceled_payments_amount and v_pending_payments_amount
			for vsale_invoice_payment_record in vcursor loop			
				if vsale_invoice_payment_record.status = 'application.common.status.canceled'
				and vsale_invoice_payment_record.cancellation_date is not null then				
					v_canceled_payments_amount := v_canceled_payments_amount + vsale_invoice_payment_record.amount;
				elsif vsale_invoice_payment_record.status = 'application.common.status.pending'
				and vsale_invoice_payment_record.cancellation_date is null then				
					v_pending_payments_amount := v_pending_payments_amount;
					vpending_payments_count := vpending_payments_count + 1;					
				end if;				
			end loop;--for vsale_invoice_payment_record in vcursor loop

			if v_canceled_payments_amount > 0 then
				--create cancelled credit note for the canceled payments 
				--parameters: vid_credit_note (must be generated by sequence), 
				--pid_sale_invoice, v_canceled_payments_amount, vcredit_note_status = cancelled
				--copy the sale invoice id_person, id_currency, the NOT discarded or modificated quantity items
				vid_credit_note := nextval('pms_credit_note_id_sq');
					
				--create one sale_invoice_payment in canceled status for the new sale invoice 
				--parameters: pid_sale_invoice, v_canceled_payments_amount, 
				--vsale_invoice_payment_status = cancelled, vid_credit_note
				--relate the payment to the credit note
				vid_sale_invoice_payment := nextval('pms_sale_invoice_payment_id_sq');
				
				if v_canceled_payments_amount >= vsale_invoice_record.total_amount then
				--no olvidar contemplar el caso v_canceled_payments_amount = vsale_invoice_record.total_amount
					v_sale_invoice_balance := 0;
					vpayment_amount := vsale_invoice_record.total_amount;
					vpending_credit_note_amount := v_canceled_payments_amount - vsale_invoice_record.total_amount;
				else
					v_sale_invoice_balance := vsale_invoice_record.total_amount - v_canceled_payments_amount;
				end if;
				vpayment_number := vpayment_number + 1;
				insert into pms_sale_invoice_payment 
						(id,id_sale_invoice,id_currency,amount,
						sale_invoice_balance,payment_number,registration_date,payment_due_date,
						creation_user,creation_date,status) 
						values
						(vid_sale_invoice_payment,vsale_invoice_record.id,vsale_invoice_record.id_currency,
						v_canceled_payments_amount,v_sale_invoice_balance,vpayment_number, now(), now(),
						pcreation_user,now(),'application.common.status.canceled');
	
				--relate the payment to the cancelled credit note
				insert into pms_sale_invoice_payment_cancel_documents (id,id_sale_invoice_payment,id_credit_note)
				values
				(nextval('pms_sale_invoice_payment_cancel_documents_id_sq'),
				vid_sale_invoice_payment,vid_credit_note);

				if vpending_credit_note_amount > 0 then
					--create a pending credit note
					--parameters: vpending_credit_note_amount, id_sale_invoice_to_copy = vsale_invoice_record.id
					--to copy: id_person, id_currency, the discarded or modificated quantity items

					--create the no usable credit note
					--parameters: 
					--credit_note_amount = vsale_invoice_record.previous_total_amount - vsale_invoice_record.total_amount - vpending_credit_note_amount
					--id_sale_invoice_to_copy = vsale_invoice_record.id
					--to copy: id_person, id_currency, the discarded or modificated quantity items
				else
					--create the no usable credit note
					--parameters: 
					--credit_note_amount = vsale_invoice_record.previous_total_amount - vsale_invoice_record.total_amount
					--id_sale_invoice_to_copy = vsale_invoice_record.id
					--to copy: id_person, id_currency, the discarded or modificated quantity items					
				end if;--if vpending_credit_note_amount > 0 then
			end if;--if v_canceled_payments_amount > 0 then
			
			if v_pending_payments_amount > 0 then
				--determinate how many pending payments had the 1st invoice: vpending_payments_count
				--create equivalent pending payments v_pending_payments_amount / vpending_payments_count
				for vsale_invoice_payment_record in vcursor loop
					if vsale_invoice_payment_record.status = 'application.common.status.pending'
					and vsale_invoice_payment_record.cancellation_date is null then
						vpayment_number := vpayment_number + 1;
						insert into pms_sale_invoice_payment 
						(id,id_sale_invoice,id_currency,
						amount,
						sale_invoice_balance,
						payment_number,registration_date,payment_due_date,
						creation_user,creation_date) 
								values
						(vid_sale_invoice_payment,vsale_invoice_record.id,vsale_invoice_record.id_currency,
						(v_pending_payments_amount / vpending_payments_count),
						((v_pending_payments_amount - ((v_pending_payments_amount / vpending_payments_count) * vcounter))),
						vpayment_number, now(),vsale_invoice_payment_record.payment_due_date,
						pcreation_user,now());
					end if;--if vsale_invoice_payment_record.status = 'application.common.status.pending'
					--and vsale_invoice_payment_record.cancellation_date is null then
				end loop;--for vsale_invoice_payment_record in vcursor loop
			end if;--if v_pending_payments_amount > 0 then

			--anull current payments registries
			update pms_sale_invoice_payment
				set 	status = 'application.common.status.annulled',
					annulment_date = now(),
					last_modif_user = pcreation_user,
					last_modif_date = now()
				where id = vsale_invoice_payment_record.id;	
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';