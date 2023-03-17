/*76-UC-REG-FIN-AP-p_finalize_activity-201611061330*/


create or replace function p_finalize_activity
(
pid_production_activity_instance		bigint,
plast_modif_user 		   			varchar
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
	RAISE INFO 'executing p_finalize_activity';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vproduction_activity_instance_record	record;
		vnext_status						varchar(50);
	begin
		select * into vproduction_activity_instance_record
		from pms_production_activity_instance where id = pid_production_activity_instance;

		if vproduction_activity_instance_record.status != 'application.common.status.in.progress' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin			
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.finalize.activity.activity.not.in.required.status.to.perform.finalization.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.in.progress'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if vproduction_activity_instance_record.delivers_partial_result = 'S' then
			vnext_status := 'application.common.status.partial.result.delivered';
		else
			vnext_status := 'application.common.status.final.product.delivered';
		end if;

		update pms_production_activity_instance
		set activity_finish_work_date = now(),
		previous_status = vproduction_activity_instance_record.status,
		status = vproduction_activity_instance_record.next_status,
		next_status = vnext_status,
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