package com.contactpoint.view.fragment;

import group.pals.android.lib.ui.lockpattern.LockPatternActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.MainActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.util.DialogFactory;

public abstract class NetworkFragment extends SherlockFragment implements NetworkListener  {

	protected AlertDialog mAlert;
	protected ProgressDialog mProgress;

	@Override
	public void showErrorDialog(String errorMessage) {
		mAlert = DialogFactory.getAlertDialog(this.getSherlockActivity());
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
		mAlert = DialogFactory.getAlertDialog(this.getSherlockActivity());
		mAlert.setTitle("Response");
		mAlert.setMessage(result);
		mAlert.show();
	}


	@Override
	public void showLoadingDialog(int message) {
		if (mProgress != null && mProgress.isShowing()) return;
		
		mProgress = DialogFactory.getProgressDialog(this.getSherlockActivity());
		mProgress.setMessage(getString(R.string.loading_msg));		
		mProgress.show();
	}


	@Override
	public void onTaskComplete() {
		mProgress.dismiss();
	}


	@Override
	public void logoutCallback() {
		// remove lock pattern
		getSherlockActivity().getSharedPreferences(LockPatternActivity.class.getSimpleName(), 0)
		.edit().remove(LockPatternActivity._PaternSha1).commit();

		Intent i = new Intent(getSherlockActivity(), MainActivity.class);
		startActivity(i);
		getSherlockActivity().finish();
	}


	@Override
	public boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) 
				this.getSherlockActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return (networkInfo != null && networkInfo.isConnected());
	}
	
	@Override
	public Context getContext() {
		return this.getSherlockActivity();
	}
}
