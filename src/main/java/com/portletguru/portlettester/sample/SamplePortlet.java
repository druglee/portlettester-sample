/**
 * 
 */
package com.portletguru.portlettester.sample;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.EventRequest;
import javax.portlet.EventResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.ProcessAction;
import javax.portlet.ProcessEvent;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.WindowState;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

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
	public static final String SESSION_ATTR_SELECTED_USER_ID = "selectedUserId";
	public static final String EVENT_USER_SELECTED = "eventUserSelected";
	
	public static final String PARAM_RESOURCE_TYPE = "paramResourceType";
	public static final String RESOURCE_TYPE_USERS = "resourceTypeUsers";
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	private boolean isParamSet;

	
	@Override
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
		String initParam = getInitParameter(INIT_PARAM_KEY);
		isParamSet = "true".equals(initParam);
	}
	
	
	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @throws PortletException
	 * @throws IOException
	 */
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

	
	/**
	 * 
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws PortletException
	 */
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
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 */
	@ProcessEvent(name = EVENT_USER_SELECTED)
	public void proccessEventUserSelected(EventRequest request, EventResponse response) throws IOException, PortletException {
		if(!isParamSet) {
			throw new PortletException("Portlet not correctly initialized.");
		}
		
		String userId = request.getParameter(PARAM_USER_ID);
		if(StringUtils.isNotEmpty(userId)) {
			PortletSession session = request.getPortletSession();
			session.setAttribute(SESSION_ATTR_SELECTED_USER_ID, userId);
			response.setWindowState(WindowState.MAXIMIZED);
		}
	}
	
	
	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response)
			throws PortletException, IOException {
		
		if(!isParamSet) {
			throw new PortletException("Portlet not correctly initialized.");
		}
		
		String resourceType = request.getParameter(PARAM_RESOURCE_TYPE);
		if(StringUtils.isEmpty(resourceType)) {
			return;
		}
		
		Writer writer = response.getWriter();
		if(RESOURCE_TYPE_USERS.equals(resourceType)) {
			List<User> users = getUsers();
			writer.write(objectMapper.writeValueAsString(users));
		}
	}
	
	private List<User> getUsers() {
		List<User> users = new LinkedList<User>();
		users.add(new User("1", "Derek"));
		users.add(new User("2", "Emma"));
		return users;
	}
}
