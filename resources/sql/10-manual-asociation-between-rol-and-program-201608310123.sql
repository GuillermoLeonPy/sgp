select * from app_sec_program

begin transaction;
insert into app_sec_program values 
(nextval('app_sec_program_id_sq'),'main.menu.module.application.utilities.applicationtools');
--rollback;
commit

select * from app_sec_rol
select * from app_sec_rol_program where id_app_sec_rol = 11
begin transaction;
update app_sec_rol_program set is_editable = 'N' where id_app_sec_rol = 11


begin transaction;
insert into app_sec_rol_program values (nextval('app_sec_rol_program_id_sq'), 11,285,'N')
commit;

