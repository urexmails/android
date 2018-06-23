package com.contactpoint.view.fragment.nav;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.view.adapter.CustomListAdapter;

public class NaviFragment extends SherlockListFragment {
	OnNaviSelectedListener mCallback;

	public static final String EXTRA = "SELECTED";
	public static final String TAG = "ListFragment";
	private int mCurrentPosition = -1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
//        int layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
//                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;

        // Create an array adapter for the list view, using the Ipsum headlines array
//        ArrayList<String> navi = new ArrayList<String>();
//		Collections.addAll(navi, getResources().getStringArray(R.array.navi_list));
        
        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(EXTRA);
        }
        
        ArrayList<Integer> navi = new ArrayList<Integer>();
        navi.add(0, R.drawable.fragment_ci);
        navi.add(1, R.drawable.fragment_work_list);
        navi.add(2, R.drawable.fragment_upload);
        navi.add(3, R.drawable.fragment_logout);
        setListAdapter(new CustomListAdapter(getActivity(), 
        		R.layout.custom_list_fragment, navi));
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
        
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView(args.getInt(EXTRA));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreate
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
        outState.putInt(EXTRA, mCurrentPosition);
    }
    
    public void updateView(int position) {
    	// Notify the parent activity of selected item
        mCallback.onNaviSelected(position);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
        
        mCurrentPosition = position;
    }
}
