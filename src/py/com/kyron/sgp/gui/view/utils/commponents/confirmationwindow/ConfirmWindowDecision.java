package py.com.kyron.sgp.gui.view.utils.commponents.confirmationwindow;

import com.vaadin.ui.Button;

public interface ConfirmWindowDecision {
	public void yes(Button.ClickEvent event);
	public void no(Button.ClickEvent event);
}
