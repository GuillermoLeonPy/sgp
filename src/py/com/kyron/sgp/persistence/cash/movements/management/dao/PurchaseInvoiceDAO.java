package py.com.kyron.sgp.persistence.cash.movements.management.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;

public interface PurchaseInvoiceDAO {

	public Long pmsPurchaseInvoiceDTOIdBySequence();
	public void insertPurchaseInvoiceDTO(PurchaseInvoiceDTO PurchaseInvoiceDTO);
	public void updatePurchaseInvoiceDTO(PurchaseInvoiceDTO PurchaseInvoiceDTO);
	public List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO(PurchaseInvoiceDTO PurchaseInvoiceDTO);
	
	/*item*/
	public Long pmsPurchaseInvoiceItemDTOIdBySequence(long id);
	public void insertPurchaseInvoiceItemDTO(PurchaseInvoiceItemDTO PurchaseInvoiceItemDTO);
	public List<PurchaseInvoiceItemDTO> listPurchaseInvoiceItemDTO(PurchaseInvoiceItemDTO PurchaseInvoiceItemDTO);
	public void deleteDiscardedPurchaseInvoiceItemDTObyPurchaseInvoiceDTO(PurchaseInvoiceDTO PurchaseInvoiceDTO);
	
	public List<PurchaseInvoicePaymentDTO> listPurchaseInvoicePaymentDTO(PurchaseInvoicePaymentDTO PurchaseInvoicePaymentDTO);
	public void effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(CashReceiptDocumentDTO CashReceiptDocumentDTO);
	
	public Long pmsPurchaseInvoiceCreditNoteDTOIdBySequence();
	public void insertPurchaseInvoiceCreditNoteDTO(PurchaseInvoiceCreditNoteDTO PurchaseInvoiceCreditNoteDTO);
	public List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO(PurchaseInvoiceCreditNoteDTO PurchaseInvoiceCreditNoteDTO);
	public Long pmsPurchaseInvoiceCreditNoteItemDTOIdBySequence(long id);
	public void insertPurchaseInvoiceCreditNoteItemDTO(PurchaseInvoiceCreditNoteItemDTO PurchaseInvoiceCreditNoteItemDTO);
	public List<PurchaseInvoiceCreditNoteItemDTO> listPurchaseInvoiceCreditNoteItemDTO(PurchaseInvoiceCreditNoteItemDTO PurchaseInvoiceCreditNoteItemDTO);	
	
}
