select * from pms_person where ruc = '80080942-4'



insert into pms_person (
id,
ruc,
commercial_name,
is_customer,
is_supplier,
is_functionary,
creation_user
) values
(nextval('pms_person_id_sq'),
'999999999-9',
'PRODUCTION MANAGEMENT SYSTEM USER ENTERPRISE NAME',
'N','N','N',
'xxx'
);


update pms_person set ruc = '80080942-4', commercial_name = UPPER('Bella Vista Textil S.A.')
WHERE id = 60;