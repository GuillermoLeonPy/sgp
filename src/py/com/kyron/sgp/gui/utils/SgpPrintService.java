package py.com.kyron.sgp.gui.utils;

import java.awt.print.PrinterJob;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.PrintServiceAttributeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SgpPrintService {

	private final Logger logger = LoggerFactory.getLogger(SgpPrintService.class);
	private PrintService printService;
	private PrintServiceAttributeSet printServiceAttributeSet;
	private Attribute[] attribute;
	private SimpleDoc doc;
	private DocPrintJob job;
	private Attribute anAttribute;
	/*private BufferedOutputStream bufferedOutputStream;
	private OutputStream outputStream;
	private File pdf;
	private DocFlavor docFlavor;*/
	
	public SgpPrintService() {
		// TODO Auto-generated constructor stub
	}
	
	public void printPdfFileByByteArray(final byte[] file, final String printService) throws PrintException, IOException{
		this.findPrintService(printService);
		this.consoleOutPutPrintServiceAttributeList();
		this.createPrintJob(file);
	}

    private void findPrintService(String printerName) {
        printerName = printerName.toLowerCase();
        this.printService = null;
        // Get array of all print services
        final PrintService[] services = PrinterJob.lookupPrintServices();
        // Retrieve a print service from the array
        for (int index = 0; this.printService == null && index < services.length; index++) {
            if (services[index].getName().toLowerCase().indexOf(printerName) >= 0) {
            	this.printService = services[index];
            }
        }
    }
    
    private void consoleOutPutPrintServiceAttributeList(){
		this.printServiceAttributeSet = this.printService.getAttributes();
		final Class<?>[] vClass =  this.printService.getSupportedAttributeCategories();
		int vClassLength  = vClass.length;
		for(int index = 0; index < vClassLength ; index++){
			Class<?> anClass = vClass[index]; 
			logger.info("\n " + anClass.getName());
			
		}
		this.attribute = this.printServiceAttributeSet.toArray();
		final int vAttributeLength = this.attribute.length;
		logger.info(	"\n ====================================="
						+	"\n the printer:" + this.printService.getName() + " attribute list"
						+	"\n =====================================");
		for(int index = 0; index < vAttributeLength ; index++){
			this.anAttribute = this.attribute[index];
			logger.info("\n " + anAttribute.getName() /*+ ": "*/ );
		}    	
    }
    
    private void createPrintJob(final byte[] file) throws PrintException, IOException{
    	/*this.pdf = File.createTempFile("output.", ".pdf");
    	this.outputStream = new FileOutputStream(this.pdf);
    	this.outputStream.write(file);
    	this.bufferedOutputStream = new BufferedOutputStream(this.outputStream);*/
    	this.doc = new SimpleDoc(file, DocFlavor.BYTE_ARRAY.PDF, null);
    	this.job = this.printService.createPrintJob();
    	this.job.print(this.doc, null);    	
    }
}
