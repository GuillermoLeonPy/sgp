/*56-UC-INST-ACT-PP-all-relational-lockers-201611071347*/

create table pms_temporary_halfway_product_storage(
locker_number								bigint not null,
occupied									varchar(1) not null default 'N',
--occupied attribute purpouse: a try to gain performance when finding out
--the minimun available locker number in the f_allocate_halfway_product function
depositor_activity_instance_unique_number		bigint,
recaller_activity_instance_unique_number		bigint
);


--ALTER TABLE pms_temporary_halfway_product_storage add occupied varchar(1) not null default 'N';
ALTER TABLE pms_temporary_halfway_product_storage 
ADD constraint chk_occupied CHECK (occupied in ('S', 'N'));

/* SE PUEDE CREAR UNA CLAVE FORANEA POR UNA COLUMNA CON CLAVE UNICA EN OTRA TABLA Y QUE LA MISMA NO
ES CLAVE PRIMARIA */

--while testing disable constraint
ALTER TABLE pms_temporary_halfway_product_storage 
drop constraint pms_temporary_halfway_product_storage_fk_01;

/*a reference to history table*/

ALTER TABLE pms_temporary_halfway_product_storage 
ADD constraint pms_temporary_halfway_product_storage_fk_01
FOREIGN KEY (depositor_activity_instance_unique_number) 
REFERENCES pms_production_activity_instance_history (activity_instance_unique_number);

--while testing disable constraint
ALTER TABLE pms_temporary_halfway_product_storage 
drop constraint pms_temporary_halfway_product_storage_fk_02;

ALTER TABLE pms_temporary_halfway_product_storage 
ADD constraint pms_temporary_halfway_product_storage_fk_02
FOREIGN KEY (recaller_activity_instance_unique_number) 
REFERENCES pms_production_activity_instance (activity_instance_unique_number);

ALTER TABLE pms_temporary_halfway_product_storage ADD CONSTRAINT
pms_temporary_halfway_product_storage_uk UNIQUE (recaller_activity_instance_unique_number);

ALTER TABLE pms_temporary_halfway_product_storage ADD CONSTRAINT
pms_temporary_halfway_product_storage_uk_02 UNIQUE (depositor_activity_instance_unique_number);

ALTER TABLE pms_temporary_halfway_product_storage 
ADD CONSTRAINT pms_temporary_halfway_product_storage_pk
PRIMARY KEY (locker_number);
/* ***************************************** */
/* CONSTRAINT CHECK CON OPERACION ARITMETICA */
/* ***************************************** */
ALTER TABLE pms_temporary_halfway_product_storage 
ADD constraint chk_recaller_activity_instance_unique_number 
CHECK (recaller_activity_instance_unique_number - depositor_activity_instance_unique_number = 1);

--delete from pms_temporary_halfway_product_storage;
insert into pms_temporary_halfway_product_storage (locker_number) values (1);
insert into pms_temporary_halfway_product_storage (locker_number) values (2);
insert into pms_temporary_halfway_product_storage (locker_number) values (3);
insert into pms_temporary_halfway_product_storage (locker_number) values (4);
insert into pms_temporary_halfway_product_storage (locker_number) values (5);


create table pms_temporary_halfway_product_storage_parameters(
minimun_available_locker_number			bigint not null,
maximun_occupied_locker_number			bigint,
total_lockers_quantity					bigint not null,
avaliable_lockers_quantity				bigint not null,
lockers_increase_quantity				bigint not null
);

ALTER TABLE pms_temporary_halfway_product_storage_parameters 
ADD constraint chk_minimun_available_locker_number 
CHECK (minimun_available_locker_number > 0);

ALTER TABLE pms_temporary_halfway_product_storage_parameters 
ADD constraint chk_avaliable_lockers_quantity 
CHECK (avaliable_lockers_quantity > 1);

ALTER TABLE pms_temporary_halfway_product_storage_parameters 
ADD constraint chk_lockers_increase_quantity 
CHECK (lockers_increase_quantity > 1);

--delete from pms_temporary_halfway_product_storage_parameters;
insert into 
pms_temporary_halfway_product_storage_parameters
values
(1,null,5,5,2);

create or replace function f_allocate_halfway_product
(pactivity_instance_unique_number		bigint,
pallocator_user varchar)
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
	RAISE INFO 'executing f_allocate_halfway_product';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vrecord										record;
		vproduction_activity_instance_record				record;
		
		v_actual_minimun_available_locker_number			bigint;
		v_actual_maximun_occupied_locker_number				bigint;
		v_actual_total_lockers_quantity					bigint;
		v_actual_avaliable_lockers_quantity				bigint;
		v_actual_lockers_increase_quantity					bigint;

		v_next_minimun_available_locker_number			bigint;
		v_next_maximun_occupied_locker_number				bigint;
		v_next_total_lockers_quantity					bigint;
		v_next_avaliable_lockers_quantity				bigint;
	begin

		select * into vproduction_activity_instance_record 
		from pms_production_activity_instance where activity_instance_unique_number = pactivity_instance_unique_number;

		if vproduction_activity_instance_record.delivers_partial_result != 'S' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.allocate.halfway.product.activity.does.not.deliver.halfway.product.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||vproduction_activity_instance_record.status||''||'#-key-#'||vproduction_activity_instance_record.next_status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;


		if vproduction_activity_instance_record.status != 'application.common.status.finalized' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.allocate.halfway.product.activity.not.in.required.status.to.allocate.halfway.product.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.finalized'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

	
		select p.* into vrecord from pms_temporary_halfway_product_storage_parameters p;

		v_actual_minimun_available_locker_number			:= vrecord.minimun_available_locker_number;
		v_actual_maximun_occupied_locker_number				:= vrecord.maximun_occupied_locker_number;
		v_actual_total_lockers_quantity					:= vrecord.total_lockers_quantity;
		v_actual_avaliable_lockers_quantity				:= vrecord.avaliable_lockers_quantity;
		v_actual_lockers_increase_quantity					:= vrecord.lockers_increase_quantity;

		PERFORM p_activity_instance_create_history_records(vproduction_activity_instance_record.id);

		update pms_temporary_halfway_product_storage
		set depositor_activity_instance_unique_number = pactivity_instance_unique_number,
		recaller_activity_instance_unique_number = pactivity_instance_unique_number + 1,
		occupied = 'S'
		where locker_number = v_actual_minimun_available_locker_number;

		select min(locker_number) into v_next_minimun_available_locker_number
		from pms_temporary_halfway_product_storage where
		occupied = 'N';

		if v_actual_maximun_occupied_locker_number is null 
		or v_actual_maximun_occupied_locker_number < v_actual_minimun_available_locker_number then
			
			v_next_maximun_occupied_locker_number := v_actual_minimun_available_locker_number;
			
		end if;
		
		if v_actual_total_lockers_quantity = v_next_minimun_available_locker_number then
			for vcounter in 1..v_actual_lockers_increase_quantity loop
				insert into pms_temporary_halfway_product_storage (locker_number) 
				values (v_actual_total_lockers_quantity + vcounter);
			end loop;
			v_next_total_lockers_quantity := v_actual_total_lockers_quantity + v_actual_lockers_increase_quantity;
			v_next_avaliable_lockers_quantity := v_actual_lockers_increase_quantity + 1;
		end if;

		if v_next_avaliable_lockers_quantity is null then
			v_next_avaliable_lockers_quantity := v_actual_avaliable_lockers_quantity - 1;
		end if;
		if v_next_total_lockers_quantity is null then
			v_next_total_lockers_quantity := v_actual_total_lockers_quantity;
		end if;
		if v_next_maximun_occupied_locker_number is null then
			v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number;
		end if;

		update pms_temporary_halfway_product_storage_parameters
		set
			minimun_available_locker_number 		= v_next_minimun_available_locker_number,
			maximun_occupied_locker_number		= v_next_maximun_occupied_locker_number,
			total_lockers_quantity				= v_next_total_lockers_quantity,
			avaliable_lockers_quantity			= v_next_avaliable_lockers_quantity;

		update pms_production_activity_instance_history
		set 
			occupied_locker_number = v_actual_minimun_available_locker_number,
			partial_result_delivery_date = now(), 
			status = vproduction_activity_instance_record.next_status,
			previous_status = vproduction_activity_instance_record.status,
			next_status = null, 
			last_modif_user = pallocator_user, 
			last_modif_date = now()		
		where id = vproduction_activity_instance_record.id;

		select * into vproduction_activity_instance_record 
		from pms_production_activity_instance 
		where 
		require_parcial_product_recall = 'S'
		and activity_instance_unique_number = pactivity_instance_unique_number + 1;

		if vproduction_activity_instance_record.id is not null then
			update pms_production_activity_instance
			set 
				recall_locker_number = v_actual_minimun_available_locker_number, 
				is_asignable = 'S',
				last_modif_user = pallocator_user, 
				last_modif_date = now()						
			where id = vproduction_activity_instance_record.id;
		end if;

		return v_actual_minimun_available_locker_number;
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


create or replace function f_empty_locker
(p_locker_number bigint,
p_activity_instance_unique_number	bigint,
p_deallocator_user varchar)
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
	RAISE INFO 'executing f_empty_locker';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	if p_locker_number is null or p_activity_instance_unique_number is null then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.f.empty.locker.no.locker.number.or.activity.instance.unique.number.specified.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	declare
		vrecord 			record;
		vparams_record 			record;
		vproduction_activity_instance_record				record;

		v_actual_minimun_available_locker_number			bigint;
		v_actual_maximun_occupied_locker_number				bigint;
		v_actual_avaliable_lockers_quantity				bigint;
		v_actual_total_lockers_quantity					bigint;

		v_next_minimun_available_locker_number			bigint;
		v_next_maximun_occupied_locker_number				bigint;
		v_next_avaliable_lockers_quantity				bigint;
	begin

		select * into vproduction_activity_instance_record 
		from pms_production_activity_instance where activity_instance_unique_number = p_activity_instance_unique_number;

		if vproduction_activity_instance_record.require_parcial_product_recall != 'S' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.empty.locker.activity.does.not.require.partial.product.recall.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||vproduction_activity_instance_record.status||''||'#-key-#'||vproduction_activity_instance_record.next_status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if vproduction_activity_instance_record.status != 'application.common.status.assigned' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.empty.locker.activity.not.in.required.status.to.perform.partial.product.recall.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.assigned'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

	
		select l.* into vrecord from pms_temporary_halfway_product_storage l
		where l.locker_number = p_locker_number
		and l.recaller_activity_instance_unique_number = p_activity_instance_unique_number
		and l.occupied = 'S';
		if vrecord.locker_number is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.f.empty.locker.specified.locker.number.and.activity.instance.unique.number.does.not.match.any.occupied.locker.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		update pms_temporary_halfway_product_storage
			set recaller_activity_instance_unique_number = null,
			depositor_activity_instance_unique_number = null,
			occupied = 'N'
		where 
			locker_number = p_locker_number;
			
		select par.* into vparams_record from pms_temporary_halfway_product_storage_parameters par;

		v_actual_minimun_available_locker_number			:= vparams_record.minimun_available_locker_number;
		v_actual_maximun_occupied_locker_number				:= vparams_record.maximun_occupied_locker_number;
		v_actual_avaliable_lockers_quantity				:= vparams_record.avaliable_lockers_quantity;
		v_actual_total_lockers_quantity					:= vparams_record.total_lockers_quantity;

		if p_locker_number < v_actual_minimun_available_locker_number then
			
			v_next_minimun_available_locker_number := p_locker_number;
		else
			v_next_minimun_available_locker_number := v_actual_minimun_available_locker_number;
		end if;

		if p_locker_number = v_actual_maximun_occupied_locker_number then
			select max(locker_number) into v_next_maximun_occupied_locker_number
			from pms_temporary_halfway_product_storage
			where occupied = 'S';
		else
			v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number;
		end if;

		v_next_avaliable_lockers_quantity := v_actual_avaliable_lockers_quantity + 1;

		update pms_temporary_halfway_product_storage_parameters
		set	
			minimun_available_locker_number 		= v_next_minimun_available_locker_number,
			maximun_occupied_locker_number		= v_next_maximun_occupied_locker_number,
			avaliable_lockers_quantity			= v_next_avaliable_lockers_quantity;

		update pms_production_activity_instance
		set 	
			parcial_product_recall_date = now(), 
			status = vproduction_activity_instance_record.next_status,
			previous_status = vproduction_activity_instance_record.status,
			next_status = 'application.common.status.in.progress', 
			last_modif_user = p_deallocator_user, 
			last_modif_date = now()
		where id = vproduction_activity_instance_record.id;
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


select * from pms_temporary_halfway_product_storage_parameters;
select * from pms_temporary_halfway_product_storage order by locker_number;
select f_allocate_halfway_product(9,'xxx');
select f_empty_locker(5,10,'xxx');