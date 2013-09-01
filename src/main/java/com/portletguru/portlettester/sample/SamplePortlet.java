/**
 * 
 */
package com.portletguru.portlettester.sample;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;

/**
 * @author Derek Linde Li
 * 
 */
public class SamplePortlet extends GenericPortlet {

	public static final String INIT_PARAM_KEY = "initParamKey";
	public static final String TEST_ACTION_NAME = "testAction";

	public static final String PARAM_USER_ID = "paramUserId";
	public static final String PREFS_PREFERRED_USER_ID = "preferredUserId";
	
	public static final String REQUEST_ATTR_SAVED_USER_ID = "savedUserId";
	
	private boolean isParamSet;

	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		String initParam = getInitParameter(INIT_PARAM_KEY);
		isParamSet = "true".equals(initParam);
	}
	
	@Override
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		
		if(!isParamSet) {
			throw new PortletException("Portlet not correctly initialized.");
		}
		
		String savedUserId = request.getParameter(PARAM_USER_ID);
		if(StringUtils.isNotEmpty(savedUserId)) {
			request.setAttribute(REQUEST_ATTR_SAVED_USER_ID, savedUserId);
		}
	}

	@ProcessAction(name = TEST_ACTION_NAME)
	public void processActionSaveUserId(ActionRequest request, ActionResponse response)
			throws IOException, PortletException {
		
		if(!isParamSet) {
			throw new PortletException("Portlet not correctly initialized.");
		}
		
		String userId = request.getParameter(PARAM_USER_ID);
		if(StringUtils.isNotEmpty(userId)) {
			PortletPreferences preferences = request.getPreferences();
			preferences.setValue(PREFS_PREFERRED_USER_ID, userId);
			preferences.store();
		}
		
		response.setRenderParameter(PARAM_USER_ID, userId);
	}
}
