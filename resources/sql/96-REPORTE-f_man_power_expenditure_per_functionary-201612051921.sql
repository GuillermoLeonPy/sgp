/*96-REPORTE-f_man_power_expenditure_per_functionary-201612051921*/

--functionary ci number
--functinary last name, first name
--activities carried out count
--minutes quantity
--professional fee (in any specified currency)
--drop view v_activity_instance_work_history_per_functionary;
create or replace view v_activity_instance_work_history_per_functionary as
select
paih.id_person,
paih.id_production_activity,
ppa.minutes_quantity::numeric activity_expected_minutes_quantity,
(((EXTRACT(EPOCH FROM (paih.activity_finish_work_date - paih.activity_start_work_date)))/60::numeric)::numeric) activity_effective_minutes_quantity,
paih.activity_finish_work_date,
(mpreq.minutes_quantity::numeric) man_power_requeriment_minutes_quantity,
mpcost.tariff_amount,
tariff.id_currency
from
pms_production_activity_instance_history paih,
pms_production_process_activity ppa,
pms_order ord,
pms_order_budget_product obp,
pms_order_budget_production_process_activity obppa,
pms_order_budget_production_process_activity_manpower obppamp,
pms_manpower_cost mpcost,
pms_tariff tariff,
pms_man_power_requirement mpreq
where
paih.id_production_activity = ppa.id
and paih.id_order = ord.id
and ord.identifier_number = obp.order_identifier_number
and obp.id = obppa.id_order_budget_product
and obppa.id_production_process_activity = paih.id_production_activity
and obppa.id = obppamp.id_order_budget_production_process_activity
and obppamp.id_manpower_cost = mpcost.id
and mpcost.id_tariff = tariff.id
and obppamp.id_manpower_requirement = mpreq.id
order by
paih.activity_finish_work_date

select * from f_dates_interval_man_power_expenditure_per_functionary
('2016-11-01 00:00:00'::timestamp, '2016-11-30 23:59:59'::timestamp,61);

create or replace function f_dates_interval_man_power_expenditure_per_functionary
(pbegin_date		timestamp,
pend_date			timestamp,
pid_currency		bigint)
returns TABLE
(id_person						bigint,
id_production_activity 				bigint,
activity_expected_minutes_quantity		numeric,
activity_effective_minutes_quantity	numeric,
activity_finish_work_date			timestamp,
man_power_requeriment_minutes_quantity	numeric,
man_power_cost						numeric)
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
			vaiwhpf.id_person,
			vaiwhpf.id_production_activity,
			vaiwhpf.activity_expected_minutes_quantity,
			vaiwhpf.activity_effective_minutes_quantity,
			vaiwhpf.activity_finish_work_date::timestamp,
			vaiwhpf.man_power_requeriment_minutes_quantity,
			f_convert_currency_amount(vaiwhpf.id_currency,pid_currency,(vaiwhpf.tariff_amount * vaiwhpf.man_power_requeriment_minutes_quantity)) man_power_cost
		from 
			v_activity_instance_work_history_per_functionary vaiwhpf
		where
			vaiwhpf.activity_finish_work_date between vbegin_date and vend_date;

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
person.personal_civil_id_document,
person.personal_last_name||', '||person.personal_name functionary_name,
xxx.activities_carried_out_count,
xxx.sum_activity_expected_minutes_quantity,
xxx.sum_activity_effective_minutes_quantity,
case when xxx.sum_activity_effective_minutes_quantity < xxx.sum_activity_expected_minutes_quantity then
(xxx.sum_activity_expected_minutes_quantity - xxx.sum_activity_effective_minutes_quantity)
else 0::numeric end saved_minutes,
xxx.sum_man_power_requeriment_minutes_quantity,
xxx.sum_man_power_cost
from pms_person person,
	(select 
		fdimpepf.id_person,
		count(fdimpepf.id_production_activity) activities_carried_out_count,
		sum(fdimpepf.activity_expected_minutes_quantity) sum_activity_expected_minutes_quantity,
		sum(fdimpepf.activity_effective_minutes_quantity) sum_activity_effective_minutes_quantity,
		sum(fdimpepf.man_power_requeriment_minutes_quantity) sum_man_power_requeriment_minutes_quantity,
		sum(fdimpepf.man_power_cost) sum_man_power_cost
	from 
		f_dates_interval_man_power_expenditure_per_functionary
		('2016-11-01 00:00:00'::timestamp, '2016-11-30 23:59:59'::timestamp,61) fdimpepf
	group by
	fdimpepf.id_person) xxx
where person.id = xxx.id_person;