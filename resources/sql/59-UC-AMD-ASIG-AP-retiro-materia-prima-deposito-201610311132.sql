/*59-UC-AMD-ASIG-AP-retiro-materia-prima-deposito-201610311132*/

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



create or replace function p_i_production_activity_instance_raw_material_supply
(
pid_production_activity_instance				bigint,
pid_order_budget_production_process_activity		bigint,
pcreation_user			   					varchar
)
returns text as
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
		vproduction_activity_instance_record	record;
		vnext_status						pms_production_activity_instance.next_status%type;
		vprepared_error_message					text;
	begin

		select * into vproduction_activity_instance_record from pms_production_activity_instance
		where id = pid_production_activity_instance;
	
		if vproduction_activity_instance_record.require_parcial_product_recall = 'S'
		and vproduction_activity_instance_record.status != 'application.common.status.partial.result.recalled' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin			
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.raw.material.supply.activity.require.partial.product.recall.which.has.not.been.performed.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.partial.result.recalled'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if vproduction_activity_instance_record.require_parcial_product_recall = 'N'
		and vproduction_activity_instance_record.status != 'application.common.status.assigned' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin			
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.raw.material.supply.activity.not.in.required.status.to.perform.raw.material.supply.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.partial.result.recalled'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		/* check existence is enough */
		vprepared_error_message := f_check_raw_material_availability_by_id_prod_activity_instance(vproduction_activity_instance_record.id);

		declare
			vpms_order_record					record;
			vpms_order_budget_product_record		record;
		begin
			select * into vpms_order_record from pms_order 
			where id = vproduction_activity_instance_record.id_order;
			
			select * into vpms_order_budget_product_record from pms_order_budget_product 
			where order_identifier_number = vpms_order_record.identifier_number
			and id_product = vproduction_activity_instance_record.id_product;

			declare
				vorder_budget_production_process_activity_record record;
			begin
				select * into vorder_budget_production_process_activity_record
				from pms_order_budget_production_process_activity
				where id_order_budget_product = vpms_order_budget_product_record.id
				and id_production_process_activity = vproduction_activity_instance_record.id_production_activity;
				
				declare
					vorder_budget_production_process_activity_raw_m_cursor cursor for
					select * from pms_order_budget_production_process_activity_raw_m
					where id_order_budget_production_process_activity = 
					vorder_budget_production_process_activity_record.id;
					
					vraw_material_requirement_record			record;
					vproduction_process_record				record;
					vproduction_process_activity_record		record;
					vraw_material_record					record;
					vmeasurment_unit_record					record;
					vraw_material_existence_record			record;
					vprepared_error_message					text;
				begin
					
					for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
						select * into vraw_material_requirement_record
						from pms_raw_material_requirement where id = 
						vorder_budget_production_process_activity_raw_m_record.id_raw_material_requirement;

						select * into vproduction_process_activity_record from pms_production_process_activity
						where id = vorder_budget_production_process_activity_record.id_production_process_activity;
						select * into vproduction_process_record from pms_production_process 
						where id = vorder_budget_production_process_activity_record.id_production_process;

						select * into vraw_material_record from pms_raw_material
						where id = vraw_material_requirement_record.id_raw_material;
						select * into vmeasurment_unit_record from pms_measurment_unit
						where id = vraw_material_requirement_record.id_measurment_unit;

						select * into vraw_material_existence_record from pms_raw_material_existence
						where id_raw_material = vraw_material_record.id
						and id_measurment_unit = vmeasurment_unit_record.id;
												
						insert into pms_production_activity_instance_raw_material_supply
							(id,id_production_activity,activity_description,
							id_production_activity_instance,id_raw_material_requirement,
							id_raw_material,raw_material_description,
							id_measurment_unit,measurment_unit_description,
							quantity,id_raw_material_existence_affected,
							creation_user)
						values
							(nextval('pms_production_activity_instance_raw_material_supply_id_sq'),vproduction_process_activity_record.id,vproduction_process_activity_record.activity_description,
							vproduction_activity_instance_record.id,vraw_material_requirement_record.id,							
							vraw_material_record.id,vraw_material_record.raw_material_description,
							vmeasurment_unit_record.id,vmeasurment_unit_record.measurment_unit_description,
							vraw_material_requirement_record.quantity,id_raw_material_existence_affected,
							creation_user);
					end loop;--for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
				end;--begin			
			end;--begin
		end;--begin		
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