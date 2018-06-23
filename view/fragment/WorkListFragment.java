package com.contactpoint.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.DeletePRVInDTO;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.service.PhotoService;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.model.util.Exchanger;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.view.adapter.ci.CIDeliveryWorklistAdapter;
import com.contactpoint.view.adapter.ci.CIUpliftWorklistAdapter;
import com.contactpoint.view.adapter.prv.PRVWorklistAdapter;

public class WorkListFragment extends NetworkFragment {
	public static final String TAG = "WorkListFragment";

	private CIUpliftWorklistAdapter mUpliftAdapter;
	private CIDeliveryWorklistAdapter mDeliveryAdapter;
	private PRVWorklistAdapter mPRVAdapter;

	private TableLayout mUpliftsTable;
	private TableLayout mDeliveriesTable;
	private TableLayout mPRVsTable;
	private TableRow mUpliftHeader;
	private TableRow mDeliveryHeader;
	private TableRow mPRVsHeader;
	private TabHost mTabHost;

	private Button mBackBtn;
	private Button mUploadBtn;
	private Button mDeleteBtn;
	private Button mOrderBtn;

	private AlertDialog mIncompleteDialog;
	private MyLocation mLocationTracker;
//	private EditText mIncompleteText;

	public static final int UPLIFT_TARGET_DELETE = 11;
	public static final int DELIVERY_TARGET_DELETE = 10;
	public static final int PRV_TARGET_DELETE = 202;
	public static final int UPLIFT_TARGET_UPLOAD = 203;
	public static final int DELIVERY_TARGET_UPLOAD = 204;
	public static final int PRV_TARGET_UPLOAD = 205;
	public static final int PRV_REDOWNLOAD_CANCELLED = 206;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);

		mUpliftAdapter = new CIUpliftWorklistAdapter(getSherlockActivity());
		mDeliveryAdapter = new CIDeliveryWorklistAdapter(getSherlockActivity());
		mPRVAdapter = new PRVWorklistAdapter(this);

		mIncompleteDialog = CommonController.dialogFromBuilder(getSherlockActivity(), 
				R.layout.prv_dialog_incomplete, android.R.style.Theme_Holo_Light_Dialog);
		mIncompleteDialog.setTitle(getResources().getString(R.string.prv_incomplete_title));
		mIncompleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_confirm), mIncompleteListener);
		mIncompleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), mIncompleteListener);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.work_list, null);

		mBackBtn = (Button) v.findViewById(R.id.btn_back);
		mUploadBtn = (Button) v.findViewById(R.id.btn_upload); 
		mDeleteBtn = (Button) v.findViewById(R.id.btn_delete); 
		mOrderBtn = (Button) v.findViewById(R.id.btn_save_order);

		mBackBtn.setOnClickListener(mBackToDashboardListener);
		mUploadBtn.setOnClickListener(mUploadListener);
		mDeleteBtn.setOnClickListener(mDeleteListener);
		mOrderBtn.setOnClickListener(mOrderListener);

		// tabs
		mTabHost = (TabHost) v.findViewById(R.id.tabhost);
		mTabHost.setup();

		TabHost.TabSpec spec1 = mTabHost.newTabSpec(getResources().getString(R.string.uplift));
		spec1.setContent(R.id.Uplift);
		spec1.setIndicator(getResources().getString(R.string.uplift));
		mTabHost.addTab(spec1);

		TabHost.TabSpec spec2 = mTabHost.newTabSpec(getResources().getString(R.string.deliveries));
		spec2.setContent(R.id.Deliveries);
		spec2.setIndicator(getResources().getString(R.string.deliveries));
		mTabHost.addTab(spec2);

		TabHost.TabSpec spec3 = mTabHost.newTabSpec(getResources().getString(R.string.prvs));
		spec3.setContent(R.id.PRVs);
		spec3.setIndicator(getResources().getString(R.string.prvs));
		mTabHost.addTab(spec3);

		mTabHost.setCurrentTab(0);

		mTabHost.setOnTabChangedListener(mTabChangeListener);
		
		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(mTabClickedListener);
		}

		mUpliftsTable = (TableLayout)v.findViewById(R.id.Uplift);
		mDeliveriesTable = (TableLayout)v.findViewById(R.id.Deliveries);
		mPRVsTable = (TableLayout)v.findViewById(R.id.PRVs);

		// add header
		mUpliftHeader = (TableRow) inflater.inflate(R.layout.work_list_tbl_header, null, false);
		mUpliftHeader.findViewById(R.id.worklist_header_name).setOnClickListener(mColumnHeaderClickListener);
		mUpliftHeader.findViewById(R.id.worklist_header_move_date).setOnClickListener(mColumnHeaderClickListener);
		mUpliftHeader.findViewById(R.id.worklist_header_suburb).setOnClickListener(mColumnHeaderClickListener);
		mUpliftHeader.findViewById(R.id.worklist_header_zone).setOnClickListener(mColumnHeaderClickListener);
		mUpliftHeader.findViewById(R.id.worklist_header_volume).setOnClickListener(mColumnHeaderClickListener);
		mUpliftHeader.findViewById(R.id.worklist_header_status).setOnClickListener(mColumnHeaderClickListener);
		mUpliftsTable.setTag(mUpliftHeader);

		mDeliveryHeader = (TableRow) inflater.inflate(R.layout.work_list_tbl_header, null, false);
		mDeliveryHeader.findViewById(R.id.worklist_header_name).setOnClickListener(mColumnHeaderClickListener);
		mDeliveryHeader.findViewById(R.id.worklist_header_move_date).setOnClickListener(mColumnHeaderClickListener);
		mDeliveryHeader.findViewById(R.id.worklist_header_suburb).setOnClickListener(mColumnHeaderClickListener);
		mDeliveryHeader.findViewById(R.id.worklist_header_zone).setOnClickListener(mColumnHeaderClickListener);
		mDeliveryHeader.findViewById(R.id.worklist_header_volume).setOnClickListener(mColumnHeaderClickListener);
		mDeliveryHeader.findViewById(R.id.worklist_header_status).setOnClickListener(mColumnHeaderClickListener);
		mDeliveriesTable.setTag(mDeliveryHeader);

		mPRVsHeader = (TableRow) inflater.inflate(R.layout.work_list_prv_tbl_header, null, false);
		mPRVsHeader.findViewById(R.id.worklist_header_order_num).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_name).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_po_num).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_suburb).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_start).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_end).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsHeader.findViewById(R.id.worklist_header_book_date).setOnClickListener(mPRVColumnHeaderClickListener);
		mPRVsTable.setTag(mPRVsHeader);

		return v;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			CommonController.refreshTable(getSherlockActivity(), mUpliftsTable, 
					R.layout.work_list_tbl_header, mUpliftAdapter);
			CommonController.refreshTable(getSherlockActivity(), mDeliveriesTable, 
					R.layout.work_list_tbl_header, mDeliveryAdapter);

			mPRVAdapter.dataSetChanged();
			CommonController.refreshTable(getSherlockActivity(), mPRVsTable, 
					R.layout.work_list_prv_tbl_header, mPRVAdapter);

			if (getArguments() != null && 
					getArguments().containsKey(MainFragmentActivity.EXTRA_UPLOAD)) {
				onUploadEventTriggered();
				getArguments().remove(MainFragmentActivity.EXTRA_UPLOAD);
			}
			
			Exchanger.mMapView = null;
		}
	}

	private OnClickListener mBackToDashboardListener = new OnClickListener() {
		public void onClick(View v) {
			CommonController.backToDashboard(getSherlockActivity());
		}
	};

	private OnClickListener mUploadListener = new OnClickListener() {
		public void onClick(View v) {
			onUploadEventTriggered();
		}
	};

	private OnClickListener mDeleteListener = new OnClickListener() {
		public void onClick(View v) {
			// do nothing if there is nothing in the delete queue
			if (ModelFactory.getCIService().getDeleteQueue().size() <= 0) {
				return;
			}

			ModelFactory.getCIService().prepareDelete();
			ModelFactory.getCIService().setNetworkListener(WorkListFragment.this);
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};

	public final View.OnClickListener PRV_DELETE_LISTENER = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ModelFactory.getPRVService().setCurrentForm((PRVForm)v.getTag());
			ModelFactory.getPRVService().preparePRVDelete();
			ModelFactory.getPRVService().setNetworkListener(WorkListFragment.this);
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};

	public final View.OnClickListener BOOK_LISTENER = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// visual feedback
			if (v instanceof TextView) {
				v.setBackgroundColor(getResources().getColor(R.color.abs__bright_foreground_holo_light));
			}
			
			ModelFactory.getPRVService().setCurrentForm((PRVForm)v.getTag());
			((MainFragmentActivity)getActivity()).onNaviSelected(MainFragmentActivity.BOOK);
		}
	};

	public final View.OnClickListener INCOMPLETE_LISTENER = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			ModelFactory.getPRVService().setCurrentForm((PRVForm)v.getTag());
			mIncompleteDialog.show();
		}
	};

	public final View.OnClickListener PRV_FORM_LISTENER = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			v.setBackgroundColor(getResources().getColor(R.color.abs__bright_foreground_holo_light));

			PRVForm form = (PRVForm)v.getTag();
			ModelFactory.getPRVService().setCurrentForm(form);
			//ModelFactory.getPRVService().preparePRVDownload(form);
			ModelFactory.getPRVService().preparePRVRelease();
			ModelFactory.getPRVService().setNetworkListener(WorkListFragment.this);
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				ModelFactory.getPRVService().initPRVInDTO();
				Intent i = new Intent(getSherlockActivity(), PRVFragmentActivity.class);
				startActivity(i);
			}
		}
	};

	private DialogInterface.OnClickListener mIncompleteListener = 
			new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			EditText incompleteText = (EditText)mIncompleteDialog.findViewById(R.id.prv_incomplete_reason);
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				if (incompleteText.getText().length() != 0) {
					ModelFactory.getPRVService().getIncompletePRVInDTO().setReason(
							incompleteText.getText().toString());
					incompleteText.setText("");
					ModelFactory.getPRVService().preparePRVIncomplete();
					ModelFactory.getPRVService().setNetworkListener(WorkListFragment.this);
					int result = ModelFactory.getPRVService().executeSoapRequest();
					if (result == NetworkListener.ERR_OFFLINE) {
						showErrorDialog(getString(R.string.network_not_connected));
					}
				} else {
					AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
					ad.setTitle(getString(R.string.dialog_error));
					ad.setMessage(getString(R.string.incomplete_invalid));
					ad.show();
				}
				break;
			}
			
			// hide soft keyboard
			if (incompleteText.isFocused()) {
				InputMethodManager imm = (InputMethodManager)getSherlockActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(incompleteText.getWindowToken(), 0);
			}
		}
	};
	
	private View.OnClickListener mOrderListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Spinner order = null;
			for (int i = 1; i < mPRVsTable.getChildCount(); i++) {
				order = (Spinner)mPRVsTable.getChildAt(i)
						.findViewById(R.id.worklist_prv_order_num);
				
				if (order == null) continue;
				
				((PRVForm)mPRVAdapter.getItem(i - 1)).setOrder(order.getSelectedItemPosition());
				ModelFactory.getPRVService().saveFormData(getSherlockActivity());
			}
		}
	};

	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case UPLIFT_TARGET_DELETE:
			CommonController.refreshTable(getSherlockActivity(), mUpliftsTable, 
					R.layout.work_list_tbl_header, mUpliftAdapter);
			afterDelete();
			break;
		case DELIVERY_TARGET_DELETE:
			CommonController.refreshTable(getSherlockActivity(), mDeliveriesTable, 
					R.layout.work_list_tbl_header, mDeliveryAdapter);
			afterDelete();
			break;
		case PRV_TARGET_DELETE:
		case PRVService.PRV_INCOMPLETE:
		case PRVService.PRV_CANCELLED:
			String response = ModelFactory.getPRVService().getResponseMessage();
			if (param == PRVService.PRV_CANCELLED) {
				response = PRVService.AUTO_CANCEL_REASON;
			}
			showResult(response);
			if (response.compareTo(DeletePRVInDTO.DELETE_FAILED) != 0) {				
				mPRVAdapter.dataSetChanged();
				CommonController.refreshTable(getSherlockActivity(), mPRVsTable, 
						R.layout.work_list_prv_tbl_header, mPRVAdapter);
				ModelFactory.getPRVService().saveFormData(getSherlockActivity());
			}
			break;
		case UPLIFT_TARGET_UPLOAD:
			CommonController.refreshTable(getSherlockActivity(), mUpliftsTable, 
					R.layout.work_list_tbl_header, mUpliftAdapter);
			afterUpload();
			break;
		case DELIVERY_TARGET_UPLOAD:
			CommonController.refreshTable(getSherlockActivity(), mDeliveriesTable, 
					R.layout.work_list_tbl_header, mDeliveryAdapter);
			afterUpload();
			break;
		case PRVService.PRV_UPLOAD:
			mPRVAdapter.dataSetChanged();
			CommonController.refreshTable(getSherlockActivity(), mPRVsTable, 
					R.layout.work_list_prv_tbl_header, mPRVAdapter);
			afterUploadPRV();
			break;
		case PRVService.PRV_REDOWNLOAD:
			ModelFactory.getPRVService().initPRVInDTO();
			Intent i = new Intent(getSherlockActivity(), PRVFragmentActivity.class);
			startActivity(i);
			break;
		case PRV_REDOWNLOAD_CANCELLED:
			ModelFactory.getPRVService().preparePRVCancelled();
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
			break;
		case PhotoService.PHOTO_UPLOAD:
			ModelFactory.getPhotoService().saveFormData(getSherlockActivity());
			if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
				ModelFactory.getPhotoService().preparePhotoUpload();
				result = ModelFactory.getPhotoService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				showResult(ModelFactory.getPhotoService().getFinalMessage());
				ModelFactory.getPhotoService().initialisePhotoUpload();
				ModelFactory.getPhotoService().saveFormData(getSherlockActivity());
				System.gc();
			}
			break;
		case GetCIService.ERROR:
			ModelFactory.getCIService().saveFormData(getSherlockActivity());
			showErrorDialog(ModelFactory.getCIService().getClientMessage());
			break;
		case PRVService.ERROR:
			ModelFactory.getPRVService().saveFormData(getSherlockActivity());
			showErrorDialog(ModelFactory.getPRVService().getClientMessage());
			break;
		case PhotoService.ERROR:
			ModelFactory.getPhotoService().saveFormData(getSherlockActivity());
			showErrorDialog(ModelFactory.getPhotoService().getClientMessage());
			break;
		case GetCIService.DELETE:
			// Getting a response other than "Delete Successful"
			showResult(ModelFactory.getCIService().getResponseMessage());
			break;
		}
	}

	private void afterDelete() {
		ModelFactory.getCIService().saveFormData(getSherlockActivity());

		if (ModelFactory.getCIService().getDeleteQueue().size() > 0) {
			// continue delete
			ModelFactory.getCIService().prepareDelete();
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		} else {
			// on delete finished
			showResult(ModelFactory.getCIService().getResponseMessage());
		}
	}

	private void afterUpload() {
		ModelFactory.getCIService().saveFormData(getSherlockActivity());
		if (ModelFactory.getCIService().getUploadQueue().size() > 0) {
			// continue upload
			showLoadingDialog(LOADING_MSG);
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(getSherlockActivity(), uploadedCILocationResult);

		} else  if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
			showLoadingDialog(LOADING_MSG);
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(getSherlockActivity(), uploadedPRVLocationResult);

		} else if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
			ModelFactory.getPhotoService().initialisePhotoUpload();
			ModelFactory.getPhotoService().preparePhotoUpload();
			int result = ModelFactory.getPhotoService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		} else {
			showResult(ModelFactory.getCIService().getResponseMessage());
			System.gc();
		}
	}

	private void afterUploadPRV() {
		ModelFactory.getPRVService().saveFormData(getSherlockActivity());
		if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
			// continue upload
			showLoadingDialog(LOADING_MSG);
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(getSherlockActivity(), uploadedPRVLocationResult);

		} else if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
			ModelFactory.getPhotoService().initialisePhotoUpload();
			ModelFactory.getPhotoService().preparePhotoUpload();
			int result = ModelFactory.getPhotoService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		} else {
			showResult(ModelFactory.getPRVService().getResponseMessage());
			System.gc();
		}
	}

	public void onUploadEventTriggered() {
				
		// populate upload queue
		ModelFactory.getCIService().populateUploadQueue();
		ModelFactory.getPRVService().populateUploadQueue(getSherlockActivity());

		// do nothing if there is nothing in the upload queue
		if (ModelFactory.getCIService().getUploadQueue().size() <= 0 &&
				ModelFactory.getPRVService().getUploadQueue().size() <= 0 &&
				!ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
			return;
		}

		ModelFactory.getPhotoService().setNetworkListener(this);
		if (ModelFactory.getCIService().getUploadQueue().size() > 0) {
			ModelFactory.getCIService().setNetworkListener(this);
			showLoadingDialog(LOADING_MSG);
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(getSherlockActivity(), uploadedCILocationResult);
			return;
		}

		if (ModelFactory.getPRVService().getUploadQueue().size() > 0) {
			ModelFactory.getPRVService().setNetworkListener(this);
			showLoadingDialog(LOADING_MSG);
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(getSherlockActivity(), uploadedPRVLocationResult);

			return;
		}
		
		if (ModelFactory.getPhotoService().hasPhotoToBeUploaded()) {
			ModelFactory.getPhotoService().initialisePhotoUpload();
			ModelFactory.getPhotoService().preparePhotoUpload();
			int result = ModelFactory.getPhotoService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	}

	private OnClickListener mColumnHeaderClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int criteria = Integer.parseInt(arg0.getTag().toString());
			ModelFactory.getCIService().sortForms(mTabHost.getCurrentTab(), criteria);

			switch (mTabHost.getCurrentTab()) {
			case 0:
				CommonController.refreshTable(getSherlockActivity(), mUpliftsTable, 
						R.layout.work_list_tbl_header, mUpliftAdapter);
				break;
			case 1:
				CommonController.refreshTable(getSherlockActivity(), mDeliveriesTable, 
						R.layout.work_list_tbl_header, mDeliveryAdapter);
				break;
			}
		}

	};

	private OnClickListener mPRVColumnHeaderClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int criteria = Integer.parseInt(arg0.getTag().toString());
			ModelFactory.getPRVService().sortForms(criteria);

			mPRVAdapter.dataSetChanged();
			CommonController.refreshTable(getSherlockActivity(), mPRVsTable, 
					R.layout.work_list_prv_tbl_header, mPRVAdapter);
		}

	};

	private OnTabChangeListener mTabChangeListener = new OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			if (tabId.compareTo(getResources().getString(R.string.prvs)) == 0) {
				mBackBtn.setVisibility(View.GONE);
				mDeleteBtn.setVisibility(View.GONE);
				mOrderBtn.setVisibility(View.VISIBLE);
			} else {
				mBackBtn.setVisibility(View.VISIBLE);
				mDeleteBtn.setVisibility(View.VISIBLE);
				mOrderBtn.setVisibility(View.GONE);
			}
		}

	};
	
	private OnTouchListener mTabClickedListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			refreshWorkListTable();
			return false;
		}
		
	};
	
	private LocationResult uploadedCILocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			onTaskComplete();
			ModelFactory.getCIService().prepareUpload();
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};
	
	private LocationResult uploadedPRVLocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			onTaskComplete();
			ModelFactory.getPRVService().preparePRVUpload();
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};
	
	private void refreshWorkListTable() {
		mPRVAdapter.dataSetChanged();
		CommonController.refreshTable(getSherlockActivity(), mPRVsTable, 
				R.layout.work_list_prv_tbl_header, mPRVAdapter);
		CommonController.refreshTable(getSherlockActivity(), mUpliftsTable, 
				R.layout.work_list_tbl_header, mUpliftAdapter);
		CommonController.refreshTable(getSherlockActivity(), mDeliveriesTable, 
				R.layout.work_list_tbl_header, mDeliveryAdapter);

	}
}
