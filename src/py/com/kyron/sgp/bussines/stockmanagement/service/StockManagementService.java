package py.com.kyron.sgp.bussines.stockmanagement.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDepositMovementDTO;

public interface StockManagementService {
	/* raw material purchase request methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialPurchaseRequestDTO insertRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialPurchaseRequestDTO updateRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)throws PmsServiceException ;
	public List<RawMaterialPurchaseRequestDTO> listRawMaterialPurchaseRequestDTO(RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)throws PmsServiceException ;
	
	/* raw material existence methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public RawMaterialExistenceDTO insertRawMaterialExistenceDTO(RawMaterialExistenceDTO rawMaterialExistenceDTO)throws PmsServiceException ;
	public List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO(RawMaterialExistenceDTO rawMaterialExistenceDTO)throws PmsServiceException ;
	
	/**/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public List<PAIRawMaterialSupplyDTO> effectuatePAIRawMaterialSupplyDTO(PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)throws PmsServiceException ;
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO(PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)throws PmsServiceException ;
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTOHistory(PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)throws PmsServiceException ;
	
	public List<SaleInvoiceProductDeliverablesDTO> listSaleInvoiceProductDeliverablesDTO(SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO)throws PmsServiceException ;
	public List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO(SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO)throws PmsServiceException ;
	public Long pmsProductDepositMovementIdentifierNumberBySequence()throws PmsServiceException ;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})
	public SaleInvoiceProductDeliverablesDTO effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO)throws PmsServiceException ;
	public List<SaleInvoiceProductDepositMovementDTO> listSaleInvoiceProductDepositMovementDTO(SaleInvoiceProductDepositMovementDTO saleInvoiceProductDepositMovementDTO)throws PmsServiceException ;
	
	public List<SIItemPDMProductInstanceInvolvedDTO> listProductInstancesReturnableByCreditNoteItemDTO(CreditNoteItemDTO creditNoteItemDTO)throws PmsServiceException ;
	
	public byte[] currentInsufficiencyRawMaterialReport(Long id_currency,HttpSession httpSession)throws PmsServiceException ;
	public byte[] orderAndInvoiceStatusReport(
			final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO,
			HttpSession httpSession)throws PmsServiceException ;
}
