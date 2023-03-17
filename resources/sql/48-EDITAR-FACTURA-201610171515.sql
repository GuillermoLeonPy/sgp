/*48-EDITAR-FACTURA-201610171515*/

create or replace function p_u_pms_sale_invoice
(
pid	   		 		   			bigint,
pstatus			 				varchar,
pvalue_added_tax_10_amount			numeric,
pvalue_added_tax_5_amount			numeric,
pexempt_amount						numeric,
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
	RAISE INFO 'executing p_u_pms_sale_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
	declare
		vvalue_added_tax_10_amount					pms_sale_invoice.value_added_tax_10_amount%type;
		vvalue_added_tax_5_amount					pms_sale_invoice.value_added_tax_5_amount%type;
		vexempt_amount								pms_sale_invoice.exempt_amount%type;
	
		vvalue_added_amount 						pms_sale_invoice.value_added_amount%type;
		vtotal_amount		 						pms_sale_invoice.total_amount%type;
		vtotal_tax_amount	 						pms_sale_invoice.total_tax_amount%type;
		vtax_10_amount								pms_sale_invoice.tax_10_amount%type;
		vtax_5_amount								pms_sale_invoice.tax_5_amount%type;
		vrecord									record;
		vstatus									pms_sale_invoice.status%type;
		vprevious_status							pms_sale_invoice.previous_status%type;
		vprevious_total_amount						pms_sale_invoice.previous_total_amount%type;
		vprevious_value_added_amount					pms_sale_invoice.previous_value_added_amount%type;
		vprevious_total_tax_amount					pms_sale_invoice.previous_total_tax_amount%type;
		vprevious_exempt_amount						pms_sale_invoice.previous_exempt_amount%type;
		vprevious_value_added_tax_10_amount			pms_sale_invoice.previous_value_added_tax_10_amount%type;
		vprevious_value_added_tax_5_amount				pms_sale_invoice.previous_value_added_tax_5_amount%type;
		vprevious_tax_10_amount						pms_sale_invoice.previous_tax_10_amount%type;
		vprevious_tax_5_amount						pms_sale_invoice.previous_tax_5_amount%type;
	begin

		select si.* into vrecord from pms_sale_invoice si where si.id = pid;
		
		if pstatus = 'application.common.status.revision' 
		and vrecord.previous_status != 'application.common.status.revision' then

			vvalue_added_tax_10_amount := pvalue_added_tax_10_amount;
			vvalue_added_tax_5_amount := pvalue_added_tax_5_amount;
			vexempt_amount := pexempt_amount;
			
			vvalue_added_amount := vvalue_added_tax_10_amount + vvalue_added_tax_5_amount;
			vtotal_amount :=  vvalue_added_amount + vexempt_amount;
			vtax_10_amount := vvalue_added_tax_10_amount * 0.0909;
			vtax_5_amount := vvalue_added_tax_5_amount * 0.0476;
			vtotal_tax_amount := vtax_10_amount + vtax_5_amount;
	
			RAISE INFO '--------------------------';
			RAISE INFO ' invoice amount : %', vtotal_amount;
			RAISE INFO ' invoice tax 10 amount : %', vtax_10_amount;
			RAISE INFO ' invoice tax 5 amount : %', vtax_5_amount;
			RAISE INFO '--------------------------';

			vstatus := vrecord.previous_status;
			vprevious_status := vrecord.status;
			vprevious_total_amount := vrecord.total_amount;
			vprevious_value_added_amount := vrecord.value_added_amount;
			vprevious_total_tax_amount := vrecord.total_tax_amount;
			vprevious_exempt_amount := vrecord.exempt_amount;
			vprevious_value_added_tax_10_amount := vrecord.value_added_tax_10_amount;
			vprevious_value_added_tax_5_amount := vrecord.value_added_tax_5_amount;
			vprevious_tax_10_amount := vrecord.tax_10_amount;
			vprevious_tax_5_amount := vrecord.tax_5_amount;
			
		else	
			vstatus := pstatus;
			vvalue_added_tax_10_amount := vrecord.value_added_tax_10_amount;
			vvalue_added_tax_5_amount := vrecord.value_added_tax_5_amount;
			vexempt_amount := vrecord.exempt_amount;
			
			vvalue_added_amount := vrecord.value_added_amount;
			vtotal_amount := vrecord.total_amount;
			vtax_10_amount := vrecord.tax_10_amount;
			vtax_5_amount := vrecord.tax_5_amount;
			vtotal_tax_amount := vrecord.total_tax_amount;
		end if;

		if pstatus = 'application.common.status.revision' then
			vprevious_status := vrecord.status;
		end if;
		
		update pms_sale_invoice
			set
				status = vstatus,
				total_amount = vtotal_amount,				
				exempt_amount = pexempt_amount,
				value_added_amount = vvalue_added_amount,
				value_added_tax_10_amount = pvalue_added_tax_10_amount,
				value_added_tax_5_amount = pvalue_added_tax_5_amount,
				total_tax_amount = vtotal_tax_amount,
				tax_10_amount = vtax_10_amount,
				tax_5_amount = vtax_5_amount,
				last_modif_user = plast_modif_user,
				last_modif_date = now(),
				previous_status = vprevious_status,
				previous_total_amount = vprevious_total_amount,
				previous_value_added_amount = vprevious_value_added_amount,
				previous_total_tax_amount = vprevious_total_tax_amount,
				previous_exempt_amount = vprevious_exempt_amount,
				previous_value_added_tax_10_amount = vprevious_value_added_tax_10_amount,
				previous_value_added_tax_5_amount = vprevious_value_added_tax_5_amount,
				previous_tax_10_amount = vprevious_tax_10_amount,
				previous_tax_5_amount = vprevious_tax_5_amount
		where
			id = vrecord.id;
				
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



create or replace function p_u_pms_sale_invoice_item
(
pid	   		 		   			bigint,
pstatus							varchar,
pquantity							bigint,
plast_modif_user	 		   			varchar
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
	RAISE INFO 'executing p_u_pms_sale_invoice_item';
	RAISE INFO '--------------------------';
	/*BEGIN VALIDATIONS*/

	declare
		vrecord		record;
	begin
		select i.* into vrecord from pms_sale_invoice_item i where i.id = pid;

		if pstatus = 'application.common.status.discarded' and vrecord.status != 'application.common.status.discarded' and vrecord.previous_status is null then
			update pms_sale_invoice_item
				set
					status = pstatus,
					previous_status = vrecord.status,
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vrecord.id;
		elsif pquantity < vrecord.quantity then
			update pms_sale_invoice_item
				set
					quantity = pquantity,
					previous_quantity = vrecord.quantity,
					value_added_tax_10_unit_price_amount = vrecord.unit_price_amount * pquantity,
					previous_exempt_unit_price_amount = vrecord.exempt_unit_price_amount,
					previous_value_added_tax_10_unit_price_amount = vrecord.value_added_tax_10_unit_price_amount,
					previous_value_added_tax_5_unit_price_amount = vrecord.value_added_tax_5_unit_price_amount,
					
					last_modif_user = plast_modif_user,
					last_modif_date = now()
				where id = vrecord.id;						
		end if;
	end;
			
	EXCEPTION
		WHEN SQLSTATE 'P9989' THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:=v_MESSAGE_TEXT;
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9999';
		WHEN OTHERS THEN
			GET STACKED DIAGNOSTICS v_RETURNED_SQLSTATE = RETURNED_SQLSTATE,v_MESSAGE_TEXT = MESSAGE_TEXT,
	                          		v_PG_EXCEPTION_DETAIL = PG_EXCEPTION_DETAIL;
	          error_message_hint:='SQLSTATE:'||v_RETURNED_SQLSTATE||', MESSAGE_TEXT: '||v_MESSAGE_TEXT;
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';
