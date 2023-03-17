/*39-TEST-f_test_error_message_201610071811*/

create table test_error_message
(
column_bigint			bigint,
column_numeric			numeric(11,2),
column_timestamp		timestamp
);

begin transaction;

insert into test_error_message values(12345678910::BIGINT,88777666.99::NUMERIC,now());

commit;
rollback;

create or replace function f_test_error_message()
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
	RAISE INFO 'executing f_test_error_message';
	RAISE INFO '--------------------------';

	declare
		b1_error_message		text;
		b1_error_message_hint	text;
		vrecord				record;
	begin
		select * into vrecord from test_error_message;
					
		b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.error'||''||vrecord.column_bigint||''||vrecord.column_numeric||''||to_char(vrecord.column_timestamp,'dd/mm/yyyy HH24:MI:SS')||'end.of.message';
		b1_error_message_hint:=b1_error_message;
		RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';		
	end;
			
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' THEN
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

select f_test_error_message();