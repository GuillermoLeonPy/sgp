/**
 * 
 */
package py.com.kyron.sgp.gui.utils;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * @author testuser
 *
 */
public class OnDemandFileDownloader extends FileDownloader {
	private final Logger logger = LoggerFactory.getLogger(OnDemandFileDownloader.class);
	private static final long serialVersionUID = 1L;
	private final OnDemandStreamResource onDemandStreamResource;
	
	/**
	 * @param resource
	 */
	/*public OnDemandFileDownloader(Resource resource) {
		super(resource);
		// TODO Auto-generated constructor stub
	}*/


	public OnDemandFileDownloader (OnDemandStreamResource onDemandStreamResource) {
	    super(new StreamResource(onDemandStreamResource, ""));
	    /*this.onDemandStreamResource = checkNotNull(onDemandStreamResource,
	      "The given on-demand stream resource may never be null!");*/
	    this.onDemandStreamResource = onDemandStreamResource;
	}
	

	@Override
	public boolean handleConnectorRequest (VaadinRequest request, VaadinResponse response, String path)
	    throws IOException {
		logger.info( "\n ======================================== "
				+"\n OnDemandFileDownloader.handleConnectorRequest() method executed"
				+"\n ======================================== ");
	    getResource().setFilename(onDemandStreamResource.getFilename());
	    return super.handleConnectorRequest(request, response, path);
	}

	private StreamResource getResource () {
	   return (StreamResource) this.getResource("dl");
	}
	
	
	public interface OnDemandStreamResource extends StreamSource {
		    String getFilename ();
	}
}
