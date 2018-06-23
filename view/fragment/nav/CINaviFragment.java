package com.contactpoint.view.fragment.nav;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.view.adapter.ci.CICustomListAdapter;

public class CINaviFragment extends SherlockListFragment {
	OnNaviSelectedListener mCallback;

	public static final String TAG = "ciListFragment";
	public static final int SIGNATURE_TAB = 8;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		// We need to use a different list item layout for devices older than Honeycomb
		//        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
		//                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

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
		getListView().setItemChecked(1, true);
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
		
		if (position == SIGNATURE_TAB && !((CICustomListAdapter)getListAdapter()).ciSignable()) {
			AlertDialog ad = DialogFactory.getAlertDialog(getSherlockActivity());
			ad.setMessage(getResources().getString(R.string.ci_incomplete_before_sign));
			ad.show();
			return;
		}
		
		// Notify the parent activity of selected item
		mCallback.onNaviSelected(position);

		// Set the item as checked to be highlighted when in two-pane layout
		getListView().setItemChecked(position, true);
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
		switch (((CIFragmentActivity)getSherlockActivity()).getFragmentType()) {
		case CIFragmentActivity.UPLIFT:
			Collections.addAll(navi, getResources().getStringArray(R.array.ci_navi_list_uplift));
			break;
		case CIFragmentActivity.DELIVERY:
			Collections.addAll(navi, getResources().getStringArray(R.array.ci_navi_list_delivery));
			break;
		default:
			break;
		}
		
		setListAdapter(new CICustomListAdapter(getActivity(), 
				R.layout.ci_custom_list_fragment, navi));
	}

	// A method to set navi item to bold if all form data is valid
	public void setNaviCompletedIndicator(int naviItem, boolean isValid) {
		// set valid according to position
		int wantedChild = naviItem - getListView().getHeaderViewsCount();
		((CICustomListAdapter)getListAdapter()).setValidAtPosition(wantedChild, isValid);
		
		getListView().invalidateViews();
	}
	
	// A method to check if all CI form is valid
	public boolean isAllValid() {
		return ((CICustomListAdapter)getListAdapter()).allCIIsValid();
	}
	
}
