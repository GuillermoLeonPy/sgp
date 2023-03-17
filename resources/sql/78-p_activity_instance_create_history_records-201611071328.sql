/*78-p_activity_instance_create_history_records-201611071328*/

/*******************************************************************************
DONT FORGET TO MODIFY THE FOREIGN KEY CONSTRAINT FOR THE
pms_temporary_halfway_product_storage.depositor_activity_instance_unique_number
********************************************************************************/


create or replace function p_activity_instance_create_history_records
(
pid_production_activity_instance			 	bigint
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
	RAISE INFO 'executing p_activity_instance_create_history_records';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vproduction_activity_instance_record	record;
	begin
		select * into vproduction_activity_instance_record
		from pms_production_activity_instance where id = pid_production_activity_instance;

		insert into pms_production_activity_instance_history		
		values
		(vproduction_activity_instance_record.id,
		vproduction_activity_instance_record.id_production_activity,
		vproduction_activity_instance_record.id_person,
		vproduction_activity_instance_record.id_order,
		vproduction_activity_instance_record.id_order_item,
		vproduction_activity_instance_record.id_product,
		vproduction_activity_instance_record.require_parcial_product_recall,
		vproduction_activity_instance_record.parcial_product_recall_date,
		vproduction_activity_instance_record.recall_locker_number,
		vproduction_activity_instance_record.is_asignable,
		vproduction_activity_instance_record.assignment_date,
		vproduction_activity_instance_record.delivers_product_instance,
		vproduction_activity_instance_record.delivers_partial_result,
		vproduction_activity_instance_record.partial_result_delivery_date,
		vproduction_activity_instance_record.occupied_locker_number,
		vproduction_activity_instance_record.status,
		vproduction_activity_instance_record.previous_status,
		vproduction_activity_instance_record.next_status,
		vproduction_activity_instance_record.activity_instance_unique_number,
		vproduction_activity_instance_record.product_instance_unique_number,
		vproduction_activity_instance_record.instantiation_date,
		vproduction_activity_instance_record.activity_start_work_date,
		vproduction_activity_instance_record.activity_finish_work_date,
		vproduction_activity_instance_record.activity_cancellation_date,
		vproduction_activity_instance_record.activity_cancellation_reason_description,
		vproduction_activity_instance_record.creation_user,
		vproduction_activity_instance_record.creation_date,
		vproduction_activity_instance_record.last_modif_user,
		vproduction_activity_instance_record.last_modif_date);

		declare
			vproduction_act_inst_rm_supply_cursor cursor for
			select * from
			pms_production_activity_instance_raw_material_supply 
			where id_production_activity_instance = vproduction_activity_instance_record.id;
		begin
			for vproduction_act_inst_rm_supply_record in vproduction_act_inst_rm_supply_cursor loop
				insert into pms_production_activity_instance_raw_material_supply_history
				values
				(vproduction_act_inst_rm_supply_record.id,
				vproduction_act_inst_rm_supply_record.id_production_activity_instance,
				vproduction_act_inst_rm_supply_record.id_production_activity,
				vproduction_act_inst_rm_supply_record.activity_description,
				vproduction_act_inst_rm_supply_record.process_description,
				vproduction_act_inst_rm_supply_record.id_raw_material_requirement,
				vproduction_act_inst_rm_supply_record.id_raw_material,
				vproduction_act_inst_rm_supply_record.raw_material_description,
				vproduction_act_inst_rm_supply_record.id_measurment_unit,
				vproduction_act_inst_rm_supply_record.measurment_unit_description,
				vproduction_act_inst_rm_supply_record.quantity,
				vproduction_act_inst_rm_supply_record.id_raw_material_existence_affected,
				vproduction_act_inst_rm_supply_record.registration_date,
				vproduction_act_inst_rm_supply_record.raw_material_effective_departure_date,
				vproduction_act_inst_rm_supply_record.creation_user,
				vproduction_act_inst_rm_supply_record.creation_date,
				vproduction_act_inst_rm_supply_record.last_modif_user,
				vproduction_act_inst_rm_supply_record.last_modif_date);

				declare
					vpai_rm_supply_purchase_invoice_affected_cursor cursor for
					select * from pms_pai_rm_supply_purchase_invoice_affected
					where id_pai_raw_material_supply = vproduction_act_inst_rm_supply_record.id;
				begin
					for vpai_rm_supply_purchase_invoice_affected_record in vpai_rm_supply_purchase_invoice_affected_cursor loop
						insert into pms_pai_rm_supply_purchase_invoice_affected_history
						values
						(vpai_rm_supply_purchase_invoice_affected_record.id,
						vpai_rm_supply_purchase_invoice_affected_record.id_pai_raw_material_supply,
						vpai_rm_supply_purchase_invoice_affected_record.id_purchase_invoice,
						vpai_rm_supply_purchase_invoice_affected_record.purchase_invoice_identifier_number,
						vpai_rm_supply_purchase_invoice_affected_record.id_purchase_invoice_item,
						vpai_rm_supply_purchase_invoice_affected_record.quantity,
						vpai_rm_supply_purchase_invoice_affected_record.creation_user,
						vpai_rm_supply_purchase_invoice_affected_record.creation_date);
					end loop;
					--for vpai_rm_supply_purchase_invoice_affected_record in vpai_rm_supply_purchase_invoice_affected_cursor loop
					delete from pms_pai_rm_supply_purchase_invoice_affected
					where id_pai_raw_material_supply = vproduction_act_inst_rm_supply_record.id;
				end;--begin

				delete from pms_production_activity_instance_raw_material_supply
				where id = vproduction_act_inst_rm_supply_record.id;
			end loop;--for vproduction_act_inst_rm_supply_record in vproduction_act_inst_rm_supply_cursor loop
		end;--begin
		delete from pms_production_activity_instance where id = vproduction_activity_instance_record.id;
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