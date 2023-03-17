create table pms_person(
id	   		 		   bigint,
ruc		 		   varchar(50),
personal_name	 		   varchar(250),
personal_last_name	 		   varchar(250),
personal_address	 		   varchar(250),
personal_email_address	 		   varchar(50),
personal_telephone_number	 		   varchar(50),
personal_civil_id_document	 		   varchar(50),
commercial_name	 		   varchar(250),
is_client		 		   varchar(1) NOT NULL,
is_customer		 		   varchar(1) NOT NULL,
is_supplier		 		   varchar(1) NOT NULL,
creation_user	 		   varchar(50) NOT NULL,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp);

--ALTER TABLE pms_person DROP is_client;
ALTER TABLE pms_person ADD is_functionary varchar(1) NOT NULL;
--ALTER TABLE pms_person DROP creation_user;
ALTER TABLE pms_person ADD creation_user varchar(50);
--ALTER TABLE pms_person DROP personal_civil_id_document;
ALTER TABLE pms_person ADD personal_civil_id_document bigint;
ALTER TABLE pms_person ADD CONSTRAINT pms_person_id_pk PRIMARY KEY (id);
ALTER TABLE pms_person ADD CONSTRAINT pms_person_ruc_uk UNIQUE (ruc);
ALTER TABLE pms_person ADD CONSTRAINT pms_personal_civil_id_document_uk UNIQUE (personal_civil_id_document);

ALTER TABLE pms_person ADD CHECK (is_client in ('S', 'N'));
ALTER TABLE pms_person ADD CHECK (is_customer in ('S', 'N'));
ALTER TABLE pms_person ADD CHECK (is_supplier in ('S', 'N'));

create sequence pms_person_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_person.id;
--rollback;



create or replace function p_i_pms_person
(
pid	   		 		   bigint,
pruc		 		   		varchar,
ppersonal_name	 		   	varchar,
ppersonal_last_name	 		varchar,
ppersonal_address	 		   varchar,
ppersonal_email_address	 		   varchar,
ppersonal_telephone_number	 		   varchar,
ppersonal_civil_id_document	 		   bigint,
pcommercial_name	 		   varchar,
pis_client		 		   varchar,
pis_customer		 		   varchar,
pis_supplier		 		   varchar,
pcreation_user	 		   varchar
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
	RAISE INFO 'executing p_i_pms_person';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/
	
	if (pruc is null or length(trim(pruc))=0) and 
		(ppersonal_civil_id_document is null)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.personal.civil.id.document.cannot.be.null.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (ppersonal_civil_id_document is not null) and 
		((ppersonal_name is null or length(trim(ppersonal_name))=0)
		or
		(ppersonal_last_name is null or length(trim(ppersonal_last_name))=0))
	then
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.name.last.name.cannot.be.null.error'||''||trim(to_char(ppersonal_civil_id_document,'999999999'))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9988';
		end;
	end if;

	declare
		vpersonal_civil_id_document	pms_person.personal_civil_id_document%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select personal_civil_id_document into vpersonal_civil_id_document
		from pms_person
		where personal_civil_id_document = ppersonal_civil_id_document;

		if vpersonal_civil_id_document is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.civil.id.document.already.exists.error'||''||trim(to_char(ppersonal_civil_id_document,'999999999'))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9987';			
		end if;
	end;

	declare
		vruc					pms_person.ruc%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select ruc into vruc
		from pms_person
		where ruc = upper(trim(pruc));

		if vruc is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.already.exists.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9986';			
		end if;
	end;

	declare
		vpersonal_civil_id_document	pms_person.personal_civil_id_document%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select personal_civil_id_document into vpersonal_civil_id_document
		from pms_person
		where personal_civil_id_document = ppersonal_civil_id_document;

		if vpersonal_civil_id_document is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.civil.id.document.already.exists.error'||''||trim(to_char(ppersonal_civil_id_document,'999999999'))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9987';			
		end if;
	end;

	declare
		vruc					pms_person.ruc%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select ruc into vruc
		from pms_person
		where ruc = upper(trim(pruc));

		if vruc is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.already.exists.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9986';			
		end if;
	end;

	if (pruc is not null and length(trim(pruc))>0) and 
		(ppersonal_civil_id_document is null) and
		(pcommercial_name is null or length(trim(pcommercial_name))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.provided.commercial.name.required.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9985';
		end;
	end if;
	/*END OF VALIDATIONS*/

	insert into pms_person
			(id,ruc,personal_name,personal_last_name,personal_address,personal_email_address,
			personal_telephone_number,personal_civil_id_document,commercial_name,is_client,
			is_customer,is_supplier,creation_user,creation_date)
	values
			(pid,
			upper(trim(pruc)),
			upper(trim(ppersonal_name)),
			upper(trim(ppersonal_last_name)),
			upper(trim(ppersonal_address)),
			upper(trim(ppersonal_email_address)),
			upper(trim(ppersonal_telephone_number)),
			ppersonal_civil_id_document,
			upper(trim(pcommercial_name)),
			pis_client,
			pis_customer,
			pis_supplier,
			pcreation_user,
			now());
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9988' or SQLSTATE 'P9987' or SQLSTATE 'P9986' or SQLSTATE 'P9985' THEN
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

-- DROP FUNCTION p_i_pms_person_test(bigint, character varying, character varying, character varying, character varying, character varying, character varying, bigint, character varying, character varying, character varying, character varying, character varying);
--      function p_i_pms_person_test(bigint, character varying, character varying, character varying, character varying, character varying, bigint, character varying, character varying, character varying, character varying, character varying, character varying) does not exist

create or replace function p_i_pms_person_test
(
pid	   		 		   bigint,
pruc		 		   		varchar,
ppersonal_name	 		   	varchar,
ppersonal_last_name	 		varchar,
ppersonal_address	 		   varchar,
ppersonal_email_address	 		   varchar,
ppersonal_telephone_number	 		   varchar,
ppersonal_civil_id_document	 		   bigint,
pcommercial_name	 		   varchar,
pis_client		 		   varchar,
pis_customer		 		   varchar,
pis_supplier		 		   varchar,
pcreation_user	 		   varchar
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
	RAISE INFO 'executing p_i_pms_person';
	RAISE INFO '--------------------------';


	if (pruc is null or length(trim(pruc))=0) and 
		(ppersonal_civil_id_document is null)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.personal.civil.id.document.cannot.be.null.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;

	if (ppersonal_civil_id_document is not null) and 
		((ppersonal_name is null or length(trim(ppersonal_name))=0)
		or
		(ppersonal_last_name is null or length(trim(ppersonal_last_name))=0))
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.name.last.name.cannot.be.null.error'||''||trim(to_char(ppersonal_civil_id_document,'999999999'))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9988';
		end;
	end if;

	declare
		vpersonal_civil_id_document	pms_person.personal_civil_id_document%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select personal_civil_id_document into vpersonal_civil_id_document
		from pms_person
		where personal_civil_id_document = ppersonal_civil_id_document;

		if vpersonal_civil_id_document is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.civil.id.document.already.exists.error'||''||trim(to_char(ppersonal_civil_id_document,'999999999'))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9987';			
		end if;
	end;

	if (pruc is not null and length(trim(pruc))>0) and 
		(ppersonal_civil_id_document is null) and
		(pcommercial_name is null or length(trim(pcommercial_name))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.provided.commercial.name.required.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end;
	end if;
	declare
		vruc					pms_person.ruc%TYPE;
		b1_error_message		text;
		b1_error_message_hint	text;
	begin
		select ruc into vruc
		from pms_person
		where ruc = upper(trim(pruc));

		if vruc is not null then
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.already.exists.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9986';			
		end if;
	end;

	if (pruc is not null and length(trim(pruc))>0) and 
		(ppersonal_civil_id_document is null) and
		(pcommercial_name is null or length(trim(pcommercial_name))=0)
	then
		declare
			b1_error_message		varchar(200);
			b1_error_message_hint	varchar(200);
		begin
			b1_error_message:='py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.ruc.provided.commercial.name.required.error'||''||upper(trim(pruc))||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9985';
		end;
	end if;
	
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9988' or SQLSTATE 'P9987' or SQLSTATE 'P9986' or SQLSTATE 'P9985' THEN
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
select p_i_pms_person_test(null,null,null,null,null,null,null,null,null,null,null,null,null);
--rollback;


BEGIN TRANSACTION;
select p_i_pms_person_test(null,'    ',null,null,null,null,null,null,null,null,null,null,null);
--rollback;

BEGIN TRANSACTION;
select p_i_pms_person_test(null::BIGINT,null::varchar,null::varchar,null::varchar,null::varchar,null::varchar,null::varchar,123::BIGINT,null::varchar,null::varchar,null::varchar,null::varchar,null::varchar);
--rollback;

BEGIN TRANSACTION;
select p_i_pms_person_test(null,null,null,null,null,null,null,123,null,null,null,null,null);
--rollback;

select to_number('00000121073', '999999999999999');
select to_number('000001210.73', '9999999999999.99');
select to_number('1210.73', '9999.99');
select to_number('121r.73', '9999.99');
select to_char('173','99.999.999');
select to_char(173173173,'999999999');
select to_char(173173173::BIGINT,'999999999');


BEGIN TRANSACTION;
select p_i_pms_person_test(nextval('pms_person_id_sq'),null,'wwwww','aaaa',null,null,null,123,null,null,null,null,null);
--rollback;

BEGIN TRANSACTION;
select p_i_pms_person(nextval('pms_person_id_sq'),null,'wwwww','aaaa',null,null,null,123,null,'N','N','N',null);
--rollback;

select * from pms_person

BEGIN TRANSACTION;
select p_i_pms_person_test(nextval('pms_person_id_sq'),'xxxxx',null,null,null,null,null,null,null,'N','N','N',null);
--rollback;

BEGIN TRANSACTION;
select p_i_pms_person(nextval('pms_person_id_sq'),'xxxxx',null,null,null,null,null,null,'werfas','N','N','N',null);
--rollback;

