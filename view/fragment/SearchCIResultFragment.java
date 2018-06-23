package com.contactpoint.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.view.adapter.ci.CIDeliverySearchResultAdapter;
import com.contactpoint.view.adapter.ci.CIUpliftSearchResultAdapter;

public class SearchCIResultFragment extends NetworkFragment {

	public static final String TAG = "SearchCIResultFragment";
	private CIUpliftSearchResultAdapter mUpliftResultAdapter;
	private CIDeliverySearchResultAdapter mDeliveryResultAdapter;
	
	private TableLayout mUpliftResultTable;
	private TableLayout mDeliveryResultTable;
	
	private Button mDownloadButton;
	
	public static final int UPLIFT_DOWNLOAD = 0;
	public static final int DELIVERY_DOWNLOAD = 1;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
		
		mUpliftResultAdapter = new CIUpliftSearchResultAdapter(getSherlockActivity());
		mDeliveryResultAdapter = new CIDeliverySearchResultAdapter(getSherlockActivity());
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			CommonController.refreshTable(getSherlockActivity(), mUpliftResultTable, 
					R.layout.carrier_inspection_tbl_header, mUpliftResultAdapter);
			CommonController.refreshTable(getSherlockActivity(), mDeliveryResultTable, 
					R.layout.carrier_inspection_tbl_header, mDeliveryResultAdapter);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.carrier_inspection_result, null);
		
		mUpliftResultTable = (TableLayout) v.findViewById(R.id.tbl_uplift);
		mDeliveryResultTable = (TableLayout) v.findViewById(R.id.tbl_deliveries);
		mDownloadButton = (Button) v.findViewById(R.id.btn_download);
		
		v.findViewById(R.id.btn_back).setOnClickListener(mBackToSearchListener);
		mDownloadButton.setOnClickListener(mDownloadListener);
		return v;
	}

	private OnClickListener mBackToSearchListener = new OnClickListener() {
		public void onClick(View v) {
			((MainFragmentActivity)getActivity()).onNaviSelected(MainFragmentActivity.SEARCH);
		}
	};

	private OnClickListener mDownloadListener = new OnClickListener() {
		public void onClick(View v) {
			// do nothing if there is no result to download
			if (ModelFactory.getCIService().getDownloadQueue().size() <= 0) return;

			mDownloadButton.setEnabled(false);
			ModelFactory.getCIService().prepareDownload();
			ModelFactory.getCIService().setNetworkListener(SearchCIResultFragment.this);
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};

	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case UPLIFT_DOWNLOAD:
			// refresh table
			CommonController.refreshTable(getSherlockActivity(), mUpliftResultTable,
					R.layout.carrier_inspection_tbl_header, mUpliftResultAdapter);
			afterDownload();
			break;
		case DELIVERY_DOWNLOAD:
			// refresh table
			CommonController.refreshTable(getSherlockActivity(), mDeliveryResultTable,
					R.layout.carrier_inspection_tbl_header, mDeliveryResultAdapter);
			afterDownload();
			break;
			
		case GetCIService.ERROR:
			showErrorDialog(ModelFactory.getCIService().getClientMessage());
			break;
		}
	}
	
	private void afterDownload() {
		ModelFactory.getCIService().saveFormData(getSherlockActivity());
		if (ModelFactory.getCIService().getDownloadQueue().size() <= 0) {
			// on download finished
			mDownloadButton.setEnabled(true);
			return;
		}	
		
		// continue download
		ModelFactory.getCIService().prepareDownload();
		int result = ModelFactory.getCIService().executeSoapRequest();
		if (result == NetworkListener.ERR_OFFLINE) {
			showErrorDialog(getString(R.string.network_not_connected));
		}
	}
}
