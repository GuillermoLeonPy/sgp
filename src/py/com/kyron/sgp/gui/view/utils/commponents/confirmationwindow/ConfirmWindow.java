package py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class ConfirmWindow extends Window {

	private Decision decision;
	private Button btnYes = new Button();
	private Button btnNo = new Button();
	private VerticalLayout layout = new VerticalLayout();
	private HorizontalLayout buttonsLayout = new HorizontalLayout();
	private boolean yesAccionRealized;
	private boolean noAccionRealized;
	
	public ConfirmWindow() {
		// TODO Auto-generated constructor stub
	}

	public ConfirmWindow(String caption) {
		super(caption);
		// TODO Auto-generated constructor stub
	}

	public ConfirmWindow(String caption, Component content) {
		super(caption, content);
		// TODO Auto-generated constructor stub
	}
	public ConfirmWindow(String caption, String question,String yes, String no, boolean visibleYesButton) {
			yesAccionRealized = false;
			noAccionRealized = false;
			setCaption(caption);
			btnYes.setCaption(yes);
			//btnYes.focus();
			btnYes.setVisible(visibleYesButton);
			btnNo.setCaption(no);
			btnNo.focus();
			setModal(true);
			center();
			buttonsLayout.addComponent(btnYes);
			buttonsLayout.setComponentAlignment(btnYes, Alignment.MIDDLE_CENTER);
			buttonsLayout.addComponent(btnNo);
			buttonsLayout.setComponentAlignment(btnNo, Alignment.MIDDLE_CENTER);
			layout.addComponent(new Label(question));
			layout.addComponent(buttonsLayout);
			setContent(layout);
			layout.setMargin(true);
			buttonsLayout.setMargin(true);
			buttonsLayout.setWidth("100%");
			setWidth("400px");
			setHeight("200px");
			setResizable(false);
			btnYes.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(Button.ClickEvent event) {
					yesAccionRealized = true;
					decision.yes(event);
					//close();
				}
			});
			btnNo.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(Button.ClickEvent event) {
					noAccionRealized = true;
					decision.no(event);
					//close();
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
	
	public void setDecision(Decision decision) {
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
				
}
