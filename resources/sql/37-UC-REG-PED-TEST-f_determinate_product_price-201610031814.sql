/* 37-UC-REG-PED-TEST-f_determinate_product_price-201610031814 */
select f_calculate_activity_raw_materials_cost(7,61);
--ussd: 1.65541
--pyg: 9,187.5

select f_calculate_activity_machine_use_cost(7,61);
--ussd: 2.7
--pyg: 14,985
select f_calculate_activity_man_power_cost(7,61);
--ussd: 50
--pyg: 277,500
--activity: 7, ussd total : 54.35541 --> pyg: 301672.5255
--activity: 7, pyg total : 301672.5 --> ussd: 54.355405405

--tasa de cambio ussd: 5550 pyg



select f_determinate_product_price(4,61);
--ussd: 209.27 --> pyg: 1 161 448.5
--pyg: 1 161 386.00 --> ussd: 209.258738739


select * from pms_product where id = 4