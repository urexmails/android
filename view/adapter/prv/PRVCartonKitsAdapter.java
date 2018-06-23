package com.contactpoint.view.adapter.prv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.client.prv.CartonKit;
import com.contactpoint.model.client.prv.CartonKits;

public class PRVCartonKitsAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private CartonKits datasource;
	
	public PRVCartonKitsAdapter(Context context, CartonKits datasource) {
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
			vi = inflater.inflate(R.layout.prv_carton_kits_template, null);
			holder = new ViewHolder();
			holder.mQtyText = (TextView) vi.findViewById(R.id.prv_ck_qty);
			holder.mTypeText = (TextView) vi.findViewById(R.id.prv_ck_type);
			holder.mDateText = (TextView) vi.findViewById(R.id.prv_ck_date);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		
		CartonKit outDTO = (CartonKit) getItem(position);
		holder.mQtyText.setText(outDTO.Quantity);
		holder.mTypeText.setText(outDTO.Type);
		holder.mDateText.setText(outDTO.DeliveryDate);
		
		return vi;
	}
	
	private static class ViewHolder {
		private TextView mQtyText;
		private TextView mTypeText;
		private TextView mDateText;
	}

}
