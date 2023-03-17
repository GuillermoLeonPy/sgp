/*92-REPORTE-f_dates_interval_man_power_expediture-201611272142*/

select * from f_dates_interval_man_power_expediture
('2013-02-28 11:01:28'::timestamp, '2016-11-28 11:10:28'::timestamp,61);

create or replace function f_dates_interval_man_power_expediture
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(operational_concept 			varchar,--message key
total_amount					numeric)
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
		('application.common.operational.monetary.concept.expenditure.operation.manpower.operation'::varchar) operational_concept,
		sum(f_convert_currency_amount(mpe.id_currency,pid_currency,mpe.amount)) total_amount
		from 
		f_dates_interval_man_power_expediture_by_currency
		(vbegin_date::timestamp, vend_date::timestamp) mpe;
		
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



select * from f_dates_interval_man_power_expediture_by_currency
('2013-02-28 11:01:28'::timestamp, '2016-11-28 11:10:28'::timestamp);

create or replace function f_dates_interval_man_power_expediture_by_currency
(pbegin_date			timestamp,
pend_date			timestamp)
returns TABLE
(id_currency		 			bigint,
amount					numeric)
as
$BODY$
declare

begin

		RETURN QUERY			
		select 
		tariff.id_currency,
		(sum((manpowercost.tariff_amount * 
		(EXTRACT(EPOCH FROM (paih.activity_finish_work_date - paih.activity_start_work_date)) / 60)
		))::numeric)
		man_power_cost
		from
			pms_tariff tariff,
			pms_manpower_cost manpowercost,
			pms_order_budget_production_process_activity_manpower obppamp,
			pms_order_budget_production_process_activity obppa,
			pms_production_activity_instance_history paih
		where
			tariff.id = manpowercost.id_tariff
			and manpowercost.id = obppamp.id_manpower_cost
			and obppamp.id_order_budget_production_process_activity = obppa.id
			and obppa.id_production_process_activity = paih.id_production_activity
			and paih.activity_finish_work_date between pbegin_date and pend_date
		group by tariff.id_currency;

end;
$BODY$
LANGUAGE 'plpgsql';

486,887.625 / 18500


		select 
		tariff.id_currency,
		(sum(( 
		(EXTRACT(EPOCH FROM (paih.activity_finish_work_date - paih.activity_start_work_date)) / 60)
		))::numeric)
		man_power_cost
		from
			pms_tariff tariff,
			pms_manpower_cost manpowercost,
			pms_order_budget_production_process_activity_manpower obppamp,
			pms_order_budget_production_process_activity obppa,
			pms_production_activity_instance_history paih
		where
			tariff.id = manpowercost.id_tariff
			and manpowercost.id = obppamp.id_manpower_cost
			and obppamp.id_order_budget_production_process_activity = obppa.id
			and obppa.id_production_process_activity = paih.id_production_activity
			--and paih.activity_finish_work_date between pbegin_date and pend_date
		group by tariff.id_currency;