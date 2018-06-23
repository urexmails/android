package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.view.SignatureView;

public class DHRMSignatureFragment extends SuperCIFragment {

	public static final String TAG = "DHRMSignatureFragment";
	private SignatureView mSignatureView;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_signature_2, null);
		
		v.findViewById(R.id.btn_prev).setOnClickListener(mPrevListener);
		v.findViewById(R.id.btn_next).setOnClickListener(mNextListener);
		v.findViewById(R.id.btn_clear).setOnClickListener(mClearListener);
		mSignatureView = (SignatureView) v.findViewById(R.id.form_signature_1);
		mSignatureView.setTag(UploadInDTO.DHRM);

		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		// insert signatures
		checkSigned(UploadInDTO.DHRM, mSignatureView);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (mSignatureView != null) {	// if called before onCreateView
			if (hidden) {
				String signature = mSignatureView.saveAsBase64String();
				if (signature != null) {
					insertSignature(UploadInDTO.DHRM, signature);
				}
			} else {
				setEnabledFromSignature(mSignatureView);
			}
		}
	}
	
	private OnClickListener mPrevListener = new OnClickListener() {
		public void onClick(View arg0) {
			getFragmentActivity().showPrevSignature();
		}
	};
	
	private OnClickListener mNextListener = new OnClickListener() {
		public void onClick(View arg0) {
			getFragmentActivity().showNextSignature();
		}
	};
	
	private OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View arg0) {
			mSignatureView.clear();
			removeSignature(UploadInDTO.DHRM);
			setEnabledFromSignature(mSignatureView);
		}
	};
}
