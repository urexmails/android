package com.contactpoint.view.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class TimePickerDialogFragment extends SherlockDialogFragment {
	private OnTimeSetListener mCallback;
	private int mHour, mMinute;

	public TimePickerDialogFragment(OnTimeSetListener callback, int h, int m) {
		mCallback = callback;
		mHour = h;
		mMinute = m;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new TimePickerDialog(getActivity(), mCallback, mHour, mMinute, true);
	}
	
	public void setTime(int h, int m) {
		mHour = h;
		mMinute = m; 
	}
}
