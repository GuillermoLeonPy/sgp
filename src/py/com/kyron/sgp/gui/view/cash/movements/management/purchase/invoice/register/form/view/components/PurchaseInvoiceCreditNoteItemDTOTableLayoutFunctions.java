package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;

public interface PurchaseInvoiceCreditNoteItemDTOTableLayoutFunctions {
	
	public void deletePurchaseInvoiceCreditNoteItemDTOFromPreliminaryList(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO);
	public Long getPurchaseInvoiceCreditNoteDTOId();
	public String getPurchaseInvoiceCreditNoteDTOStatus();
	public void editQuantity(final PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO);

}
