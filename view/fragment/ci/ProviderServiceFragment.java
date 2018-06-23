package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class ProviderServiceFragment extends SuperCIFragment {

	public static final String TAG = "ProviderServiceFragment";

	private EditText mQ3C_Num_Crew;
	private RadioGroup mQ3C_Pro_Logo;
	private Spinner mQ3C_1_Pres_Crew;
	private Spinner mQ3C_2_Skill_Crew;
	private Spinner mQ3C_3_Cond_Veh;
	private Spinner mQ3C_4_Pac_Mat;
	private Spinner mQ3C_5_Sta_Con;
	private RadioGroup mQ3C_6_Rem_Time;
	private RadioGroup mQ3C_7_Pro_Man;
	private RadioGroup mQ3C_8_Tim_Man;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_provider_service, null);

		mQ3C_Num_Crew 	  = (EditText) v.findViewById(R.id.Q3C_Num_Crew);
		mQ3C_Pro_Logo 	  = (RadioGroup) v.findViewById(R.id.Q3C_Pro_Logo);
		mQ3C_1_Pres_Crew  = (Spinner) v.findViewById(R.id.Q3C_1_Pres_Crew);
		mQ3C_2_Skill_Crew = (Spinner) v.findViewById(R.id.Q3C_2_Skill_Crew);
		mQ3C_3_Cond_Veh   = (Spinner) v.findViewById(R.id.Q3C_3_Cond_Veh);
		mQ3C_4_Pac_Mat 	  = (Spinner) v.findViewById(R.id.Q3C_4_Pac_Mat);
		mQ3C_5_Sta_Con 	  = (Spinner) v.findViewById(R.id.Q3C_5_Sta_Con);
		mQ3C_6_Rem_Time   = (RadioGroup) v.findViewById(R.id.Q3C_6_Rem_Time);
		mQ3C_7_Pro_Man 	  = (RadioGroup) v.findViewById(R.id.Q3C_7_Pro_Man);
		mQ3C_8_Tim_Man 	  = (RadioGroup) v.findViewById(R.id.Q3C_8_Tim_Man);

		// set spinner values
		ArrayAdapter<CharSequence> listAdapter = getCustomCustomDropDownView();
		mQ3C_1_Pres_Crew.setAdapter(listAdapter);
		mQ3C_2_Skill_Crew.setAdapter(listAdapter);
		mQ3C_3_Cond_Veh.setAdapter(listAdapter);
		mQ3C_4_Pac_Mat.setAdapter(listAdapter);
		mQ3C_5_Sta_Con.setAdapter(listAdapter);

		v.findViewById(R.id.btn_next).setOnClickListener(mNextListener);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q3C_NO_OF_CREW, mQ3C_Num_Crew);
		checkAnswered(UploadInDTO.P_Q3C_PROVIDER_LOGO_DISPLAYED, mQ3C_Pro_Logo);
		checkAnswered(UploadInDTO.P_Q3C_1_PRESENTATION_CREW, mQ3C_1_Pres_Crew);
		checkAnswered(UploadInDTO.P_Q3C_2_CUST_SERVICE_SKILL, mQ3C_2_Skill_Crew);
		checkAnswered(UploadInDTO.P_Q3C_3_COND_OF_VEHICLE, mQ3C_3_Cond_Veh);
		checkAnswered(UploadInDTO.P_Q3C_4_COND_OF_PACKAGING, mQ3C_4_Pac_Mat);
		checkAnswered(UploadInDTO.P_Q3C_5_STANDING_CONTAINER, mQ3C_5_Sta_Con);
		checkAnswered(UploadInDTO.P_Q3C_6_REMLST_ON_TIME, mQ3C_6_Rem_Time);
		checkAnswered(UploadInDTO.P_Q3C_7_REMLST_PROFESSIONAL, mQ3C_7_Pro_Man);
		checkAnswered(UploadInDTO.P_Q3C_8_REM_CONDUCTED_TIMELY, mQ3C_8_Tim_Man);

		// set listeners
		mQ3C_Num_Crew.addTextChangedListener(mNumOfCrewEditTextValidator);
		mQ3C_Pro_Logo.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_1_Pres_Crew.setOnItemSelectedListener(mSpinnerValidator);
		mQ3C_2_Skill_Crew.setOnItemSelectedListener(mSpinnerValidator);
		mQ3C_3_Cond_Veh.setOnItemSelectedListener(mSpinnerValidator);
		mQ3C_4_Pac_Mat.setOnItemSelectedListener(mSpinnerValidator);
		mQ3C_5_Sta_Con.setOnItemSelectedListener(mSpinnerValidator);
		mQ3C_6_Rem_Time.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_7_Pro_Man.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_8_Tim_Man.setOnCheckedChangeListener(mRadioButtonValidator);

		getFragmentActivity().validateForm(CIFragmentActivity.PROVIDER_SERVICE);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ3C_Num_Crew);
			setEnabledFromSignature(mQ3C_Pro_Logo);
			setEnabledFromSignature(mQ3C_1_Pres_Crew);
			setEnabledFromSignature(mQ3C_2_Skill_Crew);
			setEnabledFromSignature(mQ3C_3_Cond_Veh);
			setEnabledFromSignature(mQ3C_4_Pac_Mat);
			setEnabledFromSignature(mQ3C_5_Sta_Con);
			setEnabledFromSignature(mQ3C_6_Rem_Time);
			setEnabledFromSignature(mQ3C_7_Pro_Man);
			setEnabledFromSignature(mQ3C_8_Tim_Man);
		}
	}
}
