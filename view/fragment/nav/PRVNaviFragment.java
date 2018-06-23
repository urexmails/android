package com.contactpoint.view.fragment.nav;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.view.adapter.ci.CICustomListAdapter;

public class PRVNaviFragment extends SherlockListFragment {

	OnNaviSelectedListener mCallback;
	private int mCurrentPosition = -1;

	public static final String TAG = "prvListFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

		if (savedInstanceState != null) {
			mCurrentPosition = savedInstanceState.getInt(PRVFragmentActivity.EXTRA);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected list item
		// (We do this during onStart because at the point the listview is available.)
		if (getFragmentManager().findFragmentById(R.id.right_container) != null) {
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}

		getListView().setBackgroundResource(R.drawable.nav_left_bg);
		//		getListView().setItemChecked(1, true);

		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
			int extra = args.getInt(PRVFragmentActivity.EXTRA);
			updateView(extra);
		} else if (mCurrentPosition != -1) {
			// Set article based on saved instance state defined during onCreate
			updateView(mCurrentPosition);
		} else {
			mCurrentPosition = PRVFragmentActivity.CUST_DETAIL;
			updateView(mCurrentPosition);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try {
			mCallback = (OnNaviSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnNaviSelectedListener");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {    	
		updateView(position);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current article selection in case we need to recreate the fragment
		outState.putInt(PRVFragmentActivity.EXTRA, mCurrentPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// put header 
		LayoutInflater vi = (LayoutInflater)getActivity()
				.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.custom_list_fragment, null);
		ImageView img = (ImageView) v.findViewById(R.id.list_item);
		img.setImageResource(R.drawable.main_menu);

		getListView().addHeaderView(v);

		// Create an array adapter for the list view
		ArrayList<String> navi = new ArrayList<String>();
		Collections.addAll(navi, getResources().getStringArray(R.array.prv_navi_list));		
		setListAdapter(new CICustomListAdapter(getActivity(), 
				R.layout.ci_custom_list_fragment, navi));
		
		ModelFactory.getPRVService().refreshSummaryOutDTO();
		setNaviCompletedIndicator(PRVFragmentActivity.ITEM_COL, ModelFactory.getPRVService().getCurrentForm().itemCollectionCompleted());
		setNaviCompletedIndicator(PRVFragmentActivity.PRV_CHECKLIST, ModelFactory.getPRVService().getCurrentForm().checklistCompleted());
	}

	public void updateView(int position) {
		// Notify the parent activity of selected item
		mCallback.onNaviSelected(position);

		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);

		mCurrentPosition = position;
	}

	// A method to set navi item to bold if all form data is valid
	public void setNaviCompletedIndicator(int naviItem, boolean isValid) {
		// set valid according to position
		int wantedChild = naviItem - getListView().getHeaderViewsCount();
		((CICustomListAdapter)getListAdapter()).setValidAtPosition(wantedChild, isValid);

//		// if everything is valid, set the form as ready for submit
//		if (((CICustomListAdapter)getListAdapter()).allPRVIsValid()) {
//			ModelFactory.getPRVService().getCurrentForm().getDownload().setReady(true);
//		}
		getListView().invalidateViews();
	}
}
