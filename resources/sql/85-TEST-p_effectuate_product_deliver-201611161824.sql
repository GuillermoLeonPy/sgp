/*85-TEST-p_effectuate_product_deliver-201611161824*/

delete from pms_siipdm_product_instances_involved;
delete from pms_si_item_product_deposit_movement;
delete from pms_sale_invoice_product_deposit_movement;


update pms_product_deposit_movement
set departure_date = null;

			update pms_sale_invoice_item
			set					
				product_delivered_quantity = 0;

UPDATE pms_product_stock_existence SET quantity = 1;



select vsiipd.id_order, vsiipd.id_order_item, vsiipd.id_sale_invoice,
vsiipd.id_sale_invoice_item, vsiipd.id_product, vsiipd.product_id, 
vsiipd.ord_item_pending_to_production, vsiipd.ord_item_in_progress_quantity, 
vsiipd.invoice_item_total_exigible_quantity, vsiipd.invoice_item_remain_exigible_quantity,
vsiipd.invoice_item_delivered_quantity, vsiipd.invoice_item_product_stock_quantity 
from v_sale_invoice_item_product_deliverables vsiipd 
WHERE vsiipd.id_sale_invoice = 53 