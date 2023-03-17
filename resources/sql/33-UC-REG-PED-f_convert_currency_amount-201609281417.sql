/*33-UC-REG-PED-f_convert_currency_amount-201609281417*/
create or replace function f_convert_currency_amount
(
pid_currency_origin	   			bigint,
pid_currency_destination			bigint,
pamount						numeric
)
returns numeric as
$BODY$
declare
	error_message          			text;
	error_message_hint				text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE				text;
	v_MESSAGE_TEXT					text;
	v_PG_EXCEPTION_DETAIL			text;
	v_destination_currency_amount		numeric;
	v_local_currency_amount			numeric;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing f_convert_currency_amount';
	RAISE INFO '--------------------------';

	if pid_currency_origin = pid_currency_destination then
		return pamount;
	end if;

	/* check local currency existence */
	/* check origin currency exchange rate existence */
	/* check destination currency exchange rate existence */
	declare
		vid_local_currency					bigint;
		b1_error_message		text;
		b1_error_message_hint	text;
		vrecord							record;
	begin
		/* convinient to establish an unike index to pms_currency.currency_local, then non local currency rows will be null */
		select id into vid_local_currency from pms_currency where currency_local = 'S';
		if vid_local_currency is null then
			b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.convert.currency.amount.no.local.currency.error'||'end.of.message';
			b1_error_message_hint:=b1_error_message;
			RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
		end if;

		if vid_local_currency != pid_currency_origin then
			select er.id, cur.id_code into vrecord 
			from pms_currency cur left outer join pms_currency_exchange_rate er
			on cur.id = er.id_currency and er.is_active = 'S' and er.validity_end_date is null
			where
				cur.id = pid_currency_origin;

			if vrecord.id is null then
				b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.convert.currency.amount.no.valid.currency.exchange.rate.error'||''||vrecord.id_code||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;			
		end if;

		if vid_local_currency != pid_currency_destination then
			select er.id, cur.id_code into vrecord 
			from pms_currency cur left outer join pms_currency_exchange_rate er
			on cur.id = er.id_currency and er.is_active = 'S' and er.validity_end_date is null
			where
				cur.id = pid_currency_destination;

			if vrecord.id is null then
				b1_error_message:='py.com.kyron.sgp.persistence.comercialmanagement.dao.convert.currency.amount.no.valid.currency.exchange.rate.error'||''||vrecord.id_code||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;			
		end if;
	end;


	declare
		v_origin_currency		pms_currency.description%type;
		v_destination_currency	pms_currency.description%type;
	begin
		select description into v_origin_currency from pms_currency where id = pid_currency_origin;
		select description into v_destination_currency from pms_currency where id = pid_currency_destination;

		RAISE INFO '--------------------------';
		RAISE INFO 'origin currency: %',v_origin_currency;
		RAISE INFO 'destination currency: %',v_destination_currency;
		RAISE INFO '--------------------------';
	end;


	declare
		v_id_local_currency				pms_currency.id%type;
		v_local_currency_description		pms_currency.description%type;
	begin
		select id,description into v_id_local_currency,v_local_currency_description from pms_currency where currency_local = 'S';

		RAISE INFO '--------------------------';
		RAISE INFO 'local currency: %',v_local_currency_description;
		RAISE INFO '--------------------------';
		
		if v_id_local_currency != pid_currency_origin then
			declare
				v_origin_currency_exchange_rate_amount	pms_currency_exchange_rate.amount%type;
			begin
				select amount into v_origin_currency_exchange_rate_amount
				from pms_currency_exchange_rate
				where id_currency = pid_currency_origin
				and validity_end_date is null
				and is_active = 'S';
				
				v_local_currency_amount := trunc((v_origin_currency_exchange_rate_amount * pamount),2);		
				RAISE INFO '--------------------------';
				RAISE INFO 'origin currency exchange rate amount: %',v_origin_currency_exchange_rate_amount;
				RAISE INFO 'amount to convert: %',pamount;
				RAISE INFO 'local currency amount result: %',v_local_currency_amount;
				RAISE INFO '--------------------------';
			end;
		else
			v_local_currency_amount := pamount;
		end if;

		if pid_currency_destination != v_id_local_currency then
			declare
				v_destination_currency_exchange_rate_amount	pms_currency_exchange_rate.amount%type;
			begin
				select amount into v_destination_currency_exchange_rate_amount
				from pms_currency_exchange_rate
				where id_currency = pid_currency_destination
				and validity_end_date is null
				and is_active = 'S';

				v_destination_currency_amount := trunc((v_local_currency_amount / v_destination_currency_exchange_rate_amount),2);

				RAISE INFO '--------------------------';
				RAISE INFO 'destination currency exchange rate amount: %',v_destination_currency_exchange_rate_amount;
				RAISE INFO 'local currency amount to convert: %',v_local_currency_amount;
				RAISE INFO 'destination currency amount result: %',v_destination_currency_amount;
				RAISE INFO '--------------------------';
				
				return v_destination_currency_amount;
			end;
		else
			return v_local_currency_amount;
		end if;				
	end;
	
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';

select f_convert_currency_amount(61,75,5550);
/*
testeados casos:
  Moneda		Moneda
  origen		destino
1)no local, no local
2)no local, local
3)local, 	no local
*/


/*



			--convert to order currency			
			porder_id_currency--peso argentino
			record.id_currency--dolares
			record.tariff_id--DOLARES/METRO
			record.tariff_amount--50
			vlooprecord.quantity--5
			vlooprecord.id_measurment_unit--metro
			---------------------------------------------------------------------------
							--5 metros * 50 DOLARES/METRO = 250 dolares
							--convertir a moneda local
							--250 dolares x 5550 gs/dolar = 1 387 500 Gs
							--convertir a moneda destino
							--1 387 500 gs / 387 gs/peso  = 3 585,27 peso argentino

*/
