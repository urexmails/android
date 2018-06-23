package com.contactpoint.view.adapter.prv;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.client.prv.Items;
import com.contactpoint.model.client.prv.Room;
import com.contactpoint.model.client.prv.RoomItem;

public class PRVSearchAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private int mLastExpandedGroupPosition;
	private final LayoutInflater mInflater;
	private final TreeMap <Integer, Vector<Integer>> mRoomItems;
	private boolean isMM;
	
	public PRVSearchAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		isMM = ModelFactory.getPRVService().getCurrentForm().isMM();
		mRoomItems = new TreeMap<Integer, Vector<Integer>>();
		
		for (Room room : ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).rooms) {
			mRoomItems.put(room.ID, new Vector<Integer>());
		}
		for (RoomItem ri : ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).roomItems) {
			mRoomItems.get(ri.RoomID).add(ri.ItemID);
		}
		
		// sort items
		for (Map.Entry<Integer, Vector<Integer>> entry : mRoomItems.entrySet()) {
			Collections.sort(mRoomItems.get(entry.getKey()), new Comparator<Integer>() {
				@Override
				public int compare(Integer lhs, Integer rhs) {
					Items items = ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).items;					
					return items.get(lhs).Name.toLowerCase().compareTo(items.get(rhs).Name.toLowerCase());
				}
				
			});
		}
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mRoomItems.get(((Room)getGroup(groupPosition)).ID).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, 
			View v, ViewGroup parent) {
		ViewHolder holder;
        View vi = v;
        
        if (v == null) {
			vi = mInflater.inflate(R.layout.prv_expandable_child_row, null);
			holder = new ViewHolder();
			holder.mText = (TextView)vi.findViewById(R.id.child_text);
			vi.setTag(holder);			
        } else {
			holder = (ViewHolder)vi.getTag();
		}
        Item outDTO = ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).items
        		.get(getChild(groupPosition, childPosition));
        holder.mText.setText(outDTO.Name);
        holder.mText.setTag(outDTO);
        if (childPosition == 0) {
        	vi.setBackgroundResource(R.drawable.prv_item_label_top);
        } else {
        	vi.setBackgroundResource(R.drawable.prv_item_label);
        }
		
		return vi;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mRoomItems.get(((Room)getGroup(groupPosition)).ID).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).rooms.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).rooms.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return ((Room)getGroup(groupPosition)).ID;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View v, ViewGroup parent) {
		ViewHolder holder;
        View vi = v;
        
        if (v == null) {
			vi = mInflater.inflate(R.layout.prv_expandable_row, null);
			holder = new ViewHolder();
			holder.mText = (TextView)vi.findViewById(R.id.parent_text);
			vi.setTag(holder);
        } else {
			holder = (ViewHolder)vi.getTag();
		}
        
        Room outDTO = (Room) getGroup(groupPosition);
        holder.mText.setText(outDTO.Name);
		
		return vi;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public int getLastExpandedGroupPosition() {
		return mLastExpandedGroupPosition;
	}
	
	public void setLastExpandedGroupPosition(int position) {
		mLastExpandedGroupPosition = position;
	}
	
	private static class ViewHolder {
		private TextView mText;
	}

}
