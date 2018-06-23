package com.contactpoint.model.util;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.model.DTO.azure.AzureLoginDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.service.AzureAuthenticationService;

//http://sarangasl.blogspot.com.au/2011/10/android-web-service-access-tutorial.html
public class AzureAuthClient extends AsyncTask<String, Void, String> {
	

	// SIT
//	private final static String URL = "https://login.windows.net/smovemaestro.onmicrosoft.com/oauth2/token";
//	private final static String RESOURCE = "https://tabletapi.s.movemaestro.com";
//	private final static String CLIENT_ID = "768936f4-8b53-480f-ad09-de4da70ef118";
//	private final static String DOMAIN = "@smovemaestro.onmicrosoft.com";
//	public final static String URL_MM = "api.s.movemaestro.com";
//	public final static String URL_MM_TEST = "api.s.movemaestro.com";
//	public final static boolean ENABLE_TMS = false;
	
	// SIT2
//	private final static String URL = "https://login.windows.net/s2movemaestro.onmicrosoft.com/oauth2/token";
//	private final static String RESOURCE = "https://tabletapi.s2.movemaestro.com";
//	private final static String CLIENT_ID = "48d8666b-cdf1-4c79-a6d7-09af29985022";
//	private final static String DOMAIN = "@s2movemaestro.onmicrosoft.com";
//	public final static String URL_MM = "api.s2.movemaestro.com";
//	public final static String URL_MM_TEST = "api.s2.movemaestro.com";
//	public final static boolean ENABLE_TMS = false;
	
	// UAT
//	private final static String URL = "https://login.windows.net/amovemaestro.onmicrosoft.com/oauth2/token";
//	private final static String RESOURCE = "https://tabletapi.a.movemaestro.com";
//	private final static String CLIENT_ID = "3dc2b5f4-5ce6-4c28-aa4d-278cee442c4a";
//	private final static String DOMAIN = "@amovemaestro.onmicrosoft.com";
//	public final static String URL_MM = "aa.movemaestro.com";
//	public final static String URL_MM_TEST = "aa.movemaestro.com";
//	public final static boolean ENABLE_TMS = true;

	// Production
	private final static String URL = "https://login.windows.net/movemaestro.com/oauth2/token";
	private final static String RESOURCE = "https://tabletapi.b.movemaestro.com";
	private final static String CLIENT_ID = "28abf331-3221-491c-b695-65009b4eae9e";
	private final static String DOMAIN = "@movemaestro.com";
	public final static String URL_MM = "ab.movemaestro.com";
	public final static String URL_MM_TEST = "ab.movemaestro.com";
	public final static boolean ENABLE_TMS = true;

	
	private final static String GRANT_LOGIN = "password";
	private final static String GRANT_REFRESH = "refresh_token";
	private final static String SCOPE = "openid";
	
	//private final static int PORT = 443;
	//private final static int TIMEOUT = 50000;
	private final static String UNEXPECTED_ERROR = "Unexpected error occurred, please try again in a few minutes...\nError detail:\n";

	private String mMessage;
	private Object mResponse;
	//private SoapSerializationEnvelope mEnvelope;
	private String mRequest;
	private AsyncSoapTaskCompleteListener<String> listener;    

	public AzureAuthClient(AsyncSoapTaskCompleteListener<String> listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		mResponse = null;
		mMessage = null;
		String result = "";
		
		HttpsURLConnection conn = null;
		
		try {
			URL address = new URL(URL);
			conn = (HttpsURLConnection)address.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "UTF-8");
			conn.setFixedLengthStreamingMode(mRequest.getBytes().length);
			
			//send the POST out
            PrintWriter out = new PrintWriter(conn.getOutputStream());
            out.print(mRequest);
            out.close();
            
			int responseCode = conn.getResponseCode();
			
			InputStream inStream = null;
			
			// on success
			if (responseCode == 200) {
				inStream = conn.getInputStream();
				mResponse = new JSONObject(StreamToString.convert(inStream));
				result = AzureAuthenticationService.AUTHENTICATED;
			} 
			// on error
			else {
				inStream = conn.getErrorStream();
				JSONObject jsonToken = new JSONObject(StreamToString.convert(inStream));
				mMessage = jsonToken.optString("error_description");
			}
			
			inStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			mMessage = UNEXPECTED_ERROR;
			mMessage += e.getMessage() + "\n";
			for (StackTraceElement ste : e.getStackTrace()) {
				mMessage += ste.toString() + "\n";
			}
		} finally {
			conn.disconnect();
		}
		
		return result;
	}

	@Override
	protected void onPreExecute() {
		if (listener != null) {
			listener.onPreExecuteSoap();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (listener == null) {
			return;
		}
		listener.onTaskComplete(result);
	}

	public String getMessage() 						{ return mMessage; }
	public Object getResponse()						{ return mResponse; }
	
	public void setLoginRequest(AzureLoginDTO param) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("resource", RESOURCE);
		params.put("client_id", CLIENT_ID);
		params.put("grant_type", GRANT_LOGIN);
		params.put("scope", SCOPE);
		params.put("username", param.getUsername() + DOMAIN);
		params.put("password", param.getPassword());
		
		try {
			mRequest = getPostDataString(params);
		} catch (UnsupportedEncodingException e) {
			// TODO not sure what to do here.
			e.printStackTrace();
		}
	}
	
	public void setRefreshRequest(AzureTokenDTO token) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("resource", RESOURCE);
		params.put("client_id", CLIENT_ID);
		params.put("grant_type", GRANT_REFRESH);
		params.put("refresh_token", token.getRefreshToken());
		
		try {
			mRequest = getPostDataString(params);
		} catch (UnsupportedEncodingException e) {
			// TODO not sure what to do here.
			e.printStackTrace();
		}
	}
	
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
