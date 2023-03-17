/*95-REPORTE-v_current_raw_material_insufficiency_report-201612051100*/

--drop view v_current_raw_material_insufficiency_report
create or replace view v_current_raw_material_insufficiency_report as
select
vormsr.id_raw_material,
vormsr.id_measurment_unit,
rme.calculated_quantity calculated_stock_quantity,
sum(vormsr.required_quantity) sum_required_quantity
from 
v_order_raw_material_sufficiency_report vormsr,
pms_raw_material_existence rme
where
vormsr.is_valid = 'S'
and rme.id_raw_material = vormsr.id_raw_material
and rme.id_measurment_unit = vormsr.id_measurment_unit
group by
vormsr.id_raw_material,
vormsr.id_measurment_unit,
rme.calculated_quantity
order by
vormsr.id_raw_material,
vormsr.id_measurment_unit,
rme.calculated_quantity




create or replace view v_current_raw_material_insufficiency_report_with_cost as
select
vcrmir.id_raw_material,
vcrmir.id_measurment_unit,
(vcrmir.sum_required_quantity - vcrmir.calculated_stock_quantity) required_quantity,
case when tariff.id_currency is null then
(select id from pms_currency where currency_local = 'S')
else tariff.id_currency end id_currency,
case when rmcost.tariff_amount is null then 0 else rmcost.tariff_amount end tariff_amount
from 
v_current_raw_material_insufficiency_report vcrmir
left outer join pms_raw_material_cost rmcost
on vcrmir.id_raw_material = rmcost.id_raw_material
and rmcost.validity_end_date is null 
and rmcost.is_active = 'S'
left outer join pms_tariff tariff
on rmcost.id_tariff = tariff.id
order by
vcrmir.id_raw_material,
vcrmir.id_measurment_unit,
required_quantity

/* for test v_current_raw_material_insufficiency_report_with_cost 
update pms_raw_material_cost
set
validity_end_date = now(), is_active = null
where id = 7;

update pms_raw_material_cost
set
validity_end_date = null, is_active = 'S'
where id = 7;
*/

select * from f_current_insufficiency_raw_material_report_by_currency(61);

CONTEMPLAR CASO: NO EXITEN ORDENES INSATISFECHAS; EN TAL CASO EMITIR MENSAJE


update pms_order_raw_material_sufficiency_report set is_valid = 'S' where id = 56;
/*

*/


create or replace function f_current_insufficiency_raw_material_report_by_currency
(pid_currency		bigint)
returns TABLE
(raw_material_id				varchar,
measurment_unit_id	 			varchar,
required_quantity				numeric,
cost_amount					numeric)
as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin

	RETURN QUERY
	select
	rm.raw_material_id,
	mu.measurment_unit_id,
	vcrmairwc.required_quantity,
	f_convert_currency_amount(vcrmairwc.id_currency,pid_currency,((vcrmairwc.required_quantity * vcrmairwc.tariff_amount)::numeric)) cost_amount
	from
		v_current_raw_material_insufficiency_report_with_cost vcrmairwc,
		pms_raw_material rm,
		pms_measurment_unit mu
	where
		vcrmairwc.id_raw_material = rm.id
		and vcrmairwc.id_measurment_unit = mu.id
	order by
		rm.raw_material_id,
		mu.measurment_unit_id;

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