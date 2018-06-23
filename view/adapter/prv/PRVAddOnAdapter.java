package com.contactpoint.view.adapter.prv;

import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.PRVItemCollectionActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;

public class PRVAddOnAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private Vector<ItemInDTO> mItems;
	private boolean isCrate;
	private final String IS_CRATE = "%s\n%s x %s x %s"; 
	private boolean isEditing;
	private View.OnLongClickListener onLongIncrementListener;
	private View.OnLongClickListener onLongDecrementListener;

	public PRVAddOnAdapter(PRVItemCollectionActivity context) {
		isEditing = false;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		onLongIncrementListener = context.onLongIncrementListener;
		onLongDecrementListener = context.onLongDecrementListener;
	}
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
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
			vi = inflater.inflate(R.layout.prv_show_all_content_template, null);
			holder = new ViewHolder();
			holder.mNameText = (TextView)vi.findViewById(R.id.content_name);
			holder.mValueText = (EditText)vi.findViewById(R.id.content_text);
			holder.mMinusButton = (Button)vi.findViewById(R.id.btn_content_minus);
			holder.mPlusButton = (Button)vi.findViewById(R.id.btn_content_plus);
			holder.mDeleteButton = (Button)vi.findViewById(R.id.btn_item_delete);
			holder.mDeleteButton.setOnClickListener(mOnDeleteListener);

			holder.mMinusButton.setTag(holder.mValueText);
			holder.mPlusButton.setTag(holder.mValueText);
			holder.mMinusButton.setOnLongClickListener(onLongDecrementListener);
			holder.mPlusButton.setOnLongClickListener(onLongIncrementListener);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}

		ItemInDTO i = (ItemInDTO)getItem(position);
		if (!isCrate) {
			holder.mNameText.setText(i.Name);
		} else {
			holder.mNameText.setText(String.format(IS_CRATE, i.Name, i.Length, i.Width, i.Depth));
			holder.mDeleteButton.setTag(i);
			
		}
		if (isEditing) {
			holder.mDeleteButton.setVisibility(View.VISIBLE);
		} else {
			holder.mDeleteButton.setVisibility(View.GONE);
		}
		
		holder.mValueText.setText("" + i.getQty());
		holder.mValueText.setTag(i);
		
		vi.setOnLongClickListener(mLongClickListener);
		vi.setOnClickListener(mTapListener);
		return vi;
	}

	private static class ViewHolder {
		private TextView mNameText;
		private EditText mValueText;
		private Button mMinusButton;
		private Button mPlusButton;
		private Button mDeleteButton;
	}

	public void setItems(String which) {
		if (which.compareTo(ItemInDTO.CARTON) == 0) {
			mItems = ModelFactory.getPRVService().getCurrentRoom().getCartonList();
			isCrate = false;
		} else if (which.compareTo(ItemInDTO.COVER) == 0) {
			mItems = ModelFactory.getPRVService().getCurrentRoom().getCoverList();
			isCrate = false;
		} else if (which.compareTo(ItemInDTO.CRATE) == 0) {
			mItems = ModelFactory.getPRVService().getCurrentRoom().getCrateList();
			isCrate= true;
		}
	}
	
	private OnLongClickListener mLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View arg0) {
			if (isCrate && !isEditing) {
				isEditing = !isEditing;
				notifyDataSetChanged();
			}
			return true;
		}

	};
	
	private OnClickListener mTapListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isCrate && isEditing) {
				isEditing = !isEditing;
				notifyDataSetChanged();
			}
		}

	};
	
	private OnClickListener mOnDeleteListener = new OnClickListener() {

		@Override
		public void onClick(View vi) {
			ModelFactory.getPRVService().removeCrate((ItemInDTO)vi.getTag());
			if (ModelFactory.getPRVService().getCurrentRoom().getCrateList().size() == 0 && 
					isEditing) {
				isEditing = !isEditing;
			}
			notifyDataSetChanged();
		}

	};

}
