/*UC-REG-PP-requisito-maquina*/

create table pms_machine_requirement(
id	   		 					bigint,
id_production_process_activity		bigint NOT NULL,
id_machine						bigint NOT NULL,
is_active							varchar(1) default 'S',
minutes_quantity   					bigint NOT NULL,
registration_date		   			timestamp NOT NULL default now(),
validity_end_date		   			timestamp,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);

--ALTER TABLE pms_machine_requirement ADD is_active varchar(1) default 'S'
ALTER TABLE pms_machine_requirement ADD CHECK (is_active in ('S'));

ALTER TABLE pms_machine_requirement ADD CHECK (minutes_quantity > 0);
ALTER TABLE pms_machine_requirement ADD CHECK (validity_end_date > registration_date);

ALTER TABLE pms_machine_requirement ADD CONSTRAINT pms_machine_requirement_id_pk PRIMARY KEY (id);

/*
ALTER TABLE pms_machine_requirement drop CONSTRAINT pms_machine_requirement_uk_01
ALTER TABLE pms_machine_requirement ADD CONSTRAINT
pms_machine_requirement_uk_01 UNIQUE 
(id_production_process_activity);*/
ALTER TABLE pms_machine_requirement ADD CONSTRAINT
pms_machine_requirement_uk_01 UNIQUE 
(id_production_process_activity,is_active);



ALTER TABLE pms_machine_requirement 
ADD constraint pms_machine_requirement_fk_01
FOREIGN KEY (id_production_process_activity) REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_machine_requirement 
ADD constraint pms_machine_requirement_fk_02
FOREIGN KEY (id_machine) REFERENCES pms_machine (id);

create sequence pms_machine_requirement_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_machine_requirement.id;


create or replace function p_i_pms_machine_requirement
(
pid	   		 		   			bigint,
pid_production_process_activity		bigint,
pid_machine						bigint,
pminutes_quantity 			   			bigint,
pregistration_date	   				inout timestamp,
pcreation_user	 		   			varchar
)
returns timestamp as
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
	RAISE INFO 'executing p_i_pms_machine_requirement';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pminutes_quantity is null or pminutes_quantity<=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machine.requirementdao.machine.requirementdto.minutes.quantity.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;	

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select ppa.activity_id, ppa.minutes_quantity
		into vrecord
		from 
			pms_production_process_activity ppa
		where 
		ppa.id = pid_production_process_activity;

		if vrecord.minutes_quantity < pminutes_quantity then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machine.requirementdao.machine.requirementdto.minutes.quantity.exceed.activity.minutes.error'||''||vrecord.activity_id||''||vrecord.minutes_quantity||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select ppa.activity_id
		into vrecord
		from 
			pms_machine_requirement mr, 
			pms_production_process_activity ppa
		where 
		mr.id_production_process_activity = pid_production_process_activity
		and mr.id_production_process_activity = ppa.id;

		if vrecord.activity_id is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machine.requirementdao.machine.requirementdto.man.power.requirement.already.exists.for.activity.error'||''||vrecord.activity_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;


	/*END OF VALIDATIONS*/

	pregistration_date:=now();

	insert into pms_machine_requirement
			(id,
			id_production_process_activity,
			id_machine,
			minutes_quantity,
			registration_date,
			creation_user,
			creation_date)
	values
			(pid,
			pid_production_process_activity,
			pid_machine,
			pminutes_quantity,
			pregistration_date,
			pcreation_user,
			now());
			
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
