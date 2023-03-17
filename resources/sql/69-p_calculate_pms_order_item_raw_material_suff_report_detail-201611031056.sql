/*69-p_calculate_pms_order_item_raw_material_suff_report_detail-201611031056*/

create or replace function p_calculate_pms_order_item_raw_material_suff_report_detail
(
pid_order							bigint,
pid_order_item						bigint,
pminimal_quantity_capable_of_produce	bigint,
pcreation_user						varchar
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
	RAISE INFO 'executing p_calculate_pms_order_item_raw_material_suff_report_detail';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record					record;
		vorder_item_record				record;
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;

		select oi.* into vorder_item_record from pms_order_item oi
		where oi.id = pid_order_item;

		declare
			vorder_prod_raw_mat_measur_unit_req_for_one_unit_cursor cursor for
			select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
			where order_identifier_number = vorder_record.identifier_number
			and id_product = vorder_item_record.id_product;

			vraw_material_existence_record						record;
			
			vunsatisfiable_quantity								bigint;
			vtotal_quantity_per_raw_material_and_measurment_unit		numeric(11,2);
			v_missing_amount									numeric(11,2);
			vraw_material_measurment_unit_calculated_qty				numeric(11,2);
		begin
			vunsatisfiable_quantity := vorder_item_record.pending_to_instanciate_quantity - pminimal_quantity_capable_of_produce;
			
			for vorder_prod_raw_mat_measur_unit_req_for_one_unit_record in vorder_prod_raw_mat_measur_unit_req_for_one_unit_cursor loop
				/*PER ITERATION			
				per id_raw_material and id_measurment_unit
				determinate the quantity requirement to satisfy the following id_product quantity:
				this is the quantity that will not be introduced into production*/
				vtotal_quantity_per_raw_material_and_measurment_unit := vorder_prod_raw_mat_measur_unit_req_for_one_unit_record.required_one_unit_quantity * vunsatisfiable_quantity;

				select * into vraw_material_existence_record
				from pms_raw_material_existence
				where 
				id_raw_material = vorder_prod_raw_mat_measur_unit_req_for_one_unit_record.id_raw_material
				and id_measurment_unit = vorder_prod_raw_mat_measur_unit_req_for_one_unit_record.id_measurment_unit;

				if vraw_material_existence_record.id is null then
					vraw_material_measurment_unit_calculated_qty := 0;
				else
					vraw_material_measurment_unit_calculated_qty := vraw_material_existence_record.calculated_quantity;
				end if;
				
				v_missing_amount := vraw_material_measurment_unit_calculated_qty - vtotal_quantity_per_raw_material_and_measurment_unit;

				if v_missing_amount < 0 then
					/*THE AVAILABLE QUANTITY FOR THIS RAW MATERIAL AND MEASURMENT UNIT IS NOT
					ENOUGH TO SATISFY THE vunsatisfiable_quantity	
					SAVE THE v_missing_amount AS THE QUANTITY NEED TO SATISFY THE NEXT INSTANTIATION
					OF THIS ORDER ITEM INTO PRODUCTION	*/
					--TURN TO POSSITIVE VALUE
					

					PERFORM p_i_pms_order_item_raw_material_sufficiency_report_detail
						(vorder_record.id,--pid_order
						vorder_item_record.id,--pid_order_item
						pminimal_quantity_capable_of_produce,--pentered_into_producction_quantity
						vunsatisfiable_quantity,--ppending_to_instanciate_quantity
						vorder_prod_raw_mat_measur_unit_req_for_one_unit_record.id_raw_material,--pid_raw_material
						vorder_prod_raw_mat_measur_unit_req_for_one_unit_record.id_measurment_unit,--pid_measurment_unit
						vtotal_quantity_per_raw_material_and_measurment_unit,--prequired_quantity
						vraw_material_measurment_unit_calculated_qty,--pavailable_quantity
						pcreation_user--pcreation_user
						);
				/*else THE AVAILABLE QUANTITY FOR THIS RAW MATERIAL AND MEASURMENT UNIT IS ENOUGH 
					TO SATISFY THE vunsatisfiable_quantity
					NO NEED TO SAVE A RECORD FOR THIS RAW MATERIAL AND MEASURMENT UNIT*/
				end if;--if v_missing_amount < 0 then
			end loop;--for vorder_prod_raw_mat_measur_unit_req_for_one_unit_record in vorder_prod_raw_mat_measur_unit_req_for_one_unit_cursor loop
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
