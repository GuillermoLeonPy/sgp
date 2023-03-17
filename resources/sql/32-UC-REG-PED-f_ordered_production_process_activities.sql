/* 32-UC-REG-PED-f_ordered_production_process_activities */
select * from f_ordered_production_process_activities(7);
create or replace function f_ordered_production_process_activities
(pid_production_process	bigint)
returns TABLE 
(id bigint, 
activity_id VARCHAR, 
activity_description VARCHAR, 
id_production_process bigint, 
minutes_quantity bigint, 
registration_date timestamp,
validity_end_date timestamp, 
id_previous_activity bigint, 
creation_user VARCHAR,
creation_date timestamp, 
last_modif_user VARCHAR, 
last_modif_date timestamp) 
as
$BODY$
declare

begin
			RETURN QUERY
			select 
				ppa.id,
				ppa.activity_id,
				ppa.activity_description,
				ppa.id_production_process,
				ppa.minutes_quantity,
				ppa.registration_date,
				ppa.validity_end_date,
				ppa.id_previous_activity,
				ppa.creation_user,
				ppa.creation_date,
				ppa.last_modif_user,
				ppa.last_modif_date
			from
				pms_production_process_activity ppa
			where 
				ppa.validity_end_date is null and
				ppa.id = (select ppax.id from pms_production_process_activity ppax
							where 
							ppax.validity_end_date is null and
							ppax.id_production_process = pid_production_process 
							and ppax.id_previous_activity is null)
			
			UNION ALL
			
			select 
				pppa.id,
				pppa.activity_id,
				pppa.activity_description,
				pppa.id_production_process,
				pppa.minutes_quantity,
				pppa.registration_date,
				pppa.validity_end_date,
				pppa.id_previous_activity,
				pppa.creation_user,
				pppa.creation_date,
				pppa.last_modif_user,
				pppa.last_modif_date
			from
			(
					with recursive y as
						(select x
						  from pms_production_process_activity x
						  where 
						  x.validity_end_date is null and
						  x.id_previous_activity = 
						  					(select ppay.id from pms_production_process_activity ppay
						  					where 
						  						ppay.validity_end_date is null and
						  						ppay.id_production_process = pid_production_process
						  						and ppay.id_previous_activity is null)
						UNION ALL
						select xi
						  from y
						  join pms_production_process_activity xi
						  on xi.id_previous_activity = (y.x).id
						)
					select 
					(y.x).id,
					(y.x).activity_id,
					(y.x).activity_description,
					(y.x).id_production_process,
					(y.x).minutes_quantity,
					(y.x).registration_date,
					(y.x).validity_end_date,
					(y.x).id_previous_activity,
					(y.x).creation_user,
					(y.x).creation_date,
					(y.x).last_modif_user,
					(y.x).last_modif_date
					from y
					--ORDER BY (y.x).id_previous_activity
			) pppa ;
end;
$BODY$
LANGUAGE 'plpgsql';
