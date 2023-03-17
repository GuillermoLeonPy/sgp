/*74-VIEWS-order_raw_material_sufficiency_report-201611032203*/


select * from v_app_dto_order_item_sufficiency_report_detail
create or replace view v_app_dto_order_item_sufficiency_report_detail
as
select
oirmsrd.id,
oirmsrd.id_order_item_raw_material_sufficiency_report,
ormsr.id_order,--PARA POSTERIORMENTE PODER AGRUPAR Y SUMAR POR TODA LA ORDEN
oirmsr.id_order_item,--PARA POSTERIORMENTE PODER AGRUPAR Y SUMAR POR ITEM
oirmsrd.id_raw_material,
rm.raw_material_id,
oirmsrd.id_measurment_unit,
mu.measurment_unit_id,
oirmsrd.available_quantity,
sum(oirmsrd.required_quantity) sum_required_quantity,
(sum(oirmsrd.required_quantity) - oirmsrd.available_quantity) missing_quantity
from
pms_order_raw_material_sufficiency_report ormsr,
pms_order_item_raw_material_sufficiency_report oirmsr,
pms_order_item_raw_material_sufficiency_report_detail oirmsrd,
pms_raw_material rm,
pms_measurment_unit mu
where
ormsr.id = oirmsr.id_order_raw_material_sufficiency_report
and oirmsr.id = oirmsrd.id_order_item_raw_material_sufficiency_report
and ormsr.is_valid = 'S'
and oirmsrd.id_raw_material = rm.id
and oirmsrd.id_measurment_unit = mu.id
group by
oirmsrd.id,
ormsr.emission_date,
ormsr.id_order,
oirmsr.id_order_item,
oirmsrd.id_order_item_raw_material_sufficiency_report,
oirmsrd.id_raw_material,
rm.raw_material_id,
oirmsrd.id_measurment_unit,
mu.measurment_unit_id,
oirmsrd.available_quantity
order by 
ormsr.emission_date desc,
ormsr.id_order,
oirmsr.id_order_item,
rm.raw_material_id,
mu.measurment_unit_id


select * from v_app_dto_order_item_sufficiency_report
create or replace view v_app_dto_order_item_sufficiency_report
as
select
oirmsr.id,
oirmsr.id_order_raw_material_sufficiency_report,
oirmsr.id_order_item,
oi.id_order,
oi.id_product,
prod.product_id,
prod.product_description,
oi.quantity item_quantity,
oirmsr.entered_into_producction_quantity,
oirmsr.pending_to_instanciate_quantity,
oi.canceled_entering_production_by_document_quantity
from
pms_order_raw_material_sufficiency_report ormsr,
pms_order_item_raw_material_sufficiency_report oirmsr,
pms_order_item oi,
pms_product prod
where
ormsr.id = oirmsr.id_order_raw_material_sufficiency_report
and ormsr.is_valid = 'S'
and oi.id = oirmsr.id_order_item
and prod.id = oi.id_product
order by 
ormsr.emission_date desc,
prod.product_id,
prod.product_description


select * from v_app_dto_order_sufficiency_report

create or replace view v_app_dto_order_sufficiency_report
as
select
ormsr.id,
ormsr.id_order,
ormsr.emission_date report_emission_date,
ord.identifier_number order_identifier_number,
si.id id_sale_invoice,
si.identifier_number sale_invoice_identifier_number,
si.emission_date sale_invoice_emission_date,
si.bussines_name,
si.bussines_ci_ruc,
si.payment_condition sale_invoice_payment_condition,
si.status sale_invoice_status,
si.total_amount sale_invoice_total_amount,
cur.id_code currency_id_code
from
pms_order_raw_material_sufficiency_report ormsr,
pms_order ord,
pms_sale_invoice si,
pms_currency cur
where
ormsr.is_valid = 'S'
and ormsr.id_order = ord.id
and ord.id = si.id_order
and si.status != 'application.common.status.annulled'
and si.id_currency = cur.id;