package py.com.kyron.sgp.gui.view.comercialmanagement.registercurrencyformview.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyExchangeRateDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.SgpUtils;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.comercialmanagement.RegisterCurrencyFormView;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CurrencyExchangeRateTableTabLayout extends VerticalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeRateTableTabLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "register.currency.form.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private Table currencyExchangeRateTable;
    private static final String[] DEFAULT_COLLAPSIBLE = {"g_registration_date","g_validity_end_date"};
    private ComercialManagementService gestionComercialService;
    private BussinesSessionUtils bussinesSessionUtils;
    private CommonExceptionErrorNotification commonExceptionErrorNotification;
    private Label isValidLabel = null;
    private List<CurrencyExchangeRateDTO> listCurrencyExchangeRateDTO;
	private RegisterCurrencyFormView registerCurrencyFormView;
	private CurrencyDTO currencyDTO;
	
	public CurrencyExchangeRateTableTabLayout(RegisterCurrencyFormView vRegisterCurrencyFormView,CurrencyDTO currencyDTO) {
		try{			
			logger.info("\n CurrencyExchangeRateTableTabLayout()...");
			this.registerCurrencyFormView = vRegisterCurrencyFormView;
			this.currencyDTO = currencyDTO;
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
	        setSizeFull();
	        addStyleName("transactions");
	        this.setId("CurrencyExchangeRateTableTabLayout");
			this.setSpacing(true);
			this.setMargin(true);
	        Responsive.makeResponsive(this);
	        this.buildcurrencyExchangeRateTable();
	        addComponent(currencyExchangeRateTable);
	        setExpandRatio(currencyExchangeRateTable, 1);
	        this.isValidLabel = new Label(this.messages.get("application.common.validity.end.date.is.valid.value"));
	        this.isValidLabel.addStyleName("colored");
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public CurrencyExchangeRateTableTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}
	
	private void initServices() throws Exception{
		this.gestionComercialService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}

	private void buildcurrencyExchangeRateTable() throws PmsServiceException{
    	this.currencyExchangeRateTable = new Table();
    	BeanItemContainer<CurrencyExchangeRateDTO> currencyExchangeRateDTOBeanItemContainer	= new BeanItemContainer<CurrencyExchangeRateDTO>(CurrencyExchangeRateDTO.class);
    	this.updateListCurrencyExchangeRateDTO();
    	currencyExchangeRateDTOBeanItemContainer.addAll(this.listCurrencyExchangeRateDTO);    	
    	this.currencyExchangeRateTable.setContainerDataSource(currencyExchangeRateDTOBeanItemContainer); 
    	
    	this.currencyExchangeRateTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CurrencyExchangeRateDTO vCurrencyExchangeRateDTO = (CurrencyExchangeRateDTO)itemId;
				final Button editButton = new Button();
				editButton.setDescription(messages.get("application.common.table.column.operations.apply.validity.end.date.button.description"));
				editButton.setIcon(FontAwesome.EDIT);
				editButton.addStyleName("borderless");
				editButton.addClickListener(new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		                logger.info("\n editar CurrencyExchangeRateDTO...\n" + vCurrencyExchangeRateDTO.toString());
		                try{		                	
		                	registerCurrencyFormView.editCurrencyExchangeRate(vCurrencyExchangeRateDTO);
		                }catch(Exception e){
		                	commonExceptionErrorNotification.showErrorMessageNotification(e);
		                }			                
		            }
		        });
				
				try {
					editButton.setEnabled(vCurrencyExchangeRateDTO.getValidity_end_date()==null
					&& bussinesSessionUtils.getRawSessionData().getProgramKeysBySession()
					.contains(SECURED_PROGRAM_PREFIX + VIEW_NAME + "tab.exchangerate.register.form"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("\nerror",e);
				}
				return editButton;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//currencyExchangeRateTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.currencyExchangeRateTable.addGeneratedColumn("g_local_currency_id_code", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CurrencyExchangeRateDTO vCurrencyExchangeRateDTO = (CurrencyExchangeRateDTO)itemId;					
				return vCurrencyExchangeRateDTO.getLocalCurrencyDTO().getDescription();
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//currencyExchangeRateTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.currencyExchangeRateTable.addGeneratedColumn("g_registration_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CurrencyExchangeRateDTO vCurrencyExchangeRateDTO = (CurrencyExchangeRateDTO)itemId;					
				return SgpUtils.dateFormater.format(vCurrencyExchangeRateDTO.getRegistration_date());
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//currencyExchangeRateTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.currencyExchangeRateTable.addGeneratedColumn("g_validity_end_date", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final CurrencyExchangeRateDTO vCurrencyExchangeRateDTO = (CurrencyExchangeRateDTO)itemId;
				return (vCurrencyExchangeRateDTO.getValidity_end_date()!=null ? 
						SgpUtils.dateFormater.format(vCurrencyExchangeRateDTO.getValidity_end_date()):isValidLabel);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//currencyExchangeRateTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.currencyExchangeRateTable.setVisibleColumns(new Object[] {"operations","g_local_currency_id_code","amount","g_registration_date","g_validity_end_date"});
    	this.currencyExchangeRateTable.setColumnHeader("operations", this.messages.get("application.common.table.operations.column.label"));
    	this.currencyExchangeRateTable.setColumnAlignment("operations", Table.Align.LEFT);
    	
    	this.currencyExchangeRateTable.setColumnHeader("g_local_currency_id_code", this.messages.get("application.common.local.currency.label"));
    	this.currencyExchangeRateTable.setColumnAlignment("amount", Table.Align.LEFT);
    	
    	this.currencyExchangeRateTable.setColumnHeader("amount", this.messages.get("application.common.table.column.any.amount.label"));
    	this.currencyExchangeRateTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
    	
    	this.currencyExchangeRateTable.setColumnHeader("g_registration_date", this.messages.get("application.common.table.column.registration.date.label"));
    	this.currencyExchangeRateTable.setColumnAlignment("g_validity_end_date", Table.Align.LEFT);
    	
    	this.currencyExchangeRateTable.setColumnHeader("g_validity_end_date", this.messages.get("application.common.table.column.validity.end.date.label"));
    	this.currencyExchangeRateTable.setColumnAlignment("g_registration_date", Table.Align.LEFT);
    	this.currencyExchangeRateTable.setSizeFull();
    	this.currencyExchangeRateTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.currencyExchangeRateTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.currencyExchangeRateTable.addStyleName(ValoTheme.TABLE_SMALL);    
    	this.currencyExchangeRateTable.setColumnExpandRatio("operations", 0.009f); 
    	this.currencyExchangeRateTable.setColumnExpandRatio("g_local_currency_id_code", 0.025f);    	
    	this.currencyExchangeRateTable.setColumnExpandRatio("amount", 0.015f);
    	this.currencyExchangeRateTable.setColumnExpandRatio("g_registration_date", 0.018f);
    	this.currencyExchangeRateTable.setColumnExpandRatio("g_validity_end_date", 0.018f);
    	this.currencyExchangeRateTable.setSelectable(true);
    	this.currencyExchangeRateTable.setColumnCollapsingAllowed(true);
    	this.currencyExchangeRateTable.setColumnCollapsible("operations", false);
    	this.currencyExchangeRateTable.setColumnCollapsible("g_registration_date", true);
    	this.currencyExchangeRateTable.setColumnCollapsible("g_validity_end_date", true);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.currencyExchangeRateTable.setSortContainerPropertyId("measurment_unit_id");
    	//this.currencyExchangeRateTable.setSortAscending(false);
    	this.currencyExchangeRateTable.setColumnReorderingAllowed(true);
    	this.currencyExchangeRateTable.setFooterVisible(true);
    	this.currencyExchangeRateTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
	}
	
    private void updateListCurrencyExchangeRateDTO() throws PmsServiceException{
    	if(this.currencyDTO.getId()!=null)
    		this.listCurrencyExchangeRateDTO = this.gestionComercialService.listCurrencyExchangeRateDTO(new CurrencyExchangeRateDTO(this.currencyDTO.getId(), null));
    	if(this.listCurrencyExchangeRateDTO == null) this.listCurrencyExchangeRateDTO = new ArrayList<CurrencyExchangeRateDTO>();
    }
}
