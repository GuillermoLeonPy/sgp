package py.com.kyron.sgp.gui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.RuleBasedNumberFormat;

public class SgpUtils {

	private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";
	private static final String DATE_FORMAT_WITH_OUT_HOUR = "dd/MM/yyyy";
	private static final String DATE_FORMAT_EN = "MM/dd/yyyy HH:mm:ss";
	private static final String DATE_FORMAT_EN_WITH_OUT_HOUR = "MM/dd/yyyy";
	public static final SimpleDateFormat dateFormater = new SimpleDateFormat(DATE_FORMAT);
	public static final SimpleDateFormat dateFormater_EN = new SimpleDateFormat(DATE_FORMAT_EN);
	public static final SimpleDateFormat dateFormaterWithOutHour = new SimpleDateFormat(DATE_FORMAT_WITH_OUT_HOUR);
	public static final SimpleDateFormat dateFormater_EN_WithOutHour = new SimpleDateFormat(DATE_FORMAT_EN_WITH_OUT_HOUR);
	private NumberFormat nf;
	private DecimalFormat df;
	private final String BIG_DECIMAL_PATTERN = "###,###.###";
	private static final String DATE_FORMAT_WITH_DAY_MONTH = "EEEEE d, MMMMM yyyy";
	private static final String DATE_FORMAT_YEAR_MONTH = "yyyyMM";
	private static final String DATE_FORMAT_yyyyMMddHHmmss = "yyyyMMddHHmmss";
	public static SimpleDateFormat dateFormater_YEAR_MONTH;
	public static SimpleDateFormat dateFormater_DAY_MONTH;
	public static SimpleDateFormat dateFormater_yyyyMMddHHmmss;
	public static Calendar calendar;
	private com.ibm.icu.text.NumberFormat icuFormatter;
	
	private final Logger logger = LoggerFactory.getLogger(SgpUtils.class);
	
	public SgpUtils() {
		// TODO Auto-generated constructor stub
	}

	public static String dateToString(Date date){
		return dateFormater.format(date);
	}
	
	public static String getFirstWordFromString(String text){
		if(text.indexOf(" ") != -1)
			return text.substring(0, text.indexOf(" "));
		return text.substring(0, 14);
	}
	
	
	public static String parseDateParamBySessionLocale(Date dateParam, Locale locale){
		if(locale!=null && !locale.getLanguage().toLowerCase().contains("es"))
			return dateFormater_EN.format(dateParam);
		else
			return dateFormater.format(dateParam);
			
	}

	public static String parseDateParamWithOutHourBySessionLocale(Date dateParam, Locale locale){
		if(locale!=null && !locale.getLanguage().toLowerCase().contains("es"))
			return dateFormater_EN_WithOutHour.format(dateParam);
		else
			return dateFormaterWithOutHour.format(dateParam);
			
	}
	
	/* all dates loaded from database will follow the DATE_FORMAT*/
	public static Date parseDateFromString(String dateParam) throws ParseException{
		return dateFormater.parse(dateParam);
	}
	
	/*example: 88777666.99*/
	public  String decodeNumericContentFromParam(String numericDecimalParam,Locale locale){	    
		this.nf = NumberFormat.getNumberInstance(locale);
	    this.df = (DecimalFormat) nf;
	    this.df.applyPattern(this.BIG_DECIMAL_PATTERN);
	    return df.format(new BigDecimal(numericDecimalParam));
	}
	
	public Long checkPersonalCivilIdDocument(String ciRuc){
		try{
			return Long.parseLong(ciRuc);
		}catch(Exception e){
			logger.info("\nSearchPersonToolComponent.checkPersonalCivilIdDocument\nfilterText : " + ciRuc
					+ "not a Personal Civil Id Document");
		}
		return null;
	}
	
	public static int minutesBetweenDates(Date start, Date end){
		long vlong = ((end.getTime()/60000) - (start.getTime()/60000));
		return (int)vlong;
	}
	
	public String formatLongValueNumberByLocale(Long number, Locale locale){
		this.nf = NumberFormat.getNumberInstance(locale);
		return this.nf.format(number);
	}
	
	public String formatBigDecimalValueNumberByLocale(BigDecimal number, Locale locale){
		this.nf = NumberFormat.getNumberInstance(locale);
		return this.nf.format(number);
	}
	
	public static String formatDateWithDayAndMonthByLocale(Date date, Locale locale){
		dateFormater_DAY_MONTH = new SimpleDateFormat(DATE_FORMAT_WITH_DAY_MONTH, locale);
		return dateFormater_DAY_MONTH.format(date);
	}
	
	public static Date currentYearDayOfMonthByDayAndMonthNumber(int dayNumber,int monthNumber, Locale locale){
		calendar = Calendar.getInstance(locale);
		calendar.set(Calendar.DAY_OF_MONTH, dayNumber);
		calendar.set(Calendar.MONTH, monthNumber);
		return calendar.getTime();
	}
	
	public static Date firstDateOfMonth(Date anyDateOfMonth, Locale locale){
		calendar = Calendar.getInstance(locale);
		calendar.setTime(anyDateOfMonth);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date lastDateOfMonth(Date anyDateOfMonth, Locale locale){
		calendar = Calendar.getInstance(locale);
		calendar.setTime(anyDateOfMonth);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
		return calendar.getTime();
	}
	
	public static String formatDateYearMonthByLocale(Date date, Locale locale){
		dateFormater_YEAR_MONTH = new SimpleDateFormat(DATE_FORMAT_YEAR_MONTH, locale);
		return dateFormater_YEAR_MONTH.format(date);
	}
	
	public static String formatDate_yyyyMMddHHmmss_ByLocale(Date date, Locale locale){
		dateFormater_yyyyMMddHHmmss = new SimpleDateFormat(DATE_FORMAT_yyyyMMddHHmmss, locale);
		return dateFormater_yyyyMMddHHmmss.format(date);
	}
	
	public static Date setUpBeginDateFilter(Date beginDateFilter, Locale locale){
		calendar = Calendar.getInstance(locale);
		calendar.setTime(beginDateFilter);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static Date setUpEndDateFilter(Date endDateFilter, Locale locale){
		calendar = Calendar.getInstance(locale);
		calendar.setTime(endDateFilter);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getActualMaximum(Calendar.SECOND));
		calendar.set(Calendar.MILLISECOND, calendar.getActualMaximum(Calendar.MILLISECOND));
		return calendar.getTime();
	}
	
	public String spellOutLongNumber(Long number, Locale locale){
		this.icuFormatter = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
		return this.icuFormatter.format(number);
	}

	public String spellOutBigDecimalNumber(BigDecimal number, Locale locale){
		this.icuFormatter = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);
		return this.icuFormatter.format(number);
	}
	
}
