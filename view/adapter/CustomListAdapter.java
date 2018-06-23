package com.contactpoint.view.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.contactpoint.ci_prv_mm.R;

public class CustomListAdapter extends ArrayAdapter<Integer> {

	private ArrayList<Integer> navis;
	public CustomListAdapter(Context context, int imageViewId, ArrayList<Integer> navis) {
		super(context, imageViewId, navis);
		this.navis = navis;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater)this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.custom_list_fragment, null);
		}
		
		ImageView img = (ImageView) v.findViewById(R.id.list_item);
		img.setImageResource(navis.get(position));

		return v;
	}
}
