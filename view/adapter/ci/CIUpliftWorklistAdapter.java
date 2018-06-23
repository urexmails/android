package com.contactpoint.view.adapter.ci;

import com.contactpoint.model.ModelFactory;

import android.content.Context;

public class CIUpliftWorklistAdapter extends CIWorklistAdapter {

	public CIUpliftWorklistAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return ModelFactory.getCIService().getUpliftForms().size();
	}

	@Override
	public Object getItem(int position) {
		return ModelFactory.getCIService().getUpliftForms().get(position);
	}

}
