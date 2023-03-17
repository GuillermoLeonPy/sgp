/*94-REPORTE-f_dates_interval_product_sale_cost_comparison_201612031730*/
drop view v_product_cost_and_sale_comparison_by_dates;
create or replace view v_product_cost_and_sale_comparison_by_dates as
select
sii.id,-- id_sale_invoice_item
si.id_currency,
prod.product_id product,
prod.increase_over_cost_for_sale_price product_increase_over_cost_for_sale_price,
ord_item.product_unit_manufacture_cost order_item_product_unit_manufacture_cost,
ord_item.unit_price_amount order_item_unit_price_amount,
sii.unit_price_amount sale_invoice_item_unit_price_amount,
sii.quantity sale_invoice_item_quantity,
(sii.unit_price_amount * sii.quantity) sale_price_acumulated,
(ord_item.product_unit_manufacture_cost * sii.quantity) production_cost_acumulated,
((sii.unit_price_amount * sii.quantity) - (ord_item.product_unit_manufacture_cost * sii.quantity)) profit,
si.emission_date sale_date
from
pms_product prod,
pms_order_item ord_item,
pms_sale_invoice_item sii,
pms_sale_invoice si
where
prod.id = sii.id_product
and sii.id_order_item = ord_item.id
and si.id = sii.id_sale_invoice
and sii.status != 'application.common.status.discarded'
and (si.status = 'application.common.status.canceled' or si.status = 'application.common.status.partial.balance')
order by si.emission_date;


select * from v_product_cost_and_sale_comparison_by_dates

create or replace function f_correct_sale_invoice_item()
returns void
as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	declare
		vcursor cursor for
		select * from pms_order_item;
	begin
		for reg in vcursor loop
			update pms_sale_invoice_item
			set unit_price_amount = reg.unit_price_amount,
			value_added_tax_10_unit_price_amount = reg.unit_price_amount * quantity
			where id_order_item = reg.id;
		end loop;
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


create or replace function f_dates_interval_product_sale_cost_comparison
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(id									bigint,
product								varchar,
product_increase_over_cost_for_sale_price	bigint,
order_item_product_unit_manufacture_cost	numeric,
order_item_unit_price_amount				numeric,
sale_invoice_item_unit_price_amount		numeric,
sale_invoice_item_quantity				bigint,
sale_price_acumulated					numeric,
production_cost_acumulated				numeric,
profit								numeric,
sale_date								timestamp
)
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
			pcsc.id,
			pcsc.product,
			pcsc.product_increase_over_cost_for_sale_price,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.order_item_product_unit_manufacture_cost) order_item_product_unit_manufacture_cost,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.order_item_unit_price_amount) order_item_unit_price_amount,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.sale_invoice_item_unit_price_amount) sale_invoice_item_unit_price_amount,
			pcsc.sale_invoice_item_quantity,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.sale_price_acumulated) sale_price_acumulated,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.production_cost_acumulated) production_cost_acumulated,
			f_convert_currency_amount(pcsc.id_currency,pid_currency,pcsc.profit) profit,
			pcsc.sale_date
		from v_product_cost_and_sale_comparison_by_dates pcsc
		where pcsc.sale_date between vbegin_date and vend_date;

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


select * from f_dates_interval_product_sale_cost_comparison
('2013-02-28 11:01:28'::timestamp, '2016-11-28 11:10:28'::timestamp,61);
