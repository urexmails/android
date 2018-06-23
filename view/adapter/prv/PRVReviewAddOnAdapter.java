package com.contactpoint.view.adapter.prv;

import java.util.Vector;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;

public class PRVReviewAddOnAdapter extends BaseAdapter {

	private static LayoutInflater inflater;
	private Vector<ItemInDTO> mItems;
	private double mTotal;
	
	private DataSetObserver mObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			mItems.removeAllElements();
			RoomOutDTO helper = ModelFactory.getPRVService().getCurrentRoom();
			for (ItemInDTO in : helper.getCartonList()) {
				if (in.getQty() != 0) mItems.add(in);
			}
			for (ItemInDTO in : helper.getCoverList()) {
				if (in.getQty() != 0) mItems.add(in);
			}
			for (ItemInDTO in : helper.getCrateList()) {
				if (in.getQty() != 0) mItems.add(in);
			}
		}
	};

	public PRVReviewAddOnAdapter(Context context) {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mItems = new Vector<ItemInDTO>();
		registerDataSetObserver(mObserver);
		notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mTotal = 0;
		for (ItemInDTO item : mItems) {
			mTotal += Double.parseDouble(item.CubicMeterage) * item.getQty();
		}
	}
	
	public double getTotal() {
		return mTotal;
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
		ItemInDTO outDTO = (ItemInDTO)getItem(position);
		holder.mNameText.setText(outDTO.Name);
		holder.mValueText.setText("" + outDTO.getQty());

		return vi;
	}

	private static class ViewHolder {
		private TextView mNameText;
		private TextView mValueText;
	}

}
