package com.contactpoint.model.DTO.azure;

import org.json.JSONException;
import org.json.JSONObject;

public class AzureTokenDTO {
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public int getExtExpiresIn() {
		return extExpiresIn;
	}
	public void setExtExpiresIn(int extExpiresIn) {
		this.extExpiresIn = extExpiresIn;
	}
	public long getExpiresOn() {
		return expiresOn;
	}
	public void setExpiresOn(long expiresOn) {
		this.expiresOn = expiresOn;
	}
	public long getNotBefore() {
		return notBefore;
	}
	public void setNotBefore(long notBefore) {
		this.notBefore = notBefore;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getIdToken() {
		return idToken;
	}
	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}
	
	private String tokenType;
	private String scope;
	private int expiresIn;
	private int extExpiresIn;
	private long expiresOn;
	private long notBefore;
	private String resource;
	private String accessToken;
	private String refreshToken;
	private String idToken;
	
	public final static String TOKEN_TYPE = "token_type";
	public final static String SCOPE = "scope";
	public final static String EXPIRES_IN = "expires_in";
	public final static String EXT_EXPIRES_IN = "ext_expires_in";
	public final static String EXPIRES_ON = "expires_on";
	public final static String NOT_BEFORE = "not_before";
	public final static String RESOURCE = "resource";
	public final static String ACCESS_TOKEN = "access_token";
	public final static String REFRESH_TOKEN = "refresh_token";
	public final static String ID_TOKEN = "id_token";
	
	public AzureTokenDTO(JSONObject token) {
		this.tokenType = token.optString(TOKEN_TYPE);
		this.scope = token.optString(SCOPE);
		this.expiresIn = token.optInt(EXPIRES_IN);
		this.extExpiresIn = token.optInt(EXT_EXPIRES_IN);
		this.expiresOn = token.optLong(EXPIRES_ON);
		this.notBefore = token.optLong(NOT_BEFORE);
		this.resource = token.optString(RESOURCE);
		this.accessToken = token.optString(ACCESS_TOKEN);
		this.refreshToken = token.optString(REFRESH_TOKEN);
		this.idToken = token.optString(ID_TOKEN);
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			obj.putOpt(TOKEN_TYPE, this.tokenType);
			obj.putOpt(SCOPE, this.scope);
			obj.putOpt(EXPIRES_IN, this.expiresIn);
			obj.putOpt(EXT_EXPIRES_IN, this.extExpiresIn);
			obj.putOpt(EXPIRES_ON, this.expiresOn);
			obj.putOpt(NOT_BEFORE, this.notBefore);
			obj.putOpt(RESOURCE, this.resource);
			obj.putOpt(ACCESS_TOKEN, this.accessToken);
			obj.putOpt(REFRESH_TOKEN, this.refreshToken);
			obj.putOpt(ID_TOKEN, this.idToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	@Override
	public String toString() {
		return this.toJSON().toString();
	}
	
}
