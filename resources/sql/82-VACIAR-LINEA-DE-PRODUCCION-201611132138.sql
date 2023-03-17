/*82-VACIAR-LINEA-DE-PRODUCCION-201611132138*/
select id from pms_order where identifier_number = 64;
begin transaction;
select p_empty_production_line(29);

commit;

update pms_order_raw_material_sufficiency_report
set is_valid = 'N' where id_order = 18

select * from v_order_product_raw_mat_measur_unit_required_for_one_unit where order_identifier_number = 36

rollback;



create or replace function p_empty_production_line
(/*pid_order			bigint*/)
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

	declare
		vcursor cursor for
		select * from pms_order_item where id_order = pid_order;

		vsale_invoice_item_record		record;
	begin
		for vrecord in vcursor loop
			update pms_order_item
			set
				finished_quantity = 0,
				in_progress_quantity = 0,
				--canceled_entering_production_by_document_quantity = 0,
				--SI HUBO CANCELACION POR NOTA DE CREDITO NO AFECTAR
				pending_to_instanciate_quantity = vrecord.quantity - vrecord.canceled_entering_production_by_document_quantity
			where
				id = vrecord.id;

			select * into vsale_invoice_item_record
			from pms_sale_invoice_item where id_order_item = vrecord.id;

			update pms_sale_invoice_item
			set					
				product_delivered_quantity = 0,
				product_stock_quantity = 0,
				product_deliver_blocked_by_doc_quantity = 0
			where id = vsale_invoice_item_record.id;
		end loop;

		update pms_order set status = 'application.common.status.pre.production'
		where id = pid_order;

		update pms_raw_material_existence
		set calculated_quantity = 0,
		efective_quantity = 0;


		delete from pms_cash_receipt_document where id_purchase_invoice_payment is not null;
		delete from pms_purchase_invoice_payment_cancel_documents;
		delete from pms_purchase_invoice_payment;
		
		update pms_purchase_invoice
		set status = 'application.common.status.pending';

		update pms_purchase_invoice_item
		set physical_quantity_in_stock = quantity;

		update pms_order_raw_material_sufficiency_report
		set is_valid = 'N' where id_order = pid_order;

		delete from pms_sale_invoice_payment_cancel_documents;
		delete from pms_sale_invoice_payment;
		delete from pms_sale_invoice;
		delete from pms_credit_note_item;
		delete from pms_credit_note;
		delete from pms_order_item;
		delete from pms_order;
		
	end;
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


