package com.contactpoint.view.fragment;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.contactpoint.ci_prv_mm.MainFragmentActivity;
import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.CommonController;
import com.contactpoint.controller.NetworkListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.ci.SearchCIInDTO;
import com.contactpoint.model.DTO.prv.SearchPRVInDTO;
import com.contactpoint.model.client.prv.Client;
import com.contactpoint.model.client.prv.ReferenceIDDropDown;
import com.contactpoint.model.service.GetCIService;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.model.util.AzureAuthClient;


public class SearchFragment extends NetworkFragment {

	public static final String TAG = "SearchFragment";

	private TabHost mTabHost;
	private ImageView mTitle;
	private View mHorizontal;

	/* CI Components */
	private Spinner mCIRegionList;
	private Spinner mCIZoneList;
	private Spinner mCIBusinessList;
	private Spinner mVolumeList;
	private EditText mCIFrom;
	private EditText mCITo;
	private OnDateSetListener mCIFromListener;
	private OnDateSetListener mCIToListener;
	private DatePickerDialogFragment mCIFromDatepicker;
	private DatePickerDialogFragment mCIToDatepicker;

	/* PRV Components */
	private Spinner mPRVRegionList;
	private Spinner mPRVZoneList;
	private Spinner mPRVBusinessList;
	private Spinner mPRVProvCodeList;
	private EditText mPONum;
	private EditText mOrderNum;
	private EditText mPRVFrom;
	private EditText mPRVTo;
	private Spinner mClientCode;
	private OnDateSetListener mPRVFromListener;
	private OnDateSetListener mPRVToListener;
	private DatePickerDialogFragment mPRVFromDatepicker;
	private DatePickerDialogFragment mPRVToDatepicker;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setRetainInstance(true);

		mCIFromListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mCIFromDatepicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mCIFrom, dayOfMonth, monthOfYear, year);
			}

		};

		mCIToListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mCIToDatepicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mCITo, dayOfMonth, monthOfYear, year);
			}
		};

		mPRVFromListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mPRVFromDatepicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mPRVFrom, dayOfMonth, monthOfYear, year);
			}

		};

		mPRVToListener = new OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mPRVToDatepicker.setDate(dayOfMonth, monthOfYear, year);
				CommonController.updateDisplay(mPRVTo, dayOfMonth, monthOfYear, year);
			}
		};
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			if (getArguments() != null && 
					getArguments().containsKey(MainFragmentActivity.EXTRA_SHOW_CI)) {
				if (getArguments().getBoolean(MainFragmentActivity.EXTRA_SHOW_CI)) {
					mTabHost.setCurrentTab(0);
				} else {
					mTabHost.setCurrentTab(1);
				}
				getArguments().remove(MainFragmentActivity.EXTRA_SHOW_CI);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {		
		View v = inflater.inflate(R.layout.toll_search, null);

		mTitle = (ImageView) v.findViewById(R.id.title);
		mHorizontal = v.findViewById(R.id.horizontal);

		// tabs
		mTabHost = (TabHost) v.findViewById(R.id.tabhost);
		mTabHost.setup();

		TabHost.TabSpec spec1 = mTabHost.newTabSpec(getResources().getString(R.string.ci_tab));
		spec1.setContent(R.id.ci_search);
		spec1.setIndicator(getResources().getString(R.string.ci_tab));
		mTabHost.addTab(spec1);

		TabHost.TabSpec spec2 = mTabHost.newTabSpec(getResources().getString(R.string.prv_tab));
		spec2.setContent(R.id.prv_search);
		spec2.setIndicator(getResources().getString(R.string.prv_tab));
		mTabHost.addTab(spec2);

		mTabHost.setCurrentTab(0);
		mTabHost.setOnTabChangedListener(mTabChangeListener);

		v.findViewById(R.id.btn_back).setOnClickListener(mBackToDashboardListener);
		v.findViewById(R.id.prv_btn_back).setOnClickListener(mBackToDashboardListener);
		v.findViewById(R.id.btn_search).setOnClickListener(mCISearchListener);
		v.findViewById(R.id.prv_btn_search).setOnClickListener(mPRVSearchListener);

		mCIRegionList   = (Spinner) v.findViewById(R.id.ci_region_spinner);
		mCIZoneList 	= (Spinner) v.findViewById(R.id.ci_zone_spinner);
		mCIBusinessList = (Spinner) v.findViewById(R.id.ci_business_spinner);
		mVolumeList   	= (Spinner) v.findViewById(R.id.ci_volume_spinner);

		mPRVRegionList   = (Spinner) v.findViewById(R.id.prv_region_spinner);
		mPRVZoneList 	 = (Spinner) v.findViewById(R.id.prv_zone_spinner);
		mPRVBusinessList = (Spinner) v.findViewById(R.id.prv_business_spinner);
		mClientCode 	 = (Spinner) v.findViewById(R.id.prv_client_code_txt);
		mPRVProvCodeList = (Spinner) v.findViewById(R.id.prv_provider_code_txt);

		ArrayList<ReferenceIDDropDown> regionList = ModelFactory.getPRVService().getRefDataRegion();
		ArrayList<ReferenceIDDropDown> zoneList = ModelFactory.getPRVService().getRefDataZone();
		ArrayList<ReferenceIDDropDown> businessList = ModelFactory.getPRVService().getRefDataBusinessGroup();
		ArrayList<ReferenceIDDropDown> volumeList = ModelFactory.getPRVService().getRefDataVolume();
		ArrayList<Client> clientList = ModelFactory.getPRVService().getRefDataClient();
		ArrayList<ReferenceIDDropDown> providerList = ModelFactory.getPRVService().getRefDataProvider();
		
		CommonController.setSpinnerAdapter(getSherlockActivity(), mCIRegionList, regionList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mCIZoneList, zoneList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mCIBusinessList, businessList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mVolumeList, volumeList);
		
		CommonController.setSpinnerAdapter(getSherlockActivity(), mPRVRegionList, regionList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mPRVZoneList, zoneList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mPRVBusinessList, businessList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mClientCode, clientList);
		CommonController.setSpinnerAdapter(getSherlockActivity(), mPRVProvCodeList, providerList);

		mCIFrom = (EditText) v.findViewById(R.id.ci_from_txt);
		mCITo   = (EditText) v.findViewById(R.id.ci_to_txt);

		mPRVFrom = (EditText) v.findViewById(R.id.prv_from_txt);
		mPRVTo   = (EditText) v.findViewById(R.id.prv_to_txt);

		mPONum = (EditText) v.findViewById(R.id.prv_po_num_txt);
		mOrderNum = (EditText) v.findViewById(R.id.prv_order_num_txt);

		final Calendar c = Calendar.getInstance();
		int yyyy = c.get(Calendar.YEAR);
		int mm 	 = c.get(Calendar.MONTH);
		int dd 	 = c.get(Calendar.DAY_OF_MONTH);

		CommonController.updateDisplay(mCIFrom, dd, mm, yyyy);
		CommonController.updateDisplay(mCITo, dd, mm, yyyy);
		CommonController.updateDisplay(mPRVFrom, dd, mm, yyyy);
		CommonController.updateDisplay(mPRVTo, dd, mm, yyyy);

		mCIFromDatepicker = new DatePickerDialogFragment(mCIFromListener, dd, mm, yyyy);
		mCIToDatepicker   = new DatePickerDialogFragment(mCIToListener, dd, mm, yyyy);
		mPRVFromDatepicker = new DatePickerDialogFragment(mPRVFromListener, dd, mm, yyyy);
		mPRVToDatepicker   = new DatePickerDialogFragment(mPRVToListener, dd, mm, yyyy);

		mCIFrom.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mCIFromDatepicker.show(getFragmentManager(), "dialog");
				return false;
			}
		});
		mPRVFrom.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mPRVFromDatepicker.show(getFragmentManager(), "dialog");
				return false;
			}
		});

		mCITo.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mCIToDatepicker.show(getFragmentManager(), "dialog");
				return false;
			}
		});
		mPRVTo.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mPRVToDatepicker.show(getFragmentManager(), "dialog");
				return false;
			}
		});

		return v;
	}

	private OnClickListener mBackToDashboardListener = new OnClickListener() {
		public void onClick(View v) {
			CommonController.backToDashboard(getSherlockActivity());
		}
	};

	private OnClickListener mCISearchListener = new OnClickListener() {
		public void onClick(View v) {
			SearchCIInDTO param = ModelFactory.getCIService().getSearchInDTO();
			param.setFromDate(mCIFrom.getText().toString());
			param.setToDate(mCITo.getText().toString());
			param.setBusinessGroup(((ReferenceIDDropDown)mCIBusinessList.getSelectedItem()).Description);
			param.setRegion(((ReferenceIDDropDown)mCIRegionList.getSelectedItem()).Description);
			param.setZone(((ReferenceIDDropDown)mCIZoneList.getSelectedItem()).Description);
			param.setVolume(((ReferenceIDDropDown)mVolumeList.getSelectedItem()).Description);
			
			if (AzureAuthClient.ENABLE_TMS) {
				ModelFactory.getCIService().prepareSearch();
				ModelFactory.getCIService().setNetworkListener(SearchFragment.this);
				int result = ModelFactory.getCIService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				ModelFactory.getCIService().prepareMMSearch();
				ModelFactory.getCIService().setNetworkListener(SearchFragment.this);
				int result = ModelFactory.getCIService().executeSoapRequest();
				if (result == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
		}
	};

	private OnClickListener mPRVSearchListener = new OnClickListener() {
		public void onClick(View v) {
			SearchPRVInDTO param = ModelFactory.getPRVService().getSearchPRVInDTO();
			param.setPONumber(mPONum.getText().toString());
			param.setOrderNumber(mOrderNum.getText().toString());
			param.setProviderCode((ReferenceIDDropDown)mPRVProvCodeList.getSelectedItem());
			param.setFromDate(mPRVFrom.getText().toString());
			param.setToDate(mPRVTo.getText().toString());
			param.setRegion(((ReferenceIDDropDown)mPRVRegionList.getSelectedItem()).Description);
			param.setZone(((ReferenceIDDropDown)mPRVZoneList.getSelectedItem()).Description);
			param.setBusinessGroup(((ReferenceIDDropDown)mPRVBusinessList.getSelectedItem()).Description);
			param.setClientCode(((Client)mClientCode.getSelectedItem()).ClientCode);
			
			if (AzureAuthClient.ENABLE_TMS) {
				ModelFactory.getPRVService().preparePRVSearch();
				ModelFactory.getPRVService().setNetworkListener(SearchFragment.this);
				if (ModelFactory.getPRVService().executeSoapRequest() == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			} else {
				ModelFactory.getPRVService().preparePRVMMSearch();
				ModelFactory.getPRVService().setNetworkListener(SearchFragment.this);
				if (ModelFactory.getPRVService().executeSoapRequest() == NetworkListener.ERR_OFFLINE) {
					showErrorDialog(getString(R.string.network_not_connected));
				}
			}
		}
	};

	@Override
	public void callback(int param) {
		super.onTaskComplete();
		switch(param) {
		case GetCIService.SEARCH:
			ModelFactory.getCIService().prepareMMSearch();
			ModelFactory.getCIService().setNetworkListener(SearchFragment.this);
			int result = ModelFactory.getCIService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
			break;
		case GetCIService.MM_SEARCH:
			((MainFragmentActivity)getActivity()).onNaviSelected(
					MainFragmentActivity.SEARCH_RESULT);				
			break;
		case PRVService.PRV_SEARCH:
			ModelFactory.getPRVService().preparePRVMMSearch();
			ModelFactory.getPRVService().setNetworkListener(SearchFragment.this);
			result = ModelFactory.getPRVService().executeSoapRequest();
			if (result == NetworkListener.ERR_OFFLINE) {
				showErrorDialog(getString(R.string.network_not_connected));
			}
			break;
		case PRVService.PRV_MM_SEARCH:
			((MainFragmentActivity)getActivity()).onNaviSelected(
					MainFragmentActivity.SEARCH_PRV_RESULT);
			break;
		case GetCIService.ERROR:
			showErrorDialog(ModelFactory.getCIService().getClientMessage());
			break;
		case PRVService.ERROR:
			showErrorDialog(ModelFactory.getPRVService().getClientMessage());
			break;
		case GetCIService.RESULT_EMPTY:
			showResult(getResources().getString(R.string.search_empty_msg));
			break;
		case PRVService.RESULT_EMPTY:
			showResult(getResources().getString(R.string.search_empty_msg));
			break;
		default:
			break;
		}
	}

	/*private ArrayAdapter<CharSequence> getCustomAdapter(int arrayId) {
		return ArrayAdapter.createFromResource(getSherlockActivity(),
				arrayId, android.R.layout.simple_spinner_item);
	}*/

	private OnTabChangeListener mTabChangeListener = new OnTabChangeListener() {

		@Override
		public void onTabChanged(String tabId) {
			if (tabId.equals(getResources().getString(R.string.ci_tab))) {
				mTitle.setImageResource(R.drawable.title_get_carrier_inspections);
				mHorizontal.setBackgroundResource(R.color.yellow);
			} else {
				mTitle.setImageResource(R.drawable.title_available_prv);
				mHorizontal.setBackgroundResource(R.color.pink);
			}
		}

	};
}
