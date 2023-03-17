/*66-test-trigger-trying-to-read-mutating-table-201611011216*/

create table table_test_trigger(
id			bigint not null,
quantity		numeric(11,2) NOT NULL);

ALTER TABLE table_test_trigger 
ADD CONSTRAINT table_test_trigger_id_pk PRIMARY KEY (id);

insert into table_test_trigger values (1,25);
insert into table_test_trigger values (2,50);
insert into table_test_trigger values (3,100);
insert into table_test_trigger values (4,150);

create or replace function f_trigger_au_table_test_trigger()
returns trigger as
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
	RAISE INFO 'executing f_trigger_au_table_test_trigger';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vid_table_test_trigger		bigint;
		vquantity					numeric(11,2);
	begin
		vid_table_test_trigger := new.id;

		RAISE INFO '--------------------------';
		RAISE INFO 'updating row with id: %',vid_table_test_trigger;
		RAISE INFO '--------------------------';


		select sum(quantity) into vquantity
		from table_test_trigger
		where id != vid_table_test_trigger;

		vquantity := vquantity + new.quantity;

		RAISE INFO '--------------------------';
		RAISE INFO 'the new acumulated quantity for the table: %',vquantity;
		RAISE INFO '--------------------------';

		RETURN NEW;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

create trigger trigger_au_table_test_trigger
AFTER UPDATE ON table_test_trigger FOR EACH ROW 
EXECUTE PROCEDURE f_trigger_au_table_test_trigger();


update table_test_trigger set quantity = 0 where id = 4;

/* **************************** RESULTADO ************************************ */
INFO:  --------------------------
INFO:  executing f_trigger_au_table_test_trigger
INFO:  --------------------------
INFO:  --------------------------
INFO:  updating row with id: 4
INFO:  --------------------------
INFO:  --------------------------
INFO:  the new acumulated quantity for the table: 175.00
INFO:  --------------------------


Query returned successfully: one row affected, 14 msec execution time.

/* **************************** CONCLUSION ************************************ */
/* NO HAY PROBLEMA DE TABLA MUTANTE ******************************************* */
/* HACIENDO SELECCION SOBRE LA TABLA QUE ESTA MUTANDO OMITIENDO LA FILA QUE ESTA*/
/* SIENDO ACTUALIZADA												*/
/* **************************************************************************** */

DROP FUNCTION f_trigger_au_table_test_trigger() CASCADE;


/* PRUEBA INCLUYENDO EN LA SELECCION LA FILA QUE SE ESTA ACTUALIZANDO */
create or replace function f_trigger_au_table_test_trigger()
returns trigger as
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
	RAISE INFO 'executing f_trigger_au_table_test_trigger';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vquantity					numeric(11,2);
	begin
		RAISE INFO '--------------------------';
		RAISE INFO 'NOT IDENTIFYING THE ROW BEING UPDATED: ';
		RAISE INFO '--------------------------';
		
		select sum(quantity) into vquantity
		from table_test_trigger;

		vquantity := vquantity + new.quantity;

		RAISE INFO '--------------------------';
		RAISE INFO 'the new acumulated quantity for the table: %',vquantity;
		RAISE INFO '--------------------------';

		RETURN NEW;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

create trigger trigger_au_table_test_trigger
AFTER UPDATE ON table_test_trigger FOR EACH ROW 
EXECUTE PROCEDURE f_trigger_au_table_test_trigger();

update table_test_trigger set quantity = 0 where id = 3;

/* **************************** RESULTADO ************************************ */

INFO:  --------------------------
INFO:  executing f_trigger_au_table_test_trigger
INFO:  --------------------------
INFO:  --------------------------
INFO:  NOT IDENTIFYING THE ROW BEING UPDATED:
INFO:  --------------------------
INFO:  --------------------------
INFO:  the new acumulated quantity for the table: 75.00
INFO:  --------------------------


Query returned successfully: one row affected, 14 msec execution time.


/* **************************** CONCLUSION ************************************ */
/* NO HAY PROBLEMA DE TABLA MUTANTE ******************************************* */
/* HACIENDO SELECCION SOBRE LA TABLA QUE ESTA MUTANDO AUN SIN OMITIR LA FILA QUE*/
/* ESTA SIENDO ACTUALIZADA											*/
/* **************************************************************************** */



create table table_test_trigger_detail(
id							bigint not null,
id_table_test_trigger			bigint not null,
quantity		numeric(11,2) NOT NULL);

ALTER TABLE table_test_trigger_detail 
ADD CONSTRAINT table_test_trigger_detail_id_pk PRIMARY KEY (id);

ALTER TABLE table_test_trigger_detail 
ADD constraint table_test_trigger_detail_fk_01
FOREIGN KEY (id_table_test_trigger) REFERENCES table_test_trigger (id);

insert into table_test_trigger_detail values (1,4,25);
insert into table_test_trigger_detail values (2,4,50);
insert into table_test_trigger_detail values (3,4,100);
insert into table_test_trigger_detail values (4,4,150);



create or replace function f_trigger_au_table_test_trigger_detail()
returns trigger as
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
	RAISE INFO 'executing f_trigger_au_table_test_trigger_detail';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vid_table_test_trigger			bigint;
		vquantity					numeric(11,2);
	begin

		vid_table_test_trigger := new.id_table_test_trigger;
		
		RAISE INFO '--------------------------';
		RAISE INFO 'IDENTIFYING THE FATHER ROW: %', vid_table_test_trigger;
		RAISE INFO '--------------------------';		
		
		select sum(tttd.quantity) into vquantity
		from table_test_trigger ttt, table_test_trigger_detail tttd
		where ttt.id = tttd.id_table_test_trigger
		and ttt.id = vid_table_test_trigger;		

		RAISE INFO '--------------------------';
		RAISE INFO 'the new acumulated quantity for the FATHER table ROW: %',vquantity;
		RAISE INFO '--------------------------';

		update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger;
		
		RAISE INFO '--------------------------';
		RAISE INFO 'THE FATHER ROW HAS BEEN UPDATED';
		RAISE INFO '--------------------------';

		RETURN NEW;
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

create trigger trigger_au_table_test_trigger_detail
AFTER UPDATE ON table_test_trigger_detail FOR EACH ROW 
EXECUTE PROCEDURE f_trigger_au_table_test_trigger_detail();


select * from table_test_trigger where id = 4;
select * from table_test_trigger_detail;

update table_test_trigger_detail set quantity = 0 where id = 3;

/* **************************** RESULTADO ************************************ */

INFO:  --------------------------
INFO:  executing f_trigger_au_table_test_trigger_detail
INFO:  --------------------------
INFO:  --------------------------
INFO:  IDENTIFYING THE FATHER ROW: 4
INFO:  --------------------------
INFO:  --------------------------
INFO:  the new acumulated quantity for the FATHER table ROW: 75.00
INFO:  --------------------------
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  executing f_trigger_au_table_test_trigger
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  NOT IDENTIFYING THE ROW BEING UPDATED: 
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  the new acumulated quantity for the table: 225.00
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
CONTEXT:  SQL statement "update table_test_trigger set quantity = vquantity
		where id = vid_table_test_trigger"
PL/pgSQL function f_trigger_au_table_test_trigger_detail() line 34 at SQL statement
INFO:  --------------------------
INFO:  THE FATHER ROW HAS BEEN UPDATED
INFO:  --------------------------


Query returned successfully: one row affected, 19 msec execution time.



/* **************************** CONCLUSION ************************************ */
/* NO HAY PROBLEMA DE TABLA MUTANTE ******************************************* */
/* HACIENDO SELECCION SOBRE LA TABLA QUE ESTA MUTANDO AUN SIN OMITIR LA FILA QUE*/
/* ESTA SIENDO ACTUALIZADA											*/
/* **************************************************************************** */