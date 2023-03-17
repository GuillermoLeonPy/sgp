/*68-f_determinate_minimal_quantity_to_instantiate_into_production-201611030932*/

		select * 
		from v_order_product_raw_mat_measur_unit_required_for_one_unit
		where order_identifier_number = 31
		and id_product = 7;

select f_determinate_minimal_quantity_to_instantiate_into_production(31,7);
create or replace function f_determinate_minimal_quantity_to_instantiate_into_production
(porder_identifier_number 		bigint,
pid_product 					bigint)
returns bigint as
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
	RAISE INFO 'executing f_determinate_minimal_quantity_to_instantiate_into_production';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_prod_rm_mu_req_one_unit_cursor cursor for
		select * 
		from v_order_product_raw_mat_measur_unit_required_for_one_unit
		where order_identifier_number = porder_identifier_number
		and id_product = pid_product;

		vraw_material_existence_record			record;
		vproduct_units_can_be_produced			bigint;
		vraw_m_measurm_u_can_be_produced			bigint;
	begin
		for vorder_prod_rm_mu_req_one_unit_record in vorder_prod_rm_mu_req_one_unit_cursor loop
			select * into vraw_material_existence_record
			from pms_raw_material_existence where 
			id_raw_material = vorder_prod_rm_mu_req_one_unit_record.id_raw_material
			and id_measurment_unit = vorder_prod_rm_mu_req_one_unit_record.id_measurment_unit;

			if vraw_material_existence_record.id is null then
				vproduct_units_can_be_produced := 0;
				EXIT;
			elsif vraw_material_existence_record.calculated_quantity >= vorder_prod_rm_mu_req_one_unit_record.required_one_unit_quantity then
			--if vraw_material_existence_record.id is null then
				vraw_m_measurm_u_can_be_produced := trunc(vraw_material_existence_record.calculated_quantity / vorder_prod_rm_mu_req_one_unit_record.required_one_unit_quantity);
				if vproduct_units_can_be_produced is null 
				or vraw_m_measurm_u_can_be_produced < vproduct_units_can_be_produced then
					vproduct_units_can_be_produced := vraw_m_measurm_u_can_be_produced;
				end if;--if vraw_m_measurm_u_can_be_produced < vproduct_units_can_be_produced then
				if vproduct_units_can_be_produced = 0 then
					EXIT;
				end if;--if vproduct_units_can_be_produced = 0 then
			else--in this case can only be
			--vraw_material_existence_record.calculated_quantity < vorder_prod_rm_mu_req_one_unit_record.required_one_unit_quantity
				vproduct_units_can_be_produced := 0;
				EXIT;
			end if;--elsif vraw_material_existence_record.calculated_quantity < vorder_prod_rm_mu_req_one_unit_record.required_quantity then
		end loop;--for vorder_prod_rm_mu_req_one_unit_record in vorder_prod_rm_mu_req_one_unit_cursor loop
		return vproduct_units_can_be_produced;
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