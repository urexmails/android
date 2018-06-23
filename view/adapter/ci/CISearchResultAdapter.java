package com.contactpoint.view.adapter.ci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.ci.CIResult;

public abstract class CISearchResultAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private static Context mContext = null;

	public CISearchResultAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.carrier_inspection_tbl, null);			
			holder = new ViewHolder();
			holder.mNameText = (TextView) vi.findViewById(R.id.search_result_name);
			holder.mMoveDateText = (TextView) vi.findViewById(R.id.search_result_move_date);
			holder.mSuburbText = (TextView) vi.findViewById(R.id.search_result_region);
			holder.mZoneText = (TextView) vi.findViewById(R.id.search_result_zone);
			holder.mVolumeText = (TextView) vi.findViewById(R.id.search_result_volume);
			holder.mCheckBox = (CheckBox) vi.findViewById(R.id.search_result_check);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}

		CIResult outDTO = (CIResult)getItem(position);
		holder.mNameText.setText(outDTO.name.toString());
		holder.mMoveDateText.setText(outDTO.moveDate);
		holder.mSuburbText.setText(outDTO.address.suburb);
		holder.mZoneText.setText(outDTO.zone);
		holder.mVolumeText.setText(outDTO.volume);
		holder.mCheckBox.setOnCheckedChangeListener(mCheckboxListener);
		holder.mCheckBox.setTag(outDTO);

		// set color for sensitive fields
		if (ModelFactory.getUtilService().isSensitive(outDTO.sensitive)) {
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				holder.mNameText.setBackgroundDrawable(
						mContext.getResources().getDrawable(R.drawable.toll_textbox_red));
			} else {
				holder.mNameText.setBackground(mContext.getResources().getDrawable(R.drawable.toll_textbox_red));
			}
		}

		return vi;
	}
	
	private CompoundButton.OnCheckedChangeListener mCheckboxListener = 
			new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				ModelFactory.getCIService().getDownloadQueue().add((CIResult)arg0.getTag());
			} else {
				ModelFactory.getCIService().getDownloadQueue().remove(arg0.getTag());			
			}
		}
	};
	
	private static class ViewHolder {
		private TextView mNameText;
		private TextView mMoveDateText;
		private TextView mSuburbText;
		private TextView mZoneText;
		private TextView mVolumeText;
		private CheckBox mCheckBox;
	}

}
