		select * from pms_production_activity_instance_raw_material_supply
		where id_production_activity_instance = 939

		
		
		select pi.id id_purchase_invoice, pi.identifier_number, 
		pii.id id_purchase_invoice_item, pii.physical_quantity_in_stock
		--INTO vcustom_purchase_invoice_item_record
		from pms_purchase_invoice pi, pms_purchase_invoice_item pii
		where pi.id = pii.id_purchase_invoice
		and pi.id = (select min(spi.id) 
				from pms_purchase_invoice spi, pms_purchase_invoice_item spii		
				where
					spi.id = spii.id_purchase_invoice 
					and spi.emission_date = 
								(select min(ispi.emission_date)
								from pms_purchase_invoice ispi, pms_purchase_invoice_item ispii
								where ispi.id = ispii.id_purchase_invoice
								and ispii.id_raw_material = 1
								and ispii.id_measurment_unit = 8
								and ispii.physical_quantity_in_stock > 0)
				and spii.id_raw_material = 1
				and spii.id_measurment_unit = 8
				and spii.physical_quantity_in_stock > 0 )
		and pii.id_raw_material = 1
		and pii.id_measurment_unit = 8;



		select pi.id id_purchase_invoice, pi.identifier_number, 
		pii.id id_purchase_invoice_item, pii.physical_quantity_in_stock
		--INTO vcustom_purchase_invoice_item_record
		from pms_purchase_invoice pi, pms_purchase_invoice_item pii
		where pi.id = pii.id_purchase_invoice
		and pi.id = (select min(spi.id) 
				from pms_purchase_invoice spi, pms_purchase_invoice_item spii		
				where
					spi.id = spii.id_purchase_invoice 
					and spi.emission_date = 
								(select min(ispi.emission_date)
								from pms_purchase_invoice ispi, pms_purchase_invoice_item ispii
								where ispi.id = ispii.id_purchase_invoice
								and ispii.id_raw_material = 5
								and ispii.id_measurment_unit = 10
								and ispii.physical_quantity_in_stock > 0)
				and spii.id_raw_material = 5
				and spii.id_measurment_unit = 10
				and spii.physical_quantity_in_stock > 0 )
		and pii.id_raw_material = 5
		and pii.id_measurment_unit = 10;



/*83-TEST-ACTUALIZA-CANT-FISICA-STOCK-COMPROBANTE-COMPRA-201611141742*/

select 
rm.raw_material_id,
mu.measurment_unit_id,
pii.quantity,
pii.physical_quantity_in_stock
from pms_purchase_invoice_item pii,
pms_raw_material rm,
pms_measurment_unit mu
where 
pii.id_purchase_invoice = 9
and pii.id_raw_material = rm.id
and pii.id_measurment_unit = mu.id
and pii.id_raw_material = 5
and pii.id_measurment_unit = 10