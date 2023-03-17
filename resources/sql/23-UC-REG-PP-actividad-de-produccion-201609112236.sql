/*UC-REG-PP-actividad-de-produccion*/

create table pms_production_process_activity(
id	   		 		   bigint,
activity_id	 		   varchar(100) NOT NULL,
activity_description	 	varchar(250)  NOT NULL,
id_production_process					bigint NOT NULL,
minutes_quantity 			   			bigint not null,
registration_date		   			timestamp NOT NULL default now(),
validity_end_date		   timestamp,
id_previous_activity		bigint,
creation_user	 		   varchar(50),
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);


ALTER TABLE pms_production_process_activity ADD CHECK (minutes_quantity > 0);
ALTER TABLE pms_production_process_activity ADD CHECK (validity_end_date > registration_date);

ALTER TABLE pms_production_process_activity ADD CONSTRAINT pms_production_process_activity_id_pk PRIMARY KEY (id);

ALTER TABLE pms_production_process_activity ADD CONSTRAINT pms_production_process_activity_id_uk UNIQUE (activity_id);


ALTER TABLE pms_production_process_activity 
ADD constraint pms_production_process_activity_fk_01
FOREIGN KEY (id_production_process) REFERENCES pms_production_process (id);

ALTER TABLE pms_production_process_activity 
ADD constraint pms_production_process_activity_fk_02
FOREIGN KEY (id_previous_activity) REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_production_process_activity ADD CONSTRAINT 
pms_production_process_activity_id_previous_activity_uk UNIQUE (id_previous_activity);

ALTER TABLE pms_production_process_activity ADD CHECK (id != id_previous_activity);

create sequence pms_production_process_activity_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_process_activity.id;



create or replace function p_i_pms_production_process_activity
(
pid	   		 		   bigint,
pactivity_id		 		varchar,
pactivity_description	  	varchar,
pminutes_quantity 			   			bigint,
pid_production_process		bigint,
pid_previous_activity		bigint,
pregistration_date	   		inout timestamp,
pcreation_user	 		   varchar
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
	RAISE INFO 'executing p_i_pms_production_process_activity';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pactivity_id is null or length(trim(pactivity_id))=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.process.activitydto.activityid.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pactivity_description is null or length(trim(pactivity_description))=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.process.activitydto.activitydescription.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pminutes_quantity is null or pminutes_quantity<=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.process.activitydto.minutes.quantity.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;	

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select pp.process_id into vrecord
		from pms_production_process_activity ppa, pms_production_process pp
		where ppa.activity_id = upper(trim(pactivity_id))
		and ppa.id_production_process = pid_production_process
		and pp.id = ppa.id_production_process;

		if vrecord.process_id is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.process.activitydto.activityid.already.exists.for.process.error'||''||vrecord.process_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;


	declare
		vid			pms_production_process_activity.id%TYPE;		
	begin
		select id into vid
		from 
			pms_production_process_activity 
		where 
			id_previous_activity = pid_previous_activity;

		if vid is not null then
			update pms_production_process_activity
			set id_previous_activity = null where id = vid;
		end if;

		pregistration_date:=now();
	
		insert into pms_production_process_activity
				(id,
				activity_id,
				activity_description,
				minutes_quantity,
				id_production_process,
				id_previous_activity,
				registration_date,
				creation_user,
				creation_date)
		values
				(pid,
				upper(trim(pactivity_id)),
				upper(trim(pactivity_description)),
				pminutes_quantity,
				pid_production_process,
				pid_previous_activity,
				pregistration_date,
				pcreation_user,
				now());

		if vid is not null then
			update pms_production_process_activity
			set id_previous_activity = pid where id = vid;
		end if;
		
	end;
			
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


create or replace function f_pms_production_process_activity_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('pms_production_process_activity_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';
