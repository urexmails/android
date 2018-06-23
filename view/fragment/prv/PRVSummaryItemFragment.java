package com.contactpoint.view.fragment.prv;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.view.adapter.prv.PRVReviewItemAdapter;

public class PRVSummaryItemFragment extends SherlockListFragment {

	public static final String TAG = "PRVSummaryItemFragment";
	private PRVReviewItemAdapter mAdapter;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new PRVReviewItemAdapter(getSherlockActivity());
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
