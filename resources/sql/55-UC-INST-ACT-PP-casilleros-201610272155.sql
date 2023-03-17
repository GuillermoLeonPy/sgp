/*55-UC-INST-ACT-PP-casilleros-201610272155*/
--drop table pms_temporary_halfway_product_storage;
create table pms_test_temporary_halfway_product_storage(
locker_number				bigint,
occupied					varchar(1) not null default 'N'
);

ALTER TABLE pms_test_temporary_halfway_product_storage 
ADD constraint chk_occupied CHECK (occupied in ('S', 'N'));

ALTER TABLE pms_test_temporary_halfway_product_storage 
ADD CONSTRAINT pms_test_temporary_halfway_product_storage_pk
PRIMARY KEY (locker_number);

--delete from pms_test_temporary_halfway_product_storage;
insert into pms_test_temporary_halfway_product_storage (locker_number) values (1);
insert into pms_test_temporary_halfway_product_storage (locker_number) values (2);
insert into pms_test_temporary_halfway_product_storage (locker_number) values (3);
insert into pms_test_temporary_halfway_product_storage (locker_number) values (4);
insert into pms_test_temporary_halfway_product_storage (locker_number) values (5);

select * from pms_test_temporary_halfway_product_storage

--drop table pms_temporary_halfway_product_storage_parameters;
create table pms_test_temporary_halfway_product_storage_parameters(
minimun_available_locker_number			bigint not null,
next_to_minimun_available_locker_number		bigint not null,
maximun_occupied_locker_number			bigint,
total_lockers_quantity					bigint not null,
avaliable_lockers_quantity				bigint not null,
lockers_increase_quantity				bigint not null
);


ALTER TABLE pms_test_temporary_halfway_product_storage_parameters 
ADD constraint chk_minimun_available_locker_number CHECK (minimun_available_locker_number > 0);

ALTER TABLE pms_test_temporary_halfway_product_storage_parameters 
ADD constraint chk_next_to_minimun_available_locker_number CHECK (next_to_minimun_available_locker_number > 1);

ALTER TABLE pms_test_temporary_halfway_product_storage_parameters 
ADD constraint chk_avaliable_lockers_quantity CHECK (avaliable_lockers_quantity > 1);

ALTER TABLE pms_test_temporary_halfway_product_storage_parameters 
ADD constraint chk_lockers_increase_quantity CHECK (lockers_increase_quantity > 1);

ALTER TABLE pms_test_temporary_halfway_product_storage_parameters 
ADD constraint chk_total_lockers_quantity CHECK (total_lockers_quantity > 4);

--delete from pms_test_temporary_halfway_product_storage_parameters;
insert into 
pms_test_temporary_halfway_product_storage_parameters
values
(1,2,null,5,5,2);

create or replace function f_test_allocate_halfway_product
()
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
	RAISE INFO 'executing f_test_allocate_halfway_product';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	declare
		vrecord 			record;

		v_actual_minimun_available_locker_number			bigint;
		v_actual_next_to_minimun_available_locker_number		bigint;
		v_actual_maximun_occupied_locker_number				bigint;
		v_actual_total_lockers_quantity					bigint;
		v_actual_avaliable_lockers_quantity				bigint;
		v_actual_lockers_increase_quantity					bigint;

		v_next_minimun_available_locker_number			bigint;
		v_next_next_to_minimun_available_locker_number		bigint;
		v_next_maximun_occupied_locker_number				bigint;
		v_next_total_lockers_quantity					bigint;
		v_next_avaliable_lockers_quantity				bigint;
	begin
		select par.* into vrecord from pms_test_temporary_halfway_product_storage_parameters par;

		v_actual_minimun_available_locker_number			:= vrecord.minimun_available_locker_number;
		v_actual_next_to_minimun_available_locker_number		:= vrecord.next_to_minimun_available_locker_number;
		v_actual_maximun_occupied_locker_number				:= vrecord.maximun_occupied_locker_number;
		v_actual_total_lockers_quantity					:= vrecord.total_lockers_quantity;
		v_actual_avaliable_lockers_quantity				:= vrecord.avaliable_lockers_quantity;
		v_actual_lockers_increase_quantity					:= vrecord.lockers_increase_quantity;
		

		if v_actual_maximun_occupied_locker_number is null
		or v_actual_minimun_available_locker_number > v_actual_maximun_occupied_locker_number then

			if v_actual_total_lockers_quantity = (v_actual_minimun_available_locker_number + 1) then
				for vcounter in 1..v_actual_lockers_increase_quantity loop
					insert into pms_test_temporary_halfway_product_storage 
					(locker_number) values (v_actual_total_lockers_quantity + vcounter);					
				end loop;
				
				v_next_total_lockers_quantity := v_actual_total_lockers_quantity + v_actual_lockers_increase_quantity;
				v_next_avaliable_lockers_quantity = v_actual_lockers_increase_quantity + 1;				
			end if;
			
			v_next_minimun_available_locker_number = v_actual_minimun_available_locker_number + 1;

			if v_actual_maximun_occupied_locker_number != null then
				v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number + 1;
			else
				v_next_maximun_occupied_locker_number := v_actual_minimun_available_locker_number;			
			end if;
			v_next_next_to_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number + 1;

		
		elsif v_actual_minimun_available_locker_number < v_actual_maximun_occupied_locker_number 
		and (v_actual_next_to_minimun_available_locker_number - v_actual_maximun_occupied_locker_number) > 1 then
			
			v_next_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number;
			v_next_next_to_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number + 1;

		elsif v_actual_minimun_available_locker_number < v_actual_maximun_occupied_locker_number 
		and (v_actual_next_to_minimun_available_locker_number - v_actual_maximun_occupied_locker_number) > 1 then
			
			v_next_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number;
			v_next_next_to_minimun_available_locker_number := v_actual_maximun_occupied_locker_number + 1;			
		end if;

		if v_next_avaliable_lockers_quantity is null then
			v_next_avaliable_lockers_quantity := v_actual_avaliable_lockers_quantity - 1;
		end if;

		/* if note setted, is because it did not change, set with actual value */
		if v_next_minimun_available_locker_number is null then
			v_next_minimun_available_locker_number := v_actual_minimun_available_locker_number;			
		end if;
		if v_next_next_to_minimun_available_locker_number is null then
			v_next_next_to_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number;
		end if;
		if v_next_maximun_occupied_locker_number is null then
			v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number;
		end if;
		if v_next_total_lockers_quantity is null then
			v_next_total_lockers_quantity := v_actual_total_lockers_quantity;
		end if;
		
		update pms_test_temporary_halfway_product_storage_parameters
		set
			minimun_available_locker_number 		= v_next_minimun_available_locker_number,
			next_to_minimun_available_locker_number = v_next_next_to_minimun_available_locker_number,
			maximun_occupied_locker_number		= v_next_maximun_occupied_locker_number,
			total_lockers_quantity				= v_next_total_lockers_quantity,
			avaliable_lockers_quantity			= v_next_avaliable_lockers_quantity;

		update pms_test_temporary_halfway_product_storage
			set occupied = 'S' where locker_number = v_actual_minimun_available_locker_number;
			
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create or replace function f_test_empty_locker
(p_locker_number bigint)
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
	RAISE INFO 'executing f_test_empty_locker';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	if p_locker_number is null then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.f.test.empty.locker.no.locker.number.specified.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	declare
		vrecord 			record;

		v_actual_minimun_available_locker_number			bigint;
		v_actual_next_to_minimun_available_locker_number		bigint;
		v_actual_maximun_occupied_locker_number				bigint;
		v_actual_avaliable_lockers_quantity				bigint;
		v_actual_total_lockers_quantity					bigint;

		v_next_minimun_available_locker_number			bigint;
		v_next_next_to_minimun_available_locker_number		bigint;
		v_next_maximun_occupied_locker_number				bigint;
		v_next_avaliable_lockers_quantity				bigint;
		
		v_locker_number							bigint;
		v_occupied								varchar(1);
	begin
		select l.locker_number into v_locker_number from pms_test_temporary_halfway_product_storage l
		where l.locker_number = p_locker_number;
		if v_locker_number is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.f.test.empty.locker.specified.locker.number.does.not.exists.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		select l.occupied into v_occupied from pms_test_temporary_halfway_product_storage l
		where l.locker_number = v_locker_number;
		if v_occupied = 'N' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.f.test.empty.locker.specified.locker.number.already.empty.error'||''||v_locker_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		
		select par.* into vrecord from pms_test_temporary_halfway_product_storage_parameters par;

		v_actual_minimun_available_locker_number			:= vrecord.minimun_available_locker_number;
		v_actual_next_to_minimun_available_locker_number		:= vrecord.next_to_minimun_available_locker_number;
		v_actual_maximun_occupied_locker_number				:= vrecord.maximun_occupied_locker_number;
		v_actual_avaliable_lockers_quantity				:= vrecord.avaliable_lockers_quantity;
		v_actual_total_lockers_quantity					:= vrecord.total_lockers_quantity;

		if p_locker_number < v_actual_minimun_available_locker_number then
			
			v_next_minimun_available_locker_number := p_locker_number;
			v_next_next_to_minimun_available_locker_number := v_actual_minimun_available_locker_number;
		
		elsif p_locker_number > v_actual_minimun_available_locker_number
		and p_locker_number < v_actual_next_to_minimun_available_locker_number then
			
			v_next_next_to_minimun_available_locker_number := p_locker_number;
		
		elsif p_locker_number > v_actual_minimun_available_locker_number
		and p_locker_number > v_actual_next_to_minimun_available_locker_number
		and p_locker_number = v_actual_maximun_occupied_locker_number
		and ((v_actual_maximun_occupied_locker_number - v_actual_next_to_minimun_available_locker_number) > 1) then

			v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number - 1;

		elsif p_locker_number > v_actual_minimun_available_locker_number
		and p_locker_number > v_actual_next_to_minimun_available_locker_number
		and p_locker_number = v_actual_maximun_occupied_locker_number
		and ((v_actual_maximun_occupied_locker_number - v_actual_next_to_minimun_available_locker_number) = 1) then

			v_next_maximun_occupied_locker_number := v_actual_next_to_minimun_available_locker_number - 1;
		
		end if;


		v_next_avaliable_lockers_quantity := v_actual_avaliable_lockers_quantity + 1;
		
		/* if note setted, is because it did not change, set with actual value */
		if v_next_minimun_available_locker_number is null then
			v_next_minimun_available_locker_number := v_actual_minimun_available_locker_number;			
		end if;
		if v_next_next_to_minimun_available_locker_number is null then
			v_next_next_to_minimun_available_locker_number := v_actual_next_to_minimun_available_locker_number;
		end if;
		
		if v_next_maximun_occupied_locker_number is null 
		and v_next_avaliable_lockers_quantity < v_actual_total_lockers_quantity then
			
			v_next_maximun_occupied_locker_number := v_actual_maximun_occupied_locker_number;
		
		else			
			
			v_next_maximun_occupied_locker_number := null;
			
		end if;

		update pms_test_temporary_halfway_product_storage_parameters
		set	
			minimun_available_locker_number 		= v_next_minimun_available_locker_number,
			next_to_minimun_available_locker_number = v_next_next_to_minimun_available_locker_number,
			maximun_occupied_locker_number		= v_next_maximun_occupied_locker_number,
			avaliable_lockers_quantity			= v_next_avaliable_lockers_quantity;

		update pms_test_temporary_halfway_product_storage
			set occupied = 'N' where locker_number = p_locker_number;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

select * from pms_test_temporary_halfway_product_storage_parameters;
select * from pms_test_temporary_halfway_product_storage order by locker_number;
select f_test_allocate_halfway_product();
select f_test_empty_locker(1);