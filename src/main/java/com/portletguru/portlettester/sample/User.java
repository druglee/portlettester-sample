/**
 * 
 */
package com.portletguru.portlettester.sample;

/**
 * @author Derek Linde Li
 *
 */
public class User {

	private String userId;
	private String userName;
	
	public User(String id, String name) {
		userId = id;
		userName = name;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
