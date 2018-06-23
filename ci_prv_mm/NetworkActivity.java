package com.contactpoint.ci_prv_mm;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.model.util.TollUncaughtExceptionHandler;

public abstract class NetworkActivity extends SherlockActivity implements NetworkListener {

	protected AlertDialog mAlert;
	protected ProgressDialog mProgress;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new TollUncaughtExceptionHandler(this,
	            MainActivity.class));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void showLoadingDialog(int message) {
		if (mProgress != null && mProgress.isShowing()) return;
		
		mProgress = DialogFactory.getProgressDialog(this);
		switch (message) {
		case LOADING_MSG:
			mProgress.setMessage(getString(R.string.loading_msg));
			break;
		case UPDATING_MSG:
			mProgress.setMessage(getString(R.string.ref_data_update_msg));
			break;
		}
		mProgress.show();
	}

	@Override
	public void onTaskComplete() {
		mProgress.dismiss();		
	}
	
	@Override
	public void showErrorDialog(String errorMessage) {
		mAlert = DialogFactory.getAlertDialog(this);
		mAlert.setTitle(getString(R.string.dialog_error));
		mAlert.setMessage(errorMessage);
		
		if (errorMessage.compareTo(AUTH_ERR_MSG) == 0) {
			mAlert.setButton(AlertDialog.BUTTON_POSITIVE, 
					getString(R.string.btn_ok), 
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					logoutCallback();
				}
			});
		}
		mAlert.show();
	}
		
	@Override
	public void showResult(String result) {
		mAlert = DialogFactory.getAlertDialog(this);
		mAlert.setTitle("Response");
		mAlert.setMessage(result);
		mAlert.show();
	}
	
	protected DialogInterface.OnClickListener cancelListener = 
			new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			return;
		}
	};
	
	@Override
	public void logoutCallback() {
		// remove lock pattern
		getSharedPreferences(LockPatternActivity.class.getSimpleName(), 0)
		.edit().remove(LockPatternActivity._PaternSha1).commit();
		
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	public boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) 
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
	
	@Override
	public Context getContext() {
		return this;
	}
}
