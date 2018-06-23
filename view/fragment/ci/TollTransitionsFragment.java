package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class TollTransitionsFragment extends SuperCIFragment {

	public static final String TAG = "TollTransitionsFragment";

	private Spinner mQ8_Toll_Tran;
	private EditText mQ8_Time_Site;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_toll_rating, null);

		// bind elements
		mQ8_Toll_Tran = (Spinner)  v.findViewById(R.id.Q8_Toll_Tran);
		mQ8_Time_Site = (EditText) v.findViewById(R.id.Q8_Time_Site);

		// set dropdown view resource value
		mQ8_Toll_Tran.setAdapter(getCustomCustomDropDownView());

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q8_1_RATING, mQ8_Toll_Tran);
		checkAnswered(UploadInDTO.P_Q8_2_TIME_ON_SITE, mQ8_Time_Site);
		
		// set listeners
		mQ8_Toll_Tran.setOnItemSelectedListener(mSpinnerValidator);
		mQ8_Time_Site.addTextChangedListener(mTimeOnSiteEditTextValidator);
		
		getFragmentActivity().validateForm(CIFragmentActivity.TOLL_RATING);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ8_Toll_Tran);
			setEnabledFromSignature(mQ8_Time_Site);
		}
	}
}
