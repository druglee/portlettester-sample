/**
 * 
 */
package com.portletguru.portlettester.sample;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.ResourceFilter;

/**
 * @author Derek Linde Li
 * 
 */
public class SampleResourceFilter implements ResourceFilter {

	public static final String ACCEPTABLE_PATTERN = "acceptablePattern";

	private String acceptablePattern;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.portlet.filter.PortletFilter#init(javax.portlet.filter.FilterConfig
	 * )
	 */
	public void init(FilterConfig filterConfig) throws PortletException {

		String acceptablePattern = filterConfig
				.getInitParameter(ACCEPTABLE_PATTERN);

		if (acceptablePattern != null) {
			try {
				Pattern.compile(acceptablePattern);
				this.acceptablePattern = acceptablePattern;
			} catch (PatternSyntaxException e) {
				throw new PortletException(
						"Failed to parse string as regular expression:"
								+ acceptablePattern, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.portlet.filter.PortletFilter#destroy()
	 */
	public void destroy() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.portlet.filter.ResourceFilter#doFilter(javax.portlet.ResourceRequest
	 * , javax.portlet.ResourceResponse, javax.portlet.filter.FilterChain)
	 */
	public void doFilter(ResourceRequest request, ResourceResponse response,
			FilterChain chain) throws IOException, PortletException {

		if (acceptablePattern == null) {
			chain.doFilter(request, response);
			return;
		}

		PortletSession session = request.getPortletSession();
		String token = (String) session.getAttribute("token");
		if (token != null) {
			if (Pattern.matches(acceptablePattern, token)) {
				chain.doFilter(request, response);
			}
		}
	}

}
