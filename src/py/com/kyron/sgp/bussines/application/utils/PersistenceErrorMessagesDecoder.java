package py.com.kyron.sgp.bussines.application.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinSession;

import py.com.kyron.sgp.gui.config.spring.SgpApplicationContextAware;
import py.com.kyron.sgp.gui.utils.SgpUtils;

public class PersistenceErrorMessagesDecoder {

	private final Logger logger = LoggerFactory.getLogger(PersistenceErrorMessagesDecoder.class);
	private String paramValue;
	private int paramsArraylength;
	private int paramsArrayCounter;
	private Object[] formattedParams; 
	private SgpUtils sgpUtils;
	private int indexFromErrorKey;
	private int indexToErrorKey;
	private int indexFromErrorParams;
	private int indexToErrorParams;
	private String errorKeyMessage;
	private String errorParamsMessage;
	
	public PersistenceErrorMessagesDecoder() {
		// TODO Auto-generated constructor stub
		this.sgpUtils = new SgpUtils();
	}
	

	
	public String getDataBaseFunctionErrorKeyFromException(Exception exception){
		try{
			this.indexFromErrorKey = exception.getCause().getMessage().indexOf("py.com.kyron.sgp.persistence");
			if(this.indexFromErrorKey < 0 )//try the common bussines prefix
				this.indexFromErrorKey = exception.getCause().getMessage().indexOf("py.com.kyron.sgp.bussines");
			this.indexToErrorKey = exception.getCause().getMessage().indexOf(".error") + 6;
			this.printExceptionMessageErrorKey(exception);
			if(this.indexFromErrorKey < 0 || this.indexToErrorKey < 0 || this.indexFromErrorKey > this.indexToErrorKey)return "py.com.kyron.sgp.persistence.unexpected.error";
			return exception.getCause().getMessage().substring(this.indexFromErrorKey, this.indexToErrorKey);
		}catch(Exception e){
			logger.error("\nError decoding persistence error message, sending standard message key", e);
			return "py.com.kyron.sgp.persistence.unexpected.error";
		}
	}

	private void printExceptionMessageErrorKey(Exception exception){
		this.errorKeyMessage = exception.getCause().getMessage();
		logger.info( "\n ======================================"
					+"\n error key message : "
					+ this.errorKeyMessage
					+"\n error message length: " + this.errorKeyMessage.length()
					+"\n this.indexFromErrorKey : " + this.indexFromErrorKey
					+"\n this.indexToErrorKey : " + this.indexToErrorKey
					+"\n ======================================");		
	}
	
	public Object[] getDataBaseFunctionErrorParamsFromException(Exception exception){
		try{
			if(!exception.getCause().getMessage().contains("end.of.message"))
				return null;
			int indexFrom = exception.getCause().getMessage().indexOf(".error") + 7;
			int indexTo = exception.getCause().getMessage().indexOf("end.of.message");
			return exception.getCause().getMessage().substring(indexFrom, indexTo).split("");
		}catch(Exception e){
			logger.error("\nerror", e);
			return null;
		}
	}

	public Object[] getDataBaseFunctionErrorParamsFromExceptionUsingLocale(Exception exception, Locale locale){
		try{
			if(!exception.getCause().getMessage().contains("end.of.message"))
				return null;
			this.indexFromErrorParams = exception.getCause().getMessage().indexOf(".error") + 7;
			this.indexToErrorParams = exception.getCause().getMessage().indexOf("end.of.message");
			this.printExceptionMessageParams(exception);
			if(this.indexFromErrorParams > this.indexToErrorParams)return null;
			return this.formatErrorParamsBySessionLocale(
					exception.getCause().getMessage().substring(this.indexFromErrorParams, this.indexToErrorParams).split(""),locale
					);
		}catch(Exception e){
			logger.error("\nerror", e);
			return null;
		}
	}
	
	private void printExceptionMessageParams(Exception exception){
		this.errorParamsMessage = exception.getCause().getMessage();
		logger.info( "\n ======================================"
					+"\n error message params : "
					+ this.errorParamsMessage
					+"\n error message length: " + this.errorParamsMessage.length()
					+"\n this.indexFromErrorParams : " + this.indexFromErrorParams
					+"\n this.indexToErrorParams : " + this.indexToErrorParams
					+"\n ======================================");		
	}
	
	public Object[] formatErrorParamsBySessionLocale(String [] params,Locale locale){
		if(params != null){
			this.formattedParams = new Object[params.length];
			this.paramsArraylength = params.length;
			for(this.paramsArrayCounter=0; this.paramsArrayCounter < this.paramsArraylength; this.paramsArrayCounter++){
				this.paramValue = params[this.paramsArrayCounter];
				if(this.paramValue.startsWith("#-date-#"))
					this.formattedParams[this.paramsArrayCounter] = this.decodeDateContentFromParam(this.paramValue, locale);
				else if (this.paramValue.startsWith("#-numeric-#"))
					this.formattedParams[this.paramsArrayCounter] = this.sgpUtils.decodeNumericContentFromParam(this.paramValue.substring("#-numeric-#".length()),locale);
				else if (this.paramValue.startsWith("#-key-#"))
					this.formattedParams[this.paramsArrayCounter] = 
					SgpApplicationContextAware.getMessage(this.paramValue.substring("#-key-#".length()), null, this.paramValue.substring("#-key-#".length()) + ": key was not found" + ": key was not found",locale);
				else					
					this.formattedParams[this.paramsArrayCounter] = this.paramValue;
			}
			return this.formattedParams;
		}
		return null;
	}
	

	private String decodeDateContentFromParam(String dateParam,Locale locale){
		try {
			return this.formatDateParam(dateParam.substring("#-date-#".length()),locale);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("\n error decoding date param from database, returning the param without formatting",e);
			return dateParam.substring("#-date-#".length());
		}		
	}
	
	private String formatDateParam(String datParam,Locale locale) throws ParseException{
		return SgpUtils.parseDateParamBySessionLocale(SgpUtils.parseDateFromString(datParam), locale);
	}	
}
