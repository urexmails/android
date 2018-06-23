package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class ProviderServiceThirdFragment extends SuperCIFragment {

	public static final String TAG = "ProviderServiceThirdFragment";
	private TextView mQ3C_15a_Issu_Txt;
	private RadioGroup mQ3C_13_FE_Respect;
	private RadioGroup mQ3C_14_Res_Respect;
	private RadioGroup mQ3C_15_OHS_Comp;
	private CheckBox mQ3C_15a_No_Gear;
	private CheckBox mQ3C_15a_Tru_Unl;
	private CheckBox mQ3C_15a_Uns_List;
	private CheckBox mQ3C_15a_Pac;
	private CheckBox mQ3C_15a_Tru_Loc;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_provider_service_3, null);
		v.findViewById(R.id.btn_prev).setOnClickListener(mPrevListener);	

		// bind elements
		mQ3C_15a_Issu_Txt 	= (TextView) v.findViewById(R.id.txt_Q3C_15a_Issu);
		mQ3C_13_FE_Respect 	= (RadioGroup) v.findViewById(R.id.Q3C_13_FE_Respect);
		mQ3C_14_Res_Respect = (RadioGroup) v.findViewById(R.id.Q3C_14_Res_Respect);
		mQ3C_15_OHS_Comp 	= (RadioGroup) v.findViewById(R.id.Q3C_15_OHS_Comp);
		mQ3C_15a_No_Gear 	= (CheckBox) v.findViewById(R.id.Q3C_15a_No_Gear);
		mQ3C_15a_Tru_Unl 	= (CheckBox) v.findViewById(R.id.Q3C_15a_Tru_Unl);
		mQ3C_15a_Uns_List 	= (CheckBox) v.findViewById(R.id.Q3C_15a_Uns_List);
		mQ3C_15a_Pac 		= (CheckBox) v.findViewById(R.id.Q3C_15a_Pac);
		mQ3C_15a_Tru_Loc 	= (CheckBox) v.findViewById(R.id.Q3C_15a_Tru_Loc);

		// set visible
		mQ3C_15a_Issu_Txt.setVisibility(View.GONE);
		mQ3C_15a_No_Gear.setVisibility(View.GONE);
		mQ3C_15a_Tru_Unl.setVisibility(View.GONE);
		mQ3C_15a_Uns_List.setVisibility(View.GONE);
		mQ3C_15a_Pac.setVisibility(View.GONE);
		mQ3C_15a_Tru_Loc.setVisibility(View.GONE);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q3C_13_REMLST_FE_RESP, mQ3C_13_FE_Respect);
		checkAnswered(UploadInDTO.P_Q3C_14_REMLST_TREATING_RESID, mQ3C_14_Res_Respect);
		checkAnswered(UploadInDTO.P_Q3C_15_PROVIDER_OHS_COMP, mQ3C_15_OHS_Comp);
		checkAnswered(UploadInDTO.P_Q3C_15A_NO_SAFETY_GEAR, mQ3C_15a_No_Gear);
		checkAnswered(UploadInDTO.P_Q3C_15A_TRUCK_LOAD, mQ3C_15a_Tru_Unl);
		checkAnswered(UploadInDTO.P_Q3C_15A_UNSAFE_LIFT_CARRY, mQ3C_15a_Uns_List);
		checkAnswered(UploadInDTO.P_Q3C_15A_PACKING, mQ3C_15a_Pac);
		checkAnswered(UploadInDTO.P_Q3C_15A_TRUCK_LOCATION, mQ3C_15a_Tru_Loc);

		// set listeners
		mQ3C_13_FE_Respect.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_14_Res_Respect.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_15_OHS_Comp.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_15a_No_Gear.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_15a_Tru_Unl.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_15a_Uns_List.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_15a_Pac.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_15a_Tru_Loc.setOnCheckedChangeListener(mCheckBoxValidator);
		
		getFragmentActivity().validateForm(CIFragmentActivity.PROVIDER_SERVICE);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ3C_13_FE_Respect);
			setEnabledFromSignature(mQ3C_14_Res_Respect);
			setEnabledFromSignature(mQ3C_15_OHS_Comp);
			setEnabledFromSignature(mQ3C_15a_No_Gear);
			setEnabledFromSignature(mQ3C_15a_Tru_Unl);
			setEnabledFromSignature(mQ3C_15a_Uns_List);
			setEnabledFromSignature(mQ3C_15a_Pac);
			setEnabledFromSignature(mQ3C_15a_Tru_Loc);
		}
	}
}
