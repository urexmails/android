package com.contactpoint.ci_prv_mm;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.OnNaviSelectedListener;
import com.contactpoint.model.util.TollUncaughtExceptionHandler;

public abstract class SuperFragmentActivity extends SherlockFragmentActivity 
implements OnNaviSelectedListener {

	protected Fragment mVisible = null;
	protected final String MAP_KEY	= "0IlJ_s8Sx69O7ABQw-m_H5Md8INUUCQkQ6hhaDw";


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		// If we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }
		setupFragments();
		
		Thread.setDefaultUncaughtExceptionHandler(new TollUncaughtExceptionHandler(this,
	            MainActivity.class));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}

	
	public abstract void onNaviSelected(int position);
	
	/**
	 * This method does the setting up of the Fragments. It basically checks if
	 * the fragments exist and if they do, we'll hide them. If the fragments
	 * don't exist, we create them, add them to the FragmentManager and hide
	 * them.
	 */
	protected abstract void setupFragments();
	
	/**
	 * This method shows the given Fragment and if there was another visible
	 * fragment, it gets hidden. We can just do this because we know that both
	 * the mMyListFragment and the mMapFragment were added in the Activity's
	 * onCreate, so we just create the fragments once at first and not every
	 * time. This will avoid facing some problems with the MapView.
	 * 
	 * @param fragmentIn
	 *            The fragment to show.
	 */
	protected void showFragment(Fragment fragmentIn) {
		if (fragmentIn == null) return;

		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

		if (mVisible != null) ft.hide(mVisible);

		ft.show(fragmentIn).commit();
		mVisible = fragmentIn;
	}	
	
	protected void showSlideFragment(Fragment fragmentIn, int anim_in, int anim_out) {
		if (fragmentIn == null) return;

		final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.setCustomAnimations(anim_in, anim_out);
		if (mVisible != null) ft.hide(mVisible);

		ft.show(fragmentIn).commit();
		mVisible = fragmentIn;
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

}
