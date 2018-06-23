package com.contactpoint.view.adapter.prv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.client.prv.ServiceDetail;
import com.contactpoint.model.client.prv.ServiceDetails;

public class PRVServiceAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private ServiceDetails datasource;
	
	public PRVServiceAdapter(Context context, ServiceDetails datasource) {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.datasource = datasource;
	}
	
	@Override
	public int getCount() {
		return datasource.size();
	}

	@Override
	public Object getItem(int position) {
		return datasource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.prv_services_template, null);
			holder = new ViewHolder();
			holder.mNumText = (TextView) vi.findViewById(R.id.prv_ser_num);
			holder.mTypeText = (TextView) vi.findViewById(R.id.prv_ser_type);
			holder.mDateText = (TextView) vi.findViewById(R.id.prv_ser_date);
			holder.mAddressText = (TextView) vi.findViewById(R.id.prv_ser_address);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		
		ServiceDetail outDTO = (ServiceDetail)getItem(position);
		holder.mNumText.setText(outDTO.ComponentLineNo);
		holder.mTypeText.setText(outDTO.ServiceName);
		holder.mDateText.setText(outDTO.ServiceDate);
		holder.mAddressText.setText(outDTO.ServiceAddress);
		
		return vi;
	}
	
	private static class ViewHolder {
		private TextView mNumText;
		private TextView mTypeText;
		private TextView mDateText;
		private TextView mAddressText;
	}

}
