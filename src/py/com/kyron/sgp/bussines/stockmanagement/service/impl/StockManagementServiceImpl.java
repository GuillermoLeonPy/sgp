/**
 * 
 */
package py.com.kyron.sgp.bussines.stockmanagement.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.application.utils.ReportBuilder;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.CreditNoteItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.bussines.stockmanagement.domain.InsufficiencyRawMaterialReportDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialExistenceDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceItemProductDepositMovementDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDepositMovementDTO;
import py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.persistence.personmanagement.dao.PersonDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.MeasurmentUnitDAO;
import py.com.kyron.sgp.persistence.productionmanagement.dao.RawMaterialDAO;
import py.com.kyron.sgp.persistence.stockmanagement.dao.ProductDepositMovementDAO;
import py.com.kyron.sgp.persistence.stockmanagement.dao.RawMaterialExistenceDAO;
import py.com.kyron.sgp.persistence.stockmanagement.dao.RawMaterialPurchaseRequestDAO;

/**
 * @author testuser
 *
 */
public class StockManagementServiceImpl implements StockManagementService {

	private final Logger logger = LoggerFactory.getLogger(StockManagementServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private RawMaterialExistenceDAO rawMaterialExistenceDAO;
	private RawMaterialPurchaseRequestDAO rawMaterialPurchaseRequestDAO;
	private PersonDAO personDAO;
	private MeasurmentUnitDAO measurmentUnitDAO;
	private RawMaterialDAO rawMaterialDAO;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	private ProductDepositMovementDAO productDepositMovementDAO;
	private SgpUtils sgpUtils;
	private final String CURRENT_RAW_MATERIAL_INSUFFICIENCY_REPORT_PREFIX = "report.template.stock.management.current.raw.material.insufficiency.report.";
	private final String ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX = "report.template.stock.management.order.sale.invoice.status.report.";
	private ComercialManagementService comercialManagementService;
	/**
	 * 
	 */
	public StockManagementServiceImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\n StockManagementServiceImpl()...");
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService#insertRawMaterialPurchaseRequestDTO(py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO)
	 */
	@Override
	public RawMaterialPurchaseRequestDTO insertRawMaterialPurchaseRequestDTO(
			RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			rawMaterialPurchaseRequestDTO.setId(this.rawMaterialPurchaseRequestDAO.pmsRawMaterialPurchaseRequestDTOIdBySequence());
			this.rawMaterialPurchaseRequestDAO.insertRawMaterialPurchaseRequestDTO(rawMaterialPurchaseRequestDTO);
			return rawMaterialPurchaseRequestDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService#updateRawMaterialPurchaseRequestDTO(py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO)
	 */
	@Override
	public RawMaterialPurchaseRequestDTO updateRawMaterialPurchaseRequestDTO(
			RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.rawMaterialPurchaseRequestDAO.updateRawMaterialPurchaseRequestDTO(rawMaterialPurchaseRequestDTO);
			return rawMaterialPurchaseRequestDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/* (non-Javadoc)
	 * @see py.com.kyron.sgp.bussines.stockmanagement.service.StockManagementService#listRawMaterialPurchaseRequestDTO(py.com.kyron.sgp.bussines.stockmanagement.domain.RawMaterialPurchaseRequestDTO)
	 */
	@Override
	public List<RawMaterialPurchaseRequestDTO> listRawMaterialPurchaseRequestDTO(
			RawMaterialPurchaseRequestDTO rawMaterialPurchaseRequestDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<RawMaterialPurchaseRequestDTO> listRawMaterialPurchaseRequestDTO = this.rawMaterialPurchaseRequestDAO.listRawMaterialPurchaseRequestDTO(rawMaterialPurchaseRequestDTO);
			for(RawMaterialPurchaseRequestDTO vRawMaterialPurchaseRequestDTO : listRawMaterialPurchaseRequestDTO){
				vRawMaterialPurchaseRequestDTO.setRawMaterialDTO(this.rawMaterialDAO.listRawMaterialDTO(new RawMaterialDTO(vRawMaterialPurchaseRequestDTO.getId_raw_material())).get(0));
				vRawMaterialPurchaseRequestDTO.setMeasurmentUnitDTO(this.measurmentUnitDAO.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vRawMaterialPurchaseRequestDTO.getId_measurment_unit())).get(0));
				vRawMaterialPurchaseRequestDTO.setPersonDTO(this.personDAO.listPersonDTO(new PersonDTO(vRawMaterialPurchaseRequestDTO.getId_person_supplier())).get(0));
			}
			return listRawMaterialPurchaseRequestDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public BussinesSessionUtils getBussinesSessionUtils() {
		return bussinesSessionUtils;
	}

	public void setBussinesSessionUtils(BussinesSessionUtils bussinesSessionUtils) {
		this.bussinesSessionUtils = bussinesSessionUtils;
	}

	public RawMaterialExistenceDAO getRawMaterialExistenceDAO() {
		return rawMaterialExistenceDAO;
	}

	public void setRawMaterialExistenceDAO(
			RawMaterialExistenceDAO rawMaterialExistenceDAO) {
		this.rawMaterialExistenceDAO = rawMaterialExistenceDAO;
	}

	public RawMaterialPurchaseRequestDAO getRawMaterialPurchaseRequestDAO() {
		return rawMaterialPurchaseRequestDAO;
	}

	public void setRawMaterialPurchaseRequestDAO(
			RawMaterialPurchaseRequestDAO rawMaterialPurchaseRequestDAO) {
		this.rawMaterialPurchaseRequestDAO = rawMaterialPurchaseRequestDAO;
	}

	public PersonDAO getPersonDAO() {
		return personDAO;
	}

	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public MeasurmentUnitDAO getMeasurmentUnitDAO() {
		return measurmentUnitDAO;
	}

	public void setMeasurmentUnitDAO(MeasurmentUnitDAO measurmentUnitDAO) {
		this.measurmentUnitDAO = measurmentUnitDAO;
	}

	public RawMaterialDAO getRawMaterialDAO() {
		return rawMaterialDAO;
	}

	public void setRawMaterialDAO(RawMaterialDAO rawMaterialDAO) {
		this.rawMaterialDAO = rawMaterialDAO;
	}

	@Override
	public RawMaterialExistenceDTO insertRawMaterialExistenceDTO(
			RawMaterialExistenceDTO rawMaterialExistenceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			rawMaterialExistenceDTO.setId(this.rawMaterialExistenceDAO.pmsRawMaterialExistenceDTOIdBySequence());
			this.rawMaterialExistenceDAO.insertRawMaterialExistenceDTO(rawMaterialExistenceDTO);
			return rawMaterialExistenceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO(
			RawMaterialExistenceDTO rawMaterialExistenceDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<RawMaterialExistenceDTO> listRawMaterialExistenceDTO = this.rawMaterialExistenceDAO.listRawMaterialExistenceDTO(rawMaterialExistenceDTO);
			for(RawMaterialExistenceDTO vRawMaterialExistenceDTO : listRawMaterialExistenceDTO){
				vRawMaterialExistenceDTO.setMeasurmentUnitDTO(this.measurmentUnitDAO.listMeasurmentUnitDTO(new MeasurmentUnitDTO(vRawMaterialExistenceDTO.getId_measurment_unit())).get(0));
			}
			return listRawMaterialExistenceDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public void init(){
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
		this.sgpUtils = new SgpUtils();
	}

	@Override
	public List<PAIRawMaterialSupplyDTO> effectuatePAIRawMaterialSupplyDTO(
			PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.rawMaterialExistenceDAO.effectuatePAIRawMaterialSupplyDTO(paramPAIRawMaterialSupplyDTO);
			return this.listPAIRawMaterialSupplyDTO(paramPAIRawMaterialSupplyDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO(
			PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTO = this.rawMaterialExistenceDAO.listPAIRawMaterialSupplyDTO(paramPAIRawMaterialSupplyDTO);
			for(PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO : listPAIRawMaterialSupplyDTO){
				vPAIRawMaterialSupplyDTO.setListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(this.rawMaterialExistenceDAO.listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(new PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(null, vPAIRawMaterialSupplyDTO.getId())));
			}
			
			return listPAIRawMaterialSupplyDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTOHistory(
			PAIRawMaterialSupplyDTO paramPAIRawMaterialSupplyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<PAIRawMaterialSupplyDTO> listPAIRawMaterialSupplyDTOHistory = this.rawMaterialExistenceDAO.listPAIRawMaterialSupplyDTOHistory(paramPAIRawMaterialSupplyDTO);
			for(PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO : listPAIRawMaterialSupplyDTOHistory){
				vPAIRawMaterialSupplyDTO.setListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(this.rawMaterialExistenceDAO.listPAIRawMaterialSupplyPurchaseInvoiceAffectedDTOHistory(new PAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(null, vPAIRawMaterialSupplyDTO.getId())));
			}			
			return listPAIRawMaterialSupplyDTOHistory;
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
	public List<SaleInvoiceProductDeliverablesDTO> listSaleInvoiceProductDeliverablesDTO(
			SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<SaleInvoiceProductDeliverablesDTO> listSaleInvoiceProductDeliverablesDTO = 
					this.productDepositMovementDAO.listSaleInvoiceProductDeliverablesDTO(saleInvoiceProductDeliverablesDTO);
			for(SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO : listSaleInvoiceProductDeliverablesDTO){
				vSaleInvoiceProductDeliverablesDTO.setPersonDTO(this.personDAO.listPersonDTO(new PersonDTO(vSaleInvoiceProductDeliverablesDTO.getId_person())).get(0));
			}
			return listSaleInvoiceProductDeliverablesDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<SaleInvoiceItemProductDeliverablesDTO> listSaleInvoiceItemProductDeliverablesDTO(
			SaleInvoiceItemProductDeliverablesDTO saleInvoiceItemProductDeliverablesDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.productDepositMovementDAO.listSaleInvoiceItemProductDeliverablesDTO(saleInvoiceItemProductDeliverablesDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long pmsProductDepositMovementIdentifierNumberBySequence()
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.productDepositMovementDAO.pmsProductDepositMovementIdentifierNumberBySequence();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}


	@Override
	public List<SaleInvoiceProductDepositMovementDTO> listSaleInvoiceProductDepositMovementDTO(
			SaleInvoiceProductDepositMovementDTO saleInvoiceProductDepositMovementDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		List<SaleInvoiceProductDepositMovementDTO> listSaleInvoiceProductDepositMovementDTO
		= this.productDepositMovementDAO.listSaleInvoiceProductDepositMovementDTO(saleInvoiceProductDepositMovementDTO);
		for (SaleInvoiceProductDepositMovementDTO vSaleInvoiceProductDepositMovementDTO : listSaleInvoiceProductDepositMovementDTO){
			vSaleInvoiceProductDepositMovementDTO.setListSaleInvoiceItemProductDepositMovementDTO(
					this.productDepositMovementDAO.listSaleInvoiceItemProductDepositMovementDTO(
							new SaleInvoiceItemProductDepositMovementDTO(vSaleInvoiceProductDepositMovementDTO.getId())));
			for(SaleInvoiceItemProductDepositMovementDTO vSaleInvoiceItemProductDepositMovementDTO : 
				vSaleInvoiceProductDepositMovementDTO.getListSaleInvoiceItemProductDepositMovementDTO()){
				vSaleInvoiceItemProductDepositMovementDTO.setListSIItemPDMProductInstanceInvolvedDTO(
						this.productDepositMovementDAO.listSIItemPDMProductInstanceInvolvedDTO(
								new SIItemPDMProductInstanceInvolvedDTO(vSaleInvoiceItemProductDepositMovementDTO.getId())));
			}//for(SaleInvoiceItemProductDepositMovementDTO vSaleInvoiceItemProductDepositMovementDTO :
			
		}//for (SaleInvoiceProductDepositMovementDTO vSaleInvoiceProductDepositMovementDTO : listSaleInvoiceProductDepositMovementDTO){
		return listSaleInvoiceProductDepositMovementDTO;
	}

	@Override
	public SaleInvoiceProductDeliverablesDTO effectuateProductDeliverSaleInvoiceProductDeliverablesDTO(
			SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			int counter = 0;
			for(SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO : 
				saleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO()){
				if(vSaleInvoiceItemProductDeliverablesDTO.getDeliver_quantity()!=null){
					counter++;
					this.productDepositMovementDAO.effectuateProductDeliverSaleInvoiceItemProductDeliverablesDTO
					(vSaleInvoiceItemProductDeliverablesDTO);
				}//if(vSaleInvoiceItemProductDeliverablesDTO.getDeliver_quantity()!=null){				
			}//for(SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO :
			
			if(counter == 0){
				//throw new Exception(/*the cause and desired message to decode*/
				//	new Exception
				//	("py.com.kyron.sgp.persistence.personsmanagement.dao.persondao.insert.personal.name.last.name.cannot.be.null.error1234567end.of.message"));
				throw new Exception(
						new Exception
				("py.com.kyron.sgp.bussines.stockmanagement.service.impl.stock.management.service.impl.not.any.deliver.product.transaction.realized.error"));
			}
			/* READ THE UPDATED RECORDS AND ADD THE UNIQUE PRODUCT INSTANCES INVOLVED*/
			SaleInvoiceProductDeliverablesDTO vSaleInvoiceProductDeliverablesDTO = 
					this.listSaleInvoiceProductDeliverablesDTO(saleInvoiceProductDeliverablesDTO).get(0);
			vSaleInvoiceProductDeliverablesDTO.setProduct_deposit_movement_identifier_number(
					saleInvoiceProductDeliverablesDTO.getProduct_deposit_movement_identifier_number());
			vSaleInvoiceProductDeliverablesDTO.setListSaleInvoiceItemProductDeliverablesDTO(
					this.listSaleInvoiceItemProductDeliverablesDTO(
							new SaleInvoiceItemProductDeliverablesDTO(
									saleInvoiceProductDeliverablesDTO.getId_sale_invoice())));
			for(SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO : 
				vSaleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO()){
				vSaleInvoiceItemProductDeliverablesDTO.setProduct_deposit_movement_identifier_number(vSaleInvoiceProductDeliverablesDTO.getProduct_deposit_movement_identifier_number());
				vSaleInvoiceItemProductDeliverablesDTO.setListSIItemPDMProductInstanceInvolvedDTO(
						this.productDepositMovementDAO.listSIItemPDMProductInstanceInvolvedDTOBySaleInvoiceItemProductDeliverablesDTO
						(vSaleInvoiceItemProductDeliverablesDTO));				
			}

			vSaleInvoiceProductDeliverablesDTO.setTransactionRealized(true);
			return vSaleInvoiceProductDeliverablesDTO;
		}catch(Exception e){
			
			logger.info("\n===================================================================="
					+ "\n e.getCause() : " + e.getCause()
					+ "\n====================================================================");
			
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<SIItemPDMProductInstanceInvolvedDTO> listProductInstancesReturnableByCreditNoteItemDTO(
			CreditNoteItemDTO creditNoteItemDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.productDepositMovementDAO.listProductInstancesReturnableByCreditNoteItemDTO(creditNoteItemDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public byte[] currentInsufficiencyRawMaterialReport(
			Long id_currency, HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final List<InsufficiencyRawMaterialReportDTO> listInsufficiencyRawMaterialReportDTO = this.rawMaterialExistenceDAO.currentInsufficiencyRawMaterialReport(id_currency);
			
			if(listInsufficiencyRawMaterialReportDTO == null || listInsufficiencyRawMaterialReportDTO.isEmpty())return null;
			
			final Map<String,String> messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					(this.CURRENT_RAW_MATERIAL_INSUFFICIENCY_REPORT_PREFIX);
			
			BigDecimal total_cost_amount = BigDecimal.ZERO;
			for(InsufficiencyRawMaterialReportDTO vInsufficiencyRawMaterialReportDTO : listInsufficiencyRawMaterialReportDTO){
				total_cost_amount = total_cost_amount.add(vInsufficiencyRawMaterialReportDTO.getCost_amount());
				vInsufficiencyRawMaterialReportDTO.setRequired_quantity_formatted(
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vInsufficiencyRawMaterialReportDTO.getRequired_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				vInsufficiencyRawMaterialReportDTO.setCost_amount_formatted(
						vInsufficiencyRawMaterialReportDTO.getCost_amount().doubleValue() == BigDecimal.ZERO.doubleValue() ?
								messages.get(this.CURRENT_RAW_MATERIAL_INSUFFICIENCY_REPORT_PREFIX + "no.valid.cost.label") :
						this.sgpUtils.formatBigDecimalValueNumberByLocale(vInsufficiencyRawMaterialReportDTO.getCost_amount(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			}//for(InsufficiencyRawMaterialReportDTO vInsufficiencyRawMaterialReportDTO : listInsufficiencyRawMaterialReportDTO){
			
			final CurrencyDTO vCurrencyDTO = this.comercialManagementService.listCurrencyDTO(new CurrencyDTO(id_currency)).get(0);
			final Map<String,String> reportParameters = new HashMap<String,String>();
			reportParameters.put("main_title_label", messages.get(this.CURRENT_RAW_MATERIAL_INSUFFICIENCY_REPORT_PREFIX + "main.title"));
			reportParameters.put("report_emission_date_label", messages.get("application.common.emission.date.indicator.label"));
			reportParameters.put("report_emission_date", SgpUtils.parseDateParamBySessionLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("currency_label", messages.get("application.common.currency.label"));
			reportParameters.put("currency", vCurrencyDTO.getId_code() + " / " + vCurrencyDTO.getDescription());
			reportParameters.put("raw_material_label", messages.get("application.common.rawmaterialid.label"));
			reportParameters.put("measurment_unit_label", messages.get("application.common.measurmentunitid.label"));
			reportParameters.put("required_quantity_label", messages.get("application.common.required.quantity"));
			reportParameters.put("cost_amount_label", messages.get("application.common.amount.label"));
			reportParameters.put("total_cost_amount_label", messages.get("application.common.table.column.total.amount.label"));
			reportParameters.put("total_cost_amount_formatted", 
					this.sgpUtils.formatBigDecimalValueNumberByLocale(total_cost_amount,
							this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));

			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			vReportBuilder.addListObjectToReportDataSource(listInsufficiencyRawMaterialReportDTO);
			final byte[] var = vReportBuilder.getReportByteArray("current_insufficiency_raw_material_report_201612051618");
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
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
	public byte[] orderAndInvoiceStatusReport(
			SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO,
			HttpSession httpSession) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			final Map<String,String> messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
					(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX);
			Long sum_canceled_quantity = 0L;
			Long sum_pending_quantity = 0L;
			Long sum_in_progress_quantity = 0L;
			Long sum_total_exigible_quantity = 0L;
			Long sum_stock_quantity = 0L;
			Long sum_delivered_quantity = 0L;
			Long sum_returned_quantity = 0L;
			Long sum_remain_exigible_quantity = 0L;
			
			for(SaleInvoiceItemProductDeliverablesDTO vSaleInvoiceItemProductDeliverablesDTO : saleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO()){
				vSaleInvoiceItemProductDeliverablesDTO.setOrd_item_canceled_entering_production_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_canceled_entering_production(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_canceled_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_canceled_entering_production();
				
				vSaleInvoiceItemProductDeliverablesDTO.setOrd_item_pending_to_production_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_pending_to_production(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_pending_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_pending_to_production();
				
				vSaleInvoiceItemProductDeliverablesDTO.setOrd_item_in_progress_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_in_progress_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_in_progress_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getOrd_item_in_progress_quantity();
				
				vSaleInvoiceItemProductDeliverablesDTO.setInvoice_item_total_exigible_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_total_exigible_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_total_exigible_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_total_exigible_quantity();
				
				vSaleInvoiceItemProductDeliverablesDTO.setInvoice_item_remain_exigible_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_remain_exigible_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_remain_exigible_quantity();
				
				vSaleInvoiceItemProductDeliverablesDTO.setInvoice_item_delivered_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_delivered_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_delivered_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_delivered_quantity();
				
				vSaleInvoiceItemProductDeliverablesDTO.setInvoice_item_product_stock_quantity_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_product_stock_quantity(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_stock_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_product_stock_quantity();	
				
				vSaleInvoiceItemProductDeliverablesDTO.setInvoice_item_returned_quantity_stock_formatted(
						this.sgpUtils.formatLongValueNumberByLocale(vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_returned_quantity_stock(),
								this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
				sum_returned_quantity+=vSaleInvoiceItemProductDeliverablesDTO.getInvoice_item_returned_quantity_stock();				
			}
			
			final Map<String,String> reportParameters = new HashMap<String,String>();
			reportParameters.put("main_title_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "main.title"));
			reportParameters.put("report_emission_date_label", messages.get("application.common.emission.date.indicator.label"));
			reportParameters.put("report_emission_date", SgpUtils.parseDateParamBySessionLocale(new Date(), this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sale_invoice_number_label", messages.get("application.common.sale.invoice.number.indicator.label"));
			reportParameters.put("sale_invoice_number", saleInvoiceProductDeliverablesDTO.getInvoice_identifier_number());
			reportParameters.put("sale_invoice_status_label", messages.get("application.common.status.label"));
			reportParameters.put("sale_invoice_status", messages.get(saleInvoiceProductDeliverablesDTO.getInvoice_status()));
			reportParameters.put("order_number_label", messages.get("application.common.order.number.indicator.label"));
			reportParameters.put("order_number", this.sgpUtils.formatLongValueNumberByLocale(saleInvoiceProductDeliverablesDTO.getOrder_identifier_number(),
													this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("order_status_label", messages.get("application.common.status.label"));
			reportParameters.put("order_status", messages.get(saleInvoiceProductDeliverablesDTO.getOrder_status()));
			reportParameters.put("product_label", messages.get("application.common.table.column.article.label"));
			reportParameters.put("canceled_quantity_label", messages.get("application.common.status.canceled"));
			reportParameters.put("pending_quantity_label", messages.get("application.common.status.pending"));
			reportParameters.put("in_progress_quantity_label", messages.get("application.common.status.in.progress"));
			reportParameters.put("total_exigible_quantity_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "table.column.total.exigible"));
			reportParameters.put("stock_quantity_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "table.column.stock"));
			reportParameters.put("delivered_quantity_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "table.column.delivered"));
			reportParameters.put("returned_quantity_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "table.column.returned"));
			reportParameters.put("remain_exigible_quantity_label", messages.get(this.ORDER_SALE_INVOICE_STATUS_REPORT_PREFIX + "table.column.remain.exigible"));
			
			reportParameters.put("sum_canceled_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_canceled_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_pending_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_pending_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_in_progress_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_in_progress_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_total_exigible_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_total_exigible_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_stock_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_stock_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_delivered_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_delivered_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_returned_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_returned_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("sum_remain_exigible_quantity_formatted", this.sgpUtils.formatLongValueNumberByLocale(sum_remain_exigible_quantity,
					this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
			reportParameters.put("totals_label", messages.get("application.common.table.column.total.amount.label"));
			
			final ReportBuilder vReportBuilder = new ReportBuilder(httpSession);
			
			for(String key : reportParameters.keySet()){
				vReportBuilder.addReportParameter(key, reportParameters.get(key));
			}
			
			vReportBuilder.addListObjectToReportDataSource(saleInvoiceProductDeliverablesDTO.getListSaleInvoiceItemProductDeliverablesDTO());
			final byte[] var = vReportBuilder.getReportByteArray("order_status_report_201612151521");
			return var;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
}
