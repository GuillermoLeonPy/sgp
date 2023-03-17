/*54-UC-INST-ACT-PP-check-production-process-integrity*/
select * from f_ordered_production_process_activities(7);
-- 7 is the production process id

create or replace function f_check_production_process_integrity
(
pid_product	   			bigint
)
returns bigint as
$BODY$
declare
	error_message          			text;
	error_message_hint				text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE				text;
	v_MESSAGE_TEXT					text;
	v_PG_EXCEPTION_DETAIL			text;
	v_destination_currency_amount		numeric;
	v_local_currency_amount			numeric;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_check_production_process_integrity';
	RAISE INFO '--------------------------';

	declare
		v_production_process_record				record;		
		v_product_record						record;
		v_valid_production_activities_count		bigint;
	begin
		select pp.* into v_production_process_record
		from pms_production_process pp where pp.id_product = pid_product
		and pp.is_active = 'S' and pp.validity_end_date is null	and pp.is_enable = 'S';

		if v_production_process_record.id is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select prod.* into v_product_record from pms_product prod where prod.id = pid_product;
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.check.production.process.integrity.no.valid.production.process.for.product.error'||''||v_product_record.product_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if v_production_process_record.id is null then

		select count(id) into v_valid_production_activities_count 
		from pms_production_process_activity where id_production_process = v_production_process_record.id
		and validity_end_date is null;

		if v_valid_production_activities_count = 0 then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.check.production.process.integrity.no.valid.production.process.activities.for.production.process.error'||''||v_production_process_record.process_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if v_valid_production_activities_count = 0 then		

		declare
			vcursor cursor for
			select * from f_ordered_production_process_activities(v_production_process_record.id);
			v_valid_raw_material_requirements_count 	bigint;
			v_valid_man_power_requirements_count 		bigint;
		begin
			for vproduction_activity_record in vcursor loop
				
				select count(id) into v_valid_raw_material_requirements_count
				from pms_raw_material_requirement
				where id_production_process_activity = vproduction_activity_record.id
				and validity_end_date is null	and is_active = 'S';
				
				if v_valid_raw_material_requirements_count = 0 then
					declare
						b1_error_message		text;
						b1_error_message_hint	text;
					begin
						select prod.* into v_product_record from pms_product prod where prod.id = pid_product;
						b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.check.production.process.integrity.no.valid.raw.material.requirement.for.activity.error'||''||vproduction_activity_record.activity_id||''||v_production_process_record.process_id||''||v_product_record.product_id||'end.of.message';
						b1_error_message_hint:=b1_error_message;
						RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
					end;
				end if;--if v_valid_raw_material_requirements_count = 0 then
				
				select count(id) into v_valid_man_power_requirements_count
				from pms_man_power_requirement
				where id_production_process_activity = vproduction_activity_record.id
				and validity_end_date is null	and is_active = 'S';

				if v_valid_man_power_requirements_count = 0 then
					declare
						b1_error_message		text;
						b1_error_message_hint	text;
					begin
						select prod.* into v_product_record from pms_product prod where prod.id = pid_product;
						b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.check.production.process.integrity.no.valid.man.power.requirement.for.activity.error'||''||vproduction_activity_record.activity_id||''||v_production_process_record.process_id||''||v_product_record.product_id||'end.of.message';
						b1_error_message_hint:=b1_error_message;
						RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
					end;
				end if;--if v_valid_man_power_requirements_count = 0 then
			end loop;--for vproduction_activity_record in vcursor loop
		end;--begin
		return v_production_process_record.id;
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
