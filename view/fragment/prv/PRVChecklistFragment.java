package com.contactpoint.view.fragment.prv;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.view.adapter.prv.PRVChecklistAdapter;

public class PRVChecklistFragment extends SherlockFragment {

	public static final String TAG = "PRVChecklistFragment";
	private PRVChecklistAdapter mAdapter;
	private TableLayout mChecklistTbl;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
		if (mAdapter == null) {
			mAdapter = new PRVChecklistAdapter((PRVFragmentActivity)getSherlockActivity());
			mAdapter.registerDataSetObserver(mDataSetObserver);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.prv_checklist, null);
		mChecklistTbl = (TableLayout)v.findViewById(R.id.prv_checklist);
		mAdapter.notifyDataSetChanged();
		
		return v;
	}
	
	private DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			mChecklistTbl.removeAllViews();
			for (int i = 0; i < mAdapter.getCount(); i++) {
				mChecklistTbl.addView(mAdapter.getView(i, null, mChecklistTbl));
			}
		}
	};
}
