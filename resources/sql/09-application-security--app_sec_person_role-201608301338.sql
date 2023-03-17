/* app_sec_person_role */

create or replace function p_i_app_sec_person_role
(
pid	   		 		   bigint,
pid_app_sec_rol	   		 	bigint,
pid_person	   		bigint,
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
	
		declare
			vapp_user_name					pms_person.app_user_name%TYPE;
			vapp_user_passwd					pms_person.app_user_passwd%TYPE;
			vrole_name					app_sec_rol.role_name%TYPE;
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			select p.app_user_name into vapp_user_name
			from			
				pms_person p
			where 				
				p.id = pid_person;

			select p.app_user_passwd into vapp_user_passwd
			from			
				pms_person p
			where 				
				p.id = pid_person;
				
			if (vapp_user_name is null or vapp_user_passwd is null) then

				b1_error_message:='py.com.kyron.sgp.persistence.personmanagement.dao.applicationsecuritydao.applicationsecuritypersonroldto.person.appusername.required.to.asociate.rol.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';				
		
			end if;				
		end;

	
		declare
			vapp_user_name					pms_person.app_user_name%TYPE;
			vrole_name					app_sec_rol.role_name%TYPE;
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			select p.app_user_name into vapp_user_name--,
				 --asr.role_name into vrole_name
			from			
				pms_person p,
				app_sec_rol asr,
				app_sec_person_role aspr
			where 				
				p.id = pid_person
				and asr.id = pid_app_sec_rol
				and p.id = aspr.id_person
				and asr.id = aspr.id_app_sec_rol;

			if (vapp_user_name is not null) then
					select --p.app_user_name into vapp_user_name,
						 asr.role_name into vrole_name
					from			
						pms_person p,
						app_sec_rol asr,
						app_sec_person_role aspr
					where 				
						p.id = pid_person
						and asr.id = pid_app_sec_rol
						and p.id = aspr.id_person
						and asr.id = aspr.id_app_sec_rol;

							
				b1_error_message:='py.com.kyron.sgp.persistence.personmanagement.dao.applicationsecuritydao.applicationsecuritypersonroldto.already.asociated.error'||''||vapp_user_name||''||vrole_name||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';

			end if;
		end;

	insert into app_sec_person_role
			(id,id_app_sec_rol,id_person,is_editable)
	values
			(
			pid,
			pid_app_sec_rol,
			pid_person,
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
--insert into app_sec_program values (nextval('app_sec_program_id_sq'), 'xx');
insert into app_sec_rol values (nextval('app_sec_rol_id_sq'), 'yy','y','S');
--rollback;

select * from app_sec_rol

select * from pms_person
BEGIN TRANSACTION;
update pms_person set app_user_name = 'rcodas' where id = 16;
commit


BEGIN TRANSACTION;
select p_i_app_sec_person_role(nextval('app_sec_person_role_id_sq'),4,16,'S')
--rollback;

