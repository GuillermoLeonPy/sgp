/*43-UC-GEN-PAG-FAC-201610150051*/

create table pms_sale_invoice_overdue_payment_charge_condition(
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
ALTER TABLE pms_sale_invoice_overdue_payment_charge_condition ADD CHECK (is_active in ('S'));
ALTER TABLE pms_sale_invoice_overdue_payment_charge_condition ADD CHECK (days_interval > 0);
ALTER TABLE pms_sale_invoice_overdue_payment_charge_condition ADD CHECK (days_interval_percent_increment > 0);

create sequence pms_sale_invoice_overdue_payment_charge_condition_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_overdue_payment_charge_condition.id;

ALTER TABLE pms_sale_invoice_overdue_payment_charge_condition ADD CONSTRAINT 
pms_sale_invoice_overdue_payment_charge_condition_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice_overdue_payment_charge_condition ADD CONSTRAINT
pms_sale_invoice_overdue_payment_charge_condition_uk_01 UNIQUE 
(is_active);