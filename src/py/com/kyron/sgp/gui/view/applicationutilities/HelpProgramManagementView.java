package py.com.kyron.sgp.gui.view.applicationutilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.utils.CommonExceptionErrorNotification;
import py.com.kyron.sgp.gui.utils.ViewMessagesHelper;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContent;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentItem;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentItemLayout;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentItemTableLayout;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentItemTableLayoutFunctions;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentTableLayout;
import py.com.kyron.sgp.gui.view.applicationutilities.help.program.management.view.components.HelpContentTableLayoutFunctions;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class HelpProgramManagementView extends VerticalLayout implements View,HelpContentTableLayoutFunctions,HelpContentItemTableLayoutFunctions {

	private final Logger logger = LoggerFactory.getLogger(HelpProgramManagementView.class);
	private Map<String,String> messages;
	private final String VIEW_NAME = "help.program.";
	private final String SECURED_PROGRAM_PREFIX = "secured.access.program.";
	private BussinesSessionUtils bussinesSessionUtils;
	private CommonExceptionErrorNotification commonExceptionErrorNotification;
	private TabSheet tabs;
	private Tab helpContentTableLayoutTab;
	private Tab helpContentItemTableLayoutTab;
	private Tab helpContentItemLayoutTab;
	private VerticalLayout mainViewLayout;
	private String selectedTabContentComponentId;
	private Map<String,String> mainMenuEntrys;
	private Map<String,String> applicationModules;
	private Map<String,String> allHelpProgramPrefixKeys;
	private final String MAIN_MENU_MODULE_PREFIX = "main.menu.module.";
	private final String HELP_PROGRAM_PREFIX = "help.program.";
			
	public HelpProgramManagementView() {
		// TODO Auto-generated constructor stub
		try{
			logger.info("\n HelpProgramManagementView()...");
			this.messages = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(VIEW_NAME);
			this.commonExceptionErrorNotification = new CommonExceptionErrorNotification();
			this.initServices();			
	        this.addStyleName("v-scrollable");
	        this.setHeight("100%");
	        Responsive.makeResponsive(this);
			DashboardEventBus.register(this);
			this.setUpLayout();
		}catch(Exception e){
			logger.error("\nerror: ",e);
		}
	}

	public HelpProgramManagementView(Component... children) {
		super(children);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}
	private void initServices() throws Exception{
		this.bussinesSessionUtils = (BussinesSessionUtils)SgpApplicationContextAware.getApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
	}
	
	@Override
    public void detach() {
        super.detach();
        // A new instance of TransactionsView is created every time it's
        // navigated to so we'll need to clean up references to it on detach.
        DashboardEventBus.unregister(this);
    }//public void detach()
	
	private void setUpLayout(){
		this.initMainViewLayout();
		this.initTitles();
		this.initTabSheet();
		this.setUpHelpContentTableLayoutTab();
		this.setUpHelpContentItemTableLayoutTab(null);
		this.setUpHelpContentItemLayoutTab(null);
	    this.mainViewLayout.addComponent(this.tabs);
	    this.removeAndReAddMainViewLayout();
	    this.refreshTabSelection();
	}
	
	private void initMainViewLayout(){
		this.mainViewLayout = new VerticalLayout();
		this.mainViewLayout.setSpacing(true);
		this.mainViewLayout.setMargin(true);
	}
	
	private void initTitles(){
		try{
			Label title = new Label(this.messages.get(this.VIEW_NAME + "main.title"));
        	title.addStyleName("h1");
        	this.mainViewLayout.addComponent(title);
		}catch(Exception e){
			commonExceptionErrorNotification.showErrorMessageNotification(e);
		}
	}
	
	private void initTabSheet(){
    	this.tabs = new TabSheet();
    	this.tabs.setId("tabs");
    	//this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
    	this.tabs.addStyleName("framed");	
	}
	
	private void refreshTabSelection(){
		logger.info("\n##############################"
					+"\n refreshing tab selection"
					+"\n##############################");
		//this.tabs.setSelectedTab(1);
		//this.tabs.markAsDirty();
		int compomentCount = this.tabs.getComponentCount();
		for(int counter = (compomentCount - 1); counter >= 0 ; counter--){
			this.tabs.setSelectedTab(counter);
			logger.info("\n tab in position: "+ counter + " has been selected...");
		}
		this.tabs.markAsDirty();
		this.tabs.addSelectedTabChangeListener(this.setUpTabsSelectionListener());
	}
	
	private void removeAndReAddMainViewLayout(){
		this.removeAllComponents();
		this.addComponent(this.mainViewLayout);
	}
	
    private SelectedTabChangeListener setUpTabsSelectionListener(){
    	return new SelectedTabChangeListener(){
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				// TODO Auto-generated method stub				
				selectedTabContentComponentId =  event.getTabSheet().getSelectedTab().getId();
				logger.info("\ntab selection listener"
						+"\n selectedTabContentComponentId : " + selectedTabContentComponentId);				

			}    		
    	};
    }

	@Override
	public List<HelpContent> setUpHelpContentList() throws Exception {
		// TODO Auto-generated method stub
		List<HelpContent> listHelpContent = new ArrayList<HelpContent>();
		HelpContent vHelpContent = null;
		this.retrieveMainMenuEntrys();
		this.setUpApplicationModuleList();
		this.retrieveAllHelpProgramPrefixKeys();
		final Iterator<String> mainMenuEntryKeySetIterator = this.mainMenuEntrys.keySet().iterator();
		String mainMenuKey = null;
		String moduleKey = null;
		String mainMenuKeyWithOutModuleKey = null;
		logger.info( "\n =============================================================== "
					+"\n =============================================================== ");
		
		while(mainMenuEntryKeySetIterator.hasNext()){
			/*Examples: 
			  main.menu.module.cash.movements.management.sale.invoice.management
			  main.menu.module.cash.movements.management.purchase.invoice.management
			  */
			mainMenuKey = mainMenuEntryKeySetIterator.next();			
			moduleKey = this.findModuleKeyByMainMenuKey(mainMenuKey);
			/* moduleKey Example
				main.menu.module.cash.movements.management.
			 * */			
			mainMenuKeyWithOutModuleKey = mainMenuKey.substring(moduleKey.length());
			/* mainMenuKeyWithOutModuleKey Example:
			 	sale.invoice.management
			 	purchase.invoice.management
			 * */
			logger.info("\n\n mainMenuKey : " + mainMenuKey + "\n moduleKey : " + moduleKey + "\n mainMenuKeyWithOutModuleKey : " + mainMenuKeyWithOutModuleKey);
			if(this.checkIfHelpContentExistsForMainMenuKeyWithOutModuleKey(mainMenuKeyWithOutModuleKey, moduleKey)){
				vHelpContent = new HelpContent(
						mainMenuKey, 
						this.mainMenuEntrys.get(mainMenuKey), 
						moduleKey, 
						this.applicationModules.get(moduleKey));
				vHelpContent.setListHelpContentItem(this.setUpListHelpContentItemByMainMenuKey(mainMenuKey));
				listHelpContent.add(vHelpContent);
			}//if(this.checkIfHelpContentExistsForMainMenuKeyWithOutModuleKey(mainMenuKeyWithOutModuleKey, moduleKey)){			
		}//while(keySetIterator.hasNext()){			
		return listHelpContent;
	}
	
	private void retrieveMainMenuEntrys() throws Exception{
		this.mainMenuEntrys = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.MAIN_MENU_MODULE_PREFIX);		
		final Iterator<String> keySetIterator = this.mainMenuEntrys.keySet().iterator();
		String key = null;
		while(keySetIterator.hasNext()){
			key = keySetIterator.next();
			if((!key.startsWith(this.MAIN_MENU_MODULE_PREFIX) || key.charAt(key.length() - 1/*the last character*/) == '.'/*all module keys end with period*/)){
				//if an application module; then remove
				keySetIterator.remove();
			}//end if
		}//while(keySetIterator.hasNext()){
	}//private void retrieveMainMenuEntrys() throws Exception{

	private void setUpApplicationModuleList() throws Exception{
		this.applicationModules = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.MAIN_MENU_MODULE_PREFIX);		
		final Iterator<String> keySetIterator = this.applicationModules.keySet().iterator();
		String key = null;
		while(keySetIterator.hasNext()){
			key = keySetIterator.next();
			if(!key.startsWith(this.MAIN_MENU_MODULE_PREFIX) || !(key.charAt(key.length() - 1/*the last character*/) == '.'/*all module keys end with period*/)){
				//if not an application module; then remove
				keySetIterator.remove();
			}//end if
		}//while(keySetIterator.hasNext()){
	}//private void setUpApplicationModuleList() throws Exception{
	
	private void retrieveAllHelpProgramPrefixKeys() throws Exception{
		this.allHelpProgramPrefixKeys = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(this.HELP_PROGRAM_PREFIX);
		final Iterator<String> helpProgramkeySetIterator = this.allHelpProgramPrefixKeys.keySet().iterator();
		String key = null;
		while(helpProgramkeySetIterator.hasNext()){
			key = helpProgramkeySetIterator.next();
			if(!key.startsWith(this.HELP_PROGRAM_PREFIX)){
				//if not an application module; then remove
				helpProgramkeySetIterator.remove();
			}//end if
		}//while(keySetIterator.hasNext()){
	}
	
	private String findModuleKeyByMainMenuKey(final String mainMenuKey){
		final Iterator<String> moduleKeySetIterator = this.applicationModules.keySet().iterator();
		String moduleKey = null;
		while(moduleKeySetIterator.hasNext()){
			moduleKey = moduleKeySetIterator.next();
			if(mainMenuKey.startsWith(moduleKey))break; 
		}
		return moduleKey;
	}
	
	private boolean checkIfHelpContentExistsForMainMenuKeyWithOutModuleKey(
			final String mainMenuKeyWithOutModuleKey,
			final String moduleKey){
		final Iterator<String> helpProgramKeySetIterator = this.allHelpProgramPrefixKeys.keySet().iterator();
		String helpProgramKey = null;
		boolean exists = false;
		while(helpProgramKeySetIterator.hasNext()){
			helpProgramKey = helpProgramKeySetIterator.next();
			if(helpProgramKey.startsWith(this.HELP_PROGRAM_PREFIX /*help.program.*/
					+ moduleKey.substring(this.MAIN_MENU_MODULE_PREFIX.length()/* - 1*/)
					+ mainMenuKeyWithOutModuleKey))
			{
				exists = true;
				break; 
			}//end if
		}//while(helpProgramKeySetIterator.hasNext()){
		return exists;
	}
	
	private List<HelpContentItem> setUpListHelpContentItemByMainMenuKey(final String mainMenuKey) throws Exception{
		List<HelpContentItem> listHelpContentItem = new ArrayList<HelpContentItem>();
		final Map<String,String> helpContentByMainMenuKey = 
				ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale
				(this.HELP_PROGRAM_PREFIX + mainMenuKey.substring(this.MAIN_MENU_MODULE_PREFIX.length()/* - 1*/));
		
		final Iterator<String> helpContentByMainMenuKey_KeySetIterator = helpContentByMainMenuKey.keySet().iterator();
		String helpContentKey = null;
		while(helpContentByMainMenuKey_KeySetIterator.hasNext()){
			helpContentKey = helpContentByMainMenuKey_KeySetIterator.next();
			if(helpContentKey.contains(this.HELP_PROGRAM_PREFIX + mainMenuKey.substring(this.MAIN_MENU_MODULE_PREFIX.length()/* - 1*/)) && !helpContentKey.contains("subject")){
				HelpContentItem vHelpContentItem = new HelpContentItem
						(helpContentKey, 
						helpContentByMainMenuKey.get(helpContentKey));
				
				listHelpContentItem.add(vHelpContentItem);
			}//if(!helpContentKey.contains("subject")){
		}//while(helpContentByMainMenuKey_KeySetIterator.hasNext()){
		return listHelpContentItem;
	}

	@Override
	public void queryListHelpContentItem(HelpContent helpContent) throws Exception {
		// TODO Auto-generated method stub
		logger.info( "\n =============================== "
				 	+"\n queryListHelpContentItem"
				 	+"\n =============================== "
				 	+"\n helpContent : " + helpContent
				 	+"\n ------------------------------- ");
		for(HelpContentItem vHelpContentItem : helpContent.getListHelpContentItem()){
			logger.info("\n vHelpContentItem : " + vHelpContentItem);
			this.setUpHelpContentItemSubjects(vHelpContentItem);
			for(String key : vHelpContentItem.getSubjects().keySet()){
				logger.info("\n\t " + vHelpContentItem.getSubjects().get(key));
			}
		}
		this.setUpHelpContentItemTableLayoutTab(helpContent);
	}
	
	private void setUpHelpContentTableLayoutTab(){
		HelpContentTableLayout vHelpContentTableLayout = new HelpContentTableLayout(this.VIEW_NAME,this);
		vHelpContentTableLayout.setId("vHelpContentTableLayout");
		if(this.helpContentTableLayoutTab!=null)this.tabs.removeTab(this.helpContentTableLayoutTab);
		this.helpContentTableLayoutTab = this.tabs.addTab(
				vHelpContentTableLayout, 
				this.messages.get(this.VIEW_NAME + "tab.help.content.table"), 
				FontAwesome.YELP, 
				0);
	}
	
	
	private void setUpHelpContentItemSubjects(final HelpContentItem vHelpContentItem) throws Exception{		
		final Map<String,String> subjects = ViewMessagesHelper.prepareViewMessagesUsingBussinesSessionUtilsLocale(vHelpContentItem.getHelpContentItemKey());
		final Iterator<String> subjectskeySetIterator = subjects.keySet().iterator();
		String key = null;
		while(subjectskeySetIterator.hasNext()){
			key = subjectskeySetIterator.next();
			if(!key.startsWith(vHelpContentItem.getHelpContentItemKey() + ".subject")){
				//if not an application module; then remove
				subjectskeySetIterator.remove();
			}//end if
		}//while(keySetIterator.hasNext()){
		/* order the content by key */
		vHelpContentItem.setSubjects(new TreeMap<String, String>(subjects));
	}

	@Override
	public void queryHelpContentItem(HelpContentItem vHelpContentItem) {
		// TODO Auto-generated method stub
		this.setUpHelpContentItemLayoutTab(vHelpContentItem);
	}
	
	private void setUpHelpContentItemTableLayoutTab(HelpContent helpContent){
		HelpContentItemTableLayout vHelpContentItemTableLayout = 
				new HelpContentItemTableLayout
				(this.VIEW_NAME, this, helpContent);
		vHelpContentItemTableLayout.setId("vHelpContentItemTableLayout");
		if (this.helpContentItemTableLayoutTab!=null)this.tabs.removeTab(this.helpContentItemTableLayoutTab);
		this.helpContentItemTableLayoutTab = this.tabs.addTab
				(vHelpContentItemTableLayout, 
						this.messages.get(this.VIEW_NAME + "tab.help.content.item.table"), 
						FontAwesome.DEVIANTART,
						1);
		if(helpContent!=null)this.tabs.setSelectedTab(1);
	}
	
	private void setUpHelpContentItemLayoutTab(HelpContentItem helpContentItem){
		HelpContentItemLayout vHelpContentItemLayout = new HelpContentItemLayout(this.VIEW_NAME, helpContentItem);
		vHelpContentItemLayout.setId("vHelpContentItemLayout");
		if(this.helpContentItemLayoutTab!=null) this.tabs.removeTab(this.helpContentItemLayoutTab);
		this.helpContentItemLayoutTab = this.tabs.addTab(
				vHelpContentItemLayout, 
				this.messages.get(this.VIEW_NAME + "tab.help.content.item"), 
				FontAwesome.BOOK, 
				2);
		if(helpContentItem!=null)this.tabs.setSelectedTab(2);
	}
}
