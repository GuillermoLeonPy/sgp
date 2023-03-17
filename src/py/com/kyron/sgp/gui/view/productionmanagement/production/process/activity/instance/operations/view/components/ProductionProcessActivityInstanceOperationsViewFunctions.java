package py.com.kyron.sgp.gui.view.productionmanagement.production.process.activity.instance.operations.view.components;

import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.stockmanagement.domain.PAIRawMaterialSupplyDTO;

public interface ProductionProcessActivityInstanceOperationsViewFunctions {

	public void navigateToCallerView();
	public void effectuateRawMaterialDeparture() throws PmsServiceException;
	public void finalizeActivity() throws PmsServiceException;
	public void allocateHalfWayProductProductionProcessActivityInstanceDTO()throws PmsServiceException ;
	public void effectuatePartialProductRecall()throws PmsServiceException ;
	public void deliverFinalProduct()throws PmsServiceException ;
	public void queryListPAIRawMaterialSupplyPurchaseInvoiceAffectedDTO(final PAIRawMaterialSupplyDTO vPAIRawMaterialSupplyDTO);
}
