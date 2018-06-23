package com.contactpoint.model.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.contactpoint.ci_prv_mm.R;

public class DialogFactory {
	
	private static AlertDialog mAlert;
	private static ProgressDialog mProgress;
	
	public static AlertDialog getAlertDialog(Context context) {
		if (mAlert != null && mAlert.getContext() == context) {
			return mAlert;
		}
		
		mAlert = null;
		mAlert = new AlertDialog.Builder(context).create();
		mAlert.setCanceledOnTouchOutside(false);
		mAlert.setCancelable(false);
		mAlert.setButton(AlertDialog.BUTTON_POSITIVE, 
				context.getString(R.string.btn_ok), 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		return mAlert;
	}
	
	public static ProgressDialog getProgressDialog(Context context) {
		if (mProgress != null && mProgress.getContext() == context) {
			return mProgress;
		}
		
		mProgress = null;
		mProgress = new ProgressDialog(context);
		mProgress.setTitle("");
		mProgress.setIndeterminate(false);
		mProgress.setCanceledOnTouchOutside(false);
		mProgress.setCancelable(false);
		
		return mProgress;
	}

}
