package dk.statsbiblioteket.doms.ingestexample;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import dk.statsbiblioteket.doms.central.CentralWebservice;
import dk.statsbiblioteket.doms.central.CentralWebserviceService;
import dk.statsbiblioteket.doms.central.DatastreamProfile;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** Test code. */
public class ExampleTest extends TestCase {
    /** Test code :-) */
    @org.junit.Test
    public void testExample() throws Exception {
        CentralWebservice webservice = new CentralWebserviceService(
                new URL("http://localhost:7880/centralWebservice-service/central/?wsdl"),
                new QName("http://central.doms.statsbiblioteket.dk/",
                          "CentralWebserviceService"))
                .getCentralWebservicePort();
        Map<String, Object> domsAPILogin = ((BindingProvider) webservice)
                .getRequestContext();
        domsAPILogin.put(BindingProvider.USERNAME_PROPERTY, "fedoraAdmin");
        domsAPILogin.put(BindingProvider.PASSWORD_PROPERTY, "fedoraAdminPass");

        for (dk.statsbiblioteket.doms.central.SearchResult result : webservice
                .findObjects("Valse", 0, 20)) {
            System.out.println(result.getPid() + ": " + result.getTitle());
        }

        List<DatastreamProfile> datastreams = webservice
                .getObjectProfile("uuid:bd64c76a-67c5-4aa3-bc51-acfce17df791")
                .getDatastreams();
        for (DatastreamProfile datastream : datastreams) {
            System.out.println(
                    datastream.getId() + "\t" + datastream.getFormatUri());
        }
        System.out.println();

        webservice.markInProgressObject(Arrays.asList(
                new String[]{"uuid:bd64c76a-67c5-4aa3-bc51-acfce17df791"}),
                                        "In progress");
        webservice.addFileFromPermanentURL(
                "uuid:bd64c76a-67c5-4aa3-bc51-acfce17df791", "valse1.bwf", null,
                "http://stage01/doms/valse1.bwf", "info:pronom/fmt/2",
                "Added file");
        webservice.markPublishedObject(Arrays.asList(
                new String[]{"uuid:bd64c76a-67c5-4aa3-bc51-acfce17df791"}),
                                       "Ready");

        List<DatastreamProfile> datastreams2 = webservice
                .getObjectProfile("uuid:bd64c76a-67c5-4aa3-bc51-acfce17df791")
                .getDatastreams();
        for (DatastreamProfile datastream : datastreams2) {
            System.out.println(
                    datastream.getId() + "\t" + datastream.getFormatUri());
        }
    }
}
