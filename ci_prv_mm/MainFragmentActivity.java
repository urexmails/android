package com.contactpoint.ci_prv_mm;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.view.fragment.BookFragment;
import com.contactpoint.view.fragment.SearchCIResultFragment;
import com.contactpoint.view.fragment.SearchFragment;
import com.contactpoint.view.fragment.SearchPRVResultFragment;
import com.contactpoint.view.fragment.WorkListFragment;
import com.contactpoint.view.fragment.nav.NaviFragment;

/**
 * 
 * @author MyPC
 * Ref: https://xrigau.wordpress.com/2012/03/22/howto-actionbarsherlock-mapfragment-listfragment/
 */

public class MainFragmentActivity extends SuperFragmentActivity {

	public static final String EXTRA = "SELECTED";
	public static final String EXTRA_UPLOAD = "UPLOAD";
	public static final String EXTRA_SHOW_CI = "SHOW_CI";
	public static final int SEARCH = 0;
	public static final int WORKLIST = 1;
	public static final int UPLOAD = 2;
	public static final int LOGOUT = 3;
	public static final int SEARCH_RESULT = 4;
	public static final int SEARCH_PRV_RESULT = 5;
	public static final int BOOK = 6;
	
	private NaviFragment mNaviFragment;

	private SearchFragment mSearchFragment;
	private SearchCIResultFragment mSearchCIResultFragment;
	private SearchPRVResultFragment mSearchPRVResultFragment;
	private WorkListFragment mWorkListFragment;
	private BookFragment mBookFragment;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.gc();

		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
            return;
        }
		// We manually show the list Fragment.
		Bundle extras = getIntent().getExtras();
		mNaviFragment.setArguments(extras);
		onNaviSelected(extras.getInt(EXTRA));
		
		// auto upload
		if (extras.getBoolean(EXTRA_UPLOAD)) {
			mWorkListFragment.setArguments(extras);
		}
		
		if (extras.containsKey(EXTRA_SHOW_CI)) {
			mSearchFragment.setArguments(extras);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.version_only_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onNaviSelected(int position) {
		// The user selected the headline of an article from the HeadlinesFragment
			switch (position) {
			case SEARCH:
				showFragment(mSearchFragment);
				break;
			case WORKLIST:
				showFragment(mWorkListFragment);
				break;
			case UPLOAD:
				mWorkListFragment.onUploadEventTriggered();
				break;
			case LOGOUT:
				CommonController.logout(this);
				break;
			case SEARCH_RESULT:
				showFragment(mSearchCIResultFragment);
				break;
			case SEARCH_PRV_RESULT:
				showFragment(mSearchPRVResultFragment);
				break;
			case BOOK:
				showSlideFragment(mBookFragment, R.anim.slide_in_left, R.anim.slide_out_left);			
				break;
			default:
				break;
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
		mNaviFragment = (NaviFragment) getSupportFragmentManager()
				.findFragmentByTag(NaviFragment.TAG);
		if (mNaviFragment == null) {
			mNaviFragment = new NaviFragment();
			ft.add(R.id.left_container, mNaviFragment, NaviFragment.TAG);
		}

		mSearchFragment = (SearchFragment) getSupportFragmentManager()
				.findFragmentByTag(SearchFragment.TAG);
		if (mSearchFragment == null) {
			mSearchFragment = new SearchFragment();
			ft.add(R.id.right_container, mSearchFragment, SearchFragment.TAG);
		}
		ft.hide(mSearchFragment);

		mSearchCIResultFragment = (SearchCIResultFragment) getSupportFragmentManager()
				.findFragmentByTag(SearchCIResultFragment.TAG);
		if (mSearchCIResultFragment == null) {
			mSearchCIResultFragment = new SearchCIResultFragment();
			ft.add(R.id.right_container, mSearchCIResultFragment, SearchCIResultFragment.TAG);
		}
		ft.hide(mSearchCIResultFragment);
		
		mSearchPRVResultFragment = (SearchPRVResultFragment) getSupportFragmentManager()
				.findFragmentByTag(SearchPRVResultFragment.TAG);
		if (mSearchPRVResultFragment == null) {
			mSearchPRVResultFragment = new SearchPRVResultFragment();
			ft.add(R.id.right_container, mSearchPRVResultFragment, SearchPRVResultFragment.TAG);
		}
		ft.hide(mSearchPRVResultFragment);

		mWorkListFragment = (WorkListFragment) getSupportFragmentManager()
				.findFragmentByTag(WorkListFragment.TAG);
		if (mWorkListFragment == null) {
			mWorkListFragment = new WorkListFragment();
			ft.add(R.id.right_container, mWorkListFragment, WorkListFragment.TAG);
		}
		ft.hide(mWorkListFragment);
		
		mBookFragment = (BookFragment) getSupportFragmentManager()
				.findFragmentByTag(BookFragment.TAG);
		if (mBookFragment == null) {
			mBookFragment = new BookFragment();
			ft.add(R.id.right_container, mBookFragment, BookFragment.TAG);
		}
		ft.hide(mBookFragment);

		ft.commit();
	}	
}