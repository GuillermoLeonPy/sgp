package py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components;

import com.vaadin.ui.Button;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;

public interface SaleInvoiceProductDeliverablesDTOTabLayoutFunctions {

	public void effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO) throws PmsServiceException;
	public void navigateToCallerView();
	public void setUpDownloadButton(Button downloadButton);
	public void print(final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO) throws PmsServiceException;
}
