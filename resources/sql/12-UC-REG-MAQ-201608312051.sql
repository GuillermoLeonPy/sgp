/*UC-REG-MAQ*/

create table pms_machine(
id	   		 		   bigint,
machine_id	 		   varchar(50) NOT NULL,
machine_description	 	varchar(250)  NOT NULL,
creation_user	 		   varchar(50) ,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);

ALTER TABLE pms_machine ALTER COLUMN machine_id TYPE varchar(100);
ALTER TABLE pms_machine ADD CONSTRAINT pms_machine_id_pk PRIMARY KEY (id);

ALTER TABLE pms_machine ADD CONSTRAINT pms_machine_machine_id_uk UNIQUE (machine_id);

create sequence pms_machine_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_machine.id;

create or replace function p_i_pms_machine
(
pid	   		 		   bigint,
pmachine_id		 		varchar,
pmachine_description	  	varchar,
pcreation_user	 		   varchar
)
returns void as
$BODY$
declare
	error_message          varchar(200);
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_machine';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pmachine_id is null or length(trim(pmachine_id))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machinedao.machinedto.machineid.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pmachine_description is null or length(trim(pmachine_description))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machinedao.machinedto.machinedescription.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	declare
		vid					pms_machine.id%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select id into vid
		from pms_machine
		where machine_id = upper(trim(pmachine_id));

		if vid is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machinedao.machinedto.machineid.already.exists.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;

	/*END OF VALIDATIONS*/

	insert into pms_machine
			(id,machine_id,machine_description,creation_user,creation_date)
	values
			(pid,
			upper(trim(pmachine_id)),
			upper(trim(pmachine_description)),
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
