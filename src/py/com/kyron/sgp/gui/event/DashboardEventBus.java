package py.com.kyron.sgp.gui.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import py.com.kyron.sgp.gui.DashboardUI;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

/**
 * A simple wrapper for Guava event bus. Defines static convenience methods for
 * relevant actions.
 */
public class DashboardEventBus implements SubscriberExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(DashboardEventBus.class);
    private final EventBus eventBus = new EventBus(this);

    public static void post(final Object event) {
        DashboardUI.getDashboardEventbus().eventBus.post(event);
    }

    public static void register(final Object object) {
        DashboardUI.getDashboardEventbus().eventBus.register(object);
    }

    public static void unregister(final Object object) {
        DashboardUI.getDashboardEventbus().eventBus.unregister(object);
    }

    @Override
    public final void handleException(final Throwable exception,final SubscriberExceptionContext context) {
    	logger.error("error\n",exception);
        //exception.printStackTrace();
    }
}
