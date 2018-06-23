package com.contactpoint.ci_prv_mm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.PhotoController;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.UploadInDTO;
import com.contactpoint.model.DTO.ci.validator.UploadInDTOValidator;
import com.contactpoint.model.client.CIForm;
import com.contactpoint.model.util.Exchanger;
import com.contactpoint.view.fragment.ci.CustomerDetailsFragment;
import com.contactpoint.view.fragment.ci.DHRMSignatureFragment;
import com.contactpoint.view.fragment.ci.DeliveryFragment;
import com.contactpoint.view.fragment.ci.MemberRatingFragment;
import com.contactpoint.view.fragment.ci.MemberSignatureFragment;
import com.contactpoint.view.fragment.ci.ProviderServiceFragment;
import com.contactpoint.view.fragment.ci.ProviderServiceSecondFragment;
import com.contactpoint.view.fragment.ci.ProviderServiceThirdFragment;
import com.contactpoint.view.fragment.ci.ServiceDetailsFragment;
import com.contactpoint.view.fragment.ci.TTSignatureFragment;
import com.contactpoint.view.fragment.ci.TollTransitionsFragment;
import com.contactpoint.view.fragment.ci.UpliftFragment;
import com.contactpoint.view.fragment.ci.VolumeFragment;
import com.contactpoint.view.fragment.nav.CINaviFragment;
import com.google.android.maps.MapView;

/**
 * 
 * @author MyPC
 * Ref: https://xrigau.wordpress.com/2012/03/22/howto-actionbarsherlock-mapfragment-listfragment/
 */

public class CIFragmentActivity extends SuperFragmentActivity {

	public static final String EXTRA 			= "SELECTED";
	public static final String CIDOWNLOAD		= "CIDownload";
	public static final int SERVICE_DETAIL 		= 2;
	public static final int PROVIDER_SERVICE 	= 3;
	public static final int GENERIC 			= 4;
	public static final int MEMBER_RATING 		= 5;
	public static final int TOLL_RATING 		= 6;
	public static final int VOLUME 				= 7;
	public static final int SIGNATURE			= 8;
	public static final int UPLIFT				= 0;
	public static final int DELIVERY			= 1;

	private CINaviFragment mCINaviFragment;

	private CustomerDetailsFragment mCustomerDetailsFragment;
	private ServiceDetailsFragment mServiceDetailsFragment;
	private ProviderServiceFragment mProviderServiceFragment;
	private ProviderServiceSecondFragment mProviderServiceSecondFragment;
	private ProviderServiceThirdFragment mProviderServiceThirdFragment;
	private UpliftFragment mUpliftFragment;
	private DeliveryFragment mDeliveryFragment;
	private TollTransitionsFragment mTollFragment;
	private MemberRatingFragment mRatingFragment;
	private VolumeFragment mVolumeFragment;
	private MemberSignatureFragment mMemberSignatureFragment;
	private DHRMSignatureFragment mDHRMSignatureFragment;
	private TTSignatureFragment mTTSignatureFragment;

	private UploadInDTO mUploadInDTO;
	private int mFragmentType;
	private int mPosition;
	
	private PhotoController mPhotoController;
	private boolean mIsTakingPicture;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		Intent i = getIntent();
		ModelFactory.getCIService().setCurrentForm((CIForm)i.getExtras().get(CIDOWNLOAD));
		mUploadInDTO = ModelFactory.getCIService().getUploadInDTO();

		if (ModelFactory.getCIService().getCurrentForm().isUplift()) {
			mFragmentType = UPLIFT;
		} else if (ModelFactory.getCIService().getCurrentForm().isDelivery()) {
			mFragmentType = DELIVERY;
		}
			
		super.onCreate(savedInstanceState);
		
		// We instantiate the MapView here, it's really important!
		Exchanger.mMapView = new MapView(this, MAP_KEY);
		
		// We manually show the list Fragment.
		onNaviSelected(1);
		
		mPhotoController = new PhotoController(this);
		mIsTakingPicture = false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == mPhotoController.PHOTO_REQUEST_CODE)
		{
			mPhotoController.onActivityResult(requestCode, resultCode, data);
			mCINaviFragment.getListView().setItemChecked(4, true);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mIsTakingPicture) {
			mCINaviFragment.getListView().setItemChecked(4, true);
		}
		mIsTakingPicture = false;
	}

	public void onNaviSelected(int position) {
		// The user selected the headline of an article from the HeadlinesFragment
		
		//ModelFactory.getCIService().saveFormData(this);
		mPosition = position;

		switch (position) {
		case 0:
			Intent i = new Intent(getApplication(), DashboardActivity.class);
			startActivity(i);
			break;
		case 1:
			showFragment(mCustomerDetailsFragment);
			break;
		case 2:
			showFragment(mServiceDetailsFragment);
			break;
		case 3:
			showFragment(mProviderServiceFragment);
			break;
		case 4:
			if (mFragmentType == UPLIFT) {
				showFragment(mUpliftFragment);
			} else if (mFragmentType == DELIVERY) {
				showFragment(mDeliveryFragment);
			}
			break;
		case 5:
			showFragment(mRatingFragment);
			break;
		case 6:
			showFragment(mTollFragment);
			break;
		case 7:
			showFragment(mVolumeFragment);
			break;
		case 8:
			showFragment(mMemberSignatureFragment);
			break;
		default:
			break;
		}
		
		invalidateOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getSupportMenuInflater().inflate(R.menu.version_only_menu, menu);
		getSupportMenuInflater().inflate(R.menu.ci_photo_menu, menu);
		MenuItem photo = menu.findItem(R.id.menu_photo);
		
		// uplift / delivery
		photo.setVisible(mPosition == 4);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_photo:
			String poNumber = ModelFactory.getCIService().getCurrentForm().getDownload().poNumber;
			mIsTakingPicture = true;
			boolean isMM = ModelFactory.getCIService().getCurrentForm().isMM();
			if (mFragmentType == UPLIFT) {
				mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.UPLIFT, poNumber, isMM);
			} else if (mFragmentType == DELIVERY) {
				mPhotoController.onPhotoMenuSelected(PhotoController.PhotoCategory.DELIVERY, poNumber, isMM);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * This method does the setting up of the Fragments. It basically checks if
	 * the fragments exist and if they do, we'll hide them. If the fragments
	 * don't exist, we create them, add them to the FragmentManager and hide
	 * them.
	 */
	@Override
	protected void setupFragments() {
		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		// If the activity is killed while in BG, it's possible that the
		// fragment still remains in the FragmentManager, so, we don't need to
		// add it again.

		mCINaviFragment = (CINaviFragment) getSupportFragmentManager()
				.findFragmentByTag(CINaviFragment.TAG);
		if (mCINaviFragment == null) {
			mCINaviFragment = new CINaviFragment();
			ft.add(R.id.left_container, mCINaviFragment, CINaviFragment.TAG);
		}

		mCustomerDetailsFragment = (CustomerDetailsFragment) getSupportFragmentManager()
				.findFragmentByTag(CustomerDetailsFragment.TAG);
		if (mCustomerDetailsFragment == null) {
			mCustomerDetailsFragment = new CustomerDetailsFragment();
			ft.add(R.id.right_container, mCustomerDetailsFragment, 
					CustomerDetailsFragment.TAG);
		}
		ft.hide(mCustomerDetailsFragment);

		mServiceDetailsFragment = (ServiceDetailsFragment) getSupportFragmentManager()
				.findFragmentByTag(ServiceDetailsFragment.TAG);
		if (mServiceDetailsFragment == null) {
			mServiceDetailsFragment = new ServiceDetailsFragment();
			ft.add(R.id.right_container, mServiceDetailsFragment, 
					ServiceDetailsFragment.TAG);
		}
		ft.hide(mServiceDetailsFragment);

		mProviderServiceFragment = (ProviderServiceFragment) getSupportFragmentManager()
				.findFragmentByTag(ProviderServiceFragment.TAG);
		if (mProviderServiceFragment == null) {
			mProviderServiceFragment = new ProviderServiceFragment();
			ft.add(R.id.right_container, mProviderServiceFragment, 
					ProviderServiceFragment.TAG);
		}
		ft.hide(mProviderServiceFragment);

		mProviderServiceSecondFragment = 
				(ProviderServiceSecondFragment) getSupportFragmentManager()
				.findFragmentByTag(ProviderServiceSecondFragment.TAG);
		if (mProviderServiceSecondFragment == null) {
			mProviderServiceSecondFragment = new ProviderServiceSecondFragment();
			ft.add(R.id.right_container, mProviderServiceSecondFragment, 
					ProviderServiceSecondFragment.TAG);
		}
		ft.hide(mProviderServiceSecondFragment);

		mProviderServiceThirdFragment = 
				(ProviderServiceThirdFragment) getSupportFragmentManager()
				.findFragmentByTag(ProviderServiceThirdFragment.TAG);
		if (mProviderServiceThirdFragment == null) {
			mProviderServiceThirdFragment = new ProviderServiceThirdFragment();
			ft.add(R.id.right_container, mProviderServiceThirdFragment, 
					ProviderServiceThirdFragment.TAG);
		}
		ft.hide(mProviderServiceThirdFragment);

		if (mFragmentType == UPLIFT) {
			mUpliftFragment = (UpliftFragment) getSupportFragmentManager()
					.findFragmentByTag(UpliftFragment.TAG);
			if (mUpliftFragment == null) {
				mUpliftFragment = new UpliftFragment();
				ft.add(R.id.right_container, mUpliftFragment, 
						UpliftFragment.TAG);
			}
			ft.hide(mUpliftFragment);
		} else if (mFragmentType == DELIVERY) {
			mDeliveryFragment = (DeliveryFragment) getSupportFragmentManager()
					.findFragmentByTag(DeliveryFragment.TAG);
			if (mDeliveryFragment == null) {
				mDeliveryFragment = new DeliveryFragment();
				ft.add(R.id.right_container, mDeliveryFragment, 
						DeliveryFragment.TAG);
			}
			ft.hide(mDeliveryFragment);
		}

		mTollFragment = (TollTransitionsFragment) getSupportFragmentManager()
				.findFragmentByTag(TollTransitionsFragment.TAG);
		if (mTollFragment == null) {
			mTollFragment = new TollTransitionsFragment();
			ft.add(R.id.right_container, mTollFragment, 
					TollTransitionsFragment.TAG);
		}
		ft.hide(mTollFragment);

		mRatingFragment = (MemberRatingFragment) getSupportFragmentManager()
				.findFragmentByTag(MemberRatingFragment.TAG);
		if (mRatingFragment == null) {
			mRatingFragment = new MemberRatingFragment();
			ft.add(R.id.right_container, mRatingFragment, 
					MemberRatingFragment.TAG);
		}
		ft.hide(mRatingFragment);

		mVolumeFragment = (VolumeFragment) getSupportFragmentManager()
				.findFragmentByTag(VolumeFragment.TAG);
		if (mVolumeFragment == null) {
			mVolumeFragment = new VolumeFragment();
			ft.add(R.id.right_container, mVolumeFragment, 
					VolumeFragment.TAG);
		}
		ft.hide(mVolumeFragment);

		mMemberSignatureFragment = (MemberSignatureFragment) getSupportFragmentManager()
				.findFragmentByTag(MemberSignatureFragment.TAG);
		if (mMemberSignatureFragment == null) {
			mMemberSignatureFragment = new MemberSignatureFragment();
			ft.add(R.id.right_container, mMemberSignatureFragment, 
					MemberSignatureFragment.TAG);
		}
		ft.hide(mMemberSignatureFragment);

		mDHRMSignatureFragment = (DHRMSignatureFragment) getSupportFragmentManager()
				.findFragmentByTag(DHRMSignatureFragment.TAG);
		if (mDHRMSignatureFragment == null) {
			mDHRMSignatureFragment = new DHRMSignatureFragment();
			ft.add(R.id.right_container, mDHRMSignatureFragment, 
					DHRMSignatureFragment.TAG);
		}
		ft.hide(mDHRMSignatureFragment);

		mTTSignatureFragment = (TTSignatureFragment) getSupportFragmentManager()
				.findFragmentByTag(TTSignatureFragment.TAG);
		if (mTTSignatureFragment == null) {
			mTTSignatureFragment = new TTSignatureFragment();
			ft.add(R.id.right_container, mTTSignatureFragment, 
					TTSignatureFragment.TAG);
		}
		ft.hide(mTTSignatureFragment);

		ft.commit();
	}

	public void showNextSignature() {
		if (mVisible instanceof MemberSignatureFragment) {
			showSlideFragment(mDHRMSignatureFragment, R.anim.slide_in_left, R.anim.slide_out_left);			
		} else if (mVisible instanceof DHRMSignatureFragment) {
			showSlideFragment(mTTSignatureFragment, R.anim.slide_in_left, R.anim.slide_out_left);			
		} 
	}

	public void showPrevSignature() {
		if (mVisible instanceof DHRMSignatureFragment) {
			showSlideFragment(mMemberSignatureFragment, R.anim.slide_in_right, R.anim.slide_out_right);			
		} else if (mVisible instanceof TTSignatureFragment) {
			showSlideFragment(mDHRMSignatureFragment, R.anim.slide_in_right, R.anim.slide_out_right);			
		} 
	}

	public void showNextProviderService() {
		if (mVisible instanceof ProviderServiceFragment) {
			showSlideFragment(mProviderServiceSecondFragment, R.anim.slide_in_left, R.anim.slide_out_left);			
		} else if (mVisible instanceof ProviderServiceSecondFragment) {
			showSlideFragment(mProviderServiceThirdFragment, R.anim.slide_in_left, R.anim.slide_out_left);			
		} 
	}

	public void showPrevProviderService() {
		if (mVisible instanceof ProviderServiceSecondFragment) {
			showSlideFragment(mProviderServiceFragment, R.anim.slide_in_right, R.anim.slide_out_right);			
		} else if (mVisible instanceof ProviderServiceThirdFragment) {
			showSlideFragment(mProviderServiceSecondFragment, R.anim.slide_in_right, R.anim.slide_out_right);			
		} 
	}
	
	public UploadInDTO getUploadInDTO() { return mUploadInDTO; }
	public int getFragmentType() { return mFragmentType; }

	public void validateForm(int naviItem) {
		switch(naviItem) {
		case SERVICE_DETAIL:
			mCINaviFragment.setNaviCompletedIndicator(SERVICE_DETAIL, 
					UploadInDTOValidator.validateServiceDetail(
							mUploadInDTO.getQuestionAnswers()));
			break;
		case PROVIDER_SERVICE:
			mCINaviFragment.setNaviCompletedIndicator(PROVIDER_SERVICE, 
					UploadInDTOValidator.validateProviderService1(
							mUploadInDTO.getQuestionAnswers()) &&
					UploadInDTOValidator.validateProviderService2(
							mUploadInDTO.getQuestionAnswers()) &&
					UploadInDTOValidator.validateProviderService3(
							mUploadInDTO.getQuestionAnswers()));
			break;
		case MEMBER_RATING:
			mCINaviFragment.setNaviCompletedIndicator(MEMBER_RATING, 
					UploadInDTOValidator.validateMemberRating(
							mUploadInDTO.getQuestionAnswers()));
			break;
		case TOLL_RATING:
			mCINaviFragment.setNaviCompletedIndicator(TOLL_RATING, 
					UploadInDTOValidator.validateTollRating(
							mUploadInDTO.getQuestionAnswers()));
			break;
		case VOLUME:
			mCINaviFragment.setNaviCompletedIndicator(VOLUME, 
					UploadInDTOValidator.validateVolume(
							mUploadInDTO.getQuestionAnswers()));
			break;
		case GENERIC:
			if (mFragmentType == DELIVERY) {
				mCINaviFragment.setNaviCompletedIndicator(GENERIC, 
						UploadInDTOValidator.validateDelivery(
								mUploadInDTO.getQuestionAnswers()));
			} else if (mFragmentType == UPLIFT) {
				mCINaviFragment.setNaviCompletedIndicator(GENERIC, 
						UploadInDTOValidator.validateUplift(
								mUploadInDTO.getQuestionAnswers()));
			}
			break;
		case SIGNATURE:
			mCINaviFragment.setNaviCompletedIndicator(SIGNATURE, 
					UploadInDTOValidator.validateSignature(
							mUploadInDTO.getSignatureContract()));
			break;
		}
	}
	
	public boolean isAllValid() {
		return mCINaviFragment.isAllValid();
	}
	
}