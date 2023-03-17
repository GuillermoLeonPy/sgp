package py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class ConfirmOptionsWindow extends Window {
	private final Logger logger = LoggerFactory.getLogger(ConfirmOptionsWindow.class);
	private ConfirmWindowDecision decision;
	private Button btnYes = new Button();
	private Button btnNo = new Button();
	private VerticalLayout layout = new VerticalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	private boolean yesAccionRealized;
	private boolean noAccionRealized;
	private OptionGroup selectOptionGroup;
	
	public ConfirmOptionsWindow() {
		// TODO Auto-generated constructor stub
	}

	public ConfirmOptionsWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public ConfirmOptionsWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}
	public ConfirmOptionsWindow(String caption, String question,String yes, String no, List<SelectOneOption> listSelectOneOption, String selectOneOptionCaption) {
			this.initSelectOne(listSelectOneOption,selectOneOptionCaption);			
			yesAccionRealized = false;
			noAccionRealized = false;
			setCaption(caption);
			btnYes.setCaption(yes);
			btnYes.setEnabled(false);
			//btnYes.focus();
			btnNo.setCaption(no);
			setModal(true);
			center();
			buttonsLayout.addComponent(btnYes);
			buttonsLayout.setComponentAlignment(btnYes, Alignment.MIDDLE_CENTER);
			buttonsLayout.addComponent(btnNo);
			buttonsLayout.setComponentAlignment(btnNo, Alignment.MIDDLE_CENTER);
			Label questionLabel = new Label(question);
			questionLabel.addStyleName(ValoTheme.LABEL_H4);
			questionLabel.addStyleName(ValoTheme.LABEL_COLORED);
			layout.addComponent(questionLabel);
			layout.addComponent(selectOptionGroup);
			layout.addComponent(buttonsLayout);
			setContent(layout);
			layout.setMargin(true);
			buttonsLayout.setMargin(true);
			buttonsLayout.setSpacing(true);
			//buttonsLayout.setWidth("100%");
			//setWidth("400px");
			//setHeight("200px");
			
			this.setIcon(FontAwesome.SAVE);
			this.addStyleName("profile-window");
			setResizable(false);
			btnYes.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(Button.ClickEvent event) {
					selectOptionGroup.commit();
					yesAccionRealized = true;
					decision.yes(event);
					close();
				}
			});
			btnNo.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(Button.ClickEvent event) {
					selectOptionGroup.discard();
					noAccionRealized = true;
					decision.no(event);
					close();
					}
				});
			/*	
			this.addShortcutListener(new ShortcutListener("Close",ShortcutAction.KeyCode.ESCAPE, null) {
				@Override
				public void handleAction(Object sender, Object target) {
					close();
					}
				});
				*/
			UI.getCurrent().addWindow(this);
	}//public ConfirmWindow(String caption, String question,String yes, String no)
	
	public void setDecision(ConfirmWindowDecision decision) {
			this.decision = decision;
	}


	public boolean isYesAccionRealized() {
		return yesAccionRealized;
	}

//	public void setYesAccionRealized(boolean yesAccionRealized) {
//		this.yesAccionRealized = yesAccionRealized;
//	}

	public boolean isNoAccionRealized() {
		return noAccionRealized;
	}
//
//	public void setNoAccionRealized(boolean noAccionRealized) {
//		this.noAccionRealized = noAccionRealized;
//	}
	
	public void resetYesNoAccionFlags(){
		this.noAccionRealized = false;
		this.yesAccionRealized = false;
	}
				
	private void initSelectOne(List<SelectOneOption> listSelectOneOption, String selectOneOptionCaption){
    	this.selectOptionGroup = new OptionGroup();//
    	this.selectOptionGroup.addStyleName("small");
    	this.selectOptionGroup.setMultiSelect(false);
    	this.selectOptionGroup.setCaption(selectOneOptionCaption);
    	
    	BeanItemContainer<SelectOneOption> vSelectOneOptionBeanItemContainer = new BeanItemContainer<SelectOneOption>(SelectOneOption.class);
    	vSelectOneOptionBeanItemContainer.addAll(listSelectOneOption);
    	this.selectOptionGroup.setContainerDataSource(vSelectOneOptionBeanItemContainer);
    	this.selectOptionGroup.setItemCaptionPropertyId("value");
    	this.selectOptionGroup.setNullSelectionAllowed(false);
    	this.selectOptionGroup.addValueChangeListener(this.setUpValueChangeListenerForSelectOptionGroup());
	}

    private Property.ValueChangeListener setUpValueChangeListenerForSelectOptionGroup(){
    	return new Property.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
                if (event.getProperty().getValue() != null){
                	btnYes.setEnabled(true);
                	btnYes.markAsDirty();
                }//if (event.getProperty().getValue() != null){
			}//public void valueChange(ValueChangeEvent event)
        };//return new Property.ValueChangeListener() 
    }
	
	public class SelectOneOption{
		private String key;
		private String value;

		/**
		 * @param key
		 * @param value
		 */
		public SelectOneOption(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		public void setKey(String key) {
			this.key = key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "SelectOneOption [key=" + key + ", value=" + value + "]";
		}
	}

	/**
	 * @return the selectOptionGroup
	 */
	public OptionGroup getSelectOptionGroup() {
		return selectOptionGroup;
	}

	/**
	 * @param selectOptionGroup the selectOptionGroup to set
	 */
	public void setSelectOptionGroup(OptionGroup selectOptionGroup) {
		this.selectOptionGroup = selectOptionGroup;
	}

	public void adjuntWindowSizeAccordingToCientDisplay(){
		logger.info(" \n*************************************"
					+"\n adjusting window width and height according to client display size"
					+"\n browser width: " + Page.getCurrent().getBrowserWindowWidth()
					+"\n browser height: " + Page.getCurrent().getBrowserWindowHeight()
					+"\n*************************************");
		if(Page.getCurrent().getBrowserWindowWidth() < 800)
			this.setSizeFull();
		else{
			this.setWidth("350px");
			this.setHeight("300px");
		}			
	}
}
