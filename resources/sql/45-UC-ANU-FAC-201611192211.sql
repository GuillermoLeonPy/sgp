/*45-UC-ANU-FAC-201611192211*/
create table pms_credit_note(
id	   		 		   			bigint,
id_person	 		   				bigint NOT NULL,
id_currency	 					bigint NOT NULL,
modified_documens_identifier_numbers	varchar(500),
id_branch_office_sale_station			bigint NOT NULL,
status							varchar(50) not null default 'application.common.status.pending',
emission_date		   			timestamp NOT NULL default now(),
emission_reason_description			varchar(500),
validity_end_date					timestamp,--tiempo hasta el que el saldo es valido
cancellation_date   				timestamp,
annulment_date   					timestamp,
annulment_reason_description			varchar(500),
identifier_number   				varchar(15) NOT NULL,
credit_note_stamp_number				numeric(7,0) not null,
id_credit_note_stamping_numeration		bigint NOT NULL,
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

ALTER TABLE pms_credit_note add credit_note_balance numeric(11,2) NOT NULL;
ALTER TABLE pms_credit_note ADD CONSTRAINT chk_credit_note_balance_01
check (credit_note_balance >= 0);

ALTER TABLE pms_credit_note ADD CONSTRAINT chk_credit_note_balance_02
check (credit_note_balance <= total_amount);

ALTER TABLE pms_credit_note ADD CONSTRAINT
pms_credit_note_uk_01 UNIQUE (credit_note_stamp_number,id_credit_note_stamping_numeration);

ALTER TABLE pms_credit_note ADD CONSTRAINT pms_credit_note_id_pk PRIMARY KEY (id);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_cancellation_date CHECK (cancellation_date >= emission_date);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_annulment_date CHECK (annulment_date > emission_date);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_value_added_tax_10_amount CHECK (value_added_tax_10_amount >= tax_10_amount);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_value_added_tax_10_amount_02 CHECK (value_added_tax_10_amount >= 0);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_value_added_tax_5_amount CHECK (value_added_tax_5_amount >= tax_5_amount);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_value_added_tax_5_amount_02 CHECK (value_added_tax_5_amount >= 0);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_exempt_amount CHECK
(exempt_amount >= 0);

/* POR EL HECHO DE PRIMERO INSERTAR LA CABECERA (VER FUNCION: p_generate_credit_note_by_sale_invoice(?, ?, ?))
NO SE PUEDEN APLICAR ESTOS CHECKS
ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_total_amount CHECK (total_amount >= value_added_amount);

ALTER TABLE pms_credit_note ADD CONSTRAINT 
chk_value_added_amount CHECK (value_added_amount > total_tax_amount);*/

ALTER TABLE pms_credit_note 
ADD constraint pms_credit_note_fk_01
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_credit_note 
ADD constraint pms_credit_note_fk_02
FOREIGN KEY (id_currency) REFERENCES pms_currency (id);

ALTER TABLE pms_credit_note 
ADD constraint pms_credit_note_fk_03
FOREIGN KEY (id_branch_office_sale_station) REFERENCES pms_branch_office_sale_station (id);

create sequence pms_credit_note_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_credit_note.id;


create table pms_credit_note_modified_documents(
id	   		 		   			bigint,
id_credit_note	   					bigint not null,
id_sale_invoice	   				bigint
);

/* ¿una factura puede generar mas de una nota de credito ? 
 * la creacion de una nota de credito puede estar relacionada a mas de una factura
 */

ALTER TABLE pms_credit_note_modified_documents 
ADD CONSTRAINT pms_credit_note_modified_documents_id_pk PRIMARY KEY (id);


ALTER TABLE pms_credit_note_modified_documents 
ADD constraint pms_credit_note_modified_documents_fk_01
FOREIGN KEY (id_credit_note) REFERENCES pms_credit_note (id);

ALTER TABLE pms_credit_note_modified_documents 
ADD constraint pms_credit_note_modified_documents_fk_02
FOREIGN KEY (id_sale_invoice) REFERENCES pms_sale_invoice(id);


create sequence pms_credit_note_modified_documents_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_credit_note_modified_documents.id;


create table pms_credit_note_item(
id	   		 		   			bigint,
id_credit_note	 		   		bigint NOT NULL,
id_sale_invoice_item				bigint NOT NULL,
id_product	 				bigint NOT NULL,
status						varchar(50) not null default 'application.common.status.pending',
devolution_quantity		 				bigint NOT NULL,--
cancellation_withdrawal_quantity		 				bigint NOT NULL,--
unit_price_amount	   						numeric(11,2) NOT NULL,
exempt_unit_price_amount 						numeric(11,2) NOT NULL,
value_added_tax_10_unit_price_amount			numeric(11,2) NOT NULL,
value_added_tax_5_unit_price_amount				numeric(11,2) NOT NULL,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);

--ALTER table pms_credit_note_item ADD id_sale_invoice_item bigint;--NO NECESARIAMENTE UN ITEM SERA DE UNA FACTURA, PODRIA SER OTRO
--DOCUMENTO, PARA EL CASO SE AGREGARA COLUMNA
--ALTER TABLE pms_credit_note_item drop quantity CASCADE;
ALTER TABLE pms_credit_note_item ADD devolution_quantity bigint;
update pms_credit_note_item set devolution_quantity = 0;
ALTER TABLE pms_credit_note_item alter column devolution_quantity set not null;

ALTER TABLE pms_credit_note_item ADD constraint chk_devolution_quantity CHECK (devolution_quantity >= 0);

ALTER TABLE pms_credit_note_item ADD cancellation_withdrawal_quantity bigint;
update pms_credit_note_item set cancellation_withdrawal_quantity = 0;
ALTER TABLE pms_credit_note_item alter column cancellation_withdrawal_quantity set not null;

ALTER TABLE pms_credit_note_item ADD constraint chk_devolution_quantity CHECK (cancellation_withdrawal_quantity >= 0);


ALTER TABLE pms_credit_note_item ADD constraint chk_unit_price_amount CHECK  (unit_price_amount >= 0);
ALTER TABLE pms_credit_note_item ADD constraint chk_exempt_unit_price_amount_01 CHECK  (exempt_unit_price_amount >= 0);
ALTER TABLE pms_credit_note_item ADD constraint chk_exempt_unit_price_amount_02 CHECK  (exempt_unit_price_amount <= unit_price_amount);
ALTER TABLE pms_credit_note_item ADD constraint chk_value_added_tax_10_unit_price_amount_01 CHECK  (value_added_tax_10_unit_price_amount >= 0);
ALTER TABLE pms_credit_note_item ADD constraint chk_value_added_tax_5_unit_price_amount_01 CHECK  (value_added_tax_5_unit_price_amount >= 0);

ALTER TABLE pms_credit_note_item ADD CONSTRAINT pms_credit_note_item_id_pk PRIMARY KEY (id);

ALTER TABLE pms_credit_note_item 
ADD constraint pms_credit_note_item_fk_01
FOREIGN KEY (id_credit_note) REFERENCES pms_credit_note (id);

ALTER TABLE pms_credit_note_item 
ADD constraint pms_credit_note_item_fk_02
FOREIGN KEY (id_product) REFERENCES pms_product (id);

ALTER TABLE pms_credit_note_item 
ADD constraint pms_credit_note_item_fk_03
FOREIGN KEY (id_sale_invoice_item) REFERENCES pms_sale_invoice_item (id);


ALTER TABLE pms_credit_note_item ADD CONSTRAINT
pms_credit_note_item_uk_02 UNIQUE 
(id_credit_note,id_product);

create sequence pms_credit_note_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_credit_note_item.id;



