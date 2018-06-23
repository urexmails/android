package com.contactpoint.ci_prv_mm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.util.TollUncaughtExceptionHandler;

public abstract class PRVActivity extends SherlockActivity {

	private PhotoController mPhotoController;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPhotoController = new PhotoController(this);
		Thread.setDefaultUncaughtExceptionHandler(new TollUncaughtExceptionHandler(this,
	            MainActivity.class));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (this instanceof PRVItemCollectionActivity) {
			getSupportMenuInflater().inflate(R.menu.prv_menu, menu);
		} else if (this instanceof PRVCommentsInternalActivity || this instanceof PRVCommentsGeneralActivity) {
			getSupportMenuInflater().inflate(R.menu.prv_menu_comments, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ModelFactory.getPRVService().saveFormData(this);
		switch (item.getItemId()) {
		case R.id.prv_menu_service_details:
			Intent i = new Intent(getApplicationContext(), PRVFragmentActivity.class);
			i.putExtra(PRVFragmentActivity.EXTRA, PRVFragmentActivity.SERV_DETAIL);
			startActivity(i);
			return true;
		case R.id.prv_menu_general_comments:
			i = new Intent(getApplicationContext(), PRVCommentsGeneralActivity.class);
			startActivity(i);
			return true;
		case R.id.prv_menu_internal_comments:
			i = new Intent(getApplicationContext(), PRVCommentsInternalActivity.class);
			startActivity(i);
			return true;
		case R.id.menu_photo:
			PRVDownload download = ModelFactory.getPRVService().getCurrentForm().getDownload().firstElement();
			String poNumber = ModelFactory.getPRVService().getCombinedPONumber(download);
			if (this instanceof PRVCommentsGeneralActivity) {
				//mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.GENERAL, poNumber);
			} else if (this instanceof PRVCommentsInternalActivity) {
				//mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.INTERNAL, poNumber);
			} else if (this instanceof PRVItemCollectionActivity) {
				boolean isMM = ModelFactory.getPRVService().getCurrentForm().isMM();
				mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.COLLECTION, poNumber, isMM);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// http://stackoverflow.com/questions/4005728/hide-default-keyboard-on-click-in-android
		if(getCurrentFocus() != null && getCurrentFocus() instanceof EditText){
	        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	    }
	    
	    return super.dispatchTouchEvent(ev);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == mPhotoController.PHOTO_REQUEST_CODE)
		{
			mPhotoController.onActivityResult(requestCode, resultCode, data);
		}
	}
}
