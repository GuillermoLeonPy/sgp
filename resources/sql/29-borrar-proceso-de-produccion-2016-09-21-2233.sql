create or replace function p_d_pms_production_process
(
pid	   		 		   bigint
)
returns timestamp as
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
	RAISE INFO 'executing p_d_pms_production_process';
	RAISE INFO '--------------------------';

	declare
		--vrecord				record;
		b1_error_message		text;
		b1_error_message_hint	text;
		vactivities_cursor cursor for
		select * from pms_production_process_activity where id_production_process = pid;
	begin
		for vrecord in vactivities_cursor loop
			delete from pms_raw_material_requirement
			where id_production_process_activity = vrecord.id;

			delete from pms_machine_requirement 
			where id_production_process_activity = vrecord.id;

			delete from pms_man_power_requirement
			where id_production_process_activity = vrecord.id;
			
		end loop;

		delete from pms_production_process_activity where id_production_process = pid;
	end;

	delete from pms_production_process where id = pid;

			
	EXCEPTION
		WHEN SQLSTATE 'P9989' THEN
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

select * from pms_production_process
