create or replace view v_raw_material_with_out_existence_record as
select raw_material_id from pms_raw_material
where id not in (select id_raw_material from pms_raw_material_existence)
order by 1 asc

create or replace view v_raw_material_with_existence_record as
select raw_material_id from pms_raw_material
where id in (select id_raw_material from pms_raw_material_existence)
order by 1 asc