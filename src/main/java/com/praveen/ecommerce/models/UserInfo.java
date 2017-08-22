package com.praveen.ecommerce.models;

public class UserInfo {

	private String userName;
	private UserRole role;
	
	public enum UserRole{
		ADMIN,CUSTOMER
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
	
	
}
