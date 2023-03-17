/*30-UC-REG-PED-201609222354*/
create table pms_order(
id	   		 		   			bigint,
id_person	 		   		bigint NOT NULL,
id_currency	 				bigint NOT NULL,
payment_condition					varchar(50) not null,
status							varchar(50) not null default 'application.common.status.pending',
registration_date		   			timestamp NOT NULL default now(),
estimated_completion_date   			timestamp NOT NULL,
completion_date   					timestamp,
cancellation_date  					timestamp,
cancellation_reason_description		varchar(500),
identifier_number   				bigint NOT NULL,
amount	   						numeric(11,2) NOT NULL,
exempt_amount 						numeric(11,2) NOT NULL,
value_added_tax_10_amount			numeric(11,2) NOT NULL,
value_added_tax_5_amount				numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp,
credit_order_fee_quantity 				bigint,
credit_order_fee_periodicity_days_quantity 	bigint,
credit_order_payment_condition_surcharge_percentage numeric(11,2)
id_credit_order_charge_condition			bigint,
production_activities_instantiation_date 	timestamp);


/* INICIO: para edicion de pedido */
ALTER TABLE pms_order ADD previous_status varchar(50);
ALTER TABLE pms_order ADD previous_amount	   						numeric(11,2);
ALTER TABLE pms_order ADD previous_exempt_amount 						numeric(11,2);
ALTER TABLE pms_order ADD previous_value_added_tax_10_amount			numeric(11,2);
ALTER TABLE pms_order ADD previous_value_added_tax_5_amount				numeric(11,2);


ALTER TABLE pms_order ADD CONSTRAINT chk_previous_amount CHECK (previous_amount > 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_exempt_amount_01 CHECK (previous_exempt_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_exempt_amount_02 CHECK (previous_exempt_amount < previous_amount);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_value_added_tax_10_amount_01 CHECK (previous_value_added_tax_10_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_value_added_tax_10_amount_02 CHECK (previous_value_added_tax_10_amount <= previous_amount);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_value_added_tax_5_amount_01 CHECK (previous_value_added_tax_5_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_previous_value_added_tax_5_amount_02 CHECK (previous_value_added_tax_5_amount <= previous_amount);
/* FIN: para edicion de pedido */

/* INICIO: para instanciar actividades de produccion por pedido */
ALTER TABLE pms_order ADD production_activities_instantiation_date 	timestamp;
ALTER TABLE pms_order ADD CONSTRAINT chk_production_activities_instantiation_date 
check (production_activities_instantiation_date > registration_date);
/* FIN: para instanciar actividades de produccion por pedido */

--ALTER TABLE pms_order ADD credit_order_fee_quantity bigint;
--cantidad de cuotas para pedido tipo credito
ALTER TABLE pms_order ADD CONSTRAINT chk_credit_order_fee_quantity CHECK (credit_order_fee_quantity >= 2);

--ALTER TABLE pms_order ADD credit_order_fee_periodicity_days_quantity bigint;
--periodicidad de vencimiento de cuotas para pedido tipo credito
ALTER TABLE pms_order ADD CONSTRAINT chk_credit_order_fee_periodicity_days_quantity CHECK (credit_order_fee_periodicity_days_quantity >= 7);

--ALTER TABLE pms_order ADD credit_order_payment_condition_surcharge_percentage numeric(11,2);
--porcentaje de incremento de precio sobre el total del pedido, segun registro vigente de cargo por pedido credito
ALTER TABLE pms_order ADD CONSTRAINT chk_credit_order_payment_condition_surcharge_percentage CHECK (credit_order_payment_condition_surcharge_percentage > 0);

--ALTER TABLE pms_order ADD id_credit_order_charge_condition bigint;

SELECT conname
   FROM pg_constraint
  WHERE conrelid = 'pms_order'::regclass
  order by 1
  --alter table pms_order drop constraint chk_value_added_tax_5_amount_02;

ALTER TABLE pms_order ADD CONSTRAINT chk_amount CHECK (amount > 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_exempt_amount_01 CHECK (exempt_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_exempt_amount_02 CHECK (exempt_amount < amount);
ALTER TABLE pms_order ADD CONSTRAINT chk_value_added_tax_10_amount_01 CHECK (value_added_tax_10_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_value_added_tax_10_amount_02 CHECK (value_added_tax_10_amount <= amount);
ALTER TABLE pms_order ADD CONSTRAINT chk_value_added_tax_5_amount_01 CHECK (value_added_tax_5_amount >= 0);
ALTER TABLE pms_order ADD CONSTRAINT chk_value_added_tax_5_amount_02 CHECK (value_added_tax_5_amount <= amount);

ALTER TABLE pms_order ADD CONSTRAINT chk_estimated_completion_date CHECK (estimated_completion_date > registration_date);
ALTER TABLE pms_order ADD CONSTRAINT chk_completion_date CHECK (completion_date > registration_date);
ALTER TABLE pms_order ADD CONSTRAINT chk_cancellation_date CHECK (cancellation_date > registration_date);


ALTER TABLE pms_order ADD CONSTRAINT pms_order_id_pk PRIMARY KEY (id);

ALTER TABLE pms_order 
ADD constraint pms_order_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_order 
ADD constraint pms_order_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

ALTER TABLE pms_order 
ADD constraint pms_order_fk_03
FOREIGN KEY (id_credit_order_charge_condition) REFERENCES pms_credit_order_charge_condition (id);

create sequence pms_order_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order.id;

create sequence pms_order_identifier_number_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order.identifier_number;

create table pms_credit_order_charge_condition(
id	   		 		   			bigint,
is_active							varchar(1) default 'S',
days_interval						bigint not null,
days_interval_percent_increment		numeric(11,2) not null,
--porcentaje de incremento a aplicar segun cantidad de intervalos existentes 
--en la cantidad total de dias para saldar el pedido
registration_date		   			timestamp NOT NULL default now(),
validity_end_date		   			timestamp,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);
ALTER TABLE pms_credit_order_charge_condition ADD CHECK (is_active in ('S'));
ALTER TABLE pms_credit_order_charge_condition ADD CHECK (days_interval > 0);
ALTER TABLE pms_credit_order_charge_condition ADD CHECK (days_interval_percent_increment > 0);

create sequence pms_credit_order_charge_condition_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_credit_order_charge_condition.id;

ALTER TABLE pms_credit_order_charge_condition ADD CONSTRAINT 
pms_credit_order_charge_condition_id_pk PRIMARY KEY (id);

ALTER TABLE pms_credit_order_charge_condition ADD CONSTRAINT
pms_credit_order_charge_condition_uk_01 UNIQUE 
(is_active);

create or replace function p_i_pms_credit_order_charge_condition
(
pid	   		 		   			bigint,
pdays_interval	 		   		bigint,
pdays_interval_percent_increment	   						numeric,
pregistration_date		   			inout timestamp,
pcreation_user	 		   			varchar
)
returns timestamp as
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
	RAISE INFO 'executing p_i_pms_credit_order_charge_condition';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/
	if (pdays_interval is null or pdays_interval < 1)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.credit.order.charge.condition.dto.days.interval.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pdays_interval_percent_increment is null or pdays_interval_percent_increment <=  0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.credit.order.charge.condition.dto.days.interval.percent.increment.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	
	pregistration_date:=now();

	insert into pms_credit_order_charge_condition
			(id,
			days_interval,
			days_interval_percent_increment,
			registration_date,
			creation_user,
			creation_date)
	values
			(pid,
			pdays_interval,
			pdays_interval_percent_increment,
			pregistration_date,
			pcreation_user,
			now());
			
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


create or replace function p_u_pms_credit_order_charge_condition
(
pid	   		 		   			bigint,
plast_modif_user					varchar
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
	RAISE INFO 'executing p_u_pms_credit_order_charge_condition';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	update pms_credit_order_charge_condition
	set
		validity_end_date = now(),
		last_modif_user = plast_modif_user,
		last_modif_date = now(),
		is_active = null
	where
		id = pid;
			
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



create or replace function p_i_pms_order
(
pid	   		 		   			bigint,
pid_person	 		   		bigint,
pid_currency	 				bigint,
ppayment_condition					varchar,
pregistration_date		   			inout timestamp,
pestimated_completion_date   			timestamp,
pidentifier_number   				bigint,
pamount	   						numeric,
pexempt_amount 						numeric,
pvalue_added_tax_10_amount			numeric,
pvalue_added_tax_5_amount				numeric,
pcreation_user	 		   			varchar,
pcredit_order_fee_quantity 				bigint,
pcredit_order_fee_periodicity_days_quantity 	bigint,
pcredit_order_payment_condition_surcharge_percentage numeric,
pid_credit_order_charge_condition			bigint
)
returns timestamp as
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
	RAISE INFO 'executing p_i_pms_order';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/
	if (pcredit_order_fee_quantity is not null and pcredit_order_fee_quantity < 2)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.orderdto.credit.order.fee.quantity.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pcredit_order_fee_periodicity_days_quantity is not null and pcredit_order_fee_periodicity_days_quantity < 7)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.orderdto.credit.order.fee.periodicity.days.quantity.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	
	pregistration_date:=now();
	pamount:= pvalue_added_tax_10_amount + pvalue_added_tax_5_amount + pexempt_amount;

	insert into pms_order
			(id,
			id_person,
			id_currency,
			payment_condition,
			registration_date,
			estimated_completion_date,
			identifier_number,
			amount,
			exempt_amount,
			value_added_tax_10_amount,
			value_added_tax_5_amount,
			creation_user,
			creation_date,
			credit_order_fee_quantity,
			credit_order_fee_periodicity_days_quantity,
			credit_order_payment_condition_surcharge_percentage,
			id_credit_order_charge_condition)
	values
			(pid,
			pid_person,
			pid_currency,
			ppayment_condition,
			pregistration_date,
			pestimated_completion_date,
			pidentifier_number,
			pamount,
			pexempt_amount,
			pvalue_added_tax_10_amount,
			pvalue_added_tax_5_amount,
			pcreation_user,
			now(),
			pcredit_order_fee_quantity,
			pcredit_order_fee_periodicity_days_quantity,
			pcredit_order_payment_condition_surcharge_percentage,
			pid_credit_order_charge_condition);
			
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


create or replace function p_u_pms_order
(
pid	   		 		   			bigint,
pid_person	 		   		bigint,
pid_currency	 				bigint,
ppayment_condition					varchar,
pestimated_completion_date   			timestamp,
pamount	   						numeric,
pexempt_amount 						numeric,
pvalue_added_tax_10_amount			numeric,
pvalue_added_tax_5_amount				numeric,
plast_modif_user	 		   			varchar,
pcredit_order_fee_quantity 				bigint,
pcredit_order_fee_periodicity_days_quantity 	bigint,
pcredit_order_payment_condition_surcharge_percentage numeric,
pid_credit_order_charge_condition			bigint,
pstatus								varchar
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
	RAISE INFO 'executing p_u_pms_order';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/
	if (pcredit_order_fee_quantity is not null and pcredit_order_fee_quantity < 2)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.orderdto.credit.order.fee.quantity.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pcredit_order_fee_periodicity_days_quantity is not null and pcredit_order_fee_periodicity_days_quantity < 7)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.orderdao.orderdto.credit.order.fee.periodicity.days.quantity.invalid.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	
	declare
		vrecord							record;
		vprevious_status					pms_order.previous_status%type;
		vprevious_amount					pms_order.previous_amount%type;
		vprevious_exempt_amount				pms_order.previous_exempt_amount%type;
		vprevious_value_added_tax_10_amount	pms_order.previous_value_added_tax_10_amount%type;
		vprevious_value_added_tax_5_amount		pms_order.previous_value_added_tax_5_amount%type;
		
	begin
		pamount:= pvalue_added_tax_10_amount + pvalue_added_tax_5_amount + pexempt_amount;
		select o.* into vrecord from pms_order o where o.id = pid;
		
		if pstatus = 'application.common.status.revision' and vrecord.status != 'application.common.status.revision' and vrecord.previous_status is null then
			vprevious_status := vrecord.status;
		elsif vrecord.status = 'application.common.status.revision' and vrecord.previous_status is not null then
			pstatus := vrecord.previous_status;
			vprevious_status := vrecord.status;
			vprevious_amount := vrecord.amount;
			vprevious_exempt_amount := vrecord.exempt_amount;
			vprevious_value_added_tax_10_amount := vrecord.value_added_tax_10_amount;
			vprevious_value_added_tax_5_amount := vrecord.value_added_tax_5_amount;
		end if;
		
		update pms_order
				set id_person 									= pid_person,
				id_currency 									= pid_currency,
				payment_condition 								= ppayment_condition,
				estimated_completion_date 						= pestimated_completion_date,
				amount 										= pamount,
				exempt_amount 									= pexempt_amount,
				value_added_tax_10_amount 						= pvalue_added_tax_10_amount,
				value_added_tax_5_amount 						= pvalue_added_tax_5_amount,
				credit_order_fee_quantity 						= pcredit_order_fee_quantity,
				credit_order_fee_periodicity_days_quantity 			= pcredit_order_fee_periodicity_days_quantity,
				credit_order_payment_condition_surcharge_percentage 	= pcredit_order_payment_condition_surcharge_percentage,
				id_credit_order_charge_condition 					= pid_credit_order_charge_condition,
				last_modif_user								= plast_modif_user,
				last_modif_date								= now(),
				status										= pstatus,
				previous_status								= vprevious_status,
				previous_amount								= vprevious_amount,
				previous_exempt_amount 							= vprevious_exempt_amount,
				previous_value_added_tax_10_amount					= vprevious_value_added_tax_10_amount,
				previous_value_added_tax_5_amount					= vprevious_value_added_tax_5_amount
		where
				id = pid;


		if pstatus != 'application.common.status.revision' and vrecord.status != 'application.common.status.revision' then
			/* update order items with the order status */
			update pms_order_item 
				set status = pstatus,
				last_modif_user = plast_modif_user,
				last_modif_date = now()
			where 
				id_order = pid;
		end if;
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

/* item */
create table pms_order_item(
id	   		 		   						bigint,
id_order	 		   							bigint NOT NULL,
id_product	 								bigint NOT NULL,
quantity		 								bigint NOT NULL,
finished_quantity 								bigint NOT NULL default 0,
in_progress_quantity 							bigint NOT NULL default 0,
pending_to_instanciate_quantity 					bigint NOT NULL,
canceled_entering_production_by_document_quantity 	bigint NOT NULL default 0,
unit_price_amount	   							numeric(11,2) NOT NULL,
product_unit_manufacture_cost							numeric(11,2) NOT NULL,
exempt_unit_price_amount 						numeric(11,2) NOT NULL,
value_added_tax_10_unit_price_amount				numeric(11,2) NOT NULL,
value_added_tax_5_unit_price_amount				numeric(11,2) NOT NULL,
creation_user	 		   						varchar(50),
creation_date			   						timestamp NOT NULL default now(),
last_modif_user 		   						varchar(50),
last_modif_date		   						timestamp);

--ALTER TABLE pms_order_item ADD product_unit_manufacture_cost numeric(11,2);
--update pms_order_item set product_unit_manufacture_cost = unit_price_amount;
--ALTER TABLE pms_order_item alter column product_unit_manufacture_cost set not null;
--update pms_order_item set unit_price_amount = ((product_unit_manufacture_cost  + (product_unit_manufacture_cost * (5::numeric / 100::numeric)))::numeric);
--update pms_order_item set value_added_tax_10_unit_price_amount = ((unit_price_amount * quantity)::numeric);

--ALTER TABLE pms_order_item alter column product_unit_manufacture_cost set not null;

--ALTER TABLE pms_order_item ADD finished_quantity bigint NOT NULL default 0;
--ALTER TABLE pms_order_item ADD in_progress_quantity bigint NOT NULL default 0;
--ALTER TABLE pms_order_item ADD pending_to_instanciate_quantity bigint;
--update pms_order_item set pending_to_instanciate_quantity = quantity;
--ALTER TABLE pms_order_item alter column pending_to_instanciate_quantity set not null;
--ALTER TABLE pms_order_item ADD canceled_entering_production_by_document_quantity bigint NOT NULL default 0;

ALTER TABLE pms_order_item ADD constraint 
chk_product_unit_manufacture_cost CHECK  (product_unit_manufacture_cost >= 0);

ALTER TABLE pms_order_item ADD constraint 
chk_product_unit_manufacture_cost_02 CHECK  (product_unit_manufacture_cost <= unit_price_amount);

alter table pms_order_item add constraint chk_quantity_consistence
check 
(quantity = (finished_quantity + in_progress_quantity + pending_to_instanciate_quantity + canceled_entering_production_by_document_quantity));

ALTER TABLE pms_order_item ADD constraint 
chk_finished_quantity CHECK  (finished_quantity >= 0);

ALTER TABLE pms_order_item ADD constraint 
chk_in_progress_quantity CHECK  (in_progress_quantity >= 0);

ALTER TABLE pms_order_item ADD constraint 
chk_pending_to_instanciate_quantity CHECK  (pending_to_instanciate_quantity >= 0);

ALTER TABLE pms_order_item ADD constraint 
chk_canceled_entering_production_by_document_quantity CHECK  (canceled_entering_production_by_document_quantity >= 0);

/* INICIO: para edicion de pedido */
ALTER TABLE pms_order_item ADD status varchar(50) not null default 'application.common.status.pending';
ALTER TABLE pms_order_item ADD previous_status varchar(50);

/* ** */

SELECT conname
   FROM pg_constraint
  WHERE conrelid = 'pms_order_item'::regclass
  order by 1
  --alter table pms_order_item drop constraint pms_order_item_value_added_tax_5_unit_price_amount_check;


ALTER TABLE pms_order_item ADD constraint chk_quantity CHECK (quantity > 0);
ALTER TABLE pms_order_item ADD constraint chk_unit_price_amount CHECK  (unit_price_amount > 0);
ALTER TABLE pms_order_item ADD constraint chk_exempt_unit_price_amount_01 CHECK  (exempt_unit_price_amount >= 0);
ALTER TABLE pms_order_item ADD constraint chk_exempt_unit_price_amount_02 CHECK  (exempt_unit_price_amount <= unit_price_amount);
ALTER TABLE pms_order_item ADD constraint chk_value_added_tax_10_unit_price_amount_01 CHECK  (value_added_tax_10_unit_price_amount >= 0);
/*ALTER TABLE pms_order_item ADD constraint chk_value_added_tax_10_unit_price_amount_02 CHECK  (value_added_tax_10_unit_price_amount >= unit_price_amount); NO NECESARIAMENTE*/
ALTER TABLE pms_order_item ADD constraint chk_value_added_tax_5_unit_price_amount_01 CHECK  (value_added_tax_5_unit_price_amount >= 0);

/*ALTER TABLE pms_order_item ADD constraint chk_value_added_tax_5_unit_price_amount_02 CHECK  (value_added_tax_5_unit_price_amount >= unit_price_amount); NO NECESARIAMENTE*/

ALTER TABLE pms_order_item ADD CONSTRAINT pms_order_item_id_pk PRIMARY KEY (id);

ALTER TABLE pms_order_item 
ADD constraint pms_order_item_fk_01
FOREIGN KEY (id_order) REFERENCES pms_order (id);

ALTER TABLE pms_order_item 
ADD constraint pms_order_item_fk_02
FOREIGN KEY (id_product) REFERENCES pms_product (id);


create sequence pms_order_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_order_item.id;


create or replace function f_pms_order_item_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('pms_order_item_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';


create or replace function p_i_pms_order_item
(
pid	   		 		   			bigint,
pid_order	 		   		bigint,
pid_product	 				bigint,
pquantity		 				bigint,
punit_price_amount	   						numeric,
pproduct_unit_manufacture_cost				numeric,
pexempt_unit_price_amount 						numeric,
pvalue_added_tax_10_unit_price_amount			numeric,
pvalue_added_tax_5_unit_price_amount				numeric,
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
	RAISE INFO 'executing p_i_pms_order_item';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/



	insert into pms_order_item
			(id,
			id_order,
			id_product,
			quantity,
			unit_price_amount,
			exempt_unit_price_amount,
			value_added_tax_10_unit_price_amount,
			value_added_tax_5_unit_price_amount,
			creation_user,
			creation_date,
			pending_to_instanciate_quantity,
			product_unit_manufacture_cost)
	values
			(pid,
			pid_order,
			pid_product,
			pquantity,
			punit_price_amount,
			pexempt_unit_price_amount,
			pvalue_added_tax_10_unit_price_amount,
			pvalue_added_tax_5_unit_price_amount,
			pcreation_user,
			now(),
			pquantity,
			pproduct_unit_manufacture_cost);
			
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


create or replace function p_u_pms_order_item
(
pid	   		 		   			bigint,
pstatus							varchar,
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
	RAISE INFO 'executing p_u_pms_order_item';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	declare
		vrecord		record;
	begin
		select i.* into vrecord from pms_order_item i where i.id = pid;

		if pstatus = 'application.common.status.discarded' and vrecord.status != 'application.common.status.discarded' then
			update pms_order_item
				set
					status = pstatus,
					previous_status = vrecord.status,
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vrecord.id;

			--15.9. Cuando un ítem de pedido pasa a estado DESCARTADO se descartan todas las
			--actividades de producción relacionadas al ítem tales que no tengan fecha de inicio de
			--producción

			--15.10. Al descartar ítems de pedido se debe checkear si los ítems que quedan en estado distinto
			--a DESCARTADO requieren la actualizaci��n del pedido al estado finalizado.
			
		end if;
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
