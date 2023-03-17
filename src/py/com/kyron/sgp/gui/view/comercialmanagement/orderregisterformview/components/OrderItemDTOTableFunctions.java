package py.com.kyron.sgp.gui.view.comercialmanagement.orderregisterformview.components;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderItemDTO;

public interface OrderItemDTOTableFunctions {
	public void deleteOrderItemFromPreliminaryList(final OrderItemDTO vOrderItemDTO);
	public String getOrderDTOStatus();
	public void reSetLayoutAfterAnItemHasBeenAdded();
}
