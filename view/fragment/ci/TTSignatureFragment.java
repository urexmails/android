package com.contactpoint.view.fragment.ci;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.view.SignatureView;

public class TTSignatureFragment extends SuperCIFragment {

	public static final String TAG = "TTSignatureFragment";
	private SignatureView mSignatureView;
	private ProgressDialog pDialog;
	private MyLocation mLocationTracker;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_signature_3, null);
		
		v.findViewById(R.id.btn_prev).setOnClickListener(mPrevListener);
		v.findViewById(R.id.btn_finalise).setOnClickListener(mFinaliseListener);
		v.findViewById(R.id.btn_clear).setOnClickListener(mClearListener);
		v.findViewById(R.id.btn_save).setOnClickListener(mSaveListener);
		mSignatureView = (SignatureView) v.findViewById(R.id.form_signature_1);
		mSignatureView.setTag(UploadInDTO.TT);

		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		
		// insert signatures
		checkSigned(UploadInDTO.TT, mSignatureView);
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (mSignatureView != null) { // if called before onCreateView
			if (hidden) {
				saveSignature();
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
	
	private OnClickListener mFinaliseListener = new OnClickListener() {
		public void onClick(View arg0) {
			saveSignature();
			
			if (getFragmentActivity().isAllValid()) {
			//if (getFragmentActivity().getUploadInDTO().isValid()) {	
				getFragmentActivity().getUploadInDTO().setIsValid(true);
				ModelFactory.getPhotoService().finalisePhoto(ModelFactory.getCIService().getCurrentForm().getDownload().poNumber);
				ModelFactory.getCIService().saveFormData(getFragmentActivity());
				
				pDialog = ProgressDialog.show(getSherlockActivity(), "", getString(R.string.loading_msg));
				mLocationTracker = new MyLocation();
				mLocationTracker.getLocation(getSherlockActivity(), finalisedCILocationResult);
			} else {
				AlertDialog ad = DialogFactory.getAlertDialog(getFragmentActivity());
				ad.setTitle(getString(R.string.dialog_error));
				ad.setMessage(getString(R.string.form_invalid));
				ad.show();
			}
		}	
	};
	
	private OnClickListener mSaveListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// detect signatures			
			saveSignature();
			ModelFactory.getCIService().saveFormData(getFragmentActivity());
			AlertDialog ad = DialogFactory.getAlertDialog(getFragmentActivity());
			ad.setMessage(getResources().getString(R.string.dialog_save));
			ad.show();
		}
		
	};
	
	private OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View arg0) {
			mSignatureView.clear();
			removeSignature(UploadInDTO.TT);
			setEnabledFromSignature(mSignatureView);
		}
	};	
	
	private LocationResult finalisedCILocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			pDialog.dismiss();
			ModelFactory.getCIService().onFinalised();
			Intent i = new Intent(getFragmentActivity().getApplication(), 
					MainFragmentActivity.class);
			i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.WORKLIST);
			i.putExtra(MainFragmentActivity.EXTRA_UPLOAD, true);
			startActivity(i);

		}
	};
	
	private void saveSignature() {
		String signature = mSignatureView.saveAsBase64String();
		if (signature != null) {
			insertSignature(UploadInDTO.TT, signature);
		}
	}
}
