package com.contactpoint.view.adapter.ci;

import com.contactpoint.model.ModelFactory;

import android.content.Context;

public class CIDeliverySearchResultAdapter extends CISearchResultAdapter {

	public CIDeliverySearchResultAdapter(Context context) {
		super(context);
	}

	@Override
	public int getCount() {
		return ModelFactory.getCIService().getDeliveryResults().size();
	}

	@Override
	public Object getItem(int position) {
		return ModelFactory.getCIService().getDeliveryResults().get(position);
	}

}
