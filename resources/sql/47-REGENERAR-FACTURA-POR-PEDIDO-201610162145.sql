	/* 47-REGENERAR-FACTURA-POR-PEDIDO-201610162145 */

--ALTER TABLE pms_sale_invoice_item DROP CONSTRAINT pms_sale_invoice_item_uk_02;
	
	create or replace function p_ri_pms_sale_invoice
	(
	pid	   		 		   			bigint,
	pid_order			 				bigint,
	plast_modif_user 		   			varchar
	)
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
		RAISE INFO 'executing p_ri_pms_sale_invoice';
		RAISE INFO '--------------------------';
		/* BEGIN VALIDATIONS */
		declare
		
			b1_error_message		text;
			b1_error_message_hint	text;	
			
			vvalue_added_amount 						pms_sale_invoice.value_added_amount%type;
			vtotal_amount		 						pms_sale_invoice.total_amount%type;
			vtotal_tax_amount	 						pms_sale_invoice.total_tax_amount%type;
			vid_sale_invoice_stamping	 				bigint;
			vtax_10_amount								pms_sale_invoice.tax_10_amount%type;
			vtax_5_amount								pms_sale_invoice.tax_5_amount%type;
			vrecord									record;
			vsale_invoice_record						record;
	
			vprevious_total_amount 						pms_sale_invoice.previous_total_amount%type;
			vprevious_value_added_amount 					pms_sale_invoice.previous_value_added_amount%type;
			vprevious_total_tax_amount 					pms_sale_invoice.previous_total_tax_amount%type;
			vprevious_exempt_amount 						pms_sale_invoice.previous_exempt_amount%type;
			vprevious_value_added_tax_10_amount 			pms_sale_invoice.previous_value_added_tax_10_amount%type;
			vprevious_value_added_tax_5_amount 			pms_sale_invoice.previous_value_added_tax_5_amount%type;
			vprevious_tax_10_amount 						pms_sale_invoice.previous_tax_10_amount%type;
			vprevious_tax_5_amount 						pms_sale_invoice.previous_tax_5_amount%type;
	
		begin
			select ord.* into vrecord from pms_order ord where ord.id = pid_order;
			--16.4.Solo se puede regenerar una factura a partir de un pedido 
			--cuyo atributo pms_order.previous_status = REVISION 
			--y su estado sea alguno de los sgtes: CONFIRMADO,FACTURADO, EN CURSO
			if vrecord.status != 'application.common.status.confirmed' 
			and vrecord.status != 'application.common.status.invoiced' 
			and vrecord.status != 'application.common.status.in.progress' 
			and vrecord.previous_status != 'application.common.status.revision' then
			
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.dao.re.generate.sale.invoice.by.order.order.status.or.previous.status.not.in.allowed.values.error'||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
				
			select si.* into vsale_invoice_record from pms_sale_invoice si 
			where si.id_order = pid_order and si.status != 'application.common.status.annulled';
	
			if vsale_invoice_record.status != 'application.common.status.revision' then
	
					b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.sale.invoice.dao.re.generate.sale.invoice.by.order.sale.invoice.status.not.allowed.value.error'||'end.of.message';
					b1_error_message_hint:=b1_error_message;
					RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
			
			vvalue_added_amount := vrecord.value_added_tax_10_amount + vrecord.value_added_tax_5_amount;
			vtotal_amount :=  vvalue_added_amount + vrecord.exempt_amount;
			vtax_10_amount := vrecord.value_added_tax_10_amount * 0.0909;
			vtax_5_amount := vrecord.value_added_tax_5_amount * 0.0476;
			vtotal_tax_amount := vtax_10_amount + vtax_5_amount;
	
			RAISE INFO '--------------------------';
			RAISE INFO ' order amount : %', vrecord.amount;
			RAISE INFO ' invoice amount : %', vtotal_amount;
			RAISE INFO ' invoice tax 10 amount : %', vtax_10_amount;
			RAISE INFO ' invoice tax 5 amount : %', vtax_5_amount;
			RAISE INFO '--------------------------';
	
			vprevious_total_amount := vsale_invoice_record.total_amount;
			vprevious_value_added_amount := vsale_invoice_record.value_added_amount;
			vprevious_total_tax_amount := vsale_invoice_record.total_tax_amount;
			vprevious_exempt_amount := vsale_invoice_record.exempt_amount;
			vprevious_value_added_tax_10_amount := vsale_invoice_record.value_added_tax_10_amount;
			vprevious_value_added_tax_5_amount := vsale_invoice_record.value_added_tax_5_amount;
			vprevious_tax_10_amount := vsale_invoice_record.tax_10_amount;
			vprevious_tax_5_amount := vsale_invoice_record.tax_5_amount;
	
			
			update pms_sale_invoice
				set
					total_amount = vtotal_amount,				
					exempt_amount = vrecord.exempt_amount,
					value_added_amount = vvalue_added_amount,
					value_added_tax_10_amount = vrecord.value_added_tax_10_amount,
					value_added_tax_5_amount = vrecord.value_added_tax_5_amount,
					total_tax_amount = vtotal_tax_amount,
					tax_10_amount = vtax_10_amount,
					tax_5_amount = vtax_5_amount,
					last_modif_user = plast_modif_user,
					last_modif_date = now(),
					status = vsale_invoice_record.previous_status,
					previous_status = vsale_invoice_record.status
				where 
					id = vsale_invoice_record.id;
	
			/* update sale invoice items */
			declare
			    vcursor CURSOR FOR
			    select * from pms_order_item where id_order = vrecord.id order by id asc;
			    --LISTA ORDENADA POR ID ASCENDENTE
			    vsale_invoice_item_record			record;
			    vid_sale_invoice_item				pms_sale_invoice_item.id%type;
			begin
				for vorder_item_record in vcursor loop
					if vorder_item_record.status = 'application.common.status.discarded' then
						select sii.* into vsale_invoice_item_record from pms_sale_invoice_item sii 
						where sii.id_sale_invoice = vsale_invoice_record.id
						and sii.id_order_item = vorder_item_record.id;
						update pms_sale_invoice_item
							set 
								status = vorder_item_record.status,
								previous_status = vsale_invoice_item_record.status,
								last_modif_user = plast_modif_user,
								last_modif_date = now()
							where id = vsale_invoice_item_record.id;
					else--if vorder_item_record.status = 'application.common.status.discarded' then
						--check if an item has been added
						select id into vid_sale_invoice_item from pms_sale_invoice_item 
						where id_sale_invoice = vsale_invoice_record.id
						and id_order_item = vorder_item_record.id;
						if vid_sale_invoice_item is null then
							insert into pms_sale_invoice_item
								(id,
								id_sale_invoice,
								id_product,
								id_order_item,
								quantity,
								unit_price_amount,
								exempt_unit_price_amount,
								value_added_tax_10_unit_price_amount,
								value_added_tax_5_unit_price_amount,
								creation_user,
								product_exigible_by_invoice_quantity)
							values
								(nextval('pms_sale_invoice_item_id_sq'),
								pid,
								vorder_item_record.id_product,
								vorder_item_record.id,
								vorder_item_record.quantity,
								vorder_item_record.unit_price_amount,
								vorder_item_record.exempt_unit_price_amount,
								vorder_item_record.value_added_tax_10_unit_price_amount,
								vorder_item_record.value_added_tax_5_unit_price_amount,
								plast_modif_user,
								vorder_item_record.quantity);
						end if;--if vid_sale_invoice_item is null then
					end if;--if vorder_item_record.status = 'application.common.status.discarded' then
				end loop;--for vorder_item_record in vcursor loop
			end;/* update sale invoice items */
			
	
		--16.8. Si el monto de la nueva factura es menor al monto de la sumatoria de los pagos cancelados
		--de la primera se debe emitir una nota de crédito usable por el cliente [ESTADO] y otra no
		--usable por el cliente, la primera por el monto de la diferencia entre: 1)la sumatoria de pagos
		--cancelados de la primera factura y 2) el monto de la nueva y/o ultima factura, la segunda
		--nota de crédito igual al monto de la 1era factura menos el monto de la 2da factura menos el
		--monto de la 1era nota de crédito.
		--16.9. Si el monto de la nueva factura es mayor al monto de la sumatoria de los pagos cancelados
		--de la 1era: se deben generar registros de pago por la diferencia entre el monto de la
		--sumatoria de los pagos cancelados de la 1era y el monto de la nueva factura y dividir la
		--diferencia proporcionalmente según la cantidad de pagos pendientes a ser ejecutados por la
		--1era factura: al efecto de crear pagos equitativos. Se debe crear un registro de pago por la
		--nueva factura equivalente a la sumatoria de los pagos cancelados de la primera.
	
		end;	
		
		EXCEPTION
			WHEN SQLSTATE 'P9989' or SQLSTATE 'P9999' THEN
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
