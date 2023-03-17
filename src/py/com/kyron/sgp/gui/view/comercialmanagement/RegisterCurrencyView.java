/**
 * 
 */
package py.com.kyron.sgp.gui.view.comercialmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.comercialmanagement.service.ComercialManagementService;
import py.com.kyron.sgp.bussines.exception.PmsServiceException;
import py.com.kyron.sgp.bussines.productionmanagement.domain.MachineDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.BrowserResizeEvent;
import py.com.kyron.sgp.gui.event.SgpEvent.RegisterCurrencyViewEvent;
import py.com.kyron.sgp.gui.view.DashboardViewType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import py.com.kyron.sgp.gui.event.DashboardEvent.EditMonedaEvent;

/**
 * @author testuser
 *
 */
@SuppressWarnings({ "serial" })
public class RegisterCurrencyView extends VerticalLayout implements View {

	private final Logger logger = LoggerFactory.getLogger(RegisterCurrencyView.class);
	private Table currencyTable = null;
    private static final String[] DEFAULT_COLLAPSIBLE = {"description"};
	private Map<String,String> messages;
	private final String VIEW_NAME = "register.currency.";
	private ComercialManagementService gestionComercialService;

	public RegisterCurrencyView() {
		super();
		// TODO Auto-generated constructor stub
		try{				
				logger.info("\n RegistrarMonedaView()...");
				this.initServices();
				this.prepareMessages();
		        setSizeFull();
		        addStyleName("transactions");
		        DashboardEventBus.register(this);
		        buildCurrenciesTable();
		        addComponent(buildToolbar());//toolbar: buildFilter() requiere tabla inicializada
		        addComponent(currencyTable);
		        setExpandRatio(currencyTable, 1);
		        
		}catch(Exception e){
				logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
				logger.error(e.getMessage(), e);
				addComponent(new Label(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null)));
		}
		
	}//public RegistrarMonedaView()
	
	private void initServices() throws Exception{
		this.gestionComercialService = (ComercialManagementService)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.COMMERCIAL_MANAGEMENT_SERVICE);
	}//private void initServices() throws Exception
	

	
    @Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	 private Component buildToolbar() {
	        HorizontalLayout header = new HorizontalLayout();
	        header.addStyleName("viewheader");
	        header.setSpacing(true);
	        Responsive.makeResponsive(header);

	        Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));/*"Registrar moneda"*/
	        title.setSizeUndefined();
	        title.addStyleName(ValoTheme.LABEL_H1);
	        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	        header.addComponent(title);
	        
	        HorizontalLayout tools = new HorizontalLayout(buildFilter(),createRegisterCurrencyButton());	        	        
	        tools.setSpacing(true);
	        tools.addStyleName("toolbar");
	        header.addComponent(tools);
	        return header;
	 }//private Component buildToolbar()
	 
	 private Button createRegisterCurrencyButton(){
	        final Button registerCurrency = new Button(this.messages.get(this.VIEW_NAME + "button.register.currency"));/*"Registrar moneda"*/
	        registerCurrency.setDescription(this.messages.get(this.VIEW_NAME + "button.register.currency.description"));/*"Una moneda es referenciada para definir cuantitativamente montos de divisas"*/
	        registerCurrency.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(final ClickEvent event) {
	                createRegisterCurrencyFrom();
	            }
	        });
	        registerCurrency.setEnabled(true);
	        return registerCurrency;
	 }//private Button createRegisterCurrencyButton()
	 
	 private void createRegisterCurrencyFrom(){
		 logger.info("\nRegistrarMonedaView.createRegisterCurrencyFrom()");
		 this.editarMoneda(null);
	 }//private void createRegisterCurrencyFrom()
	 
	 private Component buildFilter() {
	        final TextField filter = new TextField();
	        filter.addTextChangeListener(new TextChangeListener() {
	            @Override
	            public void textChange(final TextChangeEvent event) {
	                Filterable data = (Filterable) currencyTable.getContainerDataSource();
	                data.removeAllContainerFilters();
	                data.addContainerFilter(new Filter() {
	                    @Override
	                    public boolean passesFilter(final Object itemId,final Item item) {
	                        if (event.getText() == null || event.getText().equals("")) {
	                            return true;
	                        }

	                        return filterByProperty("id_code", item, event.getText()) || filterByProperty("description", item, event.getText());

	                    }
	                    @Override
	                    public boolean appliesToProperty(final Object propertyId) {
	                        if (propertyId.equals("id_code") || propertyId.equals("description")) {
	                            return true;
	                        }
	                        return false;
	                    }
	                });
	            }
	        });
		 
	     filter.setInputPrompt(this.messages.get(this.VIEW_NAME + "text.field.filter"));/*"Filtro" */
	     filter.setIcon(FontAwesome.SEARCH);
	     filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
	     filter.addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
	    	 @Override
	         public void handleAction(final Object sender, final Object target) {
	    		 filter.setValue("");
	             ((Filterable) currencyTable.getContainerDataSource()).removeAllContainerFilters();
	         }
	     });
	     return filter;
	 }//private Component buildFilter()
	 
    private boolean filterByProperty(final String prop, final Item item, final String text) {
    	//logger.info("\n************************ MARK TABLE AS DIRTY AFTER FILTER ACTION\n************************");
    	//currencyTable.markAsDirty();
    	//currencyTable.markAsDirtyRecursive();
        if (item == null || item.getItemProperty(prop) == null || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim().toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }//private boolean filterByProperty(final String prop, final Item item, final String text)
	 
	 
	 private List<CurrencyDTO> generateCurrencyTestData(){
		 List<CurrencyDTO> monedaList = new ArrayList<CurrencyDTO>();
		 monedaList.add(new CurrencyDTO(1L,null,null,null,null, "PYG", "Guaraní / Paraguay", true));
		 monedaList.add(new CurrencyDTO(2L,null,null,null,null, "USD", "Dólar / Estados Unidos", false));
		 monedaList.add(new CurrencyDTO(3L,null,null,null,null, "ARS", "Peso / Argentina", false));
		 monedaList.add(new CurrencyDTO(4L,null,null,null,null, "PEN", "Nuevo Sol / Perú", false));
		 monedaList.add(new CurrencyDTO(5L,null,null,null,null, "BRL", "Real / Brasil", false));
		 monedaList.add(new CurrencyDTO(7L,null,null,null,null, "BOB", "Boliviano / Bolivia", false));
		 monedaList.add(new CurrencyDTO(8L,null,null,null,null, "EUR", "Euro / Alemania", false));
		 monedaList.add(new CurrencyDTO(9L,null,null,null,null, "CLP", "Peso / Chile", false));
		 monedaList.add(new CurrencyDTO(10L,null,null,null,null, "GBP", "Libra esterlina / Reino Unido", false));
		 monedaList.add(new CurrencyDTO(11L,null,null,null,null, "UYU", "Peso / Uruguay", false));
		 return monedaList;
	 }//private List<Moneda> generateCurrencyTestData()
	 
	 private Table buildCurrenciesTable() throws PmsServiceException{
		 	currencyTable = new Table();
	        BeanItemContainer<CurrencyDTO> monedaBeanItemContainer = new BeanItemContainer<CurrencyDTO>(CurrencyDTO.class);
	        monedaBeanItemContainer.addAll(this.gestionComercialService.listCurrencyDTO(new CurrencyDTO()));
	        currencyTable.setContainerDataSource(monedaBeanItemContainer);
	        currencyTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final CurrencyDTO moneda = (CurrencyDTO)itemId;
					return buildOperationsButtonPanel(moneda);
				}//public Object generateCell(Table source, Object itemId, Object columnId)
			});//currencyTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
	        //logger.info("\nbuildCurrenciesTable() -- 5");
	        currencyTable.addGeneratedColumn("currency_local", new Table.ColumnGenerator() {				
				@Override
				public Object generateCell(Table source, Object itemId, Object columnId) {
					// TODO Auto-generated method stub
					final CurrencyDTO moneda = (CurrencyDTO)itemId;
					CheckBox localChkBox = new CheckBox(null, moneda.getCurrency_local());
					localChkBox.setReadOnly(true);
					return localChkBox;
				}//public Object generateCell(Table source, Object itemId, Object columnId) {
			});//currencyTable.addGeneratedColumn("local", new Table.ColumnGenerator() {
	        currencyTable.setVisibleColumns(new Object[] {"operaciones","id_code","description","currency_local"});
	        currencyTable.setColumnHeader("operaciones", this.messages.get(this.VIEW_NAME + "table.currencies.column.operations"));/*"Operaciones"*/
	        currencyTable.setColumnHeader("id_code", this.messages.get(this.VIEW_NAME + "table.currencies.column.identifier"));/*"Identificador"*/
	        currencyTable.setColumnHeader("description", this.messages.get(this.VIEW_NAME + "table.currencies.column.description"));/*"Descripción"*/
	        currencyTable.setColumnHeader("currency_local", this.messages.get(this.VIEW_NAME + "table.currencies.column.local"));/*"Moneda local"*/
	        currencyTable.setSizeFull();
	        currencyTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
	        currencyTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
	        currencyTable.addStyleName(ValoTheme.TABLE_COMPACT);
	        currencyTable.setSelectable(true);
	        currencyTable.setColumnCollapsingAllowed(true);
	        currencyTable.setColumnCollapsible("operaciones", false);
	        currencyTable.setColumnCollapsible("id_code", false);
	        currencyTable.setColumnCollapsible("currency_local", false);
	        currencyTable.setColumnAlignment("operaciones", Table.Align.LEFT);
	        currencyTable.setColumnAlignment("id_code", Table.Align.LEFT);
	        currencyTable.setColumnAlignment("description", Table.Align.LEFT);
	        currencyTable.setColumnAlignment("currency_local", Table.Align.LEFT);
	        currencyTable.setColumnExpandRatio("operaciones", 0.025f);
	        currencyTable.setColumnExpandRatio("id_code", 0.025f);
	        currencyTable.setColumnExpandRatio("description", 0.1f);
	        currencyTable.setColumnExpandRatio("currency_local", 0.025f);
	        currencyTable.setSortContainerPropertyId("currency_local");
	        currencyTable.setSortAscending(false);
	        currencyTable.setColumnReorderingAllowed(true);
	        currencyTable.setFooterVisible(true);
	        currencyTable.setColumnFooter("operaciones", "**");
	        return currencyTable;
	 }//private Component buildCurrenciesTable()

	 @Subscribe
	 public void browserResized(final BrowserResizeEvent event) {
	     // Some columns are collapsed when browser window width gets small
	     // enough to make the table fit better.
	     if (defaultColumnsVisible()) {
	        for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        	currencyTable.setColumnCollapsed(propertyId, Page.getCurrent().getBrowserWindowWidth() < 800);
	        }
	     }
	 }//public void browserResized(final BrowserResizeEvent event)
	
	@Subscribe 
	public void returnFromRegisterCurrencyForm(final RegisterCurrencyViewEvent registerCurrencyViewEvent){
		try{
			logger.info("\nreturning from RegisterCurrencyForm\nregisterCurrencyViewEvent.getMoneda(): " 
			+ registerCurrencyViewEvent.getMoneda());			
		}catch(Exception e){
			logger.error("error\n", e);
		}
	}
	 
	private boolean defaultColumnsVisible() {
	    boolean result = true;
	    for (String propertyId : DEFAULT_COLLAPSIBLE) {
	        if (currencyTable.isColumnCollapsed(propertyId) == Page.getCurrent().getBrowserWindowWidth() < 800) {
	           result = false;
	        }
	     }
	     return result;
	}//private boolean defaultColumnsVisible()
	
	private void editarMoneda(CurrencyDTO moneda){
		try{
			UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REGISTER_CURRENCY_FORM.getViewName());
			DashboardEventBus.post(new EditMonedaEvent(moneda));
		}catch(Exception e){
			logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
			logger.error(e.getMessage(), e);
		}
	}//private void editarMoneda(Moneda moneda)
	
	
	public RegisterCurrencyView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
    private void prepareMessages() throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
    	this.setLocale(bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    	this.messages = bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }
    
	private HorizontalLayout buildOperationsButtonPanel(final CurrencyDTO moneda){
		final Button has_no_valid_exchange_rateButton = new Button();
		has_no_valid_exchange_rateButton.addStyleName("borderless");
		has_no_valid_exchange_rateButton.setIcon(FontAwesome.EXCLAMATION_CIRCLE);		
		has_no_valid_exchange_rateButton.setVisible(!moneda.getCurrency_local() && !moneda.getHas_valid_exchange_rate());		
		has_no_valid_exchange_rateButton.setDescription(messages.get(VIEW_NAME + "table.currencies.column.operations.button.has.no.valid.exchange.rate.description"));
		has_no_valid_exchange_rateButton.setResponsive(false);
		
		final Button editButton = new Button();
		editButton.setIcon(FontAwesome.EDIT);
		editButton.addStyleName("borderless");
		editButton.setDescription(this.messages.get(this.VIEW_NAME + "main.title"));
		editButton.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                logger.info("\n editar moneda...\n" + moneda.toString());
                try{			                	
                	editarMoneda(moneda);
                }catch(Exception e){
    				logger.error(e.getMessage() + "\n cause: " + (e.getCause()!=null ? e.getCause().getMessage() : null));
    				logger.error(e.getMessage(), e);
                }			                
            }
        });
			
		final HorizontalLayout operationsButtonPanel = new HorizontalLayout(editButton,has_no_valid_exchange_rateButton);
		return operationsButtonPanel;
	}
}
