/*UC-REG-COS-UM*/

/*https://www.postgresql.org/docs/9.1/static/datatype-numeric.html
The scale of a numeric is the count of decimal digits in the fractional part, to the right of the decimal point.
The precision of a numeric is the total count of significant digits in the whole number, 
that is, the number of digits to both sides of the decimal point. 
So the number 23.5141 has a precision of 6 and a scale of 4. 
Integers can be considered to have a scale of zero.
*/


create table pms_machine_use_cost(
id	   		 		   bigint,
id_machine			bigint NOT NULL,
id_tariff		 		   bigint NOT NULL,
tariff_amount	   		 		   numeric(11,2) NOT NULL,
registration_date		   timestamp NOT NULL default now(),
validity_end_date		   timestamp,
creation_user	 		   varchar(50) ,
creation_date			   timestamp NOT NULL default now(),
last_modif_user 		   varchar(50),
last_modif_date		   timestamp,
is_active 			varchar(1) default 'S');


--alter table pms_machine_use_cost add id_machine			bigint NOT NULL;
ALTER TABLE pms_machine_use_cost ADD CONSTRAINT pms_machine_use_cost_id_pk PRIMARY KEY (id);

--ALTER TABLE pms_machine_use_cost ADD is_active varchar(1) default 'S'
ALTER TABLE pms_machine_use_cost ADD CHECK (is_active in ('S'));

ALTER TABLE pms_machine_use_cost ADD CONSTRAINT
pms_machine_use_cost_uk_01 UNIQUE 
(id_machine,is_active);


ALTER TABLE pms_machine_use_cost 
ADD constraint pms_machine_use_cost_fk_01
FOREIGN KEY (id_tariff) REFERENCES pms_tariff (id);

ALTER TABLE pms_machine_use_cost 
ADD constraint pms_machine_use_cost_fk_02
FOREIGN KEY (id_machine) REFERENCES pms_machine (id);


create sequence pms_machine_use_cost_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_machine_use_cost.id;


--DROP FUNCTION p_i_pms_machine_use_cost(bigint, bigint, numeric, timestamp without time zone, character varying);
create or replace function p_i_pms_machine_use_cost
(
pid	   		 		   bigint,
pid_machine			bigint,
pid_tariff	   		 	bigint,
ptariff_amount	   		 		   numeric,
--pregistration_date			OUT timestamp, :::ver observaciones sobre uso de OUT al final del documento
pregistration_date			INOUT timestamp,
pcreation_user				varchar
)
returns timestamp as
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
	RAISE INFO 'executing p_i_pms_machine_use_cost';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	
		if (ptariff_amount is null or ptariff_amount<=0)
		then
			declare
				b1_error_message		varchar(200);
				b1_error_message_hint	varchar(200);
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.productionmanagement.dao.machinedao.machineusecostdto.tariffamount.required.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;	

	pregistration_date:=now();

	insert into pms_machine_use_cost
			(id,id_machine,id_tariff,tariff_amount,registration_date,creation_user,creation_date)
	values
			(pid,
			pid_machine,
			pid_tariff,
			ptariff_amount,
			pregistration_date,
			pcreation_user,
			now());
	
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



/*actualizar; cerrar vigencia*/
--DROP FUNCTION p_u_pms_machine_use_cost(bigint, timestamp without time zone, character varying);
create or replace function p_u_pms_machine_use_cost
(
pid	   		 		   bigint,
--pvalidity_end_date			OUT timestamp, :::ver observaciones sobre uso de OUT al final del documento
pvalidity_end_date			INOUT timestamp,
plast_modif_user				varchar
)
returns timestamp as
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
	RAISE INFO 'executing p_u_pms_machine_use_cost';
	RAISE INFO '--------------------------';

	/*BEGIN VALIDATIONS*/	

	pvalidity_end_date:=now();

	update pms_machine_use_cost
		set
			validity_end_date = pvalidity_end_date,
			last_modif_user = plast_modif_user,
			last_modif_date = now(),
			is_active = null
	where id = pid;
	
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



/*observaciones sobre OUT

2016-09-06 01:06:28,559 DEBUG [BaseJdbcLogger.java:145] ==>  Preparing: SELECT p_i_pms_machine_use_cost (?, ?, ?, ?, ?::timestamp, ?::varchar) 
2016-09-06 01:06:28,559 DEBUG [BaseJdbcLogger.java:145] ==> Parameters: 3(Long), 5(Long), 17(Long), 8800(BigDecimal), null, null


### Error querying database.  Cause: org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
### The error may exist in py/com/kyron/sgp/persistence/productionmanagement/dao/mapper/machine.xml
### The error may involve py.com.kyron.sgp.persistence.productionmanagement.dao.MachineDAO.insertMachineUseCostDTO-Inline
### The error occurred while setting parameters
### SQL: SELECT  p_i_pms_machine_use_cost               (?,               ?,               ?,               ?,               ?::timestamp,               ?::varchar)
### Cause: org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
; bad SQL grammar []; nested exception is org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
org.springframework.jdbc.BadSqlGrammarException: 
### Error querying database.  Cause: org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
### The error may exist in py/com/kyron/sgp/persistence/productionmanagement/dao/mapper/machine.xml
### The error may involve py.com.kyron.sgp.persistence.productionmanagement.dao.MachineDAO.insertMachineUseCostDTO-Inline
### The error occurred while setting parameters
### SQL: SELECT  p_i_pms_machine_use_cost               (?,               ?,               ?,               ?,               ?::timestamp,               ?::varchar)
### Cause: org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
; bad SQL grammar []; nested exception is org.postgresql.util.PSQLException: ERROR: function p_i_pms_machine_use_cost(bigint, bigint, bigint, numeric, timestamp without time zone, character varying) does not exist
  Hint: No function matches the given name and argument types. You might need to add explicit type casts.
  Position: 9
	at org.springframework.jdbc.support.SQLStateSQLExceptionTranslator.doTranslate(SQLStateSQLExceptionTranslator.java:99)


EN PG ADMIN SE OBSERVA LA FUNCION DE LA SGTE MANERA
===================================================
-- Function: p_u_pms_machine_use_cost(bigint, character varying)

-- DROP FUNCTION p_u_pms_machine_use_cost(bigint, character varying);

CREATE OR REPLACE FUNCTION p_u_pms_machine_use_cost(
    IN pid bigint,
    OUT pvalidity_end_date timestamp without time zone,
    IN plast_modif_user character varying)
  RETURNS timestamp without time zone AS
$BODY$


CONCLUSION: EL PARAMETRO TIPO OUT NO CUENTA COMO PARAMETRO DE LLAMADA
=====================================================================

*/
