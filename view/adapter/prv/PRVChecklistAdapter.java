package com.contactpoint.view.adapter.prv;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.service.PRVService;

public class PRVChecklistAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private PRVFragmentActivity mContext = null;
	private ArrayList<ReferenceIDDropDown> mQuestionList;

	public PRVChecklistAdapter(PRVFragmentActivity context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mQuestionList = ModelFactory.getPRVService().getPRVQuestions(ReferenceIDDropDown.CHECKLIST);
	}
	
	@Override
	public int getCount() {
		return mQuestionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mQuestionList.get(position);
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
			vi = inflater.inflate(R.layout.prv_checklist_template, null);
			holder = new ViewHolder();
			holder.mNumText = (TextView) vi.findViewById(R.id.prv_checklist_num);
			holder.mQuestionText = (TextView) vi.findViewById(R.id.prv_checklist_question);
			holder.mAnswer = (RadioGroup) vi.findViewById(R.id.prv_checklist_answer);
			holder.mAnswer.setOnCheckedChangeListener(mContext.PRVRadioButtonListener);

			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		
		HashMap<String, String> qa = ModelFactory.getPRVService().getCurrentForm().getQuestionAnswer();

		holder.mNumText.setText((position + 1) + ".");
		holder.mQuestionText.setText(getItem(position).toString());
		
		String tag = UploadPRVInDTO.P_PRV_ + (position + 1);
		holder.mAnswer.setTag(tag);
		if (qa.containsKey(tag)) {
			if (qa.get(tag).compareTo(PRVService.YES) == 0)	holder.mAnswer.check(R.id.prv_radio_yes);
			else holder.mAnswer.check(R.id.prv_radio_no);
		}
		
		return vi;
	}
	
	private static class ViewHolder {
		private TextView mNumText;
		private TextView mQuestionText;
		private RadioGroup mAnswer;
	}

}
