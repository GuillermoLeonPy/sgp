/*70-p_i_pms_order_item_raw_material_sufficiency_report_detail-201611030959*/

/*
ALTER TABLE pms_order_item_raw_material_sufficiency_report_detail ADD constraint 
pms_order_item_raw_material_sufficiency_report_detail_uk 
unique 
(id_order_item_raw_material_sufficiency_report,
id_raw_material,
id_measurment_unit);

ALTER TABLE pms_order_item_raw_material_sufficiency_report ADD constraint 
pms_order_item_raw_material_sufficiency_report_uk 
unique (id_order_raw_material_sufficiency_report,id_order_item);

*/



create or replace function p_i_pms_order_item_raw_material_sufficiency_report_detail
(pid_order						bigint,
pid_order_item						bigint,
pentered_into_producction_quantity		bigint,
ppending_to_instanciate_quantity		bigint,
pid_raw_material					bigint,
pid_measurment_unit					bigint,
prequired_quantity					numeric,
pavailable_quantity					numeric,
pcreation_user			   			varchar
)
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
	RAISE INFO 'executing p_i_pms_order_item_raw_material_sufficiency_report_detail';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vorder_raw_material_sufficiency_report_record 		record;
		vorder_item_raw_material_sufficiency_report_record	record;
	begin
		/*check if exists pms_order_raw_material_sufficiency_report record*/
		select * into vorder_raw_material_sufficiency_report_record
		from pms_order_raw_material_sufficiency_report
		where id_order = pid_order and is_valid = 'S';

		if vorder_raw_material_sufficiency_report_record.id is null then			
			insert into pms_order_raw_material_sufficiency_report 
			(id,id_order,creation_user)
			values
			(nextval('pms_order_raw_material_sufficiency_report_id_sq'), pid_order, pcreation_user);
		end if;--if vorder_raw_material_sufficiency_report_record.id is null then

		select * into vorder_raw_material_sufficiency_report_record
		from pms_order_raw_material_sufficiency_report
		where id_order = pid_order and is_valid = 'S';

		/*check if exists pms_order_item_raw_material_sufficiency_report record*/
		select * into vorder_item_raw_material_sufficiency_report_record
		from pms_order_item_raw_material_sufficiency_report
		where id_order_raw_material_sufficiency_report = vorder_raw_material_sufficiency_report_record.id
		and id_order_item = pid_order_item;

		if vorder_item_raw_material_sufficiency_report_record.id is null then
			insert into pms_order_item_raw_material_sufficiency_report 
			(id,
			id_order_raw_material_sufficiency_report,
			id_order_item,
			entered_into_producction_quantity,
			pending_to_instanciate_quantity
			)
			values
			(nextval('pms_order_item_raw_material_sufficiency_report_id_sq'),
			vorder_raw_material_sufficiency_report_record.id,
			pid_order_item,
			pentered_into_producction_quantity,
			ppending_to_instanciate_quantity);
		end if;--if vorder_item_raw_material_sufficiency_report_record.id is null then

		select * into vorder_item_raw_material_sufficiency_report_record
		from pms_order_item_raw_material_sufficiency_report
		where id_order_raw_material_sufficiency_report = vorder_raw_material_sufficiency_report_record.id
		and id_order_item = pid_order_item;


		insert into pms_order_item_raw_material_sufficiency_report_detail
			(id,
			id_order_item_raw_material_sufficiency_report,
			id_raw_material,
			id_measurment_unit,
			required_quantity,
			available_quantity)
		values
			(nextval('pms_order_item_raw_material_sufficiency_report_detail_id_sq'),
			vorder_item_raw_material_sufficiency_report_record.id,
			pid_raw_material,
			pid_measurment_unit,
			prequired_quantity,
			pavailable_quantity);
		
	end;--begin
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
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
