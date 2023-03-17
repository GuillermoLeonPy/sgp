/*UC-REG-UNI-MED*/

create table pms_measurment_unit(
id	   		 		   bigint,
measurment_unit_id	 		   varchar(100) NOT NULL,
measurment_unit_description	 	varchar(250)  NOT NULL,
creation_user	 		   varchar(50) ,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);

--ALTER TABLE pms_measurment_unit ALTER COLUMN measurment_unit_id TYPE varchar(100);
ALTER TABLE pms_measurment_unit ADD CONSTRAINT pms_measurment_unit_id_pk PRIMARY KEY (id);

ALTER TABLE pms_measurment_unit ADD CONSTRAINT pms_measurment_unit_measurment_unit_id_uk UNIQUE (measurment_unit_id);

create sequence pms_measurment_unit_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_measurment_unit.id;

create or replace function p_i_pms_measurment_unit
(
pid	   		 		   bigint,
pmeasurment_unit_id		 		varchar,
pmeasurment_unit_description	  	varchar,
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
	RAISE INFO 'executing p_i_pms_measurment_unit';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pmeasurment_unit_id is null or length(trim(pmeasurment_unit_id))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.measurmentunitdao.measurmentunitdto.measurmentunitid.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (pmeasurment_unit_description is null or length(trim(pmeasurment_unit_description))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.measurmentunitdao.measurmentunitdto.measurmentunitdescription.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	declare
		vid					pms_measurment_unit.id%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select id into vid
		from pms_measurment_unit
		where measurment_unit_id = upper(trim(pmeasurment_unit_id));

		if vid is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.measurmentunitdao.measurmentunitdto.measurmentunitid.already.exists.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;

	/*END OF VALIDATIONS*/

	insert into pms_measurment_unit
			(id,measurment_unit_id,measurment_unit_description,creation_user,creation_date)
	values
			(pid,
			upper(trim(pmeasurment_unit_id)),
			upper(trim(pmeasurment_unit_description)),
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
