package com.contactpoint.view.fragment.prv;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.view.adapter.prv.PRVReviewAddOnAdapter;

public class PRVSummaryAddOnFragment extends SherlockListFragment {

	public static final String TAG = "PRVSummaryAddOnFragment";
	private PRVReviewAddOnAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new PRVReviewAddOnAdapter(getSherlockActivity());
		setListAdapter(mAdapter);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		getListView().setBackgroundResource(R.drawable.prv_room_summary_details);
	}
		
	public void refresh() {
		mAdapter.notifyDataSetChanged();
	}
}
