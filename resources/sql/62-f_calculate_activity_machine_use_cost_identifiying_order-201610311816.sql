/*62-f_calculate_activity_machine_use_cost_identifiying_order-201610311816*/

create or replace function f_calculate_activity_machine_use_cost_identifiying_order
(
pid_order_budget_production_process_activity		bigint,
pid_production_process_activity	   			bigint,
porder_id_currency 		 		   			bigint,
pcreation_user								varchar
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
	RAISE INFO 'executing f_calculate_activity_machine_use_cost_identifiying_order';
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
			count(mr.id) rmr_count 
		into vrecord 
		from 
			pms_product prod join
			pms_production_process pp
			on pp.id_product = prod.id join
			pms_production_process_activity ppa
			on ppa.id_production_process = pp.id
			left outer join
			pms_machine_requirement mr
			on
			ppa.id = mr.id_production_process_activity
			and mr.validity_end_date is null
			and mr.is_active = 'S'
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
			RAISE INFO '--------------------------';
			RAISE INFO 'machine requeriments count: %',vrecord.rmr_count;
			RAISE INFO 'returning value zero';
			return 0;	
		end if;
	end;

	declare
		vrecord record;
		vcursor_machine_requeriment cursor for
		select m.machine_description,
		mr.id_machine,mr.minutes_quantity,mr.id id_machine_requirement
 		from pms_machine_requirement mr,
 		pms_machine m
		where mr.id_production_process_activity = pid_production_process_activity
		and mr.is_active = 'S'
		and mr.validity_end_date is null
		and mr.id_machine=m.id;
		
		b1_error_message		text;
		b1_error_message_hint	text;
		
		v_order_currency_calculated_activity_cost			numeric:=0;
	begin
		for vlooprecord in vcursor_machine_requeriment loop
			select tariff.tariff_id,tariff.id_currency,mc.tariff_amount, mc.id id_machine_use_cost
			into vrecord
			from pms_machine_use_cost mc, pms_tariff tariff			
			where
			mc.id_machine = vlooprecord.id_machine
			and mc.validity_end_date is null
			and mc.is_active = 'S'
			and mc.id_tariff = tariff.id;

			if vrecord.id_currency is null then
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.calculate.activity.machine.use.cost.no.valid.machine.use.cost.error'||''||vlooprecord.machine_description||''||v_production_process_activity_id||''||v_production_process_id||''||v_product_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;

			if pid_order_budget_production_process_activity is not null then
				PERFORM p_i_pms_order_budget_production_process_activity_machine(pid_order_budget_production_process_activity,vlooprecord.id_machine_requirement,vrecord.id_machine_use_cost,pcreation_user);
			end if;		
			--convert to order currency			
			/*porder_id_currency--peso argentino
			record.id_currency--dolares
			record.tariff_id--DOLARES/METRO
			record.tariff_amount--50
			vlooprecord.quantity--5
			vlooprecord.id_measurment_unit--metro*/

			RAISE INFO '--------------------------';
			RAISE INFO 'machine requirement: %',vlooprecord.machine_description;
			RAISE INFO 'requirement minutes quantity: %',vlooprecord.minutes_quantity;
			RAISE INFO 'tariff: %',vrecord.tariff_id;
			RAISE INFO 'tariff amount: %',vrecord.tariff_amount;			
			RAISE INFO '--------------------------';

			v_order_currency_calculated_activity_cost:= v_order_currency_calculated_activity_cost + f_convert_currency_amount(vrecord.id_currency,porder_id_currency, (vlooprecord.minutes_quantity * vrecord.tariff_amount));
			
		end loop;--for vlooprecord in vcursor_raw_material_requeriments loop
		return v_order_currency_calculated_activity_cost;
	end;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989'  or SQLSTATE 'P9999' THEN
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