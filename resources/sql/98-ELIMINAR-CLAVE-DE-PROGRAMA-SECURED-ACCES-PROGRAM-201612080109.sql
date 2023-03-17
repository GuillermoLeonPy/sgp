select * from app_sec_program where program_key = 'secured.access.program.rawmaterial.stock.management.form.tab.rawmaterialexistence.form'

select * from app_sec_rol_program where id_app_sec_program = (select id from app_sec_program where program_key = 'secured.access.program.rawmaterial.stock.management.form.tab.rawmaterialexistence.form')
delete from app_sec_rol_program where id = 315

delete from app_sec_program where id = 313
