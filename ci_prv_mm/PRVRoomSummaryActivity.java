package com.contactpoint.ci_prv_mm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.SummaryOutDTO;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.model.util.TollUncaughtExceptionHandler;
import com.contactpoint.view.adapter.prv.PRVReviewAddOnDetailAdapter;
import com.contactpoint.view.fragment.prv.PRVSummaryAddOnFragment;
import com.contactpoint.view.fragment.prv.PRVSummaryItemFragment;
import com.contactpoint.view.fragment.prv.PRVSummaryRoomFragment;

public class PRVRoomSummaryActivity extends SherlockFragmentActivity 
implements OnNaviSelectedListener {

	private PRVSummaryRoomFragment mRoomFragment;
	private PRVSummaryItemFragment mItemFragment;
	private PRVSummaryAddOnFragment mAddOnFragment;
	private TextView mRoom;
	private TextView mCubic;
	private TextView mCartons;
	private TextView mCovers;
	private TextView mCrates;
	private TextView mItemVol;
	private TextView mAddOnVol;
	private AlertDialog mAddOnDetailDialog;
	private PRVReviewAddOnDetailAdapter mDetailAdapter;
	private SummaryOutDTO mOutDTO;
	
	private Button mD2D;
	private Button mD2S;
	
	private PhotoController mPhotoController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prv_room_summary);

		// delete icon and title from action bar
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		if (savedInstanceState != null) {
			return;
		}
		setupFragments();

		mRoom = (TextView) findViewById(R.id.prv_review_total_room);
		mCubic = (TextView) findViewById(R.id.prv_review_total_cubic);
		mCartons = (TextView) findViewById(R.id.prv_review_total_cartons);
		mCovers = (TextView) findViewById(R.id.prv_review_total_covers);
		mCrates = (TextView) findViewById(R.id.prv_review_total_crates);
		mItemVol = (TextView) findViewById(R.id.prv_review_item_vol);
		mAddOnVol = (TextView) findViewById(R.id.prv_review_add_on_vol);

		ModelFactory.getPRVService().refreshSummaryOutDTO();
		mOutDTO = ModelFactory.getPRVService().getSummaryOutDTO();
		mRoom.setText("" + mOutDTO.getTotalRoom());
		mCubic.setText(ModelFactory.getUtilService().printFormat(mOutDTO.getTotalCubic()));
		mCartons.setText("" + mOutDTO.getTotalCartons());
		mCovers.setText("" + mOutDTO.getTotalCovers());
		mCrates.setText("" + mOutDTO.getTotalCrates());
		refresh();		
		
		// dialog
		mDetailAdapter = new PRVReviewAddOnDetailAdapter(this);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setAdapter(mDetailAdapter, null);
		mAddOnDetailDialog = builder.create();
		
		// d2d and d2s
		mD2D = (Button) findViewById(R.id.prv_btn_d2d);
		mD2S = (Button) findViewById(R.id.prv_btn_d2s);
		
		// set visibility of D2D & D2S buttons
		PRVForm form = ModelFactory.getPRVService().getCurrentForm();
		if (form.getDownload().size() > 1 && form.getD2DRoomList().size() > 0 && form.getD2SRoomList().size() > 0) {
			mD2D.setVisibility(View.VISIBLE);
			mD2D.setVisibility(View.VISIBLE);
			
			// set appearance
			if (!form.isD2D()) {
				mD2S.setTextAppearance(this, R.style.TollButton_Bold);				
			} else {
				mD2D.setTextAppearance(this, R.style.TollButton_Bold);				
			}
			
		} else {
			mD2D.setVisibility(View.GONE);
			mD2S.setVisibility(View.GONE);
		}
		
		mPhotoController = new PhotoController(this);
		
		Thread.setDefaultUncaughtExceptionHandler(new TollUncaughtExceptionHandler(this,
	            MainActivity.class));

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
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.prv_menu_comments, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		/*case R.id.menu_photo:
			PRVDownload download = ModelFactory.getPRVService().getCurrentForm().getDownload().firstElement();
			String poNumber = ModelFactory.getPRVService().getCombinedPONumber(download);
			mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.SUMMARY, poNumber);
			return true;*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == mPhotoController.PHOTO_REQUEST_CODE)
		{
			mPhotoController.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void onBackToItemListener(View v) {
		Intent i = new Intent(getApplication(), PRVItemCollectionActivity.class);
		startActivity(i);
	}

	public void onCompleteListener(View v) {
		if (!ModelFactory.getPRVService().validateItemCollection()) {
			AlertDialog ad = DialogFactory.getAlertDialog(this);
			ad.setTitle(getString(R.string.dialog_error));
			ad.setMessage(getString(R.string.item_invalid));
			ad.show();
		} else {
			Intent i = new Intent(getApplication(), PRVFragmentActivity.class);
			i.putExtra(PRVFragmentActivity.EXTRA, PRVFragmentActivity.RES_ACCESS);
			startActivity(i);
		}
	}

	public void viewCartonDetails(View v) {
		if (mOutDTO.getTotalCartons() == 0) return;
		mDetailAdapter.setItems(ModelFactory.getPRVService().mapToNameValuePair(mOutDTO.getCartonList()));
		mAddOnDetailDialog.show();
	}

	public void viewCoverDetails(View v) {
		if (mOutDTO.getTotalCovers() == 0) return;		
		mDetailAdapter.setItems(ModelFactory.getPRVService().mapToNameValuePair(mOutDTO.getCoverList()));
		mAddOnDetailDialog.show();
	}

	public void viewCrateDetails(View v) {
		if (mOutDTO.getTotalCrates() == 0) return;		
		mDetailAdapter.setItems(ModelFactory.getPRVService().mapToNameValuePair(mOutDTO.getCrateList()));
		mAddOnDetailDialog.show();
	}

	private void setupFragments() {
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		mRoomFragment = (PRVSummaryRoomFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVSummaryRoomFragment.TAG);
		if (mRoomFragment == null) {
			mRoomFragment = new PRVSummaryRoomFragment();
			ft.add(R.id.prv_sum_room_container, mRoomFragment, PRVSummaryRoomFragment.TAG);
		}

		mItemFragment = (PRVSummaryItemFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVSummaryItemFragment.TAG);
		if (mItemFragment == null) {
			mItemFragment = new PRVSummaryItemFragment();
			ft.add(R.id.prv_sum_item_container, mItemFragment, PRVSummaryItemFragment.TAG);
		}

		mAddOnFragment = (PRVSummaryAddOnFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVSummaryAddOnFragment.TAG);
		if (mAddOnFragment == null) {
			mAddOnFragment = new PRVSummaryAddOnFragment();
			ft.add(R.id.prv_sum_add_on_container, mAddOnFragment, PRVSummaryAddOnFragment.TAG);
		}
		ft.commit();
	}

	@Override
	public void onNaviSelected(int position) {
		ModelFactory.getPRVService().setCurrentRoom(position);
		mItemFragment.refresh();
		mAddOnFragment.refresh();
		refresh();
	}

	private void refresh() {
		mItemVol.setText(
				ModelFactory.getUtilService().printFormat(
						ModelFactory.getPRVService().getRoomItemVolume(
								ModelFactory.getPRVService().getCurrentRoom()))
								+ " " + getResources().getString(R.string.m3));
		mAddOnVol.setText(
				ModelFactory.getUtilService().printFormat(
						ModelFactory.getPRVService().getRoomAddOnVolume(
								ModelFactory.getPRVService().getCurrentRoom()))
								+ " " + getResources().getString(R.string.m3));
	}
	
	public void isD2D(View v) {
		ModelFactory.getPRVService().setServiceModeD2D(true);
		ModelFactory.getPRVService().initPRVInDTO();
		
		v.setEnabled(false);
		((Button)v).setTextAppearance(this, R.style.TollButton_Bold);
		findViewById(R.id.prv_btn_d2s).setEnabled(true);
		((Button)findViewById(R.id.prv_btn_d2s)).setTextAppearance(this, R.style.TollButton);
		
		ModelFactory.getPRVService().refreshSummaryOutDTO();
		mOutDTO = ModelFactory.getPRVService().getSummaryOutDTO();
		mRoom.setText("" + mOutDTO.getTotalRoom());
		mCubic.setText(ModelFactory.getUtilService().printFormat(mOutDTO.getTotalCubic()));
		mCartons.setText("" + mOutDTO.getTotalCartons());
		mCovers.setText("" + mOutDTO.getTotalCovers());
		mCrates.setText("" + mOutDTO.getTotalCrates());
		
		mRoomFragment.refresh();
		mItemFragment.refresh();
		mAddOnFragment.refresh();
		refresh();
	}
	
	public void isD2S(View v) {
		ModelFactory.getPRVService().setServiceModeD2D(false);
		ModelFactory.getPRVService().initPRVInDTO();
		
		v.setEnabled(false);
		((Button)v).setTextAppearance(this, R.style.TollButton_Bold);
		findViewById(R.id.prv_btn_d2d).setEnabled(true);
		((Button)findViewById(R.id.prv_btn_d2d)).setTextAppearance(this, R.style.TollButton);
	
		ModelFactory.getPRVService().refreshSummaryOutDTO();
		mOutDTO = ModelFactory.getPRVService().getSummaryOutDTO();
		mRoom.setText("" + mOutDTO.getTotalRoom());
		mCubic.setText(ModelFactory.getUtilService().printFormat(mOutDTO.getTotalCubic()));
		mCartons.setText("" + mOutDTO.getTotalCartons());
		mCovers.setText("" + mOutDTO.getTotalCovers());
		mCrates.setText("" + mOutDTO.getTotalCrates());
		
		mRoomFragment.refresh();
		mItemFragment.refresh();
		mAddOnFragment.refresh();
		refresh();
	}
}
