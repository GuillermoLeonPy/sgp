package py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;

import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class HelpContentItemLayout extends VerticalLayout {

	private final Logger logger = LoggerFactory.getLogger(HelpContentItemLayout.class);
	private Map<String,String> messages;
	private final String VIEW_NAME;
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private BussinesSessionUtils bussinesSessionUtils;
	private final HelpContentItem helpContentItem;
	private Label large;
	private VerticalLayout labelsVerticalLayout;
	private String title;
	
	public HelpContentItemLayout(final String VIEW_NAME,final HelpContentItem helpContentItem) {
		// TODO Auto-generated constructor stub
		this.VIEW_NAME = VIEW_NAME;
		this.helpContentItem = helpContentItem;
		try{
			logger.info("\n HelpContentItemLayout..");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.VIEW_NAME);			
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();
	        setSizeFull();
	        addStyleName("transactions");
	        DashboardEventBus.register(this);
	        Responsive.makeResponsive(this);
	        this.setUpLayoutContent();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	/*public HelpContentItemLayout(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}*/
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void initServices(){
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	private void setUpLayoutContent() throws Exception{		
		addComponent(this.buildToolbar());
		if(this.helpContentItem!=null){
			this.labelsVerticalLayout = new VerticalLayout();
			this.labelsVerticalLayout.setMargin(true);
			this.labelsVerticalLayout.setSpacing(true);
			this.labelsVerticalLayout.addStyleName("content-labels");
			for(String key : this.helpContentItem.getSubjects().keySet()){
				this.large = new Label(this.helpContentItem.getSubjects().get(key));
				this.large.addStyleName("large");
				this.labelsVerticalLayout.addComponent(this.large);
			}
			this.addComponent(this.labelsVerticalLayout);
		}
	}

	private Component buildToolbar() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);
        logger.info("\n*********************remember: an HorizontalLayout is set with the instrucction: Responsive.makeResponsive(header); !!! "
        		+"\n*********************");
        if( this.helpContentItem!= null)
        	this.title = " : " + this.helpContentItem.getHelpContentItemKeyValue();
        else
        	this.title = "";
        Label title = new Label(this.messages.get(this.VIEW_NAME + "tab.help.content.item.main.title") + this.title);
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(title);
        
        /*HorizontalLayout tools = new HorizontalLayout(this.createRegisterProcessButton());	        	        
        tools.setSpacing(true);
        tools.addStyleName("toolbar");
        header.addComponent(tools);*/
        return header;
	}//private Component buildToolbar()
}
