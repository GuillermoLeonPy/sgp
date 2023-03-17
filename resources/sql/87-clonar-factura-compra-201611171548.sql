/*87-clonar-factura-compra-201611171548*/

001-001-0000006
select * from pms_purchase_invoice where identifier_number = '001-001-0000016'
begin transaction;
select p_clone_purchase_invoice(28,'001-001-0000029');
commit;
rollback;

select identifier_number from pms_purchase_invoice where id = 27
desde:001-001-0000020
hasta: 001-001-0000029

begin transaction;

select quantity, physical_quantity_in_stock from pms_purchase_invoice_item where id_purchase_invoice = (select id from pms_purchase_invoice where identifier_number = '001-001-0000027')
begin transaction;
update pms_purchase_invoice_item
set physical_quantity_in_stock = quantity
where id_purchase_invoice = (select id from pms_purchase_invoice where identifier_number = '001-001-0000024')
rollback;
commit;
create or replace function p_clone_purchase_invoice
(
pid_purchase_invoice					bigint,
pnew_purchase_invoice_identifier_number		varchar
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
	RAISE INFO 'executing p_clone_purchase_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vpurchase_invoice_record		record;
	begin
		select * into vpurchase_invoice_record from pms_purchase_invoice
		where id = pid_purchase_invoice;
		declare
			vpurchase_invoice_item_cursor cursor for
			select * from pms_purchase_invoice_item where id_purchase_invoice = vpurchase_invoice_record.id;
		begin
			insert into pms_purchase_invoice
			(id,
			id_person,
			id_currency,
			payment_condition,
			status,
			registration_date,
			emission_date,
			cancellation_date,
			annulment_date,
			annulment_reason_description,
			identifier_number,
			stamping_number,
			stamping_number_start_validity_date,
			stamping_number_end_validity_date,
			branch_office_address,
			branch_office_telephone_nbr,
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
			credit_purchase_fee_quantity,
			credit_purchase_fee_periodicity_days_quantity,
			creation_user,
			creation_date,
			last_modif_user,
			last_modif_date,
			credit_purchase_first_payment_fee_date)
			values
			(nextval('pms_purchase_invoice_id_sq'),
			vpurchase_invoice_record.id_person,
			vpurchase_invoice_record.id_currency,
			vpurchase_invoice_record.payment_condition,
			--vpurchase_invoice_record.status,
			'application.common.status.pending',
			vpurchase_invoice_record.registration_date,
			vpurchase_invoice_record.emission_date,
			vpurchase_invoice_record.cancellation_date,
			vpurchase_invoice_record.annulment_date,
			vpurchase_invoice_record.annulment_reason_description,
			pnew_purchase_invoice_identifier_number,
			vpurchase_invoice_record.stamping_number,
			vpurchase_invoice_record.stamping_number_start_validity_date,
			vpurchase_invoice_record.stamping_number_end_validity_date,
			vpurchase_invoice_record.branch_office_address,
			vpurchase_invoice_record.branch_office_telephone_nbr,
			vpurchase_invoice_record.total_amount,
			vpurchase_invoice_record.value_added_amount,
			vpurchase_invoice_record.total_tax_amount,
			vpurchase_invoice_record.exempt_amount,
			vpurchase_invoice_record.value_added_tax_10_amount,
			vpurchase_invoice_record.value_added_tax_5_amount,
			vpurchase_invoice_record.tax_10_amount,
			vpurchase_invoice_record.tax_5_amount,
			vpurchase_invoice_record.bussines_name,
			vpurchase_invoice_record.bussines_ci_ruc,
			vpurchase_invoice_record.credit_purchase_fee_quantity,
			vpurchase_invoice_record.credit_purchase_fee_periodicity_days_quantity,
			vpurchase_invoice_record.creation_user,
			now(),
			vpurchase_invoice_record.last_modif_user,
			null,
			vpurchase_invoice_record.credit_purchase_first_payment_fee_date);

			for vrecord in vpurchase_invoice_item_cursor loop
				insert into pms_purchase_invoice_item
				(id,
				id_purchase_invoice,
				id_raw_material,
				id_measurment_unit,
				quantity,
				unit_price_amount,
				exempt_amount,
				value_added_tax_10_amount,
				value_added_tax_5_amount,
				creation_user,
				creation_date,
				last_modif_user,
				last_modif_date,
				physical_quantity_in_stock)
				values
				(nextval('pms_purchase_invoice_item_id_sq'),
				currval('pms_purchase_invoice_id_sq'),
				vrecord.id_raw_material,
				vrecord.id_measurment_unit,
				vrecord.quantity,
				vrecord.unit_price_amount,
				vrecord.exempt_amount,
				vrecord.value_added_tax_10_amount,
				vrecord.value_added_tax_5_amount,
				vrecord.creation_user,
				now(),
				vrecord.last_modif_user,
				null,
				--vrecord.physical_quantity_in_stock
				vrecord.quantity);
			end loop;
		end;
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
