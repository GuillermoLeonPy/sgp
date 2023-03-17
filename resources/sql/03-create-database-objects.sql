select current_schema()--schema_sgp
select now()

create table pms_currency(
id	   		 		   bigint,
id_code		 		   varchar(50) NOT NULL,
description	 		   varchar(100) NOT NULL,
local		 		   varchar(1) NOT NULL default 'N',
creation_user	 		   varchar(50) NOT NULL,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp)

ALTER TABLE pms_currency ADD CONSTRAINT pms_currency_id_pk PRIMARY KEY (id);
ALTER TABLE pms_currency ADD CONSTRAINT pms_currency_id_code_uk UNIQUE (id_code);
--ALTER TABLE pms_currency DROP CONSTRAINT pms_currency_id_code_uk
--ALTER TABLE pms_currency DROP creation_user;
--ALTER TABLE pms_currency ADD creation_user	varchar(50) NOT NULL;
--ALTER TABLE pms_currency ADD creation_user	varchar(50);
--ALTER TABLE pms_currency DROP local;
ALTER TABLE pms_currency ADD currency_local varchar(1) NOT NULL;

ALTER TABLE pms_currency ADD CHECK (local in ('S', 'N'));

create sequence pms_currency_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_currency.id;

create or replace function p_i_pms_currency
(pid	   		 		   bigint,
pid_code		 		   varchar,
pdescription	 		   varchar,
plocal		 		   varchar,
pcreation_user	 		   varchar)
returns void as
$BODY$
declare
	error_message          varchar(200);
	v_actual_local_currency_id     pms_currency.id%TYPE;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_currency';
	RAISE INFO '--------------------------';
	pid:=nextval('pms_currency_id_sq');

	if plocal = 'S' then
		begin
			select id INTO v_actual_local_currency_id from pms_currency where currency_local ='S';			
		end;
	end if;
	
	insert into pms_currency 
	(id,id_code,description,currency_local,creation_user)
	values
	(pid,upper(trim(pid_code)),upper(trim(pdescription)),plocal,pcreation_user);

	update pms_currency set currency_local ='N' where id = v_actual_local_currency_id;
	
	EXCEPTION
		WHEN unique_violation THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.currencydao.insert.id.code.unike.violated.error'||','||upper(trim(pid_code))||',end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||',end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create or replace function p_u_pms_currency
(pid	   		 		   bigint,
pid_code		 		   varchar,
pdescription	 		   varchar,
plocal		 		   varchar,
pupdater_user	 		   varchar)
returns void as
$BODY$
declare
	error_message          varchar(200);
	v_actual_local_currency_id     pms_currency.id%TYPE;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_u_pms_currency';
	RAISE INFO '--------------------------';
	--pid:=nextval('pms_currency_id_sq');

	if plocal = 'S' then
		begin
			select id INTO v_actual_local_currency_id from pms_currency where currency_local ='S';			
		end;
	end if;

	update pms_currency
	set 
		id_code = upper(trim(pid_code)),
		description = upper(trim(pdescription)),
		currency_local = plocal,
		last_modif_user = pupdater_user,
		last_modif_date = now()
	where
		id = pid;
		
	if v_actual_local_currency_id is not null then
		begin
			update pms_currency set currency_local ='N' where id = v_actual_local_currency_id;
		end;
	end if;
	
	EXCEPTION
		WHEN unique_violation THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.currencydao.insert.id.code.unike.violated.error'||','||upper(trim(pid_code))||',end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||',end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

--test procedure
--select nextval('pms_currency_id_sq')
BEGIN TRANSACTION;
select p_i_pms_currency(null,'aaac','bbbd','N','xx');
--rollback;
--commit;
select * from pms_currency

BEGIN TRANSACTION;
delete from pms_currency
--commit;


/*TEST PROCEDURE JUST TO RAISE AN EXCEPTION*/
create or replace function p_i_pms_currency_exception
(pid	   		 		   bigint,
pid_code		 		   varchar,
pdescription	 		   varchar,
plocal		 		   varchar,
pcreation_user	 		   varchar)
returns void as
$BODY$
declare
	error_message          varchar(200);
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_i_pms_currency';
	RAISE INFO '--------------------------';
	error_message:='gestion.comercial.service.impl.registrar.moneda.pms.currency.id.code.unike.violated'||','||upper(trim(pid_code));
	RAISE EXCEPTION '%',error_message USING HINT = error_message, ERRCODE ='P9998';


end;
$BODY$
LANGUAGE 'plpgsql';

BEGIN TRANSACTION;
select p_i_pms_currency(null,'aaab','bbbd','N',null);
--rollback;


BEGIN TRANSACTION;
select p_u_pms_currency(61,'PYG','GUARANI / PARAGUAY','S',null);
--rollback;
BEGIN TRANSACTION;
update pms_currency
	set 
		id_code = 'PYG',
		description = 'GUARANI / PARAGUAY',
		currency_local = 'S',
		last_modif_user = null,
		last_modif_date = now()
	where
		id = 61;