package com.contactpoint.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsServiceConnectionSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.contactpoint.controller.AsyncSoapTaskCompleteListener;
import com.contactpoint.model.ModelFactory;

//http://sarangasl.blogspot.com.au/2011/10/android-web-service-access-tutorial.html
public class SoapClient extends AsyncTask<String, Void, String> {

	private final static String URL = "www.tolltransitions.com.au";
	private final static int PORT = 443;
	private final static int TIMEOUT = 50000;
	private final static String UNEXPECTED_ERROR = "Unexpected error occurred, please try again in a few minutes...\nError detail:\n";

	private String mMessage;
	private Object mResponse;
	private SoapSerializationEnvelope mEnvelope;
	private AsyncSoapTaskCompleteListener<String> listener;    

	public SoapClient(AsyncSoapTaskCompleteListener<String> listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		String action = params[0];
		String path = params[1];
		String url = URL;
		
		if (params.length > 2) {
			url = params[2];
		}
		
		mResponse = null;
		mMessage = null;

		HttpsTransportSE androidHttpTransport = new HttpsTransportSE(url, PORT, path, TIMEOUT);
		androidHttpTransport.debug = true; // delete this after implementation

		try {
			((HttpsServiceConnectionSE) androidHttpTransport.getServiceConnection()).setSSLSocketFactory(new SSLSocketFactoryExtended());
			
			if (params.length > 2) {
				List<HeaderProperty> list = new ArrayList<HeaderProperty>();
				list.add(new HeaderProperty("Authorization", ModelFactory.getAzureService().getAuthorizationHeader()));
				androidHttpTransport.call(action, mEnvelope, list);
			} else {
				androidHttpTransport.call(action, mEnvelope);
			}
			ExternalIO.saveToExternalStorage("last_request.txt", androidHttpTransport.requestDump);
			ExternalIO.saveToExternalStorage("last_response.txt", androidHttpTransport.responseDump);
//			System.out.println(androidHttpTransport.requestDump);
//			System.out.println(androidHttpTransport.responseDump);
			mResponse = mEnvelope.getResponse();
			return mResponse.toString();  
		} catch(SoapFault fault) {			// on error
			mMessage = fault.faultstring;
			ExternalIO.saveToExternalStorage("error_request.txt", androidHttpTransport.requestDump);
			ExternalIO.saveToExternalStorage("error_response.txt", androidHttpTransport.responseDump);
			return fault.faultstring;
		} catch (ClassCastException e) {	// on message returned
			mResponse = mEnvelope.bodyIn;
			return mResponse.toString();
		} catch (XmlPullParserException e) {
			mMessage = androidHttpTransport.responseDump;
			ExternalIO.saveToExternalStorage("error_request.txt", androidHttpTransport.requestDump);
			ExternalIO.saveToExternalStorage("error_response.txt", androidHttpTransport.responseDump);
			return e.getMessage();
		} catch (Exception e) {
			mMessage = UNEXPECTED_ERROR;
			mMessage += e.getMessage() + "\n";
			for (StackTraceElement ste : e.getStackTrace()) {
				mMessage += ste.toString() + "\n";
			}
			
			ExternalIO.saveToExternalStorage("error_request.txt", androidHttpTransport.requestDump);
			ExternalIO.saveToExternalStorage("error_response.txt", androidHttpTransport.responseDump);
			
			return e.getMessage();
		}
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
	public void setMessage(String message)			{ mMessage = message; }
	public Object getResponse()						{ return mResponse; }
	public SoapObject getResponseAsObject() {
		try {
			if (mResponse instanceof Vector) {
				for (SoapObject obj : ((Vector<SoapObject>)mResponse)) {
					if (obj != null) {
						return obj;
					}
				}
				//return (SoapObject)((Vector<SoapObject>)mResponse).get(0);
			}
			return (SoapObject)mResponse;
		} catch (ClassCastException e) {
			return null;
		}
	}
	public void setEnvelope(SoapSerializationEnvelope envelope) {
		mEnvelope = envelope;
	}
}
