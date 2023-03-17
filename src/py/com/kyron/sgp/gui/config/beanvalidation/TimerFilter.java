package py.com.kyron.sgp.gui.config.beanvalidation;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerFilter implements Filter {

	static final Logger logger = LoggerFactory.getLogger(TimerFilter.class);
	public TimerFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,FilterChain filterChain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try{
			logger.info("\nTimerFilter.doFilter(ServletRequest req, ServletResponse res,FilterChain filterChain)"
			+"\nreq.getLocale(): " + req.getLocale());
			ClientLocaleThreadLocal.set(req.getLocale());         
	    	filterChain.doFilter(req, res);
		}finally{
			ClientLocaleThreadLocal.remove();
			logger.info("\n finally ClientLocaleThreadLocal.remove()..." 
			+ "\nClientLocaleThreadLocal.get(): " + ClientLocaleThreadLocal.get());
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
