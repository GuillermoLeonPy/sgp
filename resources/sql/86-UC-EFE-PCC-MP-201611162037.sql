/*86-UC-EFE-PCC-MP-201611162037*/
--ALTER TABLE pms_cash_receipt_document ADD emission_date	timestamp;

insert into pms_person (
id,
ruc,
commercial_name,
is_customer,
is_supplier,
is_functionary,
creation_user
) values
(nextval('pms_person_id_sq'),
'999999999-9',
'PRODUCTION MANAGEMENT SYSTEM USER ENTERPRISE NAME',
'N','N','N',
'xxx'
);

create or replace function p_effectuate_purchase_invoice_payment
(
pid_purchase_invoice_payment					bigint,
pcash_receipt_document_identifier_number		varchar,
pcash_receipt_document_emission_date			timestamp,
poverdue_amount							numeric,
plast_modif_user 		   					varchar
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
	RAISE INFO 'executing p_effectuate_purchase_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	
	declare
		vpurchase_invoice_payment_record				record;
		vpending_purchase_invoice_payment_record		record;
		vpurchase_invoice_record				record;
		vperson_record						record;
		vcash_receipt_document_amount			pms_cash_receipt_document.amount%type;
	begin
		select * into vpurchase_invoice_payment_record from pms_purchase_invoice_payment
		where id = pid_purchase_invoice_payment;


		if poverdue_amount is null then
			vcash_receipt_document_amount := vpurchase_invoice_payment_record.amount;
		else
			if poverdue_amount < vpurchase_invoice_payment_record.amount then
				declare
					b1_error_message		text;
					b1_error_message_hint	text;
				begin
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.payment.dto.overdue.amount.less.than.original.amount.error'||''||'#-numeric-#'||poverdue_amount||''||'#-numeric-#'||vpurchase_invoice_payment_record.amount||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
				end;
			end if;--if poverdue_amount < vpurchase_invoice_payment_record.amount then
			vcash_receipt_document_amount := poverdue_amount;
		end if;
		
		select * into vpurchase_invoice_record from pms_purchase_invoice where id = vpurchase_invoice_payment_record.id_purchase_invoice;

		if pcash_receipt_document_emission_date < vpurchase_invoice_record.emission_date then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.payment.dto.cash.receipt.document.emission.date.prior.to.purchase.invoice.emission.date.error'||''||'#-date-#'||to_char(pcash_receipt_document_emission_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vpurchase_invoice_record.emission_date,'DD/MM/YYYY HH24:MI:SS')||''||vpurchase_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		update pms_purchase_invoice_payment 
		set 
		status = 'application.common.status.canceled',
		cancellation_date = pcash_receipt_document_emission_date,
		last_modif_user = plast_modif_user,
		last_modif_date = now(),
		overdue_amount = poverdue_amount
		where 
			id = vpurchase_invoice_payment_record.id;

		select * into vperson_record from pms_person where ruc = '999999999-9';
		
		insert into pms_cash_receipt_document
			(id,
			id_currency,
			id_person,--the production management systems enterprise person id
			identifier_number,
			bussines_name,--the production management systems enterprise person
			bussines_ci_ruc,--the production management systems enterprise person
			amount,
			creation_user,
			id_purchase_invoice_payment,
			emission_date)
		values
			(nextval('pms_cash_receipt_document_id_sq'),
			vpurchase_invoice_payment_record.id_currency,
			vperson_record.id,
			upper(trim(pcash_receipt_document_identifier_number)),
			vperson_record.commercial_name,
			vperson_record.ruc,
			vcash_receipt_document_amount,
			plast_modif_user,
			vpurchase_invoice_payment_record.id,
			pcash_receipt_document_emission_date);

		/* check if exists any pending payment*/

		select * into vpending_purchase_invoice_payment_record
		from pms_purchase_invoice_payment where id_purchase_invoice = vpurchase_invoice_record.id
		and payment_number = vpurchase_invoice_payment_record.payment_number + 1;

		if vpending_purchase_invoice_payment_record.id is not null then
			update pms_purchase_invoice_payment
			set status = 'application.common.status.in.progress',
			last_modif_user = plast_modif_user,last_modif_date = now()
			where id = vpending_purchase_invoice_payment_record.id;

			/*update the purchase invoice status*/
			if vpurchase_invoice_record.status = 'application.common.status.payment.in.progress' then
				update pms_purchase_invoice
				set status = 'application.common.status.partial.balance',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vpurchase_invoice_record.id;
			end if;
		else
			--there is no more payments
				update pms_purchase_invoice
				set status = 'application.common.status.canceled',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vpurchase_invoice_record.id;
		end if;--if vpending_purchase_invoice_payment_record.id is not null then		
	end;--begin

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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';