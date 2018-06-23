package com.contactpoint.view.fragment.ci;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.client.ci.CIDownload;
import com.contactpoint.view.SignatureView;

public abstract class SuperCIFragment extends SherlockFragment {

	protected void insertAnswer(String questionCode, String answer, int index) {
		getFragmentActivity().getUploadInDTO().getQuestionAnswers()
		.put(questionCode, answer);
		getFragmentActivity().validateForm(index);
	}

	protected void insertSignature(String type, String signature) {
		getFragmentActivity().getUploadInDTO().getSignatureContract().put(type, signature);
		getFragmentActivity().validateForm(CIFragmentActivity.SIGNATURE);
		getFragmentActivity().getUploadInDTO().setHasSignatures(true);
	}
	
	protected void removeSignature(String type) {
		getFragmentActivity().getUploadInDTO().getSignatureContract().remove(type);
		getFragmentActivity().validateForm(CIFragmentActivity.SIGNATURE);
		
		if (getFragmentActivity().getUploadInDTO().getSignatureContract().size() == 0) {
			getFragmentActivity().getUploadInDTO().setHasSignatures(false);
		}
	}

	protected CIDownload getCIDownloadOutDTO() {
		return ModelFactory.getCIService().getCurrentForm().getDownload();
//		return ((CIFragmentActivity)getSherlockActivity()).getUploadInDTO().getCIDownload();
	}

	protected CIFragmentActivity getFragmentActivity() {
		return (CIFragmentActivity)getSherlockActivity();
	}

	protected ArrayAdapter<CharSequence> getCustomCustomDropDownView() { 
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				getSherlockActivity(), R.array.rating_list, 
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	protected void startTimeStamp() {
		HashMap<String, String> qa = getFragmentActivity().getUploadInDTO().getQuestionAnswers();
		if (!qa.containsKey(UploadInDTO.P_TRANSITIONS_START_TIMESTAMP)) {
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy h:m:s a");
			qa.put(UploadInDTO.P_TRANSITIONS_START_TIMESTAMP, df.format(new Date()));
			qa.put(UploadInDTO.P_APP_VERSION, getResources().getString(R.string.app_version));
			
			// retrieve current location
			ModelFactory.getCIService().findMyLocation(getFragmentActivity());
		}
	}
	
	protected void checkAnswered(String qCode, View v) {
		HashMap<String, String> qa = getFragmentActivity().getUploadInDTO().getQuestionAnswers();
		if (!qa.containsKey(qCode) || qa.get(qCode) == null) return;
		
		if (v instanceof EditText) {
			EditText et = (EditText)v;
			et.setText(qa.get(qCode));
		} else if (v instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup)v;
			if (qa.get(qCode).compareTo(UploadInDTO.YES) == 0) {
				((RadioButton)rg.getChildAt(0)).setChecked(true);
			} else {
				((RadioButton)rg.getChildAt(1)).setChecked(true);
			}
		} else if (v instanceof CheckBox) {
			CheckBox cb = (CheckBox)v;
			cb.setChecked(qa.get(qCode).compareTo(UploadInDTO.YES) == 0);
		} else if (v instanceof Spinner) {
			Spinner s = (Spinner)v;
			String answer = qa.get(qCode);
			if (answer.compareTo(UploadInDTO.EXCELLENT) == 0) {
				s.setSelection(1);
			} else if (answer.compareTo(UploadInDTO.GOOD) == 0) {
				s.setSelection(2);
			} else if (answer.compareTo(UploadInDTO.AVERAGE) == 0) {
				s.setSelection(3);
			} else if (answer.compareTo(UploadInDTO.POOR) == 0) {
				s.setSelection(4);
			} else {
				s.setSelection(5);
			}
		} 
	}
	
	protected boolean checkSigned(String sCode, SignatureView v) {
		HashMap<String, String> qa = getFragmentActivity().getUploadInDTO().getSignatureContract();
		if (!qa.containsKey(sCode)) {
			return false;
		}
		v.loadBase64String(qa.get(sCode));
		getFragmentActivity().getUploadInDTO().setHasSignatures(true);
		getFragmentActivity().validateForm(CIFragmentActivity.SIGNATURE);
		return true;
	}
	
	// method to enable/disable a component based on signatures
	protected void setEnabledFromSignature(View v) {
		boolean hasSignature = getFragmentActivity().getUploadInDTO().hasSignature();
		
		// signatures cases
		if (v.getTag() == UploadInDTO.Member || v.getTag() == UploadInDTO.Agent || v instanceof SignatureView) {
			hasSignature = getFragmentActivity().getUploadInDTO().getSignatureContract().containsKey(v.getTag());
		}
		
		if (v instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup)v;
			for (int i = 0; i < rg.getChildCount(); i++) {
				((RadioButton)rg.getChildAt(i)).setEnabled(!hasSignature);
			}
		} else {
			v.setEnabled(!hasSignature);
		}
	}
	
	protected OnCheckedChangeListener mRadioButtonValidator = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			startTimeStamp();
			switch(checkedId) {
			case R.id.Q1_Serv_Ord_y:
				insertAnswer(UploadInDTO.P_Q1_SERVICE_ORDERED, UploadInDTO.YES,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q1_Serv_Ord_n:
				insertAnswer(UploadInDTO.P_Q1_SERVICE_ORDERED, UploadInDTO.NO,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q2C_Conf_Date_y:
				insertAnswer(UploadInDTO.P_Q2_PROVIDER_CONFIRM, UploadInDTO.YES,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q2C_Conf_Date_n:
				insertAnswer(UploadInDTO.P_Q2_PROVIDER_CONFIRM, UploadInDTO.NO,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q2C_Time_Met_y:
				insertAnswer(UploadInDTO.P_Q2C_TIME_MET, UploadInDTO.YES,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q2C_Time_Met_n:
				insertAnswer(UploadInDTO.P_Q2C_TIME_MET, UploadInDTO.NO,
						CIFragmentActivity.SERVICE_DETAIL);
				break;
			case R.id.Q3C_Pro_Logo_y:
				insertAnswer(UploadInDTO.P_Q3C_PROVIDER_LOGO_DISPLAYED, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_Pro_Logo_n:
				insertAnswer(UploadInDTO.P_Q3C_PROVIDER_LOGO_DISPLAYED, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_6_Rem_Time_y:
				insertAnswer(UploadInDTO.P_Q3C_6_REMLST_ON_TIME, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_6_Rem_Time_n:
				insertAnswer(UploadInDTO.P_Q3C_6_REMLST_ON_TIME, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_7_Pro_Man_y:
				insertAnswer(UploadInDTO.P_Q3C_7_REMLST_PROFESSIONAL, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_7_Pro_Man_n:
				insertAnswer(UploadInDTO.P_Q3C_7_REMLST_PROFESSIONAL, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_8_Tim_Man_y:
				insertAnswer(UploadInDTO.P_Q3C_8_REM_CONDUCTED_TIMELY, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_8_Tim_Man_n:
				insertAnswer(UploadInDTO.P_Q3C_8_REM_CONDUCTED_TIMELY, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9_Plas_Cart_y:
				insertAnswer(UploadInDTO.P_Q3C_9_SPEC_PACKAGING, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9_Plas_Cart_n:
				insertAnswer(UploadInDTO.P_Q3C_9_SPEC_PACKAGING, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9a_Spec_Pac_y:
				insertAnswer(UploadInDTO.P_Q3C_9A_SPEC_MATLS_PROVIDED, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9a_Spec_Pac_n:
				insertAnswer(UploadInDTO.P_Q3C_9A_SPEC_MATLS_PROVIDED, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_10_Items_Pre_y:
				insertAnswer(UploadInDTO.P_Q3C_10_REMOVAL_AS_REQD, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_10_Items_Pre_n:
				insertAnswer(UploadInDTO.P_Q3C_10_REMOVAL_AS_REQD, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_11_ICR_Avail_y:
				insertAnswer(UploadInDTO.P_Q3C_11_ICR, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_11_ICR_Avail_n:
				insertAnswer(UploadInDTO.P_Q3C_11_ICR, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_11a_ICR_Complete_y:
				insertAnswer(UploadInDTO.P_Q3C_11A_ICR_CORRECT, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_11a_ICR_Complete_n:
				insertAnswer(UploadInDTO.P_Q3C_11A_ICR_CORRECT, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_12_Rem_Vehicle_y:
				insertAnswer(UploadInDTO.P_Q3C_12_REMLST_VEH_POS, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_12_Rem_Vehicle_n:
				insertAnswer(UploadInDTO.P_Q3C_12_REMLST_VEH_POS, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_13_FE_Respect_y:
				insertAnswer(UploadInDTO.P_Q3C_13_REMLST_FE_RESP, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_13_FE_Respect_n:
				insertAnswer(UploadInDTO.P_Q3C_13_REMLST_FE_RESP, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_14_Res_Respect_y:
				insertAnswer(UploadInDTO.P_Q3C_14_REMLST_TREATING_RESID, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_14_Res_Respect_n:
				insertAnswer(UploadInDTO.P_Q3C_14_REMLST_TREATING_RESID, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15_OHS_Comp_y:
				insertAnswer(UploadInDTO.P_Q3C_15_PROVIDER_OHS_COMP, UploadInDTO.YES,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15_OHS_Comp_n:
				insertAnswer(UploadInDTO.P_Q3C_15_PROVIDER_OHS_COMP, UploadInDTO.NO,
						CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.P_Q4U_1_CARTON_KIT_REQD_y:
				insertAnswer(UploadInDTO.P_Q4U_1_CARTON_KIT_REQD, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_1_CARTON_KIT_REQD_n:
				insertAnswer(UploadInDTO.P_Q4U_1_CARTON_KIT_REQD, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_1A_REVD_IN_TIMELY_MANNER_y:
				insertAnswer(UploadInDTO.P_Q4U_1A_REVD_IN_TIMELY_MANNER, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_1A_REVD_IN_TIMELY_MANNER_n:
				insertAnswer(UploadInDTO.P_Q4U_1A_REVD_IN_TIMELY_MANNER, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_2_MEM_REVD_GUIDE_y:
				insertAnswer(UploadInDTO.P_Q4U_2_MEM_REVD_GUIDE, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_2_MEM_REVD_GUIDE_n:
				insertAnswer(UploadInDTO.P_Q4U_2_MEM_REVD_GUIDE, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_3_MEM_AWARE_OF_ITEMS_y:
				insertAnswer(UploadInDTO.P_Q4U_3_MEM_AWARE_OF_ITEMS, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_3_MEM_AWARE_OF_ITEMS_n:
				insertAnswer(UploadInDTO.P_Q4U_3_MEM_AWARE_OF_ITEMS, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_4_MEM_AWARE_ICR_y:
				insertAnswer(UploadInDTO.P_Q4U_4_MEM_AWARE_ICR, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_4_MEM_AWARE_ICR_n:
				insertAnswer(UploadInDTO.P_Q4U_4_MEM_AWARE_ICR, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_5_MEM_AWARE_OF_WARR_y:
				insertAnswer(UploadInDTO.P_Q4U_5_MEM_AWARE_OF_WARR, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_5_MEM_AWARE_OF_WARR_n:
				insertAnswer(UploadInDTO.P_Q4U_5_MEM_AWARE_OF_WARR, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_6_MEM_AWARE_OF_CHANGES_y:
				insertAnswer(UploadInDTO.P_Q4U_6_MEM_AWARE_OF_CHANGES, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_6_MEM_AWARE_OF_CHANGES_n:
				insertAnswer(UploadInDTO.P_Q4U_6_MEM_AWARE_OF_CHANGES, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4_7_CASE_MAN_AWARE_OF_CHANG_y:
				insertAnswer(UploadInDTO.P_Q4_7_CASE_MAN_AWARE_OF_CHANG, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4_7_CASE_MAN_AWARE_OF_CHANG_n:
				insertAnswer(UploadInDTO.P_Q4_7_CASE_MAN_AWARE_OF_CHANG, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4_8_CASE_MANAGER_APPROVAL_y:
				insertAnswer(UploadInDTO.P_Q4_8_CASE_MANAGER_APPROVAL, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4_8_CASE_MANAGER_APPROVAL_n:
				insertAnswer(UploadInDTO.P_Q4_8_CASE_MANAGER_APPROVAL, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_7_PROV_CHANGES_MEM_INV_y:
				insertAnswer(UploadInDTO.P_Q4U_7_PROV_CHANGES_MEM_INV, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.P_Q4U_7_PROV_CHANGES_MEM_INV_n:
				insertAnswer(UploadInDTO.P_Q4U_7_PROV_CHANGES_MEM_INV, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_1_Prov_Unpack_y:
				insertAnswer(UploadInDTO.P_Q6D_1_MEM_ADVISED_PAID, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_1_Prov_Unpack_n:
				insertAnswer(UploadInDTO.P_Q6D_1_MEM_ADVISED_PAID, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_2_TT_Warran_y:
				insertAnswer(UploadInDTO.P_Q6D_2_MEM_TOLL_WARR, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_2_TT_Warran_n:
				insertAnswer(UploadInDTO.P_Q6D_2_MEM_TOLL_WARR, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_3_Unpack_Satis_y:
				insertAnswer(UploadInDTO.P_Q6D_3_DELIVERY_COMPLETED, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_3_Unpack_Satis_n:
				insertAnswer(UploadInDTO.P_Q6D_3_DELIVERY_COMPLETED, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_4_Remove_Debris_y:
				insertAnswer(UploadInDTO.P_Q6D_4_MEM_REM_DEBRIS, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q5D_4_Remove_Debris_n:
				insertAnswer(UploadInDTO.P_Q6D_4_MEM_REM_DEBRIS, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_1_Unpack_Cart_y:
				insertAnswer(UploadInDTO.P_Q7D_1_REM_UNPACK, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_1_Unpack_Cart_n:
				insertAnswer(UploadInDTO.P_Q7D_1_REM_UNPACK, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_2_Remove_Debris_y:
				insertAnswer(UploadInDTO.P_Q7D_2_PACK_MATL, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_2_Remove_Debris_n:
				insertAnswer(UploadInDTO.P_Q7D_2_PACK_MATL, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_3_LOL_y:
				insertAnswer(UploadInDTO.P_Q7D_3_ANYTHING_OFF_LOAD, UploadInDTO.YES,
						CIFragmentActivity.GENERIC);
				break;
			case R.id.Q6D_3_LOL_n:
				insertAnswer(UploadInDTO.P_Q7D_3_ANYTHING_OFF_LOAD, UploadInDTO.NO,
						CIFragmentActivity.GENERIC);
				break;
			}

			switch (checkedId) {
			case R.id.Q2C_Conf_Date_y:
				getFragmentActivity().findViewById(R.id.txt_Q2C_Time_Met)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q2C_Time_Met)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.Q2C_Conf_Date_n:
				getFragmentActivity().findViewById(R.id.txt_Q2C_Time_Met)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q2C_Time_Met)
				.setVisibility(View.GONE);
				break;
			case R.id.Q3C_9_Plas_Cart_y:
				getFragmentActivity().findViewById(R.id.txt_Q3C_9a_Spec_Pac)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_9a_Spec_Pac)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.txt_Q3C_9b_Mat_For)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Pro_TV)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Bike)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Pai)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Oth)
				.setVisibility(View.VISIBLE);
				LayoutParams lp = (RelativeLayout.LayoutParams) getFragmentActivity()
						.findViewById(R.id.Q3C_9b_Oth).getLayoutParams();
				lp.topMargin = ProviderServiceSecondFragment.SHORT_DISTANCE;
				getFragmentActivity().findViewById(R.id.Q3C_9b_Oth).setLayoutParams(lp);
				break;
			case R.id.Q3C_9_Plas_Cart_n:
				getFragmentActivity().findViewById(R.id.txt_Q3C_9a_Spec_Pac)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_9a_Spec_Pac)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.txt_Q3C_9b_Mat_For)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Pro_TV)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Bike)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Pai)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_9b_Oth)
				.setVisibility(View.GONE);
				lp = (RelativeLayout.LayoutParams) getFragmentActivity()
						.findViewById(R.id.Q3C_9b_Oth).getLayoutParams();
				lp.topMargin = ProviderServiceSecondFragment.LONG_DISTANCE;
				getFragmentActivity().findViewById(R.id.Q3C_9b_Oth).setLayoutParams(lp);
				break;
			case R.id.Q3C_11_ICR_Avail_y:
				getFragmentActivity().findViewById(R.id.txt_Q3C_11a_ICR_Complete)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_11a_ICR_Complete)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.Q3C_11_ICR_Avail_n:
				getFragmentActivity().findViewById(R.id.txt_Q3C_11a_ICR_Complete)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_11a_ICR_Complete)
				.setVisibility(View.GONE);
				break;
			case R.id.Q3C_12_Rem_Vehicle_y:
				getFragmentActivity().findViewById(R.id.txt_Q3C_12a_Tru_Pos)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Dri)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Dou)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Ver)
				.setVisibility(View.GONE);
				break;
			case R.id.Q3C_12_Rem_Vehicle_n:
				getFragmentActivity().findViewById(R.id.txt_Q3C_12a_Tru_Pos)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Dri)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Dou)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_12a_Tru_Ver)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.Q3C_15_OHS_Comp_y:
				getFragmentActivity().findViewById(R.id.txt_Q3C_15a_Issu)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_No_Gear)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Tru_Unl)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Uns_List)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Pac)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Tru_Loc)
				.setVisibility(View.GONE);
				break;
			case R.id.Q3C_15_OHS_Comp_n:
				getFragmentActivity().findViewById(R.id.txt_Q3C_15a_Issu)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_No_Gear)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Tru_Unl)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Uns_List)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Pac)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q3C_15a_Tru_Loc)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.P_Q4U_1_CARTON_KIT_REQD_y:
				getFragmentActivity().findViewById(R.id.txt_P_Q4U_1A_REVD_IN_TIMELY_MANNER)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.P_Q4U_1A_REVD_IN_TIMELY_MANNER)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.P_Q4U_1_CARTON_KIT_REQD_n:
				getFragmentActivity().findViewById(R.id.txt_P_Q4U_1A_REVD_IN_TIMELY_MANNER)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.P_Q4U_1A_REVD_IN_TIMELY_MANNER)
				.setVisibility(View.GONE);
				break;
			case R.id.P_Q4U_7_PROV_CHANGES_MEM_INV_y:
				getFragmentActivity().findViewById(R.id.txt_P_Q4U_7_PROV_CHANGES_MEM_INVa)
				.setVisibility(View.GONE);
				break;
			case R.id.P_Q4U_7_PROV_CHANGES_MEM_INV_n:
				getFragmentActivity().findViewById(R.id.txt_P_Q4U_7_PROV_CHANGES_MEM_INVa)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.Q6D_3_LOL_y:
				getFragmentActivity().findViewById(R.id.txt_Q6D_3a_LOL)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.txt_Q6D_3a_LOL_2)
				.setVisibility(View.VISIBLE);
				getFragmentActivity().findViewById(R.id.Q6D_3a_LOL)
				.setVisibility(View.VISIBLE);
				break;
			case R.id.Q6D_3_LOL_n:
				getFragmentActivity().findViewById(R.id.txt_Q6D_3a_LOL)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.txt_Q6D_3a_LOL_2)
				.setVisibility(View.GONE);
				getFragmentActivity().findViewById(R.id.Q6D_3a_LOL)
				.setVisibility(View.GONE);
				break;
//			case R.id.Member_Agent_Signature_Member:
//				removeSignature(UploadInDTO.Member);
//				break;
//			case R.id.Member_Agent_Signature_Agent:
//				removeSignature(UploadInDTO.Agent);
//				break;

			}
		}
	};

	protected CompoundButton.OnCheckedChangeListener mCheckBoxValidator = 
			new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			startTimeStamp();
			
			String val = arg1 ? UploadInDTO.YES : UploadInDTO.NO;
			switch(arg0.getId()) {
			case R.id.Q3C_9b_Pro_TV:
				insertAnswer(UploadInDTO.P_Q3C_9B_PLASMA_REAR_PROJ, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9b_Bike:
				insertAnswer(UploadInDTO.P_Q3C_9B_BIKE, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9b_Pai:
				insertAnswer(UploadInDTO.P_Q3C_9B_PAINTING, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_9b_Oth:
				insertAnswer(UploadInDTO.P_Q3C_9B_OTHER, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_12a_Tru_Dri:
				insertAnswer(UploadInDTO.P_Q3C_12A_TRUCK_IN_DRIVEWAY, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_12a_Tru_Dou:
				insertAnswer(UploadInDTO.P_Q3C_12A_TRUCK_DBL_PARKED, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_12a_Tru_Ver:
				insertAnswer(UploadInDTO.P_Q3C_12A_TRUCK_ON_VERGE, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15a_No_Gear:
				insertAnswer(UploadInDTO.P_Q3C_15A_NO_SAFETY_GEAR, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15a_Tru_Unl:
				insertAnswer(UploadInDTO.P_Q3C_15A_TRUCK_LOAD, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15a_Uns_List:
				insertAnswer(UploadInDTO.P_Q3C_15A_UNSAFE_LIFT_CARRY, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15a_Pac:
				insertAnswer(UploadInDTO.P_Q3C_15A_PACKING, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_15a_Tru_Loc:
				insertAnswer(UploadInDTO.P_Q3C_15A_TRUCK_LOCATION, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;	
			case R.id.Q7_Remov_Comm:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_REMLST_COMMS, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_7_REMLST_COMMS, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;
			case R.id.Q7_Over_Impress:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_OVERALL_IMPRESSION, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_7_OVERALL_IMPRESSION, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;
			case R.id.Q7_Treat_Spec_Items:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_TREAT_ITEMS, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_7_TREAT_SPEC_ITEMS, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;
			case R.id.Q7_Crew_Insuff:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_NO_CREW_INSUFF, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_7_NO_OF_CREW_INSUFF, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;
			case R.id.Q7_ICR_Compl:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_ICR_COMPLETED, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_7_ICR_COMPLETED, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;	
			}			
		}
	};

	protected OnItemSelectedListener mSpinnerValidator = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			if (arg2 != 0) startTimeStamp();
			
			String val = null;
			switch (arg2) {
			case 0:
				val = null;
				break;
			case 1:
				val = UploadInDTO.EXCELLENT;
				break;
			case 2:
				val = UploadInDTO.GOOD;
				break;
			case 3:
				val = UploadInDTO.AVERAGE;
				break;
			case 4:
				val = UploadInDTO.POOR;
				break;
			case 5:
				val = UploadInDTO.VERY_POOR;
				break;
			}

			switch (arg0.getId()) {
			case R.id.Q3C_1_Pres_Crew:
				insertAnswer(UploadInDTO.P_Q3C_1_PRESENTATION_CREW, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_2_Skill_Crew:
				insertAnswer(UploadInDTO.P_Q3C_2_CUST_SERVICE_SKILL, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_3_Cond_Veh:
				insertAnswer(UploadInDTO.P_Q3C_3_COND_OF_VEHICLE, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_4_Pac_Mat:
				insertAnswer(UploadInDTO.P_Q3C_4_COND_OF_PACKAGING, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q3C_5_Sta_Con:
				insertAnswer(UploadInDTO.P_Q3C_5_STANDING_CONTAINER, 
						val, CIFragmentActivity.PROVIDER_SERVICE);
				break;
			case R.id.Q7_Prov_Rat:
				if (ModelFactory.getCIService().getCurrentForm()
						.isDelivery()) {
					insertAnswer(UploadInDTO.P_Q6D_4_RATING, 
							val, CIFragmentActivity.MEMBER_RATING);
				} else if (ModelFactory.getCIService().getCurrentForm()
						.isUplift()) {
					insertAnswer(UploadInDTO.P_Q4U_8_RATING, 
							val, CIFragmentActivity.MEMBER_RATING);
				}
				break;
			case R.id.Q8_Toll_Tran:
				insertAnswer(UploadInDTO.P_Q8_1_RATING, 
						val, CIFragmentActivity.TOLL_RATING);
				break;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}

	};

	protected TextWatcher mTimeAttendingTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			insertAnswer(UploadInDTO.P_Q1_TIME_ATTENDING, arg0.toString(),
					CIFragmentActivity.SERVICE_DETAIL);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected TextWatcher mVolumeEditTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			if (ModelFactory.getCIService().getCurrentForm()
					.isDelivery()) {
				insertAnswer(UploadInDTO.P_Q7D_VOLUME, arg0.toString(),
						CIFragmentActivity.VOLUME);
			} else if (ModelFactory.getCIService().getCurrentForm()
					.isUplift()) {
				insertAnswer(UploadInDTO.P_Q5U_VOLUME, arg0.toString(),
						CIFragmentActivity.VOLUME);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected TextWatcher mCommentEditTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			insertAnswer(UploadInDTO.P_Q9_COMM, arg0.toString(),
					CIFragmentActivity.VOLUME);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected TextWatcher mTimeOnSiteEditTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			insertAnswer(UploadInDTO.P_Q8_2_TIME_ON_SITE, arg0.toString(),
					CIFragmentActivity.TOLL_RATING);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected TextWatcher mNumOfCrewEditTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			insertAnswer(UploadInDTO.P_Q3C_NO_OF_CREW, arg0.toString(),
					CIFragmentActivity.PROVIDER_SERVICE);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected TextWatcher mLOLEditTextValidator = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			startTimeStamp();
			insertAnswer(UploadInDTO.P_Q7D_3A_VOLUME, arg0.toString(),
					CIFragmentActivity.GENERIC);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	protected OnClickListener mNextListener = new OnClickListener() {
		public void onClick(View v) {
			getFragmentActivity().showNextProviderService();
		}
	};

	protected OnClickListener mPrevListener = new OnClickListener() {
		public void onClick(View v) {
			getFragmentActivity().showPrevProviderService();
		}
	};
}
