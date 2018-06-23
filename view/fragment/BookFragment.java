package com.contactpoint.view.fragment;

import java.text.ParseException;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.BookPRVInDTO;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.service.UtilService;
import com.contactpoint.model.util.DialogFactory;

public class BookFragment extends NetworkFragment {

	public static final String TAG = "BookFragment";

	private TextView mPONum;
	private TextView mMoveNum;
	private TextView mEndDate;
	private TextView mPPDate;
	private TextView mName;
	private TextView mBusiness;
	private TextView mHome;
	private TextView mMobile;
	private TextView mEmail;
	private TextView mCode;
	private TextView mAddress;

	private EditText mBookDate;
	private EditText mBookTime;
	private View mParent;
	private OnDateSetListener mDateListener;
	private DatePickerDialogFragment mDatepicker;
	private OnTimeSetListener mTimeListener;
	private TimePickerDialogFragment mTimePicker;

	private AlertDialog mPastEndDialog;
	private Spinner mPastEndReasonSpinner;
	//private EditText mPastEndReasonDetailText;
	
	private AlertDialog mRebookDialog;
	private Spinner mRebookReasonSpinner;
	//private EditText mRebookReasonDetailText;
	
	private AlertDialog mContactDialog;
	private Spinner mContactSpinner;
	private EditText mContactDetailText;
	private EditText mContactDateText;
	private OnDateSetListener mContactDateListener;
	private DatePickerDialogFragment mContactDatePicker;
	
	private BookPRVInDTO inDTO;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);

		mDateListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mDatepicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mBookDate, dayOfMonth, monthOfYear, year);
			}
		};

		mContactDateListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mContactDatePicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mContactDateText, dayOfMonth, monthOfYear, year);
			}
		};
		
		mTimeListener = new OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mTimePicker.setTime(hourOfDay, minute);
				CommonController.updateDisplay(mBookTime, hourOfDay, minute);
			}
		};

		mPastEndDialog = CommonController.dialogFromBuilder(getSherlockActivity(), 
				R.layout.prv_dialog_rebooking, android.R.style.Theme_Holo_Light_Dialog);
		mPastEndDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_confirm), mPastEndListener);
		mPastEndDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), mPastEndListener);
		mPastEndDialog.setTitle(getResources().getString(R.string.prv_past_end_title));

		
		mRebookDialog = CommonController.dialogFromBuilder(getSherlockActivity(), 
				R.layout.prv_dialog_rebooking, android.R.style.Theme_Holo_Light_Dialog);
		mRebookDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_confirm), mRebookListener);
		mRebookDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), mRebookListener);
		mRebookDialog.setTitle(getResources().getString(R.string.prv_rebook_title));
		
		mContactDialog = CommonController.dialogFromBuilder(getSherlockActivity(), 
				R.layout.prv_dialog_booking_contact, android.R.style.Theme_Holo_Light_Dialog);
		mContactDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_confirm), mContactListener);
		mContactDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), mContactListener);
		mContactDialog.setTitle(getResources().getString(R.string.prv_book_contact_title));

		inDTO = ModelFactory.getPRVService().getBookPRVInDTO();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			PRVDownload target = ModelFactory.getPRVService().getCurrentForm().getDownload().get(0);	
			mPONum.setText(ModelFactory.getPRVService().getCombinedPONumber(target));
			mMoveNum.setText(target.moveNumber);
			mEndDate.setText(target.PRVEndDate);
			mPPDate.setText(ModelFactory.getPRVService().getCurrentForm().getPPDate());
			mName.setText(target.memberName.toString());
			mBusiness.setText(target.memberContact.phoneBusiness);
			mHome.setText(target.memberContact.phoneHome);
			mMobile.setText(target.memberContact.phoneMobile);
			mEmail.setText(target.memberContact.email);
			mCode.setText(target.employeeCode);
			mAddress.setText(target.memberAddress.toString());

			mBookDate.setText("");
			mBookTime.setText("");

			if (ModelFactory.getUtilService().isSensitive(target.sensitive)) {
				mParent.findViewById(R.id.txt_sensitive).setVisibility(View.VISIBLE);
			} else {
				mParent.findViewById(R.id.txt_sensitive).setVisibility(View.GONE);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
		mParent = inflater.inflate(R.layout.prv_book, null);

		mBookDate = (EditText) mParent.findViewById(R.id.prv_book_date_txt);
		mBookTime = (EditText) mParent.findViewById(R.id.prv_book_time_txt);
		mPONum = (TextView)mParent.findViewById(R.id.prv_po_num_txt);
		mMoveNum = (TextView)mParent.findViewById(R.id.prv_order_num_txt);
		mEndDate = (TextView)mParent.findViewById(R.id.prv_end_date_txt);
		mPPDate = (TextView)mParent.findViewById(R.id.prv_pp_date_txt);
		mName = (TextView)mParent.findViewById(R.id.prv_cust_name_txt);
		mBusiness = (TextView)mParent.findViewById(R.id.prv_bh_text);
		mHome = (TextView)mParent.findViewById(R.id.prv_ah_text);
		mMobile = (TextView)mParent.findViewById(R.id.prv_m_text);
		mEmail = (TextView)mParent.findViewById(R.id.prv_email_text);
		mCode = (TextView)mParent.findViewById(R.id.prv_emp_code_text);
		mAddress = (TextView)mParent.findViewById(R.id.prv_address_text);

		final Calendar c = Calendar.getInstance();
		int yyyy = c.get(Calendar.YEAR);
		int mm 	 = c.get(Calendar.MONTH);
		int dd 	 = c.get(Calendar.DAY_OF_MONTH);

		mDatepicker = new DatePickerDialogFragment(mDateListener, dd, mm, yyyy);

		mBookDate.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mDatepicker.show(getFragmentManager(), "dialog");
				return false;
			}
		});

		mTimePicker = new TimePickerDialogFragment(mTimeListener, 
				c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

		mBookTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mTimePicker.show(getFragmentManager(), "time_dialog");
				return false;
			}
		});
		
		mContactDatePicker = new DatePickerDialogFragment(mContactDateListener, dd, mm, yyyy);

		mParent.findViewById(R.id.prv_confirm_booking).setOnClickListener(mConfirmBookingListener);
		mParent.findViewById(R.id.prv_contact_booking).setOnClickListener(onContactClick);
		mParent.findViewById(R.id.prv_booking_back).setOnClickListener(mBackListener);

		return mParent;
	}

	private View.OnClickListener mConfirmBookingListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String bookDate = mBookDate.getText().toString();
			String bookTime = mBookTime.getText().toString();
			String ppDate = mPPDate.getText().toString();

			// validate book date and time
			if (bookDate.length() == 0 || bookTime.length() == 0) {
				AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
				ad.setTitle(getString(R.string.dialog_error));
				ad.setMessage(getString(R.string.booking_invalid));
				ad.show();
				return;
			} 
						
			try {
				if (ModelFactory.getUtilService().getDateFromString(bookDate, UtilService.DATE_FORMAT).compareTo(
						ModelFactory.getUtilService().getDateFromString(ppDate, UtilService.DATE_FORMAT)) > 0) {
					AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
					ad.setTitle(getString(R.string.dialog_error));
					ad.setMessage(getString(R.string.booking_date_invalid));
					ad.show();
					return;				
				}
			} catch (ParseException e) {
				AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
				ad.setTitle(getString(R.string.dialog_error));
				ad.setMessage(e.getMessage());
				ad.show();
				return;				
			}
			
			inDTO.setBookedDate(bookDate + " " + bookTime);
			inDTO.setContactFailureCode("");
			inDTO.setContactFailureDate("");
			inDTO.setContactFailureDetails("");
			inDTO.setPastEndDateReasonCode("");
			inDTO.setPastEndDateReasonDetails("");
			inDTO.setReBookingReasonCode("");
			inDTO.setReBookingReasonDetails("");
			doBooking();
		}
	};
	
	private View.OnClickListener onContactClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			showContactDialog();
		}
	};

	private void doBooking() {
		
		// rebook
		if (ModelFactory.getPRVService().getCurrentForm().isBooked() && inDTO.getReBookingReasonCode().length() == 0) {
			showRebookDialog();
			return;
		}

		
		// book past end date
		try {
			if (ModelFactory.getUtilService().getDateFromString(mBookDate.getText().toString(), UtilService.DATE_FORMAT).compareTo(
					ModelFactory.getUtilService().getDateFromString(mEndDate.getText().toString(), UtilService.DATE_FORMAT)) > 0  && 
				inDTO.getPastEndDateReasonCode().length() == 0) {
				showPastEndDialog();
				return;
			}
		} catch (ParseException e) {
			AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
			ad.setTitle(getString(R.string.dialog_error));
			ad.setMessage(e.getMessage());
			ad.show();
			return;				
		}

		ModelFactory.getPRVService().preparePRVBooking();
		ModelFactory.getPRVService().setNetworkListener(BookFragment.this);
		int result = ModelFactory.getPRVService().executeSoapRequest();
		if (result == NetworkListener.ERR_OFFLINE) {
			showErrorDialog(getString(R.string.network_not_connected));
		}

	}

	private DialogInterface.OnClickListener mRebookListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				ReferenceIDDropDown reason = (ReferenceIDDropDown)mRebookReasonSpinner.getSelectedItem();
				//String detail = mRebookReasonDetailText.getText().toString();
				
				inDTO.setReBookingReasonCode(reason.ID + "");
				//inDTO.setBookingReasonDetails(detail);
				
				doBooking();
				break;
			}
		}
	};

	private DialogInterface.OnClickListener mPastEndListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				ReferenceIDDropDown reason = (ReferenceIDDropDown)mPastEndReasonSpinner.getSelectedItem();
				//String detail = mPastEndReasonDetailText.getText().toString();
				
				inDTO.setPastEndDateReasonCode(reason.ID + "");
				//inDTO.setPastEndDateReasonDetails(detail);
				doBooking();
				break;
			}
		}
	};
	
	private DialogInterface.OnClickListener mContactListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				ReferenceIDDropDown reason = (ReferenceIDDropDown)mContactSpinner.getSelectedItem();
				String detail = mContactDetailText.getText().toString();
				String date = mContactDateText.getText().toString();
				
				inDTO.setContactFailureCode(reason.ID + "");
				inDTO.setContactFailureDetails(detail);
				inDTO.setContactFailureDate(date);
				
				ModelFactory.getPRVService().preparePRVBooking();
				ModelFactory.getPRVService().setNetworkListener(BookFragment.this);
				int result = ModelFactory.getPRVService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
				break;
			}

			// hide soft keyboard
			if (mContactDetailText.isFocused()) {
				InputMethodManager imm = (InputMethodManager)getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mContactDetailText.getWindowToken(), 0);
			}
		}
	};

	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch (param) {
		case PRVService.PRV_BOOKING:
			ModelFactory.getPRVService().saveFormData(getSherlockActivity());
			if (inDTO.getBookedDate() != null && inDTO.getBookedDate().length() > 0) {
				((MainFragmentActivity)getSherlockActivity()).onNaviSelected(MainFragmentActivity.WORKLIST);
			} else {
				AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
				ad.setMessage(ModelFactory.getPRVService().getResponseMessage());
				ad.show();
			}
			
			// reset DTO
			inDTO.setBookedDate("");
			inDTO.setPastEndDateReasonCode("");
			inDTO.setPastEndDateReasonDetails("");
			inDTO.setReBookingReasonCode("");
			inDTO.setReBookingReasonDetails("");
			inDTO.setContactFailureCode("");
			inDTO.setContactFailureDetails("");
			inDTO.setContactFailureDate("");

			break;
		case PRVService.ERROR:
			showErrorDialog(ModelFactory.getPRVService().getClientMessage());
			break;
		}
	}
	
	private void showRebookDialog() {
		mRebookDialog.show();
		
		/*if (mRebookReasonDetailText == null) {
			mRebookReasonDetailText = (EditText)mRebookDialog.findViewById(R.id.prv_incomplete_reason);
		}*/
		
		if (mRebookReasonSpinner == null) {
			mRebookReasonSpinner = (Spinner)mRebookDialog.findViewById(R.id.prv_rebook_reason);
		}
		
		CommonController.setSpinnerAdapter(getSherlockActivity(), mRebookReasonSpinner, 
				ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.REBOOK));
		/*
		mRebookReasonSpinner.setAdapter(new ArrayAdapter<ReferenceIDDropDown>(getSherlockActivity(),
					android.R.layout.simple_spinner_item,
					ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.REBOOK)));
		//mRebookReasonDetailText.setText(inDTO.getBookingReasonDetails());*/
	}
	
	private void showPastEndDialog() {
		mPastEndDialog.show();
		
		if (mPastEndReasonSpinner == null) {
			mPastEndReasonSpinner = (Spinner)mPastEndDialog.findViewById(R.id.prv_rebook_reason);
			//mPastEndReasonDetailText = (EditText)mPastEndDialog.findViewById(R.id.prv_rebook_reason_text);
		}
		
		CommonController.setSpinnerAdapter(getSherlockActivity(), mPastEndReasonSpinner, 
				ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.AFTER_END_DATE));
		//mPastEndReasonDetailText.setText(inDTO.getBookingReasonDetails());
		/*mPastEndReasonSpinner.setAdapter(new ArrayAdapter<ReferenceIDDropDown>(getSherlockActivity(),
					android.R.layout.simple_spinner_item,
					ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.AFTER_END_DATE)));*/
	}
	
	private void showContactDialog() {
		mContactDialog.show();
		
		if (mContactSpinner == null) {
			mContactSpinner = (Spinner)mContactDialog.findViewById(R.id.prv_booking_contact_reason);
			CommonController.setSpinnerAdapter(getSherlockActivity(), mContactSpinner, 
					ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.CONTACT_FAILURE));
			/*
			mContactSpinner.setAdapter(new ArrayAdapter<ReferenceIDDropDown>(getSherlockActivity(),
					android.R.layout.simple_spinner_item,
					ModelFactory.getPRVService().getBookingReasonCodes(ReferenceIDDropDown.CONTACT_FAILURE)));*/
			//mPastEndReasonDetailText = (EditText)mPastEndDialog.findViewById(R.id.prv_rebook_reason_text);
			mContactDetailText = (EditText)mContactDialog.findViewById(R.id.prv_contact_detail_text);
			mContactDateText = (EditText)mContactDialog.findViewById(R.id.prv_contact_date_text);
			mContactDateText.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					mContactDatePicker.show(getFragmentManager(), "contact_date_dialog");
					return false;
				}
			});

		}
		
		mContactDetailText.setText("");
		mContactDateText.setText(ModelFactory.getUtilService().getCurrentDateWithFormat(PRVService.DATE_FORMAT));
		
		//mPastEndReasonDetailText.setText(inDTO.getBookingReasonDetails());
	}
	
	private View.OnClickListener mBackListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			((MainFragmentActivity)getSherlockActivity()).onNaviSelected(MainFragmentActivity.WORKLIST);
		}
	};

}
