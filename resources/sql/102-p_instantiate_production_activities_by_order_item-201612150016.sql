/*102-p_instantiate_production_activities_by_order_item-201612150016*/

create or replace function p_instantiate_production_activities_by_order_item
(
pid_order				 				bigint,
pid_order_item			 				bigint,
pcreation_user	 		   			varchar
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
	RAISE INFO 'executing p_instantiate_production_activities_by_order_item';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record							record;
		vorder_item_record								record;
		vorder_budget_product							record;
		view_order_raw_material_sufficiency_report_record		record;
		vinstanciable_product_quantity					bigint;
		v_custom_order_raw_material_sufficiency_report_record		record;
		
		v_order_item_actual_in_progress_quantity			bigint;
		
		v_product_instance_unique_number			pms_production_activity_instance.product_instance_unique_number%type;
		vid_production_process					pms_production_process.id%type;
		vrequire_parcial_product_recall			pms_production_activity_instance.require_parcial_product_recall%type;
		vid_production_activity_first				pms_production_activity_instance.id%type;
		vid_production_activity_last				pms_production_activity_instance.id%type;
		vid_production_activity_instance			pms_production_activity_instance.id%type;
		vis_asignable							pms_production_activity_instance.is_asignable%type;		
	begin
			select ord.* into vorder_record from pms_order ord where ord.id = pid_order;
			select * into vorder_item_record from pms_order_item where id = pid_order_item;
			select * into vorder_budget_product from pms_order_budget_product 
			where order_identifier_number = vorder_record.identifier_number
			and id_product = vorder_item_record.id_product;

			--establish the limit counter for the activities instantiation loop
			-------------------------------------------------------------------
			--check if a valid pms_order_raw_material_insufficiency_report exists
				--1) identify pms_order_raw_material_insufficiency_report
				--2) identify the pms_order_item_raw_material_insufficiency_report
					--2.1) if no record found for the item
						--2.1.1) establish the limit counter to the pms_order_item.pending_to_instanciate_quantity
					--2.2) if exists record for the item
						--2.2.1) determinate the quantity that can be instantiated into production
						--2.2.2) establish the limit counter to the quantity that can be instantiated into production
						
			--if no valid pms_order_raw_material_insufficiency_report exists
				--1) establish the limit counter to pms_order_item.pending_to_instanciate_quantity
			select * into view_order_raw_material_sufficiency_report_record
			from v_order_raw_material_sufficiency_report
			where id_order = vorder_record.id and id_order_item = vorder_item_record.id;
			if view_order_raw_material_sufficiency_report_record.id_order_item is null then
				vinstanciable_product_quantity := vorder_item_record.pending_to_instanciate_quantity;
			else
				vinstanciable_product_quantity := view_order_raw_material_sufficiency_report_record.entered_into_producction_quantity;
			end if;--if view_order_raw_material_sufficiency_report_record.id_order_item is null then

			if vinstanciable_product_quantity > 0 then
				for vitem_product_counter in 1..vinstanciable_product_quantity loop
					v_product_instance_unique_number := nextval('pms_pai_product_instance_unique_number_sq');
					vid_production_activity_first := null;
					declare
						v_activities_cursor cursor for
						select * from v_ordererd_prod_proc_activities_by_o_budget_prod_proc_activity
						where order_identifier_number = vorder_record.identifier_number
						and id_product = vorder_item_record.id_product;
					begin
						for vproduction_activity_record in v_activities_cursor loop
							if vid_production_activity_first is null then
								vid_production_activity_first := vproduction_activity_record.id_production_process_activity;
							end if;
							if vid_production_activity_first = vproduction_activity_record.id_production_process_activity then
								vrequire_parcial_product_recall := 'N';
								vis_asignable := 'S';
							else
								vrequire_parcial_product_recall := 'S';
								vis_asignable := 'N';
							end if;
							vid_production_activity_instance := nextval('pms_production_activity_instance_id_sq');
							insert into pms_production_activity_instance
							(id,
							id_production_activity,
							id_order,id_order_item,
							id_product,require_parcial_product_recall,
							activity_instance_unique_number,
							product_instance_unique_number,
							is_asignable,
							creation_user)
							values
							(vid_production_activity_instance,
							vproduction_activity_record.id_production_process_activity,
							vorder_record.id,vorder_item_record.id,
							vorder_item_record.id_product,vrequire_parcial_product_recall,
							nextval('pms_pai_activity_instance_unique_number_sq'),
							v_product_instance_unique_number,
							vis_asignable,
							pcreation_user);
							
							--CREATE
							--pms_production_activity_instance_raw_material_supply
							--RECORD AS REQUERIMENTS DICTATE
							--by parameters
								--id_production_activity_instance
								--vproduction_activity_record.id_order_budget_production_process_activity
							PERFORM p_i_production_activity_instance_raw_material_supply
							(vid_production_activity_instance,
							vproduction_activity_record.id_order_budget_production_process_activity,
							pcreation_user);
							
						end loop;--for vproduction_activity_record in vcursor loop
						--update the last activity instance record to require deliver product instance
						update pms_production_activity_instance 
						set delivers_product_instance = 'S',delivers_partial_result = 'N'
						where id = vid_production_activity_instance; 
					end;--begin
				end loop;--for vitem_product_counter in 1..vinstanciable_product_quantity loop

				--	**** VERY IMPORTANT **** 
				--update the pms_order_item record in the attributes
					--pending_to_instanciate_quantity
					--in_progress_quantity
				RAISE INFO '--------------------------';
				RAISE INFO '**** VERY IMPORTANT ****';
				RAISE INFO 'vorder_item_record.id = %',vorder_item_record.id;
				RAISE INFO 'vinstanciable_product_quantity = %',vinstanciable_product_quantity;
				RAISE INFO '--------------------------';
				
					v_order_item_actual_in_progress_quantity := vorder_item_record.in_progress_quantity;
					update pms_order_item
					set 
						status = 'application.common.status.in.progress',
						pending_to_instanciate_quantity = vorder_item_record.pending_to_instanciate_quantity - vinstanciable_product_quantity,
						in_progress_quantity = v_order_item_actual_in_progress_quantity + vinstanciable_product_quantity,
						last_modif_user = pcreation_user, last_modif_date = now()
					where id = vorder_item_record.id;					
				
				
				
				if vorder_record.status = 'application.common.status.pre.production'
				and vinstanciable_product_quantity > 0 then
					update pms_order
						set status = 'application.common.status.in.progress',
						last_modif_user = pcreation_user, last_modif_date = now(),
						production_activities_instantiation_date = now()
						where id = vorder_record.id;			
				end if;
			end if;--if vinstanciable_product_quantity > 0 then
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