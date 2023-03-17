/*59-UC-AMD-ASIG-AP-retiro-materia-prima-deposito-201610311132*/


/* ***********  ***********  ***********  ***********  ***********  ***********  *********** */
/* ***********  ***********  ***********  ***********  ***********  ***********  *********** */
/* ***********  ***********  ***********  ***********  ***********  ***********  *********** */
/* ***********  ***********  ***********  ***********  ***********  ***********  *********** */
/* ***********  ***********  ***********  ***********  ***********  ***********  *********** */
/* ATENCION 




ESTE PROCEDIMIENTO SE EJECUTA DENTRO DEL CASO DE USO INSTANCIAR ACTIVIDADES DE PRODUCCION

Y NO EN EL CASO DE USO: UC-AMD-ASIG-AP

*/


create table pms_production_activity_instance_raw_material_supply(
id	   		 		   				bigint,
id_production_activity_instance			bigint not null,
id_production_activity					bigint not null,
activity_description					varchar(250),
process_description						varchar(250),
id_raw_material_requirement				bigint not null,
id_raw_material						bigint not null,
raw_material_description					varchar(250),
id_measurment_unit						bigint not null,
measurment_unit_description				varchar(250),
quantity   							numeric(11,2) NOT NULL,
id_raw_material_existence_affected			bigint not null,
registration_date						timestamp not null default now(),
raw_material_effective_departure_date		timestamp,
creation_user							varchar(50),
creation_date							timestamp not null default now(),
last_modif_user						varchar(50),
last_modif_date						timestamp
);

ALTER TABLE pms_production_activity_instance_raw_material_supply ADD CONSTRAINT 
chk_raw_material_effective_departure_date CHECK (raw_material_effective_departure_date > registration_date);

ALTER TABLE pms_production_activity_instance_raw_material_supply ADD CHECK (quantity > 0);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD CONSTRAINT 
pms_production_activity_instance_raw_material_supply_id_pk PRIMARY KEY (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply ADD CONSTRAINT
pms_production_activity_instance_raw_material_supply_uk_01 UNIQUE 
(id_production_activity_instance,id_raw_material_requirement);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_01
FOREIGN KEY (id_production_activity_instance) 
REFERENCES pms_production_activity_instance (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_02
FOREIGN KEY (id_raw_material_requirement) REFERENCES pms_raw_material_requirement (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_03
FOREIGN KEY (id_raw_material) REFERENCES pms_raw_material (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_04
FOREIGN KEY (id_measurment_unit) REFERENCES pms_measurment_unit (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_05
FOREIGN KEY (id_raw_material_existence_affected) REFERENCES pms_raw_material_existence (id);

ALTER TABLE pms_production_activity_instance_raw_material_supply 
ADD constraint pms_production_activity_instance_raw_material_supply_fk_06
FOREIGN KEY (id_production_activity) REFERENCES pms_production_process_activity (id);

create sequence pms_production_activity_instance_raw_material_supply_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_production_activity_instance_raw_material_supply.id;

/* ATENCION 
ESTE PROCEDIMIENTO SE EJECUTA DENTRO DEL CASO DE USO INSTANCIAR ACTIVIDADES DE PRODUCCION
Y NO EN EL CASO DE USO: UC-AMD-ASIG-AP
*/

create or replace function p_i_production_activity_instance_raw_material_supply
(
pid_production_activity_instance				bigint,
pid_order_budget_production_process_activity		bigint,
pcreation_user			   					varchar
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
	RAISE INFO 'executing p_production_activity_instance_raw_material_supply';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_budget_production_process_activity_raw_m_cursor cursor for
		select * from pms_order_budget_production_process_activity_raw_m
		where id_order_budget_production_process_activity = pid_order_budget_production_process_activity;

		vproduction_activity_instance_record		record;
		vraw_material_requirement_record			record;
		vproduction_process_record				record;
		vproduction_process_activity_record		record;
		vraw_material_record					record;
		vmeasurment_unit_record					record;
		vraw_material_existence_record			record;
		
	begin

		select * into vproduction_activity_instance_record from pms_production_activity_instance
		where id = pid_production_activity_instance;
	
		for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
			select * into vraw_material_requirement_record
			from pms_raw_material_requirement where id = 
			vorder_budget_production_process_activity_raw_m_record.id_raw_material_requirement;

			select * into vproduction_process_activity_record from pms_production_process_activity
			where id = vproduction_activity_instance_record.id_production_activity;
			select * into vproduction_process_record from pms_production_process 
			where id = vproduction_process_activity_record.id_production_process;

			select * into vraw_material_record from pms_raw_material
			where id = vraw_material_requirement_record.id_raw_material;
			select * into vmeasurment_unit_record from pms_measurment_unit
			where id = vraw_material_requirement_record.id_measurment_unit;

			select * into vraw_material_existence_record from pms_raw_material_existence
			where id_raw_material = vraw_material_record.id
			and id_measurment_unit = vmeasurment_unit_record.id;
												
			insert into pms_production_activity_instance_raw_material_supply
			(id,
			id_production_activity,
			activity_description,
			process_description,
			id_production_activity_instance,
			id_raw_material_requirement,
			id_raw_material,raw_material_description,
			id_measurment_unit,measurment_unit_description,
			quantity,
			id_raw_material_existence_affected,
			creation_user)
			values
			(nextval('pms_production_activity_instance_raw_material_supply_id_sq'),
			vproduction_process_activity_record.id,
			vproduction_process_activity_record.activity_description,
			vproduction_process_record.process_description,
			vproduction_activity_instance_record.id,
			vraw_material_requirement_record.id,							
			vraw_material_record.id,vraw_material_record.raw_material_id,
			vmeasurment_unit_record.id,vmeasurment_unit_record.measurment_unit_id,
			vraw_material_requirement_record.quantity,
			vraw_material_existence_record.id,
			pcreation_user);
			
			RAISE INFO '--------------------------';
			RAISE INFO '--------------------------';			
			RAISE INFO '**-update pms_raw_material_existence-**';
			RAISE INFO 'id_product : %',vproduction_activity_instance_record.id_product;
			RAISE INFO 'vraw_material_requirement_record.quantity : %',vraw_material_requirement_record.quantity;
			RAISE INFO 'vraw_material_existence_record.calculated_quantity : %',vraw_material_existence_record.calculated_quantity;
			RAISE INFO 'calculated_quantity = %', (vraw_material_existence_record.calculated_quantity - vraw_material_requirement_record.quantity);
			RAISE INFO 'vraw_material_existence_record.id : %', vraw_material_existence_record.id;
			RAISE INFO '--------------------------';
			update pms_raw_material_existence
			set calculated_quantity = vraw_material_existence_record.calculated_quantity - vraw_material_requirement_record.quantity,
			last_modif_user = pcreation_user,
			last_modif_date = now()
			where id = vraw_material_existence_record.id;
			
		end loop;--for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
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
