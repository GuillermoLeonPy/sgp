package py.com.kyron.sgp.bussines.cash.movements.management.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.IncomeExpeditureReportDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ManPowerExpenditurePerFunctionaryDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ProductCostSaleComparisonDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;

public interface CashMovementsManagementService {

	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public SaleInvoiceDTO generateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public SaleInvoiceDTO reGenerateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public SaleInvoiceDTO updateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public SaleInvoiceDTO annulSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException;
	
	public List<SaleInvoiceDTO> listSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO)throws PmsServiceException;
	/**/
	public List<BranchOfficeDTO> listBranchOfficeDTO(BranchOfficeDTO branchOfficeDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public List<SaleInvoicePaymentDTO> insertSaleInvoicePaymentDTO(SaleInvoicePaymentDTO saleInvoicePaymentDTO)throws PmsServiceException;	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public List<SaleInvoicePaymentDTO> reGenerateSaleInvoicePaymentDTO(SaleInvoicePaymentDTO saleInvoicePaymentDTO)throws PmsServiceException;
		
	public List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO(SaleInvoicePaymentDTO saleInvoicePaymentDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CreditNoteDTO insertCreditNoteDTO(CreditNoteDTO creditNoteDTO)throws PmsServiceException;
	public List<CreditNoteDTO> listCreditNoteDTO(CreditNoteDTO creditNoteDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public SaleInvoicePaymentDTO effectuateSaleInvoicePayment(SaleInvoicePaymentDTO saleInvoicePaymentDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PurchaseInvoiceDTO insertPurchaseInvoiceDTO(PurchaseInvoiceDTO purchaseInvoiceDTO)throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PurchaseInvoiceDTO updatePurchaseInvoiceDTO(PurchaseInvoiceDTO purchaseInvoiceDTO)throws PmsServiceException;
	public List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO(PurchaseInvoiceDTO purchaseInvoiceDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PurchaseInvoiceDTO effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(CashReceiptDocumentDTO cashReceiptDocumentDTO)throws PmsServiceException;
	
	public byte[] getSaleInvoiceDTOReport(SaleInvoiceDTO saleInvoiceDTO, HttpSession httpSession)throws PmsServiceException;
	public byte[] getCreditNoteDTOReport(CreditNoteDTO creditNoteDTO,SaleInvoiceDTO saleInvoiceDTO, HttpSession httpSession)throws PmsServiceException;
	public byte[] getIncomeExpeditureReport(IncomeExpeditureReportDTO incomeExpeditureReportDTO, HttpSession httpSession)throws PmsServiceException;
	public byte[] getCashReceiptDocumentDTOReport(CashReceiptDocumentDTO cashReceiptDocumentDTO, HttpSession httpSession)throws PmsServiceException;
	public byte[] getPurchasesFormObligationTaxReport(Date monthYear)throws PmsServiceException;
	public byte[] getSalesFormObligationTaxReport(Date monthYear)throws PmsServiceException;
	
	public byte[] getProductCostSaleComparisonReport(ProductCostSaleComparisonDTO productCostSaleComparisonDTO, HttpSession httpSession)throws PmsServiceException;
	public byte[] manPowerExpenditurePerFunctionaryReport(ManPowerExpenditurePerFunctionaryDTO manPowerExpenditurePerFunctionaryDTO, HttpSession httpSession)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public PurchaseInvoiceCreditNoteDTO insertPurchaseInvoiceCreditNoteDTO(PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)throws PmsServiceException;
	public List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO(PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)throws PmsServiceException;
}
