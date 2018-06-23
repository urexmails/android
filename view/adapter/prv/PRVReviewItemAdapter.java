package com.contactpoint.view.adapter.prv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;

public class PRVReviewItemAdapter extends BaseAdapter {

	private static LayoutInflater inflater;
	private double mTotal;

	public PRVReviewItemAdapter(Context context) {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mTotal = 0;
		for (ItemInDTO item : ModelFactory.getPRVService().getCurrentItemList()) {
			mTotal += Double.parseDouble(item.CubicMeterage) * item.getQty();
		}
	}
	
	public double getTotal() {
		return mTotal;
	}
	
	@Override
	public int getCount() {
		return ModelFactory.getPRVService().getCurrentItemList().size();
	}

	@Override
	public Object getItem(int location) {
		return ModelFactory.getPRVService().getCurrentItemList().get(location);
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
		ItemInDTO outDTO = (ItemInDTO)getItem(position);
		holder.mNameText.setText(outDTO.Name);
		holder.mValueText.setText(outDTO.getQty() + " x " + outDTO.CubicMeterage);
		
		return vi;
	}
	
	private static class ViewHolder {
		private TextView mNameText;
		private TextView mValueText;
	}

}
