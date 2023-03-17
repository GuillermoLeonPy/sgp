/*73-TEST-p_instantiate_production_activities-201611031650*/

create or replace function p_instantiate_production_activities
(
pid_order			 				bigint,
pcreation_user	 		   			varchar
)

begin transaction;
select p_instantiate_production_activities(15,'xxx');
select p_supply_rm_existence_by_order_prod_rm_mu_req_for_one_unit(31,7,1,'xxx');
select * from pms_raw_material_existence
select * from v_order_raw_material_sufficiency_report
select * from pms_production_activity_instance_raw_material_supply
select * from pms_production_activity_instance
select * from pms_order_item where id_order = 15;
select * from pms_order where id = 15;
rollback;

select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
where order_identifier_number = 31 and id_product = 4;
id_product = 4 order= 31 --requires 4 units
id_product = 7 order= 31 --requires 3 units
select * from pms_order where identifier_number = 31;


select * from pms_product where product_id = 'EDU-MEDIA-CST - POL-FEM-GALA-CST-EM-2014';

select * from pms_product where id = 4
select * from pms_product where id = 7

begin transaction;

select f_determinate_product_price_identifiying_order(31,4,61,'xxx');



create or replace function p_supply_rm_existence_by_order_prod_rm_mu_req_for_one_unit
(
porder_identifier_number			 	bigint,
pid_product						bigint,
pproduct_instances_to_satisfy			bigint,
pcreation_user	 		   			varchar
)
returns void as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_supply_rm_existence_by_order_prod_rm_mu_req_for_one_unit';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vcursor cursor for
		select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
		where 
		order_identifier_number = porder_identifier_number
		and id_product = pid_product;

		vraw_material_existence_record	record;
	begin
		for vcounter in 1..pproduct_instances_to_satisfy loop
			for vrecord in vcursor loop
				select * into vraw_material_existence_record
				from pms_raw_material_existence
				where id_raw_material = vrecord.id_raw_material 
				and id_measurment_unit = vrecord.id_measurment_unit;
	
				if vraw_material_existence_record.id is not null then
					update pms_raw_material_existence
					set
					calculated_quantity = vraw_material_existence_record.calculated_quantity + vrecord.required_one_unit_quantity,
					efective_quantity = vraw_material_existence_record.efective_quantity + vrecord.required_one_unit_quantity
					where id = vraw_material_existence_record.id;
				else
					insert into pms_raw_material_existence
					(id,
					id_raw_material,
					id_measurment_unit,
					calculated_quantity,
					limit_calculated_quantity,
					efective_quantity,
					automatic_purchase_request_quantity,
					creation_user
					) values
					(nextval('pms_raw_material_existence_id_sq'),
					vrecord.id_raw_material,
					vrecord.id_measurment_unit,
					vrecord.required_one_unit_quantity,
					1,
					vrecord.required_one_unit_quantity,
					1,
					'xxx'
					);
				end if;
			end loop;
		end loop;
	end;

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
