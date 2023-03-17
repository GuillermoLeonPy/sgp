package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.management.view.components;

import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;

public interface SearchSaleInvoiceToolComponentHostView {
	public void buildSaleInvoiceDTOTable(final List<SaleInvoiceDTO> listSaleInvoiceDTO) throws Exception;

}
