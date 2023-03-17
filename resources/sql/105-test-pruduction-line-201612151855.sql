begin transaction;

SELECT p_instantiate_production_activities( 35, null);

rollback;


			select 
					osr.id,
					osr.id_order,
					osr.report_emission_date,
					osr.order_identifier_number,
					osr.id_sale_invoice,
					osr.sale_invoice_identifier_number,
					osr.sale_invoice_emission_date,
					osr.bussines_name,
					osr.bussines_ci_ruc,
					osr.sale_invoice_payment_condition,
					osr.sale_invoice_status,
					osr.sale_invoice_total_amount,
					osr.currency_id_code					
			from
				v_app_dto_order_sufficiency_report osr
				where osr.id_order = 35


			select 
				oisr.id,
				oisr.id_order_raw_material_sufficiency_report,
				oisr.id_order_item,
				oisr.id_order,
				oisr.id_product,
				oisr.product_id,
				oisr.product_description,
				oisr.item_quantity,
				oisr.entered_into_producction_quantity,
				(select orderitem.in_progress_quantity from pms_order_item orderitem where orderitem.id = oisr.id_order_item) in_progress_quantity, 
				oisr.pending_to_instanciate_quantity,
				oisr.canceled_entering_production_by_document_quantity					
			from
				v_app_dto_order_item_sufficiency_report oisr
				where
				oisr.id_order_raw_material_sufficiency_report = 87

select * from v_order_raw_material_sufficiency_report
where id_order = 35 and id_order_item = 63

select * from pms_order_item where id_order = 35

select * from pms_order where id = 35
select * from v_order_product_raw_mat_measur_unit_required_for_one_unit
where order_identifier_number = 75
and id_raw_material = 5
and id_measurment_unit = 10;


create or replace function p_i_production_activity_instance_raw_material_supply
(
pid_production_activity_instance				bigint,
pid_order_budget_production_process_activity		bigint,
pcreation_user			   					varchar
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
	RAISE INFO 'executing p_production_activity_instance_raw_material_supply';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_budget_production_process_activity_raw_m_cursor cursor for
		select * from pms_order_budget_production_process_activity_raw_m
		where id_order_budget_production_process_activity = pid_order_budget_production_process_activity;

		vproduction_activity_instance_record		record;
		vraw_material_requirement_record			record;
		vproduction_process_record				record;
		vproduction_process_activity_record		record;
		vraw_material_record					record;
		vmeasurment_unit_record					record;
		vraw_material_existence_record			record;
		
	begin

		select * into vproduction_activity_instance_record from pms_production_activity_instance
		where id = pid_production_activity_instance;
	
		for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
			select * into vraw_material_requirement_record
			from pms_raw_material_requirement where id = 
			vorder_budget_production_process_activity_raw_m_record.id_raw_material_requirement;

			select * into vproduction_process_activity_record from pms_production_process_activity
			where id = vproduction_activity_instance_record.id_production_activity;
			select * into vproduction_process_record from pms_production_process 
			where id = vproduction_process_activity_record.id_production_process;

			select * into vraw_material_record from pms_raw_material
			where id = vraw_material_requirement_record.id_raw_material;
			select * into vmeasurment_unit_record from pms_measurment_unit
			where id = vraw_material_requirement_record.id_measurment_unit;

			select * into vraw_material_existence_record from pms_raw_material_existence
			where id_raw_material = vraw_material_record.id
			and id_measurment_unit = vmeasurment_unit_record.id;
												
			insert into pms_production_activity_instance_raw_material_supply
			(id,
			id_production_activity,
			activity_description,
			process_description,
			id_production_activity_instance,
			id_raw_material_requirement,
			id_raw_material,raw_material_description,
			id_measurment_unit,measurment_unit_description,
			quantity,
			id_raw_material_existence_affected,
			creation_user)
			values
			(nextval('pms_production_activity_instance_raw_material_supply_id_sq'),
			vproduction_process_activity_record.id,
			vproduction_process_activity_record.activity_description,
			vproduction_process_record.process_description,
			vproduction_activity_instance_record.id,
			vraw_material_requirement_record.id,							
			vraw_material_record.id,vraw_material_record.raw_material_id,
			vmeasurment_unit_record.id,vmeasurment_unit_record.measurment_unit_id,
			vraw_material_requirement_record.quantity,
			vraw_material_existence_record.id,
			pcreation_user);
			
			RAISE INFO '--------------------------';
			RAISE INFO '--------------------------';			
			RAISE INFO '**-update pms_raw_material_existence-**';
			RAISE INFO 'id_product : %',vproduction_activity_instance_record.id_product;
			RAISE INFO 'vraw_material_requirement_record.quantity : %',vraw_material_requirement_record.quantity;
			RAISE INFO 'vraw_material_existence_record.calculated_quantity : %',vraw_material_existence_record.calculated_quantity;
			RAISE INFO 'calculated_quantity = %', (vraw_material_existence_record.calculated_quantity - vraw_material_requirement_record.quantity);
			RAISE INFO 'vraw_material_existence_record.id : %', vraw_material_existence_record.id;
			RAISE INFO '--------------------------';
			update pms_raw_material_existence
			set calculated_quantity = vraw_material_existence_record.calculated_quantity - vraw_material_requirement_record.quantity,
			last_modif_user = pcreation_user,
			last_modif_date = now()
			where id = vraw_material_existence_record.id;
			
		end loop;--for vorder_budget_production_process_activity_raw_m_record in vorder_budget_production_process_activity_raw_m_cursor loop
	end;--begin
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



/*******************************************************************************************/
/********************************************************************************************/
create or replace function p_instantiate_production_activities_by_order_item
(
pid_order				 				bigint,
pid_order_item			 				bigint,
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
	RAISE INFO 'executing p_instantiate_production_activities_by_order_item';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record							record;
		vorder_item_record								record;
		vorder_budget_product							record;
		view_order_raw_material_sufficiency_report_record		record;
		vinstanciable_product_quantity					bigint;
		v_custom_order_raw_material_sufficiency_report_record		record;
		
		v_order_item_actual_in_progress_quantity			bigint;
		
		v_product_instance_unique_number			pms_production_activity_instance.product_instance_unique_number%type;
		vid_production_process					pms_production_process.id%type;
		vrequire_parcial_product_recall			pms_production_activity_instance.require_parcial_product_recall%type;
		vid_production_activity_first				pms_production_activity_instance.id%type;
		vid_production_activity_last				pms_production_activity_instance.id%type;
		vid_production_activity_instance			pms_production_activity_instance.id%type;
		vis_asignable							pms_production_activity_instance.is_asignable%type;		
	begin
			select ord.* into vorder_record from pms_order ord where ord.id = pid_order;
			select * into vorder_item_record from pms_order_item where id = pid_order_item;
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
			select * into view_order_raw_material_sufficiency_report_record
			from v_order_raw_material_sufficiency_report
			where id_order = vorder_record.id and id_order_item = vorder_item_record.id;
			if view_order_raw_material_sufficiency_report_record.id_order_item is null then
				vinstanciable_product_quantity := vorder_item_record.pending_to_instanciate_quantity;
			else
				vinstanciable_product_quantity := view_order_raw_material_sufficiency_report_record.entered_into_producction_quantity;
			end if;--if view_order_raw_material_sufficiency_report_record.id_order_item is null then

			if vinstanciable_product_quantity > 0 then
				for vitem_product_counter in 1..vinstanciable_product_quantity loop
					v_product_instance_unique_number := nextval('pms_pai_product_instance_unique_number_sq');
					vid_production_activity_first := null;
					declare
						v_activities_cursor cursor for
						select * from v_ordererd_prod_proc_activities_by_o_budget_prod_proc_activity
						where order_identifier_number = vorder_record.identifier_number
						and id_product = vorder_item_record.id_product;
					begin
						for vproduction_activity_record in v_activities_cursor loop
							if vid_production_activity_first is null then
								vid_production_activity_first := vproduction_activity_record.id_production_process_activity;
							end if;
							if vid_production_activity_first = vproduction_activity_record.id_production_process_activity then
								vrequire_parcial_product_recall := 'N';
								vis_asignable := 'S';
							else
								vrequire_parcial_product_recall := 'S';
								vis_asignable := 'N';
							end if;
							vid_production_activity_instance := nextval('pms_production_activity_instance_id_sq');
							insert into pms_production_activity_instance
							(id,
							id_production_activity,
							id_order,id_order_item,
							id_product,require_parcial_product_recall,
							activity_instance_unique_number,
							product_instance_unique_number,
							is_asignable,
							creation_user)
							values
							(vid_production_activity_instance,
							vproduction_activity_record.id_production_process_activity,
							vorder_record.id,vorder_item_record.id,
							vorder_item_record.id_product,vrequire_parcial_product_recall,
							nextval('pms_pai_activity_instance_unique_number_sq'),
							v_product_instance_unique_number,
							vis_asignable,
							pcreation_user);
							
							--CREATE
							--pms_production_activity_instance_raw_material_supply
							--RECORD AS REQUERIMENTS DICTATE
							--by parameters
								--id_production_activity_instance
								--vproduction_activity_record.id_order_budget_production_process_activity
							PERFORM p_i_production_activity_instance_raw_material_supply
							(vid_production_activity_instance,
							vproduction_activity_record.id_order_budget_production_process_activity,
							pcreation_user);
							
						end loop;--for vproduction_activity_record in vcursor loop
						--update the last activity instance record to require deliver product instance
						update pms_production_activity_instance 
						set delivers_product_instance = 'S',delivers_partial_result = 'N'
						where id = vid_production_activity_instance; 
					end;--begin
				end loop;--for vitem_product_counter in 1..vinstanciable_product_quantity loop

				--	**** VERY IMPORTANT **** 
				--update the pms_order_item record in the attributes
					--pending_to_instanciate_quantity
					--in_progress_quantity
				RAISE INFO '--------------------------';
				RAISE INFO '**** VERY IMPORTANT ****';
				RAISE INFO 'vorder_item_record.id = %',vorder_item_record.id;
				RAISE INFO 'vinstanciable_product_quantity = %',vinstanciable_product_quantity;
				RAISE INFO '--------------------------';
				
					v_order_item_actual_in_progress_quantity := vorder_item_record.in_progress_quantity;
					update pms_order_item
					set 
						status = 'application.common.status.in.progress',
						pending_to_instanciate_quantity = vorder_item_record.pending_to_instanciate_quantity - vinstanciable_product_quantity,
						in_progress_quantity = v_order_item_actual_in_progress_quantity + vinstanciable_product_quantity,
						last_modif_user = pcreation_user, last_modif_date = now()
					where id = vorder_item_record.id;					
				
				
				
				if vorder_record.status = 'application.common.status.pre.production'
				and vinstanciable_product_quantity > 0 then
					update pms_order
						set status = 'application.common.status.in.progress',
						last_modif_user = pcreation_user, last_modif_date = now(),
						production_activities_instantiation_date = now()
						where id = vorder_record.id;			
				end if;
			end if;--if vinstanciable_product_quantity > 0 then
	end;--begin	

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


/**************************************************************************************/
/**************************************************************************************/

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
		
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;

		if vorder_record.status != 'application.common.status.pre.production'
		and vorder_record.status != 'application.common.status.in.progress' then
		/* **************  IMPORTANT ****************** */
		--PRODUCTION ACTIVITYS CAN BE INSTANTIATED SO MANY TIMES AS THE
		--ANY ORDER ITEM HAS pms_order_item.pending_to_instanciate_quantity > 0
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.instantiate.production.activities.order.not.in.required.status.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vorder_record.status != 'application.common.status.pre.production'
		--and vorder_record.status != 'application.common.status.in.progress' then

		--check raw materia sufficiency to instantiate production activities by order
		--parameters
			--id_order
			--pcreation_user			
		PERFORM p_check_raw_material_sufficiency(vorder_record.id,pcreation_user);		
		

	end;--begin	

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



/***************************************************************/

create or replace function p_check_raw_material_sufficiency
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
	RAISE INFO 'executing p_check_raw_material_sufficiency';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_record									record;
		vorder_raw_material_sufficiency_report_record		record;
		vorder_budget_product							record;		
		v_order_item_cursor 							cursor 
		for select * from pms_order_item where id_order = pid_order
		and status != 'application.common.status.discarded' order by id asc;

		v_minimal_quantity_capable_of_produce				bigint;
		
	begin
		select ord.* into vorder_record from pms_order ord
		where ord.id = pid_order;
	
		select * into vorder_raw_material_sufficiency_report_record
		from pms_order_raw_material_sufficiency_report
		where id_order = vorder_record.id and is_valid = 'S';

		if vorder_raw_material_sufficiency_report_record.id is not null then
			update pms_order_raw_material_sufficiency_report
			set is_valid = 'N', last_modif_user = pcreation_user, last_modif_date = now()
			where id = vorder_raw_material_sufficiency_report_record.id;
		end if;--if vorder_raw_material_sufficiency_report_record.id is not null then

		for vorder_item_record in v_order_item_cursor loop
			if vorder_item_record.pending_to_instanciate_quantity > 0 then
				/* v_minimal_quantity_capable_of_produce := 
				determinate the minimal quantity of id_product that can be instantiated into production */
				v_minimal_quantity_capable_of_produce := f_determinate_minimal_quantity_to_instantiate_into_production(vorder_record.identifier_number,vorder_item_record.id_product);			
	
				/* goal : determinate the raw material and measurment unit quantity to be setted in the
				pms_order_item_raw_material_sufficiency_report_detail
				parameters
					v_minimal_quantity_capable_of_produce
					id_order
					id_order_item	
				*/
	
				PERFORM p_calculate_pms_order_item_raw_material_suff_report_detail
					(vorder_record.id,
					vorder_item_record.id,
					v_minimal_quantity_capable_of_produce,
					pcreation_user);
	
	
				PERFORM p_instantiate_production_activities_by_order_item
					(vorder_record.id,
					vorder_item_record.id,
					pcreation_user);
			end if;
			--if vorder_item_record.pending_to_instanciate_quantity > 0 then
		end loop;--for vorder_item_record in v_order_item_cursor loop		
	end;--begin
	
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



/**/
select * from pms_purchase_invoice where identifier_number = '001-001-0000019'
select * from pms_purchase_invoice where identifier_number = '001-001-0000030'
select * from pms_purchase_invoice_item where id_purchase_invoice = 43

begin transaction;
select p_clone_purchase_invoice(31,'001-001-0000030');
commit;
rollback;

/**/
select pscc.id, pscc.product, 
pscc.product_increase_over_cost_for_sale_price,
pscc.order_item_product_unit_manufacture_cost, 
pscc.order_item_unit_price_amount,
pscc.sale_invoice_item_unit_price_amount, 
pscc.sale_invoice_item_quantity, 
pscc.sale_price_acumulated, pscc.production_cost_acumulated, 
pscc.profit, pscc.sale_date 
from f_dates_interval_product_sale_cost_comparison('2016-12-01 00:00:00.0'::timestamp, '2016-12-15 00:00:00.0'::timestamp, 61) pscc 

create or replace function f_dates_interval_product_sale_cost_comparison
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(id									bigint,
product								varchar,
product_increase_over_cost_for_sale_price	bigint,
order_item_product_unit_manufacture_cost	numeric,
order_item_unit_price_amount				numeric,
sale_invoice_item_unit_price_amount		numeric,
sale_invoice_item_quantity				bigint,
sale_price_acumulated					numeric,
production_cost_acumulated				numeric,
profit								numeric,
sale_date								timestamp
)
as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
		vbegin_date		timestamp;
		vend_date			timestamp;
begin
		if pbegin_date > pend_date then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='application.common.validation.exception.begin.date.after.end.date.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		vend_date := ((date_trunc('day', pend_date) + interval '23:59:59') ::timestamp);
		vbegin_date := (date_trunc('day', pbegin_date)::timestamp);

		RETURN QUERY
		select
			pcsc.id,
			pcsc.product,
			pcsc.product_increase_over_cost_for_sale_price,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.order_item_product_unit_manufacture_cost) order_item_product_unit_manufacture_cost,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.order_item_unit_price_amount) order_item_unit_price_amount,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.sale_invoice_item_unit_price_amount) sale_invoice_item_unit_price_amount,
			pcsc.sale_invoice_item_quantity,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.sale_price_acumulated) sale_price_acumulated,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.production_cost_acumulated) production_cost_acumulated,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.profit) profit,
			pcsc.sale_date
		from v_product_cost_and_sale_comparison_by_dates pcsc
		where pcsc.sale_date between vbegin_date and vend_date;

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
