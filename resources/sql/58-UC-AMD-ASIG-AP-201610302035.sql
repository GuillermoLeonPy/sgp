/*58-UC-AMD-ASIG-AP-201610302035*/

create or replace function p_production_activity_person_assignment
(
pid_production_activity_instance		bigint,
pid_person						bigint,
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
	RAISE INFO 'executing p_production_activity_person_assignment';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vproduction_activity_instance_record	record;
		vnext_status						pms_production_activity_instance.next_status%type;
	begin
		select * into vproduction_activity_instance_record
		from pms_production_activity_instance where id_person = pid_person;
		if vproduction_activity_instance_record.id_person is not null then
			declare
				v_person_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into v_person_record from pms_person where id = pid_person;				
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.activity.person.assignment.functionary.already.assigned.to.activity.error'||''||v_person_record.personal_last_name||', '||v_person_record.personal_name||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		select * into vproduction_activity_instance_record
		from pms_production_activity_instance where id = pid_production_activity_instance;

		if vproduction_activity_instance_record.status != 'application.common.status.pending' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin			
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.production.process.activitydao.production.activity.person.assignment.activity.not.in.required.status.to.be.assigned.error'||''||'#-numeric-#'||vproduction_activity_instance_record.product_instance_unique_number||''||'#-key-#'||'application.common.status.pending'||''||'#-key-#'||vproduction_activity_instance_record.status||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;
		
		if vproduction_activity_instance_record.require_parcial_product_recall = 'S' then
			vnext_status := 'application.common.status.partial.result.recalled';
		else
			vnext_status := 'application.common.status.in.progress';
		end if;
		
		update pms_production_activity_instance
		set id_person = pid_person, assignment_date = now(),
		status = 'application.common.status.assigned',
		is_asignable = 'N',
		next_status = vnext_status, last_modif_user = plast_modif_user,
		last_modif_date = now()
		where id = pid_production_activity_instance;

		
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
