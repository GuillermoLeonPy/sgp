package py.com.kyron.sgp.gui.utils;

import javax.servlet.http.HttpSession;

import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;

public class SgpSessionInitListener implements SessionInitListener {

	private HttpSession httpSession;
	
	public SgpSessionInitListener() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		// TODO Auto-generated method stub
		
	}

}
