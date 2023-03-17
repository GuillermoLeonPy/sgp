/*82-VACIAR-LINEA-DE-PRODUCCION-201611132138*/

begin transaction;
select erase_data();
select quantity,physical_quantity_in_stock from pms_purchase_invoice_item;
select calculated_quantity,efective_quantity from pms_raw_material_existence;
commit;
rollback;



create or replace function erase_data
()
returns void as
$BODY$
declare
	error_message          text;
	error_message_hint		text;
	/*EXCEPTION HANDLING VARIABLES*/
	v_RETURNED_SQLSTATE		text;
	v_MESSAGE_TEXT			text;
	v_PG_EXCEPTION_DETAIL	text;
begin
	RAISE INFO '--------------------------';
	RAISE INFO 'executing p_empty_production_line';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	delete from pms_siipdm_product_instances_involved;
	delete from pms_si_item_product_deposit_movement;
	delete from pms_sale_invoice_product_deposit_movement;
	
	delete from pms_product_deposit_movement;

	delete from pms_pai_rm_supply_purchase_invoice_affected_history;
	delete from pms_pai_rm_supply_purchase_invoice_affected;
	
	delete from pms_production_activity_instance_raw_material_supply_history;
	delete from pms_production_activity_instance_history;
	
	delete from pms_siipdm_product_instances_involved;
	delete from pms_si_item_product_deposit_movement;
	delete from pms_sale_invoice_product_deposit_movement;
	delete from pms_pai_rm_supply_purchase_invoice_affected;
	delete from pms_production_activity_instance_raw_material_supply;
	
	delete from pms_product_stock_existence;
	
	update pms_temporary_halfway_product_storage
	set occupied = 'N',	depositor_activity_instance_unique_number = null,
	recaller_activity_instance_unique_number = null;
	
	update pms_temporary_halfway_product_storage_parameters
	set maximun_occupied_locker_number = null,
	avaliable_lockers_quantity = (select count(*) from pms_temporary_halfway_product_storage);
	
	delete from pms_production_activity_instance;

	
	delete from pms_order_item_raw_material_sufficiency_report_detail;
	delete from pms_order_item_raw_material_sufficiency_report;
	delete from pms_order_raw_material_sufficiency_report;

	delete from pms_sale_invoice_payment_cancel_documents;
	delete from pms_cash_receipt_document;
	delete from pms_sale_invoice_payment;
	delete from pms_credit_note_modified_documents;
	delete from pms_credit_note_item;
	delete from pms_credit_note;
	delete from pms_sale_invoice_item;
	delete from pms_sale_invoice;
	delete from pms_order_item;
	delete from pms_order;

	update pms_raw_material_existence
	set calculated_quantity = 0,
	efective_quantity = 0;
	delete from pms_purchase_invoice_payment_cancel_documents;
	delete from pms_purchase_invoice_payment;
	
	update pms_purchase_invoice
	set status = 'application.common.status.pending', emission_date = '2016-12-10 00:00:00'::timestamp,
	credit_purchase_first_payment_fee_date = '2016-12-18 00:00:00'::timestamp;
	update pms_purchase_invoice_item
	set physical_quantity_in_stock = quantity;

	delete from pms_purchase_invoice_credit_note_modified_documents;
	delete from pms_purchase_invoice_credit_note_item;
	delete from pms_purchase_invoice_credit_note;
	EXCEPTION
		WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' or SQLSTATE 'P9998' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||''||error_message_hint||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';


