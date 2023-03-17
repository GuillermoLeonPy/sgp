package py.com.kyron.sgp.persistence.cash.movements.management.dao;

import java.util.List;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoicePaymentDTO;

public interface SaleInvoicePaymentDAO {

	public void insertSaleInvoicePaymentDTO(SaleInvoicePaymentDTO SaleInvoicePaymentDTO);
	public void reGenerateSaleInvoicePaymentDTO(SaleInvoicePaymentDTO SaleInvoicePaymentDTO);
	public void effectuateSaleInvoicePayment(SaleInvoicePaymentDTO SaleInvoicePaymentDTO);
	public List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO(SaleInvoicePaymentDTO SaleInvoicePaymentDTO);
	public List<CashReceiptDocumentDTO> listCashReceiptDocumentDTO(CashReceiptDocumentDTO CashReceiptDocumentDTO);
}
