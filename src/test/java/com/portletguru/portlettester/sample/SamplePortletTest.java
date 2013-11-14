/**
 * 
 */
package com.portletguru.portlettester.sample;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.portletguru.portlettester.PortletTester;
import com.portletguru.portlettester.mocks.ActionRequestGenerator;
import com.portletguru.portlettester.mocks.ActionResponseGenerator;
import com.portletguru.portlettester.mocks.EventRequestGenerator;
import com.portletguru.portlettester.mocks.EventResponseGenerator;
import com.portletguru.portlettester.mocks.PortletConfigGenerator;
import com.portletguru.portlettester.mocks.RenderRequestGenerator;
import com.portletguru.portlettester.mocks.RenderResponseGenerator;
import com.portletguru.portlettester.mocks.ResourceRequestGenerator;
import com.portletguru.portlettester.mocks.ResourceResponseGenerator;

/**
 * @author Derek Linde Li
 *
 */
public class SamplePortletTest {
	
	private static PortletTester portletTester;
	private SamplePortlet portlet;
	
	@BeforeClass
	public static void setupClass() {
		portletTester = new PortletTester();
	}
	
	@Before
	public void setup() throws PortletException {
		portlet = new SamplePortlet();
		
		PortletConfigGenerator configGenerator = portletTester.getPortletConfigGenerator();
		configGenerator.addInitParameter(SamplePortlet.INIT_PARAM_KEY, "true");
		portletTester.initPortlet(portlet, configGenerator.generatePortletConfig());
	}
	
	
	/**
	 * Test doView() when portlet is not correctly initialized
	 *  
	 * @throws PortletException
	 */
	@Test
	public void testIncorrectPortletInitialization() throws PortletException {
		portletTester.initPortlet(portlet);
		RenderRequestGenerator requestGenerator = portletTester.getRenderRequestGenerator();
		RenderResponseGenerator responseGenerator = portletTester.getRenderResponseGenerator();
		
		PortletException e = null;
		try {
			portlet.doView(requestGenerator.generateRequest(), responseGenerator.generateResponse());
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (PortletException ex) {
			e = ex;
		}
		assertNotNull(e);
	}
	
	/**
	 * Test doView() is able to set saved user id as an attribute
	 * 
	 * @throws PortletException
	 * @throws IOException
	 */
	@Test
	public void testDoView() throws PortletException, IOException {
		
		String userId = "123";
		
		RenderRequestGenerator requestGenerator = portletTester.getRenderRequestGenerator();
		RenderResponseGenerator responseGenerator = portletTester.getRenderResponseGenerator();
		
		requestGenerator.setParameter(SamplePortlet.PARAM_USER_ID, userId);
		
		RenderRequest request = requestGenerator.generateRequest();
		RenderResponse response = responseGenerator.generateResponse();
		
		portlet.doView(request, response);
		
		assertEquals(userId, request.getAttribute(SamplePortlet.REQUEST_ATTR_SAVED_USER_ID));
	}
	
	
	/**
	 * Test processActionSaveUserId() is able to save user id to preferences and 
	 * save it as render parameter
	 * 
	 * @throws IOException
	 * @throws PortletException
	 */
	@Test
	public void testProcessActionSaveUserId() throws IOException, PortletException {
		
		String userId = "123";
		
		ActionRequestGenerator requestGenerator = portletTester.getActionRequestGenerator();
		ActionResponseGenerator responseGenerator = portletTester.getActionResponseGenerator();
		
		requestGenerator.setParameter(SamplePortlet.PARAM_USER_ID, userId);
		
		ActionRequest request = requestGenerator.generateRequest();
		ActionResponse response = responseGenerator.generateResponse();
		
		portlet.processActionSaveUserId(request, response);
		
		/* verify results */
		PortletPreferences preferences = request.getPreferences();
		assertEquals(userId, preferences.getValue(SamplePortlet.PREFS_PREFERRED_USER_ID, ""));
		
		Map<String, String[]> params = response.getRenderParameterMap();
		assertEquals(userId, params.get(SamplePortlet.PARAM_USER_ID)[0]);
	}
	
	/**
	 * Test proccessEventUserSelected() is able to save user id in session and change the 
	 * window state
	 * 
	 * @throws PortletException
	 * @throws IOException
	 */
	@Test
	public void testProcessEvent() throws PortletException, IOException {
		EventRequestGenerator requestGenerator = portletTester.getEventRequestGenerator();
		EventResponseGenerator responseGenerator = portletTester.getEventResponseGenerator();
		
		String userId = "123";
		requestGenerator.setParameter(SamplePortlet.PARAM_USER_ID, userId);
		
		EventRequest request = requestGenerator.generateRequest();
		EventResponse response = responseGenerator.generateResponse();
		
		portlet.proccessEventUserSelected(request, response);
		
		PortletSession session = request.getPortletSession();
		assertEquals(userId, session.getAttribute(SamplePortlet.SESSION_ATTR_SELECTED_USER_ID));
		assertEquals(WindowState.MAXIMIZED, response.getWindowState());
	}
	
	/**
	 * Verify serveResource() returns all users in the format of JSON
	 * 
	 * @throws IOException 
	 * @throws PortletException 
	 */
	@Test
	public void testServeResource() throws PortletException, IOException {
		ResourceRequestGenerator requestGenerator = portletTester.getResourceRequestGenerator();
		ResourceResponseGenerator responseGenerator = portletTester.getResourceResponseGenerator();
		
		requestGenerator.setParameter(SamplePortlet.PARAM_RESOURCE_TYPE, SamplePortlet.RESOURCE_TYPE_USERS);
		
		ResourceRequest request = requestGenerator.generateRequest();
		ResourceResponse response = responseGenerator.generateResponse();
		
		portlet.serveResource(request, response);
		
		assertEquals("[{\"userId\":\"1\",\"userName\":\"Derek\"},{\"userId\":\"2\",\"userName\":\"Emma\"}]", 
				portletTester.getTestResults().getOutputContent());
	}
	
	
	@After
	public void tearDown() {
		portletTester.reset();
	}
	
	@AfterClass
	public static void tearDownClass() {
		portletTester = null;
	}
}
