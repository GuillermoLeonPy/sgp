package py.com.kyron.sgp.gui.view.stockmanagement.rawmaterialstockmanagementformview;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.productionmanagement.domain.RawMaterialDTO;
import py.com.kyron.sgp.gui.component.SgpForm;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.stockmanagement.RawMaterialStockManagementFormView;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class RawMaterialDTOTabLayout extends VerticalLayout {
	
	private final Logger logger = LoggerFactory.getLogger(RawMaterialDTOTabLayout.class);
	private SgpForm<RawMaterialDTO> rawMaterialDTOForm;
	private Map<String,String> messages;
	private final String VIEW_NAME = "rawmaterial.stock.management.form.";
	private final RawMaterialDTO rawMaterialDTO;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private final RawMaterialStockManagementFormView rawMaterialStockManagementFormView;
	
	
	public RawMaterialDTOTabLayout(RawMaterialStockManagementFormView rawMaterialStockManagementFormView, RawMaterialDTO rawMaterialDTO) {
		// TODO Auto-generated constructor stub
		this.rawMaterialStockManagementFormView = rawMaterialStockManagementFormView;
		this.rawMaterialDTO = rawMaterialDTO;
		try{
			logger.info("\n RawMaterialDTOTabLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
	        this.setSpacing(true);
	        this.setMargin(true);
	        Responsive.makeResponsive(this);
			this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public RawMaterialDTOTabLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	private void initTitles(){
		Label title = new Label(this.messages.get("application.common.rawmaterialid.label"));
        title.addStyleName("h1");
        this.addComponent(title);
	}
	private void setUpForm(){
		this.printRawMaterialDTO();
		this.rawMaterialDTOForm = new SgpForm<RawMaterialDTO>(RawMaterialDTO.class, new BeanItem<RawMaterialDTO>(this.rawMaterialDTO), "light", true);
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_id", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.data.text.field.rawmaterial.id.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.id.required.message"), false);
		this.rawMaterialDTOForm.bindAndSetPositionFormLayoutTextField("raw_material_description", this.messages.get(this.VIEW_NAME + "tab.rawmaterial.data.text.field.description.label")/**/, true, 100, true,this.messages.get(this.VIEW_NAME + "text.field.rawmaterial.description.required.message"), false);
	}

	private void printRawMaterialDTO(){
		logger.info("\n**********\nrawMaterialDTO\n**********\nthis.rawMaterialDTO.toString() : \n" + this.rawMaterialDTO.toString());

	}
	
	private void setUpLayoutContent(){
		this.initTitles();
    	this.setUpForm();      
		final Button cancelButton = new Button(this.messages.get("application.common.button.cancel.label"));/*"Cancelar"*/
		cancelButton.addClickListener(
				new ClickListener() {
		            @Override
		            public void buttonClick(final ClickEvent event) {
		            	try{
		            		rawMaterialDTOForm.discard();
		            		rawMaterialStockManagementFormView.navigateToCallerView();
		            	}catch(Exception e){
		            		commonExceptionErrorNotification.showErrorMessageNotification(e);		            		
		            	}
		            }
		        }
			);
		HorizontalLayout okCancelHorizontalLayout = new HorizontalLayout();
		okCancelHorizontalLayout.setMargin(new MarginInfo(true, false, true, false));
		okCancelHorizontalLayout.setSpacing(true);
		okCancelHorizontalLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		okCancelHorizontalLayout.addComponent(cancelButton);		
		this.rawMaterialDTOForm.getSgpFormLayout().addComponent(okCancelHorizontalLayout);
		this.addComponent(this.rawMaterialDTOForm.getSgpFormLayout());
	}
}
