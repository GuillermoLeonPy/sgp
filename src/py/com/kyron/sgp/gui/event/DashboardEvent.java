package py.com.kyron.sgp.gui.event;

import java.util.Collection;

import py.com.kyron.sgp.bussines.comercialmanagement.domain.CurrencyDTO;
import py.com.kyron.sgp.bussines.personmanagement.domain.PersonDTO;
import py.com.kyron.sgp.gui.domain.Transaction;
import py.com.kyron.sgp.gui.view.DashboardViewType;

/*
 * Event bus events used in Dashboard are listed here as inner classes.
 */
public abstract class DashboardEvent {

    public static final class UserLoginRequestedEvent {
        private final String userName, password;
        private final PersonDTO personDTO;

        public UserLoginRequestedEvent(final String userName, final String password, PersonDTO personDTO) {
            this.userName = userName;
            this.password = password;
            this.personDTO = personDTO;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

		public PersonDTO getPersonDTO() {
			return personDTO;
		}
    }//public static final class UserLoginRequestedEvent

    public static class BrowserResizeEvent {

    }

    public static class UserLoggedOutEvent {

    }

    public static class NotificationsCountUpdatedEvent {
    }

    public static final class ReportsCountUpdatedEvent {
        private final int count;

        public ReportsCountUpdatedEvent(final int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }//public static final class ReportsCountUpdatedEvent

    public static final class TransactionReportEvent {
        private final Collection<Transaction> transactions;

        public TransactionReportEvent(final Collection<Transaction> transactions) {
            this.transactions = transactions;
        }

        public Collection<Transaction> getTransactions() {
            return transactions;
        }
    }//public static final class TransactionReportEvent

    public static final class PostViewChangeEvent {
        private final DashboardViewType view;

        public PostViewChangeEvent(final DashboardViewType view) {
            this.view = view;
        }        

        public DashboardViewType getView() {
            return view;
        }
    }//public static final class PostViewChangeEvent
    
    

    public static class CloseOpenWindowsEvent {
    }

    public static class ProfileUpdatedEvent {
    }
    
    public static final class EditMonedaEvent {
    	private final CurrencyDTO moneda;
    	
    	public EditMonedaEvent(final CurrencyDTO moneda){
    		this.moneda = moneda;
    	}
    	
    	public CurrencyDTO getMoneda(){
    		return this.moneda;
    	}
    }//public static final class EditMonedaEvent

}//public abstract class DashboardEvent
