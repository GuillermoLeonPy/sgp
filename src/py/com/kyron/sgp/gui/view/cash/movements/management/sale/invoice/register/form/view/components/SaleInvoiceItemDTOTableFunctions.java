package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;

public interface SaleInvoiceItemDTOTableFunctions {
	public String getSaleInvoiceDTOStatus();
	public void editQuantity(SaleInvoiceItemDTO vSaleInvoiceItemDTO);
	public void reBuildTableAndTotalsPanel();
}
