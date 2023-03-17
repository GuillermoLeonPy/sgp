/*97-AGREGAR-ITEM-MENU-MODULO-UTILIDADES-201612061942*/
select * from app_sec_program where program_key = 'main.menu.module.application.utilities.applicationtools'


select f_app_sec_program_id_sq(1::bigint)

SELECT p_i_app_sec_program((select f_app_sec_program_id_sq(1::bigint)),'main.menu.module.application.utilities.help.program');