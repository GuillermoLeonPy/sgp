/*71-TEST-f_determinate_minimal_quantity_to_instantiate_into_production-201611031239*/

begin transaction;

rollback;
select * from pms_order where identifier_number = 21
select p_check_raw_material_sufficiency(10,'xxx');

select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
where order_identifier_number = 21 and id_product = 6

select f_determinate_minimal_quantity_to_instantiate_into_production(21,6);


select * from v_order_raw_material_sufficiency_report

select * from pms_order_item_raw_material_sufficiency_report

select id_order, count()

create or replace view
v_order_raw_material_sufficiency_report
as
select
ormsr.emission_date,
ormsr.is_valid,
ormsr.id_order,
oirmsr.id_order_item,
oirmsr.entered_into_producction_quantity,
oirmsr.pending_to_instanciate_quantity,
oirmsrd.id_raw_material,
oirmsrd.id_measurment_unit,
oirmsrd.required_quantity,
oirmsrd.available_quantity
from
pms_order_raw_material_sufficiency_report ormsr,
pms_order_item_raw_material_sufficiency_report oirmsr,
pms_order_item_raw_material_sufficiency_report_detail oirmsrd
where
ormsr.id = oirmsr.id_order_raw_material_sufficiency_report
and oirmsr.id = oirmsrd.id_order_item_raw_material_sufficiency_report
and ormsr.is_valid = 'S'
order by 
ormsr.emission_date desc,
ormsr.id_order,
oirmsr.id_order_item,
ormsr.is_valid;


select * from f_ordered_production_process_activities(7)
CORTE CORTE
CORTE CORTE 1
CORTE CORTE 1.2
CORTE CORTE 2
CORTE CORTE 1.5

select * from v_ordererd_prod_process_activities_by_o_budget_prod_proc_activity


create or replace view
v_ordererd_prod_proc_activities_by_o_budget_prod_proc_activity
as
select 
obp.order_identifier_number,
obp.id_product,
obppa.id id_order_budget_production_process_activity,
obppa.id_production_process,
obppa.id_production_process_activity,
ppa.activity_id
from
pms_order_budget_product obp,
pms_order_budget_production_process_activity obppa,
pms_production_process_activity ppa
where
obp.id = obppa.id_order_budget_product
and obppa.id_production_process_activity = ppa.id
order by obp.id,obppa.id;
/*HERE VERY IMPORTANT TO READ THE SAME ORDER IN THAT RECORD HAS BEEN CREATED*/
