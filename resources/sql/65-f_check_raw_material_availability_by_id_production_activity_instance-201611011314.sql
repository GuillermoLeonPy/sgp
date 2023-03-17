/*65-f_check_raw_material_availability_by_id_production_activity_instance-201611011314*/

select f_check_raw_material_availability_by_id_prod_activity_instance(19);

create or replace function f_check_raw_material_availability_by_id_prod_activity_instance
(
pid_production_activity_instance		bigint
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
	RAISE INFO 'executing f_check_raw_material_availability_by_id_prod_activity_instance';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

		declare
			vproduction_activity_instance_record	record;
			vpms_order_record					record;
			vpms_order_budget_product_record		record;
		begin
			select * into vproduction_activity_instance_record from pms_production_activity_instance
			where id = pid_production_activity_instance;
		
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

						/* check existence is enough */
						if vraw_material_existence_record.id is null then
						--there is no existence record for raw material and measurment unit
							if vprepared_error_message is null then
								vprepared_error_message := '#-row-#';
							end if;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.process.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vproduction_process_record.process_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.activity.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vproduction_process_activity_record.activity_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.not.enough.raw.material';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vraw_material_record.raw_material_id;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.measurmentunitid.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vmeasurment_unit_record.measurment_unit_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.available.quantity';
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.existence.record.does.not.exists';
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.required.quantity';
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-numeric-#'||vraw_material_requirement_record.quantity;
							vprepared_error_message := vprepared_error_message||'#-row-#';
						elsif vraw_material_requirement_record.quantity > vraw_material_existence_record.calculated_quantity then							
							if vprepared_error_message is null then
								vprepared_error_message := '#-row-#';
							end if;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.process.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vproduction_process_record.process_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.activity.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vproduction_process_activity_record.activity_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.not.enough.raw.material';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vraw_material_record.raw_material_id;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.measurmentunitid.label';
							vprepared_error_message := vprepared_error_message||'#-column-#'||vmeasurment_unit_record.measurment_unit_description;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.available.quantity';
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-numeric-#'||vraw_material_existence_record.calculated_quantity;
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-key-#'||'application.common.required.quantity';
							vprepared_error_message := vprepared_error_message||'#-column-#'||'#-numeric-#'||vraw_material_requirement_record.quantity;
							vprepared_error_message := vprepared_error_message||'#-row-#';
						end if;--if vraw_material_requirement_record.quantity > vraw_material_existence_record.calculated_quantity then						
					end loop;--for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
					return vprepared_error_message;
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