package com.contactpoint.view.adapter.prv;

import java.util.Vector;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;

public class PRVReviewAddOnDetailAdapter extends BaseAdapter {

	private static LayoutInflater inflater;
	private Vector<NameValuePair> mItems;
	
	public PRVReviewAddOnDetailAdapter(Context context) {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItems = new Vector<NameValuePair>();
	}
	
	public void setItems(Vector<NameValuePair> items) {
		mItems.removeAllElements();
		mItems.addAll(items);
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int location) {
		return mItems.get(location);
	}

	@Override
	public long getItemId(int location) {
		return location;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.prv_summary_item_template, null);
			holder = new ViewHolder();
			holder.mNameText = (TextView)vi.findViewById(R.id.name_text);
			holder.mValueText = (TextView)vi.findViewById(R.id.value_text);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		NameValuePair outDTO = (NameValuePair)getItem(position);
		holder.mNameText.setText(outDTO.getName());
		holder.mValueText.setText(outDTO.getValue());

		return vi;
	}

	private static class ViewHolder {
		private TextView mNameText;
		private TextView mValueText;
	}

}
