/*81-ACTUALIZA-CANT-FISICA-STOCK-COMPROBANTE-COMPRA-201611131331*/

create table pms_pai_rm_supply_purchase_invoice_affected(
id									bigint,
id_pai_raw_material_supply				bigint not null,
id_purchase_invoice						bigint not null,
purchase_invoice_identifier_number			varchar(15) not null,
id_purchase_invoice_item						bigint not null,
quantity								numeric(11,2) NOT NULL,
creation_user								varchar(50),
creation_date								timestamp NOT NULL default now());


ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected ADD CONSTRAINT 
chk_quantity CHECK (quantity > 0);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected 
ADD CONSTRAINT pms_pai_rm_supply_purchase_invoice_affected_id_pk PRIMARY KEY (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_fk_01
FOREIGN KEY (id_pai_raw_material_supply) 
REFERENCES pms_production_activity_instance_raw_material_supply (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_fk_02
FOREIGN KEY (id_purchase_invoice) REFERENCES pms_purchase_invoice (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_fk_03
FOREIGN KEY (id_purchase_invoice_item) REFERENCES pms_purchase_invoice_item (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected ADD CONSTRAINT
pms_pai_rm_supply_purchase_invoice_affected_uk_01 
UNIQUE (id_pai_raw_material_supply,id_purchase_invoice);


create sequence pms_pai_rm_supply_purchase_invoice_affected_id_sq
increment by 1
minvalue 1
no maxvalue
start with 1
no cycle
owned by pms_pai_rm_supply_purchase_invoice_affected.id;


/* *********************** tabla historica ***************************************** */

create table pms_pai_rm_supply_purchase_invoice_affected_history(
id									bigint,
id_pai_raw_material_supply				bigint not null,
id_purchase_invoice						bigint not null,
purchase_invoice_identifier_number			varchar(15) not null,
id_purchase_invoice_item						bigint not null,
quantity								numeric(11,2) NOT NULL,
creation_user								varchar(50),
creation_date								timestamp NOT NULL default now());


ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history ADD CONSTRAINT 
chk_quantity CHECK (quantity > 0);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history 
ADD CONSTRAINT pms_pai_rm_supply_purchase_invoice_affected_history_id_pk PRIMARY KEY (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_history_fk_01
FOREIGN KEY (id_pai_raw_material_supply) 
REFERENCES pms_production_activity_instance_raw_material_supply_history (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_history_fk_02
FOREIGN KEY (id_purchase_invoice) REFERENCES pms_purchase_invoice (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history 
ADD constraint pms_pai_rm_supply_purchase_invoice_affected_history_fk_03
FOREIGN KEY (id_purchase_invoice_item) REFERENCES pms_purchase_invoice_item (id);

ALTER TABLE pms_pai_rm_supply_purchase_invoice_affected_history ADD CONSTRAINT
pms_pai_rm_supply_purchase_invoice_affected_history_uk_01 
UNIQUE (id_pai_raw_material_supply,id_purchase_invoice);


/* ********************************************************************* */
/* ********************************************************************* */
/* ********************************************************************* */
/* ********************************************************************* */



create or replace function p_update_physical_qty_stock_purchase_invoice
(
pid_pai_raw_material_supply				bigint,
pid_raw_material						bigint,
pid_measurment_unit						bigint,
pquantity_required					numeric,
plast_modif_user						varchar,
psafety_number_recursive_calls			bigint
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
	RAISE INFO 'executing p_update_physical_qty_stock_purchase_invoice';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */

	if psafety_number_recursive_calls > 100 then
			declare
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.update.physical.quantity.stock.purchase.invoice.psafety.number.recursive.calls.satefy.limit.reached.error'||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P8888';
			end;
	end if;
	declare
		vcustom_purchase_invoice_item_record			record;
		vmissing_required_quantity		pms_purchase_invoice_item.physical_quantity_in_stock%type;
	begin

		select pi.id id_purchase_invoice, pi.identifier_number, 
		pii.id id_purchase_invoice_item, pii.physical_quantity_in_stock
		INTO vcustom_purchase_invoice_item_record
		from pms_purchase_invoice pi, pms_purchase_invoice_item pii
		where pi.id = pii.id_purchase_invoice
		and pi.id = (select min(spi.id) 
				from pms_purchase_invoice spi, pms_purchase_invoice_item spii		
				where
					(spi.status = 'application.common.status.canceled' 
					or spi.status = 'application.common.status.payment.in.progress'
					or spi.status = 'application.common.status.partial.balance')
					and spi.id = spii.id_purchase_invoice 
					and spi.emission_date = 
								(select min(ispi.emission_date)
								from pms_purchase_invoice ispi, pms_purchase_invoice_item ispii
								where 
								(ispi.status = 'application.common.status.canceled' 
								or ispi.status = 'application.common.status.payment.in.progress'
								or ispi.status = 'application.common.status.partial.balance')
								and ispi.id = ispii.id_purchase_invoice
								and ispii.id_raw_material = pid_raw_material
								and ispii.id_measurment_unit = pid_measurment_unit
								and ispii.physical_quantity_in_stock > 0)
				and spii.id_raw_material = pid_raw_material
				and spii.id_measurment_unit = pid_measurment_unit
				and spii.physical_quantity_in_stock > 0 )
		and pii.id_raw_material = pid_raw_material
		and pii.id_measurment_unit = pid_measurment_unit;

		if vcustom_purchase_invoice_item_record.id_purchase_invoice is not null then
			if vcustom_purchase_invoice_item_record.physical_quantity_in_stock >= pquantity_required then
				
				update pms_purchase_invoice_item
				set physical_quantity_in_stock = vcustom_purchase_invoice_item_record.physical_quantity_in_stock - pquantity_required,
				last_modif_user = plast_modif_user, last_modif_date = now()
				where id = vcustom_purchase_invoice_item_record.id_purchase_invoice_item;

				PERFORM p_i_pai_rm_supply_purchase_invoice_affected
				(pid_pai_raw_material_supply,
				vcustom_purchase_invoice_item_record.id_purchase_invoice,
				vcustom_purchase_invoice_item_record.id_purchase_invoice_item,
				vcustom_purchase_invoice_item_record.identifier_number,
				pquantity_required,
				plast_modif_user);
			else
				--in this case vcustom_purchase_invoice_item_record.physical_quantity_in_stock < pquantity_required
				vmissing_required_quantity := pquantity_required - vcustom_purchase_invoice_item_record.physical_quantity_in_stock;
				update pms_purchase_invoice_item
				set physical_quantity_in_stock = 0,
				last_modif_user = plast_modif_user, last_modif_date = now()
				where id = vcustom_purchase_invoice_item_record.id_purchase_invoice_item;

				PERFORM p_i_pai_rm_supply_purchase_invoice_affected
				(pid_pai_raw_material_supply,
				vcustom_purchase_invoice_item_record.id_purchase_invoice,
				vcustom_purchase_invoice_item_record.id_purchase_invoice_item,
				vcustom_purchase_invoice_item_record.identifier_number,
				vcustom_purchase_invoice_item_record.physical_quantity_in_stock,
				plast_modif_user);

				--recursive call to this function
				PERFORM p_update_physical_qty_stock_purchase_invoice
				(pid_pai_raw_material_supply,
				pid_raw_material,
				pid_measurment_unit,
				vmissing_required_quantity,
				plast_modif_user,
				(psafety_number_recursive_calls + 1));
			end if;--if vcustom_purchase_invoice_item_record.physical_quantity_in_stock >= pquantity_required then
		else
			--IF NO PURCHASE INVOICE FOUNDED:
			--there is no purchase invoice with physical existence to satisfy this operation
			declare
				vraw_material_record		record;
				vmeasurment_unit_record		record;
				b1_error_message		text;
				b1_error_message_hint	text;
			begin
				select * into vraw_material_record from pms_raw_material where id = pid_raw_material;
				select * into vmeasurment_unit_record from pms_measurment_unit where id = pid_measurment_unit;	b1_error_message:='py.com.kyron.sgp.persistence.stockmanagement.dao.rawmaterialexistencedao.update.physical.quantity.stock.purchase.invoice.no.purchase.invoice.to.satisfy.raw.material.deliver.error'||''||vraw_material_record.raw_material_id||''||vmeasurment_unit_record.measurment_unit_id||'end.of.message';
				b1_error_message_hint:=b1_error_message;
				RAISE EXCEPTION '%',b1_error_message USING HINT = b1_error_message_hint, ERRCODE ='P9989';
			end;
		end if;--if vcustom_purchase_invoice_item_record.id_purchase_invoice is not null then
		
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




create or replace function p_i_pai_rm_supply_purchase_invoice_affected
(
pid_pai_raw_material_supply				bigint,
pid_purchase_invoice						bigint,
pid_purchase_invoice_item					bigint,
ppurchase_invoice_identifier_number			varchar,
pquantity_used							numeric,
pcreation_user						varchar
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
	RAISE INFO 'executing p_i_pai_rm_supply_purchase_invoice_affected';
	RAISE INFO '--------------------------';
	/* BEGIN VALIDATIONS */
		insert into pms_pai_rm_supply_purchase_invoice_affected
				(id,
				id_pai_raw_material_supply,
				id_purchase_invoice,
				purchase_invoice_identifier_number,
				id_purchase_invoice_item,
				quantity,
				creation_user) 
		values
				(nextval('pms_pai_rm_supply_purchase_invoice_affected_id_sq'),
				pid_pai_raw_material_supply,
				pid_purchase_invoice,
				ppurchase_invoice_identifier_number,
				pid_purchase_invoice_item,
				pquantity_used,
				pcreation_user);
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
