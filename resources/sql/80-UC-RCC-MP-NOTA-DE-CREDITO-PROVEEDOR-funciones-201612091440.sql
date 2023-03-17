/*80-UC-RCC-MP-NOTA-DE-CREDITO-PROVEEDOR-funciones-201612091440*/

create or replace function p_i_purchase_invoice_credit_note
(
pid	   		 		   					bigint,
pid_purchase_invoice	   					bigint,
pemission_date								timestamp,
pemission_reason_description					varchar,
pidentifier_number   						varchar,
pstamping_number							numeric,
pstamping_number_start_validity_date			timestamp,
pbranch_office_address						varchar,
pbranch_office_telephone_nbr					varchar,
pcreation_user	 		   					varchar
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
	RAISE INFO 'executing p_i_purchase_invoice_credit_note';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/	
	declare
		vpurchase_invoice_credit_note_record		record;
		vpurchase_invoice_record					record;
		vstamping_number_end_validity_date			pms_purchase_invoice_credit_note.stamping_number_end_validity_date%type;
		vstamping_number_start_validity_date		pms_purchase_invoice_credit_note.stamping_number_start_validity_date%type;
	begin
		select * into vpurchase_invoice_credit_note_record from pms_purchase_invoice_credit_note where identifier_number = upper(trim(pidentifier_number));

		if vpurchase_invoice_credit_note_record.id is not null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.credit.note.dto.purchase.invoice.credit.note.identifier.number.already.exists.error'||''||upper(trim(pidentifier_number))||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		vstamping_number_start_validity_date := date_trunc('day', pstamping_number_start_validity_date);
		vstamping_number_end_validity_date := (
							date_trunc('day',(date_trunc('month', (pstamping_number_start_validity_date + interval '1 year')) + 
							interval '1 month' - interval '1 day')::timestamp)
							+ interval '23:59:59');		

		/*VALIDATION TO PERFORM WITH DATES*/		
		--emission date can not be > now()
		--emission must be in between vstamping_number_start_validity_date and vstamping_number_end_validity_date
		
		select * into vpurchase_invoice_record from pms_purchase_invoice where id = pid_purchase_invoice;

		insert into pms_purchase_invoice_credit_note
			(id,
			id_person,
			id_currency,
			emission_date,
			identifier_number,
			stamping_number,
			stamping_number_start_validity_date,
			stamping_number_end_validity_date,
			total_amount,
			value_added_amount,
			total_tax_amount,
			exempt_amount,
			value_added_tax_10_amount,
			value_added_tax_5_amount,
			tax_10_amount,
			tax_5_amount,
			bussines_name,
			bussines_ci_ruc,
			creation_user,
			credit_note_balance)
		values
			(pid,
			vpurchase_invoice_record.id_person,
			vpurchase_invoice_record.id_currency,
			pemission_date,
			upper(trim(pidentifier_number)),
			pstamping_number,
			vstamping_number_start_validity_date,
			vstamping_number_end_validity_date,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			vpurchase_invoice_record.bussines_name,
			vpurchase_invoice_record.bussines_ci_ruc,
			pcreation_user,
			0);

			insert into pms_purchase_invoice_credit_note_modified_documents
			(id,
			id_purchase_invoice_credit_note,
			id_purchase_invoice)
			values
			(nextval('pms_purchase_invoice_credit_note_modified_documents_id_sq'),
			pid,
			vpurchase_invoice_record.id);
			
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




create or replace function p_i_purchase_invoice_credit_note_item
(
pid	   		 		   					bigint,
pid_purchase_invoice_credit_note				bigint,
pid_purchase_invoice_item					bigint,
pquantity									numeric,
pcreation_user	 		   					varchar
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
	RAISE INFO 'executing p_i_purchase_invoice_credit_note_item';
	RAISE INFO '--------------------------';

	declare
		vpurchase_invoice_item_record				record;
		vpurchase_invoice_credit_note_record		record;
		vsum_quantity_in_all_credit_notes_items		pms_purchase_invoice_credit_note_item.quantity%type;	
		v_item_value_added_tax_10_amount			pms_purchase_invoice_credit_note_item.value_added_tax_10_amount%type;

		v_credit_note_value_added_tax_10_amount		pms_purchase_invoice_credit_note.value_added_tax_10_amount%type;
		v_credit_note_value_added_amount			pms_purchase_invoice_credit_note.value_added_amount%type;
		v_credit_note_total_amount				pms_purchase_invoice_credit_note.total_amount%type;
		v_credit_note_tax_10_amount				pms_purchase_invoice_credit_note.tax_10_amount%type;
		v_credit_note_total_tax_amount			pms_purchase_invoice_credit_note.total_tax_amount%type;
		
	begin
		select * into vpurchase_invoice_item_record from pms_purchase_invoice_item where id = pid_purchase_invoice_item;
		/*validate quantity*/		
		if pquantity is null then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
				v_raw_material_id		pms_raw_material.raw_material_id%type;
				v_measurment_unit_id	pms_measurment_unit.measurment_unit_id%type;
			begin
				select raw_material_id into v_raw_material_id from pms_raw_material where id = vpurchase_invoice_item_record.id_raw_material;
				select measurment_unit_id into v_measurment_unit_id from pms_measurment_unit where id = vpurchase_invoice_item_record.id_measurment_unit;
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.credit.note.item.dto.quantity.required.error'||''||v_raw_material_id||''||v_measurment_unit_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		if pquantity > vpurchase_invoice_item_record.quantity then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
				v_raw_material_id		pms_raw_material.raw_material_id%type;
				v_measurment_unit_id	pms_measurment_unit.measurment_unit_id%type;
			begin
				select raw_material_id into v_raw_material_id from pms_raw_material where id = vpurchase_invoice_item_record.id_raw_material;
				select measurment_unit_id into v_measurment_unit_id from pms_measurment_unit where id = vpurchase_invoice_item_record.id_measurment_unit;
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.credit.note.item.dto.quantity.greater.than.purchase.invoice.item.quantity.error'||''||'#-numeric-#'||pquantity||''||v_raw_material_id||''||v_measurment_unit_id||''||'#-numeric-#'||vpurchase_invoice_item_record.quantity||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		/*determinate the maximu quantity already in credit notes items */
		select sum(quantity) into vsum_quantity_in_all_credit_notes_items
		from pms_purchase_invoice_credit_note_item where id_purchase_invoice_item = vpurchase_invoice_item_record.id;

		if pquantity > (vpurchase_invoice_item_record.quantity - vsum_quantity_in_all_credit_notes_items) then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
				v_raw_material_id		pms_raw_material.raw_material_id%type;
				v_measurment_unit_id	pms_measurment_unit.measurment_unit_id%type;
			begin
				select raw_material_id into v_raw_material_id from pms_raw_material where id = vpurchase_invoice_item_record.id_raw_material;
				select measurment_unit_id into v_measurment_unit_id from pms_measurment_unit where id = vpurchase_invoice_item_record.id_measurment_unit;
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.purchase.invoice.dao.purchase.invoice.credit.note.item.dto.quantity.greater.than.possible.referable.quantity.error'||''||'#-numeric-#'||pquantity||''||v_raw_material_id||''||v_measurment_unit_id||''||'#-numeric-#'||(vpurchase_invoice_item_record.quantity - vsum_quantity_in_all_credit_notes_items)||''||'#-numeric-#'||vsum_quantity_in_all_credit_notes_items||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		v_item_value_added_tax_10_amount := pquantity * vpurchase_invoice_item_record.unit_price_amount;

		insert into pms_purchase_invoice_credit_note_item
			(id,
			id_purchase_invoice_credit_note,
			id_purchase_invoice_item,
			id_raw_material,
			id_measurment_unit,
			quantity,
			unit_price_amount,
			exempt_amount,
			value_added_tax_10_amount,
			value_added_tax_5_amount,
			creation_user)
		values
			(pid,
			pid_purchase_invoice_credit_note,
			pid_purchase_invoice_item,
			vpurchase_invoice_item_record.id_raw_material,
			vpurchase_invoice_item_record.id_measurment_unit,
			pquantity,
			vpurchase_invoice_item_record.unit_price_amount,
			0,
			v_item_value_added_tax_10_amount,
			0,
			pcreation_user);
		
		select * into vpurchase_invoice_credit_note_record
		from pms_purchase_invoice_credit_note where id = pid_purchase_invoice_credit_note;

		v_credit_note_value_added_tax_10_amount := v_item_value_added_tax_10_amount + vpurchase_invoice_credit_note_record.value_added_tax_10_amount;
		v_credit_note_value_added_amount := v_item_value_added_tax_10_amount + vpurchase_invoice_credit_note_record.value_added_amount;
		v_credit_note_total_amount := v_item_value_added_tax_10_amount + vpurchase_invoice_credit_note_record.total_amount;

		v_credit_note_tax_10_amount := (v_item_value_added_tax_10_amount * 0.090909) + vpurchase_invoice_credit_note_record.tax_10_amount;
		v_credit_note_total_tax_amount := (v_item_value_added_tax_10_amount * 0.090909) + vpurchase_invoice_credit_note_record.total_tax_amount;

		update pms_purchase_invoice_credit_note
		set
			value_added_tax_10_amount = v_credit_note_value_added_tax_10_amount,
			value_added_amount = v_credit_note_value_added_amount,
			total_amount = v_credit_note_total_amount,
			tax_10_amount = v_credit_note_tax_10_amount,
			total_tax_amount = v_credit_note_total_tax_amount,
			credit_note_balance = v_credit_note_total_amount
		where
			id = vpurchase_invoice_credit_note_record.id;
	end;
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
