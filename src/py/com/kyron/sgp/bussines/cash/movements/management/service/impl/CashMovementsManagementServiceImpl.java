/**
 * 
 */
package py.com.kyron.sgp.bussines.cash.movements.management.service.impl;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.RuleBasedNumberFormat;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.application.utils.ReportBuilder;
import py.com.kyron.sgp.bussines.application.utils.TextContentFileBuilder;
import py.com.kyron.sgp.bussines.application.utils.VerifierDigitChecker;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.BranchOfficeSaleStationDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CashReceiptDocumentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.IncomeExpeditureReportDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ManPowerExpenditurePerFunctionaryDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.ProductCostSaleComparisonDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceCreditNoteItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceItemDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoicePaymentDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.personmanagement.service.PersonManagementService;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.service.ProductionManagementService;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.persistence.cash.movements.management.dao.CashMovementsReportDAO;
import py.com.kyron.sgp.persistence.cash.movements.management.dao.CreditNoteDAO;
import py.com.kyron.sgp.persistence.cash.movements.management.dao.PurchaseInvoiceDAO;
import py.com.kyron.sgp.persistence.cash.movements.management.dao.SaleInvoiceDAO;
import py.com.kyron.sgp.persistence.cash.movements.management.dao.SaleInvoicePaymentDAO;
import py.com.kyron.sgp.persistence.stockmanagement.dao.ProductDepositMovementDAO;

/**
 * @author testuser
 *
 */
public class CashMovementsManagementServiceImpl implements CashMovementsManagementService {
	
	private final Logger logger = LoggerFactory.getLogger(CashMovementsManagementServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private SaleInvoiceDAO saleInvoiceDAO;
	private ComercialManagementService comercialManagementService;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	private SaleInvoicePaymentDAO saleInvoicePaymentDAO;
	private CreditNoteDAO creditNoteDAO;
	private PurchaseInvoiceDAO purchaseInvoiceDAO;
	private PersonManagementService personManagementService;
	private ProductionManagementService productionManagementService;
	private ProductDepositMovementDAO productDepositMovementDAO;
	private SgpUtils sgpUtils;
	private CashMovementsReportDAO cashMovementsReportDAO;
	private TextContentFileBuilder textContentFileBuilder;
	private final String COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX = "report.template.cash.movements.management.common.form.obligation.tax.report.";
	private final String MAN_POWER_EXPENDITURE_PER_FUNCTIONARY_REPORT_PREFIX = "report.template.cash.movements.management.man.power.expenditure.per.functionary.report.";
	

	/**
	 * 
	 */
	public CashMovementsManagementServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService#insertSaleInvoiceDTO(py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO)
	 */
	@Override
	public SaleInvoiceDTO generateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			saleInvoiceDTO.setId(this.saleInvoiceDAO.pmsSaleInvoiceDTOIdBySequence());
			this.saleInvoiceDAO.insertSaleInvoiceDTO(saleInvoiceDTO);
			SaleInvoiceDTO vSaleInvoiceDTO = this.saleInvoiceDAO.listSaleInvoiceDTO(saleInvoiceDTO).get(0);
			List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO = this.saleInvoiceDAO.listSaleInvoiceItemDTO(new SaleInvoiceItemDTO(null, vSaleInvoiceDTO.getId()));
			for(SaleInvoiceItemDTO vSaleInvoiceItemDTO : listSaleInvoiceItemDTO){
				vSaleInvoiceItemDTO.setProductDTO(this.comercialManagementService.listProductDTO(new ProductDTO(vSaleInvoiceItemDTO.getId_product())).get(0));
			}
			vSaleInvoiceDTO.setListSaleInvoiceItemDTO(listSaleInvoiceItemDTO);
			this.insertSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, saleInvoiceDTO.getId()));
			return vSaleInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				("py.com.softshop.dtp.persistence.unexpected.error",
						e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale
				(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService#updateSaleInvoiceDTO(py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO)
	 */
	@Override
	public SaleInvoiceDTO updateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			
			this.saleInvoiceDAO.updateSaleInvoiceDTO(saleInvoiceDTO);			
			/* update sale invoice items */
			for(SaleInvoiceItemDTO vSaleInvoiceItemDTO : saleInvoiceDTO.getListSaleInvoiceItemDTO()){
				this.saleInvoiceDAO.updateSaleInvoiceItemDTO(vSaleInvoiceItemDTO);
			}
			
			if(saleInvoiceDTO.getStatus().equals("application.common.status.revision")){
				final OrderDTO vOrderDTO = this.comercialManagementService.listOrderDTO(new OrderDTO(saleInvoiceDTO.getId_order())).get(0);
				vOrderDTO.setPrevious_status(vOrderDTO.getStatus());
				vOrderDTO.setStatus("application.common.status.revision");
				this.comercialManagementService.updateOrderDTO(vOrderDTO);
			}
			return saleInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.cash.movements.management.service.CashMovementsManagementService#listSaleInvoiceDTO(py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO)
	 */
	@Override
	public List<SaleInvoiceDTO> listSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO)throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<SaleInvoiceDTO> listSaleInvoiceDTO = this.saleInvoiceDAO.listSaleInvoiceDTO(saleInvoiceDTO);
			for(SaleInvoiceDTO vSaleInvoiceDTO : listSaleInvoiceDTO){
				vSaleInvoiceDTO.setOrderDTO(this.comercialManagementService.listOrderDTO(new OrderDTO(vSaleInvoiceDTO.getId_order())).get(0));
				List<SaleInvoiceItemDTO> listSaleInvoiceItemDTO = this.saleInvoiceDAO.listSaleInvoiceItemDTO(new SaleInvoiceItemDTO(null, vSaleInvoiceDTO.getId()));
				for(SaleInvoiceItemDTO vSaleInvoiceItemDTO : listSaleInvoiceItemDTO){
					vSaleInvoiceItemDTO.setProductDTO(this.comercialManagementService.listProductDTO(new ProductDTO(vSaleInvoiceItemDTO.getId_product())).get(0));
				}
				vSaleInvoiceDTO.setListSaleInvoiceItemDTO(listSaleInvoiceItemDTO);
			}
			return listSaleInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the bussinesSessionUtils
	 */
	public BussinesSessionUtils getBussinesSessionUtils() {
		return bussinesSessionUtils;
	}

	/**
	 * @param bussinesSessionUtils the bussinesSessionUtils to set
	 */
	public void setBussinesSessionUtils(BussinesSessionUtils bussinesSessionUtils) {
		this.bussinesSessionUtils = bussinesSessionUtils;
	}

	/**
	 * @return the saleInvoiceDAO
	 */
	public SaleInvoiceDAO getSaleInvoiceDAO() {
		return saleInvoiceDAO;
	}

	/**
	 * @param saleInvoiceDAO the saleInvoiceDAO to set
	 */
	public void setSaleInvoiceDAO(SaleInvoiceDAO saleInvoiceDAO) {
		this.saleInvoiceDAO = saleInvoiceDAO;
	}


	public void init(){
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
		this.sgpUtils = new SgpUtils();
	}

	/**
	 * @return the comercialManagementService
	 */
	public ComercialManagementService getComercialManagementService() {
		return comercialManagementService;
	}

	/**
	 * @param comercialManagementService the comercialManagementService to set
	 */
	public void setComercialManagementService(
			ComercialManagementService comercialManagementService) {
		this.comercialManagementService = comercialManagementService;
	}

	@Override
	public List<BranchOfficeDTO> listBranchOfficeDTO(BranchOfficeDTO branchOfficeDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<BranchOfficeDTO> listBranchOfficeDTO = this.saleInvoiceDAO.listBranchOfficeDTO(branchOfficeDTO);
			for(BranchOfficeDTO vBranchOfficeDTO : listBranchOfficeDTO){
				vBranchOfficeDTO.setListBranchOfficeSaleStationDTO(this.saleInvoiceDAO.listBranchOfficeSaleStationDTO(new BranchOfficeSaleStationDTO(null, vBranchOfficeDTO.getId())));
			}
			return listBranchOfficeDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the saleInvoicePaymentDAO
	 */
	public SaleInvoicePaymentDAO getSaleInvoicePaymentDAO() {
		return saleInvoicePaymentDAO;
	}

	/**
	 * @param saleInvoicePaymentDAO the saleInvoicePaymentDAO to set
	 */
	public void setSaleInvoicePaymentDAO(SaleInvoicePaymentDAO saleInvoicePaymentDAO) {
		this.saleInvoicePaymentDAO = saleInvoicePaymentDAO;
	}

	@Override
	public List<SaleInvoicePaymentDTO> insertSaleInvoicePaymentDTO(
			SaleInvoicePaymentDTO saleInvoicePaymentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.saleInvoicePaymentDAO.insertSaleInvoicePaymentDTO(saleInvoicePaymentDTO);
			return this.listSaleInvoicePaymentDTO(saleInvoicePaymentDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO(
			SaleInvoicePaymentDTO saleInvoicePaymentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<SaleInvoicePaymentDTO> listSaleInvoicePaymentDTO = this.saleInvoicePaymentDAO.listSaleInvoicePaymentDTO(saleInvoicePaymentDTO);
			for(SaleInvoicePaymentDTO vSaleInvoicePaymentDTO : listSaleInvoicePaymentDTO){
				vSaleInvoicePaymentDTO.setCurrencyDTO(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vSaleInvoicePaymentDTO.getId_currency())).get(0));
				List<CashReceiptDocumentDTO> listCashReceiptDocumentDTO = 
						this.saleInvoicePaymentDAO.listCashReceiptDocumentDTO
						(new CashReceiptDocumentDTO(null, vSaleInvoicePaymentDTO.getId()));
				if(listCashReceiptDocumentDTO!=null && !listCashReceiptDocumentDTO.isEmpty()){					
					listCashReceiptDocumentDTO.get(0).setCurrencyDTO(vSaleInvoicePaymentDTO.getCurrencyDTO());					
					vSaleInvoicePaymentDTO.setCashReceiptDocumentDTO(listCashReceiptDocumentDTO.get(0));
				}
			}
			return listSaleInvoicePaymentDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public SaleInvoiceDTO reGenerateSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.saleInvoiceDAO.reGenerateSaleInvoiceDTO(saleInvoiceDTO);	
			this.reGenerateSaleInvoicePaymentDTO(new SaleInvoicePaymentDTO(null, saleInvoiceDTO.getId()));
			return this.listSaleInvoiceDTO(new SaleInvoiceDTO(saleInvoiceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<SaleInvoicePaymentDTO> reGenerateSaleInvoicePaymentDTO(
			SaleInvoicePaymentDTO saleInvoicePaymentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.saleInvoicePaymentDAO.reGenerateSaleInvoicePaymentDTO(saleInvoicePaymentDTO);
			return this.listSaleInvoicePaymentDTO(saleInvoicePaymentDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public SaleInvoiceDTO annulSaleInvoiceDTO(SaleInvoiceDTO saleInvoiceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.saleInvoiceDAO.annulSaleInvoiceDTO(saleInvoiceDTO);
			return this.listSaleInvoiceDTO(new SaleInvoiceDTO(saleInvoiceDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the creditNoteDAO
	 */
	public CreditNoteDAO getCreditNoteDAO() {
		return creditNoteDAO;
	}

	/**
	 * @param creditNoteDAO the creditNoteDAO to set
	 */
	public void setCreditNoteDAO(CreditNoteDAO creditNoteDAO) {
		this.creditNoteDAO = creditNoteDAO;
	}

	@Override
	public CreditNoteDTO insertCreditNoteDTO(CreditNoteDTO creditNoteDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			creditNoteDTO.setId(this.creditNoteDAO.pmsCreditNoteDTOIdBySequence());
			this.creditNoteDAO.insertCreditNoteDTO(creditNoteDTO);
			Long productDepositMovementIdentifierNumber = null;
			long idGenerationHelperVar = 0;
			int creditNoteItemsProcessedCounter = 0;
			for(CreditNoteItemDTO vCreditNoteItemDTO : creditNoteDTO.getListCreditNoteItemDTO()){
				if((vCreditNoteItemDTO.getCancellation_withdrawal_quantity() != null && vCreditNoteItemDTO.getCancellation_withdrawal_quantity() > 0)
				||(vCreditNoteItemDTO.getDevolution_quantity() !=null  && vCreditNoteItemDTO.getDevolution_quantity() > 0)){					
					vCreditNoteItemDTO.setId_credit_note(creditNoteDTO.getId());
					vCreditNoteItemDTO.setId(this.creditNoteDAO.pmsCreditNoteItemDTOIdBySequence(idGenerationHelperVar++));
					this.creditNoteDAO.insertCreditNoteItemDTO(vCreditNoteItemDTO);
					if(vCreditNoteItemDTO.getListProductInstances() != null && !vCreditNoteItemDTO.getListProductInstances().isEmpty()){
						if(productDepositMovementIdentifierNumber == null)productDepositMovementIdentifierNumber = this.productDepositMovementDAO.pmsProductDepositMovementIdentifierNumberBySequence();
						for(SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO : 
							vCreditNoteItemDTO.getListProductInstances()){							
							if(vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit()!=null && vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit()){
								vSIItemPDMProductInstanceInvolvedDTO.setProduct_deposit_movement_identifier_number(productDepositMovementIdentifierNumber);
								vSIItemPDMProductInstanceInvolvedDTO.setId_sale_invoice(creditNoteDTO.getId_sale_invoice());
								vSIItemPDMProductInstanceInvolvedDTO.setId_sale_invoice_item(vCreditNoteItemDTO.getId_sale_invoice_item());
								vSIItemPDMProductInstanceInvolvedDTO.setId_credit_note(creditNoteDTO.getId());
								vSIItemPDMProductInstanceInvolvedDTO.setId_credit_note_item(vCreditNoteItemDTO.getId());
								vSIItemPDMProductInstanceInvolvedDTO.setReturn_quantity(vCreditNoteItemDTO.getDevolution_quantity());								
								this.productDepositMovementDAO.effectuateProductReturnSIItemPDMProductInstanceInvolvedDTO(vSIItemPDMProductInstanceInvolvedDTO);
							}//if(vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit()!=null && vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit()){
						}//for(SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO :
					}//if(vCreditNoteItemDTO.getListReturnableProductInstances() != null && !vCreditNoteItemDTO.getListReturnableProductInstances().isEmpty()){
					creditNoteItemsProcessedCounter++;
				}//if((vCreditNoteItemDTO.getCancellation_withdrawal_quantity() != null && vCreditNoteItemDTO.getCancellation_withdrawal_quantity() > 0)
				//||(vCreditNoteItemDTO.getDevolution_quantity() !=null  && vCreditNoteItemDTO.getDevolution_quantity() > 0)){
			}//for(CreditNoteItemDTO vCreditNoteItemDTO : creditNoteDTO.getListCreditNoteItemDTO()){
			if(creditNoteItemsProcessedCounter == 0){
				throw new Exception(
						new Exception
				("py.com.kyron.sgp.bussines.cash.movements.management.service.impl.cash.movements.management.service.impl.insert.credit.note.not.any.credit.note.item.processed.error"));
			}
			return this.listCreditNoteDTO(new CreditNoteDTO(creditNoteDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<CreditNoteDTO> listCreditNoteDTO(CreditNoteDTO creditNoteDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<CreditNoteDTO> listCreditNoteDTO = this.creditNoteDAO.listCreditNoteDTO(creditNoteDTO);
			for(CreditNoteDTO vCreditNoteDTO : listCreditNoteDTO){
				vCreditNoteDTO.setListCreditNoteItemDTO(this.creditNoteDAO.listCreditNoteItemDTO(new CreditNoteItemDTO(null, vCreditNoteDTO.getId())));
				vCreditNoteDTO.setCurrencyDTO(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vCreditNoteDTO.getId_currency())).get(0));
				for(CreditNoteItemDTO vCreditNoteItemDTO : vCreditNoteDTO.getListCreditNoteItemDTO()){
					vCreditNoteItemDTO.setProductDTO(this.comercialManagementService.listProductDTO(new ProductDTO(vCreditNoteItemDTO.getId_product())).get(0));
					vCreditNoteItemDTO.setId_sale_invoice(creditNoteDTO.getId_sale_invoice());
					vCreditNoteItemDTO.setListProductInstances(this.productDepositMovementDAO.listProductInstancesReturnedByCreditNoteItemDTO(vCreditNoteItemDTO));
				}
			}
			return listCreditNoteDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public SaleInvoicePaymentDTO effectuateSaleInvoicePayment(
			SaleInvoicePaymentDTO saleInvoicePaymentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.saleInvoicePaymentDAO.effectuateSaleInvoicePayment(saleInvoicePaymentDTO);
			return saleInvoicePaymentDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the purchaseInvoiceDAO
	 */
	public PurchaseInvoiceDAO getPurchaseInvoiceDAO() {
		return purchaseInvoiceDAO;
	}

	/**
	 * @param purchaseInvoiceDAO the purchaseInvoiceDAO to set
	 */
	public void setPurchaseInvoiceDAO(PurchaseInvoiceDAO purchaseInvoiceDAO) {
		this.purchaseInvoiceDAO = purchaseInvoiceDAO;
	}

	@Override
	public PurchaseInvoiceDTO insertPurchaseInvoiceDTO(
			PurchaseInvoiceDTO purchaseInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			purchaseInvoiceDTO.setId(this.purchaseInvoiceDAO.pmsPurchaseInvoiceDTOIdBySequence());
			this.purchaseInvoiceDAO.insertPurchaseInvoiceDTO(purchaseInvoiceDTO);
			long idItem = 0;
			for(PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO : purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()){
				vPurchaseInvoiceItemDTO.setId_purchase_invoice(purchaseInvoiceDTO.getId());
				vPurchaseInvoiceItemDTO.setId(this.purchaseInvoiceDAO.pmsPurchaseInvoiceItemDTOIdBySequence(idItem++));
				this.purchaseInvoiceDAO.insertPurchaseInvoiceItemDTO(vPurchaseInvoiceItemDTO);
			}
			return purchaseInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public PurchaseInvoiceDTO updatePurchaseInvoiceDTO(
			PurchaseInvoiceDTO purchaseInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			/*
			 * FIRST DO THE MODIFICATIONS IN THE DETAIL
			 * */
			
			long idItem = 0;
			for(PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO : purchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()){
				if(vPurchaseInvoiceItemDTO.getId() == null){
					vPurchaseInvoiceItemDTO.setId_purchase_invoice(purchaseInvoiceDTO.getId());
					vPurchaseInvoiceItemDTO.setId(this.purchaseInvoiceDAO.pmsPurchaseInvoiceItemDTOIdBySequence(idItem++));
					this.purchaseInvoiceDAO.insertPurchaseInvoiceItemDTO(vPurchaseInvoiceItemDTO);
				}
			}
			this.purchaseInvoiceDAO.deleteDiscardedPurchaseInvoiceItemDTObyPurchaseInvoiceDTO(purchaseInvoiceDTO);
			/*
			 * AND LAST DO THE MODIFICATION OF THE MASTER DATA
			 * the update program has logic wich involve analisis of the detail records set 
			 * */
			this.purchaseInvoiceDAO.updatePurchaseInvoiceDTO(purchaseInvoiceDTO);
			return purchaseInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO(
			PurchaseInvoiceDTO purchaseInvoiceDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO = this.purchaseInvoiceDAO.listPurchaseInvoiceDTO(purchaseInvoiceDTO);
			for(PurchaseInvoiceDTO vPurchaseInvoiceDTO : listPurchaseInvoiceDTO){
				vPurchaseInvoiceDTO.setListPurchaseInvoiceItemDTO(this.purchaseInvoiceDAO.listPurchaseInvoiceItemDTO(new PurchaseInvoiceItemDTO(null, vPurchaseInvoiceDTO.getId(), null)));
				vPurchaseInvoiceDTO.setPersonDTO(this.personManagementService.listPersonDTO(new PersonDTO(vPurchaseInvoiceDTO.getId_person())).get(0));
				vPurchaseInvoiceDTO.setCurrencyDTO(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vPurchaseInvoiceDTO.getId_currency())).get(0));
				vPurchaseInvoiceDTO.setListPurchaseInvoicePaymentDTO(
						this.purchaseInvoiceDAO.listPurchaseInvoicePaymentDTO
						(new PurchaseInvoicePaymentDTO
									(null,vPurchaseInvoiceDTO.getId())
						)
					);
				for(PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO : vPurchaseInvoiceDTO.getListPurchaseInvoicePaymentDTO()){
					vPurchaseInvoicePaymentDTO.setCurrencyDTO(vPurchaseInvoiceDTO.getCurrencyDTO());
					List<CashReceiptDocumentDTO> listCashReceiptDocumentDTO  = 
							this.saleInvoicePaymentDAO.listCashReceiptDocumentDTO
							(new CashReceiptDocumentDTO(null, null, vPurchaseInvoicePaymentDTO.getId()));
					if(listCashReceiptDocumentDTO!=null && !listCashReceiptDocumentDTO.isEmpty())
						vPurchaseInvoicePaymentDTO.setCashReceiptDocumentDTO(listCashReceiptDocumentDTO.get(0));
				}//for(PurchaseInvoicePaymentDTO vPurchaseInvoicePaymentDTO : vPurchaseInvoiceDTO.getListPurchaseInvoicePaymentDTO()){
				
				for(PurchaseInvoiceItemDTO vPurchaseInvoiceItemDTO : vPurchaseInvoiceDTO.getListPurchaseInvoiceItemDTO()){
					vPurchaseInvoiceItemDTO.setRawMaterialDTO(this.productionManagementService.listRawMaterialDTO(new RawMaterialDTO(vPurchaseInvoiceItemDTO.getId_raw_material())).get(0));
					vPurchaseInvoiceItemDTO.setMeasurmentUnitDTO(this.productionManagementService.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vPurchaseInvoiceItemDTO.getId_measurment_unit())).get(0));
				}
			}
			return listPurchaseInvoiceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the personManagementService
	 */
	public PersonManagementService getPersonManagementService() {
		return personManagementService;
	}

	/**
	 * @param personManagementService the personManagementService to set
	 */
	public void setPersonManagementService(
			PersonManagementService personManagementService) {
		this.personManagementService = personManagementService;
	}

	/**
	 * @return the productionManagementService
	 */
	public ProductionManagementService getProductionManagementService() {
		return productionManagementService;
	}

	/**
	 * @param productionManagementService the productionManagementService to set
	 */
	public void setProductionManagementService(
			ProductionManagementService productionManagementService) {
		this.productionManagementService = productionManagementService;
	}

	@Override
	public PurchaseInvoiceDTO effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(
			CashReceiptDocumentDTO cashReceiptDocumentDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final Map<String,String> map = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice.");
			cashReceiptDocumentDTO.setPms_implementing_enterprise_ruc(map.get("report.template.cash.movements.management.sale.invoice.enterprise.bussines.ruc"));
			this.purchaseInvoiceDAO.effectuatePurchaseInvoicePaymentCashReceiptDocumentDTO(cashReceiptDocumentDTO);
			return this.listPurchaseInvoiceDTO(new PurchaseInvoiceDTO(cashReceiptDocumentDTO.getId_purchase_invoice())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the productDepositMovementDAO
	 */
	public ProductDepositMovementDAO getProductDepositMovementDAO() {
		return productDepositMovementDAO;
	}

	/**
	 * @param productDepositMovementDAO the productDepositMovementDAO to set
	 */
	public void setProductDepositMovementDAO(
			ProductDepositMovementDAO productDepositMovementDAO) {
		this.productDepositMovementDAO = productDepositMovementDAO;
	}

	@Override
	public byte[] getSaleInvoiceDTOReport(SaleInvoiceDTO saleInvoiceDTO,HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			
			/*final WrappedHttpSession vWrappedHttpSession = (WrappedHttpSession)VaadinSession.getCurrent().getSession();
			final HttpSession vHttpSession = vWrappedHttpSession.getHttpSession();*/
			
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			final Map<String,String> reportParameters = 
					this.setUpSaleInvoiceTemplateHeader(
							saleInvoiceDTO,
							ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
							("report.template.cash.movements.management.sale.invoice."),
							"report.template.cash.movements.management.sale.invoice.");
			
			reportParameters.putAll(this.setUpSaleInvoiceTemplateFooter(
					saleInvoiceDTO,
					ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice."),
					"report.template.cash.movements.management.sale.invoice."));
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			this.setUpListSaleInvoiceItemDTOForReport(saleInvoiceDTO);
			vReportBuilder.addListObjectToReportDataSource(saleInvoiceDTO.getListSaleInvoiceItemDTO());
			/*final OutputStream out = 
					new FileOutputStream(
							"/home/testuser/docs/" 
					+ "my_file_" + (new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date()))
					+ ".pdf"
					);*/
			
			final byte[] var = vReportBuilder.getReportByteArray("sale_invoice_201611231040");
			/*out.write(var, 0, var.length);*/
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}		
	}
	
	private Map<String,String> setUpSaleInvoiceTemplateHeader(
			final SaleInvoiceDTO vSaleInvoiceDTO, 
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		Map<String,String> map = new HashMap<String,String>();
		map.put("enterpriseName", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.name"));
		map.put("enterpriseBussinesActivity", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.activity"));
		map.put("enterpriseBussinesOfficeAddress", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.address"));
		map.put("enterpriseBussinesOfficeTelepohoneNumber", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.telepohone.number"));
		map.put("ruc_label_value", messages.get("application.common.ruc.indicator.label"));
		map.put("enterpriseBussinesRuc", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc"));
		map.put("stampNumberLabelValue", messages.get(REPORT_MESSAGES_PREFIX + "stamping.number.label"));
		map.put("sale_invoice_stamp_number", "" + this.saleInvoiceDAO.getSaleInvoiceStampingNumber(vSaleInvoiceDTO));
		map.put("sale_invoice_stamping_effective_beginning_date_label_value", messages.get(REPORT_MESSAGES_PREFIX + "stamping.effective.beginning.date.label"));
		map.put("effective_beginning_date_formatted", 
				SgpUtils.parseDateParamBySessionLocale(
				this.saleInvoiceDAO.getSaleInvoiceStampingEffectiveBeginningDate(vSaleInvoiceDTO),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sale_invoice_effective_end_date_label_value", messages.get(REPORT_MESSAGES_PREFIX + "stamping.effective.end.date.label"));
		map.put("effective_end_date_formatted", SgpUtils.parseDateParamBySessionLocale(
				this.saleInvoiceDAO.getSaleInvoiceStampingEffectiveEndDate(vSaleInvoiceDTO),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("document_type", messages.get("application.common.document.type.sale.invoice"));
		map.put("identifier_number_label_value", messages.get("application.common.sale.invoice.number.indicator.label"));
		map.put("identifier_number", vSaleInvoiceDTO.getIdentifier_number());
		map.put("bussines_name_label_value", messages.get("application.common.customer.label"));
		map.put("bussines_name", vSaleInvoiceDTO.getBussines_ci_ruc() + " / " + vSaleInvoiceDTO.getBussines_name());
		map.put("payment_condition_label_value", messages.get("application.common.payment.condition.selector.description"));
		map.put("payment_condition", messages.get(vSaleInvoiceDTO.getPayment_condition()));
		map.put("emission_date_label_value", messages.get("application.common.emission.date.indicator.label"));
		map.put("emission_date_formatted", SgpUtils.parseDateParamBySessionLocale(
				vSaleInvoiceDTO.getEmission_date(),this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));		
		map.put("col_article_label_value", messages.get("application.common.table.column.article.label"));
		map.put("col_quantity_label_value", messages.get("application.common.quantity.label"));
		map.put("col_unit_price_amount_label", messages.get("application.common.table.column.unit.price.amount.label"));
		map.put("col_total_amount_label", messages.get("application.common.table.column.total.amount.label"));
		return map;
	}

	private Map<String,String> setUpSaleInvoiceTemplateFooter(
			final SaleInvoiceDTO vSaleInvoiceDTO, 
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		Map<String,String> map = new HashMap<String,String>();
		map.put("value_added_tax_10_amount_label_value", messages.get("application.common.text.field.total.value.added.tax.10"));
		map.put("value_added_tax_10_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getValue_added_tax_10_amount(),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_tax_5_amount_label_value", messages.get("application.common.text.field.total.value.added.tax.5"));
		map.put("value_added_tax_5_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getValue_added_tax_5_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("exempt_amount_label_value", messages.get("application.common.text.field.total.value.excempt.tax"));
		map.put("exempt_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getExempt_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_amount_label_value", messages.get("application.common.text.field.total.value.added.tax"));
		map.put("value_added_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getValue_added_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_10_amount_label_value", messages.get("application.common.text.field.value.added.tax.10"));
		map.put("tax_10_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getTax_10_amount(),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_5_amount_label_value", messages.get("application.common.text.field.value.added.tax.5"));
		map.put("tax_5_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getTax_5_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_tax_amount_label_value", messages.get("application.common.text.field.value.added.tax"));
		map.put("total_tax_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getTotal_tax_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_amount_label_value", messages.get("application.common.table.column.total.amount.label"));
		map.put("total_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceDTO.getTotal_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("currency_id_label_value", messages.get("application.common.currency.label"));
		map.put("currency_id", vSaleInvoiceDTO.getOrderDTO().getCurrencyDTO().getId_code() + " / " + vSaleInvoiceDTO.getOrderDTO().getCurrencyDTO().getDescription());
		return map;
	}
	
	public void setUpListSaleInvoiceItemDTOForReport(SaleInvoiceDTO vSaleInvoiceDTO){
		final Iterator<SaleInvoiceItemDTO> iSaleInvoiceItemDTO = vSaleInvoiceDTO.getListSaleInvoiceItemDTO().iterator();
		SaleInvoiceItemDTO vSaleInvoiceItemDTO;
		while(iSaleInvoiceItemDTO.hasNext()){
			vSaleInvoiceItemDTO = iSaleInvoiceItemDTO.next();
			if(vSaleInvoiceItemDTO.getStatus().equals("application.common.status.discarded"))
				iSaleInvoiceItemDTO.remove();
			else{
				vSaleInvoiceItemDTO.setQuantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(
								vSaleInvoiceItemDTO.getQuantity(), 
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vSaleInvoiceItemDTO.setUnit_price_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceItemDTO.getUnit_price_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vSaleInvoiceItemDTO.setValue_added_tax_10_unit_price_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vSaleInvoiceItemDTO.getValue_added_tax_10_unit_price_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			}//end if
		}//while(iSaleInvoiceItemDTO.hasNext()){
	}

	@Override
	public byte[] getCreditNoteDTOReport(CreditNoteDTO creditNoteDTO,
			SaleInvoiceDTO saleInvoiceDTO,HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			final Map<String,String> reportParameters = 
					this.setUpCreditNoteTemplateHeader(
							creditNoteDTO,
							saleInvoiceDTO,
							ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
							("report.template.cash.movements.management.sale.invoice."),
							"report.template.cash.movements.management.sale.invoice.");
			
			reportParameters.putAll(this.setUpCreditNoteTemplateFooter(
					creditNoteDTO,
					saleInvoiceDTO,
					ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice."),
					"report.template.cash.movements.management.sale.invoice."));
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			this.setUpListCreditNoteItemDTOForReport(creditNoteDTO);
			vReportBuilder.addListObjectToReportDataSource(creditNoteDTO.getListCreditNoteItemDTO());
			/*final OutputStream out = 
					new FileOutputStream(
							"/home/testuser/docs/" 
					+ "my_file_" + (new SimpleDateFormat("dd-MM-yyyy-HHmmss").format(new Date()))
					+ ".pdf"
					);*/
			
			final byte[] var = vReportBuilder.getReportByteArray("credit_note_201611261020");
			/*out.write(var, 0, var.length);*/
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	private Map<String,String> setUpCreditNoteTemplateHeader(
			final CreditNoteDTO vCreditNoteDTO,
			final SaleInvoiceDTO vSaleInvoiceDTO, 
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		Map<String,String> map = new HashMap<String,String>();
		map.put("enterpriseName", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.name"));
		map.put("enterpriseBussinesActivity", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.activity"));
		map.put("enterpriseBussinesOfficeAddress", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.address"));
		map.put("enterpriseBussinesOfficeTelepohoneNumber", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.telepohone.number"));
		map.put("ruc_label_value", messages.get("application.common.ruc.indicator.label"));
		map.put("enterpriseBussinesRuc", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc"));
		map.put("stampNumberLabelValue", messages.get(REPORT_MESSAGES_PREFIX + "stamping.number.label"));
		map.put("sale_invoice_stamp_number", "" +  this.saleInvoiceDAO.getSaleInvoiceStampingNumber(vSaleInvoiceDTO));
		map.put("sale_invoice_stamping_effective_beginning_date_label_value", messages.get(REPORT_MESSAGES_PREFIX + "stamping.effective.beginning.date.label"));
		map.put("effective_beginning_date_formatted", 
				SgpUtils.parseDateParamBySessionLocale(
				this.saleInvoiceDAO.getSaleInvoiceStampingEffectiveBeginningDate(vSaleInvoiceDTO),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sale_invoice_effective_end_date_label_value", messages.get(REPORT_MESSAGES_PREFIX + "stamping.effective.end.date.label"));
		map.put("effective_end_date_formatted", SgpUtils.parseDateParamBySessionLocale(
				this.saleInvoiceDAO.getSaleInvoiceStampingEffectiveEndDate(vSaleInvoiceDTO),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("document_type", messages.get("application.common.document.type.credit.note"));
		map.put("identifier_number_label_value", 
				messages.get("application.common.number.indicator.label")
				+" " + messages.get("application.common.document.type.credit.note").toLowerCase());
		map.put("identifier_number", vCreditNoteDTO.getIdentifier_number());
		map.put("bussines_name_label_value", messages.get("application.common.customer.label"));
		map.put("bussines_name", vCreditNoteDTO.getBussines_ci_ruc() + " / " + vCreditNoteDTO.getBussines_name());
		map.put("emission_date_label_value", messages.get("application.common.emission.date.indicator.label"));
		map.put("emission_date_formatted", SgpUtils.parseDateParamBySessionLocale(
				vCreditNoteDTO.getEmission_date(),this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("affected_sale_invoice_label", messages.get("application.common.sale.invoice.number.indicator.label"));
		map.put("affected_sale_invoice", vSaleInvoiceDTO.getIdentifier_number());
		map.put("col_article_label_value", messages.get("application.common.table.column.article.label"));
		map.put("col_quantity_label_value", messages.get("application.common.quantity.label"));
		map.put("col_unit_price_amount_label", messages.get("application.common.table.column.unit.price.amount.label"));
		map.put("col_total_amount_label", messages.get("application.common.table.column.total.amount.label"));
		return map;
	}

	private Map<String,String> setUpCreditNoteTemplateFooter(
			final CreditNoteDTO vCreditNoteDTO,
			final SaleInvoiceDTO vSaleInvoiceDTO, 
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		Map<String,String> map = new HashMap<String,String>();
		map.put("value_added_tax_10_amount_label_value", messages.get("application.common.text.field.total.value.added.tax.10"));
		map.put("value_added_tax_10_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getValue_added_tax_10_amount(),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_tax_5_amount_label_value", messages.get("application.common.text.field.total.value.added.tax.5"));
		map.put("value_added_tax_5_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getValue_added_tax_5_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("exempt_amount_label_value", messages.get("application.common.text.field.total.value.excempt.tax"));
		map.put("exempt_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getExempt_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_amount_label_value", messages.get("application.common.text.field.total.value.added.tax"));
		map.put("value_added_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getValue_added_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_10_amount_label_value", messages.get("application.common.text.field.value.added.tax.10"));
		map.put("tax_10_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getTax_10_amount(),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_5_amount_label_value", messages.get("application.common.text.field.value.added.tax.5"));
		map.put("tax_5_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getTax_5_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_tax_amount_label_value", messages.get("application.common.text.field.value.added.tax"));
		map.put("total_tax_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getTotal_tax_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_amount_label_value", messages.get("application.common.table.column.total.amount.label"));
		map.put("total_amount", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteDTO.getTotal_amount(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("currency_id_label_value", messages.get("application.common.currency.label"));
		map.put("currency_id", vSaleInvoiceDTO.getOrderDTO().getCurrencyDTO().getId_code() + " / " + vSaleInvoiceDTO.getOrderDTO().getCurrencyDTO().getDescription());
		return map;
	}
	
	public void setUpListCreditNoteItemDTOForReport(CreditNoteDTO vCreditNoteDTO){
		final Iterator<CreditNoteItemDTO> iCreditNoteItemDTO = vCreditNoteDTO.getListCreditNoteItemDTO().iterator();
		CreditNoteItemDTO vCreditNoteItemDTO;
		while(iCreditNoteItemDTO.hasNext()){
			vCreditNoteItemDTO = iCreditNoteItemDTO.next();
			vCreditNoteItemDTO.setQuantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(
								vCreditNoteItemDTO.getCancellation_withdrawal_quantity() + vCreditNoteItemDTO.getDevolution_quantity(), 
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			vCreditNoteItemDTO.setUnit_price_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteItemDTO.getUnit_price_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			vCreditNoteItemDTO.setValue_added_tax_10_unit_price_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vCreditNoteItemDTO.getValue_added_tax_10_unit_price_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));			
		}//while(iCreditNoteItemDTO.hasNext()){
	}

	/**
	 * @return the cashMovementsReportDAO
	 */
	public CashMovementsReportDAO getCashMovementsReportDAO() {
		return cashMovementsReportDAO;
	}

	/**
	 * @param cashMovementsReportDAO the cashMovementsReportDAO to set
	 */
	public void setCashMovementsReportDAO(
			CashMovementsReportDAO cashMovementsReportDAO) {
		this.cashMovementsReportDAO = cashMovementsReportDAO;
	}

	@Override
	public byte[] getIncomeExpeditureReport(
			IncomeExpeditureReportDTO incomeExpeditureReportDTO,
			HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<IncomeExpeditureReportDTO> listIncomeExpeditureReportDTO = this.cashMovementsReportDAO.incomeExpeditureReport(incomeExpeditureReportDTO);
			BigDecimal total_amount_result = BigDecimal.ZERO;
			BigDecimal value_added_tax_10_result = BigDecimal.ZERO;
			BigDecimal value_added_tax_5_result = BigDecimal.ZERO;
			BigDecimal value_added_amount_result = BigDecimal.ZERO;
			BigDecimal exempt_amount_result = BigDecimal.ZERO;
			BigDecimal tax_10_amount_result = BigDecimal.ZERO;
			BigDecimal tax_5_amount_result = BigDecimal.ZERO;
			BigDecimal total_tax_amount_result = BigDecimal.ZERO;
			
			final Map<String,String> messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.income.expediture.");
			for(IncomeExpeditureReportDTO vIncomeExpeditureReportDTO : listIncomeExpeditureReportDTO){
				total_amount_result = total_amount_result.add(vIncomeExpeditureReportDTO.getTotal_amount());
				value_added_tax_10_result = value_added_tax_10_result.add(vIncomeExpeditureReportDTO.getValue_added_tax_10_amount());
				value_added_tax_5_result = value_added_tax_5_result.add(vIncomeExpeditureReportDTO.getValue_added_tax_5_amount());
				value_added_amount_result = value_added_amount_result.add(vIncomeExpeditureReportDTO.getValue_added_amount());
				exempt_amount_result = exempt_amount_result.add(vIncomeExpeditureReportDTO.getExempt_amount());
				tax_10_amount_result = tax_10_amount_result.add(vIncomeExpeditureReportDTO.getTax_10_amount());
				tax_5_amount_result = tax_5_amount_result.add(vIncomeExpeditureReportDTO.getTax_5_amount());
				total_tax_amount_result = total_tax_amount_result.add(vIncomeExpeditureReportDTO.getTotal_tax_amount());
				
				vIncomeExpeditureReportDTO.setOperational_concept_message(messages.get(vIncomeExpeditureReportDTO.getOperational_concept()));
				vIncomeExpeditureReportDTO.setTotal_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getTotal_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setValue_added_tax_10_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getValue_added_tax_10_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setValue_added_tax_5_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getValue_added_tax_5_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setValue_added_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getValue_added_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setExempt_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getExempt_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setTax_10_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getTax_10_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setTax_5_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getTax_5_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vIncomeExpeditureReportDTO.setTotal_tax_amount_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vIncomeExpeditureReportDTO.getTotal_tax_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			}//for(IncomeExpeditureReportDTO vIncomeExpeditureReportDTO : listIncomeExpeditureReportDTO){
			
			
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			final Map<String,String> reportParameters = 
					this.setUpIncomeExpeditureReportTemplateHeader(
							incomeExpeditureReportDTO,
							messages,
							"report.template.cash.movements.management.income.expediture.");
			
			reportParameters.putAll(this.setUpIncomeExpeditureReportTemplateFooter(
					total_amount_result,
					value_added_tax_10_result,
					value_added_tax_5_result,
					value_added_amount_result,
					exempt_amount_result,
					tax_10_amount_result,
					tax_5_amount_result,
					total_tax_amount_result,
					ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice."),
					"report.template.cash.movements.management.sale.invoice."));
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			vReportBuilder.addListObjectToReportDataSource(listIncomeExpeditureReportDTO);
			final byte[] var = vReportBuilder.getReportByteArray("income_expediture_report_201611281307");
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private Map<String,String> setUpIncomeExpeditureReportTemplateHeader(
			final IncomeExpeditureReportDTO incomeExpeditureReportDTO,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("main_title_label", messages.get(REPORT_MESSAGES_PREFIX + "main.title"));
		map.put("report_emission_date_label", messages.get("application.common.emission.date.indicator.label"));
		map.put("report_emission_date", SgpUtils.parseDateParamBySessionLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("currency_label", messages.get("application.common.currency.label"));
		map.put("currency", incomeExpeditureReportDTO.getCurrencyDTO().getId_code() + " / " + incomeExpeditureReportDTO.getCurrencyDTO().getDescription());
		map.put("begin_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.begin.date"));
		map.put("begin_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(incomeExpeditureReportDTO.getBeginDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("end_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.end.date"));
		map.put("end_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(incomeExpeditureReportDTO.getEndDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("operational_concept_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.operational.concept"));
		map.put("total_amount_label", messages.get("application.common.table.column.total.amount.label"));
		map.put("value_added_tax_10_amount_label", messages.get("application.common.text.field.total.value.added.tax.10"));
		map.put("value_added_tax_5_amount_label", messages.get("application.common.text.field.total.value.added.tax.5"));
		map.put("value_added_amount_label", messages.get("application.common.text.field.total.value.added.tax"));
		map.put("exempt_amount_label", messages.get("application.common.text.field.total.value.excempt.tax"));
		map.put("tax_10_amount_label", messages.get("application.common.text.field.value.added.tax.10"));
		map.put("tax_5_amount_label", messages.get("application.common.text.field.value.added.tax.5"));
		map.put("total_tax_amount_label", messages.get("application.common.text.field.value.added.tax"));
		return map;		
	}
	
	private Map<String,String> setUpIncomeExpeditureReportTemplateFooter(
			final BigDecimal total_amount_result,
			final BigDecimal value_added_tax_10_result,
			final BigDecimal value_added_tax_5_result,
			final BigDecimal value_added_amount_result,
			final BigDecimal exempt_amount_result,
			final BigDecimal tax_10_amount_result,
			final BigDecimal tax_5_amount_result,
			final BigDecimal total_tax_amount_result,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("total_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_tax_10_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(value_added_tax_10_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_tax_5_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(value_added_tax_5_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("value_added_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(value_added_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("exempt_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(exempt_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_10_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(tax_10_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("tax_5_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(tax_5_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_tax_amount_result_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_tax_amount_result,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		return map;
	}

	@Override
	public byte[] getCashReceiptDocumentDTOReport(
			CashReceiptDocumentDTO cashReceiptDocumentDTO,
			HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			final Map<String,String> reportParameters = 
					this.setUpCashReceiptDocumentTemplateHeader(
							ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
							("report.template.cash.movements.management.sale.invoice."),
							"report.template.cash.movements.management.sale.invoice.");
			
			reportParameters.putAll(this.setUpCashReceiptDocumentTemplateBody(
					ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.cash.receipt.document."),
					"report.template.cash.movements.management.cash.receipt.document.",
					cashReceiptDocumentDTO));
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			vReportBuilder.addSingleObjectToReportDataSource(cashReceiptDocumentDTO);
			final byte[] var = vReportBuilder.getReportByteArray("cash_receipt_document_201611292115");
			/*out.write(var, 0, var.length);*/
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private Map<String,String> setUpCashReceiptDocumentTemplateHeader(
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		Map<String,String> map = new HashMap<String,String>();
		map.put("enterpriseName", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.name"));
		map.put("enterpriseBussinesActivity", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.activity"));
		map.put("enterpriseBussinesOfficeAddress", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.address"));
		map.put("enterpriseBussinesOfficeTelepohoneNumber", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.office.telepohone.number"));
		map.put("ruc_label_value", messages.get("application.common.ruc.indicator.label"));
		map.put("enterpriseBussinesRuc", messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc"));
		map.put("document_type", messages.get("application.common.document.type.cash.receipt.document"));
		return map;
	}
	
	private Map<String,String> setUpCashReceiptDocumentTemplateBody(
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX,
			final CashReceiptDocumentDTO cashReceiptDocumentDTO){
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("identifier_number_label_value", 
				messages.get("application.common.number.indicator.label"));		
		map.put("identifier_number", cashReceiptDocumentDTO.getIdentifier_number());
		map.put("currency_id_label_value", messages.get("application.common.currency.label"));
		map.put("currency_id", cashReceiptDocumentDTO.getCurrencyDTO().getId_code() + " / " + cashReceiptDocumentDTO.getCurrencyDTO().getDescription());
		map.put("received_from_label", messages.get(REPORT_MESSAGES_PREFIX + "label.received.from"));
		map.put("bussines_name", 
				cashReceiptDocumentDTO.getBussines_name()
				+ " / " + messages.get("application.common.ruc.indicator.label") 
				+ " " + cashReceiptDocumentDTO.getBussines_ci_ruc());
		map.put("total_amount_label_value", messages.get(REPORT_MESSAGES_PREFIX + "label.total.amount"));
		map.put("total_amount", 
				cashReceiptDocumentDTO.getCurrencyDTO().getId_code() + " " +
				this.sgpUtils.formatBigDecimalValueNumberByLocale(cashReceiptDocumentDTO.getAmount().setScale(0, RoundingMode.DOWN),
				this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())
				+" / " + this.sgpUtils.spellOutBigDecimalNumber(cashReceiptDocumentDTO.getAmount().setScale(0, RoundingMode.DOWN), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("cash_receipt_concept_label", messages.get(REPORT_MESSAGES_PREFIX + "label.cash.receipt.concept"));
		map.put("cash_receipt_concept", 
				messages.get(REPORT_MESSAGES_PREFIX + "cash.receipt.concept")
				+ " " + messages.get("application.common.number.indicator.label") 
				+ " " 
				+ cashReceiptDocumentDTO.getIdentifier_number().substring(
						0, 
						cashReceiptDocumentDTO.getIdentifier_number().length() - 2 ));
		map.put("place_and_date_label", messages.get(REPORT_MESSAGES_PREFIX + "label.place.and.date"));
		map.put("place_and_date", SgpUtils.formatDateWithDayAndMonthByLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sign_label", messages.get(REPORT_MESSAGES_PREFIX + "label.sign"));
		
		return map;
	}

	@Override
	public byte[] getPurchasesFormObligationTaxReport(Date monthYear)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			/* EXISTE FUNCION QUE TRAE LOS TOTALES. UTILIZADA EN EL REPORTE DE INGRESOS Y EGRESOS */
			
			/*final IncomeExpeditureReportDTO vIncomeExpeditureReportDTO = this.cashMovementsReportDAO.purchasesExpeditureReport(
					new IncomeExpeditureReportDTO(
							SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
							SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
							this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0).getId(), 
							null));*/
			
			final List<PurchaseInvoiceDTO> listPurchaseInvoiceDTO = 
					this.purchaseInvoiceDAO.listPurchaseInvoiceDTO(
							new PurchaseInvoiceDTO(
									SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),
									SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),
									true));
			
			BigDecimal acumulatedTotalAmount = BigDecimal.ZERO;
			final CurrencyDTO vLocalCurrencyDTO = this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0);
			BigDecimal vConvertedCurrencyAmount = null;
			for(PurchaseInvoiceDTO vPurchaseInvoiceDTO : listPurchaseInvoiceDTO){
				/* all values must be in local currency */
				if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
					vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
							new CurrencyDTO(
									vPurchaseInvoiceDTO.getId_currency(), 
									vLocalCurrencyDTO.getId(), 
									(vPurchaseInvoiceDTO.getValue_added_tax_10_amount())));
					acumulatedTotalAmount = acumulatedTotalAmount.add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
				}else{//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){				
					acumulatedTotalAmount = acumulatedTotalAmount.add
					(vPurchaseInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
				}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			}//for(PurchaseInvoiceDTO vPurchaseInvoiceDTO : listPurchaseInvoiceDTO){
			
			
			/* issued credit notes */
			final List<CreditNoteDTO> listCreditNoteDTO = this.creditNoteDAO.listCreditNoteDTO(
					new CreditNoteDTO(
							SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
							SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())));
			for(CreditNoteDTO vCreditNoteDTO : listCreditNoteDTO){
				/* all values must be in local currency */
				if(!vCreditNoteDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
					vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
							new CurrencyDTO(
									vCreditNoteDTO.getId_currency(), 
									vLocalCurrencyDTO.getId(), 
									(vCreditNoteDTO.getValue_added_tax_10_amount())));
					acumulatedTotalAmount = acumulatedTotalAmount.add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
				}else{//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){				
					acumulatedTotalAmount = acumulatedTotalAmount.add
					(vCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
				}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){				
			}//for(CreditNoteDTO vCreditNoteDTO : listCreditNoteDTO){
			
			
			this.textContentFileBuilder = new TextContentFileBuilder();
			final int detailsRowQuantity = 
					(listPurchaseInvoiceDTO != null ? listPurchaseInvoiceDTO.size() : 0) 
					+ (listCreditNoteDTO !=null ? listCreditNoteDTO.size() : 0);
			final Map<String,String> messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice.");
			messages.putAll(ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					(COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX));
			this.setPurchasesFormObligationTaxReportMasterRow(
					acumulatedTotalAmount, 
					monthYear,
					messages,
					"report.template.cash.movements.management.sale.invoice.",
					detailsRowQuantity);			

			
			for(PurchaseInvoiceDTO vPurchaseInvoiceDTO : listPurchaseInvoiceDTO){
				this.setPurchasesFormObligationTaxReportDetailRowByPurchaseInvoiceDTO(
						vPurchaseInvoiceDTO,
						vLocalCurrencyDTO,
						vConvertedCurrencyAmount);
			}//for(PurchaseInvoiceDTO vPurchaseInvoiceDTO : listPurchaseInvoiceDTO){
			for(CreditNoteDTO vCreditNoteDTO : listCreditNoteDTO){
				this.setPurchasesFormObligationTaxReportDetailRowByCreditNoteDTO(
						vCreditNoteDTO, 
						vLocalCurrencyDTO, 
						vConvertedCurrencyAmount);
			}//for(CreditNoteDTO vCreditNoteDTO : listCreditNoteDTO){
			logger.info( "\n ======================================================================== "
						+"\n" + this.textContentFileBuilder.getTextFileContent()
						+"\n ======================================================================== ");
			return this.textContentFileBuilder.getByteArrayData();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private void setPurchasesFormObligationTaxReportMasterRow(
			/*final IncomeExpeditureReportDTO vIncomeExpeditureReportDTO,*/
			final BigDecimal acumulatedTotalAmount,
			final Date monthYear,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX,
			final int detailRowsCount){
		//header indicator
		this.textContentFileBuilder.insertValue("1");
		this.textContentFileBuilder.insertTabCharacter();
		//month excercise
		this.textContentFileBuilder.insertValue(SgpUtils.formatDateYearMonthByLocale(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("1");
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("911");
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("211");
		this.textContentFileBuilder.insertTabCharacter();
		//informer ruc with out verifier digit
		this.textContentFileBuilder.insertValue(
				messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").
				substring(0, messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").length() - 2));
		this.textContentFileBuilder.insertTabCharacter();
		//informer ruc verifier digit
		this.textContentFileBuilder.insertValue(
				messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").
				substring(messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").length() - 1));
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(messages.get(REPORT_MESSAGES_PREFIX + "enterprise.name"));
		this.textContentFileBuilder.insertTabCharacter();
		//physical person enterprise representant ruc with out verifier digit 
		this.textContentFileBuilder.insertValue(
				this.determinateRucFromProperty_Bussines_ci_ruc(
				messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.ruc"))
				);
		this.textContentFileBuilder.insertTabCharacter();
		//physical person enterprise representant ruc verifier digit
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(
				messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.ruc")));
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.name"));
		//details rows count
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + detailRowsCount);
		//total amount
		this.textContentFileBuilder.insertTabCharacter();
		//this.textContentFileBuilder.insertValue("" + vIncomeExpeditureReportDTO.getTotal_amount().setScale(0, RoundingMode.HALF_EVEN));
		this.textContentFileBuilder.insertValue("" + acumulatedTotalAmount);		
		//constant
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("NO");
		//constant
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("2");
	}
	
	private void setPurchasesFormObligationTaxReportDetailRowByPurchaseInvoiceDTO(
			final PurchaseInvoiceDTO vPurchaseInvoiceDTO,
			final CurrencyDTO vLocalCurrencyDTO,
			BigDecimal vConvertedCurrencyAmount) throws PmsServiceException{
		this.textContentFileBuilder.insertNewLineCharacter();
		//detail row indicator
		this.textContentFileBuilder.insertValue("2");
		//ruc with out verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucFromProperty_Bussines_ci_ruc(vPurchaseInvoiceDTO.getBussines_ci_ruc()));
		//ruc verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(vPurchaseInvoiceDTO.getBussines_ci_ruc()));
		//enterprise bussines name
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vPurchaseInvoiceDTO.getBussines_name());
		//stamp number
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + vPurchaseInvoiceDTO.getStamping_number());
		//document type: 1 = invoice
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("1");
		//document nbr
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vPurchaseInvoiceDTO.getIdentifier_number());		
		//document emission date
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				SgpUtils.parseDateParamWithOutHourBySessionLocale(vPurchaseInvoiceDTO.getEmission_date(), 
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		/* all values must be in local currency */
		if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vPurchaseInvoiceDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vPurchaseInvoiceDTO.getValue_added_tax_10_amount())));
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));
		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vPurchaseInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vPurchaseInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));			
		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){		

		//value added 5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//excempt amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//constant
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("0");
		//document payment condition, 1 = cash, 2 = credit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vPurchaseInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.cash") ? "1" : "2");
		//credit payment condition fee quantity
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				vPurchaseInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.credit") ? 
				"" + vPurchaseInvoiceDTO.getCredit_purchase_fee_quantity() : "0");
		
	}
	private void setPurchasesFormObligationTaxReportDetailRowByCreditNoteDTO(
			final CreditNoteDTO vCreditNoteDTO,
			final CurrencyDTO vLocalCurrencyDTO,
			BigDecimal vConvertedCurrencyAmount) throws PmsServiceException{
		this.textContentFileBuilder.insertNewLineCharacter();
		//detail row indicator
		this.textContentFileBuilder.insertValue("2");
		//ruc with out verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucFromProperty_Bussines_ci_ruc(vCreditNoteDTO.getBussines_ci_ruc()));
		//ruc verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(vCreditNoteDTO.getBussines_ci_ruc()));
		//enterprise bussines name
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vCreditNoteDTO.getBussines_name());
		//stamp number
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + this.creditNoteDAO.getCreditNoteStampingNumber(vCreditNoteDTO));
		//document type: 3 = credit note
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("3");
		//document nbr
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vCreditNoteDTO.getIdentifier_number());		
		//document emission date
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				SgpUtils.parseDateParamWithOutHourBySessionLocale(vCreditNoteDTO.getEmission_date(), 
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		/* all values must be in local currency */
		if(!vCreditNoteDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vCreditNoteDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vCreditNoteDTO.getValue_added_tax_10_amount())));
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));
		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));			
		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){		

		//value added 5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//excempt amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//constant
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("0");
		//document payment condition, 1 = cash, 2 = credit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("1");
		//credit payment condition fee quantity
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue( "0");
		
	}
	
	
	private String determinateRucFromProperty_Bussines_ci_ruc(final String bussines_ci_ruc){
		if(!bussines_ci_ruc.contains("-"))return bussines_ci_ruc;
		else return bussines_ci_ruc.substring(0,bussines_ci_ruc.length() - 2);
	}
	
	private String determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(final String bussines_ci_ruc){
		if(!bussines_ci_ruc.contains("-"))return "" + VerifierDigitChecker.getDigitoVerificador(bussines_ci_ruc);
		else return bussines_ci_ruc.substring(bussines_ci_ruc.length() - 1);
	}
	
	@Override
	public byte[] getSalesFormObligationTaxReport(Date monthYear)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			/*final IncomeExpeditureReportDTO vIncomeExpeditureReportDTO = this.cashMovementsReportDAO.salesIncomeReport(
					new IncomeExpeditureReportDTO(
							SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
							SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()), 
							this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0).getId(), 
							null));*/
			
			final List<SaleInvoiceDTO> listSaleInvoiceDTO = this.listSaleInvoiceDTO(
					new SaleInvoiceDTO(
							SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),
							SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),
							true));		
			
			this.textContentFileBuilder = new TextContentFileBuilder();
			BigDecimal acumulatedSumAmountReported = BigDecimal.ZERO;
			final CurrencyDTO vLocalCurrencyDTO = this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(true)).get(0);
			BigDecimal vConvertedCurrencyAmount = null;
			for(SaleInvoiceDTO vSaleInvoiceDTO : listSaleInvoiceDTO){
				/* all values must be in local currency */
				if(!vSaleInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
					vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
							new CurrencyDTO(
									vSaleInvoiceDTO.getId_currency(), 
									vLocalCurrencyDTO.getId(), 
									(vSaleInvoiceDTO.getValue_added_tax_10_amount())));
					acumulatedSumAmountReported = acumulatedSumAmountReported.add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN))
							);
				}else{//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){				
					acumulatedSumAmountReported = acumulatedSumAmountReported.add
					(vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).add
					(vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN))
					);
				}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			}//for(SaleInvoiceDTO vSaleInvoiceDTO : listSaleInvoiceDTO){
			
			//received credit notes
			final List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO = this.listPurchaseInvoiceCreditNoteDTO
					(new PurchaseInvoiceCreditNoteDTO(SgpUtils.firstDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()),
							SgpUtils.lastDateOfMonth(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale())));
			for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
				/* all values must be in local currency */
				if(!vPurchaseInvoiceCreditNoteDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
					vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
							new CurrencyDTO(
									vPurchaseInvoiceCreditNoteDTO.getId_currency(), 
									vLocalCurrencyDTO.getId(), 
									(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount())));
					acumulatedSumAmountReported = acumulatedSumAmountReported.add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).add
							(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN))
							);
				}else{//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){				
					acumulatedSumAmountReported = acumulatedSumAmountReported.add
					(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).add
					(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN))
					);
				}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			}//for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
			
			
			
			final Map<String,String> messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.sale.invoice.");
			messages.putAll(ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					(COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX));
			this.setSalesFormObligationTaxReportMasterRow(
					acumulatedSumAmountReported, 
					monthYear,
					messages,
					"report.template.cash.movements.management.sale.invoice.",
					listSaleInvoiceDTO.size() + listPurchaseInvoiceCreditNoteDTO.size());
			
			for(SaleInvoiceDTO vSaleInvoiceDTO : listSaleInvoiceDTO){
				this.setSalesFormObligationTaxReportDetailRow(
						vSaleInvoiceDTO,
						vLocalCurrencyDTO,
						vConvertedCurrencyAmount);
			}
			
			for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
				this.setSalesFormObligationTaxReportDetailRow(
						vPurchaseInvoiceCreditNoteDTO,
						vLocalCurrencyDTO, 
						vConvertedCurrencyAmount);
			}//for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
			
			logger.info( "\n ======================================================================== "
					+"\n" + this.textContentFileBuilder.getTextFileContent()
					+"\n ======================================================================== ");
		return this.textContentFileBuilder.getByteArrayData();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private void setSalesFormObligationTaxReportMasterRow(
			/*final IncomeExpeditureReportDTO vIncomeExpeditureReportDTO, */
			final BigDecimal acumulatedSumAmountReported,
			final Date monthYear,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX,
			final int detailRowsCount){
		//header indicator
		this.textContentFileBuilder.insertValue("1");
		this.textContentFileBuilder.insertTabCharacter();
		//month excercise
		this.textContentFileBuilder.insertValue(SgpUtils.formatDateYearMonthByLocale(monthYear, this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("1");
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("921");
		this.textContentFileBuilder.insertTabCharacter();
		//constant
		this.textContentFileBuilder.insertValue("221");
		this.textContentFileBuilder.insertTabCharacter();
		//informer ruc with out verifier digit
		this.textContentFileBuilder.insertValue(
				messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").
				substring(0, messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").length() - 2));
		this.textContentFileBuilder.insertTabCharacter();
		//informer ruc verifier digit
		this.textContentFileBuilder.insertValue(
				messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").
				substring(messages.get(REPORT_MESSAGES_PREFIX + "enterprise.bussines.ruc").length() - 1));
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(messages.get(REPORT_MESSAGES_PREFIX + "enterprise.name"));
		this.textContentFileBuilder.insertTabCharacter();
		//physical person enterprise representant ruc with out verifier digit 
		this.textContentFileBuilder.insertValue(
				this.determinateRucFromProperty_Bussines_ci_ruc(
				messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.ruc"))
				);
		this.textContentFileBuilder.insertTabCharacter();
		//physical person enterprise representant ruc verifier digit
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(
				messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.ruc")));
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(messages.get(this.COMMON_FORM_OBLIGATION_TAX_REPORT_PREFIX + "pms.system.implementing.company.physical.person.enterprise.representant.name"));
		//details rows count
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + detailRowsCount);
		//total amount
		this.textContentFileBuilder.insertTabCharacter();
		/*this.textContentFileBuilder.insertValue("" + //THE ADD OPERATION IS DONE WITH ROUNDED NUMBERS
				vIncomeExpeditureReportDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.HALF_EVEN).
		add(vIncomeExpeditureReportDTO.getValue_added_tax_10_amount().multiply(BigDecimal.valueOf(0.1).setScale(0, RoundingMode.HALF_EVEN))));*/
		this.textContentFileBuilder.insertValue("" + acumulatedSumAmountReported);
		//constant
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("2");
	}
	
	private void setSalesFormObligationTaxReportDetailRow(
			final SaleInvoiceDTO vSaleInvoiceDTO,
			final CurrencyDTO vLocalCurrencyDTO,
			BigDecimal vConvertedCurrencyAmount) throws PmsServiceException{
		this.textContentFileBuilder.insertNewLineCharacter();
		//detail row indicator
		this.textContentFileBuilder.insertValue("2");
		//customer ruc
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucFromProperty_Bussines_ci_ruc(vSaleInvoiceDTO.getBussines_ci_ruc()));
		//ruc verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(vSaleInvoiceDTO.getBussines_ci_ruc()));
			
		//enterprise bussines name
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vSaleInvoiceDTO.getBussines_name());
		//document type: 1 = invoice
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("1");
		//document nbr
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vSaleInvoiceDTO.getIdentifier_number());
		//document emission date
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				SgpUtils.parseDateParamWithOutHourBySessionLocale(vSaleInvoiceDTO.getEmission_date(), 
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		/* all values must be in local currency */
		if(!vSaleInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vSaleInvoiceDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vSaleInvoiceDTO.getValue_added_tax_10_amount())));
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));

		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));

		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		
		//value added 5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//excempt amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		/* all values must be in local currency */
		if(!vSaleInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vSaleInvoiceDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vSaleInvoiceDTO.getValue_added_tax_10_amount())));		
			//total amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + //THE ADD OPERATION IS DONE WITH ROUNDED NUMBERS
					vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).add
					(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN)));
		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{			
			//total amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + //THE ADD OPERATION IS DONE WITH ROUNDED NUMBERS
					vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).add
					(vSaleInvoiceDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN)));
		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		//document payment condition, 1 = cash, 2 = credit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vSaleInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.cash") ? "1" : "2");
		//credit payment condition fee quantity
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				vSaleInvoiceDTO.getPayment_condition().equals("application.common.payment.condition.credit") ? 
				"" + vSaleInvoiceDTO.getOrderDTO().getCredit_order_fee_quantity() : "0");
		//stamp number
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" +  this.saleInvoiceDAO.getSaleInvoiceStampingNumber(vSaleInvoiceDTO));
		
	}

	private void setSalesFormObligationTaxReportDetailRow(
			final PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO,
			final CurrencyDTO vLocalCurrencyDTO,
			BigDecimal vConvertedCurrencyAmount) throws PmsServiceException{
		this.textContentFileBuilder.insertNewLineCharacter();
		//detail row indicator
		this.textContentFileBuilder.insertValue("2");
		//customer ruc
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucFromProperty_Bussines_ci_ruc(vPurchaseInvoiceCreditNoteDTO.getBussines_ci_ruc()));
		//ruc verifier digit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(this.determinateRucVerifierDigitFromProperty_Bussines_ci_ruc(vPurchaseInvoiceCreditNoteDTO.getBussines_ci_ruc()));
			
		//enterprise bussines name
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vPurchaseInvoiceCreditNoteDTO.getBussines_name());
		//document type: 3 = credit note
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("3");
		//document nbr
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(vPurchaseInvoiceCreditNoteDTO.getIdentifier_number());
		//document emission date
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue(
				SgpUtils.parseDateParamWithOutHourBySessionLocale(vPurchaseInvoiceCreditNoteDTO.getEmission_date(), 
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		/* all values must be in local currency */
		if(!vPurchaseInvoiceCreditNoteDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vPurchaseInvoiceCreditNoteDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount())));
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));

		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{
			//value added 10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN));
			//10% tax amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN));

		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		
		//value added 5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//5% tax amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		//excempt amount
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" + BigDecimal.ZERO);
		/* all values must be in local currency */
		if(!vPurchaseInvoiceCreditNoteDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
			vConvertedCurrencyAmount = this.comercialManagementService.convertCurrencyAmount(
					new CurrencyDTO(
							vPurchaseInvoiceCreditNoteDTO.getId_currency(), 
							vLocalCurrencyDTO.getId(), 
							(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount())));		
			//total amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + //THE ADD OPERATION IS DONE WITH ROUNDED NUMBERS
					vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).add
					(vConvertedCurrencyAmount.setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN)));
		}//if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		else{			
			//total amount
			this.textContentFileBuilder.insertTabCharacter();
			this.textContentFileBuilder.insertValue("" + //THE ADD OPERATION IS DONE WITH ROUNDED NUMBERS
					vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).add
					(vPurchaseInvoiceCreditNoteDTO.getValue_added_tax_10_amount().setScale(0, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.1)).setScale(0, RoundingMode.DOWN)));
		}////if(!vPurchaseInvoiceDTO.getId_currency().equals(vLocalCurrencyDTO.getId())){
		//document payment condition, 1 = cash, 2 = credit
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue( "1" );
		//credit payment condition fee quantity
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("0");
		//stamp number
		this.textContentFileBuilder.insertTabCharacter();
		this.textContentFileBuilder.insertValue("" +  vPurchaseInvoiceCreditNoteDTO.getStamping_number());
		
	}
	
	@Override
	public byte[] getProductCostSaleComparisonReport(
			ProductCostSaleComparisonDTO productCostSaleComparisonDTO,
			HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final List<ProductCostSaleComparisonDTO> listProductCostSaleComparisonDTO = 
					this.cashMovementsReportDAO.productCostSaleComparisonReport(productCostSaleComparisonDTO);
			
			Long sum_sale_invoice_item_quantity = 0L;
			BigDecimal sum_sale_price_acumulated = BigDecimal.ZERO;
			BigDecimal sum_production_cost_acumulated = BigDecimal.ZERO;
			BigDecimal sum_profit = BigDecimal.ZERO;
			
			for(ProductCostSaleComparisonDTO vProductCostSaleComparisonDTO : listProductCostSaleComparisonDTO){
				sum_sale_invoice_item_quantity = Long.sum(sum_sale_invoice_item_quantity, vProductCostSaleComparisonDTO.getSale_invoice_item_quantity());
				sum_sale_price_acumulated = sum_sale_price_acumulated.add(vProductCostSaleComparisonDTO.getSale_price_acumulated());
				sum_production_cost_acumulated = sum_production_cost_acumulated.add(vProductCostSaleComparisonDTO.getProduction_cost_acumulated());
				sum_profit = sum_profit.add(vProductCostSaleComparisonDTO.getProfit());
				
				vProductCostSaleComparisonDTO.setSale_invoice_item_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vProductCostSaleComparisonDTO.getSale_invoice_item_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vProductCostSaleComparisonDTO.setSale_price_acumulated_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vProductCostSaleComparisonDTO.getSale_price_acumulated(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vProductCostSaleComparisonDTO.setProduction_cost_acumulated_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vProductCostSaleComparisonDTO.getProduction_cost_acumulated(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vProductCostSaleComparisonDTO.setProfit_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vProductCostSaleComparisonDTO.getProfit(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vProductCostSaleComparisonDTO.setSale_date_formatted(SgpUtils.parseDateParamBySessionLocale(vProductCostSaleComparisonDTO.getSale_date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vProductCostSaleComparisonDTO.setIncrease_over_cost_for_sale_price_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vProductCostSaleComparisonDTO.getProduct_increase_over_cost_for_sale_price(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				
			}//for(ProductCostSaleComparisonDTO vProductCostSaleComparisonDTO : listProductCostSaleComparisonDTO){
			
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			final Map<String,String> reportParameters = 
					this.setUpProductCostSaleComparisonReportTemplateHeader(
							productCostSaleComparisonDTO,
							ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
							("report.template.cash.movements.management.product.cost.sale.comparison.report."),
							"report.template.cash.movements.management.product.cost.sale.comparison.report.");
			
			reportParameters.putAll(this.setUpProductCostSaleComparisonReportTemplateFooter(
					sum_sale_invoice_item_quantity,
					sum_sale_price_acumulated,
					sum_production_cost_acumulated,
					sum_profit,
					ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					("report.template.cash.movements.management.product.cost.sale.comparison.report."),
					"report.template.cash.movements.management.product.cost.sale.comparison.report."));
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			vReportBuilder.addListObjectToReportDataSource(listProductCostSaleComparisonDTO);
			final byte[] var = vReportBuilder.getReportByteArray("product_cost_sale_comparison_201612032003");
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private Map<String,String> setUpProductCostSaleComparisonReportTemplateHeader(
			final ProductCostSaleComparisonDTO productCostSaleComparisonDTO,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("main_title_label", messages.get(REPORT_MESSAGES_PREFIX + "main.title"));
		map.put("report_emission_date_label", messages.get("application.common.emission.date.indicator.label"));
		map.put("report_emission_date", SgpUtils.parseDateParamBySessionLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("currency_label", messages.get("application.common.currency.label"));
		map.put("currency", productCostSaleComparisonDTO.getCurrencyDTO().getId_code() + " / " + productCostSaleComparisonDTO.getCurrencyDTO().getDescription());
		map.put("begin_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.begin.date"));
		map.put("begin_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(productCostSaleComparisonDTO.getBeginDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("end_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.end.date"));
		map.put("end_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(productCostSaleComparisonDTO.getEndDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		map.put("product_label", messages.get("application.common.product.label"));
		map.put("increase_over_cost_for_sale_price_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.increase.over.cost.for.sale.price"));
		map.put("product_quantity_label", messages.get("application.common.quantity.label"));
		map.put("product_sale_price_acumulated_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.sale.price.acumulated"));
		map.put("product_production_cost_acumulated_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.production.cost.acumulated"));
		map.put("product_profit_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.profit"));
		map.put("product_sale_date_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.sale.date"));
		return map;		
	}
	
	private Map<String,String> setUpProductCostSaleComparisonReportTemplateFooter(
			final Long sum_sale_invoice_item_quantity,
			final BigDecimal sum_sale_price_acumulated,
			final BigDecimal sum_production_cost_acumulated,
			final BigDecimal sum_profit,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("totals_label", messages.get(REPORT_MESSAGES_PREFIX + "label.totals"));		
		map.put("sum_sale_invoice_item_quantity_formatted", 
				this.sgpUtils.formatLongValueNumberByLocale(sum_sale_invoice_item_quantity,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sum_sale_price_acumulated_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(sum_sale_price_acumulated,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sum_production_cost_acumulated_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(sum_production_cost_acumulated,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("sum_profit_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(sum_profit,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		return map;
	}

	@Override
	public byte[] manPowerExpenditurePerFunctionaryReport(
			ManPowerExpenditurePerFunctionaryDTO manPowerExpenditurePerFunctionaryDTO,
			HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			manPowerExpenditurePerFunctionaryDTO.setBeginDate
			(SgpUtils.setUpBeginDateFilter(manPowerExpenditurePerFunctionaryDTO.getBeginDate(),
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			
			manPowerExpenditurePerFunctionaryDTO.setEndDate
			(SgpUtils.setUpEndDateFilter(manPowerExpenditurePerFunctionaryDTO.getEndDate(),
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			
			final List<ManPowerExpenditurePerFunctionaryDTO> listManPowerExpenditurePerFunctionaryDTO = 
					this.cashMovementsReportDAO.manPowerExpenditurePerFunctionaryReport(manPowerExpenditurePerFunctionaryDTO);
			
			final Map<String,String> reportParameters = 
					this.setUpManPowerExpenditurePerFunctionaryReporTemplateHeader(
									ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
									(this.MAN_POWER_EXPENDITURE_PER_FUNCTIONARY_REPORT_PREFIX), 
									this.MAN_POWER_EXPENDITURE_PER_FUNCTIONARY_REPORT_PREFIX, 
									manPowerExpenditurePerFunctionaryDTO);
			
			
			Long total_activities_carried_out_count = 0L;
			BigDecimal total_sum_activity_expected_minutes_quantity = BigDecimal.ZERO;
			BigDecimal total_sum_activity_effective_minutes_quantity = BigDecimal.ZERO;
			BigDecimal total_saved_minutes = BigDecimal.ZERO;
			BigDecimal total_sum_man_power_cost = BigDecimal.ZERO;
			
			for(ManPowerExpenditurePerFunctionaryDTO vManPowerExpenditurePerFunctionaryDTO: listManPowerExpenditurePerFunctionaryDTO){
				total_activities_carried_out_count+= vManPowerExpenditurePerFunctionaryDTO.getActivities_carried_out_count();
				total_sum_activity_expected_minutes_quantity = 
						total_sum_activity_expected_minutes_quantity.add
						(vManPowerExpenditurePerFunctionaryDTO.getSum_activity_expected_minutes_quantity());
				total_sum_activity_effective_minutes_quantity = 
						total_sum_activity_effective_minutes_quantity.add
						(vManPowerExpenditurePerFunctionaryDTO.getSum_activity_effective_minutes_quantity());
				total_saved_minutes = total_saved_minutes.add
						(vManPowerExpenditurePerFunctionaryDTO.getSaved_minutes());				
				total_sum_man_power_cost = total_sum_man_power_cost.add
						(vManPowerExpenditurePerFunctionaryDTO.getSum_man_power_cost());
				vManPowerExpenditurePerFunctionaryDTO.setPersonal_civil_id_document_formatted
				(this.sgpUtils.formatLongValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getPersonal_civil_id_document(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vManPowerExpenditurePerFunctionaryDTO.setActivities_carried_out_count_formatted
				(this.sgpUtils.formatLongValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getActivities_carried_out_count(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vManPowerExpenditurePerFunctionaryDTO.setSum_activity_expected_minutes_quantity_formatted
				(this.sgpUtils.formatBigDecimalValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getSum_activity_expected_minutes_quantity(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vManPowerExpenditurePerFunctionaryDTO.setSum_activity_effective_minutes_quantity_formatted
				(this.sgpUtils.formatBigDecimalValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getSum_activity_effective_minutes_quantity(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vManPowerExpenditurePerFunctionaryDTO.setSaved_minutes_formatted
				(this.sgpUtils.formatBigDecimalValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getSaved_minutes(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vManPowerExpenditurePerFunctionaryDTO.setSum_man_power_cost_formatted
				(this.sgpUtils.formatBigDecimalValueNumberByLocale(vManPowerExpenditurePerFunctionaryDTO.getSum_man_power_cost(),
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));				
			}//for(ManPowerExpenditurePerFunctionaryDTO vManPowerExpenditurePerFunctionaryDTO: listManPowerExpenditurePerFunctionaryDTO){
			
			
			reportParameters.putAll(
					this.setUpManPowerExpenditurePerFunctionaryReporTemplateFooter(
							total_activities_carried_out_count, 
							total_sum_activity_expected_minutes_quantity, 
							total_sum_activity_effective_minutes_quantity, 
							total_saved_minutes, 
							total_sum_man_power_cost, 
							ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
							(this.MAN_POWER_EXPENDITURE_PER_FUNCTIONARY_REPORT_PREFIX), 
							this.MAN_POWER_EXPENDITURE_PER_FUNCTIONARY_REPORT_PREFIX));
			
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			vReportBuilder.addListObjectToReportDataSource(listManPowerExpenditurePerFunctionaryDTO);
			final byte[] var = vReportBuilder.getReportByteArray("man_power_expenditure_per_functionary_report_201612052231");
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
	
	private Map<String,String> setUpManPowerExpenditurePerFunctionaryReporTemplateHeader(
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX,
			final ManPowerExpenditurePerFunctionaryDTO manPowerExpenditurePerFunctionaryDTO){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("main_title_label", messages.get(REPORT_MESSAGES_PREFIX + "main.title"));
		map.put("report_emission_date_label", messages.get("application.common.emission.date.indicator.label"));
		map.put("report_emission_date", SgpUtils.parseDateParamBySessionLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("currency_label", messages.get("application.common.currency.label"));
		map.put("currency", manPowerExpenditurePerFunctionaryDTO.getCurrencyDTO().getId_code() + " / " + manPowerExpenditurePerFunctionaryDTO.getCurrencyDTO().getDescription());
		map.put("begin_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.begin.date"));
		map.put("begin_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(manPowerExpenditurePerFunctionaryDTO.getBeginDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("end_date_label", messages.get(REPORT_MESSAGES_PREFIX + "filter.end.date"));
		map.put("end_date", SgpUtils.parseDateParamWithOutHourBySessionLocale(manPowerExpenditurePerFunctionaryDTO.getEndDate(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		
		map.put("personal_civil_id_document_label", messages.get("application.common.personal.civil.id.document.filter.input.prompt"));
		map.put("functionary_name_label", messages.get("application.common.quantity.label"));
		map.put("activities_carried_out_count_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.activities.carried.out.count"));
		map.put("sum_activity_expected_minutes_quantity_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.activity.expected.minutes.quantity"));
		map.put("sum_activity_effective_minutes_quantity_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.activity.effective.minutes.quantity"));
		map.put("saved_minutes_label", messages.get(REPORT_MESSAGES_PREFIX + "table.column.activity.saved_minutes.quantity"));
		map.put("sum_man_power_cost_label", messages.get("application.common.amount.label"));
		return map;
	}
	
	private Map<String,String> setUpManPowerExpenditurePerFunctionaryReporTemplateFooter(
			final Long total_activities_carried_out_count,
			final BigDecimal total_sum_activity_expected_minutes_quantity,
			final BigDecimal total_sum_activity_effective_minutes_quantity,
			final BigDecimal total_saved_minutes,
			final BigDecimal total_sum_man_power_cost,
			final Map<String,String> messages,
			final String REPORT_MESSAGES_PREFIX){
		final Map<String,String> map = new HashMap<String,String>();
		map.put("totals_label", messages.get(REPORT_MESSAGES_PREFIX + "label.totals"));		
		map.put("total_activities_carried_out_count_formatted", 
				this.sgpUtils.formatLongValueNumberByLocale(total_activities_carried_out_count,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_sum_activity_expected_minutes_quantity_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_sum_activity_expected_minutes_quantity,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_sum_activity_effective_minutes_quantity_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_sum_activity_effective_minutes_quantity,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_saved_minutes_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_saved_minutes,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		map.put("total_sum_man_power_cost_formatted", 
				this.sgpUtils.formatBigDecimalValueNumberByLocale(total_sum_man_power_cost,
						this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		return map;
	}

	@Override
	public PurchaseInvoiceCreditNoteDTO insertPurchaseInvoiceCreditNoteDTO(
			PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			purchaseInvoiceCreditNoteDTO.setId(this.purchaseInvoiceDAO.pmsPurchaseInvoiceCreditNoteDTOIdBySequence());
			long idPurchaseInvoiceCreditNoteItemDTO = 0;
			int creditNoteItemsProcessedCounter = 0;
			this.purchaseInvoiceDAO.insertPurchaseInvoiceCreditNoteDTO(purchaseInvoiceCreditNoteDTO);
			for(PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO : purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()){
				if(vPurchaseInvoiceCreditNoteItemDTO.getQuantity()!=null && vPurchaseInvoiceCreditNoteItemDTO.getQuantity().compareTo(BigDecimal.ZERO) == 1){
					vPurchaseInvoiceCreditNoteItemDTO.setId_purchase_invoice_credit_note(purchaseInvoiceCreditNoteDTO.getId());
					vPurchaseInvoiceCreditNoteItemDTO.setId(this.purchaseInvoiceDAO.pmsPurchaseInvoiceCreditNoteItemDTOIdBySequence(idPurchaseInvoiceCreditNoteItemDTO++));
					this.purchaseInvoiceDAO.insertPurchaseInvoiceCreditNoteItemDTO(vPurchaseInvoiceCreditNoteItemDTO);
					creditNoteItemsProcessedCounter++;
				}//if(vPurchaseInvoiceCreditNoteItemDTO.getQuantity()!=null && vPurchaseInvoiceCreditNoteItemDTO.getQuantity().compareTo(BigDecimal.ZERO) == 1){
			}//for(PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO : purchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()){
			if(creditNoteItemsProcessedCounter == 0){
				throw new Exception(
						new Exception
				("py.com.kyron.sgp.bussines.cash.movements.management.service.impl.cash.movements.management.service.impl.insert.credit.note.not.any.credit.note.item.processed.error"));
			}
			return this.listPurchaseInvoiceCreditNoteDTO(new PurchaseInvoiceCreditNoteDTO(purchaseInvoiceCreditNoteDTO.getId())).get(0);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO(
			PurchaseInvoiceCreditNoteDTO purchaseInvoiceCreditNoteDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<PurchaseInvoiceCreditNoteDTO> listPurchaseInvoiceCreditNoteDTO
			 = this.purchaseInvoiceDAO.listPurchaseInvoiceCreditNoteDTO(purchaseInvoiceCreditNoteDTO);
			
			for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
				vPurchaseInvoiceCreditNoteDTO.setPersonDTO(this.personManagementService.listPersonDTO(new PersonDTO(vPurchaseInvoiceCreditNoteDTO.getId_person())).get(0));
				vPurchaseInvoiceCreditNoteDTO.setCurrencyDTO(this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(vPurchaseInvoiceCreditNoteDTO.getId_currency())).get(0));
				vPurchaseInvoiceCreditNoteDTO.setListPurchaseInvoiceCreditNoteItemDTO(
						this.purchaseInvoiceDAO.listPurchaseInvoiceCreditNoteItemDTO(new PurchaseInvoiceCreditNoteItemDTO(vPurchaseInvoiceCreditNoteDTO.getId())));
						
				for(PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO : vPurchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()){
					vPurchaseInvoiceCreditNoteItemDTO.setRawMaterialDTO(this.productionManagementService.listRawMaterialDTO(new RawMaterialDTO(vPurchaseInvoiceCreditNoteItemDTO.getId_raw_material())).get(0));
					vPurchaseInvoiceCreditNoteItemDTO.setMeasurmentUnitDTO(this.productionManagementService.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vPurchaseInvoiceCreditNoteItemDTO.getId_measurment_unit())).get(0));
				}//for(PurchaseInvoiceCreditNoteItemDTO vPurchaseInvoiceCreditNoteItemDTO : vPurchaseInvoiceCreditNoteDTO.getListPurchaseInvoiceCreditNoteItemDTO()){
			}//for(PurchaseInvoiceCreditNoteDTO vPurchaseInvoiceCreditNoteDTO : listPurchaseInvoiceCreditNoteDTO){
			return listPurchaseInvoiceCreditNoteDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
}
