package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.DTO.ci.validator.DecimalDigitsInputFilter;

public class VolumeFragment extends SuperCIFragment {

	public static final String TAG = "VolumeFragment";
	private EditText mQ9_Vol;
	private EditText mQ9_Comm;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_volume, null);

		// bind elements
		mQ9_Vol = (EditText) v.findViewById(R.id.Q9_Vol);
		mQ9_Comm = (EditText) v.findViewById(R.id.Q9_Comm);

		// set filter
		mQ9_Vol.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		if (ModelFactory.getCIService().getCurrentForm().isDelivery()) {
			checkAnswered(UploadInDTO.P_Q7D_VOLUME, mQ9_Vol);
		} else if (ModelFactory.getCIService().getCurrentForm().isUplift()) {
			checkAnswered(UploadInDTO.P_Q5U_VOLUME, mQ9_Vol);
		}
		checkAnswered(UploadInDTO.P_Q9_COMM, mQ9_Comm);

		// set listeners
		mQ9_Vol.addTextChangedListener(mVolumeEditTextValidator);
		mQ9_Comm.addTextChangedListener(mCommentEditTextValidator);

		getFragmentActivity().validateForm(CIFragmentActivity.VOLUME);
	}
}
