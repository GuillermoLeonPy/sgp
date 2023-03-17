/*84-UC-REG-SDP-201611141831*/

create table pms_purchase_invoice(
id										bigint,
id_person									bigint NOT NULL,
id_currency								bigint NOT NULL,
payment_condition							varchar(50) not null,
status									varchar(50) not null default 'application.common.status.pending',
registration_date							timestamp not null default now(),
emission_date								timestamp,
cancellation_date							timestamp,
annulment_date								timestamp,
annulment_reason_description					varchar(500),
identifier_number							varchar(15) NOT NULL,
stamping_number							numeric(8,0) not null,
stamping_number_start_validity_date			timestamp,
stamping_number_end_validity_date				timestamp,
branch_office_address						varchar(500),
branch_office_telephone_nbr					varchar(500),
total_amount								numeric(11,2) NOT NULL, -- GRAVA IVA 10% + GRAVA IVA 5% + EXCENTO
value_added_amount							numeric(11,2) NOT NULL, --GRAVA IVA 10% + GRAVA IVA 5%
total_tax_amount							numeric(11,2) NOT NULL, --TOTAL IVA: IVA 10% + IVA 5%
exempt_amount								numeric(11,2) NOT NULL, --EXCENTO
value_added_tax_10_amount					numeric(11,2) NOT NULL, --GRAVA IVA 10%
value_added_tax_5_amount						numeric(11,2) NOT NULL, --GRAVA IVA 5%
tax_10_amount								numeric(11,2) NOT NULL, --IVA 10%
tax_5_amount								numeric(11,2) NOT NULL, --IVA 5%
bussines_name								varchar(500) not null,
bussines_ci_ruc							varchar(50) not null,
credit_purchase_fee_quantity					bigint,
credit_purchase_fee_periodicity_days_quantity	bigint,
credit_purchase_first_payment_fee_date			timestamp,
creation_user								varchar(50),
creation_date								timestamp NOT NULL default now(),
last_modif_user							varchar(50),
last_modif_date							timestamp);

--this field does not apply to purchase invoices because this value can be established at the 
--item level 
alter table pms_purchase_invoice drop credit_payment_condition_surcharge_percentage;

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_credit_purchase_fee_quantity CHECK (credit_purchase_fee_quantity >= 2);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_credit_purchase_fee_periodicity_days_quantity 
CHECK (credit_purchase_fee_periodicity_days_quantity >= 15);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT
pms_purchase_invoice_uk_01 UNIQUE (stamping_number,identifier_number);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT pms_purchase_invoice_id_pk PRIMARY KEY (id);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT chk_cancellation_date CHECK (cancellation_date > emission_date);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT chk_annulment_date CHECK (annulment_date > emission_date);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT chk_credit_purchase_first_payment_fee_date
CHECK (credit_purchase_first_payment_fee_date >= emission_date);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_value_added_tax_10_amount CHECK (value_added_tax_10_amount >= tax_10_amount);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_value_added_tax_10_amount_02 CHECK (value_added_tax_10_amount >= 0);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_value_added_tax_5_amount CHECK (value_added_tax_5_amount >= tax_5_amount);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_value_added_tax_5_amount_02 CHECK (value_added_tax_5_amount >= 0);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT chk_exempt_amount CHECK (exempt_amount >= 0);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT chk_total_amount CHECK (total_amount >= value_added_amount);

ALTER TABLE pms_purchase_invoice ADD CONSTRAINT 
chk_value_added_amount CHECK (value_added_amount > total_tax_amount);

ALTER TABLE pms_purchase_invoice ADD constraint pms_purchase_invoice_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_purchase_invoice ADD constraint pms_purchase_invoice_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

create sequence pms_purchase_invoice_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice.id;


create table pms_purchase_invoice_item(
id	   		 		   			bigint,
id_purchase_invoice		   			bigint not null,
id_raw_material					bigint not null,
id_measurment_unit					bigint not null,
quantity		 					numeric(11,2) NOT NULL,
physical_quantity_in_stock			numeric(11,2) NOT NULL,
unit_price_amount	   				numeric(11,2) NOT NULL,
exempt_amount 			numeric(11,2) NOT NULL,
value_added_tax_10_amount			numeric(11,2) NOT NULL,
value_added_tax_5_amount		numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);

--ALTER TABLE pms_purchase_invoice_item ADD physical_quantity_in_stock			numeric(11,2);
--update pms_purchase_invoice_item set physical_quantity_in_stock = quantity;
--ALTER TABLE pms_purchase_invoice_item alter column physical_quantity_in_stock set not null;

--ALTER TABLE pms_purchase_invoice_item alter column quantity type numeric(11,2);
--ALTER TABLE pms_purchase_invoice_item alter column physical_quantity_in_stock type numeric(11,2);

ALTER TABLE pms_purchase_invoice_item ADD constraint chk_physical_quantity_in_stock
CHECK (physical_quantity_in_stock >= 0);

ALTER TABLE pms_purchase_invoice_item ADD constraint chk_physical_quantity_in_stock_02
CHECK (physical_quantity_in_stock <= quantity);

ALTER TABLE pms_purchase_invoice_item ADD constraint chk_quantity CHECK (quantity > 0);
ALTER TABLE pms_purchase_invoice_item ADD constraint chk_unit_price_amount CHECK  (unit_price_amount > 0);
ALTER TABLE pms_purchase_invoice_item ADD constraint chk_exempt_amount_01 
CHECK  (exempt_amount >= 0);

ALTER TABLE pms_purchase_invoice_item ADD constraint 
chk_exempt_amount_02 CHECK  (exempt_amount <= unit_price_amount);

ALTER TABLE pms_purchase_invoice_item ADD constraint 
chk_value_added_tax_10_amount_01 CHECK  (value_added_tax_10_amount >= 0);

ALTER TABLE pms_purchase_invoice_item ADD constraint 
chk_value_added_tax_5_amount_01 CHECK  (value_added_tax_5_amount >= 0);

ALTER TABLE pms_purchase_invoice_item ADD CONSTRAINT pms_purchase_invoice_item_id_pk PRIMARY KEY (id);

ALTER TABLE pms_purchase_invoice_item 
ADD constraint pms_purchase_invoice_item_fk_01
FOREIGN KEY (id_purchase_invoice) REFERENCES pms_purchase_invoice (id);

ALTER TABLE pms_purchase_invoice_item 
ADD constraint pms_purchase_invoice_item_fk_02
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_purchase_invoice_item 
ADD constraint pms_purchase_invoice_item_fk_03
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);


ALTER TABLE pms_purchase_invoice_item ADD CONSTRAINT
pms_purchase_invoice_item_uk_01 UNIQUE (id_purchase_invoice,id_raw_material,id_measurment_unit);

create sequence pms_purchase_invoice_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_item.id;


create or replace function p_i_pms_purchase_invoice
(
pid	   		 		   					bigint,
pid_person	 		   					bigint,
pid_currency	 							bigint,
ppayment_condition							varchar,
pemission_date								timestamp,
pidentifier_number   						varchar,
pstamping_number							numeric,
pstamping_number_start_validity_date			timestamp,
pbranch_office_address						varchar,
pbranch_office_telephone_nbr					varchar,
pexempt_amount 							numeric,
pvalue_added_tax_10_amount					numeric,
pvalue_added_tax_5_amount					numeric,
pcredit_purchase_fee_quantity 						bigint,
pcredit_purchase_fee_periodicity_days_quantity 			bigint,
pcredit_purchase_first_payment_fee_date			timestamp,
pbussines_name								varchar,
pbussines_ci_ruc							varchar,
pcreation_user	 		   							varchar
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
	RAISE INFO 'executing p_i_pms_purchase_invoice';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	declare
		vpurchase_invoice_record		record;
		vtotal_amount			pms_purchase_invoice.total_amount%type;
		vvalue_added_amount		pms_purchase_invoice.value_added_amount%type;
		vtax_10_amount 		pms_purchase_invoice.tax_10_amount%type;
		vtax_5_amount 			pms_purchase_invoice.tax_5_amount%type;
		vtotal_tax_amount 		pms_purchase_invoice.total_tax_amount%type;
		vstamping_number_end_validity_date				pms_purchase_invoice.stamping_number_end_validity_date%type;
		vstamping_number_start_validity_date			pms_purchase_invoice.stamping_number_start_validity_date%type;
	begin

		if (pcredit_purchase_fee_quantity is not null and pcredit_purchase_fee_quantity < 2)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.fee.quantity.invalid.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
	
		if (pcredit_purchase_fee_periodicity_days_quantity is not null and pcredit_purchase_fee_periodicity_days_quantity < 15)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.fee.periodicity.days.quantity.invalid.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		if pcredit_purchase_fee_quantity is not null 
		and pcredit_purchase_fee_periodicity_days_quantity is not null 
		and pcredit_purchase_first_payment_fee_date is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.first.payment.fee.date.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		select * into vpurchase_invoice_record from pms_purchase_invoice where identifier_number = upper(trim(pidentifier_number));
		if vpurchase_invoice_record.id is not null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.purchase.invoice.identifier.number.already.exists.error'||''||upper(trim(pidentifier_number))||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		
		vvalue_added_amount := pvalue_added_tax_10_amount + pvalue_added_tax_5_amount;
		vtotal_amount :=  vvalue_added_amount + pexempt_amount;
		vtax_10_amount := pvalue_added_tax_10_amount * 0.090909;
		vtax_5_amount := pvalue_added_tax_5_amount * 0.047619;
		vtotal_tax_amount := vtax_10_amount + vtax_5_amount;

		vstamping_number_start_validity_date := date_trunc('day', pstamping_number_start_validity_date);
		vstamping_number_end_validity_date := (
							date_trunc('day',(date_trunc('month', (pstamping_number_start_validity_date + interval '1 year')) + 
							interval '1 month' - interval '1 day')::timestamp)
							+ interval '23:59:59');
	
		insert into pms_purchase_invoice
				(id,
				id_person,
				id_currency,
				payment_condition,
				emission_date,
				identifier_number,
				stamping_number,
				stamping_number_start_validity_date,
				stamping_number_end_validity_date,
				branch_office_address,
				branch_office_telephone_nbr,
				total_amount,
				value_added_amount,
				exempt_amount,
				value_added_tax_10_amount,
				value_added_tax_5_amount,
				tax_10_amount,
				tax_5_amount,
				total_tax_amount,
				credit_purchase_fee_quantity,
				credit_purchase_fee_periodicity_days_quantity,
				credit_purchase_first_payment_fee_date,	
				bussines_name,
				bussines_ci_ruc,
				creation_user)
		values
				(pid,
				pid_person,
				pid_currency,
				ppayment_condition,
				pemission_date,
				upper(trim(pidentifier_number)),
				pstamping_number,
				vstamping_number_start_validity_date,
				vstamping_number_end_validity_date,
				pbranch_office_address,
				pbranch_office_telephone_nbr,
				vtotal_amount,
				vvalue_added_amount,
				pexempt_amount,
				pvalue_added_tax_10_amount,
				pvalue_added_tax_5_amount,
				vtax_10_amount,
				vtax_5_amount,
				vtotal_tax_amount,
				pcredit_purchase_fee_quantity,
				pcredit_purchase_fee_periodicity_days_quantity,
				pcredit_purchase_first_payment_fee_date,
				pbussines_name,
				pbussines_ci_ruc,
				pcreation_user);
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




create or replace function p_u_pms_purchase_invoice
(
pid	   		 		   					bigint,
pid_person	 		   					bigint,
pid_currency	 							bigint,
ppayment_condition							varchar,
pstatus									varchar,
pemission_date								timestamp,
pidentifier_number   						varchar,
pstamping_number							numeric,
pstamping_number_start_validity_date			timestamp,
pbranch_office_address						varchar,
pbranch_office_telephone_nbr					varchar,
pexempt_amount 							numeric,
pvalue_added_tax_10_amount					numeric,
pvalue_added_tax_5_amount					numeric,
pcredit_purchase_fee_quantity 						bigint,
pcredit_purchase_fee_periodicity_days_quantity 			bigint,
pcredit_purchase_first_payment_fee_date			timestamp,
pbussines_name								varchar,
pbussines_ci_ruc							varchar,
plast_modif_user	 		   							varchar
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
	RAISE INFO 'executing p_u_pms_purchase_invoice';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	declare
		vpurchase_invoice_record			record;
		vtotal_amount			pms_purchase_invoice.total_amount%type;
		vvalue_added_amount		pms_purchase_invoice.value_added_amount%type;
		vtax_10_amount 		pms_purchase_invoice.tax_10_amount%type;
		vtax_5_amount 			pms_purchase_invoice.tax_5_amount%type;
		vtotal_tax_amount 		pms_purchase_invoice.total_tax_amount%type;
		vstamping_number_end_validity_date				pms_purchase_invoice.stamping_number_end_validity_date%type;
		vstamping_number_start_validity_date			pms_purchase_invoice.stamping_number_start_validity_date%type;
		vstatus				pms_purchase_invoice.status%type;
		vpurchase_invoice_balance		pms_purchase_invoice_payment.purchase_invoice_balance%type;
		vpayment_due_date		pms_purchase_invoice_payment.payment_due_date%type;
	begin
		select * into vpurchase_invoice_record from pms_purchase_invoice where identifier_number = upper(trim(pidentifier_number));
		if vpurchase_invoice_record.id is not null and vpurchase_invoice_record.id != pid then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.purchase.invoice.identifier.number.already.exists.error'||''||upper(trim(pidentifier_number))||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		if (pcredit_purchase_fee_quantity is not null and pcredit_purchase_fee_quantity < 2)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.fee.quantity.invalid.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if (pcredit_purchase_fee_periodicity_days_quantity is not null and pcredit_purchase_fee_periodicity_days_quantity < 15)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.fee.periodicity.days.quantity.invalid.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		if pcredit_purchase_fee_quantity is not null 
		and pcredit_purchase_fee_periodicity_days_quantity is not null 
		and pcredit_purchase_first_payment_fee_date is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.dto.credit.purchase.first.payment.fee.date.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		vvalue_added_amount := pvalue_added_tax_10_amount + pvalue_added_tax_5_amount;
		vtotal_amount :=  vvalue_added_amount + pexempt_amount;
		vtax_10_amount := pvalue_added_tax_10_amount * 0.090909;
		vtax_5_amount := pvalue_added_tax_5_amount * 0.047619;
		vtotal_tax_amount := vtax_10_amount + vtax_5_amount;
		
		vstamping_number_start_validity_date := date_trunc('day', pstamping_number_start_validity_date);
		vstamping_number_end_validity_date := (
							date_trunc('day',(date_trunc('month', (pstamping_number_start_validity_date + interval '1 year')) + 
							interval '1 month' - interval '1 day')::timestamp)
							+ interval '23:59:59');
		
		
		if pstatus = 'application.common.status.confirmed' 
		and ppayment_condition = 'application.common.payment.condition.cash' then
		--there is no further calculations to do with the invoice
			vstatus := 'application.common.status.canceled';
			
		elsif pstatus = 'application.common.status.confirmed' 
		and ppayment_condition = 'application.common.payment.condition.credit' then
			vstatus := 'application.common.status.payment.in.progress';
			
		else
			vstatus := pstatus;
		end if;--if pstatus = 'application.common.status.confirmed' then

		update pms_purchase_invoice
		set
				id_person 							= pid_person,
				id_currency							= pid_currency,
				payment_condition						= ppayment_condition,
				status								= vstatus,
				emission_date							= pemission_date,
				identifier_number						= pidentifier_number,
				stamping_number						= pstamping_number,
				stamping_number_start_validity_date		= vstamping_number_start_validity_date,
				stamping_number_end_validity_date			= vstamping_number_end_validity_date,
				branch_office_address					= pbranch_office_address,
				branch_office_telephone_nbr				= pbranch_office_telephone_nbr,
				total_amount							= vtotal_amount,
				value_added_amount						= vvalue_added_amount,
				exempt_amount							= pexempt_amount,
				value_added_tax_10_amount				= pvalue_added_tax_10_amount,
				value_added_tax_5_amount					= pvalue_added_tax_5_amount,
				tax_10_amount							= vtax_10_amount,
				tax_5_amount							= vtax_5_amount,
				total_tax_amount						= vtotal_tax_amount,
				credit_purchase_fee_quantity				= pcredit_purchase_fee_quantity,
				credit_purchase_fee_periodicity_days_quantity = pcredit_purchase_fee_periodicity_days_quantity,
				credit_purchase_first_payment_fee_date		= pcredit_purchase_first_payment_fee_date,
				bussines_name							= pbussines_name,
				bussines_ci_ruc						= pbussines_ci_ruc,
				last_modif_user								= plast_modif_user,
				last_modif_date								= now()
			where
				id = pid;

			if pstatus = 'application.common.status.confirmed' then
				PERFORM p_update_raw_material_existence_by_purchase_invoice(pid,plast_modif_user);				
			end if;--if pstatus = 'application.common.status.confirmed' then

			if pstatus = 'application.common.status.confirmed' 
			and ppayment_condition = 'application.common.payment.condition.credit' then
				--create purchase invoice payments records
				for vcounter in 1..pcredit_purchase_fee_quantity loop									
					if vcounter = 1 then
						vpurchase_invoice_balance := (vtotal_amount - trunc((vtotal_amount / pcredit_purchase_fee_quantity),2));	
						vpayment_due_date := date_trunc('day',pcredit_purchase_first_payment_fee_date) + interval '23:59:59';
						insert into pms_purchase_invoice_payment
						(id,
						id_purchase_invoice,
						id_currency,
						status,
						amount,
						purchase_invoice_balance,
						payment_number,
						payment_due_date,
						creation_user)
						values
						(nextval('pms_purchase_invoice_payment_id_sq'),
						pid,
						pid_currency,
						'application.common.status.in.progress',
						trunc((vtotal_amount / pcredit_purchase_fee_quantity),2),
						vpurchase_invoice_balance,
						vcounter,
						vpayment_due_date,
						plast_modif_user);
					else
						vpurchase_invoice_balance := (vpurchase_invoice_balance - trunc((vtotal_amount / pcredit_purchase_fee_quantity),2));
						vpayment_due_date := f_determinate_due_date((vpayment_due_date + (pcredit_purchase_fee_periodicity_days_quantity * interval '1 day'))::timestamp);
						insert into pms_purchase_invoice_payment
						(id,
						id_purchase_invoice,
						id_currency,
						amount,
						purchase_invoice_balance,
						payment_number,
						payment_due_date,
						creation_user)
						values
						(nextval('pms_purchase_invoice_payment_id_sq'),
						pid,
						pid_currency,
						trunc((vtotal_amount / pcredit_purchase_fee_quantity),2),
						vpurchase_invoice_balance,
						vcounter,
						vpayment_due_date,
						plast_modif_user);
					end if;--if vcounter = 1 then
				end loop;--for vcounter in 1..pcredit_purchase_fee_quantity loop
			end if;
			--if pstatus = 'application.common.status.confirmed' 
			--and ppayment_condition = 'application.common.payment.condition.credit' then
	end;--

	
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


create or replace function p_test_confirm_pms_purchase_invoice
(pid_purchase_invoice	bigint)
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
	RAISE INFO 'executing p_test_confirm_credit_pms_purchase_invoice';
	RAISE INFO '--------------------------';
	declare
		vrecord	record;
	begin
		select * into vrecord from pms_purchase_invoice where id = pid_purchase_invoice;

		PERFORM p_u_pms_purchase_invoice
					(vrecord.id,
					vrecord.id_person,
					vrecord.id_currency,
					vrecord.payment_condition,
					'application.common.status.confirmed',
					vrecord.emission_date,
					vrecord.identifier_number,
					vrecord.stamping_number,
					vrecord.stamping_number_start_validity_date,
					vrecord.branch_office_address,
					vrecord.branch_office_telephone_nbr,
					vrecord.exempt_amount,
					vrecord.value_added_tax_10_amount,
					vrecord.value_added_tax_5_amount,
					vrecord.credit_purchase_fee_quantity,
					vrecord.credit_purchase_fee_periodicity_days_quantity,
					vrecord.credit_purchase_first_payment_fee_date,
					vrecord.bussines_name,
					vrecord.bussines_ci_ruc,
					'xx'
					);
		
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



create or replace function p_i_pms_purchase_invoice_item
(
pid	   		 		   			bigint,
pid_purchase_invoice	 		   		bigint,
pid_raw_material	 				bigint,
pid_measurment_unit					bigint,
pquantity		 				numeric,
punit_price_amount	   						numeric,
pexempt_amount 						numeric,
pvalue_added_tax_10_amount			numeric,
pvalue_added_tax_5_amount				numeric,
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
	RAISE INFO 'executing p_i_pms_purchase_invoice_item';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	insert into pms_purchase_invoice_item
			(id,
			id_purchase_invoice,
			id_raw_material,
			id_measurment_unit,
			quantity,
			physical_quantity_in_stock,
			unit_price_amount,
			exempt_amount,
			value_added_tax_10_amount,
			value_added_tax_5_amount,
			creation_user)
	values
			(pid,
			pid_purchase_invoice,
			pid_raw_material,
			pid_measurment_unit,
			pquantity,
			pquantity,
			punit_price_amount,
			pexempt_amount,
			pvalue_added_tax_10_amount,
			pvalue_added_tax_5_amount,
			pcreation_user);
			
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create or replace function f_pms_purchase_invoice_item_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('pms_purchase_invoice_item_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';



create or replace function p_update_raw_material_existence_by_purchase_invoice
(
pid_purchase_invoice	   			bigint,
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
	RAISE INFO 'executing p_update_raw_material_existence_by_purchase_invoice';
	RAISE INFO '--------------------------';
	declare
		vpurchase_invoice_item_cursor	cursor for
		select * from pms_purchase_invoice_item where id_purchase_invoice = pid_purchase_invoice;
		vraw_material_existence_record			record;
	begin
		for vpurchase_invoice_item_record in vpurchase_invoice_item_cursor loop
			select * into vraw_material_existence_record
			from pms_raw_material_existence where 
			id_raw_material = vpurchase_invoice_item_record.id_raw_material
			and id_measurment_unit = vpurchase_invoice_item_record.id_measurment_unit;

			if vraw_material_existence_record.id is not null then
				update pms_raw_material_existence
				set 
					calculated_quantity = vraw_material_existence_record.calculated_quantity + vpurchase_invoice_item_record.quantity,
					efective_quantity = vraw_material_existence_record.efective_quantity + vpurchase_invoice_item_record.quantity,
					last_modif_user = plast_modif_user, last_modif_date = now()
				where id = vraw_material_existence_record.id;
			else--if vraw_material_existence_record.id is not null then
				insert into pms_raw_material_existence
				(id,
				id_raw_material,
				id_measurment_unit,
				calculated_quantity,
				limit_calculated_quantity,
				efective_quantity,
				automatic_purchase_request_quantity,
				creation_user)
				values
				(nextval('pms_raw_material_existence_id_sq'),
				vpurchase_invoice_item_record.id_raw_material,
				vpurchase_invoice_item_record.id_measurment_unit,
				vpurchase_invoice_item_record.quantity,
				0,
				vpurchase_invoice_item_record.quantity,
				0,
				plast_modif_user);
			end if;--if vraw_material_existence_record.id is not null then
		end loop;--for vpurchase_invoice_item_record in vpurchase_invoice_item_cursor loop
	end;--begin
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';


create table pms_purchase_invoice_payment(
id	   		 		   			bigint,
id_purchase_invoice		   			bigint NOT NULL,
id_currency	 				bigint NOT NULL,
status							varchar(50) not null default 'application.common.status.pending',
amount							numeric(11,2) NOT NULL,
purchase_invoice_balance				numeric(11,2) NOT NULL,--SALDO DE FACTURA
payment_number			   			bigint NOT NULL,
registration_date					timestamp not null default now(),
payment_due_date					timestamp not null,
cancellation_date   				timestamp,
annulment_date   					timestamp,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);


ALTER TABLE pms_purchase_invoice_payment ADD overdue_amount numeric(11,2);
ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT chk_overdue_amount CHECK (overdue_amount >= amount);
--SI SE PERDONA EL RETRASO
--ALTER TABLE pms_purchase_invoice_payment drop CONSTRAINT chk_overdue_amount

ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT pms_purchase_invoice_payment_id_pk PRIMARY KEY (id);


ALTER TABLE pms_purchase_invoice_payment 
ADD constraint pms_purchase_invoice_payment_fk_01
FOREIGN KEY (id_purchase_invoice) REFERENCES pms_purchase_invoice (id);


ALTER TABLE pms_purchase_invoice_payment 
ADD constraint pms_purchase_invoice_payment_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

/* LA FECHA DE CANCELACION ES LA FECHA DE EMISION DEL RECIBO DE DINERO
LA FECHA DE EMISION DEL RECIBO DE DINERO NO NECESARIAMENTE DEBE SER MAYOR A registration_date
QUE ES LA FECHA EN LA QUE EL SISTEMA CREA el registro
ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT 
chk_cancellation_date CHECK (cancellation_date >= registration_date);
*/

ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT 
chk_annulment_date CHECK (annulment_date > registration_date);

/* LA FECHA DE VENCIMIENTO NO NECESARIAMENTE DEBE SER MAYOR A registration_date
QUE ES LA FECHA EN LA QUE EL SISTEMA CREA el registro
ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT 
chk_payment_due_date CHECK (payment_due_date >= registration_date);
*/


ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT chk_amount CHECK (amount >= 0);

ALTER TABLE pms_purchase_invoice_payment ADD CONSTRAINT chk_purchase_invoice_balance 
CHECK (purchase_invoice_balance >= 0);


create sequence pms_purchase_invoice_payment_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_payment.id;


ALTER TABLE pms_cash_receipt_document ADD id_purchase_invoice_payment	bigint;
ALTER TABLE pms_cash_receipt_document alter column id_sale_invoice_payment drop not null;


ALTER TABLE pms_cash_receipt_document 
ADD constraint pms_cash_receipt_document_fk_04
FOREIGN KEY (id_purchase_invoice_payment) REFERENCES pms_purchase_invoice_payment (id);


create table pms_purchase_invoice_credit_note(
id	   		 		   			bigint,
id_person	 		   				bigint NOT NULL,
id_currency	 					bigint NOT NULL,
modified_documens_identifier_numbers	varchar(500),
status							varchar(50) not null default 'application.common.status.pending',
emission_date		   			timestamp NOT NULL,
emission_reason_description			varchar(500),
validity_end_date					timestamp,--tiempo hasta el que el saldo es valido
cancellation_date   				timestamp,
annulment_date   					timestamp,
annulment_reason_description			varchar(500),
identifier_number   				varchar(15) NOT NULL,
total_amount  						numeric(11,2) NOT NULL, --GRAVA IVA 10% + GRAVA IVA 5% + EXCENTO
value_added_amount					numeric(11,2) NOT NULL, --GRAVA IVA 10% + GRAVA IVA 5%
total_tax_amount					numeric(11,2) NOT NULL, --TOTAL IVA: IVA 10% + IVA 5%
exempt_amount 						numeric(11,2) NOT NULL, --EXCENTO
value_added_tax_10_amount			numeric(11,2) NOT NULL, --GRAVA IVA 10%
value_added_tax_5_amount				numeric(11,2) NOT NULL, --GRAVA IVA 5%
tax_10_amount						numeric(11,2) NOT NULL, --IVA 10%
tax_5_amount						numeric(11,2) NOT NULL, --IVA 5%
bussines_name						varchar(500) not null,
bussines_ci_ruc					varchar(50) not null,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp,
credit_note_balance 				numeric(11,2) NOT NULL);


ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT chk_credit_note_balance_01
check (credit_note_balance >= 0);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT chk_credit_note_balance_02
check (credit_note_balance <= total_amount);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT pms_purchase_invoice_credit_note_id_pk PRIMARY KEY (id);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_cancellation_date CHECK (cancellation_date >= emission_date);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_annulment_date CHECK (annulment_date > emission_date);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_value_added_tax_10_amount CHECK (value_added_tax_10_amount >= tax_10_amount);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_value_added_tax_10_amount_02 CHECK (value_added_tax_10_amount >= 0);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_value_added_tax_5_amount CHECK (value_added_tax_5_amount >= tax_5_amount);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_value_added_tax_5_amount_02 CHECK (value_added_tax_5_amount >= 0);

ALTER TABLE pms_purchase_invoice_credit_note ADD CONSTRAINT 
chk_exempt_amount CHECK
(exempt_amount >= 0);

ALTER TABLE pms_purchase_invoice_credit_note 
ADD constraint pms_purchase_invoice_credit_note_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_purchase_invoice_credit_note 
ADD constraint pms_purchase_invoice_credit_note_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

create sequence pms_purchase_invoice_credit_note_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_credit_note.id;


create table pms_purchase_invoice_credit_note_modified_documents(
id	   		 		   			bigint,
id_purchase_invoice_credit_note		bigint not null,
id_purchase_invoice	   				bigint
);

/* ¿una factura puede generar mas de una nota de credito ? 
 * la creacion de una nota de credito puede estar relacionada a mas de una factura
 */

ALTER TABLE pms_purchase_invoice_credit_note_modified_documents 
ADD CONSTRAINT pms_purchase_invoice_credit_note_modified_documents_id_pk PRIMARY KEY (id);


ALTER TABLE pms_purchase_invoice_credit_note_modified_documents 
ADD constraint pms_purchase_invoice_credit_note_modified_documents_fk_01
FOREIGN KEY (id_purchase_invoice_credit_note) REFERENCES pms_purchase_invoice_credit_note (id);

ALTER TABLE pms_purchase_invoice_credit_note_modified_documents 
ADD constraint pms_purchase_invoice_credit_note_modified_documents_fk_02
FOREIGN KEY (id_purchase_invoice) REFERENCES pms_purchase_invoice(id);


create sequence pms_purchase_invoice_credit_note_modified_documents_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_credit_note_modified_documents.id;


create table pms_purchase_invoice_credit_note_item(
id	   		 		   			bigint,
id_purchase_invoice_credit_note	 		   		bigint NOT NULL,
id_purchase_invoice_item				bigint NOT NULL,
id_raw_material					bigint NOT NULL,
id_measurment_unit					bigint NOT NULL,
quantity		 				numeric(11,2) NOT NULL,
unit_price_amount	   						numeric(11,2) NOT NULL,
exempt_amount 						numeric(11,2) NOT NULL,
value_added_tax_10_amount			numeric(11,2) NOT NULL,
value_added_tax_5_amount				numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);

--ALTER table pms_credit_note_item ADD id_sale_invoice_item bigint;--NO NECESARIAMENTE UN ITEM SERA DE UNA FACTURA, PODRIA SER OTRO
--DOCUMENTO, PARA EL CASO SE AGREGARA COLUMNA

ALTER TABLE pms_purchase_invoice_credit_note_item ADD constraint chk_quantity CHECK (quantity >= 0);
ALTER TABLE pms_purchase_invoice_credit_note_item ADD constraint 
chk_unit_price_amount CHECK  (unit_price_amount >= 0);
ALTER TABLE pms_purchase_invoice_credit_note_item ADD 
constraint chk_exempt_amount_01 CHECK  (exempt_amount >= 0);


ALTER TABLE pms_purchase_invoice_credit_note_item ADD 
constraint chk_value_added_tax_10_amount_01 
CHECK  (value_added_tax_10_amount >= 0);

ALTER TABLE pms_purchase_invoice_credit_note_item ADD 
constraint chk_value_added_tax_5_amount_01 
CHECK  (value_added_tax_5_amount >= 0);

ALTER TABLE pms_purchase_invoice_credit_note_item ADD 
CONSTRAINT pms_purchase_invoice_credit_note_item_id_pk PRIMARY KEY (id);

ALTER TABLE pms_purchase_invoice_credit_note_item 
ADD constraint pms_purchase_invoice_credit_note_item_fk_01
FOREIGN KEY (id_purchase_invoice_credit_note) REFERENCES pms_purchase_invoice_credit_note (id);

ALTER TABLE pms_purchase_invoice_credit_note_item 
ADD constraint pms_purchase_invoice_credit_note_item_fk_02
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_purchase_invoice_credit_note_item 
ADD constraint pms_purchase_invoice_credit_note_item_fk_03
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);

ALTER TABLE pms_purchase_invoice_credit_note_item 
ADD constraint pms_purchase_invoice_credit_note_item_fk_04
FOREIGN KEY (id_purchase_invoice_item) REFERENCES pms_purchase_invoice_item (id);


ALTER TABLE pms_purchase_invoice_credit_note_item ADD CONSTRAINT
pms_purchase_invoice_credit_note_item_uk_01 UNIQUE 
(id_purchase_invoice_credit_note,id_raw_material,id_measurment_unit);

create sequence pms_purchase_invoice_credit_note_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_credit_note_item.id;


create table pms_purchase_invoice_payment_cancel_documents(
id	   		 		   			bigint,
id_purchase_invoice_payment 				bigint not null,
id_purchase_invoice_credit_note  					bigint,
resulting_payment_amount				numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);
/* un pago por factura de compra puede ser cancelado por mas de una nota de credito */

--ALTER TABLE pms_sale_invoice_payment_cancel_documents add resulting_payment_amount numeric(11,2) NOT NULL;
--ALTER TABLE pms_sale_invoice_payment_cancel_documents 
--ADD CONSTRAINT chk_resulting_payment_amount check (resulting_payment_amount >= 0);

ALTER TABLE pms_purchase_invoice_payment_cancel_documents 
ADD CONSTRAINT pms_purchase_invoice_payment_cancel_documents_id_pk PRIMARY KEY (id);


ALTER TABLE pms_purchase_invoice_payment_cancel_documents 
ADD constraint pms_purchase_invoice_payment_cancel_documents_fk_01
FOREIGN KEY (id_purchase_invoice_payment) REFERENCES pms_purchase_invoice_payment (id);

ALTER TABLE pms_purchase_invoice_payment_cancel_documents 
ADD constraint pms_purchase_invoice_payment_cancel_documents_fk_02
FOREIGN KEY (id_purchase_invoice_credit_note) REFERENCES pms_purchase_invoice_credit_note(id);


create sequence pms_purchase_invoice_payment_cancel_documents_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_payment_cancel_documents.id;
