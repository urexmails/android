package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class MemberRatingFragment extends SuperCIFragment {

	public static final String TAG = "MemberRatingFragment";

	private Spinner mQ7_Prov_Rat;
	private CheckBox mQ7_Remov_Comm;
	private CheckBox mQ7_Over_Impress;
	private CheckBox mQ7_Treat_Spec_Items;
	private CheckBox mQ7_Crew_Insuff;
	private CheckBox mQ7_ICR_Compl;
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_rating, null);

		// bind elements
		mQ7_Prov_Rat 			= (Spinner) v.findViewById(R.id.Q7_Prov_Rat);
		mQ7_Remov_Comm 			= (CheckBox) v.findViewById(R.id.Q7_Remov_Comm);
		mQ7_Over_Impress 		= (CheckBox) v.findViewById(R.id.Q7_Over_Impress);
		mQ7_Treat_Spec_Items 	= (CheckBox) v.findViewById(R.id.Q7_Treat_Spec_Items);
		mQ7_Crew_Insuff 		= (CheckBox) v.findViewById(R.id.Q7_Crew_Insuff);
		mQ7_ICR_Compl 			= (CheckBox) v.findViewById(R.id.Q7_ICR_Compl);

		// set dropdown view resource value
		mQ7_Prov_Rat.setAdapter(getCustomCustomDropDownView());

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		if (ModelFactory.getCIService().getCurrentForm().isDelivery()) {
			checkAnswered(UploadInDTO.P_Q6D_4_RATING, mQ7_Prov_Rat);
			checkAnswered(UploadInDTO.P_Q6D_4_REMLST_COMMS, mQ7_Remov_Comm);
			checkAnswered(UploadInDTO.P_Q6D_4_OVERALL_IMPRESSION, mQ7_Over_Impress);
			checkAnswered(UploadInDTO.P_Q6D_4_TREAT_ITEMS, mQ7_Treat_Spec_Items);
			checkAnswered(UploadInDTO.P_Q6D_4_NO_CREW_INSUFF, mQ7_Crew_Insuff);
			checkAnswered(UploadInDTO.P_Q6D_4_ICR_COMPLETED, mQ7_ICR_Compl);
		} else if (ModelFactory.getCIService().getCurrentForm().isUplift()) {
			checkAnswered(UploadInDTO.P_Q4U_8_RATING, mQ7_Prov_Rat);
			checkAnswered(UploadInDTO.P_Q4U_7_REMLST_COMMS, mQ7_Remov_Comm);
			checkAnswered(UploadInDTO.P_Q4U_7_OVERALL_IMPRESSION, mQ7_Over_Impress);
			checkAnswered(UploadInDTO.P_Q4U_7_TREAT_SPEC_ITEMS, mQ7_Treat_Spec_Items);
			checkAnswered(UploadInDTO.P_Q4U_7_NO_OF_CREW_INSUFF, mQ7_Crew_Insuff);
			checkAnswered(UploadInDTO.P_Q4U_7_ICR_COMPLETED, mQ7_ICR_Compl);
		}

		// set listeners
		mQ7_Prov_Rat.setOnItemSelectedListener(mSpinnerValidator);
		mQ7_Remov_Comm.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ7_Over_Impress.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ7_Treat_Spec_Items.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ7_Crew_Insuff.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ7_ICR_Compl.setOnCheckedChangeListener(mCheckBoxValidator);
		
		getFragmentActivity().validateForm(CIFragmentActivity.MEMBER_RATING);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ7_Prov_Rat);
			setEnabledFromSignature(mQ7_Remov_Comm);
			setEnabledFromSignature(mQ7_Over_Impress);
			setEnabledFromSignature(mQ7_Treat_Spec_Items);
			setEnabledFromSignature(mQ7_Crew_Insuff);
			setEnabledFromSignature(mQ7_ICR_Compl);
		}
	}
}
