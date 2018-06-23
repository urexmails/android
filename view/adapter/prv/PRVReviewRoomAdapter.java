package com.contactpoint.view.adapter.prv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.RoomOutDTO;

public class PRVReviewRoomAdapter extends BaseAdapter {

	private static LayoutInflater inflater;
	
	public PRVReviewRoomAdapter(Context context) {
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return ModelFactory.getPRVService().getCurrentRoomList().size();
	}

	@Override
	public Object getItem(int location) {
		return ModelFactory.getPRVService().getCurrentRoomList().get(location);
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
			vi = inflater.inflate(R.layout.prv_summary_room_template, null);
			holder = new ViewHolder();
			holder.mText = (TextView)vi.findViewById(R.id.value_text);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder)vi.getTag();
		}
		
		RoomOutDTO outDTO = (RoomOutDTO)getItem(position);
		holder.mText.setText(outDTO.Name);
		
		return vi;
	}

	private static class ViewHolder {
		private TextView mText;
	}
}
