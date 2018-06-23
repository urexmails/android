package com.contactpoint.ci_prv_mm;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.UploadPRVInDTO;
import com.contactpoint.model.DTO.prv.validator.UploadPRVInDTOValidator;
import com.contactpoint.model.client.prv.PRVDownload;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.util.DialogFactory;
import com.contactpoint.model.util.Exchanger;
import com.contactpoint.model.util.MyLocation;
import com.contactpoint.model.util.MyLocation.LocationResult;
import com.contactpoint.view.adapter.ci.CICustomListAdapter;
import com.contactpoint.view.fragment.nav.PRVNaviFragment;
import com.contactpoint.view.fragment.prv.PRVAccessFragment;
import com.contactpoint.view.fragment.prv.PRVChecklistFragment;
import com.contactpoint.view.fragment.prv.PRVCustomerDetailsFragment;
import com.contactpoint.view.fragment.prv.PRVServiceDetailsFragment;
import com.google.android.maps.MapView;

public class PRVFragmentActivity extends SuperFragmentActivity {

	public static final String EXTRA = "SELECTED";
	private PRVNaviFragment mPRVNaviFragment;
	private PRVCustomerDetailsFragment mCustomerDetailsFragment;
	private PRVServiceDetailsFragment mServiceDetailsFragment;
	private PRVChecklistFragment mChecklistFragment;
	private PRVAccessFragment mAccessFragment;
	private ProgressDialog pDialog;
	private MyLocation mLocationTracker;

	private AlertDialog mDateDiffersDialog;
	private RadioGroup mReasonGroup;
	private EditText mOtherReasonText;
	
	private int mPosition;
	
	private PhotoController mPhotoController;
	
	private final Handler mHandler = new Handler();
	private final Runnable mTriggered = new Runnable() {
		public void run() {
			onUploadTriggered();
		}
	};

	public static final int MAIN_MENU = 0;
	public static final int CUST_DETAIL = 1;
	public static final int SERV_DETAIL = 2;
	public static final int ITEM_COL = 3;
	public static final int RES_ACCESS = 4;
	public static final int PRV_CHECKLIST = 5;
	public static final int FINALISE = 6;
	
	private static final int MINIMUM_REASON_LENGTH = 3;
	private static final int MINIMUM_DATE_DIFFER_DIALOG_WIDTH = 800;
	
	public static final String EXTRA_ITEM_COMPLETE = "COMPLETED";
	
	private boolean mIsTakingPicture;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		

		// We instantiate the MapView here, it's really important!
		Exchanger.mMapView = new MapView(this, MAP_KEY);

		// We manually show the list Fragment.
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(EXTRA)) {
			mPRVNaviFragment.setArguments(extras);
		}

		createDateDiffersDialog();

		mPhotoController = new PhotoController(this);
		mIsTakingPicture = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.prv_menu, menu);
		
		MenuItem photo = menu.findItem(R.id.menu_photo);
		
		// uplift / delivery
		photo.setVisible(mPosition == RES_ACCESS);

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
		case R.id.menu_photo:
			PRVDownload download = ModelFactory.getPRVService().getCurrentForm().getDownload().firstElement();
			String poNumber = ModelFactory.getPRVService().getCombinedPONumber(download);
			mIsTakingPicture = true;
			boolean isMM = ModelFactory.getPRVService().getCurrentForm().isMM();
			mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.ACCESS, poNumber, isMM);
			return true;
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
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mIsTakingPicture) {
			mPRVNaviFragment.getListView().setItemChecked(RES_ACCESS, true);
		}
		mIsTakingPicture = false;
	}

	@Override
	public void onNaviSelected(int position) {
		ModelFactory.getPRVService().saveFormData(this);
		mPosition = position;
		
		switch (position) {
		case MAIN_MENU:
			Intent i = new Intent(getApplication(), DashboardActivity.class);
			startActivity(i);
			break;
		case CUST_DETAIL: showFragment(mCustomerDetailsFragment); break;
		case SERV_DETAIL: showFragment(mServiceDetailsFragment); break;
		case ITEM_COL: 
			i = new Intent(getApplication(), PRVItemCollectionActivity.class);
			startActivity(i);
			break;
		case RES_ACCESS: showFragment(mAccessFragment); break;
		case PRV_CHECKLIST: showFragment(mChecklistFragment); break;
		case FINALISE: 
			pDialog = ProgressDialog.show(this, "", getString(R.string.loading_msg));
			mLocationTracker = new MyLocation();
			mLocationTracker.getLocation(this, finalisedPRVLocationResult);
			break;
		default: break;
		}
		
		invalidateOptionsMenu();
	}

	@Override
	protected void setupFragments() {
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		// If the activity is killed while in BG, it's possible that the
		// fragment still remains in the FragmentManager, so, we don't need to
		// add it again.

		mPRVNaviFragment = (PRVNaviFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVNaviFragment.TAG);
		if (mPRVNaviFragment == null) {
			mPRVNaviFragment = new PRVNaviFragment();
			ft.add(R.id.left_container, mPRVNaviFragment, PRVNaviFragment.TAG);
		}

		mCustomerDetailsFragment = (PRVCustomerDetailsFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVCustomerDetailsFragment.TAG);
		if (mCustomerDetailsFragment == null) {
			mCustomerDetailsFragment = new PRVCustomerDetailsFragment();
			ft.add(R.id.right_container, mCustomerDetailsFragment, 
					PRVCustomerDetailsFragment.TAG);
		}
		ft.hide(mCustomerDetailsFragment);

		mServiceDetailsFragment = (PRVServiceDetailsFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVServiceDetailsFragment.TAG);
		if (mServiceDetailsFragment == null) {
			mServiceDetailsFragment = new PRVServiceDetailsFragment();
			ft.add(R.id.right_container, mServiceDetailsFragment, 
					PRVServiceDetailsFragment.TAG);
		}
		ft.hide(mServiceDetailsFragment);

		mChecklistFragment = (PRVChecklistFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVChecklistFragment.TAG);
		if (mChecklistFragment == null) {
			mChecklistFragment = new PRVChecklistFragment();
			ft.add(R.id.right_container, mChecklistFragment, 
					PRVChecklistFragment.TAG);
		}
		ft.hide(mChecklistFragment);

		mAccessFragment = (PRVAccessFragment) getSupportFragmentManager()
				.findFragmentByTag(PRVAccessFragment.TAG);
		if (mAccessFragment == null) {
			mAccessFragment = new PRVAccessFragment();
			ft.add(R.id.right_container, mAccessFragment, 
					PRVAccessFragment.TAG);
		}
		ft.hide(mAccessFragment);

		ft.commit();
	}

	public void onUploadTriggered() {
		// if everything is valid, set the form as ready for submit
		
		if (!((CICustomListAdapter)mPRVNaviFragment.getListAdapter()).allPRVIsValid()) {
			AlertDialog ad = DialogFactory.getAlertDialog(this);
			ad.setTitle(getString(R.string.dialog_error));
			ad.setMessage(getString(R.string.prv_invalid));
			ad.show();
			return;
		}
		
		if (ModelFactory.getPRVService().completeDateDiffers()) {
			mDateDiffersDialog.show();
			// width 
			WindowManager.LayoutParams lp = mDateDiffersDialog.getWindow().getAttributes();
			lp.width = MINIMUM_DATE_DIFFER_DIALOG_WIDTH;
			mDateDiffersDialog.getWindow().setAttributes(lp);
			
			// enable/disable button
			mDateDiffersDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			return;
		} 
		
		
				
		/*
		if (ModelFactory.getPRVService().getSummaryOutDTO().getTotalCubic() == 0) {
			AlertDialog ad = DialogFactory.getAlertDialog(this);
			ad.setTitle(getString(R.string.dialog_warning));
			ad.setMessage(getString(R.string.prv_zero_cubic));
			ad.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.btn_ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					uploadPRV();
				}
			});
			ad.show();
			return;
		}*/

		PRVDownload download = ModelFactory.getPRVService().getCurrentForm().getDownload().firstElement();
		String poNumber = ModelFactory.getPRVService().getCombinedPONumber(download);
		ModelFactory.getPhotoService().finalisePhoto(poNumber);
		ModelFactory.getPRVService().getCurrentForm().setReady(true);
		ModelFactory.getPRVService().getCurrentForm().setOrder(0);
		ModelFactory.getPRVService().saveFormData(this);
		Intent i = new Intent(this, MainFragmentActivity.class);
		i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.WORKLIST);
		i.putExtra(MainFragmentActivity.EXTRA_UPLOAD, true);
		startActivity(i);
		return;
	}
	/*
	private void uploadPRV() {
		ModelFactory.getPRVService().getCurrentForm().setReady(true);
		ModelFactory.getPRVService().getCurrentForm().setOrder(0);
		ModelFactory.getPRVService().saveFormData(this);
		Intent i = new Intent(this, MainFragmentActivity.class);
		i.putExtra(MainFragmentActivity.EXTRA, MainFragmentActivity.WORKLIST);
		i.putExtra(MainFragmentActivity.EXTRA_UPLOAD, true);
		startActivity(i);
	}*/

	/*
	private OnCheckedChangeListener mDifferListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId != -1) {
				mReasonGroup2.setOnCheckedChangeListener(null);
				mReasonGroup2.clearCheck();
				mReasonGroup2.setOnCheckedChangeListener(mListener2);	
//				mOtherReasonText.setEnabled(false);				
			}
		}

	};

	private OnCheckedChangeListener mListener2 = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (checkedId != -1) {
				mReasonGroup1.setOnCheckedChangeListener(null);
				mReasonGroup1.clearCheck();
				mReasonGroup1.setOnCheckedChangeListener(mListener1);				
//				mOtherReasonText.setEnabled(true);
			}
		}

	};*/

	private TextWatcher mOtherWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			mDateDiffersDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(arg0.toString().length() >= MINIMUM_REASON_LENGTH);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	};

	private OnClickListener mDiffersListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			String qCode = "";
			String ans = PRVService.YES;
			switch(which) {
			case AlertDialog.BUTTON_POSITIVE:
				switch (mReasonGroup.getCheckedRadioButtonId()) {
				case R.id.prv_differs_rescheduled:
					qCode = UploadPRVInDTO.P_DD_IS_TRANSF_RESCHEDULE;
					break;
				case R.id.prv_differs_not_home:
					qCode = UploadPRVInDTO.P_DD_TRANSF_UNAVAILABLE;
					break;
				case R.id.prv_differs_incorrect:
					qCode = UploadPRVInDTO.P_DD_INCORRECT_BOOKING;
					break;
				case R.id.prv_differs_other:
					qCode = UploadPRVInDTO.P_DD_OTHER;
					break;
				}
				
				ModelFactory.getPRVService().putQuestionAnswer(
						PRVFragmentActivity.this, 
						UploadPRVInDTO.P_DATE_DIFFERS, 
						PRVService.YES);
				ModelFactory.getPRVService().putQuestionAnswer(PRVFragmentActivity.this, 
						qCode, ans);
				
				//if (mOtherReasonText.getText().length() != 0) {
					ModelFactory.getPRVService().putQuestionAnswer(PRVFragmentActivity.this, 
							UploadPRVInDTO.P_DD_COMMENTS, mOtherReasonText.getText().toString());
					onUploadTriggered();
				/*	
				} else {
					AlertDialog ad = DialogFactory.getAlertDialog(PRVFragmentActivity.this);
					ad.setMessage(getResources().getString(R.string.date_differ_invalid));
					ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							mDateDiffersDialog.show();
						}
					});
					ad.show();
				}*/
				
				break;
			}
		}

	};

	private void createDateDiffersDialog() {
		AlertDialog.Builder builder;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.prv_dialog_date_differs, null);

		mReasonGroup = (RadioGroup) layout.findViewById(R.id.prv_differs_reason);
		//mReasonGroup2 = (RadioGroup) layout.findViewById(R.id.prv_differs_other_reason);
		//mReasonGroup.setOnCheckedChangeListener(mDifferListener);				
		//mReasonGroup2.setOnCheckedChangeListener(mListener2);	
		mOtherReasonText = (EditText)layout.findViewById(R.id.prv_differs_other_text);
		mOtherReasonText.addTextChangedListener(mOtherWatcher);

		builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
		builder.setView(layout);
		
		layout.setOnTouchListener(CommonController.OnDialogTouchListener);
		mDateDiffersDialog = builder.create();

		mDateDiffersDialog.setTitle(getResources().getString(R.string.prv_differs_title));
		mDateDiffersDialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_ok), mDiffersListener);
		mDateDiffersDialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), mDiffersListener);
	}

	public void validateForm(int naviItem) {
		switch(naviItem) {
		case PRV_CHECKLIST:
			ModelFactory.getPRVService().getCurrentForm().setChecklistCompleted(
					UploadPRVInDTOValidator.validateCheckList(getApplicationContext(), 
							ModelFactory.getPRVService().getCurrentForm().getQuestionAnswer()));
			mPRVNaviFragment.setNaviCompletedIndicator(PRV_CHECKLIST, 
					ModelFactory.getPRVService().getCurrentForm().checklistCompleted());
			break;
		default: break;
		}
	}
	
	public OnCheckedChangeListener PRVRadioButtonListener = 
			new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			ModelFactory.getPRVService().putQuestionAnswer(
					group.getContext(),
					group.getTag().toString(), 
					checkedId);
			validateForm(PRV_CHECKLIST);
		}
	};
	
	public CompoundButton.OnCheckedChangeListener PRVCheckBoxListener = 
			new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			ModelFactory.getPRVService().putQuestionAnswer(
					arg0.getContext(),
					arg0.getTag().toString(), 
					arg1);
		}
	};	
	
	private LocationResult finalisedPRVLocationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			ModelFactory.getUtilService().updateLocationInDTO(location);
			pDialog.dismiss();
			ModelFactory.getPRVService().onFinalised();
			//onUploadTriggered();
			mHandler.post(mTriggered);
		}
	};


}
