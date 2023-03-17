select * from pms_credit_note where identifier_number = '001-001-0044012'
select * from pms_credit_note_item where id_credit_note = 15;
select sum(value_added_tax_10_unit_price_amount) from pms_credit_note_item where id_credit_note = 10;
select * from pms_credit_note_modified_documents where id_credit_note = 13;

delete from pms_credit_note_item where id_credit_note = 13;
delete from pms_credit_note where id = 13;
delete from pms_credit_note_modified_documents where id = 8;




select * from pms_sale_invoice where identifier_number = '001-001-0024003'
select * from pms_sale_invoice_payment where id_sale_invoice = 36;

update pms_sale_invoice_payment
set annulment_date = null,
status = 'application.common.status.in.progress'
where id = 21


create sequence pms_sale_invoice_payment_cancel_documents_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_sale_invoice_payment_cancel_documents.id;



create or replace view v_credit_note_items as
select cn.identifier_number,cn.modified_documens_identifier_numbers,cn.total_amount,cn.value_added_amount,cn.total_tax_amount,
cn.exempt_amount,cn.value_added_tax_10_amount,cn.value_added_tax_5_amount,cn.tax_10_amount,
cn.tax_5_amount,
prod.product_id, cni.quantity, cni.unit_price_amount, cni.exempt_unit_price_amount,
cni.value_added_tax_10_unit_price_amount,cni.value_added_tax_5_unit_price_amount
from pms_credit_note cn, pms_credit_note_item cni, pms_product prod
where cn.id = cni.id_credit_note
and cni.id_product = prod.id
order by cn.emission_date asc;


select * from v_credit_note_items where modified_documens_identifier_numbers = '001-001-0024014';

select sum(total_amount) from v_credit_note_items where modified_documens_identifier_numbers = '001-001-0024014';
