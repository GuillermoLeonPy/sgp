/*67-p_check_raw_material_sufficiency-201611021619*/

/* here create a view by raw material and measurment unit, indicating the quantity 
required 
*/
--drop view v_order_product_raw_mat_measur_unit_required_for_one_unit
create or replace view v_order_product_raw_mat_measur_unit_required_for_one_unit
as
select 
obp.id,
obp.order_identifier_number,
obp.id_product,
rmr.id_raw_material,
rmr.id_measurment_unit,
sum(rmr.quantity) required_one_unit_quantity
from
	pms_order_budget_product obp,
	pms_order_budget_production_process_activity obppa,
	pms_order_budget_production_process_activity_raw_m obpparm,
	pms_raw_material_requirement rmr
where
	obp.id = obppa.id_order_budget_product
	and obppa.id = obpparm.id_order_budget_production_process_activity
	and obpparm.id_raw_material_requirement = rmr.id
group by 
obp.id,
obp.order_identifier_number,
obp.id_product,
rmr.id_raw_material,
rmr.id_measurment_unit
order by 
obp.order_identifier_number,
obp.id_product,
rmr.id_raw_material,
rmr.id_measurment_unit;

select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
where order_identifier_number = 31;



create or replace function p_check_raw_material_sufficiency
(
pid_order			 				bigint,
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
	RAISE INFO 'executing p_check_raw_material_sufficiency';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record									record;
		vorder_raw_material_sufficiency_report_record		record;
		vorder_budget_product							record;		
		v_order_item_cursor 							cursor 
		for select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;

		v_minimal_quantity_capable_of_produce				bigint;
		
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;
	
		select * into vorder_raw_material_sufficiency_report_record
		from pms_order_raw_material_sufficiency_report
		where id_order = vorder_record.id and is_valid = 'S';

		if vorder_raw_material_sufficiency_report_record.id is not null then
			update pms_order_raw_material_sufficiency_report
			set is_valid = 'N', last_modif_user = pcreation_user, last_modif_date = now()
			where id = vorder_raw_material_sufficiency_report_record.id;
		end if;--if vorder_raw_material_sufficiency_report_record.id is not null then

		for vorder_item_record in v_order_item_cursor loop
			/* v_minimal_quantity_capable_of_produce := 
			determinate the minimal quantity of id_product that can be instantiated into production */
			v_minimal_quantity_capable_of_produce := f_determinate_minimal_quantity_to_instantiate_into_production(vorder_record.identifier_number,vorder_item_record.id_product);			

			/* goal : determinate the raw material and measurment unit quantity to be setted in the
			pms_order_item_raw_material_sufficiency_report_detail
			parameters
				v_minimal_quantity_capable_of_produce
				id_order
				id_order_item	
			*/
			PERFORM p_calculate_pms_order_item_raw_material_suff_report_detail
				(vorder_record.id,
				vorder_item_record.id,
				v_minimal_quantity_capable_of_produce,
				pcreation_user);
		end loop;--for vorder_item_record in v_order_item_cursor loop		
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

/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
	/* 
			goal : 
			determinate the raw material and measurment unit quantity to be setted in the
			pms_order_item_raw_material_sufficiency_report_detail
			parameters
				v_minimal_quantity_capable_of_produce
				id_order
				id_order_item			
			
			
			LOOP THE ID_RAW_MATERIAL AND ID_MEASURMENT_UNIT FOR THE ID_PRODUCT

			PER ITERATION
			
			per id_raw_material and id_measurment_unit
			determinate the quantity requirement to satisfy the following id_product quantity:
			this is the quantity that will not be introduced into production
			(pms_order_item.pending_to_instanciate_quantity - v_minimal_quantity_capable_of_produce)
			by performing the next calculations
			
			vunsatisfiable_quantity := pms_order_item.pending_to_instanciate_quantity - v_minimal_quantity_capable_of_produce;

			vtotal_quantity_per_raw_material_and_measurment_unit := 
			cursor.required_quantity * vunsatisfiable_quantity;
			where cursor.required_quantity is the quantity required to produce one unit of id_product

			v_missing_amount := 
			vraw_material_existence_record.calculated_quantity - vtotal_quantity_per_raw_material_and_measurment_unit;

			if v_missing_amount < 0 then
				THE AVAILABLE QUANTITY FOR THIS RAW MATERIAL AND MEASURMENT UNIT IS NOT
				ENOUGH TO SATISFY THE vunsatisfiable_quantity

				SAVE THE v_missing_amount AS THE QUANTITY NEED TO SATISFY THE NEXT INSTANTIATION
				OF THIS ORDER ITEM INTO PRODUCTION
			
				TURN TO POSSITIVE VALUE
				v_missing_amount := v_missing_amount * -1;

						the next section needs the following parameters
						goal: insert a pms_order_item_raw_material_sufficiency_report_detail
						
						id_order
						id_order_item
						entered_into_producction_quantity ---> v_minimal_quantity_capable_of_produce
						pending_to_instanciate_quantity   ---> vunsatisfiable_quantity
						id_raw_material
						id_measurment_unit
						required_quantity				---> v_missing_amount
						available_quantity				---> vraw_material_existence_record.calculated_quantity

				
			else

				THE AVAILABLE QUANTITY FOR THIS RAW MATERIAL AND MEASURMENT UNIT IS ENOUGH 
				TO SATISFY THE vunsatisfiable_quantity

				NO NEED TO SAVE A RECORD FOR THIS RAW MATERIAL AND MEASURMENT UNIT
			end if;			
			*/


/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
/* *************************** *************************************** ************************************* */
/* determinate the minimal quantity of id_product that can be instantiated into production */

f_determinate_minimal_quantity_to_instantiate_into_production()
			declare
				/*cursor
					id_order_budget_product
					id_raw_material
					id_measurment_unit
					required_quantity
				*/
				vraw_material_existence_record					record;
				vorder_item_raw_material_sufficiency_report_record	record;
				vorder_item_raw_material_suff_report_detail_record	record;
				vavailable_quantity
			begin
				for cursor loop
					select * into vraw_material_existence_record
					from pms_raw_material_existence where id_raw_material = cursor.id_raw_material
					and id_measurment_unit = cursor.id_measurment_unit;

					if vraw_material_existence_record.id is null then
						/* not one instance of id_product can be produced
						in a variable called: vproduct_units_can_be_produced
						set the ZERO value.
						this will be the value that will be assigned to entered_into_producction_quantity
						*/
					elsif vraw_material_existence_record.calculated_quantity < cursor.required_quantity

						/* determinate how many units can be introduced into production 
						with te available quantity:
						divide: vraw_material_existence_record.calculated_quantity / cursor.required_quantity
						where cursor.required_quantity is the quantity required to produce one unit of id_product

						assign the truncated quotient in the variable: v_current_raw_material_measurment_unit_can_be_produced
						if v_current_raw_material_measurment_unit_can_be_produced < vproduct_units_can_be_produced then
							a new minor quantity of product capable of being produced has been found
							vproduct_units_can_be_produced := v_current_raw_material_measurment_unit_can_be_produced;
						end if											
						*/
					end if;--elsif vraw_material_existence_record.calculated_quantity < cursor.required_quantity									
				end loop;--for cursor loop

				/*at this point we have determinated the vproduct_units_can_be_produced for this item*/
				/* again cycle the same cursor */
			end;--begin





/* ******************************************************************** *************************************** */
/* ******************************************************************** *************************************** */

						/* the next section needs the following parameters
						goal: insert a pms_order_item_raw_material_sufficiency_report_detail
						
						id_order
						id_order_item
						entered_into_producction_quantity
						pending_to_instanciate_quantity
						id_raw_material
						id_measurment_unit
						required_quantity
						available_quantity

						write a function to do the following section
						*/

						/*check if exists pms_order_raw_material_sufficiency_report record*/
						select * into vorder_raw_material_sufficiency_report_record
						from pms_order_raw_material_sufficiency_report
						where id_order = vorder_record.id and is_valid = 'S';

						if vorder_raw_material_sufficiency_report_record.id is null then
							insert into pms_order_raw_material_sufficiency_report 
							(id,id_order,creation_user)
							values
							(nextval('pms_order_raw_material_sufficiency_report_id_sq'), vorder_record.id, pcreation_user);
						end if;--if vorder_raw_material_sufficiency_report_record.id is null then

						select * into vorder_raw_material_sufficiency_report_record
						from pms_order_raw_material_sufficiency_report
						where id_order = vorder_record.id and is_valid = 'S';
						
						/*check if exists pms_order_item_raw_material_sufficiency_report record*/
						select * into vorder_item_raw_material_sufficiency_report_record
						from pms_order_item_raw_material_sufficiency_report
						where id_order_raw_material_sufficiency_report = vorder_raw_material_sufficiency_report_record.id
						and id_order_item = vorder_item_record.id;

						if vorder_item_raw_material_sufficiency_report_record.id is null then
							insert into pms_order_item_raw_material_sufficiency_report 
							(id,id_order_raw_material_sufficiency_report,id_order_item)
							values
							(nextval('pms_order_raw_material_sufficiency_report_id_sq'),vorder_raw_material_sufficiency_report_record.id,vorder_item_record.id);
						end if;--if vorder_item_raw_material_sufficiency_report_record.id is null then

						select * into vorder_item_raw_material_sufficiency_report_record
						from pms_order_item_raw_material_sufficiency_report
						where id_order_raw_material_sufficiency_report = vorder_raw_material_sufficiency_report_record.id
						and id_order_item = vorder_item_record.id;

						/*check if exists pms_order_item_raw_material_sufficiency_report_detail record */
						select * into vorder_item_raw_material_suff_report_detail_record
						from pms_order_item_raw_material_sufficiency_report_detail
						where 
						id_order_item_raw_material_sufficiency_report = 
						vorder_item_raw_material_sufficiency_report_record.id 
						and id_raw_material = cursor.id_raw_material
						and id_measurment_unit = cursor.id_measurment_unit; 

						if vorder_item_raw_material_suff_report_detail_record.id is null then
							insert into pms_order_item_raw_material_sufficiency_report_detail
							(id,
							id_order_item_raw_material_sufficiency_report,
							id_raw_material,
							id_measurment_unit,
							required_quantity,
							available_quantity)
							values
							(nextval('pms_order_item_raw_material_sufficiency_report_detail_id_sq'),
							vorder_item_raw_material_sufficiency_report_record.id,
							cursor.id_raw_material,
							cursor.id_measurment_unit,
							);
						end if;