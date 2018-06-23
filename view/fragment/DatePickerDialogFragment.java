package com.contactpoint.view.fragment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class DatePickerDialogFragment extends SherlockDialogFragment {

	private OnDateSetListener mCallback;
	private int mYear, mMonth, mDay;

	public DatePickerDialogFragment(OnDateSetListener callback, int d, int m, int y) {
		mCallback = callback;
		mYear = y;
		mMonth = m;
		mDay = d;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), mCallback, mYear, mMonth,mDay);
	}
	
	public void setDate(int d, int m, int y) {
		mYear = y;
		mMonth = m;
		mDay = d;
	}

}
