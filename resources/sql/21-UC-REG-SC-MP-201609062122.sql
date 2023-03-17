/*UC-REG-SC-MP*/
/*UC-REG-SC-MP*/
create table pms_raw_material_existence(
id	   		 		   			bigint,
id_raw_material					bigint NOT NULL,
id_measurment_unit					bigint NOT NULL,
calculated_quantity   				numeric(11,2) NOT NULL default 0,
limit_calculated_quantity   			numeric(11,2) NOT NULL,
efective_quantity   				numeric(11,2) NOT NULL default 0,
automatic_purchase_request_quantity   	numeric(11,2) NOT NULL,
registration_date		   			timestamp NOT NULL default now(),
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);


ALTER TABLE pms_raw_material_existence ADD CONSTRAINT pms_raw_material_existence_id_pk PRIMARY KEY (id);

ALTER TABLE pms_raw_material_existence ADD CONSTRAINT ppms_raw_material_existence_uk_01 UNIQUE (id_raw_material,id_measurment_unit);

ALTER TABLE pms_raw_material_existence ADD constraint chk_calculated_quantity CHECK (calculated_quantity >= 0);


ALTER TABLE pms_raw_material_existence ADD CHECK (limit_calculated_quantity >= 0);
ALTER TABLE pms_raw_material_existence ADD CHECK (efective_quantity >= 0);
ALTER TABLE pms_raw_material_existence ADD CHECK (automatic_purchase_request_quantity >= 0);

ALTER TABLE pms_raw_material_existence 
ADD constraint pms_raw_material_existence_fk_01
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_raw_material_existence 
ADD constraint pms_raw_material_existence_fk_02
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);


create sequence pms_raw_material_existence_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_raw_material_existence.id;


create or replace function p_i_pms_raw_material_existence
(
pid	   				 		   bigint,
pid_raw_material					bigint,
pid_measurment_unit	   			 	bigint,
plimit_calculated_quantity 			   numeric,
pautomatic_purchase_request_quantity 	   numeric,
pregistration_date					INOUT timestamp,
pcreation_user						varchar
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
	RAISE INFO 'executing p_i_pms_raw_material_existence';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
		if (plimit_calculated_quantity is null or plimit_calculated_quantity<=0)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.rawmaterialexistencedto.limit.calculated.quantity.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;	

		if (pautomatic_purchase_request_quantity is null or pautomatic_purchase_request_quantity<=0)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.rawmaterialexistencedto.automatic.purchase.request.quantity.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

	declare
		vrecord	record;
	begin
		select 
			rm.raw_material_id,
			mu.measurment_unit_id
		into
			vrecord
	
			from pms_raw_material_existence rme,
			pms_raw_material rm,
			pms_measurment_unit mu
		where
			rme.id_raw_material = pid_raw_material
			and rme.id_measurment_unit = pid_measurment_unit
			and rme.id_measurment_unit = mu.id
			and rm.id = rme.id_raw_material;

		if (vrecord.raw_material_id is not null) then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.rawmaterialexistencedto.alredy.exists.for.rawmaterial.and.measurment.unit.error'||''||vrecord.raw_material_id||''||vrecord.measurment_unit_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
	end;
	pregistration_date:=now();

	insert into pms_raw_material_existence
			(id,
			id_raw_material,
			id_measurment_unit,
			limit_calculated_quantity,
			automatic_purchase_request_quantity,
			registration_date,
			creation_user,
			creation_date)
	values
			(pid,
			pid_raw_material,
			pid_measurment_unit,
			plimit_calculated_quantity,
			pautomatic_purchase_request_quantity,
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

/****************************************************************************************/
/****************************************************************************************/

create table pms_raw_material_purchase_request(
id	   		 		   			bigint,
id_raw_material					bigint NOT NULL,
id_measurment_unit					bigint NOT NULL,
id_person_supplier					bigint,
quantity   				numeric(11,2) NOT NULL,
registration_date		   			timestamp NOT NULL default now(),
status	 		   			varchar(50) default 'application.common.status.pending',
creation_user	 		   			varchar(50),
creation_date			   			timestamp NOT NULL default now(),
last_modif_user 		   			varchar(50),
last_modif_date		   			timestamp);

--ALTER TABLE pms_raw_material_purchase_request ADD id_person_supplier bigint;
ALTER TABLE pms_raw_material_purchase_request ADD CONSTRAINT pms_raw_material_purchase_request_id_pk PRIMARY KEY (id);


ALTER TABLE pms_raw_material_purchase_request ADD CHECK (quantity >= 0);

ALTER TABLE pms_raw_material_purchase_request 
ADD constraint pms_raw_material_purchase_request_fk_01
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_raw_material_purchase_request 
ADD constraint pms_raw_material_purchase_request_fk_02
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);

ALTER TABLE pms_raw_material_purchase_request 
ADD constraint pms_raw_material_purchase_request_fk_03
FOREIGN KEY (id_person_supplier) REFERENCES pms_person (id);

create sequence pms_raw_material_purchase_request_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_raw_material_purchase_request.id;


create or replace function p_i_pms_raw_material_purchase_request
(
pid	   				 		   bigint,
pid_raw_material					bigint,
pid_measurment_unit	   			 	bigint,
pid_person_supplier					bigint,
pquantity 			   			numeric,
pregistration_date					INOUT timestamp,
pcreation_user						varchar
)
returns timestamp as
$BODY$
declare
	error_message          	text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_raw_material_purchase_request';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
		if (pquantity is null or pquantity<=0)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.raw.material.purchase.request.dao.raw.material.purchase.request.dto.quantity.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;	

	declare
		vrecord	record;
	begin
		select 
			rm.raw_material_id,
			mu.measurment_unit_id,		
			rmpr.quantity,
			rmpr.registration_date,
			person.commercial_name
		into
			vrecord
	
			from pms_raw_material_purchase_request rmpr,
			pms_raw_material rm,
			pms_measurment_unit mu,
			pms_person person
		where
			rmpr.id_raw_material = pid_raw_material
			and rmpr.id_measurment_unit = pid_measurment_unit
			and rmpr.status = 'application.common.status.pending'
			and rmpr.id_person_supplier = person.id
			and rmpr.id_measurment_unit = mu.id
			and rmpr.id_raw_material = rm.id;

		if (vrecord.raw_material_id is not null) then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.raw.material.purchase.request.dao.raw.material.purchase.request.dto.pending.request.exists.for.raw.material.and.measurment.unit.error'||''||vrecord.raw_material_id||''||vrecord.measurment_unit_id||''||trim(to_char(vrecord.quantity,'999999999'))||''||to_char(vrecord.registration_date,'dd/mm/yyyy HH24:MM:SS')||''||vrecord.commercial_name||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
	end;
	
	pregistration_date:=now();

	insert into pms_raw_material_purchase_request
			(id,
			id_raw_material,
			id_measurment_unit,
			id_person_supplier,
			quantity,
			registration_date,
			creation_user,
			creation_date)
	values
			(pid,
			pid_raw_material,
			pid_measurment_unit,
			pid_person_supplier,
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

select * from pms_raw_material_purchase_request

create or replace function p_u_pms_raw_material_purchase_request
(
pid	   				 		   bigint,
pid_raw_material					bigint,
pid_measurment_unit	   			 	bigint,
pid_person_supplier					bigint,
pquantity 			   			numeric,
plast_modif_user						varchar
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
	RAISE INFO 'executing p_u_pms_raw_material_purchase_request';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
		if (pquantity is null or pquantity<=0)
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.raw.material.purchase.request.dao.raw.material.purchase.request.dto.quantity.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;	

	
	declare
		vrecord	record;
	begin
		select 
			rm.raw_material_id,
			mu.measurment_unit_id,		
			rmpr.quantity,
			rmpr.registration_date,
			person.commercial_name
		into
			vrecord
	
			from pms_raw_material_purchase_request rmpr,
			pms_raw_material rm,
			pms_measurment_unit mu,
			pms_person person
		where
			rmpr.id_raw_material = pid_raw_material
			and rmpr.id_measurment_unit = pid_measurment_unit
			and rmpr.id != pid
			and rmpr.status = 'application.common.status.pending'
			and rmpr.id_person_supplier = person.id
			and rmpr.id_measurment_unit = mu.id
			and rmpr.id_raw_material = rm.id;

		if (vrecord.raw_material_id is not null) then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.raw.material.purchase.request.dao.raw.material.purchase.request.dto.pending.request.exists.for.raw.material.and.measurment.unit.error'||''||vrecord.raw_material_id||''||vrecord.measurment_unit_id||''||trim(to_char(vrecord.quantity,'999999999'))||''||to_char(vrecord.registration_date,'dd/mm/yyyy HH24:MM:SS')||''||vrecord.commercial_name||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
	end;

	update pms_raw_material_purchase_request
	set
			id_person_supplier = pid_person_supplier,
			quantity = pquantity,
			last_modif_user = plast_modif_user,
			last_modif_date = now()
	where
			id = pid;
	
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
