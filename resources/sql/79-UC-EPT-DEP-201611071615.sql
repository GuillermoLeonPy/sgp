/*79-UC-EPT-DEP-201611071615*/

create table pms_product_deposit_movement(
id	   		 		   				bigint,
id_production_activity_instance   			bigint NOT NULL,--from activity instance HISTORY TABLE
id_person		 						bigint not null,--from activity instance HISTORY TABLE
id_order								bigint NOT NULL,--from activity instance HISTORY TABLE
id_order_item							bigint NOT NULL,--from activity instance HISTORY TABLE
id_product							bigint not null,--from activity instance HISTORY TABLE
id_sale_invoice						bigint not null,
id_sale_invoice_item					bigint not null,
--the sale invoice document by this product instance can be retired
product_instance_unique_number			bigint NOT NULL,--from activity instance HISTORY TABLE,
income_date						timestamp not null default now(),
departure_date						timestamp,
departure_blocked					varchar(1) NOT NULL default 'N',
departure_block_date				timestamp,
id_credit_note						bigint,
id_credit_note_item					bigint,
--id_credit_note: reference to the credit note that blocked the departure of the product
creation_user						varchar(50),
creation_date						timestamp not null default now(),
last_modif_user					varchar(50),
last_modif_date					timestamp
);

/* to modify a column setted as not null, to accept null values
ALTER TABLE mytable ALTER COLUMN mycolumn DROP NOT NULL;
*/

ALTER TABLE pms_product_deposit_movement ADD CONSTRAINT
pms_product_deposit_movement_uk_01 UNIQUE (id_production_activity_instance);

ALTER TABLE pms_product_deposit_movement 
ADD constraint chk_departure_blocked CHECK (departure_blocked in ('S', 'N'));

ALTER TABLE pms_product_deposit_movement ADD CONSTRAINT 
chk_departure_date CHECK (departure_date > income_date);

ALTER TABLE pms_product_deposit_movement ADD CONSTRAINT 
chk_departure_block_date CHECK (departure_block_date > income_date);

ALTER TABLE pms_product_deposit_movement 
ADD CONSTRAINT pms_product_deposit_movement_id_pk
PRIMARY KEY (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_01
FOREIGN KEY (id_production_activity_instance) 
REFERENCES pms_production_activity_instance_history (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_02
FOREIGN KEY (id_person) REFERENCES pms_person (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_03
FOREIGN KEY (id_order) REFERENCES pms_order (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_04
FOREIGN KEY (id_order_item) REFERENCES pms_order_item (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_05
FOREIGN KEY (id_product) 
REFERENCES pms_product (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_06
FOREIGN KEY (id_sale_invoice) REFERENCES pms_sale_invoice (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_07 
FOREIGN KEY (id_sale_invoice_item) 
REFERENCES pms_sale_invoice_item (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_08
FOREIGN KEY (id_credit_note) 
REFERENCES pms_credit_note (id);

ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_09 
FOREIGN KEY (id_credit_note_item) 
REFERENCES pms_credit_note_item (id);

/*
ALTER TABLE pms_product_deposit_movement 
ADD constraint pms_product_deposit_movement_fk_10 
FOREIGN KEY (product_instance_unique_number) 
REFERENCES pms_production_activity_instance_history (product_instance_unique_number);

Error: ERROR: there is no unique constraint matching given keys for referenced table 
"pms_production_activity_instance_history"
SQLState:  42830
ErrorCode: 0

*/

ALTER TABLE pms_product_deposit_movement ADD CONSTRAINT
pms_product_deposit_movement_uk 
UNIQUE (product_instance_unique_number);

create sequence pms_product_deposit_movement_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_product_deposit_movement.id;

create table pms_product_stock_existence(
id	   		 		   				bigint,
id_product							bigint not null,
quantity								bigint not null default 1,
creation_user						varchar(50),
creation_date						timestamp not null default now(),
last_modif_user					varchar(50),
last_modif_date					timestamp
);

ALTER TABLE pms_product_stock_existence 
ADD constraint chk_quantity CHECK (quantity >= 0);

ALTER TABLE pms_product_stock_existence ADD CONSTRAINT
pms_product_stock_existence_uk_01 UNIQUE (id_product);

ALTER TABLE pms_product_stock_existence 
ADD CONSTRAINT 
pms_product_stock_existence_id_pk PRIMARY KEY (id);

ALTER TABLE pms_product_stock_existence 
ADD constraint pms_product_stock_existence_fk_01
FOREIGN KEY (id_product) 
REFERENCES pms_product (id);

create sequence pms_product_stock_existence_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_product_stock_existence.id;


create or replace function p_i_pms_product_deposit_movement
(
pid_production_activity_instance			 	bigint,
pcreation_user								varchar
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
	RAISE INFO 'executing p_i_pms_product_deposit_movement';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vproduction_activity_instance_record				record;
		vproduction_activity_instance_history_record			record;
		vsale_invoice_record							record;
		vsale_invoice_item_record						record;
		vorder_item_record								record;
		vproduct_stock_existence_record					record;
	begin
		select * into vproduction_activity_instance_record 
		from pms_production_activity_instance
		where id = pid_production_activity_instance;

		if vproduction_activity_instance_record.delivers_product_instance != 'S' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.activity.does.not.delivers.product.instance.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||vproduction_activity_instance_record.status||''||'#-key-#'||vproduction_activity_instance_record.next_status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if vproduction_activity_instance_record.status != 'application.common.status.finalized' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin			
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.activity.not.in.required.status.to.deliver.product.instance.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.finalized'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		/*move the pms_production_activity_instance record to history*/
		PERFORM p_activity_instance_create_history_records(vproduction_activity_instance_record.id);		

		select * into vproduction_activity_instance_history_record 
		from pms_production_activity_instance_history
		where id = vproduction_activity_instance_record.id;
		
		/*update the pms_production_activity_instance_history record*/
		update pms_production_activity_instance_history
		set 			
			status = vproduction_activity_instance_history_record.next_status,
			previous_status = vproduction_activity_instance_history_record.status,
			next_status = null, 
			last_modif_user = pcreation_user, 
			last_modif_date = now()		
		where id = vproduction_activity_instance_history_record.id;	
				
		select * into vsale_invoice_record
		from pms_sale_invoice where id_order = vproduction_activity_instance_history_record.id_order
		and status != 'application.common.status.annulled';

		select * into vsale_invoice_item_record
		from pms_sale_invoice_item 
		where id_order_item = vproduction_activity_instance_history_record.id_order_item
		and id_sale_invoice = vsale_invoice_record.id
		and id_product = vproduction_activity_instance_history_record.id_product;


		insert into pms_product_deposit_movement
		(id,
		id_production_activity_instance,--from activity instance HISTORY TABLE
		id_person,--from activity instance HISTORY TABLE
		id_order,--from activity instance HISTORY TABLE
		id_order_item,--from activity instance HISTORY TABLE
		id_product,--from activity instance HISTORY TABLE
		id_sale_invoice,
		id_sale_invoice_item,
		--the sale invoice document by this product instance can be retired
		product_instance_unique_number,--from activity instance HISTORY TABLE,
		creation_user)
		values
		(nextval('pms_product_deposit_movement_id_sq'),
		vproduction_activity_instance_history_record.id,
		vproduction_activity_instance_history_record.id_person,
		vproduction_activity_instance_history_record.id_order,
		vproduction_activity_instance_history_record.id_order_item,
		vproduction_activity_instance_history_record.id_product,
		vsale_invoice_record.id,
		vsale_invoice_item_record.id,
		vproduction_activity_instance_history_record.product_instance_unique_number,
		pcreation_user);

		/*		VERY IMPORTANT: 
				--------------
		UPDATE THE ORDER ITEM
			pms_order_item.finished_quantity
			pms_order_item.in_progress_quantity*/

		select * into vorder_item_record from pms_order_item 
		where id = vproduction_activity_instance_history_record.id_order_item;

		update pms_order_item
		set finished_quantity = vorder_item_record.finished_quantity + 1,
		in_progress_quantity = vorder_item_record.in_progress_quantity - 1,
		last_modif_user = pcreation_user, last_modif_date = now()
		where id = vorder_item_record.id;

		update pms_sale_invoice_item 
		set 
		product_stock_quantity = vsale_invoice_item_record.product_stock_quantity + 1,
		last_modif_user =  pcreation_user, last_modif_date = now()
		where id = vsale_invoice_item_record.id;

		select * into vproduct_stock_existence_record from pms_product_stock_existence
		where id_product = vsale_invoice_item_record.id_product;

		if vproduct_stock_existence_record.id is null then
			insert into pms_product_stock_existence (id,id_product,creation_user)
			values (nextval('pms_product_stock_existence_id_sq'),vsale_invoice_item_record.id_product,pcreation_user);
		else
			update pms_product_stock_existence
			set quantity = vproduct_stock_existence_record.quantity + 1,
			last_modif_user =  pcreation_user, last_modif_date = now()
			where id = vproduct_stock_existence_record.id;
		end if;
		
		/* this operations will be applyed in trigger: trigger_au_pms_order_item
		CHECK IF APPLY TO MODIFY THE ORDER_ITEM STATUS
		CHECK IF APPLY TO MODIFY THE ORDER STATUS		*/		
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



create or replace function f_trigger_au_pms_order_item()
returns trigger as
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
	RAISE INFO 'executing f_trigger_au_pms_order_item';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vid_order_item								bigint;
		vorder_item_record							record;
		vorder_item_sum_record						record;
		vorder_record								record;
	begin
		vid_order_item := new.id;
		
		select * into vorder_item_record from pms_order_item
		where id = vid_order_item;

		select 
		sum(i.in_progress_quantity + i.pending_to_instanciate_quantity) sum_in_progress_pending_to_instanciate,
		sum(i.finished_quantity) sum_finished_quantity
		into	vorder_item_sum_record
		from pms_order_item i where i.status != 'application.common.status.discarded' and i.id_order = vorder_item_record.id_order;		
		
		if vorder_item_sum_record.sum_in_progress_pending_to_instanciate = 0
		and vorder_item_sum_record.sum_finished_quantity > 0 then
			select * into vorder_record from pms_order where id = vorder_item_record.id_order;
			update pms_order
			set status = 'application.common.status.finalized',
			previous_status = vorder_record.status,
			last_modif_user = vorder_item_record.last_modif_user, last_modif_date = now()
			where id = vorder_record.id;
		elsif vorder_item_sum_record.sum_in_progress_pending_to_instanciate = 0
		and vorder_item_sum_record.sum_finished_quantity = 0 then
			select * into vorder_record from pms_order where id = vorder_item_record.id_order;
			update pms_order
			set status = 'application.common.status.finalized.by.document',
			previous_status = vorder_record.status,
			last_modif_user = vorder_item_record.last_modif_user, last_modif_date = now()
			where id = vorder_record.id;			
		end if;
	RETURN NEW;
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


create trigger trigger_au_pms_order_item
AFTER UPDATE OF 
finished_quantity,in_progress_quantity,pending_to_instanciate_quantity,canceled_entering_production_by_document_quantity
 ON pms_order_item FOR EACH ROW 
EXECUTE PROCEDURE f_trigger_au_pms_order_item();
