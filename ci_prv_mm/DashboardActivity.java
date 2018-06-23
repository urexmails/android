package com.contactpoint.ci_prv_mm;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.service.PhotoService;
import com.contactpoint.model.util.AzureAuthClient;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.model.util.TollUncaughtExceptionHandler;
import com.contactpoint.view.fragment.WorkListFragment;

public class DashboardActivity extends NetworkActivity {

	private static final int _ReqCreatePattern = 0;
	private static final int _ReqSignIn = 1;

	private SharedPreferences mPrefs;
	private MyLocation mLocationTracker;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		// set listeners
		findViewById(R.id.dashboard_logout).setOnClickListener(mLogoutListener);
		findViewById(R.id.dashboard_ci).setOnClickListener(mCIListener);
		findViewById(R.id.dashboard_work_list).setOnClickListener(mWorkListListener);
		findViewById(R.id.dashboard_upload).setOnClickListener(mUploadListener);
		findViewById(R.id.dashboard_prv).setOnClickListener(mPRVListener);

		mPrefs = getSharedPreferences(LockPatternActivity.class.getSimpleName(), 0);

		if (AzureAuthClient.ENABLE_TMS) {
			// Check TMS Reference Data
			if (ModelFactory.getPRVService().refDataNeedRefresh()) {
				ModelFactory.getPRVService().prepareRefDataCheckDate();
				ModelFactory.getPRVService().setNetworkListener(this);
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
		} else {
			// Check MM Reference Data
			if (ModelFactory.getPRVService().mmRefDataNeedRefresh()) {
				ModelFactory.getPRVService().prepareMMRefDataCheckDate();
				ModelFactory.getPRVService().setNetworkListener(this);
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
		}

		Thread.setDefaultUncaughtExceptionHandler(new TollUncaughtExceptionHandler(this,
	            MainActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_change_pattern:
			Intent intent = new Intent(this, LockPatternActivity.class);
			intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.ComparePattern);
			intent.putExtra(LockPatternActivity._PaternSha1, 
					mPrefs.getString(LockPatternActivity._PaternSha1, null));
			startActivityForResult(intent, _ReqSignIn);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case _ReqCreatePattern:
			if (resultCode == RESULT_OK) {
				//				Intent i = new Intent(getApplication(), DashboardActivity.class);
				//				startActivity(i);
			}
			break;
		case _ReqSignIn:
			if (resultCode == RESULT_OK) {
				// signing in ok
				Intent intent = new Intent(DashboardActivity.this, LockPatternActivity.class);
				intent.putExtra(LockPatternActivity._Mode, LockPatternActivity.LPMode.CreatePattern);
				startActivityForResult(intent, _ReqCreatePattern);
			} else {
				// signing in failed
				// do nothing
			}
			break;
		}
	}

	private OnClickListener mLogoutListener = new OnClickListener() {

		public void onClick(View arg0) {
			CommonController.logout(DashboardActivity.this);
		}

	};

	private OnClickListener mCIListener = new OnClickListener() {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplication(), MainFragmentActivity.class);
			i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.SEARCH);
			i.putExtra(MainFragmentActivity.EXTRA_SHOW_CI, true);
			startActivity(i);
		}

	};

	private OnClickListener mWorkListListener = new OnClickListener() {

		public void onClick(View arg0) {
			Intent i = new Intent(getApplication(), MainFragmentActivity.class);
			i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.WORKLIST);
			startActivity(i);
		}

	};

	private OnClickListener mUploadListener = new OnClickListener() {
		public void onClick(View v) {
			// populate upload queue
			ModelFactory.getCIService().populateUploadQueue();
			ModelFactory.getPRVService().populateUploadQueue(DashboardActivity.this);

			// do nothing if no form is ready for submission
			if (ModelFactory.getCIService().getUploadQueue().size() <= 0 &&
					ModelFactory.getPRVService().getUploadQueue().size() <= 0 &&
					!ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				return;
			}

			ModelFactory.getPhotoService().setNetworkListener(DashboardActivity.this);
			if (ModelFactory.getCIService().getUploadQueue().size() > 0) {
				ModelFactory.getCIService().setNetworkListener(DashboardActivity.this);
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(DashboardActivity.this, uploadedCILocationResult);

				return;
			}

			if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
				ModelFactory.getPRVService().setNetworkListener(DashboardActivity.this);
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(DashboardActivity.this, uploadedPRVLocationResult);

				return;
			}
			
			if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().initialisePhotoUpload();
				ModelFactory.getPhotoService().preparePhotoUpload();
				int result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
		}
	};

	private OnClickListener mPRVListener = new OnClickListener() {
		public void onClick(View v) {
			Intent i = new Intent(getApplication(), MainFragmentActivity.class);
			i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.SEARCH);
			i.putExtra(MainFragmentActivity.EXTRA_SHOW_CI, false);
			startActivity(i);
		}
	};

	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case WorkListFragment.UPLIFT_TARGET_UPLOAD:
		case WorkListFragment.DELIVERY_TARGET_UPLOAD:
			ModelFactory.getCIService().saveFormData(this);

			if (ModelFactory.getCIService().getUploadQueue().size() > 0) {
				// continue upload
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(DashboardActivity.this, uploadedCILocationResult);

			} else  if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(DashboardActivity.this, uploadedPRVLocationResult);

			} else if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().initialisePhotoUpload();
				ModelFactory.getPhotoService().preparePhotoUpload();
				int result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				showResult(ModelFactory.getCIService().getResponseMessage());
				System.gc();
			}
			break;
		case PRVService.PRV_UPLOAD:
			ModelFactory.getPRVService().saveFormData(this);
			if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
				// continue upload
				showLoadingDialog(LOADING_MSG);
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(this, uploadedPRVLocationResult);
			} else if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().initialisePhotoUpload();
				ModelFactory.getPhotoService().preparePhotoUpload();
				int result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				showResult(ModelFactory.getPRVService().getResponseMessage());
				System.gc();
			}
			break;
		case PRVService.PRV_REF_DOWNLOAD:
			ModelFactory.getPRVService().saveRefData(this);
			
			// check MM ref data
			if (ModelFactory.getPRVService().mmRefDataNeedRefresh()) {
				ModelFactory.getPRVService().prepareMMRefDataCheckDate();
				ModelFactory.getPRVService().setNetworkListener(this);
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
			break;
		case PRVService.PRV_REF_CHECK:
			if (!ModelFactory.getPRVService().refDataIsUpToDate()) {
				ModelFactory.getPRVService().prepareRefDataDownload();
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				if (ModelFactory.getPRVService().mmRefDataNeedRefresh()) {
					ModelFactory.getPRVService().prepareMMRefDataCheckDate();
					ModelFactory.getPRVService().setNetworkListener(this);
					int result = ModelFactory.getPRVService().executeSoapRequest();
					if (result == NetworkListener.ERR_OFFLINE) {
						showErrorDialog(getString(R.string.network_not_connected));
					}
				}
			}
			break;
		case PRVService.PRV_MM_REF_CHECK:
			if (!ModelFactory.getPRVService().mmRefDataIsUpToDate()) {
				ModelFactory.getPRVService().prepareMMRefDataDownload();
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
			break;
		case PRVService.PRV_MM_REF_DOWNLOAD:
			ModelFactory.getPRVService().saveMMRefData(this);
			break;
		case PhotoService.PHOTO_UPLOAD:
			ModelFactory.getPhotoService().saveFormData(this);
			if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().preparePhotoUpload();
				int result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				showResult(ModelFactory.getPhotoService().getFinalMessage());
				ModelFactory.getPhotoService().initialisePhotoUpload();
				ModelFactory.getPhotoService().saveFormData(this);
				System.gc();
			}
			break;
		case GetCIService.ERROR:
			showErrorDialog(ModelFactory.getCIService().getClientMessage());
			break;
		case PRVService.ERROR:
			showErrorDialog(ModelFactory.getPRVService().getClientMessage());
			break;
		case PhotoService.ERROR:
			ModelFactory.getPhotoService().saveFormData(this);
			showErrorDialog(ModelFactory.getPhotoService().getClientMessage());
			break;
		}
	}

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

}
