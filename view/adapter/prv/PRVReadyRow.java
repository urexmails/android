package com.contactpoint.view.adapter.prv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.view.adapter.Row;
import com.contactpoint.view.fragment.WorkListFragment;

public class PRVReadyRow implements Row {

	private final WorkListFragment mFragment;
	private final LayoutInflater mInflater;
	private final PRVForm outDTO;
	
	public PRVReadyRow(WorkListFragment fragment, PRVForm target) {
		mFragment = fragment;
		mInflater = (LayoutInflater)mFragment.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		outDTO = target;
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public View getView(View convertView) {
		ViewHolder holder;
        View vi = convertView;

        if (convertView == null) {
			vi = mInflater.inflate(R.layout.work_list_prv_tbl_ready, null);
			holder = new ViewHolder();
			holder.mNameText = (TextView) vi.findViewById(R.id.worklist_prv_name);
			holder.mPONumberText = (TextView) vi.findViewById(R.id.worklist_prv_po_num);
			holder.mSuburbText = (TextView) vi.findViewById(R.id.worklist_prv_suburb);
			holder.mStartText = (TextView) vi.findViewById(R.id.worklist_prv_start);
			holder.mEndText = (TextView) vi.findViewById(R.id.worklist_prv_end);
			holder.mBookText = (TextView) vi.findViewById(R.id.worklist_prv_book_date);
				        
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
        
        PRVDownload download = outDTO.getDownload().get(0);
        holder.mNameText.setText(download.memberName.toString());
        holder.mPONumberText.setText(ModelFactory.getPRVService().getCombinedPONumber(download));
        holder.mSuburbText.setText(download.memberAddress.suburb);
        holder.mStartText.setText(download.PRVStartDate);
        holder.mEndText.setText(download.PRVEndDate);
        holder.mBookText.setText(download.bookedDateTime);

		if (ModelFactory.getUtilService().isSensitive(download.sensitive)) {
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				holder.mNameText.setBackgroundDrawable(
						mFragment.getResources().getDrawable(R.drawable.toll_textbox_red));
			} else {
				holder.mNameText.setBackground(mFragment.getResources().getDrawable(R.drawable.toll_textbox_red));
			}
		}
				
		return vi;
	}

	@Override
	public int getViewType() {
		return PRVWorklistAdapter.RowType.READY_ROW.ordinal();
	}

	private static class ViewHolder {
		private TextView mNameText;
		private TextView mPONumberText;
		private TextView mSuburbText;
		private TextView mStartText;
		private TextView mEndText;
		private TextView mBookText;
	}

}
