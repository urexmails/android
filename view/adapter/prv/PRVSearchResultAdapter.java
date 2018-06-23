package com.contactpoint.view.adapter.prv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.PRVResult;
import com.contactpoint.view.fragment.SearchPRVResultFragment;

public class PRVSearchResultAdapter extends BaseAdapter {

	private final LayoutInflater inflater;
	private final Context mContext;

	public PRVSearchResultAdapter(Context context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return ModelFactory.getPRVService().getPRVResults().size();
	}

	@Override
	public Object getItem(int index) {
		return ModelFactory.getPRVService().getPRVResults().get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.prv_search_tbl, null);
			holder = new ViewHolder();
			holder.mNameText = (TextView) vi.findViewById(R.id.search_result_name);
			holder.mClientCodeText = (TextView) vi.findViewById(R.id.search_result_client_code);
			holder.mZoneText = (TextView) vi.findViewById(R.id.search_result_zone);
			holder.mSuburbText = (TextView) vi.findViewById(R.id.search_result_suburb);
			holder.mStartText = (TextView) vi.findViewById(R.id.search_result_start);
			holder.mEndText = (TextView) vi.findViewById(R.id.search_result_end);
			holder.mBookText = (TextView) vi.findViewById(R.id.search_result_book_time);
			holder.mCheckBox = (CheckBox) vi.findViewById(R.id.search_result_check);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}

		PRVResult outDTO = (PRVResult)getItem(position);
		holder.mNameText.setText(outDTO.name.toString());
		holder.mClientCodeText.setText(outDTO.clientCode);
		holder.mZoneText.setText(outDTO.zone);
		holder.mSuburbText.setText(outDTO.suburb);
		holder.mStartText.setText(outDTO.pRVStartDate);
		holder.mEndText.setText(outDTO.pRVEndDate);
		holder.mBookText.setText(outDTO.bookedDateTime);
		holder.mCheckBox.setOnCheckedChangeListener(SearchPRVResultFragment.CHECKBOX_LISTENER);
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

	public static class ViewHolder {
		private TextView mNameText;
		private TextView mClientCodeText;
		private TextView mZoneText;
		private TextView mSuburbText;
		private TextView mStartText;
		private TextView mEndText;
		private TextView mBookText;
		private CheckBox mCheckBox;
	}

}
