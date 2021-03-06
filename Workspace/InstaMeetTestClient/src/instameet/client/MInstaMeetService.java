
package instameet.client;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "mInstaMeetService", targetNamespace = "http://service/", wsdlLocation = "http://localhost:1234/InstaMeet?wsdl")
public class MInstaMeetService
    extends Service
{

    private final static URL MINSTAMEETSERVICE_WSDL_LOCATION;
    private final static WebServiceException MINSTAMEETSERVICE_EXCEPTION;
    private final static QName MINSTAMEETSERVICE_QNAME = new QName("http://service/", "mInstaMeetService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:1234/InstaMeet?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        MINSTAMEETSERVICE_WSDL_LOCATION = url;
        MINSTAMEETSERVICE_EXCEPTION = e;
    }

    public MInstaMeetService() {
        super(__getWsdlLocation(), MINSTAMEETSERVICE_QNAME);
    }

    public MInstaMeetService(WebServiceFeature... features) {
        super(__getWsdlLocation(), MINSTAMEETSERVICE_QNAME, features);
    }

    public MInstaMeetService(URL wsdlLocation) {
        super(wsdlLocation, MINSTAMEETSERVICE_QNAME);
    }

    public MInstaMeetService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, MINSTAMEETSERVICE_QNAME, features);
    }

    public MInstaMeetService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MInstaMeetService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns InstaMeetService
     */
    @WebEndpoint(name = "InstaMeetServicePort")
    public InstaMeetService getInstaMeetServicePort() {
        return super.getPort(new QName("http://service/", "InstaMeetServicePort"), InstaMeetService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns InstaMeetService
     */
    @WebEndpoint(name = "InstaMeetServicePort")
    public InstaMeetService getInstaMeetServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service/", "InstaMeetServicePort"), InstaMeetService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (MINSTAMEETSERVICE_EXCEPTION!= null) {
            throw MINSTAMEETSERVICE_EXCEPTION;
        }
        return MINSTAMEETSERVICE_WSDL_LOCATION;
    }

}
