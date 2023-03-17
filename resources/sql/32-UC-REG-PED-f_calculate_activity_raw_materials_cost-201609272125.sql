/*32-UC-REG-PED-f_calculate_activity_raw_materials_cost-201609272125*/
create or replace function f_calculate_activity_raw_materials_cost
(
pid_production_process_activity	   			bigint,
porder_id_currency 		 		   			bigint
)
returns numeric as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;

	v_product_id							pms_product.product_id%type;
	v_production_process_id					pms_production_process.process_id%type;
	v_production_process_activity_id			pms_production_process_activity.activity_id%type;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_calculate_activity_raw_materials_cost';
	RAISE INFO '--------------------------';

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select 
			prod.product_id,
			pp.process_id,
			ppa.activity_id,
			count(rmr.id) rmr_count 
		into vrecord 
		from 
			pms_product prod join
			pms_production_process pp
			on pp.id_product = prod.id join
			pms_production_process_activity ppa
			on ppa.id_production_process = pp.id
			left outer join
			pms_raw_material_requirement rmr
			on
			ppa.id = rmr.id_production_process_activity
			and rmr.validity_end_date is null
			and rmr.is_active = 'S'
		where 
			ppa.id = pid_production_process_activity
			and pp.validity_end_date is null			
			and ppa.validity_end_date is null
			group by 
			prod.product_id,pp.process_id,ppa.activity_id;

		v_product_id:=vrecord.product_id;
		v_production_process_id:=vrecord.process_id;
		v_production_process_activity_id:=vrecord.activity_id;
		RAISE INFO '--------------------------';
		RAISE INFO 'product: %',v_product_id;
		RAISE INFO 'process: %',v_production_process_id;
		RAISE INFO 'activity: %',v_production_process_activity_id;
		RAISE INFO '--------------------------';
		
		if vrecord.rmr_count = 0 then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.calculate.activity.raw.materials.cost.no.valid.requirements.error'||''||vrecord.activity_id||''||vrecord.process_id||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;
	end;

	declare
		vrecord record;
		vcursor_raw_material_requeriments cursor for
		select rm.raw_material_id,rm.raw_material_description,mu.measurment_unit_id,mu.measurment_unit_description,
		rmr.id_raw_material,rmr.id_measurment_unit,rmr.quantity
 		from pms_raw_material_requirement rmr,
 		pms_measurment_unit mu,
 		pms_raw_material rm
		where rmr.id_production_process_activity = pid_production_process_activity
		and rmr.is_active = 'S'
		and rmr.validity_end_date is null
		and rmr.id_raw_material=rm.id
		and rmr.id_measurment_unit=mu.id;
		b1_error_message		text;
		b1_error_message_hint	text;
		
		v_order_currency_calculated_activity_cost			numeric:=0;
	begin
		for vlooprecord in vcursor_raw_material_requeriments loop
			select tariff.tariff_id,tariff.id_currency,rmc.tariff_amount into vrecord
			from pms_raw_material_cost rmc, pms_tariff tariff			
			where
			rmc.id_raw_material = vlooprecord.id_raw_material
			and rmc.validity_end_date is null
			and rmc.is_active = 'S'
			and rmc.id_tariff = tariff.id;

			if vrecord.id_currency is null then
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.calculate.activity.raw.materials.cost.no.valid.raw.material.cost.error'||''||vlooprecord.raw_material_id||''||vlooprecord.measurment_unit_id||''||v_production_process_activity_id||''||v_production_process_id||''||v_product_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;

			--convert to order currency			
			/*porder_id_currency--peso argentino
			record.id_currency--dolares
			record.tariff_id--DOLARES/METRO
			record.tariff_amount--50
			vlooprecord.quantity--5
			vlooprecord.id_measurment_unit--metro*/

			RAISE INFO '--------------------------';
			RAISE INFO 'raw material requirement: %',vlooprecord.raw_material_description;
			RAISE INFO 'requirement measurment unit: %',vlooprecord.measurment_unit_description;
			RAISE INFO 'requirement quantity: %',vlooprecord.quantity;
			RAISE INFO 'tariff: %',vrecord.tariff_id;
			RAISE INFO 'tariff amount: %',vrecord.tariff_amount;			
			RAISE INFO '--------------------------';

			v_order_currency_calculated_activity_cost:= v_order_currency_calculated_activity_cost + f_convert_currency_amount(vrecord.id_currency,porder_id_currency, (vlooprecord.quantity * vrecord.tariff_amount));
			
		end loop;--for vlooprecord in vcursor_raw_material_requeriments loop
		return v_order_currency_calculated_activity_cost;
	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' THEN
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

        SELECT 
        		p_i_pms_production_process_activity(
        			nextval('pms_production_process_activity_id_sq')::BIGINT,
        			'xxxxx xxxxx'::varchar,
        			'xxxxx xxxxx'::varchar,
        			1::BIGINT,
        			8::BIGINT,
        			null::BIGINT,
        			null::timestamp,
        			'xxxxx xxxxx'::varchar)

--drop view v_activities_requirements_costs
create view v_activities_requirements_costs as
select 
ppa.activity_id,
rm.raw_material_id,
mu.measurment_unit_id,
rmr.quantity,
rmc.tariff_amount,
tariff.tariff_id,
ppa.id id_activity
from 
pms_production_process_activity ppa join 
pms_raw_material_requirement rmr
	on rmr.id_production_process_activity = ppa.id 
join 
pms_measurment_unit mu
	on rmr.id_measurment_unit=mu.id
join 
pms_raw_material rm
	on rm.id = rmr.id_raw_material
left outer join 
pms_raw_material_cost rmc
	on rmr.id_raw_material = rmc.id_raw_material and rmc.validity_end_date is null and rmc.is_active = 'S'
left outer join 
pms_tariff tariff
	on rmc.id_tariff = tariff.id
where
	--ppa.id = 7
	--and 
	ppa.validity_end_date is null
	and rmr.is_active = 'S'
	and rmr.validity_end_date is null;

select * from v_activities_requirements_costs
select f_calculate_activity_raw_materials_cost(7,75);
