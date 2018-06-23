package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;

public class ProviderServiceSecondFragment extends SuperCIFragment {

	public static final String TAG = "ProviderServiceSecondFragment";

	private TextView mQ3C_9a_Spec_Pac_Txt;
	private TextView mQ3C_9b_Mat_For_Txt;
	private TextView mQ3C_11a_ICR_Txt;
	private TextView mQ3C_12a_Tru_Pos_Txt;
	private RadioGroup mQ3C_9_Plas_Cart;
	private RadioGroup mQ3C_9a_Spec_Pac;
	private CheckBox mQ3C_9b_Pro_TV;
	private CheckBox mQ3C_9b_Bike;
	private CheckBox mQ3C_9b_Pai;
	private CheckBox mQ3C_9b_Oth;
	private RadioGroup mQ3C_10_Items_Pre;
	private RadioGroup mQ3C_11_ICR_Avail;
	private RadioGroup mQ3C_11a_ICR_Complete;
	private RadioGroup mQ3C_12_Rem_Vehicle;
	private CheckBox mQ3C_12a_Tru_Dri;
	private CheckBox mQ3C_12a_Tru_Dou;
	private CheckBox mQ3C_12a_Tru_Ver;

	private LayoutParams mQ3C_10_Items_Pre_Layout;	// fix positioning
	public static final int LONG_DISTANCE 	= 50;
	public static final int SHORT_DISTANCE 	= 5;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_provider_service_2, null);
		v.findViewById(R.id.btn_next).setOnClickListener(mNextListener);	
		v.findViewById(R.id.btn_prev).setOnClickListener(mPrevListener);	

		// bind elements
		mQ3C_9a_Spec_Pac_Txt 	= (TextView) v.findViewById(R.id.txt_Q3C_9a_Spec_Pac);
		mQ3C_9b_Mat_For_Txt 	= (TextView) v.findViewById(R.id.txt_Q3C_9b_Mat_For);
		mQ3C_11a_ICR_Txt 		= (TextView) v.findViewById(R.id.txt_Q3C_11a_ICR_Complete);
		mQ3C_12a_Tru_Pos_Txt 	= (TextView) v.findViewById(R.id.txt_Q3C_12a_Tru_Pos);
		mQ3C_9_Plas_Cart 		= (RadioGroup) v.findViewById(R.id.Q3C_9_Plas_Cart);
		mQ3C_9a_Spec_Pac 		= (RadioGroup) v.findViewById(R.id.Q3C_9a_Spec_Pac);
		mQ3C_9b_Pro_TV 			= (CheckBox) v.findViewById(R.id.Q3C_9b_Pro_TV);
		mQ3C_9b_Bike 			= (CheckBox) v.findViewById(R.id.Q3C_9b_Bike);
		mQ3C_9b_Pai 			= (CheckBox) v.findViewById(R.id.Q3C_9b_Pai);
		mQ3C_9b_Oth 			= (CheckBox) v.findViewById(R.id.Q3C_9b_Oth);
		mQ3C_10_Items_Pre 		= (RadioGroup) v.findViewById(R.id.Q3C_10_Items_Pre);
		mQ3C_11_ICR_Avail 		= (RadioGroup) v.findViewById(R.id.Q3C_11_ICR_Avail);
		mQ3C_11a_ICR_Complete 	= (RadioGroup) v.findViewById(R.id.Q3C_11a_ICR_Complete);
		mQ3C_12_Rem_Vehicle 	= (RadioGroup) v.findViewById(R.id.Q3C_12_Rem_Vehicle);
		mQ3C_12a_Tru_Dri 		= (CheckBox) v.findViewById(R.id.Q3C_12a_Tru_Dri);
		mQ3C_12a_Tru_Dou 		= (CheckBox) v.findViewById(R.id.Q3C_12a_Tru_Dou);
		mQ3C_12a_Tru_Ver 		= (CheckBox) v.findViewById(R.id.Q3C_12a_Tru_Ver);

		// set visibility for some elements
		mQ3C_9a_Spec_Pac_Txt.setVisibility(View.GONE);
		mQ3C_9a_Spec_Pac.setVisibility(View.GONE);
		mQ3C_9b_Mat_For_Txt.setVisibility(View.GONE);
		mQ3C_9b_Pro_TV.setVisibility(View.GONE);
		mQ3C_9b_Bike.setVisibility(View.GONE);
		mQ3C_9b_Pai.setVisibility(View.GONE);
		mQ3C_9b_Oth.setVisibility(View.GONE);
		mQ3C_11a_ICR_Txt.setVisibility(View.GONE);
		mQ3C_11a_ICR_Complete.setVisibility(View.GONE);
		mQ3C_12a_Tru_Pos_Txt.setVisibility(View.GONE);
		mQ3C_12a_Tru_Dri.setVisibility(View.GONE);
		mQ3C_12a_Tru_Dou.setVisibility(View.GONE);
		mQ3C_12a_Tru_Ver.setVisibility(View.GONE);

		mQ3C_10_Items_Pre_Layout = 
				(RelativeLayout.LayoutParams) mQ3C_10_Items_Pre.getLayoutParams();
		mQ3C_10_Items_Pre_Layout.topMargin = LONG_DISTANCE;
		mQ3C_10_Items_Pre.setLayoutParams(mQ3C_10_Items_Pre_Layout);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q3C_9_SPEC_PACKAGING, mQ3C_9_Plas_Cart);
		checkAnswered(UploadInDTO.P_Q3C_9A_SPEC_MATLS_PROVIDED, mQ3C_9a_Spec_Pac);
		checkAnswered(UploadInDTO.P_Q3C_9B_PLASMA_REAR_PROJ, mQ3C_9b_Pro_TV);
		checkAnswered(UploadInDTO.P_Q3C_9B_BIKE, mQ3C_9b_Bike);
		checkAnswered(UploadInDTO.P_Q3C_9B_PAINTING, mQ3C_9b_Pai);
		checkAnswered(UploadInDTO.P_Q3C_9B_OTHER, mQ3C_9b_Oth);
		checkAnswered(UploadInDTO.P_Q3C_10_REMOVAL_AS_REQD, mQ3C_10_Items_Pre);
		checkAnswered(UploadInDTO.P_Q3C_11_ICR, mQ3C_11_ICR_Avail);
		checkAnswered(UploadInDTO.P_Q3C_11A_ICR_CORRECT, mQ3C_11a_ICR_Complete);
		checkAnswered(UploadInDTO.P_Q3C_12_REMLST_VEH_POS, mQ3C_12_Rem_Vehicle);
		checkAnswered(UploadInDTO.P_Q3C_12A_TRUCK_IN_DRIVEWAY, mQ3C_12a_Tru_Dri);
		checkAnswered(UploadInDTO.P_Q3C_12A_TRUCK_DBL_PARKED, mQ3C_12a_Tru_Dou);
		checkAnswered(UploadInDTO.P_Q3C_12A_TRUCK_ON_VERGE, mQ3C_12a_Tru_Ver);

		// set listener
		mQ3C_9_Plas_Cart.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_9a_Spec_Pac.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_9b_Pro_TV.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_9b_Bike.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_9b_Pai.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_9b_Oth.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_10_Items_Pre.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_11_ICR_Avail.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_11a_ICR_Complete.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_12_Rem_Vehicle.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ3C_12a_Tru_Dri.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_12a_Tru_Dou.setOnCheckedChangeListener(mCheckBoxValidator);
		mQ3C_12a_Tru_Ver.setOnCheckedChangeListener(mCheckBoxValidator);
		
		getFragmentActivity().validateForm(CIFragmentActivity.PROVIDER_SERVICE);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ3C_9_Plas_Cart);
			setEnabledFromSignature(mQ3C_9a_Spec_Pac);
			setEnabledFromSignature(mQ3C_9b_Pro_TV);
			setEnabledFromSignature(mQ3C_9b_Bike);
			setEnabledFromSignature(mQ3C_9b_Pai);
			setEnabledFromSignature(mQ3C_9b_Oth);
			setEnabledFromSignature(mQ3C_10_Items_Pre);
			setEnabledFromSignature(mQ3C_11_ICR_Avail);
			setEnabledFromSignature(mQ3C_11a_ICR_Complete);
			setEnabledFromSignature(mQ3C_12_Rem_Vehicle);
			setEnabledFromSignature(mQ3C_12a_Tru_Dri);
			setEnabledFromSignature(mQ3C_12a_Tru_Dou);
			setEnabledFromSignature(mQ3C_12a_Tru_Ver);
		}
	}
}