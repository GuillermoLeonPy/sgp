/*app_sec_rol_program*/

create or replace function p_i_app_sec_rol_program
(
pid	   		 		   bigint,
pid_app_sec_rol	   		 	bigint,
pid_app_sec_program	   		bigint,
pis_editable				varchar
)
returns void as
$BODY$
declare
	error_message          varchar(200);
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_app_sec_rol';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
	/*BEGIN VALIDATIONS*/
	/*BEGIN VALIDATIONS*/	
		declare
			vprogram_key					app_sec_program.program_key%TYPE;
			vrole_name					app_sec_rol.role_name%TYPE;
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			select asp.program_key into vprogram_key--,
				 --asr.role_name into vrole_name
			from			
				app_sec_program asp,
				app_sec_rol asr,
				app_sec_rol_program asrp
			where 				
				asp.id = pid_app_sec_program
				and asr.id = pid_app_sec_rol
				and asp.id = asrp.id_app_sec_program
				and asr.id = asrp.id_app_sec_rol;

			if (vprogram_key is not null) then

					select --asp.program_key into vprogram_key,
						 asr.role_name into vrole_name
					from			
						app_sec_program asp,
						app_sec_rol asr,
						app_sec_rol_program asrp
					where 				
						asp.id = pid_app_sec_program
						and asr.id = pid_app_sec_rol
						and asp.id = asrp.id_app_sec_program
						and asr.id = asrp.id_app_sec_rol;

			
				/*check for the key.param substring in the message: means that the error param is a message key*/
				b1_error_message:='py.com.kyron.sgp.persistence.personmanagement.dao.applicationsecuritydao.applicationsecurityrolprogramdto.program.already.asociated.to.rol.key.param.error'||''||vprogram_key||''||vrole_name||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
		end;
	insert into app_sec_rol_program
			(id,id_app_sec_rol,id_app_sec_program,is_editable)
	values
			(
			pid,
			pid_app_sec_rol,
			pid_app_sec_program,
			upper(trim(pis_editable))
			);
	
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


BEGIN TRANSACTION;
insert into app_sec_program values (nextval('app_sec_program_id_sq'), 'xx');
insert into app_sec_rol values (nextval('app_sec_rol_id_sq'), 'yy','y','S');
--rollback;

BEGIN TRANSACTION;
select p_i_app_sec_rol_program
(nextval('app_sec_rol_program_id_sq'),currval('app_sec_rol_id_sq'),currval('app_sec_program_id_sq'),'S')
--rollback;


