package py.com.kyron.sgp.gui.view;

import py.com.kyron.sgp.gui.view.application.report.management.ReportExcecutionManagementView;
import py.com.kyron.sgp.gui.view.application.report.management.ReportListManagementView;
import py.com.kyron.sgp.gui.view.applicationutilities.ApplicationUtilitiesView;
import py.com.kyron.sgp.gui.view.applicationutilities.HelpProgramManagementView;
import py.com.kyron.sgp.gui.view.cash.movements.management.PurchaseInvoiceManagementView;
import py.com.kyron.sgp.gui.view.cash.movements.management.PurchaseInvoiceRegisterFormView;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceManagementView;
import py.com.kyron.sgp.gui.view.cash.movements.management.SaleInvoiceRegisterFormView;
import py.com.kyron.sgp.gui.view.comercialmanagement.CustomerManagementView;
import py.com.kyron.sgp.gui.view.comercialmanagement.OrderManagementView;
import py.com.kyron.sgp.gui.view.comercialmanagement.OrderRegisterFormView;
import py.com.kyron.sgp.gui.view.comercialmanagement.ProductManagementView;
import py.com.kyron.sgp.gui.view.comercialmanagement.ProductRegisterFormView;
import py.com.kyron.sgp.gui.view.comercialmanagement.RegisterCurrencyFormView;
import py.com.kyron.sgp.gui.view.comercialmanagement.RegisterCurrencyView;
import py.com.kyron.sgp.gui.view.dashboard.DashboardView;
import py.com.kyron.sgp.gui.view.personmanagement.PersonManagementView;
import py.com.kyron.sgp.gui.view.personmanagement.PersonRegisterFormView;
import py.com.kyron.sgp.gui.view.personmanagement.RoleManagementView;
import py.com.kyron.sgp.gui.view.personmanagement.RoleRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.MachineManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.MachineRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.ManPowerCostManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.ManPowerCostRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.MeasurmentUnitManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.MeasurmentUnitRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.OrderRawMaterialSufficiencyReportView;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessActivitiesInstancesAssignmentManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessActivityInstanceOperationsView;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.ProductionProcessManagementOperationView;
import py.com.kyron.sgp.gui.view.productionmanagement.RawMaterialManagementView;
import py.com.kyron.sgp.gui.view.productionmanagement.RawMaterialRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessActivityRequirementRegisterFormView;
import py.com.kyron.sgp.gui.view.productionmanagement.production.process.administration.forms.ProductionProcessRegisterFormView;
import py.com.kyron.sgp.gui.view.reports.ReportsView;
import py.com.kyron.sgp.gui.view.sales.SalesView;
import py.com.kyron.sgp.gui.view.schedule.ScheduleView;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementView;
import py.com.kyron.sgp.gui.view.stockmanagement.SaleInvoiceProductDeliverablesFormView;
import py.com.kyron.sgp.gui.view.stockmanagement.SaleInvoiceProductDeliverablesManagementView;
import py.com.kyron.sgp.gui.view.stockmanagement.SupplierManagementView;
import py.com.kyron.sgp.gui.view.transactions.TransactionsView;

import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

public enum DashboardViewType {
	
	/************************/
	/*welcome view*/
	/************************/	
	WELCOME_VIEW("pms.welcome", WelcomeView.class,FontAwesome.HOME, false, "pms.application", false),
	
	/************************/
	/*application report management*/
	/************************/
	REPORT_LIST_MANAGEMENT("report.list.management", ReportListManagementView.class,FontAwesome.PIE_CHART, false, "module.application.report.management.", true),
	REPORT_EXCECUTION_MANAGEMENT("report.excecution.management", ReportExcecutionManagementView.class,FontAwesome.HOME, false, "module.application.report.management.", false),
	
	/************************/
	/*cash movements management*/
	/************************/
	SALE_INVOICE_MANAGEMENT("sale.invoice.management", SaleInvoiceManagementView.class,FontAwesome.SELLSY, false, "module.cash.movements.management.", true),
	SALE_INVOICE_REGISTER_FORM("sale.invoice.management.form", SaleInvoiceRegisterFormView.class,FontAwesome.HOME, false, "module.cash.movements.management.", false),
	PURCHASE_INVOICE_MANAGEMENT("purchase.invoice.management", PurchaseInvoiceManagementView.class,FontAwesome.BUYSELLADS, false, "module.cash.movements.management.", true),
	PURCHASE_INVOICE_REGISTER_FORM("purchase.invoice.management.form", PurchaseInvoiceRegisterFormView.class,FontAwesome.HOME, false, "module.cash.movements.management.", false),
	
	/************************/
	/*commercial management*/
	/************************/
	REGISTER_CURRENCY("register.currency", RegisterCurrencyView.class,FontAwesome.CALCULATOR, false, "module.comercial.management.", true),
	REGISTER_CURRENCY_FORM("register.currency.form", RegisterCurrencyFormView.class,FontAwesome.HOME, false, "module.comercial.management.", false),
	CUSTOMER_MANAGEMENT("customer.management", CustomerManagementView.class,FontAwesome.SHOPPING_BAG, false, "module.comercial.management.", true),
	PRODUCT_MANAGEMENT("product.management", ProductManagementView.class,FontAwesome.SHOPPING_BASKET, false, "module.comercial.management.", true),
	REGISTER_PRODUCT_FORM("product.management.form", ProductRegisterFormView.class,FontAwesome.HOME, false, "module.comercial.management.", false),
	ORDER_MANAGEMENT("order.management", OrderManagementView.class,FontAwesome.REORDER, false, "module.comercial.management.", true),
	REGISTER_ORDER_FORM("order.management.form", OrderRegisterFormView.class,FontAwesome.HOME, false, "module.comercial.management.", false),
	
	/************************/
	/*stock management*/
	/************************/
	SUPPLIER_MANAGEMENT("supplier.management", SupplierManagementView.class,FontAwesome.TRUCK, false, "module.stock.management.", true),
	RAW_MATERIAL_STOCK_MANAGEMENT("rawmaterial.stock.management", RawMaterialStockManagementView.class,FontAwesome.TABLE, false, "module.stock.management.", true),
	RAW_MATERIAL_STOCK_MANAGEMENT_FORM("rawmaterial.stock.management.form", RawMaterialStockManagementFormView.class,FontAwesome.HOME, false, "module.stock.management.", false),
	SALE_INVOICE_PRODUCT_DELIVERABLES_MANAGEMENT("sale.invoice.product.deliverables.management", SaleInvoiceProductDeliverablesManagementView.class,FontAwesome.GET_POCKET, false, "module.stock.management.", true),
	SALE_INVOICE_PRODUCT_DELIVERABLES_FORM("sale.invoice.product.deliverables.form", SaleInvoiceProductDeliverablesFormView.class,FontAwesome.HOME, false, "module.stock.management.", false),
	
	/************************/
	/*production management*/
	/************************/
	MACHINE_MANAGEMENT("machine.management", MachineManagementView.class,FontAwesome.GEARS, false, "module.producction.management.", true),
	REGISTER_MACHINE_FORM("machine.management.form", MachineRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),	
	RAW_MATERIAL_MANAGEMENT("rawmaterial.management", RawMaterialManagementView.class,FontAwesome.INDUSTRY, false, "module.producction.management.", true),
	REGISTER_RAW_MATERIAL_FORM("rawmaterial.management.form", RawMaterialRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	MEASURMENT_UNIT_MANAGEMENT("measurmentunit.management", MeasurmentUnitManagementView.class,FontAwesome.BALANCE_SCALE, false, "module.producction.management.", true),
	REGISTER_MEASURMENT_UNIT_FORM("measurmentunit.management.form", MeasurmentUnitRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	MAN_POWER_COST_MANAGEMENT("manpowercost.management", ManPowerCostManagementView.class,FontAwesome.BAR_CHART, false, "module.producction.management.", true),
	REGISTER_MAN_POWER_COST_FORM("manpowercost.management.form", ManPowerCostRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_MANAGEMENT("production.process.management", ProductionProcessManagementView.class,FontAwesome.PRODUCT_HUNT, false, "module.producction.management.", true),
	PRODUCTION_PROCESS_MANAGEMENT_OPERATION("production.process.management.operation", ProductionProcessManagementOperationView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_REGISTER_FORM("production.process.management.form", ProductionProcessRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_ACTIVITY_REGISTER_FORM("production.process.activity.management.form", ProductionProcessActivityRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_ACTIVITY_REQUIREMENT_REGISTER_FORM("production.process.activity.requirement.management.form", ProductionProcessActivityRequirementRegisterFormView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_ACTIVITY_INSTANCES_MANAGEMENT("production.process.activities.instances.assignment.management", ProductionProcessActivitiesInstancesAssignmentManagementView.class,FontAwesome.LINE_CHART, false, "module.producction.management.", true),
	ORDER_RAW_MATERIAL_SUFFICIENCY_REPORT_VIEW("order.raw.material.sufficiency.report.view", OrderRawMaterialSufficiencyReportView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	PRODUCTION_PROCESS_ACTIVITY_INSTANCES_OPERATIONS_VIEW("production.process.activity.instance.operations", ProductionProcessActivityInstanceOperationsView.class,FontAwesome.HOME, false, "module.producction.management.", false),
	
	
	/************************************/
	/*application credentials management*/
	/************************************/
	PERSON_MANAGEMENT("person.management", PersonManagementView.class,FontAwesome.USER_SECRET, false, "module.application.credentials.management.", true),
	REGISTER_PERSON_FORM("person.management.form", PersonRegisterFormView.class,FontAwesome.HOME, false, "module.application.credentials.management.", false),
	ROLE_MANAGEMENT("role.management", RoleManagementView.class,FontAwesome.USER_MD, false, "module.application.credentials.management.", true),
	REGISTER_ROLE_FORM("role.management.form", RoleRegisterFormView.class,FontAwesome.HOME, false, "module.application.credentials.management.", false),

	/************************/
	/*application utilities*/
	/************************/
	APPLICATION_UTILITIES("applicationtools", ApplicationUtilitiesView.class,FontAwesome.WRENCH, false, "module.application.utilities.", true),
	HELP_PROGRAM_MANAGEMENT("help.program", HelpProgramManagementView.class,FontAwesome.YELP, false, "module.application.utilities.", true),
	
	
	/************************************/
	/************************************/
    DASHBOARD("dashboard", DashboardView.class, FontAwesome.HOME, true, "quicktickets", true), 
    SALES("sales", SalesView.class, FontAwesome.BAR_CHART_O, false, "quicktickets",true), 
    TRANSACTIONS("transactions", TransactionsView.class, FontAwesome.TABLE, false, "module.producction.management.",true), 
    REPORTS("reports", ReportsView.class, FontAwesome.FILE_TEXT_O, true, "quicktickets",true), 
    SCHEDULE("schedule", ScheduleView.class, FontAwesome.CALENDAR_O, false, "quicktickets",true);


    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;

	private final String module;
	private boolean showInMainMenu;

    private DashboardViewType(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful, final String module, final boolean showInMainMenu) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.module = module;
        this.showInMainMenu = showInMainMenu;
    }

    public boolean isStateful() {
        return stateful;
    }

    public String getModule() {
		return module;
	}
    
    public boolean isShowInMainMenu() {
		return showInMainMenu;
	}

	public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static DashboardViewType getByViewName(final String viewName) {
        DashboardViewType result = null;
        for (DashboardViewType viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }

}
