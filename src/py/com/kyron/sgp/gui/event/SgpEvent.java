package py.com.kyron.sgp.gui.event;

import py.com.kyron.sgp.bussines.applicationsecurity.domain.ApplicationSecurityRolDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.PurchaseInvoiceDTO;
import py.com.kyron.sgp.bussines.cash.movements.management.domain.SaleInvoiceDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.OrderDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.ProductDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerCostDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ManPowerRequirementDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MeasurmentUnitDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessActivityInstanceDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.ProductionProcessDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialRequirementDTO;
import py.com.kyron.sgp.bussines.stockmanagement.domain.SaleInvoiceProductDeliverablesDTO;
import py.com.kyron.sgp.gui.view.application.report.management.report.list.management.view.components.ReportProgramListLayout.ReportProgram;

public abstract class SgpEvent {

	public SgpEvent() {
		// TODO Auto-generated constructor stub
	}

	public static final class RegisterCurrencyViewEvent{
    	private final CurrencyDTO moneda;
    	
    	public RegisterCurrencyViewEvent(final CurrencyDTO moneda){
    		this.moneda = moneda;
    	}
    	
    	public CurrencyDTO getMoneda(){
    		return this.moneda;
    	}
	}
	
	public static final class PersonRegisterFormViewEvent{
    	private final PersonDTO personDTO;
    	private final String callerView;
    	private final boolean massiveInsertMode;
    	private final boolean insertInSupplierMode;
    	private final boolean insertInCustomerMode;
    	private final boolean insertInFuntionaryMode;
    	
    	public PersonRegisterFormViewEvent(final PersonDTO personDTO, String callerView, boolean massiveInsertMode, 
    			boolean insertInSupplierMode, boolean insertInCustomerMode, boolean insertInFuntionaryMode){
    		this.personDTO = personDTO;
    		this.callerView = callerView;
    		this.massiveInsertMode = massiveInsertMode;
    		this.insertInSupplierMode = insertInSupplierMode;
    		this.insertInCustomerMode = insertInCustomerMode;
    		this.insertInFuntionaryMode = insertInFuntionaryMode;
    	}

		public PersonDTO getPersonDTO() {
			return personDTO;
		}

		public String getCallerView() {
			return callerView;
		}

		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public boolean isInsertInSupplierMode() {
			return insertInSupplierMode;
		}

		public boolean isInsertInCustomerMode() {
			return insertInCustomerMode;
		}

		public boolean isInsertInFuntionaryMode() {
			return insertInFuntionaryMode;
		}
	}//public static final class PersonRegisterFormViewEvent{
	
	public static final class RoleAdministrationViewEvent{
		private final ApplicationSecurityRolDTO applicationSecurityRolDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param applicationSecurityRolDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public RoleAdministrationViewEvent(final ApplicationSecurityRolDTO applicationSecurityRolDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.applicationSecurityRolDTO = applicationSecurityRolDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}
		public ApplicationSecurityRolDTO getApplicationSecurityRolDTO() {
			return applicationSecurityRolDTO;
		}
		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}
		
	}//public static final class RoleAdministrationView{
	
	public static final class MachineRegisterFormViewEvent{
		private final MachineDTO machineDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param machineDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public MachineRegisterFormViewEvent(final MachineDTO machineDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.machineDTO = machineDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public MachineDTO getMachineDTO() {
			return machineDTO;
		}
		
	}//public static final class RoleAdministrationView{
	
	public static final class ProductRegisterFormViewEvent{
		private final ProductDTO productDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param productDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public ProductRegisterFormViewEvent(final ProductDTO productDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.productDTO = productDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public ProductDTO getProductDTO() {
			return productDTO;
		}
		
	}//public static final class RoleAdministrationView{
	
	public static final class RawMaterialRegisterFormViewEvent{
		private final RawMaterialDTO rawMaterialDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param rawMaterialDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public RawMaterialRegisterFormViewEvent(final RawMaterialDTO rawMaterialDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.rawMaterialDTO = rawMaterialDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public RawMaterialDTO getRawMaterialDTO() {
			return rawMaterialDTO;
		}		
	}//public static final class RoleAdministrationView{
	
	public static final class MeasurmentUnitRegisterFormViewEvent{
		private final MeasurmentUnitDTO measurmentUnitDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param measurmentUnitDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public MeasurmentUnitRegisterFormViewEvent(final MeasurmentUnitDTO measurmentUnitDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.measurmentUnitDTO = measurmentUnitDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public MeasurmentUnitDTO getMeasurmentUnitDTO() {
			return measurmentUnitDTO;
		}
	
	}//public static final class RoleAdministrationView{
	
	public static final class ManPowerCostRegisterFormViewEvent{
		private final ManPowerCostDTO manPowerCostDTO;
		private final String callerView;
		private final boolean massiveInsertMode;
		/**
		 * @param manPowerCostDTO
		 * @param callerView
		 * @param massiveInsertMode
		 */
		public ManPowerCostRegisterFormViewEvent(final ManPowerCostDTO manPowerCostDTO,
				final String callerView, final boolean massiveInsertMode) {
			super();
			this.manPowerCostDTO = manPowerCostDTO;
			this.callerView = callerView;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}

		public ManPowerCostDTO getManPowerCostDTO() {
			return manPowerCostDTO;
		}	
	}//public static final class RoleAdministrationView{
	
	public static final class RawMaterialStockManagementFormViewEvent{
		private final RawMaterialDTO rawMaterialDTO;
		private final String callerView;
		/**
		 * @param rawMaterialDTO
		 * @param callerView
		 */
		public RawMaterialStockManagementFormViewEvent(final RawMaterialDTO rawMaterialDTO,
				final String callerView) {
			super();
			this.rawMaterialDTO = rawMaterialDTO;
			this.callerView = callerView;
		}
		public RawMaterialDTO getRawMaterialDTO() {
			return rawMaterialDTO;
		}
		public String getCallerView() {
			return callerView;
		}
	}//public static final class RoleAdministrationView{
	
	public static final class ProductionProcessManagementOperationViewEvent{
		private final ProductDTO productDTO;
		private final String callerView;
		/**
		 * @param productDTO
		 * @param callerView
		 */
		public ProductionProcessManagementOperationViewEvent(final ProductDTO productDTO, final String callerView) {
			super();
			this.productDTO = productDTO;
			this.callerView = callerView;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the productDTO
		 */
		public ProductDTO getProductDTO() {
			return productDTO;
		}
	}//public static final class RoleAdministrationView{
	
	public static final class ProductionProcessRegisterFormViewEvent{
		private final ProductDTO productDTO;
		private final ProductionProcessDTO productionProcessDTO;
		private final String callerView;
		private final boolean editFormMode;
		/**
		 * @param productDTO
		 * @param productionProcessDTO
		 * @param callerView
		 * @param editFormMode
		 */
		public ProductionProcessRegisterFormViewEvent(final ProductDTO productDTO, final ProductionProcessDTO productionProcessDTO, final String callerView, final boolean editFormMode) {
			super();
			this.productDTO = productDTO;
			this.productionProcessDTO = productionProcessDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the productionProcessDTO
		 */
		public ProductionProcessDTO getProductionProcessDTO() {
			return productionProcessDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}

		/**
		 * @return the productDTO
		 */
		public ProductDTO getProductDTO() {
			return productDTO;
		}
	}//public static final class RoleAdministrationView{
	
	public static final class ProductionProcessActivityRegisterFormViewEvent{
		private final ProductDTO productDTO;
		private final ProductionProcessDTO productionProcessDTO;
		private final ProductionProcessActivityDTO productionProcessActivityDTO;
		private final String callerView;
		private final boolean editFormMode;
		/**
		 * @param productDTO
		 * @param productionProcessDTO
		 * @param productionProcessActivityDTO
		 * @param callerView
		 * @param editFormMode
		 */
		public ProductionProcessActivityRegisterFormViewEvent(final ProductDTO productDTO, final ProductionProcessDTO productionProcessDTO, final ProductionProcessActivityDTO productionProcessActivityDTO,final String callerView, final boolean editFormMode) {
			super();
			this.productDTO = productDTO;
			this.productionProcessDTO = productionProcessDTO;
			this.productionProcessActivityDTO = productionProcessActivityDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the productionProcessDTO
		 */
		public ProductionProcessDTO getProductionProcessDTO() {
			return productionProcessDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}

		/**
		 * @return the productDTO
		 */
		public ProductDTO getProductDTO() {
			return productDTO;
		}

		/**
		 * @return the productionProcessActivityDTO
		 */
		public ProductionProcessActivityDTO getProductionProcessActivityDTO() {
			return productionProcessActivityDTO;
		}
	}//public static final class RoleAdministrationView{
	
	public static final class ProductionProcessActivityRequirementRegisterFormViewEvent{
		private final ProductDTO productDTO;
		private final ProductionProcessDTO productionProcessDTO;
		private final ProductionProcessActivityDTO productionProcessActivityDTO;
		private final RawMaterialRequirementDTO rawMaterialRequirementDTO;
		private final MachineRequirementDTO machineRequirementDTO;
		private final ManPowerRequirementDTO manPowerRequirementDTO;
		private final String callerView;
		private final boolean editFormMode;
		
		/**
		 * @param productDTO
		 * @param productionProcessDTO
		 * @param productionProcessActivityDTO
		 * @param rawMaterialRequirementDTO
		 * @param machineRequirementDTO
		 * @param manPowerRequirementDTO
		 * @param callerView
		 * @param editFormMode
		 */
		
		public ProductionProcessActivityRequirementRegisterFormViewEvent(
				final ProductDTO productDTO,
				final ProductionProcessDTO productionProcessDTO, 
				final ProductionProcessActivityDTO productionProcessActivityDTO,
				final RawMaterialRequirementDTO rawMaterialRequirementDTO,
				final MachineRequirementDTO machineRequirementDTO,
				final ManPowerRequirementDTO manPowerRequirementDTO,
				final String callerView, final boolean editFormMode) {
			super();
			this.productDTO = productDTO;
			this.productionProcessDTO = productionProcessDTO;
			this.productionProcessActivityDTO = productionProcessActivityDTO;
			this.rawMaterialRequirementDTO = rawMaterialRequirementDTO;
			this.machineRequirementDTO = machineRequirementDTO;
			this.manPowerRequirementDTO = manPowerRequirementDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the productionProcessDTO
		 */
		public ProductionProcessDTO getProductionProcessDTO() {
			return productionProcessDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}
		
		/**
		 * @return the productionProcessActivityDTO
		 */
		public ProductionProcessActivityDTO getProductionProcessActivityDTO() {
			return productionProcessActivityDTO;
		}

		/**
		 * @return the rawMaterialRequirementDTO
		 */
		public RawMaterialRequirementDTO getRawMaterialRequirementDTO() {
			return rawMaterialRequirementDTO;
		}

		/**
		 * @return the machineRequirementDTO
		 */
		public MachineRequirementDTO getMachineRequirementDTO() {
			return machineRequirementDTO;
		}

		/**
		 * @return the manPowerRequirementDTO
		 */
		public ManPowerRequirementDTO getManPowerRequirementDTO() {
			return manPowerRequirementDTO;
		}

		/**
		 * @return the productDTO
		 */
		public ProductDTO getProductDTO() {
			return productDTO;
		}
	}//public static final class RoleAdministrationView{
	
	public static final class OrderRegisterFormViewEvent{
		private final OrderDTO orderDTO;
		private final String callerView;
		private final boolean editFormMode;
		private final boolean massiveInsertMode;
		/**
		 * @param orderDTO
		 * @param callerView
		 * @param editFormMode
		 * @param massiveInsertMode
		 */
		public OrderRegisterFormViewEvent(final OrderDTO orderDTO, final String callerView,final boolean editFormMode,final boolean massiveInsertMode) {
			super();
			this.orderDTO = orderDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
			this.massiveInsertMode = massiveInsertMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the orderDTO
		 */
		public OrderDTO getOrderDTO() {
			return orderDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}

		/**
		 * @return the massiveInsertMode
		 */
		public boolean isMassiveInsertMode() {
			return massiveInsertMode;
		}
	}
	
	public static final class SaleInvoiceRegisterFormViewEvent{
		private final OrderDTO orderDTO;
		private final SaleInvoiceDTO saleInvoiceDTO;
		private final String callerView;
		private final boolean editFormMode;
		/**
		 * @param orderDTO
		 * @param saleInvoiceDTO
		 * @param callerView
		 * @param editFormMode
		 * @param massiveInsertMode
		 */
		public SaleInvoiceRegisterFormViewEvent(
				final OrderDTO orderDTO,
				final SaleInvoiceDTO saleInvoiceDTO,
				final String callerView,
				final boolean editFormMode) {
			super();
			this.orderDTO = orderDTO;
			this.saleInvoiceDTO = saleInvoiceDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the orderDTO
		 */
		public OrderDTO getOrderDTO() {
			return orderDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}

		/**
		 * @return the saleInvoiceDTO
		 */
		public SaleInvoiceDTO getSaleInvoiceDTO() {
			return saleInvoiceDTO;
		}
	}
	
	public static final class OrderRawMaterialSufficiencyReportViewEvent{
		private final OrderDTO orderDTO;
		private final String callerView;
		/**
		 * @param orderDTO
		 * @param callerView
		 */
		public OrderRawMaterialSufficiencyReportViewEvent(
				final OrderDTO orderDTO,
				final String callerView) {
			super();
			this.orderDTO = orderDTO;
			this.callerView = callerView;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the orderDTO
		 */
		public OrderDTO getOrderDTO() {
			return orderDTO;
		}

	}
	
	public static final class ProductionProcessActivityInstanceOperationsViewEvent{
		private final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO;
		private final String callerView;
		private final boolean operationPartialProductRecall;
		private final boolean operationRawMaterialSupply;
		private final boolean operationFinalizeActivity;
		private final boolean operationDeliversPartialResult;
		private final boolean operationDeliversFinalProduct;
		
		public ProductionProcessActivityInstanceOperationsViewEvent(
				final String callerView,
				final ProductionProcessActivityInstanceDTO productionProcessActivityInstanceDTO,
				final boolean operationPartialProductRecall,
				final boolean operationRawMaterialSupply,
				final boolean operationFinalizeActivity,
				final boolean operationDeliversPartialResult,
				final boolean operationDeliversFinalProduct				
				){
			super();
			this.callerView = callerView;
			this.productionProcessActivityInstanceDTO = productionProcessActivityInstanceDTO;
			this.operationPartialProductRecall = operationPartialProductRecall;
			this.operationRawMaterialSupply = operationRawMaterialSupply;
			this.operationFinalizeActivity = operationFinalizeActivity;
			this.operationDeliversPartialResult = operationDeliversPartialResult;
			this.operationDeliversFinalProduct = operationDeliversFinalProduct;
		}

		/**
		 * @return the productionProcessActivityInstanceDTO
		 */
		public ProductionProcessActivityInstanceDTO getProductionProcessActivityInstanceDTO() {
			return productionProcessActivityInstanceDTO;
		}

		/**
		 * @return the callerView
		 */
		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the operationPartialProductRecall
		 */
		public boolean isOperationPartialProductRecall() {
			return operationPartialProductRecall;
		}

		/**
		 * @return the operationRawMaterialSupply
		 */
		public boolean isOperationRawMaterialSupply() {
			return operationRawMaterialSupply;
		}

		/**
		 * @return the operationFinalizeActivity
		 */
		public boolean isOperationFinalizeActivity() {
			return operationFinalizeActivity;
		}

		/**
		 * @return the operationDeliversPartialResult
		 */
		public boolean isOperationDeliversPartialResult() {
			return operationDeliversPartialResult;
		}

		/**
		 * @return the operationDeliversFinalProduct
		 */
		public boolean isOperationDeliversFinalProduct() {
			return operationDeliversFinalProduct;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ProductionProcessActivityInstanceOperationsViewEvent [productionProcessActivityInstanceDTO="
					+ productionProcessActivityInstanceDTO
					+ ", callerView="
					+ callerView
					+ ", operationPartialProductRecall="
					+ operationPartialProductRecall
					+ ", operationRawMaterialSupply="
					+ operationRawMaterialSupply
					+ ", operationFinalizeActivity="
					+ operationFinalizeActivity
					+ ", operationDeliversPartialResult="
					+ operationDeliversPartialResult
					+ ", operationDeliversFinalProduct="
					+ operationDeliversFinalProduct
					+ ", getClass()="
					+ getClass()
					+ ", hashCode()="
					+ hashCode()
					+ ", toString()=" + super.toString() + "]";
		}		
	}
	
	public static final class PurchaseInvoiceRegisterFormViewEvent{
		private final PurchaseInvoiceDTO purchaseInvoiceDTO;
		private final String callerView;
		private final boolean editFormMode;
		/**
		 * @param purchaseInvoiceDTO
		 * @param callerView
		 * @param editFormMode
		 */
		public PurchaseInvoiceRegisterFormViewEvent(
				final PurchaseInvoiceDTO purchaseInvoiceDTO,
				final String callerView,
				final boolean editFormMode) {
			super();
			this.purchaseInvoiceDTO = purchaseInvoiceDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the purchaseInvoiceDTO
		 */
		public PurchaseInvoiceDTO getPurchaseInvoiceDTO() {
			return purchaseInvoiceDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}
	}
	
	public static final class SaleInvoiceProductDeliverablesFormViewEvent{
		private final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO;
		private final String callerView;
		private final boolean editFormMode;
		private final boolean queryFormMode;
		/**
		 * @param saleInvoiceProductDeliverablesDTO
		 * @param callerView
		 * @param editFormMode
		 * @param queryFormMode
		 */
		public SaleInvoiceProductDeliverablesFormViewEvent(
				final SaleInvoiceProductDeliverablesDTO saleInvoiceProductDeliverablesDTO,
				final String callerView,
				final boolean editFormMode,
				final boolean queryFormMode) {
			super();
			this.saleInvoiceProductDeliverablesDTO = saleInvoiceProductDeliverablesDTO;
			this.callerView = callerView;
			this.editFormMode = editFormMode;
			this.queryFormMode = queryFormMode;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the saleInvoiceProductDeliverablesDTO
		 */
		public SaleInvoiceProductDeliverablesDTO getSaleInvoiceProductDeliverablesDTO() {
			return saleInvoiceProductDeliverablesDTO;
		}

		/**
		 * @return the editFormMode
		 */
		public boolean isEditFormMode() {
			return editFormMode;
		}

		/**
		 * @return the queryFormMode
		 */
		public boolean isQueryFormMode() {
			return queryFormMode;
		}
	}
	
	public static final class ReportExcecutionManagementViewEvent{
		private final ReportProgram reportProgram;
		private final String callerView;
		/**
		 * @param reportProgram
		 * @param callerView
		 */
		public ReportExcecutionManagementViewEvent(
				final ReportProgram reportProgram,
				final String callerView) {
			super();
			this.reportProgram = reportProgram;
			this.callerView = callerView;
		}

		public String getCallerView() {
			return callerView;
		}

		/**
		 * @return the reportProgram
		 */
		public ReportProgram getReportProgram() {
			return reportProgram;
		}
	}
}
