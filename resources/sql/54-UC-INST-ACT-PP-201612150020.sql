/*54-UC-INST-ACT-PP-201611031616*/



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

