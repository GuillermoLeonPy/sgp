/*52-GENERAR-NOTA-DE-CREDITO-POR-FACTURA-201610211750*/



create or replace function p_generate_credit_note_by_sale_invoice
(
pid							bigint,
pid_sale_invoice				bigint,
pcreation_user 		   		varchar
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
	RAISE INFO 'executing p_generate_credit_note_by_sale_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	
	declare
		vsale_invoice_record			record;
		vsale_station_record			record;
		vid_sale_invoice_stamping		bigint;/* check sale invoice stamping: ALSO VALID TO OTHER DOCUMENTS !! */
		vcredit_note_stamp_number			pms_credit_note.credit_note_stamp_number%type;
		vid_credit_note_stamping_numeration	pms_credit_note.id_credit_note_stamping_numeration%type;
		videntifier_number							pms_credit_note.identifier_number%type;
	begin
		/* check the sale invoice status in 
				application.common.status.partial.balance 
			or	application.common.status.canceled */
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = pid_sale_invoice;
		if vsale_invoice_record.status != 'application.common.status.partial.balance' 
		and vsale_invoice_record.status != 'application.common.status.canceled' then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.generate.credit.note.by.sale.invoice.sale.invoice.not.in.required.status.error'||''||vsale_invoice_record.identifier_number||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;

		/* determinate the numeration details*/
		/* identifier number *//* branch office and sale station same as sale invoice */
		select bo.id_code_ruc office_ruc ,bost.id_code_ruc sale_station_ruc
		into vsale_station_record
		from pms_branch_office_sale_station bost,pms_branch_office bo
		where
		bost.id = vsale_invoice_record.id_branch_office_sale_station
		and bost.id_branch_office = bo.id;
		
		/* check sale invoice stamping: ALSO VALID TO OTHER DOCUMENTS !! */
		select id into
		vid_sale_invoice_stamping
		from pms_sale_invoice_stamping where 
		is_active = 'S'
		and now() between effective_beginning_date and effective_end_date;
		
		vid_credit_note_stamping_numeration := f_determinate_credit_note_stamping_number_id(vid_sale_invoice_stamping);
		vcredit_note_stamp_number := f_determinate_credit_note_stamping_number(vid_sale_invoice_stamping,pcreation_user);
		videntifier_number := vsale_station_record.office_ruc||'-'||vsale_station_record.sale_station_ruc||'-'||trim(to_char(vcredit_note_stamp_number,'0000000'));

		RAISE INFO '--------------------------';
		RAISE INFO ' CREDIT NOTE identifier number : %', videntifier_number;
		RAISE INFO '--------------------------';


		/* copy from the sale invoice
			1)id_person	2)id_currency	3)bussines_name	4)bussines_ci_ruc
			5)id_branch_office_sale_station
		*/

		insert into pms_credit_note
		(id,	id_person,	id_currency,	modified_documens_identifier_numbers,
		id_branch_office_sale_station,	identifier_number,	credit_note_stamp_number,
		id_credit_note_stamping_numeration,	bussines_name,	bussines_ci_ruc,
		creation_user,
		total_amount,value_added_amount,total_tax_amount,exempt_amount,value_added_tax_10_amount,
		value_added_tax_5_amount,tax_10_amount,tax_5_amount,credit_note_balance)
		values
		(pid,	vsale_invoice_record.id_person, vsale_invoice_record.id_currency, vsale_invoice_record.identifier_number,
		vsale_invoice_record.id_branch_office_sale_station,	videntifier_number,	vcredit_note_stamp_number,
		vid_credit_note_stamping_numeration,	vsale_invoice_record.bussines_name,	vsale_invoice_record.bussines_ci_ruc,
		pcreation_user,
		0,0,0,0,0,
		0,0,0,0);


		insert into pms_credit_note_modified_documents values
		(nextval('pms_credit_note_modified_documents_id_sq'), pid, vsale_invoice_record.id);
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';


select * from pms_credit_note where identifier_number = '001-001-0044003';

create or replace function p_generate_credit_note_item_by_sale_invoice_item
(
pid_credit_note				bigint,
pid_sale_invoice_item			bigint,
pquantity						bigint,
pcreation_user 		   		varchar
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
	RAISE INFO 'executing p_generate_credit_note_item_by_sale_invoice_item';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */	

	-- copy sale invoice item
	--	1)id_product	2)quantity	3)unit_price_amount		4)exempt_unit_price_amount
	--	5)value_added_tax_10_unit_price_amount	6)value_added_tax_5_unit_price_amount
	declare
		vrecord				record;
		vcredit_note_record		record;
		vsale_invoice_record	record;
		vorder_item_record		record;
		vtotal_amount					pms_credit_note.total_amount%type;
		vvalue_added_amount				pms_credit_note.value_added_amount%type;
		vtotal_tax_amount				pms_credit_note.total_tax_amount%type;
		vexempt_amount					pms_credit_note.exempt_amount%type;
		vvalue_added_tax_10_amount		pms_credit_note.value_added_tax_10_amount%type;
		vvalue_added_tax_5_amount			pms_credit_note.value_added_tax_5_amount%type;
		vtax_10_amount					pms_credit_note.tax_10_amount%type;
		vtax_5_amount					pms_credit_note.tax_5_amount%type;
		vcredit_note_balance				pms_credit_note.credit_note_balance%type;

		vsale_invoice_acum_credit_notes_total_amount		pms_credit_note.total_amount%type;
		vcredit_notes_items_qty_acum_total_per_sale_i_item	pms_credit_note.total_amount%type;
		v_product_id					pms_product.product_id%type;
	begin
		select sii.* into vrecord from pms_sale_invoice_item sii where sii.id = pid_sale_invoice_item;

		insert into pms_credit_note_item
		(id,	id_credit_note,
		id_product,	quantity,	unit_price_amount,	
		exempt_unit_price_amount,
		value_added_tax_10_unit_price_amount,	value_added_tax_5_unit_price_amount,
		creation_user,id_sale_invoice_item)
		values
		(nextval('pms_credit_note_item_id_sq'),	pid_credit_note,
		vrecord.id_product,	pquantity,	vrecord.unit_price_amount,	vrecord.exempt_unit_price_amount,
		(vrecord.unit_price_amount * pquantity),	vrecord.value_added_tax_5_unit_price_amount,
		pcreation_user,vrecord.id);

		/* update the credit note totals 
			total_amount
			value_added_amount
			total_tax_amount
			exempt_amount
			value_added_tax_10_amount
			value_added_tax_5_amount
			tax_10_amount
			tax_5_amount
			credit_note_balance
		*/
		
		select cn.* into vcredit_note_record from pms_credit_note cn where cn.id = pid_credit_note;

		if vcredit_note_record.value_added_tax_10_amount is null then
			vvalue_added_tax_10_amount := vrecord.unit_price_amount * pquantity;	
		else
			vvalue_added_tax_10_amount := vcredit_note_record.value_added_tax_10_amount + (vrecord.unit_price_amount * pquantity);	
		end if;

		if vcredit_note_record.value_added_tax_5_amount is null then
			vvalue_added_tax_5_amount := vrecord.value_added_tax_5_unit_price_amount;
		else
			vvalue_added_tax_5_amount := vcredit_note_record.value_added_tax_5_amount + vrecord.value_added_tax_5_unit_price_amount;
		end if;
		
		vvalue_added_amount := vvalue_added_tax_10_amount + vvalue_added_tax_5_amount;

		if vcredit_note_record.exempt_amount is null then		
			vexempt_amount	:= vrecord.exempt_unit_price_amount;
		else
			vexempt_amount	:= vcredit_note_record.exempt_amount + vrecord.exempt_unit_price_amount;
		end if;

		vtotal_amount := vvalue_added_amount + vexempt_amount;
		vtax_10_amount	:= vvalue_added_tax_10_amount * 0.0909;
		vtax_5_amount := vvalue_added_tax_5_amount * 0.0476;
		vtotal_tax_amount := vtax_10_amount + vtax_5_amount;
		vcredit_note_balance := vtotal_amount;
		--check if the acumulated sale invoice item credit note items total amount 
		--is greater than the sale invoice item total amount
		select si.* into vsale_invoice_record from pms_sale_invoice si where si.id = vrecord.id_sale_invoice;

		select sum(cni.quantity) 
		into vcredit_notes_items_qty_acum_total_per_sale_i_item
		from pms_credit_note_item cni, pms_credit_note cn, pms_credit_note_modified_documents cnmd
		where cni.id_credit_note = cn.id
		and cnmd.id_credit_note = cn.id
		and cnmd.id_sale_invoice = vrecord.id_sale_invoice
		and cni.id_sale_invoice_item = vrecord.id;

		select product_id into v_product_id from pms_product where id = vrecord.id_product;

		
		RAISE INFO '--------------------------';
		RAISE INFO ' product : %', v_product_id;
		RAISE INFO ' acumulated quantity in all credit notes for the item: %', vcredit_notes_items_qty_acum_total_per_sale_i_item;
		RAISE INFO ' sale invoice item quantity : %', vrecord.quantity;
		RAISE INFO '--------------------------';

		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			if vcredit_notes_items_qty_acum_total_per_sale_i_item > vrecord.quantity then
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.generate.credit.note.item.by.sale.invoice.item.credit.notes.items.acum.quantity.per.sale.invoice.item.greater.than.sale.invoice.item.quantity.error'||''||v_product_id||''||vsale_invoice_record.identifier_number||''||vrecord.quantity||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
		end;

		/* check if the acumulated credit notes total amount is greater than the invoice amount */
		select sum(cn.total_amount) into vsale_invoice_acum_credit_notes_total_amount
		from pms_credit_note cn, pms_credit_note_modified_documents cnmd
		where cn.id = cnmd.id_credit_note and cnmd.id_sale_invoice = vsale_invoice_record.id;

		RAISE INFO '--------------------------';
		RAISE INFO ' vsale_invoice_acum_credit_notes_total_amount : %', vsale_invoice_acum_credit_notes_total_amount;
		RAISE INFO ' vsale_invoice_record.total_amount : %', vsale_invoice_record.total_amount;
		RAISE INFO ' vsale_invoice_record.identifier_number : %', vsale_invoice_record.identifier_number;
		RAISE INFO '--------------------------';
		
		declare
			b1_error_message		text;
			b1_error_message_hint	text;
		begin
			if vsale_invoice_acum_credit_notes_total_amount > vsale_invoice_record.total_amount then
				b1_error_message:='py.com.kyron.sgp.persistence.cash.movements.management.dao.generate.credit.note.item.by.sale.invoice.item.credit.notes.acumulated.amount.greater.than.sale.invoice.amount.error'||''||'#-numeric-#'||vsale_invoice_acum_credit_notes_total_amount||''||'#-numeric-#'||vsale_invoice_record.total_amount||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end if;
		end;

		update pms_credit_note
			set
		total_amount					= vtotal_amount,
		value_added_amount				= vvalue_added_amount,
		total_tax_amount				= vtotal_tax_amount,
		exempt_amount					= vexempt_amount,
		value_added_tax_10_amount		= vvalue_added_tax_10_amount,
		value_added_tax_5_amount			= vvalue_added_tax_5_amount,
		tax_10_amount					= vtax_10_amount,
		tax_5_amount					= vtax_5_amount,
		credit_note_balance				= vcredit_note_balance

		where id = vcredit_note_record.id;

		/* */
		
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
			error_message:='py.com.kyron.sgp.persistence.unexpected.error'||'end.of.message';
			RAISE EXCEPTION '%',error_message USING HINT = error_message_hint, ERRCODE ='P9998';
end;
$BODY$
LANGUAGE 'plpgsql';



insert into pms_sale_invoice_stamping_numeration
values
(nextval('pms_sale_invoice_stamping_numeration_id_sq'),
24077,24078,
1,
NULL,now(),null,null,24078
);