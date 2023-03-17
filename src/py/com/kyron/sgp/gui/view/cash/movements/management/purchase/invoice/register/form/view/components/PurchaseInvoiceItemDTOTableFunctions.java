package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;

public interface PurchaseInvoiceItemDTOTableFunctions {

	public void deletePurchaseInvoiceItemDTOFromPreliminaryList(final PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO);
	public String getPurchaseInvoiceDTOStatus();
}
