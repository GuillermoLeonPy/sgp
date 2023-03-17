/*84-UC-REG-SDP-201611141831*/

select * from pms_sale_invoice where status != 'application.common.status.annulled'

select * from v_sale_invoice_product_deliverables
--drop view v_sale_invoice_product_deliverables;
create or replace view v_sale_invoice_product_deliverables as
select
si.id id_sale_invoice,
si.identifier_number invoice_identifier_number,
si.status invoice_status,
si.id_person,
sum(sii.product_stock_quantity) invoice_product_physical_quantity_in_stock,
sum(sii.product_delivered_quantity) invoice_product_delivered_quantity,
sum(sii.product_returned_quantity_stock) invoice_product_returned_quantity_stock,
ord.id id_order,
ord.identifier_number order_identifier_number,
ord.status order_status,
sum(sii.product_exigible_by_invoice_quantity) invoice_product_exigible_quantity,
sum(ord_item.canceled_entering_production_by_document_quantity) order_product_canceled_entering_production,
sum(ord_item.pending_to_instanciate_quantity) order_product_quantity_pending_to_production,
sum(ord_item.in_progress_quantity) order_product_quantity_in_progress,
sum(ord_item.finished_quantity) order_product_finished_quantity
from 
pms_order ord,
pms_order_item ord_item,
pms_sale_invoice si,
pms_sale_invoice_item sii
where
si.status != 'application.common.status.annulled'
and ord_item.status != 'application.common.status.discarded'
and ord.id = ord_item.id_order
and si.id_order = ord.id
and si.id = sii.id_sale_invoice
and sii.id_order_item = ord_item.id
group by
si.id,
si.identifier_number,
si.status,
si.id_person,
ord.id,
ord.identifier_number,
ord.status
order by si.identifier_number desc

--drop view v_sale_invoice_item_product_deliverables
create or replace view v_sale_invoice_item_product_deliverables
as
select
--ord_item.status,
ord_item.id_order,
ord_item.id id_order_item,
sii.id_sale_invoice,
sii.id id_sale_invoice_item,
sii.id_product,
product.product_id,
ord_item.canceled_entering_production_by_document_quantity ord_item_canceled_entering_production,
ord_item.pending_to_instanciate_quantity ord_item_pending_to_production,
ord_item.in_progress_quantity ord_item_in_progress_quantity,
(sii.quantity 
- sii.product_deliver_blocked_by_doc_quantity 
- ord_item.canceled_entering_production_by_document_quantity) invoice_item_total_exigible_quantity,
(sii.product_exigible_by_invoice_quantity) invoice_item_remain_exigible_quantity,
sii.product_delivered_quantity invoice_item_delivered_quantity,
sii.product_stock_quantity invoice_item_product_stock_quantity,
sii.product_returned_quantity_stock invoice_item_returned_quantity_stock
from
pms_product product,
pms_sale_invoice_item sii,
pms_order_item ord_item
where
ord_item.status != 'application.common.status.discarded'
and product.id = sii.id_product
and sii.id_order_item = ord_item.id
order by 
sii.id_sale_invoice,
sii.id


/* ************************************************************** */
create table pms_sale_invoice_product_deposit_movement(
id										bigint,
id_sale_invoice							bigint not null,
sale_invoice_identifier_number				varchar(15) not null,
movement_date								timestamp not null default now(),
movement_type								varchar(50) not null default 'application.common.storage.operation.outcome',
product_deposit_movement_identifier_number		bigint not null,
creation_user								varchar(50),
creation_date								timestamp not null default now()
);

/* to manage product devolution */

ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD id_credit_note bigint;

ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD credit_note_identifier_number	varchar(15);

ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD constraint pms_sale_invoice_product_deposit_movement_fk_02
FOREIGN KEY (id_credit_note) 
REFERENCES pms_credit_note(id);

/* */

ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD CONSTRAINT chk_movement_type 
CHECK 
(movement_type in
('application.common.storage.operation.outcome','application.common.storage.operation.income'));


ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD CONSTRAINT 
pms_sale_invoice_product_deposit_movement_id_pk PRIMARY KEY (id);

ALTER TABLE pms_sale_invoice_product_deposit_movement 
ADD constraint pms_sale_invoice_product_deposit_movement_fk_01
FOREIGN KEY (id_sale_invoice) 
REFERENCES pms_sale_invoice(id);

ALTER TABLE pms_sale_invoice_product_deposit_movement ADD CONSTRAINT
pms_sale_invoice_product_deposit_movement_uk 
UNIQUE (product_deposit_movement_identifier_number);

create sequence pms_sale_invoice_product_deposit_movement_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_product_deposit_movement.id;

create sequence pms_product_deposit_movement_identifier_number_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_product_deposit_movement.product_deposit_movement_identifier_number;


create table pms_si_item_product_deposit_movement(
id									bigint,
id_sale_invoice_product_deposit_movement	bigint not null,
id_sale_invoice_item					bigint not null,
id_product							bigint not null,
product_id							varchar(100),
quantity								bigint not null
);

/* to manage product devolution */

ALTER TABLE pms_si_item_product_deposit_movement 
ADD id_credit_note_item bigint;


ALTER TABLE pms_si_item_product_deposit_movement 
ADD constraint pms_si_item_product_deposit_movement_fk_04
FOREIGN KEY (id_credit_note_item) 
REFERENCES pms_credit_note_item(id);

ALTER TABLE pms_si_item_product_deposit_movement ADD CONSTRAINT
pms_si_item_product_deposit_movement_uk_02 
UNIQUE (id_sale_invoice_product_deposit_movement,id_credit_note_item);

ALTER TABLE pms_si_item_product_deposit_movement 
ADD CONSTRAINT 
pms_si_item_product_deposit_movement_id_pk PRIMARY KEY (id);

ALTER TABLE pms_si_item_product_deposit_movement 
ADD constraint pms_si_item_product_deposit_movement_fk_01
FOREIGN KEY (id_sale_invoice_product_deposit_movement) 
REFERENCES pms_sale_invoice_product_deposit_movement(id);

ALTER TABLE pms_si_item_product_deposit_movement 
ADD constraint pms_si_item_product_deposit_movement_fk_02
FOREIGN KEY (id_sale_invoice_item) 
REFERENCES pms_sale_invoice_item(id);

ALTER TABLE pms_si_item_product_deposit_movement 
ADD constraint pms_si_item_product_deposit_movement_fk_03
FOREIGN KEY (id_product) REFERENCES pms_product(id);

ALTER TABLE pms_si_item_product_deposit_movement ADD CONSTRAINT
pms_si_item_product_deposit_movement_uk 
UNIQUE (id_sale_invoice_product_deposit_movement,id_sale_invoice_item);


create sequence pms_si_item_product_deposit_movement_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_si_item_product_deposit_movement.id;


/*ALTER TABLE pms_product_deposit_movement ADD CONSTRAINT
pms_product_deposit_movement_uk 
UNIQUE (product_instance_unique_number);

so here a foreign constraint
*/



create table pms_siipdm_product_instances_involved(
id_si_item_product_deposit_movement		bigint not null,
product_instance_unique_number			bigint not null
);


ALTER TABLE pms_siipdm_product_instances_involved 
ADD constraint pms_siipdm_product_instances_involved_fk_01
FOREIGN KEY (id_si_item_product_deposit_movement) 
REFERENCES pms_si_item_product_deposit_movement(id);

ALTER TABLE pms_siipdm_product_instances_involved 
ADD constraint pms_siipdm_product_instances_involved_fk_02
FOREIGN KEY (product_instance_unique_number) 
REFERENCES pms_product_deposit_movement(product_instance_unique_number);

ALTER TABLE pms_siipdm_product_instances_involved 
ADD CONSTRAINT 
pms_siipdm_product_instances_involved_id_pk 
PRIMARY KEY (id_si_item_product_deposit_movement,product_instance_unique_number);


create or replace function p_effectuate_product_deliver
(pproduct_deposit_movement_identifier_number		bigint,
pid_order									bigint,
pid_order_item								bigint,
pid_sale_invoice							bigint,
pid_sale_invoice_item						bigint,
pdeliver_quantity							bigint,
plast_modif_user 		   					varchar)
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
	RAISE INFO 'executing p_effectuate_product_deliver';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vsale_invoice_item_record				record;
		vsale_i_item_remain_exigible_qty		bigint;
	begin
		/*validate quantity*/
		select * into vsale_invoice_item_record from pms_sale_invoice_item where id = pid_sale_invoice_item;
		vsale_i_item_remain_exigible_qty := vsale_invoice_item_record.product_exigible_by_invoice_quantity;
		if pdeliver_quantity > vsale_i_item_remain_exigible_qty then
			declare
				vproduct_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.product.deliver.requested.quantity.greater.than.remain.exigible.quantity.error'||''||vproduct_record.product_id||''||vsale_i_item_remain_exigible_qty||''||pdeliver_quantity||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if pdeliver_quantity > vsale_invoice_item_record.product_stock_quantity then
			declare
				vproduct_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.product.deliver.requested.quantity.greater.than.stock.quantity.error'||''||vproduct_record.product_id||''||vsale_invoice_item_record.product_stock_quantity||''||pdeliver_quantity||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if pdeliver_quantity > vsale_invoice_item_record.product_stock_quantity then

		if pdeliver_quantity <= 0 then
			declare
				vproduct_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.product.deliver.requested.quantity.invalid.value.error'||''||vproduct_record.product_id||''||pdeliver_quantity||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if pdeliver_quantity > vsale_invoice_item_record.product_stock_quantity then

		if pdeliver_quantity is null then
			declare
				vproduct_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.product.deliver.requested.quantity.null.value.error'||''||vproduct_record.product_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if pdeliver_quantity > vsale_invoice_item_record.product_stock_quantity then

		/*idenfify the pms_product_deposit_movement to departure*/
		declare
			vproduct_deposit_movement_cursor cursor for
			select * from pms_product_deposit_movement where
			departure_date is null 
			and id_order = pid_order 
			and id_order_item = pid_order_item 
			and id_sale_invoice = pid_sale_invoice
			and id_sale_invoice_item = pid_sale_invoice_item
			order by income_date asc;
			vdelivered_quantity_counter	bigint;
			v_same_time_deliver_to_all_records		timestamp;
			vproduct_stock_existence_record		record;
		begin
			vdelivered_quantity_counter := 0;
			v_same_time_deliver_to_all_records := now();
			for vproduct_deposit_movement_record in vproduct_deposit_movement_cursor loop
				if vdelivered_quantity_counter < pdeliver_quantity then
					vdelivered_quantity_counter := vdelivered_quantity_counter + 1;
				else
					EXIT;
				end if;
				update pms_product_deposit_movement
				set departure_date = v_same_time_deliver_to_all_records,
				last_modif_user = plast_modif_user, last_modif_date = v_same_time_deliver_to_all_records
				where id = vproduct_deposit_movement_record.id;

				PERFORM p_i_pms_sale_invoice_product_deposit_movement
				(pproduct_deposit_movement_identifier_number,
				vproduct_deposit_movement_record.id_sale_invoice,
				vproduct_deposit_movement_record.id_sale_invoice_item,
				vproduct_deposit_movement_record.id_product,
				pdeliver_quantity,
				vproduct_deposit_movement_record.product_instance_unique_number,
				plast_modif_user);
			end loop;
			--for vproduct_deposit_movement_record in vproduct_deposit_movement_cursor loop

			/* update the sale invoice item */
			update pms_sale_invoice_item 
			set product_delivered_quantity = vsale_invoice_item_record.product_delivered_quantity + pdeliver_quantity,
			product_stock_quantity = vsale_invoice_item_record.product_stock_quantity - pdeliver_quantity,
			product_exigible_by_invoice_quantity = 
			vsale_invoice_item_record.product_exigible_by_invoice_quantity - pdeliver_quantity,
			last_modif_user = plast_modif_user,
			last_modif_date = now()
			where id = vsale_invoice_item_record.id;


			/*update pms_product_stock_existence*/
			select * into vproduct_stock_existence_record from pms_product_stock_existence
			where id_product = vsale_invoice_item_record.id_product;
			update pms_product_stock_existence
			set quantity = vproduct_stock_existence_record.quantity - pdeliver_quantity,
			last_modif_user = plast_modif_user, last_modif_date = now()
			where id = vproduct_stock_existence_record.id;
		end;--begin
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


create or replace function p_i_pms_sale_invoice_product_deposit_movement
(pproduct_deposit_movement_identifier_number		bigint,
pid_sale_invoice							bigint,
pid_sale_invoice_item						bigint,
pid_product								bigint,
pquantity									bigint,
pproduct_instance_unique_number				bigint,
pcreation_user 		   					varchar)
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
	RAISE INFO 'executing p_i_pms_sale_invoice_product_deposit_movement';
	RAISE INFO '--------------------------';
	declare
		vsale_invoice_record						record;
		vsale_invoice_product_deposit_movement_record	record;
		vsi_item_product_deposit_movement				record;
		vproduct_record							record;
		vproduct_instance_counter_verifier				bigint;
	begin
		select * into vsale_invoice_product_deposit_movement_record
		from pms_sale_invoice_product_deposit_movement 
		where product_deposit_movement_identifier_number = pproduct_deposit_movement_identifier_number;

		if vsale_invoice_product_deposit_movement_record.id is null then
			select * into vsale_invoice_record from pms_sale_invoice where id = pid_sale_invoice;
			insert into pms_sale_invoice_product_deposit_movement
			(id,
			id_sale_invoice,
			sale_invoice_identifier_number,
			product_deposit_movement_identifier_number,
			creation_user)
			values
			(nextval('pms_sale_invoice_product_deposit_movement_id_sq'),
			pid_sale_invoice,
			vsale_invoice_record.identifier_number,
			pproduct_deposit_movement_identifier_number,
			pcreation_user);

			select * into vsale_invoice_product_deposit_movement_record
			from pms_sale_invoice_product_deposit_movement 
			where product_deposit_movement_identifier_number = pproduct_deposit_movement_identifier_number;
		end if;--if vsale_invoice_product_deposit_movement_record.id is null then

		select * into vsi_item_product_deposit_movement from pms_si_item_product_deposit_movement
		where id_sale_invoice_product_deposit_movement = vsale_invoice_product_deposit_movement_record.id
		and id_sale_invoice_item = pid_sale_invoice_item;

		if vsi_item_product_deposit_movement.id is null then
			select * into vproduct_record from pms_product where id = pid_product;
			insert into pms_si_item_product_deposit_movement
				(id,
				id_sale_invoice_product_deposit_movement,
				id_sale_invoice_item,
				id_product,
				product_id,
				quantity)
			values
				(nextval('pms_si_item_product_deposit_movement_id_sq'),
				vsale_invoice_product_deposit_movement_record.id,
				pid_sale_invoice_item,
				vproduct_record.id,
				vproduct_record.product_id,
				pquantity);

			select * into vsi_item_product_deposit_movement from pms_si_item_product_deposit_movement
			where id_sale_invoice_product_deposit_movement = vsale_invoice_product_deposit_movement_record.id
			and id_sale_invoice_item = pid_sale_invoice_item;
		end if;--if vsi_item_product_deposit_movement.id is null then

		insert into pms_siipdm_product_instances_involved
		values
		(vsi_item_product_deposit_movement.id,
		pproduct_instance_unique_number);

		select count(*) into vproduct_instance_counter_verifier
		from pms_siipdm_product_instances_involved
		where id_si_item_product_deposit_movement = vsi_item_product_deposit_movement.id;

		if vproduct_instance_counter_verifier > vsi_item_product_deposit_movement.quantity then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vproduct_record from pms_product where id = pid_product;
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.sale.invoice.product.deposit.movement.product.unique.instance.number.counted.greater.than.sale.invoice.item.quantity.specified.error'||''||vproduct_record.product_id||''||vsi_item_product_deposit_movement.quantity||''||vproduct_instance_counter_verifier||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;		
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
