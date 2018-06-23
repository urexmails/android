package com.contactpoint.model.DTO.azure;

public class AzureLoginDTO {
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	
	public void setUsername(String username) { this.username = username; }
	public void setPassword(String password) { this.password = password; }
	
	private String username;
	private String password;
}
