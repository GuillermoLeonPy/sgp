/*88-efectuar-devolucion-de-productos-201611202045*/

create or replace function p_effectuate_return_product_deposit_movement
(pproduct_deposit_movement_identifier_number		bigint,
pid_sale_invoice							bigint,
pid_sale_invoice_item						bigint,
pid_credit_note							bigint,
pid_credit_note_item						bigint,
preturn_quantity							bigint,
pproduct_instance_unique_number				bigint,
pcreation_user 		   					varchar)
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
	RAISE INFO 'executing p_i_pms_sale_invoice_product_deposit_movement';
	RAISE INFO '--------------------------';
	declare
		vsale_invoice_record						record;
		vsale_invoice_item_record					record;
		vsale_invoice_product_deposit_movement_record	record;
		vsiprod_dep_movement_already_returned_record		record;
		vsi_item_product_deposit_movement				record;
		vproduct_record							record;
		vproduct_instance_counter_verifier				bigint;
		vsiipdm_product_instances_involved_record		record;
		vcredit_note_record							record;
	begin
		/*check if the product was delivered by the sale invoice and sale invoice item */
		select siipdmpii.* into vsiipdm_product_instances_involved_record
		from
			pms_siipdm_product_instances_involved siipdmpii,
			pms_si_item_product_deposit_movement siipdm,
			pms_sale_invoice_product_deposit_movement sipdm
		where
			siipdmpii.id_si_item_product_deposit_movement = siipdm.id
			and siipdm.id_sale_invoice_product_deposit_movement = sipdm.id
			and siipdm.id_sale_invoice_item = pid_sale_invoice_item
			and sipdm.id_sale_invoice = pid_sale_invoice
			and siipdmpii.product_instance_unique_number = pproduct_instance_unique_number
			and sipdm.movement_type = 'application.common.storage.operation.outcome';

		if vsiipdm_product_instances_involved_record.product_instance_unique_number is null then
			select * into vsale_invoice_item_record from pms_sale_invoice_item where id = pid_sale_invoice_item;
			select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
			select * into vsale_invoice_record from pms_sale_invoice where id = vsale_invoice_item_record.id_sale_invoice;
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.return.product.deposit.movement.product.instance.unique.number.not.delivered.by.sale.invoice.error'||''||'#-numeric-#'||pproduct_instance_unique_number||''||vproduct_record.product_id||''||vsale_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vsiipdm_product_instances_involved_record.product_instance_unique_number is null then

		/*check if the product is already returned by the sale invoice and sale invoice item */
		select 
			siipdmpii2.product_instance_unique_number,
			sipdm2.id_credit_note, sipdm2.credit_note_identifier_number,
			sipdm2.sale_invoice_identifier_number, sipdm2.id_sale_invoice
			into vsiprod_dep_movement_already_returned_record
		from
			pms_siipdm_product_instances_involved siipdmpii2,
			pms_si_item_product_deposit_movement siipdm2,
			pms_sale_invoice_product_deposit_movement sipdm2
		where
			siipdmpii2.product_instance_unique_number = pproduct_instance_unique_number
			and siipdmpii2.id_si_item_product_deposit_movement = siipdm2.id
			and siipdm2.id_sale_invoice_product_deposit_movement = sipdm2.id
			and siipdm2.id_sale_invoice_item = pid_sale_invoice_item
			and sipdm2.id_sale_invoice = pid_sale_invoice
			and sipdm2.movement_type = 'application.common.storage.operation.income';

		if vsiprod_dep_movement_already_returned_record.product_instance_unique_number is not null then
			select * into vsale_invoice_item_record from pms_sale_invoice_item where id = pid_sale_invoice_item;
			select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.return.product.deposit.movement.product.instance.unique.number.already.returned.error'||''||'#-numeric-#'||pproduct_instance_unique_number||''||vproduct_record.product_id||''||vsale_invoice_record.identifier_number||''||vsiprod_dep_movement_already_returned_record.credit_note_identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vsiprod_dep_movement_already_returned_record.product_instance_unique_number is not null then

	
		select * into vsale_invoice_product_deposit_movement_record
		from pms_sale_invoice_product_deposit_movement 
		where product_deposit_movement_identifier_number = pproduct_deposit_movement_identifier_number;

		if vsale_invoice_product_deposit_movement_record.id is null then
			select * into vsale_invoice_record from pms_sale_invoice where id = pid_sale_invoice;
			select * into vcredit_note_record from pms_credit_note where id = pid_credit_note;
			insert into pms_sale_invoice_product_deposit_movement
			(id,
			id_sale_invoice,
			sale_invoice_identifier_number,
			movement_type,
			product_deposit_movement_identifier_number,
			creation_user,
			id_credit_note,
			credit_note_identifier_number)
			values
			(nextval('pms_sale_invoice_product_deposit_movement_id_sq'),
			vsale_invoice_record.id,
			vsale_invoice_record.identifier_number,
			'application.common.storage.operation.income',
			pproduct_deposit_movement_identifier_number,
			pcreation_user,
			vcredit_note_record.id,
			vcredit_note_record.identifier_number);

			select * into vsale_invoice_product_deposit_movement_record
			from pms_sale_invoice_product_deposit_movement 
			where product_deposit_movement_identifier_number = pproduct_deposit_movement_identifier_number;
		end if;--if vsale_invoice_product_deposit_movement_record.id is null then

		select * into vsi_item_product_deposit_movement from pms_si_item_product_deposit_movement
		where id_sale_invoice_product_deposit_movement = vsale_invoice_product_deposit_movement_record.id
		and id_sale_invoice_item = pid_sale_invoice_item
		and id_credit_note_item = pid_credit_note_item;

		if vsi_item_product_deposit_movement.id is null then
			select * into vsale_invoice_item_record from pms_sale_invoice_item where id = pid_sale_invoice_item;
			select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
			insert into pms_si_item_product_deposit_movement
				(id,
				id_sale_invoice_product_deposit_movement,
				id_sale_invoice_item,
				id_product,
				product_id,
				quantity,
				id_credit_note_item)
			values
				(nextval('pms_si_item_product_deposit_movement_id_sq'),
				vsale_invoice_product_deposit_movement_record.id,
				pid_sale_invoice_item,
				vproduct_record.id,
				vproduct_record.product_id,
				preturn_quantity,
				pid_credit_note_item);

			select * into vsi_item_product_deposit_movement from pms_si_item_product_deposit_movement
			where id_sale_invoice_product_deposit_movement = vsale_invoice_product_deposit_movement_record.id
			and id_sale_invoice_item = pid_sale_invoice_item
			and id_credit_note_item = pid_credit_note_item;
		end if;--if vsi_item_product_deposit_movement.id is null then

		insert into pms_siipdm_product_instances_involved
		values
		(vsi_item_product_deposit_movement.id,
		pproduct_instance_unique_number);

		select count(*) into vproduct_instance_counter_verifier
		from pms_siipdm_product_instances_involved
		where id_si_item_product_deposit_movement = vsi_item_product_deposit_movement.id;

		if vproduct_instance_counter_verifier > vsi_item_product_deposit_movement.quantity then
			select * into vsale_invoice_item_record from pms_sale_invoice_item where id = pid_sale_invoice_item;
			select * into vproduct_record from pms_product where id = vsale_invoice_item_record.id_product;
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.product.deposit.movement.dao.product.deposit.movement.effectuate.return.product.deposit.movement.product.instance.unique.number.count.greater.than.returned.quantity.error'||''||vproduct_record.product_id||''||vsi_item_product_deposit_movement.quantity||''||vproduct_instance_counter_verifier||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;		
	end;--begin
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