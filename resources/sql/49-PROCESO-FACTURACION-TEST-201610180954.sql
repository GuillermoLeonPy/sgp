delete from pms_sale_invoice_payment;

delete from pms_sale_invoice where id = 40;
delete from pms_sale_invoice_item where id_sale_invoice = 40;

update pms_sale_invoice set previous_status = 'application.common.status.pending' where id = 36;


update pms_order set status = 'application.common.status.pending' where id = 8
update pms_order set status = 'application.common.status.confirmed' where id = 8

DELETE from pms_production_process where id = 8;

select * from pms_production_process_activity where id_production_process = 8;

delete from pms_production_process_activity where id = 13;

select * from pms_product where id = 7;
		select prodp.id, product.product_id 
		from 
		pms_product product left outer join
		pms_production_process prodp 
		on product.id = prodp.id_product 
		where product.id = 7
		and prodp.validity_end_date is null	


		