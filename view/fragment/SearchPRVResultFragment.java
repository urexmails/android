package com.contactpoint.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.PRVResult;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.view.adapter.prv.PRVSearchResultAdapter;

public class SearchPRVResultFragment extends NetworkFragment {

	public static final String TAG = "SearchPRVResultFragment";
	private PRVSearchResultAdapter mResultAdapter;
	
	private TableLayout mPRVResultTable;
	private Button mDownloadButton;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
		mResultAdapter = new PRVSearchResultAdapter(getSherlockActivity());
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			CommonController.refreshTable(getSherlockActivity(), mPRVResultTable, 
					R.layout.prv_search_tbl_header, mResultAdapter);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.prv_result, null);
		
		mPRVResultTable = (TableLayout) v.findViewById(R.id.tbl_prv_result);
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
			if (ModelFactory.getPRVService().getDownloadQueue().size() <= 0) return;

			mDownloadButton.setEnabled(false);
			ModelFactory.getPRVService().preparePRVDownload();
			ModelFactory.getPRVService().setNetworkListener(SearchPRVResultFragment.this);
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
		}
	};
	
	public final static CompoundButton.OnCheckedChangeListener CHECKBOX_LISTENER = 
			new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				ModelFactory.getPRVService().getDownloadQueue().add((PRVResult)arg0.getTag());
			} else {
				ModelFactory.getPRVService().getDownloadQueue().remove(arg0.getTag());			
			}
		}
	};
	
	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case PRVService.PRV_DOWNLOAD:
			// remove from table
			CommonController.refreshTable(getSherlockActivity(), mPRVResultTable, 
					R.layout.prv_search_tbl_header, mResultAdapter);			
			ModelFactory.getPRVService().saveFormData(getSherlockActivity());
			if (ModelFactory.getPRVService().getDownloadQueue().size() <= 0) {
				// on download finished
				mDownloadButton.setEnabled(true);
				break;
			}	
			
			// continue download
			ModelFactory.getPRVService().preparePRVDownload();
			int result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
			break;
		case PRVService.ERROR:
			showErrorDialog(ModelFactory.getPRVService().getClientMessage());
			mDownloadButton.setEnabled(true);
			break;
		}
	}
}
