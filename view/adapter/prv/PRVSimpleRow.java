package com.contactpoint.view.adapter.prv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.view.adapter.Row;
import com.contactpoint.view.fragment.WorkListFragment;

public class PRVSimpleRow implements Row {

	private final WorkListFragment mFragment;
	private final LayoutInflater mInflater;
	private final PRVForm outDTO;
	
	public PRVSimpleRow(WorkListFragment fragment, PRVForm target) {
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
			vi = mInflater.inflate(R.layout.work_list_prv_tbl, null);
			holder = new ViewHolder();
			holder.mOrderSpinner = (Spinner) vi.findViewById(R.id.worklist_prv_order_num);
			holder.mNameText = (TextView) vi.findViewById(R.id.worklist_prv_name);
			holder.mPONumberText = (TextView) vi.findViewById(R.id.worklist_prv_po_num);
			holder.mSuburbText = (TextView) vi.findViewById(R.id.worklist_prv_suburb);
			holder.mStartText = (TextView) vi.findViewById(R.id.worklist_prv_start);
			holder.mEndText = (TextView) vi.findViewById(R.id.worklist_prv_end);
			holder.mBookBtn = (Button) vi.findViewById(R.id.worklist_prv_book_btn);
			holder.mDeleteBtn = (Button) vi.findViewById(R.id.worklist_prv_delete_btn);
			
			holder.mBookBtn.setOnClickListener(mFragment.BOOK_LISTENER);
			holder.mDeleteBtn.setOnClickListener(mFragment.PRV_DELETE_LISTENER);
			
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
	        		mFragment.getSherlockActivity(), R.array.order_list, 
					android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        holder.mOrderSpinner.setAdapter(adapter);
			
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
        
        PRVDownload download = outDTO.getDownload().get(0);
        holder.mOrderSpinner.setSelection(outDTO.getOrder());
        holder.mNameText.setText(download.memberName.toString());
        holder.mPONumberText.setText(ModelFactory.getPRVService().getCombinedPONumber(download));
        holder.mSuburbText.setText(download.memberAddress.suburb);
        holder.mStartText.setText(download.PRVStartDate);
        holder.mEndText.setText(download.PRVEndDate);
        
		if (ModelFactory.getUtilService().isSensitive(download.sensitive)) {
			int sdk = android.os.Build.VERSION.SDK_INT;
			if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				holder.mNameText.setBackgroundDrawable(
						mFragment.getResources().getDrawable(R.drawable.toll_textbox_red));
			} else {
				holder.mNameText.setBackground(mFragment.getResources().getDrawable(R.drawable.toll_textbox_red));
			}
		}
		
		holder.mBookBtn.setTag(outDTO);
		holder.mDeleteBtn.setTag(outDTO);
        
		return vi;
	}

	@Override
	public int getViewType() {
		return PRVWorklistAdapter.RowType.SIMPLE_ROW.ordinal();
	}

	private static class ViewHolder {
		private Spinner mOrderSpinner;
		private TextView mNameText;
		private TextView mPONumberText;
		private TextView mSuburbText;
		private TextView mStartText;
		private TextView mEndText;
		private Button mBookBtn;
		private Button mDeleteBtn;
	}

}
