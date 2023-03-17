

select * from v_users_role_programkeys

--drop view v_users_role_programkeys
create or replace view v_users_role_programkeys as
select p.personal_civil_id_document, /*p.ruc ,*/p.app_user_name, p.app_user_passwd, r.role_name, prog.program_key
from pms_person p, app_sec_rol r, app_sec_program prog, app_sec_rol_program asrp, app_sec_person_role aspr
where
p.id = aspr.id_person
and r.id = aspr.id_app_sec_rol
and prog.id = asrp.id_app_sec_program
and r.id = asrp.id_app_sec_rol order by p.app_user_name asc