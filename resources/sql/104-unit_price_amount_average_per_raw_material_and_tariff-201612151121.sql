/*104-unit_price_amount_average_per_raw_material_and_tariff-201612151121*/

create or replace function f_unit_price_amount_average_per_raw_material_and_tariff
(
pid_raw_material					bigint,
pid_tariff						bigint
)
returns numeric as
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
	RAISE INFO 'executing unit_price_amount_average_per_raw_material_and_tariff';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vtariff_record					record;
		v_acumulated_unit_price_amount	pms_purchase_invoice_item.unit_price_amount%type;
		v_acumulated_prices_count		bigint;
	begin
		select * into vtariff_record from pms_tariff where id = pid_tariff;
		v_acumulated_unit_price_amount := 0;

		select count(pi.id) into v_acumulated_prices_count
			from pms_purchase_invoice pi, 
			pms_purchase_invoice_item pii
		where
			pi.id = pii.id_purchase_invoice
			and pi.status != 'application.common.status.pending'
			and pii.id_raw_material = pid_raw_material
			and pii.id_measurment_unit = vtariff_record.id_measurment_unit;

		if v_acumulated_prices_count = 0 then
			return 0::numeric;
		end if;

		
		declare
				v_sum_per_currency_of_unit_price_amount_cursor cursor for
				select 
					pi.id_currency purchase_invoice_id_currency,
					sum(pii.unit_price_amount) sum_p_invoice_item_unit_price_amount					
				from
					pms_purchase_invoice pi,
					pms_purchase_invoice_item pii
				where
					pi.id = pii.id_purchase_invoice
					and pi.status != 'application.common.status.pending'
					and pii.id_raw_material = pid_raw_material
					and pii.id_measurment_unit = vtariff_record.id_measurment_unit
				group by
					pi.id_currency;
		begin
				for v_sum_per_currency_of_unit_price_amount_record in 
				v_sum_per_currency_of_unit_price_amount_cursor loop
					
					v_acumulated_unit_price_amount := v_acumulated_unit_price_amount 
												+ f_convert_currency_amount
												(v_sum_per_currency_of_unit_price_amount_record.purchase_invoice_id_currency,
												vtariff_record.id_currency,
												v_sum_per_currency_of_unit_price_amount_record.sum_p_invoice_item_unit_price_amount);
				
				end loop;
			return trunc((v_acumulated_unit_price_amount / (v_acumulated_prices_count::numeric)),2);
		end;--begin
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




			select 
					rmc.id,
					rmc.id_raw_material,
					rmc.id_tariff,
					rmc.tariff_amount,
					f_unit_price_amount_average_per_raw_material_and_tariff(rmc.id_raw_material,rmc.id_tariff),
					rmc.registration_date,
					rmc.validity_end_date,
					rmc.creation_user,
					rmc.creation_date,
					rmc.last_modif_user,
					rmc.last_modif_date
			from
				pms_raw_material_cost rmc