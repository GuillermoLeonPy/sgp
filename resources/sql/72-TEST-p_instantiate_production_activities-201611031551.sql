/*72-TEST-p_instantiate_production_activities-201611031551*/


create or replace function p_instantiate_production_activities
(
pid_order			 				bigint,
pcreation_user	 		   			varchar
)


select * from pms_order where id = 10;
update pms_order set status = 'application.common.status.pre.production' where id = 10;

update pms_order_item set status = 'application.common.status.pre.production' 
where id_order = 10
and status != 'application.common.status.discarded';

delete from pms_production_activity_instance;


begin transaction;

select p_instantiate_production_activities(10,'xxx');

select * from v_order_raw_material_sufficiency_report

rollback;
