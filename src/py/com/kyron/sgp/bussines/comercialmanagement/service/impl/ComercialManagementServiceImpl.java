package py.com.kyron.sgp.bussines.comercialmanagement.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.PersistenceErrorMessagesDecoder;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderItemRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.persistence.comercialmanagement.dao.CurrencyDAO;
import py.com.kyron.sgp.persistence.comercialmanagement.dao.OrderDAO;
import py.com.kyron.sgp.persistence.comercialmanagement.dao.OrderItemDAO;
import py.com.kyron.sgp.persistence.comercialmanagement.dao.ProductDAO;
import py.com.kyron.sgp.persistence.personmanagement.dao.PersonDAO;

public class ComercialManagementServiceImpl implements ComercialManagementService {
	private final Logger logger = LoggerFactory.getLogger(ComercialManagementServiceImpl.class);
	private BussinesSessionUtils bussinesSessionUtils;
	private CurrencyDAO currencyDAO;
	private ProductDAO productDAO;
	private OrderDAO orderDAO;
	private OrderItemDAO orderItemDAO;
	private PersonDAO personDAO;
	private PersistenceErrorMessagesDecoder persistenceErrorMessagesDecoder;
	
	public ComercialManagementServiceImpl() {
		// TODO Auto-generated constructor stub
		logger.info("\nComercialManagementServiceImpl()...");
	}

	@Override
	public CurrencyDTO registrarMoneda(CurrencyDTO currencyDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nregistrarMoneda\ncurrencyDTO.toString(): " + currencyDTO.toString());
			this.currencyDAO.insert(currencyDTO);
			return currencyDTO;
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

	public CurrencyDAO getCurrencyDAO() {
		return currencyDAO;
	}

	public void setCurrencyDAO(CurrencyDAO currencyDAO) {
		this.currencyDAO = currencyDAO;
	}

	@Override
	public List<CurrencyDTO> listCurrencyDTO(CurrencyDTO currencyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<CurrencyDTO> listCurrencyDTO = this.currencyDAO.listCurrencyDTO(currencyDTO);
			for(CurrencyDTO vCurrencyDTO: listCurrencyDTO){
				logger.info(vCurrencyDTO.toString());
			}
			
			return listCurrencyDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public CurrencyDTO updateCurrency(CurrencyDTO currencyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nupdateCurrency\ncurrencyDTO.toString(): " + currencyDTO.toString());
			this.currencyDAO.update(currencyDTO);
			return currencyDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	public ProductDAO getProductDAO() {
		return productDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	@Override
	public ProductDTO insertProductDTO(ProductDTO productDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			productDTO.setId(this.productDAO.pmsProductIdBySequence());
			this.productDAO.insertProductDTO(productDTO);
			return productDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<ProductDTO> listProductDTO(ProductDTO productDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.productDAO.listProductDTO(productDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public CurrencyExchangeRateDTO insertCurrencyExchangeRateDTO(
			CurrencyExchangeRateDTO currencyExchangeRateDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			currencyExchangeRateDTO.setId(this.currencyDAO.pmsCurrencyExchangeRateDTOIdBySequence());
			this.currencyDAO.insertCurrencyExchangeRateDTO(currencyExchangeRateDTO);
			return currencyExchangeRateDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public CurrencyExchangeRateDTO updateCurrencyExchangeRateDTO(
			CurrencyExchangeRateDTO currencyExchangeRateDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.currencyDAO.updateCurrencyExchangeRateDTO(currencyExchangeRateDTO);
			return currencyExchangeRateDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO(
			CurrencyExchangeRateDTO currencyExchangeRateDTO)throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO = this.currencyDAO.listCurrencyExchangeRateDTO(currencyExchangeRateDTO);
			for(CurrencyExchangeRateDTO vCurrencyExchangeRateDTO : listCurrencyExchangeRateDTO){
				vCurrencyExchangeRateDTO.setLocalCurrencyDTO(this.currencyDAO.listCurrencyDTO(new CurrencyDTO(currencyExchangeRateDTO.getId_currency_local())).get(0));
			}
			return listCurrencyExchangeRateDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long getCurrencyExchangeRateValidRowId(Long id_currency)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.currencyDAO.getCurrencyExchangeRateValidRowId(id_currency);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public OrderDTO insertOrderDTO(OrderDTO orderDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			orderDTO.setId(this.orderDAO.pmsOrderDTOIdBySequence());
			this.orderDAO.insertOrderDTO(orderDTO);
			long idItem = 0;
			for(OrderItemDTO vOrderItemDTO:orderDTO.getListOrderItemDTO()){
				vOrderItemDTO.setId_order(orderDTO.getId());
				vOrderItemDTO.setId(this.orderItemDAO.pmsOrderItemDTOIdBySequence(idItem++));
				this.orderItemDAO.insertOrderItemDTO(vOrderItemDTO);
			}
			return orderDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<OrderDTO> listOrderDTO(OrderDTO orderDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			List<OrderDTO> listOrderDTO = this.orderDAO.listOrderDTO(orderDTO);
			for(OrderDTO vOrderDTO:listOrderDTO){
				vOrderDTO.setListOrderItemDTO(this.orderItemDAO.listOrderItemDTO(new OrderItemDTO(null, vOrderDTO.getId())));
				vOrderDTO.setPersonDTO(this.personDAO.listPersonDTO(new PersonDTO(vOrderDTO.getId_person())).get(0));
				vOrderDTO.setCurrencyDTO(this.currencyDAO.listCurrencyDTO(new CurrencyDTO(vOrderDTO.getId_currency())).get(0));
				for(OrderItemDTO vOrderItemDTO:vOrderDTO.getListOrderItemDTO()){
					vOrderItemDTO.setProductDTO(this.productDAO.listProductDTO(new ProductDTO(vOrderItemDTO.getId_product())).get(0));
				}
			}			
			return listOrderDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	/**
	 * @return the orderDAO
	 */
	public OrderDAO getOrderDAO() {
		return orderDAO;
	}

	/**
	 * @param orderDAO the orderDAO to set
	 */
	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	/**
	 * @return the orderItemDAO
	 */
	public OrderItemDAO getOrderItemDAO() {
		return orderItemDAO;
	}

	/**
	 * @param orderItemDAO the orderItemDAO to set
	 */
	public void setOrderItemDAO(OrderItemDAO orderItemDAO) {
		this.orderItemDAO = orderItemDAO;
	}

	/**
	 * @return the personDAO
	 */
	public PersonDAO getPersonDAO() {
		return personDAO;
	}

	/**
	 * @param personDAO the personDAO to set
	 */
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	@Override
	public BigDecimal determinateProductPriceByProductIdCurrencyId(
			OrderItemDTO orderItemDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.orderItemDAO.determinateProductPriceByProductIdCurrencyId(orderItemDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public CreditOrderChargeConditionDTO insertCreditOrderChargeConditionDTO(
			CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			creditOrderChargeConditionDTO.setId(this.orderDAO.pmsCreditOrderChargeConditionDTOIdBySequence());
			this.orderDAO.insertCreditOrderChargeConditionDTO(creditOrderChargeConditionDTO);
			return creditOrderChargeConditionDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public CreditOrderChargeConditionDTO updateCreditOrderChargeConditionDTO(
			CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.orderDAO.updateCreditOrderChargeConditionDTO(creditOrderChargeConditionDTO);
			return creditOrderChargeConditionDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public List<CreditOrderChargeConditionDTO> listCreditOrderChargeConditionDTO(
			CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.orderDAO.listCreditOrderChargeConditionDTO(null);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long getCreditOrderChargeConditionValidRowId()
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.orderDAO.getCreditOrderChargeConditionValidRowId();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public Long pmsOrderDTOIdentifierNumberBySequence()
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.orderDAO.pmsOrderDTOIdentifierNumberBySequence();
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public OrderDTO updateOrderDTO(OrderDTO orderDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			this.orderDAO.updateOrderDTO(orderDTO);
			this.orderItemDAO.deleteDiscardedOrderItemDTObyOrderDTO(orderDTO);
			long idItem = 0;
			for(OrderItemDTO vOrderItemDTO:orderDTO.getListOrderItemDTO()){
				if(vOrderItemDTO.getId() == null){
					vOrderItemDTO.setId_order(orderDTO.getId());
					vOrderItemDTO.setId(this.orderItemDAO.pmsOrderItemDTOIdBySequence(idItem++));
					this.orderItemDAO.insertOrderItemDTO(vOrderItemDTO);
				}else if(vOrderItemDTO.getStatus().equals("application.common.status.discarded")
						&& vOrderItemDTO.getPrevious_status()!=null){
					this.orderItemDAO.updateOrderItemDTO(vOrderItemDTO);
				}
			}
			return orderDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}	

	public void init(){
		this.persistenceErrorMessagesDecoder = new PersistenceErrorMessagesDecoder();
	}

	@Override
	public BigDecimal determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(
			OrderItemDTO orderItemDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.orderItemDAO.determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(orderItemDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public OrderRawMaterialSufficiencyReportDTO getOrderRawMaterialSufficiencyReportDTO(OrderDTO orderDTO) throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			OrderRawMaterialSufficiencyReportDTO vOrderRawMaterialSufficiencyReportDTO = this.orderDAO.getOrderRawMaterialSufficiencyReportDTO(orderDTO);
			vOrderRawMaterialSufficiencyReportDTO.setListOrderItemRawMaterialSufficiencyReportDTO(this.orderDAO.listOrderItemRawMaterialSufficiencyReportDTO(new OrderItemRawMaterialSufficiencyReportDTO(vOrderRawMaterialSufficiencyReportDTO.getId())));
			vOrderRawMaterialSufficiencyReportDTO.setListOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder(this.orderDAO.listOrderItemRawMaterialSufficiencyReportDetailDTOByIdOrder(vOrderRawMaterialSufficiencyReportDTO.getId_order()));
			return vOrderRawMaterialSufficiencyReportDTO;
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}

	@Override
	public BigDecimal convertCurrencyAmount(CurrencyDTO currencyDTO)
			throws PmsServiceException {
		// TODO Auto-generated method stub
		try{
			return this.currencyDAO.convertCurrencyAmount(currencyDTO);
		}catch(Exception e){
			logger.error("\ne.toString() : " + e.toString(), e);			
			throw new PmsServiceException
				(persistenceErrorMessagesDecoder.getDataBaseFunctionErrorKeyFromException(e),e,
				persistenceErrorMessagesDecoder.getDataBaseFunctionErrorParamsFromExceptionUsingLocale(e,this.bussinesSessionUtils.getRawSessionData().getUserSessionLocale()));
		}
	}
}
