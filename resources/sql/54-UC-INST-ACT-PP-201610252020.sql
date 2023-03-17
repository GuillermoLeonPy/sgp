/*54-UC-INST-ACT-PP-201610252020*/

create table pms_production_activity_instance(
id	   		 		   				bigint,
id_production_activity 		   			bigint NOT NULL,
id_person		 						bigint,--a functionary person
id_order							bigint NOT NULL,
id_order_item							bigint NOT NULL,
id_product							bigint NOT NULL,
require_parcial_product_recall			varchar(1) NOT NULL default 'S',
parcial_product_recall_date					timestamp,
recall_locker_number				bigint,
--only the first production process activity instance does not require partial product recall
is_asignable	 		   				varchar(1) NOT NULL default 'N',
--the (activity_instance_unique_number - 1) must be in partial result delivered status
assignment_date						timestamp,
--the id_person functionary attribute has been setted
delivers_product_instance   				varchar(1) NOT NULL default 'N',
--majority of instantiated activities do not deliver product instance
delivers_partial_result   				varchar(1) NOT NULL default 'S',
partial_result_delivery_date   			timestamp,
occupied_locker_number				bigint,
--majority of instantiated activities deliver partial result
status								varchar(50) not null default 'application.common.status.pending',
previous_status						varchar(50),
--convinient attribute because not all the activities instances pass through the same stages or status
next_status							varchar(50) default 'application.common.status.assigned',
--convinient attribute because not all the activities instances pass through the same stages or status
activity_instance_unique_number			bigint NOT NULL,
product_instance_unique_number			bigint NOT NULL,
instantiation_date						timestamp not null default now(),
activity_start_work_date					timestamp,
--the date when the activity reach the initiated status
activity_finish_work_date				timestamp,
--the date when the activity reach the finalized status
activity_cancellation_date				timestamp,
activity_cancellation_reason_description 	varchar(800),
creation_user	 		   				varchar(50),
creation_date			   				timestamp NOT NULL default now(),
last_modif_user 		   				varchar(50),
last_modif_date		   				timestamp
);

--ALTER TABLE pms_production_activity_instance ADD id_order bigint NOT NULL;
--ALTER TABLE pms_production_activity_instance ADD parcial_product_recall_date timestamp;
--ALTER TABLE pms_production_activity_instance ADD recall_locker_number bigint;
--ALTER TABLE pms_production_activity_instance ADD partial_result_delivery_date timestamp;
--ALTER TABLE pms_production_activity_instance ADD occupied_locker_number bigint;

ALTER TABLE pms_production_activity_instance 
ADD constraint chk_require_parcial_product_recall 
CHECK (require_parcial_product_recall in ('S', 'N'));

ALTER TABLE pms_production_activity_instance 
ADD constraint chk_is_asignable
CHECK (is_asignable in ('S', 'N'));

ALTER TABLE pms_production_activity_instance 
ADD constraint chk_delivers_product_instance
CHECK (delivers_product_instance in ('S', 'N'));

ALTER TABLE pms_production_activity_instance 
ADD constraint chk_delivers_partial_result
CHECK (delivers_partial_result in ('S', 'N'));

ALTER TABLE pms_production_activity_instance ADD CONSTRAINT 
chk_activity_finish_work_date CHECK (activity_finish_work_date > activity_start_work_date);

ALTER TABLE pms_production_activity_instance ADD CONSTRAINT 
chk_activity_start_work_date CHECK (activity_start_work_date > instantiation_date);

ALTER TABLE pms_production_activity_instance ADD CONSTRAINT 
chk_activity_cancellation_date CHECK (activity_cancellation_date > instantiation_date);

ALTER TABLE pms_production_activity_instance 
ADD CONSTRAINT pms_production_activity_instance_id_pk
PRIMARY KEY (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_01
FOREIGN KEY (id_production_activity) 
REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_02
FOREIGN KEY (id_production_activity) 
REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_03
FOREIGN KEY (id_person) 
REFERENCES pms_person (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_04
FOREIGN KEY (id_order_item) 
REFERENCES pms_order_item (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_05
FOREIGN KEY (id_product) 
REFERENCES pms_product (id);

ALTER TABLE pms_production_activity_instance 
ADD constraint pms_production_activity_instance_fk_06
FOREIGN KEY (id_order) 
REFERENCES pms_order (id);

/*SOLO UNA OCURRENCIA DE LA ACTIVIDAD DE PRODUCCION, PARA EL MISMO ITEM DE PEDIDO Y LA INSTANCIA 
DE PRODUCTO */
ALTER TABLE pms_production_activity_instance ADD CONSTRAINT
pms_production_activity_instance_uk_01 UNIQUE 
(id_production_activity,id_order_item,product_instance_unique_number);

ALTER TABLE pms_production_activity_instance ADD CONSTRAINT
pms_production_activity_instance_uk_02 UNIQUE (activity_instance_unique_number);


create sequence pms_production_activity_instance_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_activity_instance.id;

create sequence pms_pai_product_instance_unique_number_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_activity_instance.product_instance_unique_number;

create sequence pms_pai_activity_instance_unique_number_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_activity_instance.activity_instance_unique_number;

select * from f_ordered_production_process_activities(7);

delete from pms_production_activity_instance;

update pms_order 
set status = 'application.common.status.pre.production'
where id = 10;

update pms_order_item set status = 'application.common.status.pre.production'
where id_order = 10 and status != 'application.common.status.discarded';	

create or replace function p_instantiate_production_activities
(
pid_order			 				bigint,
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
	RAISE INFO 'executing p_instantiate_production_activities';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		v_order_item_cursor cursor for
		select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;
		vorder_record							record;
		v_product_instance_unique_number			pms_production_activity_instance.product_instance_unique_number%type;
		vid_production_process					pms_production_process.id%type;
		vrequire_parcial_product_recall			pms_production_activity_instance.require_parcial_product_recall%type;
		vid_production_activity_first				pms_production_activity_instance.id%type;
		vid_production_activity_last				pms_production_activity_instance.id%type;
		vid_production_activity_instance			pms_production_activity_instance.id%type;
		vis_asignable							pms_production_activity_instance.is_asignable%type;
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;

		if vorder_record.status != 'application.common.status.pre.production' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.instantiate.production.activities.order.not.in.required.status.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vorder_record.status != 'application.common.status.pre.production' then

		for vorder_item_record in v_order_item_cursor loop
			vid_production_process := f_check_production_process_integrity(vorder_item_record.id_product);
			for vitem_product_counter in 1..vorder_item_record.quantity loop
				v_product_instance_unique_number := nextval('pms_pai_product_instance_unique_number_sq');
				vid_production_activity_first := null;
				declare
					v_activities_cursor cursor for
					select * from f_ordered_production_process_activities(vid_production_process);
				begin
					for vproduction_activity_record in v_activities_cursor loop
						if vid_production_activity_first is null then
							vid_production_activity_first := vproduction_activity_record.id;
						end if;
						if vid_production_activity_first = vproduction_activity_record.id then
							vrequire_parcial_product_recall := 'N';
							vis_asignable := 'S';
						else
							vrequire_parcial_product_recall := 'S';
							vis_asignable := 'N';
						end if;
						vid_production_activity_instance := nextval('pms_production_activity_instance_id_sq');
						insert into pms_production_activity_instance
						(id,id_production_activity,id_order,id_order_item,
						id_product,require_parcial_product_recall,
						activity_instance_unique_number,product_instance_unique_number,
						is_asignable,
						creation_user)
						values
						(vid_production_activity_instance,vproduction_activity_record.id,vorder_record.id,vorder_item_record.id,
						vorder_item_record.id_product,vrequire_parcial_product_recall,
						nextval('pms_pai_activity_instance_unique_number_sq'),v_product_instance_unique_number,
						vis_asignable,
						pcreation_user);
					end loop;--for vproduction_activity_record in vcursor loop
					--update the last activity instance record to require deliver product instance
					update pms_production_activity_instance 
					set delivers_product_instance = 'S',delivers_partial_result = 'N'
					where id = vid_production_activity_instance; 
				end;--begin
			end loop;--for vitem_product_counter in 1..vorder_item_record.quantity loop			
		end loop;--for vorder_item_record in vcursor loop
		/* UPDATE ORDER AND ORDER ITEM STATUS */
		update pms_order
			set status = 'application.common.status.in.progress',
			last_modif_user = pcreation_user, last_modif_date = now(),
			production_activities_instantiation_date = now()
			where id = vorder_record.id;
		update pms_order_item set status = 'application.common.status.in.progress',
			last_modif_user = pcreation_user, last_modif_date = now() 
			where id_order = vorder_record.id and status != 'application.common.status.discarded';		
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

select f_check_production_process_integrity(4)
select p_instantiate_production_activities(10,'xxx');
select * from f_ordered_production_process_activities(7);;

select * from pms_order where id = 10;
select * from pms_order_item where id = 12--id_order = 10;

select * from pms_production_process where

update pms_production_process set is_enable = 'S';


select f_test_check_production_process_integrity(10);



create or replace function f_test_check_production_process_integrity
(pid_order			 				bigint)
returns void as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;

	vid_production_process					pms_production_process.id%type;
	vcursor cursor for
		select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_test_check_production_process_integrity';
	RAISE INFO '--------------------------';

	for vorder_item_record in vcursor loop	
			RAISE INFO '**************************';
			RAISE INFO 'vorder_item_record.id: %',vorder_item_record.id;
			RAISE INFO 'vorder_item_record.id_product: %',vorder_item_record.id_product;
			RAISE INFO '**************************';
			vid_production_process := f_check_production_process_integrity(vorder_item_record.id_product);
			RAISE INFO '**************************';
			RAISE INFO 'vid_production_process: %',vid_production_process;
			RAISE INFO '**************************';
	end loop;
	

	
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';