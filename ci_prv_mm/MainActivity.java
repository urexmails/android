package com.contactpoint.ci_prv_mm;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.azure.AzureLoginDTO;
import com.contactpoint.model.DTO.azure.AzureTokenDTO;
import com.contactpoint.model.client.authentication.AuthenticationToken;
import com.contactpoint.model.client.authentication.Login;
import com.contactpoint.model.client.authentication.RequestLogin;
import com.contactpoint.model.service.AuthenticationService;
import com.contactpoint.model.service.AzureAuthenticationService;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.service.PhotoService;
import com.contactpoint.model.util.AutoLogoutReceiver;
import com.contactpoint.model.util.AzureAuthClient;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.view.fragment.WorkListFragment;

//http://code.google.com/p/android-lockpattern/wiki/QuickUse
public class MainActivity extends NetworkActivity {
	// Preffered variables for pattern lock
	private static final int _ReqCreatePattern = 0;
	private static final int _ReqSignIn = 1;
	
	private SharedPreferences mPatternPrefs;
	private SharedPreferences mAuthPrefs;
	private EditText mUsernameET;
	private EditText mPasswordET;
	private EditText mTMSUsernameET;
	private EditText mTMSPasswordET;
	private TextView mMMInvalidErrorTV;
	private TextView mTMSInvalidErrorTV;
	private MyLocation mLocationTracker;
	
	private static final String MACHINE_KEY = "83273BB6-D113-47E0-912F-85BC6E562F0B";
	private static final String GROUP = "TollTransitions - CI Consultants";
	private static final String AUTHENTICATION_PREFERENCES = "AuthenticationToken";
	public static final String USERNAME = "Username";
	private static final String PASSWORD = "Password";
	private static final String AZURE_PREFERENCES = "AzureToken";
	public static final String DAILY_EXPIRE = "DailyExpire";
	
	private PendingIntent mPendingIntent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// delete icon and title from action bar
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		mUsernameET = (EditText) findViewById(R.id.login_et_uname);
		mPasswordET = (EditText) findViewById(R.id.login_et_pwd);
		mTMSUsernameET = (EditText) findViewById(R.id.login_et_tws_uname);
		mTMSPasswordET = (EditText) findViewById(R.id.login_et_tws_pwd);
		
		mUsernameET.setOnFocusChangeListener(mUsernameFocusListener);
		findViewById(R.id.login_iv_submit).setOnClickListener(mLoginListener);

		// initialize mPrefs
		mPatternPrefs = getSharedPreferences(LockPatternActivity.class.getSimpleName(), 0);
		mAuthPrefs = getSharedPreferences(MainActivity.class.getSimpleName(), 0);
		
		// if this activity is called by AutoLogoutReceiver
		if (getIntent().getExtras() != null &&
				getIntent().getExtras().containsKey(DAILY_EXPIRE)) {
			mPatternPrefs.edit().remove(LockPatternActivity._PaternSha1).commit();
		}

		// if we already have a lock pattern, use it as default authentication
		if (mPatternPrefs.contains(LockPatternActivity._PaternSha1)
				&& mAuthPrefs.contains(AUTHENTICATION_PREFERENCES) 
				&& mAuthPrefs.contains(AZURE_PREFERENCES)) {
			Intent intent = new Intent(this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, 
					LockPatternActivity.LPMode.ComparePattern);
			intent.putExtra(LockPatternActivity._PaternSha1, 
					mPatternPrefs.getString(LockPatternActivity._PaternSha1, null));
			startActivityForResult(intent, _ReqSignIn);
		}

		if (getIntent().getExtras() != null && 
				getIntent().getExtras().containsKey(USERNAME)) {
			String username = getIntent().getExtras().getString(USERNAME);
			mUsernameET.setText(username);
			mPasswordET.setText("");
			prepopulateTMSCredential(username);
		}
		
		mMMInvalidErrorTV = (TextView) findViewById(R.id.login_error_invalid_mm_tv);
		mTMSInvalidErrorTV = (TextView) findViewById(R.id.login_error_invalid_tms_tv);
//		InternalIO.resetFiles(this);
		//DisplayMetrics displayMetrics = getResources().getDisplayMetrics();    
		//float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
		//System.out.println("DP Width: " + dpWidth);
		//System.out.println("Density: " + getResources().getDisplayMetrics().density);
		//System.out.println("Screen size: " + (getResources().getConfiguration().screenLayout & 
		//		Configuration.SCREENLAYOUT_SIZE_MASK));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.version_only_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	// login listener
	private OnClickListener mLoginListener = new OnClickListener() {

		public void onClick(View arg0) {
			mMMInvalidErrorTV.setVisibility(View.GONE);
			mTMSInvalidErrorTV.setVisibility(View.GONE);
			
			AzureLoginDTO inDTO = new AzureLoginDTO();
			inDTO.setUsername(mUsernameET.getText().toString());
			inDTO.setPassword(mPasswordET.getText().toString());
			
			ModelFactory.getAzureService().setNetworkListener(MainActivity.this);
			ModelFactory.getAzureService().prepareLogin(inDTO);
			
			int result = ModelFactory.getAzureService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case _ReqCreatePattern:
			if (resultCode == RESULT_OK) {	
				// successfully create new pattern
				// add username to shared preference for pattern sign in
				String username = mUsernameET.getText().toString();
				String tmsUsername = mTMSUsernameET.getText().toString();
				String tmsPassword = mTMSPasswordET.getText().toString();
				
				SharedPreferences.Editor pref = mAuthPrefs.edit();
				pref.putString(AUTHENTICATION_PREFERENCES, tmsUsername + AuthenticationService.DOMAIN);
				
				// add azure access token
				pref.putString(AZURE_PREFERENCES, ModelFactory.getAzureService().getAzureToken().toString());
				pref.commit();
				
				// Hyap 9798 - save TMS credential securely
				saveTMSCredential(username, tmsUsername, tmsPassword);
				
				scheduleLogout();
				
				Intent i = new Intent(getApplication(), DashboardActivity.class);
				startActivity(i);
			}
			break;
		case _ReqSignIn:
			if (resultCode == RESULT_OK) {
				// signing in ok
				// set authentication token
				AuthenticationToken token = new AuthenticationToken();
				token.username = mAuthPrefs.getString(AUTHENTICATION_PREFERENCES, null);
				token.deviceID = Secure.getString(getContentResolver(),
						Secure.ANDROID_ID);
				token.machineKey = MACHINE_KEY;
				token.group = GROUP;
				
				// set azure authentication token
				JSONObject azureJsonToken = null;
				try {
					azureJsonToken = new JSONObject(mAuthPrefs.getString(AZURE_PREFERENCES, null));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				if (azureJsonToken != null) {
					AzureTokenDTO azureToken = new AzureTokenDTO(azureJsonToken);
					ModelFactory.getAzureService().setAzureToken(azureToken);
					
					ModelFactory.getCIService().setAuthenticationToken(token);
					ModelFactory.getPRVService().setAuthenticationToken(token);
					ModelFactory.getPhotoService().setAuthenticationToken(token);
					
					ModelFactory.getCIService().loadFormData(this);
					ModelFactory.getPRVService().loadFormData(this);
					ModelFactory.getPhotoService().loadFormData(this);
					
					Intent i = new Intent(getApplication(), DashboardActivity.class);
					startActivity(i);
				}
				
			} else {
				// signing in failed
				// do nothing
			}
			break;
		}
	}
	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case AzureAuthenticationService.AZURE_LOGIN:
			// set authentication token
			AuthenticationToken token = new AuthenticationToken();
			token.username = mTMSUsernameET.getText().toString() + AuthenticationService.DOMAIN;
			token.deviceID = Secure.getString(getContentResolver(),
					Secure.ANDROID_ID);
			token.machineKey = MACHINE_KEY;
			token.group = GROUP;
			
			if (AzureAuthClient.ENABLE_TMS) {
				// successfully login to Azure, login to TMS
				RequestLogin inDTO = new RequestLogin();
				inDTO.loginCredentials = new Login();
				inDTO.loginCredentials.username = mTMSUsernameET.getText().toString();
				inDTO.loginCredentials.password = mTMSPasswordET.getText().toString();
				inDTO.token = token;
	
				ModelFactory.getAuthenticationService().setNetworkListener(MainActivity.this);
				ModelFactory.getAuthenticationService().prepareLogin(inDTO);
				int result = ModelFactory.getAuthenticationService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
				break;
			} else {
				ModelFactory.getCIService().setAuthenticationToken(token);
				ModelFactory.getPRVService().setAuthenticationToken(token);
				ModelFactory.getPhotoService().setAuthenticationToken(token);
			}
		case AuthenticationService.LOGIN:
			// successfully login, call GetEnvironment
			ModelFactory.getPRVService().setNetworkListener(MainActivity.this);
			ModelFactory.getPRVService().prepareGetEnvironment();
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
			break;
		case PRVService.PRV_ENVIRONMENT:
			ModelFactory.getCIService().loadFormData(this);
			ModelFactory.getPRVService().loadFormData(this);
			ModelFactory.getPhotoService().loadFormData(this);

			// automatic submit and delete
			// populate upload queue
			ModelFactory.getCIService().setNetworkListener(this);
			ModelFactory.getCIService().populateUploadQueue();
			result = 0;
		case WorkListFragment.UPLIFT_TARGET_UPLOAD:
		case WorkListFragment.DELIVERY_TARGET_UPLOAD:
			ModelFactory.getCIService().saveFormData(this);
			if (ModelFactory.getCIService().getUploadQueue().size() > 0) {
				// continue upload
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(MainActivity.this, uploadedCILocationResult);

				return;
			} 
			
			// on upload finished, delete the rest
			ModelFactory.getCIService().populateDeleteQueue();
		case WorkListFragment.UPLIFT_TARGET_DELETE:
		case WorkListFragment.DELIVERY_TARGET_DELETE:
			ModelFactory.getCIService().saveFormData(this);
			if (ModelFactory.getCIService().getDeleteQueue().size() > 0) {
				// continue delete
				ModelFactory.getCIService().prepareDelete();
				result = ModelFactory.getCIService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
				return;
			}
			
			// on delete finished, upload prv
			ModelFactory.getPRVService().setNetworkListener(this);
			ModelFactory.getPRVService().populateUploadQueue(this);
		case PRVService.PRV_UPLOAD:
			ModelFactory.getPRVService().saveFormData(this);
			if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
				// continue upload
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(MainActivity.this, uploadedPRVLocationResult);

				return;
			}
			
			// on upload finished, delete the rest
		case WorkListFragment.PRV_TARGET_DELETE:
			ModelFactory.getPRVService().saveFormData(this);
			if (ModelFactory.getPRVService().getPRVForms().size() > 0) {
				ModelFactory.getPRVService().setCurrentForm(ModelFactory.getPRVService().getPRVForms().get(0));
				ModelFactory.getPRVService().preparePRVDelete();
				result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
				return;
			}
			ModelFactory.getPhotoService().setNetworkListener(this);
		case PhotoService.PHOTO_UPLOAD:
			ModelFactory.getPhotoService().saveFormData(this);
			if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().preparePhotoUpload();
				result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
				return;
			}
			
			// on delete finished, empty password text field
			mPasswordET.setText("");
			//mTMSPasswordET.setText("");
			ModelFactory.getPhotoService().clearPhoto(this);
			ModelFactory.getPhotoService().saveFormData(this);
			
			System.gc();

			// if nothing needs to be deleted, create pattern lock
			Intent intent = new Intent(MainActivity.this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
			startActivityForResult(intent, _ReqCreatePattern);
			break;
		case AuthenticationService.FAILURE:
			//showResult(ModelFactory.getAuthenticationService().getResponseMessage() + "\nError code: " + param);
			//break;
		case AuthenticationService.ERROR:
			//showErrorDialog(ModelFactory.getAuthenticationService().getClientMessage() + "\nError code: " + param);
			mTMSInvalidErrorTV.setVisibility(View.VISIBLE);
			break;
		case GetCIService.ERROR:
			showErrorDialog(ModelFactory.getCIService().getClientMessage() + "\nError code: " + param);
			break;
		case PRVService.ERROR:
			showErrorDialog(ModelFactory.getPRVService().getClientMessage() + "\nError code: " + param);
			break;
		case AzureAuthenticationService.FAILURE:
		case AzureAuthenticationService.ERROR:
			//showErrorDialog(ModelFactory.getAzureService().getClientMessage() + "\nError code: " + param);
			mMMInvalidErrorTV.setVisibility(View.VISIBLE);
			break;
		} // switch
	} // callback
	
	private LocationResult uploadedCILocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			onTaskComplete();
			ModelFactory.getCIService().prepareUpload();
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};

	private LocationResult uploadedPRVLocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			onTaskComplete();
			ModelFactory.getPRVService().preparePRVUpload();
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};
	
	@SuppressLint("NewApi")
	private void scheduleLogout() {
		Intent logoutIntent = new Intent(MainActivity.this, AutoLogoutReceiver.class);
		mPendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, logoutIntent, 0);
		
		AlarmManager manager1 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	    Calendar calendar1 = Calendar.getInstance();
	    calendar1.set(Calendar.HOUR_OF_DAY, 23);
	    calendar1.set(Calendar.MINUTE, 0);
	    calendar1.set(Calendar.SECOND, 0);
	    
	    manager1.setExact(AlarmManager.RTC, calendar1.getTimeInMillis(), mPendingIntent);
	}
	
	private void saveTMSCredential(String username, String tmsUsername, String tmsPwd) {
		try {
			// create JSON object
			JSONObject cred = new JSONObject();
			cred.put(USERNAME, tmsUsername);
			cred.put(PASSWORD, tmsPwd);
			
			// convert to bytes
			byte[] data = cred.toString().getBytes("UTF-8");
			
			// encode bytes as base64 string
			String base64 = Base64.encodeToString(data, Base64.DEFAULT);
			
			// store encoded string in shared preferences
			SharedPreferences.Editor pref = mAuthPrefs.edit();
			pref.putString(username, base64);
			pref.commit();
		} catch (Exception e) {
			e.printStackTrace();
			// do nothing
		}
	}
	
	private void prepopulateTMSCredential(String username) {
		try {
			// get base64 string
			String base64 = mAuthPrefs.getString(username, null);
			// decode as bytes
			byte[] data = Base64.decode(base64, Base64.DEFAULT);
			// convert back to string
			String jsonCred = new String(data, "UTF-8");
			JSONObject cred = new JSONObject(jsonCred);
			
    		mTMSUsernameET.setText(cred.optString(USERNAME));
    		mTMSPasswordET.setText(cred.optString(PASSWORD));
		} catch (Exception e) {
			// do nothing
		}
	}
	
	private OnFocusChangeListener mUsernameFocusListener = new OnFocusChangeListener() {
	    public void onFocusChange(View view, boolean hasFocus) {
	        if (!hasFocus) {
	        	prepopulateTMSCredential(mUsernameET.getText().toString());
	        }
	    }
	};
}