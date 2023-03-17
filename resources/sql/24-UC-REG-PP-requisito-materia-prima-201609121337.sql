/*UC-REG-PP-requisito-materia-prima*/

create table pms_raw_material_requirement(
id	   		 		   			bigint,
id_raw_material	 		   		bigint NOT NULL,
id_measurment_unit	 				bigint NOT NULL,
id_production_process_activity		bigint NOT NULL,
is_active							varchar(1) default 'S',
quantity   						numeric(11,2) NOT NULL,
registration_date		   			timestamp NOT NULL default now(),
validity_end_date		   			timestamp,
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);


--ALTER TABLE pms_raw_material_requirement ADD is_active varchar(1) default 'S'
ALTER TABLE pms_raw_material_requirement ADD CHECK (is_active in ('S'));

ALTER TABLE pms_raw_material_requirement ADD CHECK (quantity > 0);
ALTER TABLE pms_raw_material_requirement ADD CHECK (validity_end_date > registration_date);

ALTER TABLE pms_raw_material_requirement ADD CONSTRAINT pms_raw_material_requirement_id_pk PRIMARY KEY (id);


/*
ALTER TABLE pms_raw_material_requirement drop CONSTRAINT pms_raw_material_requirement_uk_01
ALTER TABLE pms_raw_material_requirement ADD CONSTRAINT
pms_raw_material_requirement_uk_01 UNIQUE 
(id_raw_material,id_measurment_unit,id_production_process_activity);*/


ALTER TABLE pms_raw_material_requirement ADD CONSTRAINT
pms_raw_material_requirement_uk_01 UNIQUE 
(id_raw_material,id_measurment_unit,id_production_process_activity,is_active);

ALTER TABLE pms_raw_material_requirement 
ADD constraint pms_raw_material_requirement_fk_01
FOREIGN KEY (id_production_process_activity) REFERENCES pms_production_process_activity (id);

ALTER TABLE pms_raw_material_requirement 
ADD constraint pms_raw_material_requirement_fk_02
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_raw_material_requirement 
ADD constraint pms_raw_material_requirement_fk_03
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);


create sequence pms_raw_material_requirement_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_raw_material_requirement.id;


create or replace function f_pms_raw_material_requirement_id_sq
(pid	   		 		   bigint)
returns bigint as
$BODY$
declare
	vsn	bigint;
begin
	select nextval('pms_raw_material_requirement_id_sq') into vsn;
	return vsn;
end;
$BODY$
LANGUAGE 'plpgsql';

create or replace function p_i_pms_raw_material_requirement
(
pid	   		 		   			bigint,
pid_raw_material					bigint,
pid_measurment_unit	   			 	bigint,
pid_production_process_activity		bigint,
pquantity 			   			numeric,
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
	RAISE INFO 'executing p_i_pms_raw_material_requirement';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	if (pquantity is null or pquantity<=0)
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.raw.material.requirementdao.raw.material.requirementdto.quantity.required.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;	

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select rm.raw_material_id, mu.measurment_unit_id, ppa.activity_id 
		into vrecord
		from 
			pms_raw_material_requirement rmr, 
			pms_raw_material rm, 
			pms_measurment_unit mu,
			pms_production_process_activity ppa
		where 
		rm.id = pid_raw_material
		and mu.id = pid_measurment_unit
		and rmr.id_production_process_activity = pid_production_process_activity
		and rmr.id_raw_material = rm.id
		and rmr.id_measurment_unit = mu.id
		and rmr.id_production_process_activity = ppa.id
		and rmr.is_active = 'S';

		if vrecord.raw_material_id is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.raw.material.requirementdao.raw.material.requirementdto.rawmaterial.measurmentunit.requirement.already.exists.for.activity.error'||''||vrecord.raw_material_id||''||vrecord.measurment_unit_id||''||vrecord.activity_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';			
		end if;
	end;


	/*END OF VALIDATIONS*/

	pregistration_date:=now();

	insert into pms_raw_material_requirement
			(id,
			id_raw_material,
			id_measurment_unit,
			id_production_process_activity,
			quantity,
			registration_date,
			creation_user,
			creation_date)
	values
			(pid,
			pid_raw_material,
			pid_measurment_unit,
			pid_production_process_activity,
			pquantity,
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



