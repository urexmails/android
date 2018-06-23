package com.contactpoint.view.adapter.prv;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.PRVFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.service.PRVService;

public class PRVAccessAdapter extends BaseAdapter {

	private LayoutInflater inflater = null;
	private PRVFragmentActivity mContext = null;
	private static final int COLUMN = 3;
	private ArrayList<ReferenceIDDropDown> mQuestionList;
	private String[] mAccessQuestions = null;
	
	public PRVAccessAdapter(PRVFragmentActivity context) {
		mContext = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mQuestionList = ModelFactory.getPRVService().getPRVQuestions(ReferenceIDDropDown.RESIDENTIAL);
		mAccessQuestions = ModelFactory.getPRVService().getCurrentForm().isMM() ? UploadPRVInDTO.MM_ACCESS : UploadPRVInDTO.ACCESS;
	}
	
	@Override
	public int getCount() {
		return mQuestionList.size() / COLUMN;
		//return mContext.getResources().getStringArray(R.array.prv_access_questions).length / COLUMN;
	}

	@Override
	public Object getItem(int position) {
		return mQuestionList.get(position);
		//return mContext.getResources().getStringArray(R.array.prv_access_questions)[position];
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
			vi = inflater.inflate(R.layout.prv_access_template, null);
			holder = new ViewHolder();
			holder.mQ1 = (TextView) vi.findViewById(R.id.prv_access_question_1);
			holder.mAnswer_U_1 = (CheckBox) vi.findViewById(R.id.prv_access_answer_u_1);
			holder.mAnswer_D_1 = (CheckBox) vi.findViewById(R.id.prv_access_answer_d_1);
			holder.mQ2 = (TextView) vi.findViewById(R.id.prv_access_question_2);
			holder.mAnswer_U_2 = (CheckBox) vi.findViewById(R.id.prv_access_answer_u_2);
			holder.mAnswer_D_2 = (CheckBox) vi.findViewById(R.id.prv_access_answer_d_2);
			holder.mQ3 = (TextView) vi.findViewById(R.id.prv_access_question_3);
			holder.mAnswer_U_3 = (CheckBox) vi.findViewById(R.id.prv_access_answer_u_3);
			holder.mAnswer_D_3 = (CheckBox) vi.findViewById(R.id.prv_access_answer_d_3);

			holder.mAnswer_U_1.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			holder.mAnswer_U_2.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			holder.mAnswer_U_3.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			holder.mAnswer_D_1.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			holder.mAnswer_D_2.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			holder.mAnswer_D_3.setOnCheckedChangeListener(mContext.PRVCheckBoxListener);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}

		position *= COLUMN;
		holder.mQ1.setText(getItem(position).toString());
		holder.mQ2.setText(getItem(position + 1).toString());
		holder.mQ3.setText(getItem(position + 2).toString());
				
		HashMap<String, String> qa = ModelFactory.getPRVService().getCurrentForm().getQuestionAnswer();
		String tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.U_ + mAccessQuestions[position];
		holder.mAnswer_U_1.setTag(tag);
		holder.mAnswer_U_1.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);
		
		tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.U_ + mAccessQuestions[position + 1];
		holder.mAnswer_U_2.setTag(tag);
		holder.mAnswer_U_2.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);
		
		tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.U_ + mAccessQuestions[position + 2];
		holder.mAnswer_U_3.setTag(tag);
		holder.mAnswer_U_3.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);
		
		tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.D_ + mAccessQuestions[position];
		holder.mAnswer_D_1.setTag(tag);
		holder.mAnswer_D_1.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);
		
		tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.D_ + mAccessQuestions[position + 1];
		holder.mAnswer_D_2.setTag(tag);
		holder.mAnswer_D_2.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);

		tag = UploadPRVInDTO.P_ACCESS_ + UploadPRVInDTO.D_ + mAccessQuestions[position + 2];
		holder.mAnswer_D_3.setTag(tag);				
		holder.mAnswer_D_3.setChecked(qa.containsKey(tag) && qa.get(tag).compareTo(PRVService.YES) == 0);
		
		return vi;
	}
	
	private static class ViewHolder {
		private TextView mQ1;
		private CheckBox mAnswer_U_1;
		private CheckBox mAnswer_D_1;
		private TextView mQ2;
		private CheckBox mAnswer_U_2;
		private CheckBox mAnswer_D_2;
		private TextView mQ3;
		private CheckBox mAnswer_U_3;
		private CheckBox mAnswer_D_3;
	}

}
