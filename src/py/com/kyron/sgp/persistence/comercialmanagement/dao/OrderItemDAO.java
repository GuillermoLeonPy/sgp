package py.com.kyron.sgp.persistence.comercialmanagement.dao;

import java.math.BigDecimal;
import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;

public interface OrderItemDAO {

	public Long pmsOrderItemDTOIdBySequence(long id);
	public void insertOrderItemDTO(OrderItemDTO OrderItemDTO);
	public void updateOrderItemDTO(OrderItemDTO OrderItemDTO);
	public List<OrderItemDTO> listOrderItemDTO(OrderItemDTO OrderItemDTO);
	public BigDecimal determinateProductPriceByProductIdCurrencyId(OrderItemDTO OrderItemDTO);
	public BigDecimal determinateProductPriceByProductIdCurrencyIdIdentifyingOrder(OrderItemDTO OrderItemDTO);
	public void deleteDiscardedOrderItemDTObyOrderDTO(OrderDTO OrderDTO);
}
