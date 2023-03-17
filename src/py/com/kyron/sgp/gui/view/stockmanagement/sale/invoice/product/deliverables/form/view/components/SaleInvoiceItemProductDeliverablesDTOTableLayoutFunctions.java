package py.com.kyron.sgp.gui.view.stockmanagement.sale.invoice.product.deliverables.form.view.components;

import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;

public interface SaleInvoiceItemProductDeliverablesDTOTableLayoutFunctions {
	public void setRequestDeliverQuantity(final SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO);
	public void queryListSIItemPDMProductInstanceInvolvedDTO(final SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO);
	public SaleInvoiceProductDeliverablesDTO getSaleInvoiceProductDeliverablesDTO();
	public boolean getEditFormMode();
	public boolean getQueryFormMode();
}
