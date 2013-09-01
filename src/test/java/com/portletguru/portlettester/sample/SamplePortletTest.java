/**
 * 
 */
package com.portletguru.portlettester.sample;

import javax.portlet.PortletException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.portletguru.portlettester.PortletTester;

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
		portletTester.initPortlet(portlet);
	}
	
	@Test
	public void test() {
		Assert.assertTrue(true);
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
