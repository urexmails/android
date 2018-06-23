package com.contactpoint.view.fragment.ci;

import java.util.Calendar;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.view.fragment.TimePickerDialogFragment;

public class ServiceDetailsFragment extends SuperCIFragment {

	public static final String TAG = "ServiceDetailsFragment";
	
	private TextView mQuestion_2C_2;
	private EditText mP_Q1_TIME_ATTENDING;
	private RadioGroup mQ1_Serv_Ord;
	private RadioGroup mQ2C_Conf_Date;
	private RadioGroup mQ2C_Time_Met;
	private OnTimeSetListener mTimeListener;
	private TimePickerDialogFragment mTimePicker;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);	
		mTimeListener = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mTimePicker.setTime(hourOfDay, minute);
				CommonController.updateDisplay(mP_Q1_TIME_ATTENDING, hourOfDay, minute);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_service_detail, null);

		((TextView)v.findViewById(R.id.txt_order_no_val)).setText(
				getCIDownloadOutDTO().moveNumber);
		((TextView)v.findViewById(R.id.txt_case_no_val)).setText(
				getCIDownloadOutDTO().caseNumber);
		((TextView)v.findViewById(R.id.txt_ori_vol_val)).setText(
				getCIDownloadOutDTO().volume);
		((TextView)v.findViewById(R.id.txt_supplier_code_val)).setText(
				getCIDownloadOutDTO().supplierCode);
		
		mQuestion_2C_2 		 = (TextView)   v.findViewById(R.id.txt_Q2C_Time_Met);
		mP_Q1_TIME_ATTENDING = (EditText) v.findViewById(R.id.P_Q1_TIME_ATTENDING);
		mQ1_Serv_Ord 		 = (RadioGroup) v.findViewById(R.id.Q1_Serv_Ord);
		mQ2C_Conf_Date 		 = (RadioGroup) v.findViewById(R.id.Q2C_Conf_Date);
		mQ2C_Time_Met 		 = (RadioGroup) v.findViewById(R.id.Q2C_Time_Met);

		mQuestion_2C_2.setVisibility(View.GONE);
		mQ2C_Time_Met.setVisibility(View.GONE);

		final Calendar c = Calendar.getInstance();
		mTimePicker = new TimePickerDialogFragment(mTimeListener, 
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

		mP_Q1_TIME_ATTENDING.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mTimePicker.show(getFragmentManager(), "time_dialog");
				return false;
			}
		});
		
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		// insert answers
		checkAnswered(UploadInDTO.P_Q1_TIME_ATTENDING, mP_Q1_TIME_ATTENDING);
		checkAnswered(UploadInDTO.P_Q1_SERVICE_ORDERED, mQ1_Serv_Ord);
		checkAnswered(UploadInDTO.P_Q2_PROVIDER_CONFIRM, mQ2C_Conf_Date);
		checkAnswered(UploadInDTO.P_Q2C_TIME_MET, mQ2C_Time_Met);
		
		// listeners
		mP_Q1_TIME_ATTENDING.addTextChangedListener(mTimeAttendingTextValidator);
		mQ1_Serv_Ord.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ2C_Conf_Date.setOnCheckedChangeListener(mRadioButtonValidator);
		mQ2C_Time_Met.setOnCheckedChangeListener(mRadioButtonValidator);
				
		getFragmentActivity().validateForm(CIFragmentActivity.SERVICE_DETAIL);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			// enable / disable based on signatures
			setEnabledFromSignature(mP_Q1_TIME_ATTENDING);
			setEnabledFromSignature(mQ1_Serv_Ord);
			setEnabledFromSignature(mQ2C_Conf_Date);
			setEnabledFromSignature(mQ2C_Time_Met);
		}
	}
}
