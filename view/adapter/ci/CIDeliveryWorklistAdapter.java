package com.contactpoint.view.adapter.ci;

import android.content.Context;

import com.contactpoint.model.ModelFactory;

public class CIDeliveryWorklistAdapter extends CIWorklistAdapter {

	public CIDeliveryWorklistAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return ModelFactory.getCIService().getDeliveryForms().size();
	}

	@Override
	public Object getItem(int position) {
		return ModelFactory.getCIService().getDeliveryForms().get(position);
	}

}
