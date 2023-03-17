/*90-REPORTE-f_dates_interval_purchase_invoice_expenditure-201611271415*/

select * from f_dates_interval_purchase_invoice_expenditure((now() - interval '45 day' )::timestamp, now()::timestamp,61);

create or replace function f_dates_interval_purchase_invoice_expenditure
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(operational_concept 			varchar,--message key
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
		('application.common.operational.monetary.concept.expenditure.operation.purchases'::varchar) operational_concept,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.total_amount)) total_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.value_added_tax_10_amount)) value_added_tax_10_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.value_added_tax_5_amount)) value_added_tax_5_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.value_added_amount)) value_added_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.exempt_amount)) exempt_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.tax_10_amount)) tax_10_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.tax_5_amount)) tax_5_amount,
		sum(f_convert_currency_amount(pi.id_currency,pid_currency,pi.total_tax_amount)) total_tax_amount
		from 
		f_dates_interval_purchase_invoice_expenditure_by_currency
		(vbegin_date::timestamp, vend_date::timestamp) pi;
		
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


select * from 
f_dates_interval_purchase_invoice_expenditure_by_currency
((now() - interval '45 day' )::timestamp, now()::timestamp);

create or replace function f_dates_interval_purchase_invoice_expenditure_by_currency
(pbegin_date			timestamp,
pend_date			timestamp)
returns TABLE
(id_currency		 			bigint,
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

begin

		RETURN QUERY	
		select pi.id_currency,
		sum(pi.total_amount) total_amount,
		sum(pi.value_added_tax_10_amount) value_added_tax_10_amount,
		sum(pi.value_added_tax_5_amount) value_added_tax_5_amount,
		sum(pi.value_added_amount) value_added_amount,
		sum(pi.exempt_amount) exempt_amount,
		sum(pi.tax_10_amount) tax_10_amount,
		sum(pi.tax_5_amount) tax_5_amount,
		sum(pi.total_tax_amount) total_tax_amount
		from pms_purchase_invoice pi
		where
		pi.emission_date between pbegin_date and pend_date
		and 
		(pi.status = 'application.common.status.canceled'
		or pi.status = 'application.common.status.partial.balance')
		group by pi.id_currency;
end;
$BODY$
LANGUAGE 'plpgsql';


		select 
		pi.id_currency,
		sum(pi.total_amount) total_amount,
		sum(pi.value_added_tax_10_amount) value_added_tax_10_amount,
		sum(pi.value_added_tax_5_amount) value_added_tax_5_amount,
		sum(pi.value_added_amount) value_added_amount,
		sum(pi.exempt_amount) exempt_amount,
		sum(pi.tax_10_amount) tax_10_amount,
		sum(pi.tax_5_amount) tax_5_amount,
		sum(pi.total_tax_amount) total_tax_amount
		from pms_purchase_invoice pi
		where
		--pi.emission_date between ((now() - interval '45 day' ) and (now())
		--and 
		pi.status = 'application.common.status.canceled' or pi.status = 'application.common.status.partial.balance'
		group by pi.id_currency;
