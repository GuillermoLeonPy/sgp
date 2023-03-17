package py.com.kyron.sgp.gui.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.bussines.application.utils.ApplicationUtils;
import py.com.kyron.sgp.bussines.application.utils.ServicesBeanNames;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.bussines.session.utils.BussinesSessionUtils;
import py.com.kyron.sgp.gui.DashboardUI;
import py.com.kyron.sgp.gui.component.ProfilePreferencesWindow;
import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.domain.Transaction;
import py.com.kyron.sgp.gui.domain.User;
import py.com.kyron.sgp.gui.event.DashboardEventBus;
import py.com.kyron.sgp.gui.event.DashboardEvent.NotificationsCountUpdatedEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.PostViewChangeEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.ProfileUpdatedEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.ReportsCountUpdatedEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.TransactionReportEvent;
import py.com.kyron.sgp.gui.event.DashboardEvent.UserLoggedOutEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.AcceptItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.DragAndDropWrapper.DragStartMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A responsive menu component providing user information and the controls for
 * primary navigation between the views.
 */
@SuppressWarnings({ "serial", "unchecked" })
public final class DashboardMenu extends CustomComponent {
	private final Logger logger = LoggerFactory.getLogger(DashboardMenu.class);
    public static final String ID = "dashboard-menu";
    public static final String REPORTS_BADGE_ID = "dashboard-menu-reports-badge";
    public static final String NOTIFICATIONS_BADGE_ID = "dashboard-menu-notifications-badge";
    private static final String STYLE_VISIBLE = "valo-menu-visible";
    private Label notificationsBadge;
    private Label reportsBadge;
    private MenuItem settingsItem;
    private int menuItemCountByModule;
	private Map<String,String> messages;
	private final String VIEW_NAME = "main.menu.";
	private Set<String> programKeysBySession;

    public DashboardMenu() {
    	try{
	        setPrimaryStyleName("valo-menu");
	        setId(ID);
	        setSizeUndefined();
	        this.prepareMessages();
	        //this.verifyMessages();
	        // There's only one DashboardMenu per UI so this doesn't need to be
	        // unregistered from the UI-scoped DashboardEventBus.
	        DashboardEventBus.register(this);
	        setCompositionRoot(buildContent());
    	}catch(Exception e){
    		logger.error("\nerror", e);
    	}
    }

    private Component buildContent() {
        final CssLayout menuContent = new CssLayout();
        menuContent.addStyleName("sidebar");
        menuContent.addStyleName(ValoTheme.MENU_PART);       
        menuContent.addStyleName("no-vertical-drag-hints");
        menuContent.addStyleName("no-horizontal-drag-hints");
        menuContent.setWidth(null);
        menuContent.setHeight("100%");
        

        menuContent.addComponent(buildTitle());
        menuContent.addComponent(buildUserMenu());
        menuContent.addComponent(buildToggleButton());
        //menuContent.addComponent(buildMenuItems());
        //menuContent.addComponent(buildMenuItemsSgp());
        menuContent.addComponent(buildMenuItemsSgpCssLayout());

        return menuContent;
    }

    private Component buildTitle() {
        Label logo = new Label(/*this.messages.get("application.name.label.short") + " " 
        						+*/
        						"<strong>" + this.messages.get("application.name.label") + "</strong>",
                ContentMode.HTML);
        logo.setSizeUndefined();
        HorizontalLayout logoWrapper = new HorizontalLayout(logo);
        logoWrapper.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);
        logoWrapper.addStyleName("valo-menu-title");
        return logoWrapper;
    }

    private User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(
                User.class.getName());
    }

    private Component buildUserMenu() {
        final MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        //final User user = getCurrentUser();
        settingsItem = settings.addItem("", new ThemeResource("img/profile-pic-300px.jpg"), null);
        updateUserName(null);
        
        logger.info("\n*********************\n remember to check the nice screens in the user session menu, it can be useful "	+ "\n*********************\n");
        /*settingsItem.addItem(this.messages.get(this.VIEW_NAME + "user.profile.edit"), new Command(){
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, false);
            }
        });
        settingsItem.addItem(this.messages.get(this.VIEW_NAME + "user.profile.preferences"), new Command() {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                ProfilePreferencesWindow.open(user, true);
            }
        });
        settingsItem.addSeparator();*/
        settingsItem.addItem(this.messages.get(this.VIEW_NAME + "user.logout"), new Command()/*"Cerrar sesión"*/ {
            @Override
            public void menuSelected(final MenuItem selectedItem) {
                DashboardEventBus.post(new UserLoggedOutEvent());
            }
        });
        return settings;
    }

    private Component buildToggleButton() {
        Button valoMenuToggleButton = new Button("Menu", new ClickListener() {
            @Override
            public void buttonClick(final ClickEvent event) {
                if (getCompositionRoot().getStyleName().contains(STYLE_VISIBLE)) {
                    getCompositionRoot().removeStyleName(STYLE_VISIBLE);
                } else {
                    getCompositionRoot().addStyleName(STYLE_VISIBLE);
                }
            }
        });
        valoMenuToggleButton.setIcon(FontAwesome.LIST);
        valoMenuToggleButton.addStyleName("valo-menu-toggle");
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        valoMenuToggleButton.addStyleName(ValoTheme.BUTTON_SMALL);
        return valoMenuToggleButton;
    }

    private Component buildMenuItemsSgp(){
    	CssLayout menuItemsLayout = new CssLayout();
    	menuItemsLayout.setPrimaryStyleName("valo-menuitems");
    	Label label = null;
    	Component subMenuItems = null;
    	for (int a = 1; a <= 4; a++){
    		//if (a==1){
                label = new Label("Gestión comercial", ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                subMenuItems = buildMenuItemsByModule("gestioncomercial");
    		//}
//    		if (a==2){
//                label = new Label("Gestión de producción", ContentMode.HTML);
//                label.setPrimaryStyleName("valo-menu-subtitle");
//                label.addStyleName("h4");
//                label.setSizeUndefined();
//                subMenuItems = buildMenuItemsByModule("quicktickets");
//    		}
//    		if (a==3){
//                label = new Label("Gestión de almacenamiento", ContentMode.HTML);
//                label.setPrimaryStyleName("valo-menu-subtitle");
//                label.addStyleName("h4");
//                label.setSizeUndefined();
//                subMenuItems = buildMenuItemsByModule("quicktickets");
//    		}
//    		if (a==4){
//                label = new Label("Gestión de movimientos", ContentMode.HTML);
//                label.setPrimaryStyleName("valo-menu-subtitle");
//                label.addStyleName("h4");
//                label.setSizeUndefined();
//                subMenuItems = buildMenuItemsByModule("quicktickets");
//    		}
    		label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + menuItemCountByModule + "</span>");
    		menuItemsLayout.addComponent(label);
    		menuItemsLayout.addComponent(subMenuItems);
    	}
    	return menuItemsLayout;
    }//private Component buildMenuItemsSgp()
    
    private Component buildMenuItemsSgpCssLayout(){
    	CssLayout menuItemsLayout = new CssLayout();
    	menuItemsLayout.setPrimaryStyleName("valo-menuitems");
    	Component[] vector = new Component[]{};
    	//Arrays.asList(vector);
    	//menuItemsLayout.addComponents(vector);
    	List<Component> listComponents = new ArrayList<Component>();
    	//listComponents.toArray(vector);
    	Label label = null;
    	
    	for (int a = -1; a <= 5; a++){
    		if (a==-1){
                label = new Label((this.messages.get(this.VIEW_NAME + "module.application.report.management.")), ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                //listComponents = buildMenuListItemsByModule("quicktickets");
                listComponents = buildMenuListItemsByModule("module.application.report.management.");
    		}
    		if (a==0){
                label = new Label((this.messages.get(this.VIEW_NAME + "module.cash.movements.management.")), ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                //listComponents = buildMenuListItemsByModule("quicktickets");
                listComponents = buildMenuListItemsByModule("module.cash.movements.management.");
    		}
    		if (a==1){
                label = new Label(this.messages.get(this.VIEW_NAME + "module.comercial.management.") , ContentMode.HTML);/*"Gestión comercial"*/
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                //listComponents = buildMenuListItemsByModule("gestioncomercial");
                listComponents = buildMenuListItemsByModule("module.comercial.management.");
    		}
    		if (a==2){
                label = new Label(this.messages.get(this.VIEW_NAME + "module.stock.management."), ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                listComponents = buildMenuListItemsByModule("module.stock.management.");
    		}
    		if (a==3){
                label = new Label(this.messages.get(this.VIEW_NAME + "module.producction.management."), ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                listComponents = buildMenuListItemsByModule("module.producction.management.");
    		}
    		if (a==4){
                label = new Label(this.messages.get(this.VIEW_NAME + "module.application.credentials.management."), ContentMode.HTML);/*"Gestión de producción"*/
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                listComponents = buildMenuListItemsByModule("module.application.credentials.management.");
    		}
    		if (a==5){
                label = new Label((this.messages.get(this.VIEW_NAME + "module.application.utilities.")), ContentMode.HTML);
                label.setPrimaryStyleName("valo-menu-subtitle");
                label.addStyleName("h4");
                label.setSizeUndefined();
                listComponents = buildMenuListItemsByModule("module.application.utilities.");
    		}
    		if(listComponents!=null && !listComponents.isEmpty()){
	    		label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + menuItemCountByModule + "</span>");
	    		menuItemsLayout.addComponent(label);
	    		vector = new Component[]{};
	    		vector = listComponents.toArray(vector);
	    		menuItemsLayout.addComponents(vector);
    		}
    	}
    	return menuItemsLayout;
    }//private Component buildMenuItemsSgp()
    
    private Component buildMenuItems() {
    	menuItemCountByModule = 0;
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");        
        for (final DashboardViewType view : DashboardViewType.values()){
        	if("quicktickets".equalsIgnoreCase(view.getModule())){
		        	menuItemCountByModule++;
		            Component menuItemComponent = new ValoMenuItemButton(view);            
		            if (view == DashboardViewType.REPORTS) {
		                // Add drop target to reports button
		                DragAndDropWrapper reports = new DragAndDropWrapper(menuItemComponent);
		                reports.setSizeUndefined();
		                reports.setDragStartMode(DragStartMode.NONE);
		                reports.setDropHandler(new DropHandler(){
							   @Override
							   public void drop(final DragAndDropEvent event) {
							          UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REPORTS.getViewName());
							          Table table = (Table) event.getTransferable().getSourceComponent();
							          DashboardEventBus.post(new TransactionReportEvent((Collection<Transaction>) table.getValue()));
							   }					
							   @Override
							   public AcceptCriterion getAcceptCriterion() {
							          return AcceptItem.ALL;
							   }
		                });//reports.setDropHandler(new DropHandler()
		                menuItemComponent = reports;
		            }//if (view == DashboardViewType.REPORTS)
		
		            if (view == DashboardViewType.DASHBOARD) {
		                notificationsBadge = new Label();
		                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,notificationsBadge);
		            }//if (view == DashboardViewType.DASHBOARD)
		            if (view == DashboardViewType.REPORTS) {
		                reportsBadge = new Label();
		                reportsBadge.setId(REPORTS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,reportsBadge);
		            }//if (view == DashboardViewType.REPORTS)
		            menuItemsLayout.addComponent(menuItemComponent);
        	}//if("quicktickets".equalsIgnoreCase(view.getModule()))
        }//for (final DashboardViewType view : DashboardViewType.values())        
        return menuItemsLayout;
    }//private Component buildMenuItems()
    
    private List<Component> buildMenuListItemsByModule(String module) {
    	menuItemCountByModule = 0;
    	List<Component> listComponents = new ArrayList<Component>();
        for (final DashboardViewType view : DashboardViewType.values()){        	
        	if(module.equalsIgnoreCase(view.getModule()) && view.isShowInMainMenu() 
        	&& this.checkUserSessionCredentialsForProgram(this.VIEW_NAME + module + view.getViewName())){
        			//logger.info("\nbuildMenuListItemsByModule : " + view.getViewName());
        			menuItemCountByModule++;
		            Component menuItemComponent = new ValoMenuItemButton(view);		
		            if (view == DashboardViewType.REPORTS) {
		                // Add drop target to reports button
		                DragAndDropWrapper reports = new DragAndDropWrapper(menuItemComponent);
		                reports.setSizeUndefined();
		                reports.setDragStartMode(DragStartMode.NONE);
		                reports.setDropHandler(new DropHandler(){
							   @Override
							   public void drop(final DragAndDropEvent event) {
							          UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REPORTS.getViewName());
							          Table table = (Table) event.getTransferable().getSourceComponent();
							          DashboardEventBus.post(new TransactionReportEvent((Collection<Transaction>) table.getValue()));
							   }					
							   @Override
							   public AcceptCriterion getAcceptCriterion() {
							          return AcceptItem.ALL;
							   }
		                });//reports.setDropHandler(new DropHandler()
		                menuItemComponent = reports;
		            }//if (view == DashboardViewType.REPORTS)
		
		            if (view == DashboardViewType.DASHBOARD) {
		                notificationsBadge = new Label();
		                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,notificationsBadge);
		            }//if (view == DashboardViewType.DASHBOARD)
		            if (view == DashboardViewType.REPORTS) {
		                reportsBadge = new Label();
		                reportsBadge.setId(REPORTS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,reportsBadge);
		            }//if (view == DashboardViewType.REPORTS)
		            listComponents.add(menuItemComponent);
        	}//if(module.equalsIgnoreCase(view.getModule()) && view.isShowInMainMenu())
        }//for (final DashboardViewType view : DashboardViewType.values())
        return listComponents;
    }//private Component buildMenuItems()
    
    private boolean checkUserSessionCredentialsForProgram(final String programKey){    	
    	try{
    		if(this.programKeysBySession == null) this.prepareProgramKeysBySession();
    		if(this.programKeysBySession == null || this.programKeysBySession.isEmpty()) return false;
    		logger.info("\n#####################checkUserSessionCredentialsForProgram#####################\n"
    		+ programKey + ":" + this.programKeysBySession.contains(programKey) 
    		+ "\n###############################################################################\n");
    		return this.programKeysBySession.contains(programKey);
		} catch (Exception e) {
		// TODO Auto-generated catch block
			logger.error("\n###############\nerror checking user session credentials"
			+"\nprogramKey : " + programKey + "\n###############\n", e);
			return false;
		}
    }
    
    private void prepareProgramKeysBySession() throws Exception{
		BussinesSessionUtils bussinesSessionUtils = (BussinesSessionUtils)
		SgpApplicationContextAware.getXmlWebApplicationContext().getBean(ServicesBeanNames.BUSSINES_SESSION_UTILS);
		this.programKeysBySession = bussinesSessionUtils.getRawSessionData().getProgramKeysBySession();    	
    }
    
    
    private Component buildMenuItemsByModule(String module) {
    	menuItemCountByModule = 0;
        CssLayout menuItemsLayout = new CssLayout();
        menuItemsLayout.addStyleName("valo-menuitems");
        for (final DashboardViewType view : DashboardViewType.values()){
        	if(module.equalsIgnoreCase(view.getModule())){        	
        			menuItemCountByModule++;
		            Component menuItemComponent = new ValoMenuItemButton(view);		
		            if (view == DashboardViewType.REPORTS) {
		                // Add drop target to reports button
		                DragAndDropWrapper reports = new DragAndDropWrapper(menuItemComponent);
		                reports.setSizeUndefined();
		                reports.setDragStartMode(DragStartMode.NONE);
		                reports.setDropHandler(new DropHandler(){
							   @Override
							   public void drop(final DragAndDropEvent event) {
							          UI.getCurrent().getNavigator().navigateTo(DashboardViewType.REPORTS.getViewName());
							          Table table = (Table) event.getTransferable().getSourceComponent();
							          DashboardEventBus.post(new TransactionReportEvent((Collection<Transaction>) table.getValue()));
							   }					
							   @Override
							   public AcceptCriterion getAcceptCriterion() {
							          return AcceptItem.ALL;
							   }
		                });//reports.setDropHandler(new DropHandler()
		                menuItemComponent = reports;
		            }//if (view == DashboardViewType.REPORTS)
		
		            if (view == DashboardViewType.DASHBOARD) {
		                notificationsBadge = new Label();
		                notificationsBadge.setId(NOTIFICATIONS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,notificationsBadge);
		            }//if (view == DashboardViewType.DASHBOARD)
		            if (view == DashboardViewType.REPORTS) {
		                reportsBadge = new Label();
		                reportsBadge.setId(REPORTS_BADGE_ID);
		                menuItemComponent = buildBadgeWrapper(menuItemComponent,reportsBadge);
		            }//if (view == DashboardViewType.REPORTS)
		            menuItemsLayout.addComponent(menuItemComponent);
        	}//if(module.equalsIgnoreCase(view.getModule()))
        }//for (final DashboardViewType view : DashboardViewType.values())
        return menuItemsLayout;
    }//private Component buildMenuItems()

    private Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
        CssLayout dashboardWrapper = new CssLayout(menuItemButton);
        dashboardWrapper.addStyleName("badgewrapper");
        dashboardWrapper.addStyleName(ValoTheme.MENU_ITEM);
        badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
        badgeLabel.setWidthUndefined();
        badgeLabel.setVisible(false);
        dashboardWrapper.addComponent(badgeLabel);
        return dashboardWrapper;
    }

    @Override
    public void attach() {
        super.attach();
//        updateNotificationsCount(null);
    }

    @Subscribe
    public void postViewChange(final PostViewChangeEvent event) {
        // After a successful view change the menu can be hidden in mobile view.
        getCompositionRoot().removeStyleName(STYLE_VISIBLE);
    }

    @Subscribe
    public void updateNotificationsCount(final NotificationsCountUpdatedEvent event) {
        int unreadNotificationsCount = DashboardUI.getDataProvider().getUnreadNotificationsCount();
        notificationsBadge.setValue(String.valueOf(unreadNotificationsCount));
        notificationsBadge.setVisible(unreadNotificationsCount > 0);
    }

    @Subscribe
    public void updateReportsCount(final ReportsCountUpdatedEvent event) {
        reportsBadge.setValue(String.valueOf(event.getCount()));
        reportsBadge.setVisible(event.getCount() > 0);
    }

    @Subscribe
    public void updateUserName(final ProfileUpdatedEvent event) {
        User user = getCurrentUser();
        settingsItem.setText(user.getFirstName() + " " + user.getLastName());
    }
    
    private void prepareMessages() throws Exception{    	
    	BussinesSessionUtils bussinesSessionUtils =	(BussinesSessionUtils)SgpApplicationContextAware.getXmlWebApplicationContext().getBean("bussinesSessionUtils");
    	this.setLocale(bussinesSessionUtils.getRawSessionData().getUserSessionLocale());
    	this.messages = bussinesSessionUtils.getApplicationUtils().loadMessagesByViewAndKeysAndUserSessionLocale(this.VIEW_NAME, this.getLocale());
    }
    
    private void verifyMessages(){
    	logger.info("\n*************************DashboardMenu, checking loaded messages\n*************************");
    	Set<String> keys = this.messages.keySet();
    	for(String key: keys){
    		logger.info("\nkey : "  + key + ", value : "+this.messages.get(key));
    	}
    }

    public final class ValoMenuItemButton extends Button {

        private static final String STYLE_SELECTED = "selected";
        private final DashboardViewType view;
        public ValoMenuItemButton(final DashboardViewType view) {
            this.view = view;
            setPrimaryStyleName("valo-menu-item");
            setIcon(view.getIcon());
            //setCaption(view.getViewName().substring(0, 1).toUpperCase() + view.getViewName().substring(1));
//            logger.info("\nValoMenuItemButton\nVIEW_NAME + view.getModule() + view.name(): " + VIEW_NAME + view.getModule() + view.getViewName()
//            		+"\nmessages.get(VIEW_NAME + view.getModule() + view.name()) : " + messages.get(VIEW_NAME + view.getModule() + view.getViewName()));
            setCaption(messages.get(VIEW_NAME + view.getModule() + view.getViewName()));//main.menu.module.comercial.management.register.currency
            DashboardEventBus.register(this);
            addClickListener(new ClickListener() {
                @Override
                public void buttonClick(final ClickEvent event) {
                    UI.getCurrent().getNavigator().navigateTo(view.getViewName());
                }
            });

        }

        @Subscribe
        public void postViewChange(final PostViewChangeEvent event) {
            removeStyleName(STYLE_SELECTED);
            if (event.getView() == view) {
                addStyleName(STYLE_SELECTED);
            }
        }
    }//public final class ValoMenuItemButton extends Button   

}
