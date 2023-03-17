package py.com.kyron.sgp.gui.view.comercialmanagement.ordermanagementview.components;

import java.util.List;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;

public interface SearchOrderToolComponentHostView {

	public void buildOrderDTOTable(final List<OrderDTO> listOrderDTO) throws Exception;
}
