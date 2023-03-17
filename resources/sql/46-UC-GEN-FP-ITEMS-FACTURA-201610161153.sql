/*46-UC-GEN-FP-ITEMS-FACTURA-201610161153*/
--drop table pms_sale_invoice_item
create table pms_sale_invoice_item(
id	   		 		   			bigint,
id_sale_invoice	 		   		bigint NOT NULL,
id_product	 				bigint NOT NULL,
id_order_item					bigint NOT NULL,
status						varchar(50) not null default 'application.common.status.pending',
previous_status						varchar(50),
quantity		 				bigint NOT NULL,
previous_quantity		 				bigint ,--PARA EDICION DE CANTIDAD
unit_price_amount	   						numeric(11,2) NOT NULL,
exempt_unit_price_amount 						numeric(11,2) NOT NULL,
value_added_tax_10_unit_price_amount			numeric(11,2) NOT NULL,
value_added_tax_5_unit_price_amount				numeric(11,2) NOT NULL,
previous_exempt_unit_price_amount 						numeric(11,2),--PARA EDICION DE CANTIDAD
previous_value_added_tax_10_unit_price_amount			numeric(11,2) ,--PARA EDICION DE CANTIDAD
previous_value_added_tax_5_unit_price_amount				numeric(11,2) ,--PARA EDICION DE CANTIDAD
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp,
product_delivered_quantity 				bigint not null default 0,--CANTIDAD DE productos ya entregados por la factura
product_stock_quantity					bigint not null default 0,--CANTIDAD DE productos terminados almacenados en stock
product_deliver_blocked_by_doc_quantity		bigint not null default 0,--CANTIDAD de productos con entrega bloqueada por nota de credito
product_returned_quantity_stock			bigint not null default 0,
product_exigible_by_invoice_quantity		bigint not null
);

alter table pms_sale_invoice_item add product_returned_quantity_stock bigint not null default 0;
alter table pms_sale_invoice_item add product_exigible_by_invoice_quantity bigint not null;
update pms_sale_invoice_item set product_exigible_by_invoice_quantity = 0;
alter table pms_sale_invoice_item alter column product_exigible_by_invoice_quantity set not null;

alter table pms_sale_invoice_item 
add constraint chk_product_returned_quantity_stock check
(product_returned_quantity_stock >= 0);

alter table pms_sale_invoice_item 
add constraint chk_product_returned_quantity_stock_02 check
(product_returned_quantity_stock <= product_delivered_quantity);

alter table pms_sale_invoice_item 
add constraint chk_product_exigible_by_invoice_quantity check
(product_exigible_by_invoice_quantity >= 0);

alter table pms_sale_invoice_item 
add constraint chk_product_exigible_by_invoice_quantity_02  check
(product_exigible_by_invoice_quantity <= quantity);





--alter table pms_sale_invoice_item add canceled_for_deliver_quantity_by_document	bigint 	not null default 0;

/*SELECT conname
   FROM pg_constraint
  WHERE conrelid = 'pms_sale_invoice_item'::regclass
  order by 1*/
  --alter table pms_sale_invoice_item drop constraint pms_sale_invoice_item_value_added_tax_5_unit_price_amount_check;

ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_delivered_quantity CHECK 
(product_delivered_quantity <= quantity);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_stock_quantity CHECK 
(product_stock_quantity <= quantity);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_deliver_blocked_by_doc_quantity CHECK 
(product_deliver_blocked_by_doc_quantity >= 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_deliver_blocked_by_doc_quantity_02 CHECK 
(product_deliver_blocked_by_doc_quantity <= product_stock_quantity);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_sum_delivered_stock_quantity CHECK 
(product_delivered_quantity + product_stock_quantity <= quantity);

ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_delivered_quantity_02 CHECK (product_delivered_quantity >= 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_product_stock_quantity_02 CHECK (product_stock_quantity >= 0);



ALTER TABLE pms_sale_invoice_item ADD constraint chk_quantity CHECK (quantity > 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_unit_price_amount CHECK  (unit_price_amount > 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_exempt_unit_price_amount_01 CHECK  (exempt_unit_price_amount >= 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_exempt_unit_price_amount_02 CHECK  (exempt_unit_price_amount <= unit_price_amount);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_value_added_tax_10_unit_price_amount_01 CHECK  (value_added_tax_10_unit_price_amount >= 0);
/*ALTER TABLE pms_sale_invoice_item ADD constraint chk_value_added_tax_10_unit_price_amount_02 CHECK  (value_added_tax_10_unit_price_amount >= unit_price_amount); NO NECESARIAMENTE*/
ALTER TABLE pms_sale_invoice_item ADD constraint chk_value_added_tax_5_unit_price_amount_01 CHECK  (value_added_tax_5_unit_price_amount >= 0);
/*ALTER TABLE pms_sale_invoice_item ADD constraint chk_value_added_tax_5_unit_price_amount_02 CHECK  (value_added_tax_5_unit_price_amount >= unit_price_amount); NO NECESARIAMENTE*/

ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_quantity CHECK (previous_quantity > 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_exempt_unit_price_amount_01 CHECK  (previous_exempt_unit_price_amount >= 0);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_exempt_unit_price_amount_02 CHECK  (previous_exempt_unit_price_amount <= unit_price_amount);
ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_value_added_tax_10_unit_price_amount_01 CHECK  (previous_value_added_tax_10_unit_price_amount >= 0);
/*ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_value_added_tax_10_unit_price_amount_02 CHECK  (previous_value_added_tax_10_unit_price_amount >= unit_price_amount);NO NECESARIAMENTE*/
ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_value_added_tax_5_unit_price_amount_01 CHECK  (previous_value_added_tax_5_unit_price_amount >= 0);
/*ALTER TABLE pms_sale_invoice_item ADD constraint chk_previous_value_added_tax_5_unit_price_amount_02 CHECK  (previous_value_added_tax_5_unit_price_amount >= unit_price_amount);NO NECESARIAMENTE*/


ALTER TABLE pms_sale_invoice_item ADD CONSTRAINT pms_sale_invoice_item_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice_item 
ADD constraint pms_sale_invoice_item_fk_01
FOREIGN KEY (id_sale_invoice) REFERENCES pms_sale_invoice (id);

ALTER TABLE pms_sale_invoice_item 
ADD constraint pms_sale_invoice_item_fk_02
FOREIGN KEY (id_product) REFERENCES pms_product (id);

ALTER TABLE pms_sale_invoice_item 
ADD constraint pms_sale_invoice_item_fk_03
FOREIGN KEY (id_order_item) REFERENCES pms_order_item (id);

ALTER TABLE pms_sale_invoice_item ADD CONSTRAINT
pms_sale_invoice_item_uk_01 UNIQUE 
(id_sale_invoice,id_order_item);

/*
NO APLICA POR LA FUNCIONALIDAD DE REGENERAR FACTURA POR PEDIDO: EN EL CASO DE UNA DEVOLUCION PARCIAL DE UN ITEM
EL CLIENTE PUEDE DECIDIR DEJAR UN NUMERO DE UNIDADES; PARA LO CUAL SE DESCARTA EL ITEM DE PEDIDO, Y SE CREA UN NUEVO
ITEM DE PEDIDO CON LA CANTIDAD DE UNIDADES QUE DESEA LLEVAR

ALTER TABLE pms_sale_invoice_item ADD CONSTRAINT
pms_sale_invoice_item_uk_02 UNIQUE 
(id_sale_invoice,id_product);*/

create sequence pms_sale_invoice_item_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_item.id;
