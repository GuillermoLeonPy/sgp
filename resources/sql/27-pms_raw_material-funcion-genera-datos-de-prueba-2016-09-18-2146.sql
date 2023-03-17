/*27-pms_raw_material-funcion-genera-datos-de-prueba-2016-09-18-2146*/
create or replace function p_pms_raw_material_generate_test_data()
returns void as
$BODY$
declare
	error_message          varchar(200);
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;

	vcursor CURSOR IS
	select * from pms_raw_material order by 1;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_pms_raw_material_generate_test_data';
	RAISE INFO '--------------------------';

	for vcursor_record in vcursor loop

		if position('COLOR' in vcursor_record.raw_material_id) = 0 
		and position('BLANCO' in vcursor_record.raw_material_id) = 0 
		and 
		(position('TIPOTELA' in vcursor_record.raw_material_id) != 0 
		or position('HILO-FIBRA' in vcursor_record.raw_material_id) != 0) then
			insert into pms_raw_material
					(id,raw_material_id,raw_material_description,creation_user,creation_date)
			values
					(nextval('pms_raw_material_id_sq'),
					upper(trim(vcursor_record.raw_material_id||', COLOR')),
					upper(trim(vcursor_record.raw_material_description||'. COLOR')),
					null,
					now());

			update pms_raw_material set 
				raw_material_id = upper(trim(vcursor_record.raw_material_id||', BLANCO')),
				raw_material_description = upper(trim(vcursor_record.raw_material_description||'. BLANCO'))
			where
				id = vcursor_record.id;
		end if;
	end loop;


			
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

select id, raw_material_id, position('TELA' in raw_material_id) from pms_raw_material

/*function use script*/

begin transaction;
select p_pms_raw_material_generate_test_data();
rollback;

select * from pms_raw_material order by 2;
commit;
