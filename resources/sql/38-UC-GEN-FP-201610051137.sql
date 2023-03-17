/*38-UC-GEN-FP-201610051137*/
create table pms_company(
id	   		 		   			bigint,
ruc								varchar(50) not null,
bussines_name			varchar(500) not null,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);

ALTER TABLE pms_company ADD CONSTRAINT pms_company_id_pk PRIMARY KEY (id);
ALTER TABLE pms_company ADD CONSTRAINT pms_company_uk_01 UNIQUE (ruc);

create sequence pms_company_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_company.id;


create table pms_company_bussines_activity(
id	   		 		   			bigint,
id_company						bigint not null,
bussines_activity					varchar(100) not null,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);

ALTER TABLE pms_company_bussines_activity ADD CONSTRAINT pms_company_bussines_activity_id_pk PRIMARY KEY (id);

ALTER TABLE pms_company_bussines_activity 
ADD constraint pms_company_bussines_activity_fk_01
FOREIGN KEY (id_company) REFERENCES pms_company (id);

create sequence pms_company_bussines_activity_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_company_bussines_activity.id;


create table pms_branch_office(
id	   		 		   			bigint,
id_company						bigint,
id_code_ruc				varchar(3) not null,--the sale invoice ruc: example: 001
description				varchar(500) not null,
telephone_number			varchar(100),
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);

ALTER TABLE pms_branch_office ADD CONSTRAINT pms_branch_office_id_pk PRIMARY KEY (id);
ALTER TABLE pms_branch_office ADD CONSTRAINT pms_branch_office_uk_01 UNIQUE (id_code_ruc);


ALTER TABLE pms_branch_office 
ADD constraint pms_branch_office_fk_01
FOREIGN KEY (id_company) REFERENCES pms_company (id);

create sequence pms_branch_office_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_branch_office.id;

create table pms_branch_office_sale_station(
id	   		 		   			bigint,
id_code_ruc				varchar(3) not null, --tha sale invoice ruc, example: 003
sale_invoice_description				varchar(250) not null,
id_branch_office					bigint not null,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp
);

ALTER TABLE pms_branch_office_sale_station ADD CONSTRAINT pms_branch_office_sale_station_id_pk PRIMARY KEY (id);

ALTER TABLE pms_branch_office_sale_station ADD CONSTRAINT pms_branch_office_sale_station_uk_01 UNIQUE 
(id_code_ruc);

ALTER TABLE pms_branch_office_sale_station 
ADD constraint pms_branch_office_sale_station_fk_01
FOREIGN KEY (id_branch_office) REFERENCES pms_branch_office (id);

create sequence pms_branch_office_sale_station_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_branch_office_sale_station.id;


create table pms_sale_invoice_stamping(
id	   		 		   			bigint,
sale_invoice_stamping_number			numeric(8,0) not null,
effective_beginning_date				timestamp NOT NULL,
effective_end_date					timestamp NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp,
is_active 						varchar(1) default 'S'
);

--ALTER TABLE pms_sale_invoice_stamping ADD is_active varchar(1) default 'S';
ALTER TABLE pms_sale_invoice_stamping ADD CONSTRAINT chk_is_active CHECK (is_active in ('S'));
ALTER TABLE pms_sale_invoice_stamping ADD CONSTRAINT
pms_sale_invoice_stamping_uk_01 UNIQUE (is_active);

ALTER TABLE pms_sale_invoice_stamping ADD CONSTRAINT pms_sale_invoice_stamping_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice_stamping ADD CONSTRAINT 
chk_effective_end_date_01 CHECK (effective_end_date > effective_beginning_date);
ALTER TABLE pms_sale_invoice_stamping ADD CONSTRAINT 
chk_effective_end_date_02 CHECK (effective_end_date > now());


create sequence pms_sale_invoice_stamping_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_stamping.id;


create table pms_sale_invoice_stamping_numeration(
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

--ALTER TABLE pms_sale_invoice_stamping_numeration ADD id_sale_invoice_stamping	bigint not null;
--ALTER TABLE pms_sale_invoice_stamping_numeration ADD next_number_to_use numeric(7,0) not null;
--ALTER TABLE pms_sale_invoice_stamping_numeration ADD is_active varchar(1);
ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT chk_is_active CHECK (is_active in ('S'));
ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT
pms_sale_invoice_stamping_numeration_uk_01 UNIQUE (is_active);


ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT pms_sale_invoice_stamping_numeration_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT 
chk_end_number CHECK (end_number > start_number);

ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT 
chk_start_number CHECK (start_number > 0);

ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT 
chk_next_number_to_use_01 CHECK (next_number_to_use >= start_number);

ALTER TABLE pms_sale_invoice_stamping_numeration ADD CONSTRAINT 
chk_next_number_to_use_02 CHECK (next_number_to_use <= end_number);

ALTER TABLE pms_sale_invoice_stamping_numeration 
ADD constraint pms_sale_invoice_stamping_numeration_fk_01
FOREIGN KEY (id_sale_invoice_stamping) REFERENCES pms_sale_invoice_stamping (id);

create sequence pms_sale_invoice_stamping_numeration_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_stamping_numeration.id;



create table pms_sale_invoice(
id	   		 		   			bigint,
id_person	 		   		bigint NOT NULL,
id_currency	 				bigint NOT NULL,
id_order	 		   				bigint NOT NULL,
id_branch_office_sale_station			bigint NOT NULL,
payment_condition					varchar(50) not null,
status							varchar(50) not null default 'application.common.status.pending',
emission_date		   			timestamp NOT NULL default now(),
cancellation_date   				timestamp,
annulment_date   					timestamp,
annulment_reason_description			varchar(500),
identifier_number   				varchar(15) NOT NULL,
sale_invoice_stamp_number			numeric(7,0) not null,
id_sale_invoice_stamping_numeration	bigint NOT NULL,
total_amount  						numeric(11,2) NOT NULL, -- GRAVA IVA 10% + GRAVA IVA 5% + EXCENTO
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
last_modif_date		   			timestamp);

/* INICIO: PARA EDICION DE FACTURA, COMPLEMENTARIO VER ARCHIVO: 46-UC-GEN-FP-ITEMS-FACTURA-201610161153.sql*/
ALTER TABLE pms_sale_invoice ADD previous_status varchar(50);
ALTER TABLE pms_sale_invoice ADD previous_total_amount  						numeric(11,2) ; -- GRAVA IVA 10% + GRAVA IVA 5% + EXCENTO
ALTER TABLE pms_sale_invoice ADD previous_value_added_amount					numeric(11,2) ; --GRAVA IVA 10% + GRAVA IVA 5%
ALTER TABLE pms_sale_invoice ADD previous_total_tax_amount					numeric(11,2) ; --TOTAL IVA: IVA 10% + IVA 5%
ALTER TABLE pms_sale_invoice ADD previous_exempt_amount 						numeric(11,2) ; --EXCENTO
ALTER TABLE pms_sale_invoice ADD previous_value_added_tax_10_amount			numeric(11,2) ; --GRAVA IVA 10%
ALTER TABLE pms_sale_invoice ADD previous_value_added_tax_5_amount				numeric(11,2) ; --GRAVA IVA 5%
ALTER TABLE pms_sale_invoice ADD previous_tax_10_amount						numeric(11,2) ; --IVA 10%
ALTER TABLE pms_sale_invoice ADD previous_tax_5_amount						numeric(11,2) ; --IVA 5%


ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_value_added_tax_10_amount_01 CHECK (previous_value_added_tax_10_amount >= previous_tax_10_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_value_added_tax_10_amount_02 CHECK (previous_value_added_tax_10_amount >= 0);


ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_value_added_tax_5_amount CHECK (previous_value_added_tax_5_amount >= previous_tax_5_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_value_added_tax_5_amount_02 CHECK (previous_value_added_tax_5_amount >= 0);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_exempt_amount CHECK
(previous_exempt_amount >= 0)

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_total_amount CHECK (previous_total_amount >= previous_value_added_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_previous_value_added_amount CHECK (previous_value_added_amount > previous_total_tax_amount);
/* FIN: PARA EDICION DE FACTURA, COMPLEMENTARIO VER ARCHIVO: 46-UC-GEN-FP-ITEMS-FACTURA-201610161153.sql*/

--ALTER TABLE pms_sale_invoice ADD sale_invoice_stamp_number			numeric(7,0) not null;
--ALTER TABLE pms_sale_invoice ADD id_sale_invoice_stamping_numeration	bigint NOT NULL;
--ALTER TABLE pms_sale_invoice ADD id_branch_office_sale_station			bigint NOT NULL;

ALTER TABLE pms_sale_invoice ADD CONSTRAINT
pms_sale_invoice_uk_01 UNIQUE (sale_invoice_stamp_number,id_sale_invoice_stamping_numeration);

/*ALTER TABLE pms_sale_invoice ADD CONSTRAINT
pms_sale_invoice_uk_02 UNIQUE (id_order); 
NO APLICA POR QUE SE PUEDE ANULAR UNA FACTURA POR UN PEDIDO Y LUEGO GENERAR OTRA
*/

ALTER TABLE pms_sale_invoice ADD CONSTRAINT pms_sale_invoice_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_cancellation_date CHECK (cancellation_date > emission_date);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_annulment_date CHECK (annulment_date > emission_date);

--ALTER TABLE pms_sale_invoice drop CONSTRAINT chk_value_added_tax_10_amount;
ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_value_added_tax_10_amount CHECK (value_added_tax_10_amount >= tax_10_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_value_added_tax_10_amount_02 CHECK (value_added_tax_10_amount >= 0);

--ALTER TABLE pms_sale_invoice drop CONSTRAINT chk_value_added_tax_5_amount;
ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_value_added_tax_5_amount CHECK (value_added_tax_5_amount >= tax_5_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_value_added_tax_5_amount_02 CHECK (value_added_tax_5_amount >= 0);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_exempt_amount CHECK
(exempt_amount >= 0)

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_total_amount CHECK (total_amount >= value_added_amount);

ALTER TABLE pms_sale_invoice ADD CONSTRAINT 
chk_value_added_amount CHECK (value_added_amount > total_tax_amount);

ALTER TABLE pms_sale_invoice 
ADD constraint pms_sale_invoice_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_sale_invoice 
ADD constraint pms_sale_invoice_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

ALTER TABLE pms_sale_invoice 
ADD constraint pms_sale_invoice_fk_03
FOREIGN KEY (id_order) REFERENCES pms_order (id);

ALTER TABLE pms_sale_invoice 
ADD constraint pms_sale_invoice_fk_04
FOREIGN KEY (id_sale_invoice_stamping) REFERENCES pms_sale_invoice_stamping (id);

ALTER TABLE pms_sale_invoice 
ADD constraint pms_sale_invoice_fk_05
FOREIGN KEY (id_branch_office_sale_station) REFERENCES pms_branch_office_sale_station (id);

create sequence pms_sale_invoice_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice.id;

--delete from pms_sale_invoice
select * from pms_sale_invoice

/*
update pms_sale_invoice_stamping_numeration
set start_number = 24000,next_number_to_use = 24000, is_active = 'S'
where id = 4;
*/
select p_i_pms_sale_invoice(nextval('pms_sale_invoice_id_sq'),7,1,'xxx');
create or replace function p_i_pms_sale_invoice
(
pid	   		 		   			bigint,
pid_order			 				bigint,
pid_branch_office_sale_station		bigint,
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
	RAISE INFO 'executing p_i_pms_sale_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vvalue_added_amount 						pms_sale_invoice.value_added_amount%type;
		vtotal_amount		 						pms_sale_invoice.total_amount%type;
		vtotal_tax_amount	 						pms_sale_invoice.total_tax_amount%type;
		vid_sale_invoice_stamping	 				bigint;
		vtax_10_amount								pms_sale_invoice.tax_10_amount%type;
		vtax_5_amount								pms_sale_invoice.tax_5_amount%type;
		vbussines_name								pms_sale_invoice.bussines_name%type;
		vbussines_ci_ruc							pms_sale_invoice.bussines_ci_ruc%type;
		vid_person	 		   					bigint;
		vid_currency	 							bigint;
		videntifier_number							pms_sale_invoice.identifier_number%type;
		vsale_invoice_stamp_number					pms_sale_invoice.sale_invoice_stamp_number%type;
		vid_sale_invoice_stamping_numeration			pms_sale_invoice.id_sale_invoice_stamping_numeration%type;
		vrecord									record;
		vperson_record								record;
		vsale_station_record						record;
	begin

		select ord.* into vrecord from pms_order ord where ord.id = pid_order;
		
		vvalue_added_amount := vrecord.value_added_tax_10_amount + vrecord.value_added_tax_5_amount;
		vtotal_amount :=  vvalue_added_amount + vrecord.exempt_amount;
		vtax_10_amount := vrecord.value_added_tax_10_amount * 0.090909;
		vtax_5_amount := vrecord.value_added_tax_5_amount * 0.047619;
		vtotal_tax_amount := vtax_10_amount + vtax_5_amount;

		RAISE INFO '--------------------------';
		RAISE INFO ' order amount : %', vrecord.amount;
		RAISE INFO ' invoice amount : %', vtotal_amount;
		RAISE INFO ' invoice tax 10 amount : %', vtax_10_amount;
		RAISE INFO ' invoice tax 5 amount : %', vtax_5_amount;
		RAISE INFO '--------------------------';
		
		/* fill bussines name and ruc*/
		select person.* into vperson_record from pms_person person where person.id = vrecord.id_person;
		if vperson_record.commercial_name is not null and vperson_record.ruc is not null then
			vbussines_name := vperson_record.commercial_name;
			vbussines_ci_ruc := vperson_record.ruc;
		else
			vbussines_name := vperson_record.personal_name||' '||vperson_record.personal_last_name;
			vbussines_ci_ruc := vperson_record.personal_civil_id_document;
		end if;

		/* identifier number *//* invoice number assignment */
		select bo.id_code_ruc office_ruc ,bost.id_code_ruc sale_station_ruc
		into vsale_station_record
		from pms_branch_office_sale_station bost,pms_branch_office bo
		where
		bost.id = pid_branch_office_sale_station
		and bost.id_branch_office = bo.id;
		
		/* check sale invoice stamping */
		select id into
		vid_sale_invoice_stamping
		from pms_sale_invoice_stamping where 
		is_active = 'S'
		and now() between effective_beginning_date and effective_end_date;
		
		vid_sale_invoice_stamping_numeration := f_determinate_sale_invoice_stamping_number_id(vid_sale_invoice_stamping);
		vsale_invoice_stamp_number := f_determinate_sale_invoice_stamping_number(vid_sale_invoice_stamping,pcreation_user);
		videntifier_number := vsale_station_record.office_ruc||'-'||vsale_station_record.sale_station_ruc||'-'||trim(to_char(vsale_invoice_stamp_number,'0000000'));

		RAISE INFO '--------------------------';
		RAISE INFO ' invoice identifier number : %', videntifier_number;
		RAISE INFO '--------------------------';

		
		
		insert into pms_sale_invoice
				(id,
				id_person,
				id_currency,
				id_order,
				id_branch_office_sale_station,
				payment_condition,
				total_amount,				
				exempt_amount,
				value_added_amount,
				value_added_tax_10_amount,
				value_added_tax_5_amount,
				total_tax_amount,
				tax_10_amount,
				tax_5_amount,
				bussines_name,
				bussines_ci_ruc,
				identifier_number,
				sale_invoice_stamp_number,
				id_sale_invoice_stamping_numeration,
				creation_user)
		values
				(pid,
				vrecord.id_person,
				vrecord.id_currency,
				vrecord.id,
				pid_branch_office_sale_station,
				vrecord.payment_condition,
				vtotal_amount,				
				vrecord.exempt_amount,
				vvalue_added_amount,
				vrecord.value_added_tax_10_amount,
				vrecord.value_added_tax_5_amount,
				vtotal_tax_amount,
				vtax_10_amount,
				vtax_5_amount,
				vbussines_name,
				vbussines_ci_ruc,
				videntifier_number,
				vsale_invoice_stamp_number,
				vid_sale_invoice_stamping_numeration,
				pcreation_user);

		/* insert sale invoice items */
		declare
		    vcursor CURSOR FOR
		    select * from pms_order_item where id_order = vrecord.id order by id asc;
		begin
			for vorder_item_record in vcursor loop
				insert into pms_sale_invoice_item
					(id,
					id_sale_invoice,
					id_product,
					id_order_item,
					quantity,
					unit_price_amount,
					exempt_unit_price_amount,
					value_added_tax_10_unit_price_amount,
					value_added_tax_5_unit_price_amount,
					creation_user,
					product_exigible_by_invoice_quantity)
				values
					(nextval('pms_sale_invoice_item_id_sq'),
					pid,
					vorder_item_record.id_product,
					vorder_item_record.id,
					vorder_item_record.quantity,
					vorder_item_record.unit_price_amount,
					vorder_item_record.exempt_unit_price_amount,
					vorder_item_record.value_added_tax_10_unit_price_amount,
					vorder_item_record.value_added_tax_5_unit_price_amount,
					pcreation_user,
					vorder_item_record.quantity);
			end loop;
		end;
		


		/* update order to the invoiced status */
		update pms_order
			set status = 'application.common.status.invoiced', last_modif_user = pcreation_user, last_modif_date = now()
			where id = vrecord.id;
		/* update order items to the order status */
		update pms_order_item
			set status = 'application.common.status.invoiced', last_modif_user = pcreation_user, last_modif_date = now()
			where id_order = vrecord.id;
				
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

select f_determinate_sale_invoice_stamping_number_id(1);
select f_determinate_sale_invoice_stamping_number(1,'xxx');
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


--select f_determinate_sale_invoice_stamping_number_id(1);
create or replace function f_determinate_sale_invoice_stamping_number_id
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
	RAISE INFO 'executing f_determinate_sale_invoice_stamping_number_id';
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

