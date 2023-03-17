/*35-UC-REG-PED-f_calculate_activity_man_power_cost-201609291955*/
create or replace function f_calculate_activity_man_power_cost
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
	RAISE INFO 'executing f_calculate_activity_man_power_cost';
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
			count(mpr.id) rmr_count 
		into vrecord 
		from 
			pms_product prod join
			pms_production_process pp
			on pp.id_product = prod.id join
			pms_production_process_activity ppa
			on ppa.id_production_process = pp.id
			join
			pms_man_power_requirement mpr
			on
			ppa.id = mpr.id_production_process_activity
			and mpr.validity_end_date is null
			and mpr.is_active = 'S'
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
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.calculate.activity.man.power.cost.no.valid.requirements.error'||''||vrecord.activity_id||''||vrecord.process_id||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;
	end;

	declare
		vrecord record;
		vcursor_man_power_requirement cursor for
		select mpr.minutes_quantity
 		from pms_man_power_requirement mpr
		where mpr.id_production_process_activity = pid_production_process_activity
		and mpr.is_active = 'S'
		and mpr.validity_end_date is null;
		
		b1_error_message		text;
		b1_error_message_hint	text;
		
		v_order_currency_calculated_activity_cost			numeric:=0;
	begin
		for vlooprecord in vcursor_man_power_requirement loop
			select tariff.tariff_id,tariff.id_currency,mpc.tariff_amount into vrecord
			from pms_manpower_cost mpc, pms_tariff tariff			
			where
			mpc.validity_end_date is null
			and mpc.is_active = 'S'
			and mpc.id_tariff = tariff.id;

			if vrecord.id_currency is null then
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.calculate.activity.man.power.cost.no.valid.cost.error'||''||v_production_process_activity_id||''||v_production_process_id||''||v_product_id||'end.of.message';
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
			RAISE INFO 'man power requirement minutes quantity: %',vlooprecord.minutes_quantity;
			RAISE INFO 'tariff: %',vrecord.tariff_id;
			RAISE INFO 'tariff amount: %',vrecord.tariff_amount;			
			RAISE INFO '--------------------------';

			v_order_currency_calculated_activity_cost:= v_order_currency_calculated_activity_cost + f_convert_currency_amount(vrecord.id_currency,porder_id_currency, (vlooprecord.minutes_quantity * vrecord.tariff_amount));
			
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

--drop table vrecord;
select * from v_activities_man_power_requirement_cost
15 * 18500 =  277500 gs --> gs 277 500 / 5550 = 50
select f_calculate_activity_man_power_cost(7,62);


create view v_activities_man_power_requirement_cost as
select 
ppa.activity_id,
mpr.minutes_quantity,
mpc.tariff_amount,
tariff.tariff_id,
ppa.id id_activity
from 
pms_production_process_activity ppa join 
pms_man_power_requirement mpr
	on mpr.id_production_process_activity = ppa.id 
left outer join 
pms_manpower_cost mpc
	on mpc.validity_end_date is null and mpc.is_active = 'S'
left outer join 
pms_tariff tariff
	on mpc.id_tariff = tariff.id
where
	--ppa.id = 7
	--and 
	ppa.validity_end_date is null
	and mpr.is_active = 'S'
	and mpr.validity_end_date is null;
