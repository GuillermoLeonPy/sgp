/*75-UC-ES-MPD-p_effectuate_ppai_raw_material_supply-201611140956*/

create or replace function p_effectuate_ppai_raw_material_supply
(
pid_production_activity_instance		bigint,
plast_modif_user		   			varchar
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
	RAISE INFO 'executing p_effectuate_ppai_raw_material_supply';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vproduction_activity_instance_record	record;
		vpai_raw_material_supply_record		record;
		
	begin
		select * into vproduction_activity_instance_record
		from pms_production_activity_instance where id = pid_production_activity_instance;

		if vproduction_activity_instance_record.require_parcial_product_recall = 'S'
		and vproduction_activity_instance_record.status != 'application.common.status.partial.result.recalled'
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.effectuate.ppai.raw.material.supply.activity.no.in.required.status.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.partial.result.recalled'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		elsif vproduction_activity_instance_record.require_parcial_product_recall = 'N'
		and vproduction_activity_instance_record.status != 'application.common.status.assigned' 
		then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.effectuate.ppai.raw.material.supply.activity.no.in.required.status.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.assigned'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;			
		end if;
		
		select id_production_activity_instance, count(id) count_id
		into vpai_raw_material_supply_record
		from pms_production_activity_instance_raw_material_supply
		where id_production_activity_instance = vproduction_activity_instance_record.id
		group by id_production_activity_instance;

		if vpai_raw_material_supply_record.id_production_activity_instance is null
		or vpai_raw_material_supply_record.count_id = 0 then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.effectuate.ppai.raw.material.supply.activity.has.no.raw.material.supply.record.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		update pms_production_activity_instance_raw_material_supply
		set raw_material_effective_departure_date = now(),
		last_modif_user = plast_modif_user, last_modif_date = now()
		where id_production_activity_instance = vproduction_activity_instance_record.id;

		declare
			vcursor cursor for
			select * from
			pms_production_activity_instance_raw_material_supply
			where id_production_activity_instance = vproduction_activity_instance_record.id;
			vraw_material_existence_record record;
		begin
			for vrecord in vcursor loop
				select * into vraw_material_existence_record from pms_raw_material_existence
				where id = vrecord.id_raw_material_existence_affected;
				
				update pms_raw_material_existence
				set efective_quantity = vraw_material_existence_record.efective_quantity - vrecord.quantity,
				last_modif_user = plast_modif_user, last_modif_date = now()
				where id = vraw_material_existence_record.id;				
			end loop;
		end;

		update pms_production_activity_instance
			set 
			previous_status = vproduction_activity_instance_record.status,
			status = vproduction_activity_instance_record.next_status,
			next_status = 'application.common.status.finalized',
			activity_start_work_date = now(),
			last_modif_user = plast_modif_user,
			last_modif_date = now()
		where id = vproduction_activity_instance_record.id;			
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