package com.contactpoint.view.fragment.ci;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.view.SignatureView;

public class MemberSignatureFragment extends SuperCIFragment {

	public static final String TAG = "MemberSignatureFragment";
	private SignatureView mSignatureView;
	private RadioGroup mMemberAgentSignature;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.ci_signature_1, null);

		v.findViewById(R.id.btn_next).setOnClickListener(mNextListener);
		v.findViewById(R.id.btn_clear).setOnClickListener(mClearListener);

		mMemberAgentSignature = (RadioGroup) v.findViewById(R.id.Member_Agent_Signature);
		mSignatureView = (SignatureView) v.findViewById(R.id.form_signature_1);
		mMemberAgentSignature.check(R.id.Member_Agent_Signature_Member);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		// insert signatures
		if (checkSigned(UploadInDTO.Member, mSignatureView)) {
			mMemberAgentSignature.check(R.id.Member_Agent_Signature_Member);
			mMemberAgentSignature.setTag(UploadInDTO.Member);
			mSignatureView.setTag(UploadInDTO.Member);
		} else if (checkSigned(UploadInDTO.Agent, mSignatureView)) {
			mMemberAgentSignature.check(R.id.Member_Agent_Signature_Agent);
			mMemberAgentSignature.setTag(UploadInDTO.Agent);
			mSignatureView.setTag(UploadInDTO.Agent);
		}

		mMemberAgentSignature.setOnCheckedChangeListener(mChangeListener);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (mSignatureView != null) {
			if (hidden) {
				String signature = mSignatureView.saveAsBase64String();
				if (signature != null) {
					switch(mMemberAgentSignature.getCheckedRadioButtonId()) {
					case R.id.Member_Agent_Signature_Member:
						insertSignature(UploadInDTO.Member, signature);
						break;
					case R.id.Member_Agent_Signature_Agent:
						insertSignature(UploadInDTO.Agent, signature);
						break;
					default:
						insertSignature(UploadInDTO.Member, signature);
						break;	
					}
				}
			} else {
				setEnabledFromSignature(mMemberAgentSignature);
				setEnabledFromSignature(mSignatureView);
			}
		}
	}

	private OnClickListener mNextListener = new OnClickListener() {
		public void onClick(View arg0) {
			getFragmentActivity().showNextSignature();
		}
	};

	private OnClickListener mClearListener = new OnClickListener() {
		public void onClick(View arg0) {
			clearSignature();
			setEnabledFromSignature(mMemberAgentSignature);
			setEnabledFromSignature(mSignatureView);
		}
	};

	private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			clearSignature();
			switch (checkedId) {
				case R.id.Member_Agent_Signature_Member:
					mSignatureView.setTag(UploadInDTO.Member);
					mMemberAgentSignature.setTag(UploadInDTO.Member);
					break;
				case R.id.Member_Agent_Signature_Agent:
					mSignatureView.setTag(UploadInDTO.Agent);
					mMemberAgentSignature.setTag(UploadInDTO.Agent);
					break;
			}
		}
	};

	private void clearSignature() {
		mSignatureView.clear();
		removeSignature(UploadInDTO.Member);
		removeSignature(UploadInDTO.Agent);
	}
}
