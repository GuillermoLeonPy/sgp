package py.com.kyron.sgp.bussines.comercialmanagement.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CreditOrderChargeConditionDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.OrderRawMaterialSufficiencyReportDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;

public interface ComercialManagementService {
	/* CURRENCY METHODS*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CurrencyDTO registrarMoneda(CurrencyDTO currencyDTO) throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CurrencyDTO updateCurrency(CurrencyDTO currencyDTO) throws PmsServiceException;
	/*spring-framework-reference.pdf, pag:381
	 * In proxy mode (which is the default), only external method calls coming in through the proxy are
	intercepted. This means that self-invocation, in effect, a method within the target object calling
	another method of the target object, will not lead to an actual transaction at runtime even if the
	invoked method is marked with @Transactional
	 */
	public List<CurrencyDTO> listCurrencyDTO(CurrencyDTO currencyDTO)throws PmsServiceException;
	//
	public BussinesSessionUtils getBussinesSessionUtils();
	
	/* PRODUCT METHODS*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public ProductDTO insertProductDTO(ProductDTO productDTO)throws PmsServiceException;	
	public List<ProductDTO> listProductDTO(ProductDTO productDTO)throws PmsServiceException;
	
	
	/* currency exchange rate methods*/
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CurrencyExchangeRateDTO insertCurrencyExchangeRateDTO(CurrencyExchangeRateDTO currencyExchangeRateDTO)throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CurrencyExchangeRateDTO updateCurrencyExchangeRateDTO(CurrencyExchangeRateDTO currencyExchangeRateDTO)throws PmsServiceException;
	public List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO(CurrencyExchangeRateDTO currencyExchangeRateDTO)throws PmsServiceException;
	public Long getCurrencyExchangeRateValidRowId(Long id_currency)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public OrderDTO insertOrderDTO(OrderDTO orderDTO)throws PmsServiceException;
	public List<OrderDTO> listOrderDTO(OrderDTO orderDTO)throws PmsServiceException;
	
	public BigDecimal determinateProductPriceByProductIdCurrencyId(OrderItemDTO orderItemDTO)throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public BigDecimal determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(OrderItemDTO orderItemDTO)throws PmsServiceException;
	public Long pmsOrderDTOIdentifierNumberBySequence()throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public OrderDTO updateOrderDTO(OrderDTO orderDTO)throws PmsServiceException;
	
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CreditOrderChargeConditionDTO insertCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)throws PmsServiceException;
	@Transactional(value="transactionManager", propagation=Propagation.REQUIRED, isolation=Isolation.SERIALIZABLE, rollbackFor={PmsServiceException.class})/*crear PmsServiceException e indicar aqui*/
	public CreditOrderChargeConditionDTO updateCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)throws PmsServiceException;
	public List<CreditOrderChargeConditionDTO> listCreditOrderChargeConditionDTO(CreditOrderChargeConditionDTO creditOrderChargeConditionDTO)throws PmsServiceException;
	
	public Long getCreditOrderChargeConditionValidRowId()throws PmsServiceException ;
	
	public OrderRawMaterialSufficiencyReportDTO getOrderRawMaterialSufficiencyReportDTO(OrderDTO orderDTO)throws PmsServiceException;
	
	public BigDecimal convertCurrencyAmount(CurrencyDTO currencyDTO)throws PmsServiceException;
}
