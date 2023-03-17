/*93-REPORTE-f_dates_interval_income_expediture-201611280948*/

select * from f_dates_interval_income_expediture
('2013-02-28 11:01:28'::timestamp, '2016-11-28 11:10:28'::timestamp,61)
order by order_column;

create or replace function f_dates_interval_income_expediture
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(order_column					bigint,
operational_concept 			varchar,--message key
total_amount					numeric,
value_added_tax_10_amount		numeric,
value_added_tax_5_amount			numeric,
value_added_amount				numeric,
exempt_amount					numeric,
tax_10_amount					numeric,
tax_5_amount					numeric,
total_tax_amount				numeric)
as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
		vbegin_date		timestamp;
		vend_date			timestamp;
begin
		if pbegin_date > pend_date then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='application.common.validation.exception.begin.date.after.end.date.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		vend_date := ((date_trunc('day', pend_date) + interval '23:59:59') ::timestamp);
		vbegin_date := (date_trunc('day', pbegin_date)::timestamp);

		RETURN QUERY
		select
			1::bigint,
			si.operational_concept,
			si.total_amount,
			si.value_added_tax_10_amount,
			si.value_added_tax_5_amount,
			si.value_added_amount,
			si.exempt_amount,
			si.tax_10_amount,
			si.tax_5_amount,
			si.total_tax_amount
		from f_dates_interval_sale_invoice_income(vbegin_date::timestamp, vend_date::timestamp,pid_currency) si
		UNION
		select 
			2::bigint,
			pi.operational_concept,
			pi.total_amount * -1,
			pi.value_added_tax_10_amount * -1,
			pi.value_added_tax_5_amount * -1,
			pi.value_added_amount * -1,
			pi.exempt_amount * -1,
			pi.tax_10_amount * -1,
			pi.tax_5_amount * -1,
			pi.total_tax_amount * -1
		from f_dates_interval_purchase_invoice_expenditure(vbegin_date::timestamp, vend_date::timestamp,pid_currency) pi
		UNION
		select
			3::bigint,
			me.operational_concept,
			me.total_amount * -1,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric
		from f_dates_interval_machine_use_expediture(vbegin_date::timestamp, vend_date::timestamp,pid_currency) me
		UNION
		select
			4::bigint,
			mpe.operational_concept,
			mpe.total_amount * -1,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric,
			0::numeric
		from f_dates_interval_man_power_expediture(vbegin_date::timestamp, vend_date::timestamp,pid_currency) mpe;
		
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