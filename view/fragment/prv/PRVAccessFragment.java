package com.contactpoint.view.fragment.prv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.view.adapter.prv.PRVAccessAdapter;

public class PRVAccessFragment extends SherlockFragment {

	public static final String TAG = "PRVAccessFragment";
	private PRVAccessAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
		if (mAdapter == null) {
			mAdapter = new PRVAccessAdapter((PRVFragmentActivity)getSherlockActivity());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.prv_access, null);
		((ListView)v.findViewById(R.id.prv_access)).setAdapter(mAdapter);
		
		return v;
	}
}
