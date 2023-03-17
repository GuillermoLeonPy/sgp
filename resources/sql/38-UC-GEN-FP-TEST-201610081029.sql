/*38-UC-GEN-FP-TEST-201610081029*/

begin transaction;

insert into pms_company values (
nextval('pms_company_id_sq'),
'90023716-1',
'BELLA VISTA TEXTIL S.A.',NULL,now(),null,null);

commit;

insert into pms_company_bussines_activity values (
nextval('pms_company_bussines_activity_id_sq'),
currval('pms_company_id_sq'),
'CONFECCION DE UNIFORMES ESCOLARES, EMPRESARIALES, OPERATIVOS INTRUSTRIALES',NULL,now(),null,null);


insert into pms_branch_office
values
(nextval('pms_branch_office_id_sq'),
currval('pms_company_id_sq'),
'001','Rosario 759 c/ Yrendagüe','642-271',NULL,now(),null,null);

insert into pms_branch_office_sale_station
values(
nextval('pms_branch_office_sale_station_id_sq'),
'001','CAJA 01',
currval('pms_branch_office_id_sq'),
NULL,now(),null,null);


--select date_trunc('day', now())

insert into pms_sale_invoice_stamping
values
(
nextval('pms_sale_invoice_stamping_id_sq'),
11724601,date_trunc('day', now()),
(
date_trunc('day',(date_trunc('month', (now() + interval '1 year')) + 
interval '1 month' - interval '1 day')
::timestamp)
+ interval '23:59:59')
,NULL,now(),null,null
);


/* ultimo dia del mes */

select last_day(now())
select (date_trunc('month', now()) + 
    interval '1 month' - interval '1 day')::date;

select (date_trunc('month', now()) + 
    interval '1 month' - interval '1 day')::timestamp;

select (date_trunc('month', now()) + 
interval '1 month' - interval '1 day'
+ interval '23:59:59') 
::timestamp;

select 
(
date_trunc('day',(date_trunc('month', (now() + interval '1 year')) + 
interval '1 month' - interval '1 day')
::timestamp)
+ interval '23:59:59');


select (date_trunc('month', (now() + interval '1 year')) + 
interval '1 month' - interval '1 day'
+ interval '23:59:59') 
::timestamp;

/* ultimo dia del mes */

insert into pms_sale_invoice_stamping_numeration
values
(nextval('pms_sale_invoice_stamping_numeration_id_sq'),
24077,24078,
1,
NULL,now(),null,null,24078
);

commit;

select * from pms_sale_invoice_stamping_numeration
select to_char(start_number,'999G999D99') from pms_sale_invoice_stamping_numeration
select to_char(start_number,'099G999D99') from pms_sale_invoice_stamping_numeration
select to_char(start_number,'09999999') from pms_sale_invoice_stamping_numeration