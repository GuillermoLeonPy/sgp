package py.com.kyron.sgp.gui.view.cash.movements.management.sale.invoice.register.form.view.components;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.stockmanagement.domain.SIItemPDMProductInstanceInvolvedDTO;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class CreditNoteItemReturnableProductListWindow extends Window {

	private final Logger logger = LoggerFactory.getLogger(CreditNoteItemReturnableProductListWindow.class);
	private final String VIEW_NAME;
	private Map<String,String> messages;
	private VerticalLayout mainViewLayout;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final List<SIItemPDMProductInstanceInvolvedDTO> listReturnableProductInstances;
	private Table listReturnableProductInstancesTable;
	private final boolean queryWindowMode;
	private boolean cancellButtoActionflag;
	
	public CreditNoteItemReturnableProductListWindow(
			final List<SIItemPDMProductInstanceInvolvedDTO> listReturnableProductInstances,
			final String VIEW_NAME,
			final boolean queryWindowMode) {
		// TODO Auto-generated constructor stub
		this.listReturnableProductInstances = listReturnableProductInstances;
		this.VIEW_NAME = VIEW_NAME;
		this.queryWindowMode = queryWindowMode;
		try{
			
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			//this.addStyleName("v-scrollable");
			this.addStyleName("profile-window");
			this.setUpWindowCaptionAndMainTitle();
			this.initMainViewLayout();
			this.initTitles();
			this.buildListReturnableProductInstancesTable();
			this.setUpOkCancelButtons();
			this.setContent(this.mainViewLayout);
			Responsive.makeResponsive(this);
			UI.getCurrent().addWindow(this);
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public CreditNoteItemReturnableProductListWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}*/

	/*public CreditNoteItemReturnableProductListWindow(String caption,
			Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}*/

	private void setUpWindowCaptionAndMainTitle(){
		this.setCaption(this.messages.get(this.VIEW_NAME + "tab.credit.note.form.credit.note.item.returnable.product.list.window.caption"));
		this.setModal(true);
		this.setClosable(true);
		this.center();
		this.setSizeFull();//
		this.setResizable(true);
        this.setIcon(FontAwesome.PRODUCT_HUNT);
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
		//this.mainViewLayout.addStyleName("v-scrollable");
		this.mainViewLayout.setSizeFull();
		this.mainViewLayout.setMargin(new MarginInfo(true, false, false, false));
		this.mainViewLayout.setIcon(FontAwesome.COGS);
	}
	
	private void initTitles(){
		Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.credit.note.form.credit.note.item.returnable.product.list.window.main.title"));
        title.addStyleName(ValoTheme.LABEL_H4);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        //HorizontalLayout vHorizontalLayout = new HorizontalLayout(title);
        //vHorizontalLayout.setMargin(true);
        //this.mainViewLayout.addComponent(vHorizontalLayout);
        this.mainViewLayout.addComponent(title);
	}
	
	private void buildListReturnableProductInstancesTable(){
    	this.listReturnableProductInstancesTable = new Table();
    	BeanItemContainer<SIItemPDMProductInstanceInvolvedDTO> SIItemPDMProductInstanceInvolvedDTOBeanItemContainer	= new BeanItemContainer<SIItemPDMProductInstanceInvolvedDTO>(SIItemPDMProductInstanceInvolvedDTO.class);
    	SIItemPDMProductInstanceInvolvedDTOBeanItemContainer.addAll(this.listReturnableProductInstances);
    	this.listReturnableProductInstancesTable.setContainerDataSource(SIItemPDMProductInstanceInvolvedDTOBeanItemContainer);
    	this.listReturnableProductInstancesTable.addGeneratedColumn("operations", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO = (SIItemPDMProductInstanceInvolvedDTO)itemId;
				return buildOperationsButtonPanel(vSIItemPDMProductInstanceInvolvedDTO);
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//listCreditNoteItemDTOTable.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listReturnableProductInstancesTable.addGeneratedColumn("g_product_instance_unique_number", new Table.ColumnGenerator() {				
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				// TODO Auto-generated method stub
				final SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO = (SIItemPDMProductInstanceInvolvedDTO)itemId;
				final ObjectProperty<Long> property = new ObjectProperty<Long>(vSIItemPDMProductInstanceInvolvedDTO.getProduct_instance_unique_number());
				final Label label = new Label(property);				
				label.addStyleName("colored");
				final HorizontalLayout vHorizontalLayout = new HorizontalLayout(label);
				vHorizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
				return vHorizontalLayout;
			}//public Object generateCell(Table source, Object itemId, Object columnId)
		});//tableListSaleInvoiceItemProductDeliverablesDTO.addGeneratedColumn("operaciones", new Table.ColumnGenerator() {
    	this.listReturnableProductInstancesTable.setVisibleColumns(new Object[] {"operations","g_product_instance_unique_number"});
    	this.listReturnableProductInstancesTable.setColumnHeader("operations", this.messages.get(this.VIEW_NAME + "tab.credit.note.form.credit.note.item.returnable.product.list.window.table.column.operations"));
    	this.listReturnableProductInstancesTable.setColumnAlignment("operations", Table.Align.LEFT);
    	this.listReturnableProductInstancesTable.setColumnHeader("g_product_instance_unique_number", this.messages.get(this.VIEW_NAME + "tab.credit.note.form.credit.note.item.returnable.product.list.window.table.column.product.instance.unique.number"));
    	this.listReturnableProductInstancesTable.setColumnAlignment("g_product_instance_unique_number", Table.Align.LEFT);
    	this.listReturnableProductInstancesTable.setSizeFull();
    	//this.personDTOTable.addStyleName(ValoTheme.TABLE_BORDERLESS);
    	this.listReturnableProductInstancesTable.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
    	//this.rolesTable.addStyleName(ValoTheme.TABLE_COMPACT);
    	this.listReturnableProductInstancesTable.addStyleName(ValoTheme.TABLE_SMALL);
    	this.listReturnableProductInstancesTable.setColumnExpandRatio("operations", 0.1f);    	
    	this.listReturnableProductInstancesTable.setColumnExpandRatio("g_product_instance_unique_number", 0.1f);
    	this.listReturnableProductInstancesTable.setSelectable(true);
    	this.listReturnableProductInstancesTable.setColumnCollapsingAllowed(true);
    	this.listReturnableProductInstancesTable.setColumnCollapsible("operations", false);
    	this.listReturnableProductInstancesTable.setColumnCollapsible("g_product_instance_unique_number", false);
    	//this.rolesTable.setColumnCollapsible("role_description", false);
    	//this.personDTOTable.setSortContainerPropertyId("commercial_name");
    	//this.personDTOTable.setSortAscending(false);
    	this.listReturnableProductInstancesTable.setColumnReorderingAllowed(true);
    	this.listReturnableProductInstancesTable.setFooterVisible(true);
    	this.listReturnableProductInstancesTable.setColumnFooter(this.messages.get("application.common.table.operations.column.label"), "**");
    	this.mainViewLayout.addComponent(this.listReturnableProductInstancesTable);
	}
	
	private HorizontalLayout buildOperationsButtonPanel(final SIItemPDMProductInstanceInvolvedDTO vSIItemPDMProductInstanceInvolvedDTO){
		final CheckBox returnChkBox = 
				new CheckBox
				(null, 
				vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit() == null ? false : vSIItemPDMProductInstanceInvolvedDTO.getReturnUnit());
		returnChkBox.setReadOnly(this.queryWindowMode);
		returnChkBox.setEnabled(!this.queryWindowMode);
		returnChkBox.addValueChangeListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Boolean vBoolean = (Boolean)event.getProperty().getValue();
				logger.info( "\n=========================="
							+"\n return unit check : " + vBoolean
							+"\n product nbr : " + vSIItemPDMProductInstanceInvolvedDTO.getProduct_instance_unique_number() 
							+"\n==========================");				
				vSIItemPDMProductInstanceInvolvedDTO.setReturnUnit(vBoolean);
			}			
		});
		HorizontalLayout vHorizontalLayout = new HorizontalLayout(returnChkBox);
		return vHorizontalLayout;
	}
	
    private void setUpOkCancelButtons(){
    	Button okButton = new Button(this.messages.get("application.common.button.save.label"));/*"Cancelar"*/
		okButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		close();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		okButton.setEnabled(!this.queryWindowMode);
		
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		cancellButtoActionflag = true;
		            		close();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		
		
		final HorizontalLayout wrapperHorizontalLayout = new HorizontalLayout();
		wrapperHorizontalLayout.setSpacing(true);
		wrapperHorizontalLayout.setMargin(true);
		wrapperHorizontalLayout.addComponent(okButton);
		wrapperHorizontalLayout.addComponent(cancelButton);
		final HorizontalLayout vHorizontalLayout = new HorizontalLayout(wrapperHorizontalLayout);
		vHorizontalLayout.setSizeFull();
		vHorizontalLayout.setComponentAlignment(wrapperHorizontalLayout, Alignment.MIDDLE_RIGHT);
		this.mainViewLayout.addComponent(vHorizontalLayout);
		
    }
    
    
	public void adjuntWindowSizeAccordingToCientDisplay(){
		logger.info("\n*************************************"
					+"\n adjusting window width and height according to client display size"
					+"\n browser width: " + Page.getCurrent().getBrowserWindowWidth()
					+"\n browser height: " + Page.getCurrent().getBrowserWindowHeight()
					+"\n*************************************");
		if(Page.getCurrent().getBrowserWindowWidth() < 800)
			this.setSizeFull();
		else{
			this.setWidth("650px");
			this.setHeight("800px");
		}			
	}

	/**
	 * @return the cancellButtoActionflag
	 */
	public boolean isCancellButtoActionflag() {
		return cancellButtoActionflag;
	}

	/**
	 * @param cancellButtoActionflag the cancellButtoActionflag to set
	 */
	public void setCancellButtoActionflag(boolean cancellButtoActionflag) {
		this.cancellButtoActionflag = cancellButtoActionflag;
	}

	/**
	 * @return the queryWindowMode
	 */
	public boolean isQueryWindowMode() {
		return queryWindowMode;
	}
}
