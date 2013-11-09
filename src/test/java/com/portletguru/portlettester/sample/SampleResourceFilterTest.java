/**
 * 
 */
package com.portletguru.portlettester.sample;

import java.io.IOException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.FilterConfig;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import com.portletguru.portlettester.PortletTester;
import com.portletguru.portlettester.TestResultHolder;
import com.portletguru.portlettester.mocks.FilterConfigGenerator;
import com.portletguru.portlettester.mocks.ResourceRequestGenerator;
import com.portletguru.portlettester.mocks.ResourceResponseGenerator;

/**
 * This class demonstrates how to use PortletTester to test a PortletFilter
 * 
 * @author Derek Linde Li
 *
 */
public class SampleResourceFilterTest {

	private static PortletTester portletTester;
	private SampleResourceFilter resourceFilter;
	
	@BeforeClass
	public static void setupClass() {
		portletTester = new PortletTester();
	}
	
	@Before
	public void setup() throws PortletException {
		resourceFilter = new SampleResourceFilter();
	}
	
	@Test
	public void testTokenValid() throws PortletException, IOException {
		initCommonConfig();
		
		ResourceRequestGenerator requestGenerator = portletTester.getResourceRequestGenerator();
		ResourceResponseGenerator responseGenerator = portletTester.getResourceResponseGenerator();
		
		ResourceRequest request = requestGenerator.generateRequest();
		ResourceResponse response = responseGenerator.generateResponse();
		
		PortletSession session = request.getPortletSession();
		session.setAttribute("token", "admin, user");
		
		resourceFilter.doFilter(request, response, portletTester.getFilterChain());
		
		TestResultHolder testResult = portletTester.getTestResults();
		assertTrue(testResult.isFilterPassed());
	}
	
	@Test
	public void testTokenInvalid() throws PortletException, IOException {
		initCommonConfig();
		
		ResourceRequestGenerator requestGenerator = portletTester.getResourceRequestGenerator();
		ResourceResponseGenerator responseGenerator = portletTester.getResourceResponseGenerator();
		
		ResourceRequest request = requestGenerator.generateRequest();
		ResourceResponse response = responseGenerator.generateResponse();
		
		PortletSession session = request.getPortletSession();
		session.setAttribute("token", "user");
		
		resourceFilter.doFilter(request, response, portletTester.getFilterChain());
		
		TestResultHolder testResult = portletTester.getTestResults();
		assertFalse(testResult.isFilterPassed());
	}
	
	@Test
	public void testNoPattern() throws PortletException, IOException {
		initNoPatternConfig();
		
		ResourceRequestGenerator requestGenerator = portletTester.getResourceRequestGenerator();
		ResourceResponseGenerator responseGenerator = portletTester.getResourceResponseGenerator();
		ResourceRequest request = requestGenerator.generateRequest();
		ResourceResponse response = responseGenerator.generateResponse();
		
		resourceFilter.doFilter(request, response, portletTester.getFilterChain());
		
		TestResultHolder testResult = portletTester.getTestResults();
		assertTrue(testResult.isFilterPassed());
	}
	
	private void initCommonConfig() throws PortletException {
		FilterConfigGenerator configGenerator = portletTester.getFilterConfigGenerator();
		configGenerator.addInitParameter(SampleResourceFilter.ACCEPTABLE_PATTERN, ".*admin.*");
		FilterConfig filterConfig = configGenerator.generateFilterConfig();
		portletTester.initFilter(resourceFilter, filterConfig);
	}
	
	
	private void initNoPatternConfig() throws PortletException {
		FilterConfigGenerator configGenerator = portletTester.getFilterConfigGenerator();
		FilterConfig filterConfig = configGenerator.generateFilterConfig();
		portletTester.initFilter(resourceFilter, filterConfig);
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
