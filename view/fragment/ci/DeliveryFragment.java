package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.DTO.ci.validator.DecimalDigitsInputFilter;

public class DeliveryFragment extends SuperCIFragment {

	public static final String TAG = "DeliveryFragment";

	private TextView mQ6D_3a_LOL_Txt;
	private TextView mQ6D_3a_LOL_2_Txt;
	private EditText mQ6D_3a_LOL;
	private RadioGroup mQ5D_1_Prov_Unpack;
	private RadioGroup mQ5D_2_TT_Warran;
	private RadioGroup mQ5D_3_Unpack_Satis;
	private RadioGroup mQ5D_4_Remove_Debris;
	private RadioGroup mQ6D_1_Unpack_Cart;
	private RadioGroup mQ6D_2_Remove_Debris;
	private RadioGroup mQ6D_3_LOL;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_delivery, null);

		// bind elements
		mQ6D_3a_LOL_Txt 		= (TextView) v.findViewById(R.id.txt_Q6D_3a_LOL);
		mQ6D_3a_LOL_2_Txt 		= (TextView) v.findViewById(R.id.txt_Q6D_3a_LOL_2);
		mQ6D_3a_LOL 			= (EditText) v.findViewById(R.id.Q6D_3a_LOL);
		mQ5D_1_Prov_Unpack 		= (RadioGroup) v.findViewById(R.id.Q5D_1_Prov_Unpack);
		mQ5D_2_TT_Warran 		= (RadioGroup) v.findViewById(R.id.Q5D_2_TT_Warran);
		mQ5D_3_Unpack_Satis 	= (RadioGroup) v.findViewById(R.id.Q5D_3_Unpack_Satis);
		mQ5D_4_Remove_Debris 	= (RadioGroup) v.findViewById(R.id.Q5D_4_Remove_Debris);
		mQ6D_1_Unpack_Cart 		= (RadioGroup) v.findViewById(R.id.Q6D_1_Unpack_Cart);
		mQ6D_2_Remove_Debris 	= (RadioGroup) v.findViewById(R.id.Q6D_2_Remove_Debris);
		mQ6D_3_LOL 				= (RadioGroup) v.findViewById(R.id.Q6D_3_LOL);

		// set visibility
		mQ6D_3a_LOL_Txt.setVisibility(View.GONE);
		mQ6D_3a_LOL_2_Txt.setVisibility(View.GONE);
		mQ6D_3a_LOL.setVisibility(View.GONE);

		// set filter
		mQ6D_3a_LOL.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert answers
		checkAnswered(UploadInDTO.P_Q6D_1_MEM_ADVISED_PAID, mQ5D_1_Prov_Unpack);
		checkAnswered(UploadInDTO.P_Q6D_2_MEM_TOLL_WARR, mQ5D_2_TT_Warran);
		checkAnswered(UploadInDTO.P_Q6D_3_DELIVERY_COMPLETED, mQ5D_3_Unpack_Satis);
		checkAnswered(UploadInDTO.P_Q6D_4_MEM_REM_DEBRIS, mQ5D_4_Remove_Debris);
		checkAnswered(UploadInDTO.P_Q7D_1_REM_UNPACK, mQ6D_1_Unpack_Cart);
		checkAnswered(UploadInDTO.P_Q7D_2_PACK_MATL, mQ6D_2_Remove_Debris);
		checkAnswered(UploadInDTO.P_Q7D_3_ANYTHING_OFF_LOAD, mQ6D_3_LOL);
		checkAnswered(UploadInDTO.P_Q7D_3A_VOLUME, mQ6D_3a_LOL);

		// set listener
		mQ6D_3a_LOL.addTextChangedListener(mLOLEditTextValidator);
		mQ5D_1_Prov_Unpack.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ5D_2_TT_Warran.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ5D_3_Unpack_Satis.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ5D_4_Remove_Debris.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ6D_1_Unpack_Cart.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ6D_2_Remove_Debris.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ6D_3_LOL.setOnCheckedChangeListener(mRadioButtonValidator);
		
		getFragmentActivity().validateForm(CIFragmentActivity.GENERIC);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mQ6D_3a_LOL);
			setEnabledFromSignature(mQ5D_1_Prov_Unpack);
			setEnabledFromSignature(mQ5D_2_TT_Warran);
			setEnabledFromSignature(mQ5D_3_Unpack_Satis);
			setEnabledFromSignature(mQ5D_4_Remove_Debris);
			setEnabledFromSignature(mQ6D_1_Unpack_Cart);
			setEnabledFromSignature(mQ6D_2_Remove_Debris);
			setEnabledFromSignature(mQ6D_3_LOL);
		}
	}
}
