package com.contactpoint.view.fragment.prv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.view.adapter.prv.PRVReviewRoomAdapter;

public class PRVSummaryRoomFragment extends SherlockListFragment {

	private OnNaviSelectedListener mCallback;
	public static final String TAG = "PRVSummaryRoomFragment";
	private PRVReviewRoomAdapter mAdapter;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onStart() {
		super.onStart();
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		getListView().setBackgroundResource(R.drawable.prv_room_summary_label);
		
		refresh();
	}
		
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnNaviSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnNaviSelectedListener");
		}
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {    	
		// Notify the parent activity of selected item
		mCallback.onNaviSelected(position);

		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAdapter = new PRVReviewRoomAdapter(getSherlockActivity());
		setListAdapter(mAdapter);
	}
	
	public void refresh() {
		mAdapter.notifyDataSetChanged();
		
		for (int i = 0; i < ModelFactory.getPRVService().getCurrentRoomList().size(); i++) {
			if (ModelFactory.getPRVService().getCurrentRoom() == ModelFactory.getPRVService().getCurrentRoomList().get(i)) {
				getListView().setItemChecked(i,  true);
			}
		}

	}
}
