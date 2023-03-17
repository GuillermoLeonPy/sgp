/* p_i_app_sec_rol*/

create or replace function p_i_app_sec_rol
(
pid	   		 		   bigint,
prole_name		 		   varchar,
prole_description 		   varchar,
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
	
	if (prole_name is null or length(trim(prole_name))=0) or 
		(prole_description is null or length(trim(prole_description))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personmanagement.dao.applicationsecuritydao.applicationsecurityroldto.cannot.be.null.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	
	insert into app_sec_rol
			(id,role_name,role_description,is_editable)
	values
			(
			pid,
			upper(trim(prole_name)),
			upper(trim(prole_description)),
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
select p_i_app_sec_rol(nextval('app_sec_rol_id_sq'),'x','s',null);
--rollback;
