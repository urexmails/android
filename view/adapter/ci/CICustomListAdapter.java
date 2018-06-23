package com.contactpoint.view.adapter.ci;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;

// class used in CINaviFragment as custom list adapter
public class CICustomListAdapter extends ArrayAdapter<String>{

	private ArrayList<String> navis;
	private boolean[] isValid;
	public CICustomListAdapter(Context context, int viewId, ArrayList<String> navis) {
		super(context, viewId, navis);
		this.navis = navis;
		isValid = new boolean[navis.size()];
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.ci_custom_list_fragment, null);
		}
		
		TextView text = (TextView) v.findViewById(R.id.list_item);
		text.setTextColor(getContext().getResources().getColor(R.color.black));
		text.setText(navis.get(position));
		text.setBackgroundResource(R.drawable.nav_item_bg);
		
		// change the view if valid
		if (isValid[position]) {
			text.setTypeface(null, Typeface.BOLD);
			Drawable tick = getContext().getResources().getDrawable(R.drawable.black_tick);
			float density = getContext().getResources().getDisplayMetrics().density;
			int left = (int) (10 * density);
			int right = (int) (40 * density);
			int bottom = (int) (40 * density);
			int top = (int) (0 * density);
			//tick.setBounds(20, 0, 60, 70);
			tick.setBounds(left, top, right, bottom);
			text.setCompoundDrawables(tick, null, null, null);
			text.setCompoundDrawablePadding(0);
		} else {
			text.setTypeface(null, Typeface.NORMAL);
			text.setCompoundDrawables(null, null, null, null);
		}
				
		return v;
	}
	
	public void setValidAtPosition(int position, boolean valid) {
		isValid[position] = valid;
	}
	
	public boolean allCIIsValid() {
		boolean valid = true;
		for (int i = 1; i < isValid.length && valid; i++) {
			valid = isValid[i] && valid;
		}
		return valid;
	}
	
	public boolean ciSignable() {
		boolean valid = true;
		for (int i = 1; i < isValid.length - 2 && valid; i++) {
			valid = isValid[i] && valid;
		}
		return valid;
	}
	
	public boolean allPRVIsValid() {
		return isValid[2] && isValid[4];
	}
}
