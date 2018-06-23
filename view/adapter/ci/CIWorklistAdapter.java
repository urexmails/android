package com.contactpoint.view.adapter.ci;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.CIFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.CIForm;

public abstract class CIWorklistAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private static Context mContext = null;

	public CIWorklistAdapter(Context context) {
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
			vi = inflater.inflate(R.layout.work_list_tbl, null);			
			holder = new ViewHolder();
			holder.mNameText = (TextView) vi.findViewById(R.id.worklist_name);
			holder.mMoveDateText = (TextView) vi.findViewById(R.id.worklist_move_date);
			holder.mSuburbText = (TextView) vi.findViewById(R.id.worklist_region);
			holder.mZoneText = (TextView) vi.findViewById(R.id.worklist_zone);
			holder.mVolumeText = (TextView) vi.findViewById(R.id.worklist_volume);
			holder.mStatusText = (TextView) vi.findViewById(R.id.worklist_status);			
			holder.mCheckBox = (CheckBox) vi.findViewById(R.id.worklist_check);
			holder.mCheckBox.setOnCheckedChangeListener(mCheckboxListener);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		
		CIForm outDTO = (CIForm) getItem(position);
		SpannableString underlinedContent = new SpannableString(outDTO.getDownload()
				.memberName.toString());
		underlinedContent.setSpan(new UnderlineSpan(), 0, underlinedContent.length(), 0);
		holder.mNameText.setText(underlinedContent);
		
		holder.mMoveDateText.setText(outDTO.getDownload().moveDate);
		holder.mSuburbText.setText(outDTO.getDownload().memberAddress.suburb);
		holder.mZoneText.setText(outDTO.getDownload().zone);
		holder.mVolumeText.setText(outDTO.getDownload().volume);
		
		// CIForm status
		if (outDTO.getUpload().isValid()) {
			holder.mStatusText.setText(mContext.getResources().getString(R.string.form_ready));
		} else {
			if (outDTO.getUpload().getQuestionAnswers().size() != 0 || outDTO.getUpload().getQuestionAnswers().size() != 0) {
				holder.mStatusText.setText(mContext.getResources().getString(R.string.form_ongoing));
			} else {
				holder.mStatusText.setText(mContext.getResources().getString(R.string.form_new));
			}
			holder.mNameText.setTag(outDTO);
			holder.mNameText.setOnClickListener(mFormListener);
		}
		
		// set tag for delete
		holder.mCheckBox.setTag(outDTO);
		
		// set color for sensitive fields
		if (ModelFactory.getUtilService().isSensitive(outDTO.getDownload().sensitive)) {
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
				ModelFactory.getCIService().getDeleteQueue().add((CIForm)arg0.getTag());
			} else {
				ModelFactory.getCIService().getDeleteQueue().remove(arg0.getTag());			
			}
		}
	};
	
	private OnClickListener mFormListener = new OnClickListener() {

		public void onClick(View v) {
			Intent i = new Intent(mContext, CIFragmentActivity.class);
			i.putExtra(CIFragmentActivity.CIDOWNLOAD, (CIForm)v.getTag());
			v.setBackgroundColor(mContext.getResources().getColor(R.color.abs__bright_foreground_holo_light));
			mContext.startActivity(i);
		}

	};
	
	public static class ViewHolder {
		private TextView mNameText;
		private TextView mMoveDateText;
		private TextView mSuburbText;
		private TextView mZoneText;
		private TextView mVolumeText;
		private TextView mStatusText;
		private CheckBox mCheckBox;
	}

}
