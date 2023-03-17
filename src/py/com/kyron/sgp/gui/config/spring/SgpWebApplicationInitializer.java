/**
 * 
 */
package py.com.kyron.sgp.gui.config.spring;

import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.WebApplicationInitializer;

/**
 * @author testuser
 *
 */
public class SgpWebApplicationInitializer implements WebApplicationInitializer {
	private Logger log = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	
	public SgpWebApplicationInitializer() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.WebApplicationInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		log.info("\n========================================================================================"
				+"\nbinding the vaadin servlet to the spring application context to define the session scope"
				+"\n========================================================================================"
		+"\nContext Path : "+servletContext.getContextPath()
		+"\nContext Name : " + servletContext.getServletContextName()
		+"\n--------------------------------------------------------");
		Enumeration<String> initParametersNames = servletContext.getInitParameterNames();
		while(initParametersNames.hasMoreElements()){
			String parameterName = initParametersNames.nextElement();
			log.info("\nparameter Name : " + parameterName + ", value: " + servletContext.getInitParameter(parameterName));
		}
		servletContext.addListener(new RequestContextListener());
		SgpApplicationContextAware.setXmlWebApplicationContext(new XmlWebApplicationContext());
		SgpApplicationContextAware.getXmlWebApplicationContext().setConfigLocation("/WEB-INF/sgp-servlet.xml");
		SgpApplicationContextAware.getXmlWebApplicationContext().setServletContext(servletContext);
		SgpApplicationContextAware.getXmlWebApplicationContext().refresh();
	}

}
