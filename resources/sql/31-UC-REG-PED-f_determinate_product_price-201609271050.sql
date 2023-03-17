/*31-UC-REG-PED-f_determinate_product_price-201609271050*/

/*
1) identificar proceso de produccion vigente
	*) si no encuentra lanza excepcion
2) identificar actividades vigentes de proceso de produccion
	*) si no encuentra lanza excepcion
3) identificar requerimientos de materia prima con sus unidades de medida
	*) si no encuentra ninguno lanza excepcion
4) identificar registro de costo de materia prima segun unidad de medida de requerimiento
	*) si no encuentra lanza excepcion
5) multiplicar registro de costo de materia prima por cantidad del requerimiento
6) convertir el costo resultante a la moneda de cotizacion del pedido
	*) identificar registro de tasa de cambio de moneda; si no encuentra lanza excepcion

7) identificar requerimiento de uso de maquina cantidad de minutos 
8) identificar registro de costo vigente de uso de maquina
	*) si no encuentra ninguno lanza excepcion
9) multiplicar registro de costo vigente de uso de maquina por cantidad de minutos del requerimiento
10) convertir el costo resultante a la moneda de cotizacion del pedido
	*) identificar registro de tasa de cambio de moneda; si no encuentra lanza excepcion

11) identificar requerimiento de mano de obra segun cantidad de minutos
	*) si no encuentra lanza excepcion
12) identificar registro de costo vigente de mano de obra
	*) si no encuentra lanza excepcion
13) multiplicar registro de costo vigente de mano de obra por cantidad de minutos del requerimiento
14) convertir el costo resultante a la moneda de cotizacion del pedido
	*) identificar registro de tasa de cambio de moneda; si no encuentra lanza excepcion
*/

create or replace function f_determinate_product_price
(
pid_product	   		 		   			bigint,
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

	v_production_process_id						pms_production_process.id%type;
	v_production_process_activity_id				pms_production_process_activity.id%type;
	v_raw_material_cost_record_id					pms_raw_material_cost.id%type;

begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_determinate_product_price';
	RAISE INFO '--------------------------';
	
	/*BEGIN VALIDATIONS*/
	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select prodp.id, product.product_id 
		into vrecord
		from 
		pms_product product left outer join
		pms_production_process prodp 
		on product.id = prodp.id_product 
		where product.id = pid_product
		and prodp.validity_end_date is null;
		
		RAISE INFO '--------------------------';
		RAISE INFO 'vrecord.product_id: %',vrecord.product_id;
		RAISE INFO '--------------------------';
		
		if vrecord.id is null then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.determinate.product.price.no.valid.production.process.for.product.error'||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		else
			v_production_process_id:=vrecord.id;
		end if;
	end;

	RAISE INFO '--------------------------';
	RAISE INFO 'v_production_process_id: %',v_production_process_id;
	RAISE INFO '--------------------------';

	declare
		vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select prod.product_id,pp.process_id,count(ppa.id) process_activities_count
			into vrecord
		from 
			pms_product prod join
			pms_production_process pp
			on pp.id_product = prod.id 
			left outer join
			pms_production_process_activity ppa
			on ppa.id_production_process = pp.id			
		where	
			pp.id = v_production_process_id			
			and pp.validity_end_date is null			
			and ppa.validity_end_date is null
			group by prod.product_id,pp.process_id;

		if vrecord.process_activities_count = 0 then
			b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.determinate.product.price.no.valid.production.activities.for.process.error'||''||vrecord.process_id||''||vrecord.product_id||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;
	end;
	
	declare
		vcursor cursor for
		select * from f_ordered_production_process_activities(v_production_process_id);		
	
		v_process_acumulated_raw_material_cost			pms_raw_material_cost.tariff_amount%type;
		v_process_acumulated_man_power_cost			pms_manpower_cost.tariff_amount%type;
		v_process_acumulated_machine_use_cost			pms_machine_use_cost.tariff_amount%type;
	begin
		v_process_acumulated_raw_material_cost:=0;
		v_process_acumulated_man_power_cost:=0;
		v_process_acumulated_machine_use_cost:=0;
		
		for vrecord in vcursor loop			
			v_process_acumulated_raw_material_cost:= v_process_acumulated_raw_material_cost + f_calculate_activity_raw_materials_cost(vrecord.id,porder_id_currency);
			v_process_acumulated_machine_use_cost:= v_process_acumulated_machine_use_cost + f_calculate_activity_machine_use_cost(vrecord.id,porder_id_currency);
			v_process_acumulated_man_power_cost:= v_process_acumulated_man_power_cost + f_calculate_activity_man_power_cost(vrecord.id,porder_id_currency);
		end loop;

		return (v_process_acumulated_raw_material_cost + v_process_acumulated_machine_use_cost + v_process_acumulated_man_power_cost);
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

select * from f_ordered_production_process_activities(7);


select id, activity_id, id_previous_activity from pms_production_process_activity

        SELECT 
        		p_i_pms_production_process(
        			nextval('pms_production_process_id_sq')::BIGINT,
        			'xxxxx xxxxx'::varchar,
        			'xxxxx xxxxx'::varchar,
        			7::BIGINT,
        			now()::timestamp,
        			'xxxxx xxxxx'::varchar);

select * from pms_production_process

select * from v_activities_requirements_costs
select f_determinate_product_price(4,75);
select * from f_ordered_production_process_activities(7);		
