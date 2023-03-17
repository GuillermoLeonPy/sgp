package py.com.kyron.sgp.gui.view.cash.movements.management.purchase.invoice.register.form.view.components;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;

public interface PurchaseInvoiceRegisterFormViewFunctions {
	public void navigateToCallerView();
	public void saveButtonActionPurchaseInvoiceDTOTabLayout(final PurchaseInvoiceDTO purchaseInvoiceDTO) throws PmsServiceException;
	public void initAndShowCashReceiptDocumentDTOWindow(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO)throws PmsServiceException;
	public void queryCashReceiptDocumentDTOWindow(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO);
	public void insertPurchaseInvoiceCreditNoteDTO(final PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)throws PmsServiceException;
	public void cancelButtonPurchaseInvoiceCreditNoteDTOLayout(final boolean editFormMode);
	public void preparePurchaseInvoicePaymentDTOPaymentProcedure(final PurchaseInvoicePaymentDTO purchaseInvoicePaymentDTO)throws PmsServiceException;
}
