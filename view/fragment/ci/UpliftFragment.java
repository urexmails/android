package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class UpliftFragment extends SuperCIFragment {

	public static final String TAG = "UpliftFragment";

	private TextView mP_Q4U_1A_REVD_IN_TIMELY_MANNER_Txt;
	private TextView mP_Q4U_7_PROV_CHANGES_MEM_INV_Txt;
	private RadioGroup mP_Q4U_1_CARTON_KIT_REQD;
	private RadioGroup mP_Q4U_1A_REVD_IN_TIMELY_MANNER;
	private RadioGroup mP_Q4U_2_MEM_REVD_GUIDE;
	private RadioGroup mP_Q4U_3_MEM_AWARE_OF_ITEMS;
	private RadioGroup mP_Q4U_4_MEM_AWARE_ICR;
	private RadioGroup mP_Q4U_5_MEM_AWARE_OF_WARR;
	private RadioGroup mP_Q4U_6_MEM_AWARE_OF_CHANGES;
	private RadioGroup mP_Q4_7_CASE_MAN_AWARE_OF_CHANG;
	private RadioGroup mP_Q4_8_CASE_MANAGER_APPROVAL;
	private RadioGroup mP_Q4U_7_PROV_CHANGES_MEM_INV;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_uplift, null);

		// bind elements
		mP_Q4U_1A_REVD_IN_TIMELY_MANNER_Txt	= (TextView) v.findViewById(R.id.txt_P_Q4U_1A_REVD_IN_TIMELY_MANNER);
		mP_Q4U_7_PROV_CHANGES_MEM_INV_Txt  	= (TextView) v.findViewById(R.id.txt_P_Q4U_7_PROV_CHANGES_MEM_INVa);
		mP_Q4U_1_CARTON_KIT_REQD 			= (RadioGroup) v.findViewById(R.id.P_Q4U_1_CARTON_KIT_REQD);
		mP_Q4U_1A_REVD_IN_TIMELY_MANNER 	= (RadioGroup) v.findViewById(R.id.P_Q4U_1A_REVD_IN_TIMELY_MANNER);
		mP_Q4U_2_MEM_REVD_GUIDE 			= (RadioGroup) v.findViewById(R.id.P_Q4U_2_MEM_REVD_GUIDE);
		mP_Q4U_3_MEM_AWARE_OF_ITEMS 		= (RadioGroup) v.findViewById(R.id.P_Q4U_3_MEM_AWARE_OF_ITEMS);
		mP_Q4U_4_MEM_AWARE_ICR 				= (RadioGroup) v.findViewById(R.id.P_Q4U_4_MEM_AWARE_ICR);
		mP_Q4U_5_MEM_AWARE_OF_WARR 			= (RadioGroup) v.findViewById(R.id.P_Q4U_5_MEM_AWARE_OF_WARR);
		mP_Q4U_6_MEM_AWARE_OF_CHANGES 		= (RadioGroup) v.findViewById(R.id.P_Q4U_6_MEM_AWARE_OF_CHANGES);
		mP_Q4_7_CASE_MAN_AWARE_OF_CHANG 	= (RadioGroup) v.findViewById(R.id.P_Q4_7_CASE_MAN_AWARE_OF_CHANG);
		mP_Q4_8_CASE_MANAGER_APPROVAL 		= (RadioGroup) v.findViewById(R.id.P_Q4_8_CASE_MANAGER_APPROVAL);
		mP_Q4U_7_PROV_CHANGES_MEM_INV 		= (RadioGroup) v.findViewById(R.id.P_Q4U_7_PROV_CHANGES_MEM_INV);

		// set visibility
		mP_Q4U_1A_REVD_IN_TIMELY_MANNER_Txt.setVisibility(View.GONE);
		mP_Q4U_1A_REVD_IN_TIMELY_MANNER.setVisibility(View.GONE);
		mP_Q4U_7_PROV_CHANGES_MEM_INV_Txt.setVisibility(View.GONE);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q4U_1_CARTON_KIT_REQD, mP_Q4U_1_CARTON_KIT_REQD);
		checkAnswered(UploadInDTO.P_Q4U_1A_REVD_IN_TIMELY_MANNER, mP_Q4U_1A_REVD_IN_TIMELY_MANNER);
		checkAnswered(UploadInDTO.P_Q4U_2_MEM_REVD_GUIDE, mP_Q4U_2_MEM_REVD_GUIDE);
		checkAnswered(UploadInDTO.P_Q4U_3_MEM_AWARE_OF_ITEMS, mP_Q4U_3_MEM_AWARE_OF_ITEMS);
		checkAnswered(UploadInDTO.P_Q4U_4_MEM_AWARE_ICR, mP_Q4U_4_MEM_AWARE_ICR);
		checkAnswered(UploadInDTO.P_Q4U_5_MEM_AWARE_OF_WARR, mP_Q4U_5_MEM_AWARE_OF_WARR);
		checkAnswered(UploadInDTO.P_Q4U_6_MEM_AWARE_OF_CHANGES, mP_Q4U_6_MEM_AWARE_OF_CHANGES);
		checkAnswered(UploadInDTO.P_Q4_7_CASE_MAN_AWARE_OF_CHANG, mP_Q4_7_CASE_MAN_AWARE_OF_CHANG);
		checkAnswered(UploadInDTO.P_Q4_8_CASE_MANAGER_APPROVAL, mP_Q4_8_CASE_MANAGER_APPROVAL);
		checkAnswered(UploadInDTO.P_Q4U_7_PROV_CHANGES_MEM_INV, mP_Q4U_7_PROV_CHANGES_MEM_INV);

		// set listener
		mP_Q4U_1_CARTON_KIT_REQD.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_1A_REVD_IN_TIMELY_MANNER.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_2_MEM_REVD_GUIDE.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_3_MEM_AWARE_OF_ITEMS.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_4_MEM_AWARE_ICR.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_5_MEM_AWARE_OF_WARR.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_6_MEM_AWARE_OF_CHANGES.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4_7_CASE_MAN_AWARE_OF_CHANG.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4_8_CASE_MANAGER_APPROVAL.setOnCheckedChangeListener(mRadioButtonValidator);
		mP_Q4U_7_PROV_CHANGES_MEM_INV.setOnCheckedChangeListener(mRadioButtonValidator);

		getFragmentActivity().validateForm(CIFragmentActivity.GENERIC);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mP_Q4U_1_CARTON_KIT_REQD);
			setEnabledFromSignature(mP_Q4U_1A_REVD_IN_TIMELY_MANNER);
			setEnabledFromSignature(mP_Q4U_2_MEM_REVD_GUIDE);
			setEnabledFromSignature(mP_Q4U_3_MEM_AWARE_OF_ITEMS);
			setEnabledFromSignature(mP_Q4U_4_MEM_AWARE_ICR);
			setEnabledFromSignature(mP_Q4U_5_MEM_AWARE_OF_WARR);
			setEnabledFromSignature(mP_Q4U_6_MEM_AWARE_OF_CHANGES);
			setEnabledFromSignature(mP_Q4_7_CASE_MAN_AWARE_OF_CHANG);
			setEnabledFromSignature(mP_Q4_8_CASE_MANAGER_APPROVAL);
			setEnabledFromSignature(mP_Q4U_7_PROV_CHANGES_MEM_INV);
		}
	}
}
