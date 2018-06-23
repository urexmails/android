package com.contactpoint.view.adapter.ci;

import android.content.Context;

import com.contactpoint.model.ModelFactory;

public class CIUpliftSearchResultAdapter extends CISearchResultAdapter {
	
	public CIUpliftSearchResultAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return ModelFactory.getCIService().getUpliftResults().size();
	}

	@Override
	public Object getItem(int position) {
		return ModelFactory.getCIService().getUpliftResults().get(position);
	}

}
