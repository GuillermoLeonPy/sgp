/*51-UC-EFE-PFV-201610211156*/

create table pms_cash_receipt_document(
id	   		 		   			bigint,
id_sale_invoice_payment	   			bigint not null,
id_currency						bigint not null,
id_person							bigint not null,
identifier_number   				varchar(18) NOT NULL,
bussines_name						varchar(500) not null,
bussines_ci_ruc					varchar(50) not null,
registration_date					timestamp not null default now(),
amount							numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);
);

ALTER TABLE pms_cash_receipt_document ADD id_purchase_invoice_payment	bigint;
ALTER TABLE pms_cash_receipt_document ADD emission_date	timestamp;
ALTER TABLE pms_cash_receipt_document alter column id_sale_invoice_payment drop not null;

ALTER TABLE pms_cash_receipt_document ADD CONSTRAINT chk_amount CHECK (amount > 0);
ALTER TABLE pms_cash_receipt_document ADD CONSTRAINT pms_cash_receipt_document_id_pk PRIMARY KEY (id);

ALTER TABLE pms_cash_receipt_document 
ADD constraint pms_cash_receipt_document_fk_01
FOREIGN KEY (id_sale_invoice_payment) REFERENCES pms_sale_invoice_payment (id);


ALTER TABLE pms_cash_receipt_document 
ADD constraint pms_cash_receipt_document_fk_02
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_cash_receipt_document 
ADD constraint pms_cash_receipt_document_fk_03
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

ALTER TABLE pms_cash_receipt_document 
ADD constraint pms_cash_receipt_document_fk_04
FOREIGN KEY (id_purchase_invoice_payment) REFERENCES pms_purchase_invoice_payment (id);

create sequence pms_cash_receipt_document_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_cash_receipt_document.id;


create or replace function p_effectuate_sale_invoice_payment
(
pid_sale_invoice_payment				bigint,
pid_credit_note					bigint,
plast_modif_user 		   			varchar
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
	RAISE INFO 'executing p_effectuate_sale_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	

	declare
		vsale_invoice_payment_record				record;
		vpending_sale_invoice_payment_count		bigint;
		vsale_invoice_record					record;
		vcredit_note_record						record;
		vcurrency_record						record;
		vorder_record							record;
		vcredit_note_balance					pms_credit_note.credit_note_balance%type;
		v_resulting_credit_note_balance			pms_credit_note.credit_note_balance%type;
		v_resulting_credit_note_status			pms_credit_note.status%type;
		v_cash_receipt_document_amount			pms_cash_receipt_document.amount%type;
		v_cash_receipt_document_identifier_number	pms_cash_receipt_document.identifier_number%type;
	begin
		select sip.* into vsale_invoice_payment_record from pms_sale_invoice_payment sip
		where sip.id = pid_sale_invoice_payment;
		if vsale_invoice_payment_record.status != 'application.common.status.in.progress' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.effectuate.sale.invoice.payment.payment.not.in.required.status.to.be.effectuated.error'||''||vsale_invoice_payment_record.payment_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = vsale_invoice_payment_record.id_sale_invoice;

		if pid_credit_note is not null then
			select cn.* into vcredit_note_record from pms_credit_note cn
			where cn.id = pid_credit_note;

			/* ****** CHECK THE CREDIT NOTE STATUS ******** */
			if vcredit_note_record.status = 'application.common.status.canceled' or
			vcredit_note_record.status = 'application.common.status.annulled' then
				declare
					b1_error_message		text;
					b1_error_message_hint	text;
				begin
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.effectuate.sale.invoice.payment.credit.note.not.required.status.to.be.used.error'||''||vcredit_note_record.identifier_number||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
				end;
			end if;

			if vcredit_note_record.credit_note_balance < 1 then
				declare
					b1_error_message		text;
					b1_error_message_hint	text;
				begin
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.effectuate.sale.invoice.payment.credit.note.has.no.balance.to.use.error'||''||vcredit_note_record.identifier_number||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
				end;
			end if;--if vcredit_note_record.credit_note_balance < 1 then

			--update the credit note balance
			if vcredit_note_record.id_currency != vsale_invoice_payment_record.id_currency then
				--convert the credit note balance to the payment currency
				vcredit_note_balance := f_convert_currency_amount(vcredit_note_record.id_currency,vsale_invoice_payment_record.id_currency,vcredit_note_record.credit_note_balance);
			else
				vcredit_note_balance := vcredit_note_record.credit_note_balance;
			end if;--if vcredit_note_record.id_currency != vsale_invoice_payment_record.id_currency then
			
			--check if then credit note balance is enougth with the sale invoice payment
			if vcredit_note_balance < vsale_invoice_payment_record.amount then				
				--calculate the difference to make a cash receipt document
				v_cash_receipt_document_amount := vsale_invoice_payment_record.amount - vcredit_note_balance;
				--calculate the resulting balance: now the credit note balance is zero
				v_resulting_credit_note_balance := 0;
				--select currency.* into vcurrency_record from pms_currency currency 
				--where currency.id = vsale_invoice_payment_record.id_currency;
				--declare
				--	b1_error_message		text;
				--	b1_error_message_hint	text;
				--begin
				--	b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.payment.dao.effectuate.sale.invoice.payment.credit.note.has.no.enough.balance.error'||''||vcredit_note_record.identifier_number||''||vcurrency_record.id_code||''||'#-numeric-#'||vcredit_note_balance||''||'#-numeric-#'||vsale_invoice_payment_record.amount||'end.of.message';
				--	b1_error_message_hint:=b1_error_message;
				--	RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
				--end;
			else--if vcredit_note_balance < vsale_invoice_payment_record.amount then
				--calculate the resulting balance
				if vcredit_note_record.id_currency != vsale_invoice_payment_record.id_currency then
					v_resulting_credit_note_balance := f_convert_currency_amount(vsale_invoice_payment_record.id_currency,vcredit_note_record.id_currency,(vcredit_note_balance - vsale_invoice_payment_record.amount ));
				else--if vcredit_note_record.id_currency != vsale_invoice_payment_record.id_currency then
					v_resulting_credit_note_balance := vcredit_note_balance - vsale_invoice_payment_record.amount;
				end if;--if vcredit_note_record.id_currency != vsale_invoice_payment_record.id_currency then
			end if;--if vcredit_note_balance < vsale_invoice_payment_record.amount then

			if v_resulting_credit_note_balance < 1 then
				--switch the credit note status
				v_resulting_credit_note_status := 'application.common.status.canceled';
				update pms_credit_note
					set credit_note_balance = v_resulting_credit_note_balance,
					status = v_resulting_credit_note_status,last_modif_user = plast_modif_user,
					last_modif_date = now(),cancellation_date = now()
				where id = vcredit_note_record.id;
			else--if v_resulting_credit_note_balance < 1 then
				v_resulting_credit_note_status := 'application.common.status.partial.balance';
				update pms_credit_note
					set credit_note_balance = v_resulting_credit_note_balance,
					status = v_resulting_credit_note_status,last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vcredit_note_record.id;
			end if;--if v_resulting_credit_note_balance < 1 then			
			/* ********************************** */
			/* calculate charge if due date apply */
			/* ********************************** */
			--effectuate the payment 
			update pms_sale_invoice_payment
				set status = 'application.common.status.canceled',
				cancellation_date = now(),
				last_modif_user = plast_modif_user,
				last_modif_date = now()
			where id = vsale_invoice_payment_record.id;
			--check if a cash receipt document must be created
			if v_cash_receipt_document_amount is not null then
				--the difference between the credit note balance and the sale invoice payment to make a cash receipt document
				v_cash_receipt_document_identifier_number := vsale_invoice_record.identifier_number||'-'||trim(to_char(vsale_invoice_payment_record.payment_number,'99'));
				insert into pms_cash_receipt_document
				(id,	id_sale_invoice_payment,	id_currency,	id_person,
				identifier_number,	bussines_name,	bussines_ci_ruc,
				amount,	creation_user)
				values
				(nextval('pms_cash_receipt_document_id_sq'),vsale_invoice_payment_record.id, vsale_invoice_payment_record.id_currency, vsale_invoice_record.id_person,
				v_cash_receipt_document_identifier_number, vsale_invoice_record.bussines_name,vsale_invoice_record.bussines_ci_ruc,
				v_cash_receipt_document_amount, plast_modif_user);
			else--if v_cash_receipt_document_amount is not null then
				--to register the money amount payed when a credit note was used
				v_cash_receipt_document_amount := 0;
			end if;--if v_cash_receipt_document_amount is not null then

			/* register the document used in the payment: in this case the credit note*/
			insert into pms_sale_invoice_payment_cancel_documents (
				id,	
				id_sale_invoice_payment,	id_credit_note,	resulting_payment_amount,
				creation_user) values
				(nextval('pms_sale_invoice_payment_cancel_documents_id_sq'),
				vsale_invoice_payment_record.id,	vcredit_note_record.id,	v_cash_receipt_document_amount,
				plast_modif_user);
			
			/* check if all the payments has been cancelled and the sale invoice must be cancelled */
			select count(id) into vpending_sale_invoice_payment_count 
			from pms_sale_invoice_payment where id_sale_invoice = vsale_invoice_record.id
			and status = 'application.common.status.pending';
								
			if vpending_sale_invoice_payment_count = 0 then
				update pms_sale_invoice set status = 'application.common.status.canceled',
				cancellation_date = now(),last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vsale_invoice_record.id;

				update pms_sale_invoice_item set status = 'application.common.status.canceled',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id_sale_invoice = vsale_invoice_record.id
				and status != 'application.common.status.discarded';					
			else/* if this was not the las payment of the invoice, update the next payment status */
				update pms_sale_invoice_payment
					set status = 'application.common.status.in.progress',
					last_modif_user = plast_modif_user,last_modif_date = now()
					where 
					id_sale_invoice = vsale_invoice_record.id
					and status = 'application.common.status.pending'
					and payment_number = (vsale_invoice_payment_record.payment_number + 1);

				/* update the sale invoice to the application.common.status.partial.balance */
				update pms_sale_invoice set status = 'application.common.status.partial.balance',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vsale_invoice_record.id;

				update pms_sale_invoice_item set status = 'application.common.status.partial.balance',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id_sale_invoice = vsale_invoice_record.id
				and status != 'application.common.status.discarded';
				
			end if;--if vpending_sale_invoice_payment_count = 0 then	
		else--if pid_credit_note is not null then
			--effectuate the payment with out credit note use
			update pms_sale_invoice_payment
				set status = 'application.common.status.canceled',
				cancellation_date = now(),
				last_modif_user = plast_modif_user,
				last_modif_date = now()
			where id = vsale_invoice_payment_record.id;				
			/* check if all the payments has been cancelled and the sale invoice must be cancelled */
			select count(id) into vpending_sale_invoice_payment_count 
			from pms_sale_invoice_payment where id_sale_invoice = vsale_invoice_record.id
			and status = 'application.common.status.pending';

			--check if the sale invoice has a credit payment condition, to make a cash receipt document
			--indicating the payment amount
			if vsale_invoice_record.payment_condition = 'application.common.payment.condition.credit' then
				v_cash_receipt_document_identifier_number := vsale_invoice_record.identifier_number||'-'||trim(to_char(vsale_invoice_payment_record.payment_number,'99'));
				insert into pms_cash_receipt_document
				(id,	id_sale_invoice_payment,	id_currency,	id_person,
				identifier_number,	bussines_name,	bussines_ci_ruc,
				amount,	creation_user)
				values
				(nextval('pms_cash_receipt_document_id_sq'),vsale_invoice_payment_record.id, vsale_invoice_payment_record.id_currency, vsale_invoice_record.id_person,
				v_cash_receipt_document_identifier_number, vsale_invoice_record.bussines_name,vsale_invoice_record.bussines_ci_ruc,
				vsale_invoice_payment_record.amount, plast_modif_user);
			end if;
			
			if vpending_sale_invoice_payment_count = 0 then
				update pms_sale_invoice set status = 'application.common.status.canceled',
				cancellation_date = now(),last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vsale_invoice_record.id;

				update pms_sale_invoice_item set status = 'application.common.status.canceled',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id_sale_invoice = vsale_invoice_record.id
				and status != 'application.common.status.discarded';					
			else/* if this was not the las payment of the invoice, update the next payment status */
				update pms_sale_invoice_payment
					set status = 'application.common.status.in.progress',
					last_modif_user = plast_modif_user,last_modif_date = now()
					where 
					id_sale_invoice = vsale_invoice_record.id
					and status = 'application.common.status.pending'
					and payment_number = (vsale_invoice_payment_record.payment_number + 1);
				/* update the sale invoice to the application.common.status.partial.balance */
				update pms_sale_invoice set status = 'application.common.status.partial.balance',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id = vsale_invoice_record.id;

				update pms_sale_invoice_item set status = 'application.common.status.partial.balance',
				last_modif_user = plast_modif_user,last_modif_date = now()
				where id_sale_invoice = vsale_invoice_record.id
				and status != 'application.common.status.discarded';
			end if;--if vpending_sale_invoice_payment_count = 0 then			
		end if;--if pid_credit_note is not null then
		
		--update the order to the pre production status IF NOT ALREADY
		select ord.* into vorder_record from pms_order ord where ord.id = vsale_invoice_record.id_order;
		if vorder_record.status = 'application.common.status.invoiced' then
			update pms_order
				set status = 'application.common.status.pre.production',
				last_modif_user = plast_modif_user, last_modif_date = now()
			where id = vorder_record.id;

			update pms_order_item
				set status = 'application.common.status.invoiced', 
				last_modif_user = plast_modif_user, last_modif_date = now()
				where id_order = vorder_record.id
				and status != 'application.common.status.discarded';
		end if;--if vorder_record.status = 'application.common.status.invoiced' then
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
