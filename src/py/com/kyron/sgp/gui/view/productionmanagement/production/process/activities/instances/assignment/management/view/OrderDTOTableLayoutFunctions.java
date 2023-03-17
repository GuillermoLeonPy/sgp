package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activities.instances.assignment.management.view;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;

public interface OrderDTOTableLayoutFunctions {

	public void goToRegisterOrderView(OrderDTO vOrderDTO);
	public void instantiateProductionProcessActivities(OrderDTO vOrderDTO)throws PmsServiceException;
	public void queryOrderRawMaterialSufficiencyReportDTO(final OrderDTO vOrderDTO);
}
