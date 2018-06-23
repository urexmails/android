package com.contactpoint.view.adapter.prv;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.view.adapter.Row;
import com.contactpoint.view.fragment.WorkListFragment;

public class PRVWorklistAdapter extends BaseAdapter {

	private final List<Row> rows;
	private final WorkListFragment mFragment;
	
	public PRVWorklistAdapter(WorkListFragment fragment) {
		mFragment = fragment;
		rows = new ArrayList<Row>();
		dataSetChanged();
	}
	
	public void dataSetChanged() {
		rows.removeAll(rows);
		for (PRVForm target : ModelFactory.getPRVService().getPRVForms()) {
			if (target.isReady()) {
				rows.add(new PRVReadyRow(mFragment, target));				
			} else if (target.isBooked()) {
				rows.add(new PRVBookedRow(mFragment, target));
			} else {
				rows.add(new PRVSimpleRow(mFragment, target));
			}
		}
	}
	
	@Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return rows.get(position).getViewType();
    }
    
	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Object getItem(int position) {
		return ModelFactory.getPRVService().getPRVForms().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return rows.get(position).getView(convertView);
	}
	
	public enum RowType {
	    SIMPLE_ROW,
	    BOOKED_ROW,
	    READY_ROW
	}
}
