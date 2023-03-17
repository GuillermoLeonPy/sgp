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

create or replace function p_effectuate_purchase_invoice_payment_with_credit_note
(
pid_purchase_invoice_payment					bigint,
pcash_receipt_document_identifier_number		varchar,
pcash_receipt_document_emission_date			timestamp,
poverdue_amount							numeric,
pid_purchase_invoice_credit_note				bigint,
ppms_implementing_enterprise_ruc				varchar,--to find the id, bussines name from pms_person record
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
		vperson_record								record;
		vcash_receipt_document_amount					pms_cash_receipt_document.amount%type;
		vpurchase_invoice_credit_note_record			record;
		vpurchase_invoice_credit_note_next_status		pms_purchase_invoice_credit_note.status%type;
		vpurchase_invoice_credit_note_next_balance		pms_purchase_invoice_credit_note.credit_note_balance%type;
		vpurchase_invoice_credit_note_current_balance	pms_purchase_invoice_credit_note.credit_note_balance%type;
	begin
		select * into vpurchase_invoice_payment_record from pms_purchase_invoice_payment
		where id = pid_purchase_invoice_payment;
		--CHECK THE PAYMENT STATUS: must be: 'application.common.status.in.progress'
		--another user could have effectuate the payment 
		--in the period in wich you open then modal credit note seleccion window !		
		if vpurchase_invoice_payment_record.status != 'application.common.status.in.progress' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.effectuate.purchase.invoice.payment.purchase.invoice.payment.status.invalid.error'||''||vpurchase_invoice_payment_record.payment_number||''||'#-key-#'||vpurchase_invoice_payment_record.status||''||'#-key-#'||'application.common.status.in.progress'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vpurchase_invoice_payment_record.status != 'application.common.status.in.progress' then	
		
		if pid_purchase_invoice_credit_note is not null then
			select * into vpurchase_invoice_credit_note_record
			from pms_purchase_invoice_credit_note where id = pid_purchase_invoice_credit_note;
			--MUST check credit note status !
			if vpurchase_invoice_credit_note_record.status = 'application.common.status.canceled' then
				declare
					b1_error_message		text;
					b1_error_message_hint	text;
				begin
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.effectuate.purchase.invoice.payment.purchase.invoice.credit.note.status.invalid.error'||''||vpurchase_invoice_credit_note_record.identifier_number||''||'#-key-#'||'application.common.status.canceled'||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
				end;
			end if;--if vpurchase_invoice_credit_note_record.status = 'application.common.status.canceled' then
			--check credit note currency same as payment
			if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
				vpurchase_invoice_credit_note_current_balance := 
					f_convert_currency_amount(
						vpurchase_invoice_credit_note_record.id_currency,
						vpurchase_invoice_payment_record.id_currency,
						vpurchase_invoice_credit_note_record.credit_note_balance);
			else
				vpurchase_invoice_credit_note_current_balance := vpurchase_invoice_credit_note_record.credit_note_balance;
			end if;
			--if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
		end if;--if pid_purchase_invoice_credit_note is not null then
	

		if poverdue_amount is not null and poverdue_amount < vpurchase_invoice_payment_record.amount then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.effectuate.purchase.invoice.payment.overdue.amount.less.than.purchase.invoice.payment.amount.error'||''||'#-numeric-#'||poverdue_amount||''||'#-numeric-#'||vpurchase_invoice_payment_record.amount||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if poverdue_amount is not null and poverdue_amount < vpurchase_invoice_payment_record.amount then
		

		if poverdue_amount is null and pid_purchase_invoice_credit_note is null then
			--normal(supoused safe date) payment
			vcash_receipt_document_amount := vpurchase_invoice_payment_record.amount;
			--check cash receipt document emmision date > purchase invoice emission date
			select * into vperson_record from pms_person where ruc = upper(trim(ppms_implementing_enterprise_ruc));
			PERFORM p_i_cash_receipt_document_by_purchase_invoice_payment
				(vperson_record.id,
				vpurchase_invoice_payment_record.id_purchase_invoice,
				vpurchase_invoice_payment_record.id,
				pcash_receipt_document_identifier_number,
				vcash_receipt_document_amount,
				pcash_receipt_document_emission_date,
				'N',--possible values: 'S':when overdue amount payment excecution; or 'N'
				'S',--possible values: 'S':when NOT overdue amount payment excecution; or 'N'
				plast_modif_user);
			
			PERFORM p_execute_purchase_invoice_payment(
					vpurchase_invoice_payment_record.id,
					(pcash_receipt_document_emission_date)::timestamp,
					poverdue_amount,
					plast_modif_user);
			
		elsif poverdue_amount is null and pid_purchase_invoice_credit_note is not null then
				--normal(supoused safe date) payment with credit note

				if vpurchase_invoice_credit_note_current_balance >= vpurchase_invoice_payment_record.amount then
					--no need of cash receipt document
					vpurchase_invoice_credit_note_next_balance := vpurchase_invoice_credit_note_current_balance - vpurchase_invoice_payment_record.amount;					
					if vpurchase_invoice_credit_note_next_balance = 0::numeric then
						vpurchase_invoice_credit_note_next_status := 'application.common.status.canceled';
					else
						vpurchase_invoice_credit_note_next_status := 'application.common.status.partial.balance';
					end if;

					PERFORM p_i_purchase_invoice_payment_cancel_documents
							(vpurchase_invoice_credit_note_record.id,
							vpurchase_invoice_payment_record.id,
							0::numeric,
							plast_modif_user);
					
				else--if vpurchase_invoice_credit_note_current_balance >= vpurchase_invoice_payment_record.amount then
				
					--in this case vpurchase_invoice_credit_note_current_balance < vpurchase_invoice_payment_record.amount
					vpurchase_invoice_credit_note_next_status := 'application.common.status.canceled';
					vpurchase_invoice_credit_note_next_balance := 0;					
					--check cash receipt document emmision date > purchase invoice emission date					
					vcash_receipt_document_amount := vpurchase_invoice_payment_record.amount - vpurchase_invoice_credit_note_current_balance;

					PERFORM p_i_purchase_invoice_payment_cancel_documents
							(vpurchase_invoice_credit_note_record.id,
							vpurchase_invoice_payment_record.id,
							vcash_receipt_document_amount,
							plast_modif_user);
					
					select * into vperson_record from pms_person where ruc = upper(trim(ppms_implementing_enterprise_ruc));
					PERFORM p_i_cash_receipt_document_by_purchase_invoice_payment
						(vperson_record.id,
						vpurchase_invoice_payment_record.id_purchase_invoice,
						vpurchase_invoice_payment_record.id,
						pcash_receipt_document_identifier_number,
						vcash_receipt_document_amount,
						pcash_receipt_document_emission_date,
						'N',--possible values: 'S':when overdue amount payment excecution; or 'N'
						'S',--possible values: 'S':when NOT overdue amount payment excecution; or 'N'
						plast_modif_user);								
				end if;
				--if vpurchase_invoice_credit_note_current_balance >= vpurchase_invoice_payment_record.amount then

				PERFORM p_execute_purchase_invoice_payment(
						vpurchase_invoice_payment_record.id,
						(now())::timestamp,
						poverdue_amount,
						plast_modif_user);

				if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
					update pms_purchase_invoice_credit_note
						set 
							status = vpurchase_invoice_credit_note_next_status,
							credit_note_balance = f_convert_currency_amount(
												vpurchase_invoice_payment_record.id_currency,
												vpurchase_invoice_credit_note_record.id_currency,												
												vpurchase_invoice_credit_note_next_balance),
							last_modif_user = plast_modif_user,
							last_modif_date = now()
					where id = vpurchase_invoice_credit_note_record.id;
				else--if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
					update pms_purchase_invoice_credit_note
						set 
							status = vpurchase_invoice_credit_note_next_status,
							credit_note_balance = vpurchase_invoice_credit_note_next_balance,
							last_modif_user = plast_modif_user,
							last_modif_date = now()
					where id = vpurchase_invoice_credit_note_record.id;
				end if;--if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
					
		elsif poverdue_amount is not null and pid_purchase_invoice_credit_note is not null then
			--overdued payment with credit note
			if vpurchase_invoice_credit_note_current_balance >= poverdue_amount then
				--no need of cash receipt document				
				vpurchase_invoice_credit_note_next_balance := vpurchase_invoice_credit_note_current_balance - poverdue_amount;
				if vpurchase_invoice_credit_note_next_balance = 0::numeric then
					vpurchase_invoice_credit_note_next_status := 'application.common.status.canceled';
				else
					vpurchase_invoice_credit_note_next_status := 'application.common.status.partial.balance';
				end if;

				PERFORM p_i_purchase_invoice_payment_cancel_documents
						(vpurchase_invoice_credit_note_record.id,
						vpurchase_invoice_payment_record.id,
						0::numeric,
						plast_modif_user);
			else--if vpurchase_invoice_credit_note_current_balance >= poverdue_amount then
	
				--in this case vpurchase_invoice_credit_note_current_balance < poverdue_amount				
				--CHECK pcash_receipt_document_emission_date >= vpurchase_invoice_payment_record.payment_due_date
				--because THERE IS AN OVERDUE AMOUNT					
				vpurchase_invoice_credit_note_next_status := 'application.common.status.canceled';
				vpurchase_invoice_credit_note_next_balance := 0;

				vcash_receipt_document_amount := poverdue_amount - vpurchase_invoice_credit_note_current_balance;

				PERFORM p_i_purchase_invoice_payment_cancel_documents
						(vpurchase_invoice_credit_note_record.id,
						vpurchase_invoice_payment_record.id,
						vcash_receipt_document_amount,
						plast_modif_user);
				
				select * into vperson_record from pms_person where ruc = upper(trim(ppms_implementing_enterprise_ruc));
				
				PERFORM p_i_cash_receipt_document_by_purchase_invoice_payment
					(vperson_record.id,
					vpurchase_invoice_payment_record.id_purchase_invoice,
					vpurchase_invoice_payment_record.id,
					pcash_receipt_document_identifier_number,
					vcash_receipt_document_amount,
					pcash_receipt_document_emission_date,
					'S',--possible values: 'S':when overdue amount payment excecution; or 'N'
					'N',--possible values: 'S':when NOT overdue amount payment excecution; or 'N'
					plast_modif_user);
			end if;
			--if vpurchase_invoice_credit_note_record.credit_note_balance >= poverdue_amount then

			PERFORM p_execute_purchase_invoice_payment(
					vpurchase_invoice_payment_record.id,
					(now())::timestamp,
					poverdue_amount,
					plast_modif_user);				


			if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
				update pms_purchase_invoice_credit_note
				set 
					status = vpurchase_invoice_credit_note_next_status,
					credit_note_balance = f_convert_currency_amount(
											vpurchase_invoice_payment_record.id_currency,
											vpurchase_invoice_credit_note_record.id_currency,												
											vpurchase_invoice_credit_note_next_balance),
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where 
					id = vpurchase_invoice_credit_note_record.id;
			else--if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
				update pms_purchase_invoice_credit_note
				set 
					status = vpurchase_invoice_credit_note_next_status,
					credit_note_balance = vpurchase_invoice_credit_note_next_balance,
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where 
					id = vpurchase_invoice_credit_note_record.id;
			end if;--if vpurchase_invoice_credit_note_record.id_currency != vpurchase_invoice_payment_record.id_currency then
		elsif poverdue_amount is not null and pid_purchase_invoice_credit_note is null then
			--overdued payment with out credit note
			--pcash_receipt_document_identifier_number can not be null
			--pcash_receipt_document_emission_date can not be null
			--CHECK pcash_receipt_document_emission_date >= vpurchase_invoice_payment_record.payment_due_date			
			--because THERE IS AN OVERDUE AMOUNT
			select * into vperson_record from pms_person where ruc = upper(trim(ppms_implementing_enterprise_ruc));
			vcash_receipt_document_amount := poverdue_amount;
			PERFORM p_i_cash_receipt_document_by_purchase_invoice_payment
				(vperson_record.id,
				vpurchase_invoice_payment_record.id_purchase_invoice,
				vpurchase_invoice_payment_record.id,
				pcash_receipt_document_identifier_number,
				vcash_receipt_document_amount,
				pcash_receipt_document_emission_date,
				'S',--possible values: 'S':when overdue amount payment excecution; or 'N'
				'N',--possible values: 'S':when NOT overdue amount payment excecution; or 'N'
				plast_modif_user);
			
			PERFORM p_execute_purchase_invoice_payment(
					vpurchase_invoice_payment_record.id,
					(pcash_receipt_document_emission_date)::timestamp,
					poverdue_amount,
					plast_modif_user);
		end if;
		--if poverdue_amount is null and pid_purchase_invoice_credit_note is null then			
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


create or replace function p_i_cash_receipt_document_by_purchase_invoice_payment
(
pid_person										bigint,
pid_purchase_invoice								bigint,
pid_purchase_invoice_payment							bigint,
pcash_receipt_document_identifier_number				varchar,
pcash_receipt_document_amount							numeric,
pcash_receipt_document_emission_date					timestamp,
pcheck_emission_date_after_payment_due_date				varchar,--possible values: 'S':when overdue amount payment excecution; or 'N'
pcheck_emission_date_after_purchase_invoice_emision_date	varchar,--possible values: 'S':when NOT overdue amount payment excecution; or 'N'
pcreation_user 		   							varchar
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
	RAISE INFO 'executing p_i_cash_receipt_document_by_purchase_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vperson_record							record;
		vpurchase_invoice_payment_record			record;
		vcash_receipt_document_emission_date		timestamp;
		vpurchase_invoice_record					record;
	begin
		select * into vperson_record from pms_person where id = pid_person;
		select * into vpurchase_invoice_payment_record from pms_purchase_invoice_payment where id = pid_purchase_invoice_payment;
		select * into vpurchase_invoice_record from pms_purchase_invoice where id = pid_purchase_invoice;

		if pcash_receipt_document_identifier_number is null or length(trim(pcash_receipt_document_identifier_number))=0  then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.p.i.cash.receipt.document.by.purchase.invoice.payment.identifier.number.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		--pcash_receipt_document_emission_date can not be null
		if pcash_receipt_document_emission_date is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.p.i.cash.receipt.document.by.purchase.invoice.payment.emission.date.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		vcash_receipt_document_emission_date := (date_trunc('day', pcash_receipt_document_emission_date)::timestamp);

		--check cash receipt document emmision date > purchase invoice emission date					
		if pcheck_emission_date_after_purchase_invoice_emision_date = 'S'
		and vcash_receipt_document_emission_date < vpurchase_invoice_record.emission_date then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.p.i.cash.receipt.document.by.purchase.invoice.payment.cash.receipt.document.emission.date.prior.to.purchase.invoice.emission.date.error'||''||'#-date-#'||to_char(pcash_receipt_document_emission_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vpurchase_invoice_record.emission_date,'DD/MM/YYYY HH24:MI:SS')||''||vpurchase_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		
		if pcheck_emission_date_after_payment_due_date = 'S' 
		and vcash_receipt_document_emission_date < vpurchase_invoice_payment_record.payment_due_date then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.p.i.cash.receipt.document.by.purchase.invoice.payment.cash.receipt.document.emission.date.prior.to.payment.due.date.error'||''||'#-date-#'||to_char(vcash_receipt_document_emission_date,'DD/MM/YYYY HH24:MI:SS')||''||'#-date-#'||to_char(vpurchase_invoice_payment_record.payment_due_date,'DD/MM/YYYY HH24:MI:SS')||''||vpurchase_invoice_payment_record.payment_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
					
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
			pcash_receipt_document_amount,
			pcreation_user,
			vpurchase_invoice_payment_record.id,
			vcash_receipt_document_emission_date);
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



create or replace function p_i_purchase_invoice_payment_cancel_documents
(
pid_purchase_invoice_credit_note						bigint,
pid_purchase_invoice_payment							bigint,
presulting_payment_amount							numeric,
pcreation_user 		   							varchar
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
	RAISE INFO 'executing p_i_purchase_invoice_payment_cancel_documents';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	insert into pms_purchase_invoice_payment_cancel_documents
		(id,
		id_purchase_invoice_payment,
		id_purchase_invoice_credit_note,
		resulting_payment_amount,
		creation_user)
	values
		(nextval('pms_purchase_invoice_payment_cancel_documents_id_sq'),
		pid_purchase_invoice_payment,
		pid_purchase_invoice_credit_note,
		presulting_payment_amount,
		pcreation_user);
	
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





create or replace function p_execute_purchase_invoice_payment
(
pid_purchase_invoice_payment				bigint,
pcancellation_date						timestamp,
poverdue_amount						numeric,
plast_modif_user 		   				varchar
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
	RAISE INFO 'executing p_execute_purchase_invoice_payment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vpurchase_invoice_payment_record			record;
		vpurchase_invoice_record					record;
		vpending_purchase_invoice_payment_record	record;
	begin
		select * into vpurchase_invoice_payment_record from pms_purchase_invoice_payment where id = pid_purchase_invoice_payment;
		select * into vpurchase_invoice_record from pms_purchase_invoice where id = vpurchase_invoice_payment_record.id_purchase_invoice;
		update pms_purchase_invoice_payment 
		set 
			status = 'application.common.status.canceled',
			cancellation_date = pcancellation_date,
			last_modif_user = plast_modif_user,
			last_modif_date = now(),
			overdue_amount = poverdue_amount
		where 
			id = vpurchase_invoice_payment_record.id;

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