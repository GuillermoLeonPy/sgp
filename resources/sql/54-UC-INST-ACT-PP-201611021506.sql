/*54-UC-INST-ACT-PP-201611030039*/

create or replace function p_instantiate_production_activities
(
pid_order			 				bigint,
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
	RAISE INFO 'executing p_instantiate_production_activities';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record							record;
		v_order_item_cursor cursor for
		select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;
		vorder_budget_product					record;
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;

		if vorder_record.status != 'application.common.status.pre.production' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.instantiate.production.activities.order.not.in.required.status.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vorder_record.status != 'application.common.status.pre.production' then


		--check raw materia sufficiency to instantiate production activities by order
		--1) invalidate if exists pms_order_raw_material_insufficiency_report is valid
		--2) if not enough raw material: make a pms_order_raw_material_insufficiency_report			
			--2.1) make a pms_order_raw_material_insufficiency_report
		

		for vorder_item_record in v_order_item_cursor loop
			select * into vorder_budget_product from pms_order_budget_product 
			where order_identifier_number = vorder_record.identifier_number
			and id_product = vorder_item_record.id_product;

			--establish the limit counter for the activities instantiation loop
			-------------------------------------------------------------------
			--check if a valid pms_order_raw_material_insufficiency_report exists
				--1) identify pms_order_raw_material_insufficiency_report
				--2) identify the pms_order_item_raw_material_insufficiency_report
					--2.1) if no record found for the item
						--2.1.1) establish the limit counter to the pms_order_item.pending_to_instanciate_quantity
					--2.2) if exists record for the item
						--2.2.1) determinate the quantity that can be instantiated into production
						--2.2.2) establish the limit counter to the quantity that can be instantiated into production
						
			--if no valid pms_order_raw_material_insufficiency_report exists
				--1) establish the limit counter to pms_order_item.pending_to_instanciate_quantity
			
			for vitem_product_counter in 1..vorder_item_record.quantity loop
				v_product_instance_unique_number := nextval('pms_pai_product_instance_unique_number_sq');
				vid_production_activity_first := null;
				declare
					v_activities_cursor cursor for
					select * from f_ordered_production_process_activities(vid_production_process);
				begin
					for vproduction_activity_record in v_activities_cursor loop
						if vid_production_activity_first is null then
							vid_production_activity_first := vproduction_activity_record.id;
						end if;
						if vid_production_activity_first = vproduction_activity_record.id then
							vrequire_parcial_product_recall := 'N';
							vis_asignable := 'S';
						else
							vrequire_parcial_product_recall := 'S';
							vis_asignable := 'N';
						end if;
						vid_production_activity_instance := nextval('pms_production_activity_instance_id_sq');
						insert into pms_production_activity_instance
						(id,id_production_activity,id_order,id_order_item,
						id_product,require_parcial_product_recall,
						activity_instance_unique_number,product_instance_unique_number,
						is_asignable,
						creation_user)
						values
						(vid_production_activity_instance,vproduction_activity_record.id,vorder_record.id,vorder_item_record.id,
						vorder_item_record.id_product,vrequire_parcial_product_recall,
						nextval('pms_pai_activity_instance_unique_number_sq'),v_product_instance_unique_number,
						vis_asignable,
						pcreation_user);
					end loop;--for vproduction_activity_record in vcursor loop
					--update the last activity instance record to require deliver product instance
					update pms_production_activity_instance 
					set delivers_product_instance = 'S',delivers_partial_result = 'N'
					where id = vid_production_activity_instance; 
				end;--begin
			end loop;--for vitem_product_counter in 1..vorder_item_record.quantity loop			
		end loop;--for vorder_item_record in vcursor loop
		
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

select f_check_production_process_integrity(4)
select p_instantiate_production_activities(10,'xxx');
select * from f_ordered_production_process_activities(7);;

select * from pms_order where id = 10;
select * from pms_order_item where id = 12--id_order = 10;

select * from pms_production_process where

update pms_production_process set is_enable = 'S';


select f_test_check_production_process_integrity(10);



create or replace function f_test_check_production_process_integrity
(pid_order			 				bigint)
returns void as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;

	vid_production_process					pms_production_process.id%type;
	vcursor cursor for
		select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_test_check_production_process_integrity';
	RAISE INFO '--------------------------';

	for vorder_item_record in vcursor loop	
			RAISE INFO '**************************';
			RAISE INFO 'vorder_item_record.id: %',vorder_item_record.id;
			RAISE INFO 'vorder_item_record.id_product: %',vorder_item_record.id_product;
			RAISE INFO '**************************';
			vid_production_process := f_check_production_process_integrity(vorder_item_record.id_product);
			RAISE INFO '**************************';
			RAISE INFO 'vid_production_process: %',vid_production_process;
			RAISE INFO '**************************';
	end loop;
	

	
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';