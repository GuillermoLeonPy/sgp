/*63-TEST-f_determinate_product_price_identifiying_order-201610311908*/


create or replace function f_determinate_product_price_identifiying_order
(
porder_identifier_number	 		   			bigint,
pid_product	   		 		   			bigint,
porder_id_currency 		 		   			bigint,
pcreation_user								varchar
)


begin transaction;

select f_determinate_product_price_identifiying_order(23,4,61,'xxx');
select f_determinate_product_price_identifiying_order(23,6,61,'xxx');

rollback;
commit;

select * from pms_order_budget_product
select * from pms_order_budget_production_process_activity
select * from pms_order_budget_production_process_activity_machine
select * from pms_order_budget_production_process_activity_manpower
select * from pms_order_budget_production_process_activity_raw_m

select * from pms_product where product_id = 'EDU-MEDIA-CST - PAN-MASC-OFICIAL-CST-EM-2014';

select * from pms_product where product_id = 'EDU-MEDIA-CST - CAM-MASC-OFICIAL-CST-EM-2014';
