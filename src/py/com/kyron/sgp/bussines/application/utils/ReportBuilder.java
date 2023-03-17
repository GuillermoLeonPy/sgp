package py.com.kyron.sgp.bussines.application.utils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportBuilder {

	private final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);
	private final String REPORTS_PATH = "reports";
	private final String REPORT_FILE_EXTENSION = ".jasper";
	private Map<String,Object > reportParameters;
	private JRDataSource reportDataSource;
	private List<Object> reportlistDataSource;
	private Map<String, List<?>> subReportlistDataSource;
	private String effective_path;
	
	
	public ReportBuilder(HttpSession vHttpSession) {
		// TODO Auto-generated constructor stub
		//HttpSession vHttpSession = (HttpSession)VaadinSession.getCurrent().getSession();
		this.effective_path = vHttpSession.getServletContext().getRealPath(this.REPORTS_PATH + "/") + "/";
		logger.info("\n ============================================== "
				+	"\n this.effective_path :  " + this.effective_path
				+	"\n ============================================== ");
		this.reportParameters = new HashMap<String,Object>();
		this.reportlistDataSource = new LinkedList<Object>();
		this.subReportlistDataSource = new HashMap<String,List<?>>();
		this.reportDataSource = null;
	}
	
	public void addSingleObjectToReportDataSource(Object object){
		this.reportlistDataSource.add(object);
	}
	
	public void addListObjectToReportDataSource(List<?> list){
		for(Object object : list)
			this.addSingleObjectToReportDataSource(object);
	}

	public byte[] getReportByteArray(String reportName) throws JRException{
		JasperPrint jasperPrint = JasperFillManager.fillReport(
				this.effective_path + reportName + this.REPORT_FILE_EXTENSION, 
				this.reportParameters, 
				new JRBeanCollectionDataSource(this.reportlistDataSource));
		/*ByteArrayOutputStream vByteArrayOutputStream = new ByteArrayOutputStream();
		JRPdfExporter vJRPdfExporter = new JRPdfExporter();*/
		return JasperExportManager.exportReportToPdf(jasperPrint);
		/*vJRPdfExporter.setExporterInput((ExporterInput)jasperPrint);
		vJRPdfExporter.setExporterOutput((OutputStreamExporterOutput) vByteArrayOutputStream);
		vJRPdfExporter.exportReport();
		return vByteArrayOutputStream.toByteArray();*/
	}
	
	public void addReportParameter(String key, Object value){
		this.reportParameters.put(key, value);
	}
}
