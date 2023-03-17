/*insert procedures*/

create or replace function p_i_app_sec_program
(
pid	   		 		   bigint,
pprogram_key		 		   		varchar
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
	RAISE INFO 'executing p_i_app_sec_program';
	RAISE INFO '--------------------------';
	
	insert into app_sec_program
			(id,program_key)
	values
			(pid,
			lower(trim(pprogram_key)));

	/*BEGIN VALIDATIONS*/	
		declare
			vid					app_sec_program.id%TYPE;
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			select id into vid from
			app_sec_program where id = pid and program_key like '% %';

			if (vid is not null) then
				b1_error_message:='py.com.kyron.sgp.persistence.personmanagement.dao.applicationsecuritydao.applicationsecurityprogramdto.inner.space.character.error'||''||lower(trim(pprogram_key))||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
		end;
	

	
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
select p_i_app_sec_program(nextval('app_sec_program_id_sq'),'www ww');
--rollback;
