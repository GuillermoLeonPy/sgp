package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.management.view.components;

import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;

public interface PurchaseInvoiceManagementViewFunctions {
	public void buildPurchaseInvoiceDTOTable(List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO);
	public void goToPurchaseInvoiceRegisterFormView(PurchaseInvoiceDTO vPurchaseInvoiceDTO);
}
