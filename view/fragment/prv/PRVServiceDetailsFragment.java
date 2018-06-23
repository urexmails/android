package com.contactpoint.view.fragment.prv;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.view.adapter.prv.PRVCartonKitsAdapter;
import com.contactpoint.view.adapter.prv.PRVServiceAdapter;

public class PRVServiceDetailsFragment extends SherlockFragment {

	public static final String TAG = "PRVServiceDetailsFragment";
	
	
	private TextView mPONum;
	private TextView mOrderNum;
	private TextView mStartDate;
	private TextView mEndDate;
	private TextView mCaseNum;
	private TextView mCaseManager;
	private TextView mBookDate;
	private TextView mBookTime;
	
	private LinearLayout mContainer;
	
	/*
	private TableRow mCartonHeader1;
	private TableRow mCartonHeader2;
	
	private TextView mServiceType1;
	private TextView mServiceType2;
	private TextView mOriVol1;
	private TextView mOriVol2;
	
	private EditText mStartNote1;
	private EditText mStartNote2;
	private EditText mEndNote1;
	private EditText mEndNote2;
	
	private PRVCartonKitsAdapter mCartonAdapter1;
	private PRVCartonKitsAdapter mCartonAdapter2;
	private PRVServiceAdapter mServiceAdapter1;
	private PRVServiceAdapter mServiceAdapter2;
	private TableLayout mCartonTable1;
	private TableLayout mCartonTable2;
	private TableLayout mServiceTable1;
	private TableLayout mServiceTable2;*/

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = (RelativeLayout)inflater.inflate(R.layout.prv_service_detail, null);
		PRVDownload outDTO = ModelFactory.getPRVService().getCurrentForm().getDownload().get(0);

		mPONum = (TextView)v.findViewById(R.id.prv_po_num_txt);
		mOrderNum = (TextView)v.findViewById(R.id.prv_order_num_txt);
		mStartDate = (TextView)v.findViewById(R.id.prv_start_date_txt);
		mEndDate = (TextView)v.findViewById(R.id.prv_end_date_txt);
		mCaseNum = (TextView)v.findViewById(R.id.prv_case_num_txt);
		mCaseManager = (TextView)v.findViewById(R.id.prv_case_manager_txt);
		mBookDate = (TextView)v.findViewById(R.id.prv_book_date_txt);
		mBookTime = (TextView)v.findViewById(R.id.prv_book_time_txt);
		mContainer = (LinearLayout)v.findViewById(R.id.prv_service_container);
		
		mPONum.setText(ModelFactory.getPRVService().getCombinedPONumber(outDTO));
		mOrderNum.setText(outDTO.moveNumber);
		mStartDate.setText(outDTO.PRVStartDate);
		mEndDate.setText(outDTO.PRVEndDate);
		mCaseNum.setText(outDTO.caseNumber);
		mCaseManager.setText(outDTO.CaseManagerName);
		
		String [] bookDateTime = outDTO.bookedDateTime.split(" ");
		mBookDate.setText(bookDateTime[0]);
		mBookTime.setText(bookDateTime[1]);
		
		View form;
		
		for (PRVDownload download : ModelFactory.getPRVService().getCurrentForm().getDownload()) {
			form = inflater.inflate(R.layout.prv_services_multiform_template, null);
			
			final TableLayout cartonTable = (TableLayout)form.findViewById(R.id.prv_service_carton_tbl);
			final TableLayout serviceTable = (TableLayout)form.findViewById(R.id.prv_service_detail_tbl);
			final View cartonHeader = inflater.inflate(R.layout.prv_service_tbl_header, null);
			
			final PRVCartonKitsAdapter cartonAdapter = new PRVCartonKitsAdapter(getSherlockActivity(), download.cartonKits);
			cartonAdapter.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					cartonTable.removeAllViews();
					cartonTable.addView(cartonHeader);
					for (int i = 0; i < cartonAdapter.getCount(); i++) {
						cartonTable.addView(cartonAdapter.getView(i, null, cartonTable));
					}
				}
			});
			
			final PRVServiceAdapter serviceAdapter = new PRVServiceAdapter(getSherlockActivity(), download.serviceDetails);
			serviceAdapter.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					serviceTable.removeAllViews();
					for (int i = 0; i < serviceAdapter.getCount(); i++) {
						serviceTable.addView(serviceAdapter.getView(i, null, serviceTable));
					}
				}
			});
			
			cartonAdapter.notifyDataSetChanged();
			serviceAdapter.notifyDataSetChanged();

			((TextView)form.findViewById(R.id.prv_ori_vol_txt)).setText(download.volume);
			((TextView)form.findViewById(R.id.prv_door_to_door)).setText(download.ServiceType);
			((EditText)form.findViewById(R.id.prv_start_note_txt)).setText(download.StartNote);
			((EditText)form.findViewById(R.id.prv_end_note_txt)).setText(download.EndNote);
			
			mContainer.addView(form);
		}

		/*
		// form 1
		View form = inflater.inflate(R.layout.prv_services_multiform_template, null);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.BELOW, R.id.prv_end_date);
		form.setLayoutParams(lp);
		
		mCartonTable1 = (TableLayout)form.findViewById(R.id.prv_service_carton_tbl);
		mServiceTable1 = (TableLayout)form.findViewById(R.id.prv_service_detail_tbl);
		
		mCartonAdapter1 = new PRVCartonKitsAdapter(getSherlockActivity(), outDTO.cartonKits);
		mCartonAdapter1.registerDataSetObserver(mCartonDataSetObserver1);
		mServiceAdapter1 = new PRVServiceAdapter(getSherlockActivity(), outDTO.serviceDetails);
		mServiceAdapter1.registerDataSetObserver(mServiceDataSetObserver1);

		mCartonAdapter1.notifyDataSetChanged();
		mServiceAdapter1.notifyDataSetChanged();
		
		mOriVol1 = (TextView)form.findViewById(R.id.prv_ori_vol_txt);
		mServiceType1 = (TextView)form.findViewById(R.id.prv_door_to_door);
		mStartNote1 = (EditText)form.findViewById(R.id.prv_start_note_txt);
		mEndNote1 = (EditText)form.findViewById(R.id.prv_end_note_txt);
				
		mServiceType1.setText(outDTO.ServiceType);
		mOriVol1.setText(outDTO.volume);
		mStartNote1.setText(outDTO.StartNote);
		mEndNote1.setText(outDTO.EndNote);
		
		mContainer.addView(form);
		
		// form 2
		try {
			outDTO = ModelFactory.getPRVService().getCurrentForm().getDownload().get(1); 
			View form2 = inflater.inflate(R.layout.prv_services_multiform_template, null);
			lp = new RelativeLayout.LayoutParams(
			        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.BELOW, R.id.prv_end_date);
			lp.setMargins(0, 20 + form.getHeight(), 0, 0);
			form2.setLayoutParams(lp);
			
			mCartonTable2 = (TableLayout)form2.findViewById(R.id.prv_service_carton_tbl);
			mServiceTable2 = (TableLayout)form2.findViewById(R.id.prv_service_detail_tbl);
			
			mCartonAdapter2 = new PRVCartonKitsAdapter(getSherlockActivity(), outDTO.cartonKits);
			mCartonAdapter2.registerDataSetObserver(mCartonDataSetObserver2);
			mServiceAdapter2 = new PRVServiceAdapter(getSherlockActivity(), outDTO.serviceDetails);
			mServiceAdapter2.registerDataSetObserver(mServiceDataSetObserver2);

			mCartonAdapter2.notifyDataSetChanged();
			mServiceAdapter2.notifyDataSetChanged();
			
			mOriVol2 = (TextView)form.findViewById(R.id.prv_ori_vol_txt);
			mServiceType2 = (TextView)form.findViewById(R.id.prv_door_to_door);
			mStartNote2 = (EditText)form.findViewById(R.id.prv_start_note_txt);
			mEndNote2 = (EditText)form.findViewById(R.id.prv_end_note_txt);
					
			mServiceType2.setText(outDTO.ServiceType);
			mOriVol2.setText(outDTO.volume);
			mStartNote2.setText(outDTO.StartNote);
			mEndNote2.setText(outDTO.EndNote);
			
			mContainer.addView(form2);
		} catch (ArrayIndexOutOfBoundsException ex) {
			
		}
		
		//mStartNote.addTextChangedListener(mUpliftNote);
		//mEndNote.addTextChangedListener(mDeliveryNote);*/

		v.invalidate();
		
		return v;
	}
	
	/*
	private DataSetObserver mCartonDataSetObserver1 = new DataSetObserver() {
		@Override
		public void onChanged() {
			mCartonTable1.removeAllViews();
			mCartonTable1.addView(mCartonHeader1);
			for (int i = 0; i < mCartonAdapter1.getCount(); i++) {
				mCartonTable1.addView(mCartonAdapter1.getView(i, null, mCartonTable1));
			}
		}
	};
	
	private DataSetObserver mCartonDataSetObserver2 = new DataSetObserver() {
		@Override
		public void onChanged() {
			mCartonTable2.removeAllViews();
			mCartonTable2.addView(mCartonHeader2);
			for (int i = 0; i < mCartonAdapter2.getCount(); i++) {
				mCartonTable2.addView(mCartonAdapter2.getView(i, null, mCartonTable2));
			}
		}
	};
	
	private DataSetObserver mServiceDataSetObserver1 = new DataSetObserver() {
		@Override
		public void onChanged() {
			mServiceTable1.removeAllViews();
			for (int i = 0; i < mServiceAdapter1.getCount(); i++) {
				mServiceTable1.addView(mServiceAdapter1.getView(i, null, mServiceTable1));
			}
		}
	};
	
	private DataSetObserver mServiceDataSetObserver2 = new DataSetObserver() {
		@Override
		public void onChanged() {
			mServiceTable2.removeAllViews();
			for (int i = 0; i < mServiceAdapter2.getCount(); i++) {
				mServiceTable2.addView(mServiceAdapter2.getView(i, null, mServiceTable2));
			}
		}
	};*/
	
	/* not required, both textviews are uneditable
	private TextWatcher mUpliftNote = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			ModelFactory.getPRVService().putQuestionAnswer(
					getSherlockActivity(),
					UploadPRVInDTO.P_UPLIFT_NOTES, 
					arg0.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};
	
	private TextWatcher mDeliveryNote = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			ModelFactory.getPRVService().putQuestionAnswer(
					getSherlockActivity(),
					UploadPRVInDTO.P_DELIVERY_NOTES, 
					arg0.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};*/
}
