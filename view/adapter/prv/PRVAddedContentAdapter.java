package com.contactpoint.view.adapter.prv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;

public class PRVAddedContentAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private boolean isEditing;

	public PRVAddedContentAdapter(Context context) {
		isEditing = false;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		try {
			return ModelFactory.getPRVService().getCurrentItemList().size();
		} catch (NullPointerException ex) {
			return 0;
		}
	}

	@Override
	public Object getItem(int index) {
		return ModelFactory.getPRVService().getCurrentItemList().get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.prv_template_item_detail_item, null);
			holder = new ViewHolder();
			holder.mNameText = (TextView) vi.findViewById(R.id.item_name);
			holder.mValueText = (TextView) vi.findViewById(R.id.item_value);
			holder.mMinusButton = (Button) vi.findViewById(R.id.btn_item_delete);
			holder.mMinusButton.setOnClickListener(mOnDeleteListener);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}

		ItemInDTO inDTO = (ItemInDTO)getItem(position);
		holder.mNameText.setText(inDTO.Name);
		holder.mValueText.setText(inDTO.getQty() + " x " + inDTO.CubicMeterage);
		holder.mMinusButton.setTag(inDTO);

		if (isEditing) {
			holder.mMinusButton.setVisibility(View.VISIBLE);
		} else {
			holder.mMinusButton.setVisibility(View.GONE);
		}

		vi.setOnLongClickListener(mLongClickListener);
		vi.setOnClickListener(mTapListener);
		return vi;
	}

	private static class ViewHolder {
		private TextView mNameText;
		private TextView mValueText;
		private Button mMinusButton;
	}

	private OnLongClickListener mLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			if (!isEditing) {
				isEditing = !isEditing;
				notifyDataSetChanged();
			}
			return false;
		}

	};

	private OnClickListener mTapListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isEditing) {
				isEditing = !isEditing;
				notifyDataSetChanged();
			}
		}

	};

	private OnClickListener mOnDeleteListener = new OnClickListener() {

		@Override
		public void onClick(View vi) {
			ModelFactory.getPRVService().removeItem((ItemInDTO)vi.getTag());
			if (ModelFactory.getPRVService().getCurrentItemList().size() == 0 && isEditing) {
				isEditing = !isEditing;
			}
			notifyDataSetChanged();
		}

	};

}
