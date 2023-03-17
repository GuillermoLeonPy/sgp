/*80-UC-RCC-MP-NOTA-DE-CREDITO-PROVEEDOR-201612091426*/
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
stamping_number							numeric(8,0) not null,
stamping_number_start_validity_date			timestamp,
stamping_number_end_validity_date				timestamp,
branch_office_address						varchar(500),
branch_office_telephone_nbr					varchar(500),
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

ALTER TABLE pms_purchase_invoice_credit_note_item ADD CONSTRAINT
pms_purchase_invoice_credit_note_item_uk_02 UNIQUE 
(id_purchase_invoice_credit_note,id_purchase_invoice_item);

create sequence pms_purchase_invoice_credit_note_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_purchase_invoice_credit_note_item.id;

create or replace function f_pms_purchase_invoice_credit_note_item_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('pms_purchase_invoice_credit_note_item_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';

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
