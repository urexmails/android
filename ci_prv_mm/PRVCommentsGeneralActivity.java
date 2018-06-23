package com.contactpoint.ci_prv_mm;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;

public class PRVCommentsGeneralActivity extends PRVActivity {

	private EditText mGeneral;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prv_comments_general);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		mGeneral = (EditText)findViewById(R.id.prv_general_comment_txt);
		mGeneral.addTextChangedListener(mGeneralWatcher);
		mGeneral.setText(ModelFactory.getPRVService().getCurrentForm().getQuestionAnswer()
				.get(UploadPRVInDTO.P_GENERAL_COMMENTS));
	}
	
	private TextWatcher mGeneralWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			ModelFactory.getPRVService().putQuestionAnswer(
					PRVCommentsGeneralActivity.this, 
					UploadPRVInDTO.P_GENERAL_COMMENTS, 
					arg0.toString());
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};
	
	public void onBack(View v) {
		finish();
	}
	
}
