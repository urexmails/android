package com.contactpoint.ci_prv_mm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.controller.BasicOnKeyboardActionListener;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.DTO.prv.ItemInDTO;
import com.contactpoint.model.DTO.prv.RoomOutDTO;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.prv.Item;
import com.contactpoint.model.service.PRVService;
import com.contactpoint.view.SelectRoomCustomTableLayout;
import com.contactpoint.view.TollCustomKeyboard;
import com.contactpoint.view.adapter.prv.PRVAddOnAdapter;
import com.contactpoint.view.adapter.prv.PRVAddedContentAdapter;
import com.contactpoint.view.adapter.prv.PRVSearchAdapter;

public class PRVItemCollectionActivity extends PRVActivity {

	//private final String FORMAT = "%.3f";

	private AlertDialog mSelectRoomDialog;
	private AlertDialog mAddCrateDialog;
	private AlertDialog mCustomItemDialog;
	private AlertDialog mShowAllDialog;

	private ExpandableListView mRoomList;
	private PRVSearchAdapter mRoomAdapter;
	private PRVAddOnAdapter mAddOnAdapter;

	private EditText mRunningTotal;
	private EditText mCubicMetres;
	private EditText mItemQty;
	private AutoCompleteTextView mAutoComplete;
	private TextView mRoomNameLabel;
	private TextView mLatestItemLabel;

	private SelectRoomCustomTableLayout mSelectRoomTable;
	private TableLayout mShowAllTable;
	private ListView mAddedContentList;

	private TollCustomKeyboard mKeyboardView;

	/* Add On Components */
	private TextView mCarton1;
	private TextView mCarton2;
	private TextView mCarton3;
	private TextView mCover1;
	private TextView mCover2;
	private TextView mCover3;
	private TextView mCrate;
	private EditText mCartonTxt1;
	private EditText mCartonTxt2;
	private EditText mCartonTxt3;
	private EditText mCoverTxt1;
	private EditText mCoverTxt2;
	private EditText mCoverTxt3;
	private EditText mCrateTxt;
	private TableRow mCrateRow;

	/* Custom Item Form */
	private EditText mCustomItemName;
	private EditText mCustomItemLength;
	private EditText mCustomItemWidth;
	private EditText mCustomItemDepth;
	private EditText mCustomItemCubic;

	/* Custom Crate Form */
	private EditText mCustomCrateName;
	private EditText mCustomCrateLength;
	private EditText mCustomCrateWidth;
	private EditText mCustomCrateDepth;
	private EditText mCustomCrateCubic;
	
	private DataSetObserver mAddedContentObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			mRunningTotal.setText(ModelFactory.getUtilService().printFormat(
					ModelFactory.getPRVService().getRoomRunningTotal(
							ModelFactory.getPRVService().getCurrentRoom())));
			/*
			mRunningTotal.setText(String.format(FORMAT, 
					ModelFactory.getPRVService().getRoomRunningTotal(
							ModelFactory.getPRVService().getCurrentRoom())));*/
		}
	};

	private DataSetObserver mShowAllObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			// remove contents
			mShowAllTable.removeAllViews();

			for (int i = 0; i < mAddOnAdapter.getCount(); i++) {
				mShowAllTable.addView(mAddOnAdapter.getView(i, null, mShowAllTable));
			}
			mRunningTotal.setText(ModelFactory.getUtilService().printFormat(
					ModelFactory.getPRVService().getRoomRunningTotal(
							ModelFactory.getPRVService().getCurrentRoom())));
			/*
			mRunningTotal.setText(String.format(FORMAT, 
					ModelFactory.getPRVService().getRoomRunningTotal(
							ModelFactory.getPRVService().getCurrentRoom())));*/

			//			refreshRight();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prv_item_collection);

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		// set up auto complete text view adapter		
		ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this,
				android.R.layout.simple_dropdown_item_1line, 
				ModelFactory.getPRVService().getStandardItems());
		mAutoComplete = (AutoCompleteTextView) findViewById(R.id.txt_search);
		mAutoComplete.setAdapter(adapter);
		mAutoComplete.setOnTouchListener(mAutoCompleteTouchListener);

		// set expandable list view adapter and listener
		mRoomList = (ExpandableListView)findViewById(R.id.search_list);
		mRoomList.setOnGroupExpandListener(mExpandOnlyOneListener);
		mRoomAdapter = new PRVSearchAdapter(this);
		mRoomList.setAdapter(mRoomAdapter);

		// catch elements
		mRunningTotal = (EditText)findViewById(R.id.txt_running_total);
		mCubicMetres = (EditText)findViewById(R.id.txt_cubic_metres);
		mItemQty = (EditText)findViewById(R.id.txt_item_qty);
		mRoomNameLabel = (TextView)findViewById(R.id.tv_room_title);
		mLatestItemLabel = (TextView)findViewById(R.id.prv_item_name);
		mAddedContentList = (ListView)findViewById(R.id.prv_added_list);
		mKeyboardView = (TollCustomKeyboard) findViewById(R.id.keyboard_view);

		TableRow tr = (TableRow) findViewById(R.id.carton_1);
		mCarton1 = (TextView)tr.findViewById(R.id.content_name);
		mCartonTxt1 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCartonTxt1);
		tr.findViewById(R.id.btn_content_plus).setTag(mCartonTxt1);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		tr = (TableRow) findViewById(R.id.carton_2);
		mCarton2 = (TextView)tr.findViewById(R.id.content_name);
		mCartonTxt2 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCartonTxt2);
		tr.findViewById(R.id.btn_content_plus).setTag(mCartonTxt2);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		tr = (TableRow) findViewById(R.id.carton_3);
		mCarton3 = (TextView)tr.findViewById(R.id.content_name);
		mCartonTxt3 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCartonTxt3);
		tr.findViewById(R.id.btn_content_plus).setTag(mCartonTxt3);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		tr = (TableRow) findViewById(R.id.cover_1);
		mCover1 = (TextView)tr.findViewById(R.id.content_name);
		mCoverTxt1 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCoverTxt1);
		tr.findViewById(R.id.btn_content_plus).setTag(mCoverTxt1);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		tr = (TableRow) findViewById(R.id.cover_2);
		mCover2 = (TextView)tr.findViewById(R.id.content_name);
		mCoverTxt2 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCoverTxt2);
		tr.findViewById(R.id.btn_content_plus).setTag(mCoverTxt2);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		tr = (TableRow) findViewById(R.id.cover_3);
		mCover3 = (TextView)tr.findViewById(R.id.content_name);
		mCoverTxt3 = (EditText)tr.findViewById(R.id.content_text);
		tr.findViewById(R.id.btn_content_minus).setTag(mCoverTxt3);
		tr.findViewById(R.id.btn_content_plus).setTag(mCoverTxt3);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		mCrateRow = (TableRow) findViewById(R.id.crates_1);
		mCrate = (TextView)mCrateRow.findViewById(R.id.content_name);
		mCrateTxt = (EditText)mCrateRow.findViewById(R.id.content_text);
		mCrateRow.findViewById(R.id.btn_content_minus).setTag(mCrateTxt);
		mCrateRow.findViewById(R.id.btn_content_plus).setTag(mCrateTxt);
		tr.findViewById(R.id.btn_content_minus).setOnLongClickListener(onLongDecrementListener);
		tr.findViewById(R.id.btn_content_plus).setOnLongClickListener(onLongIncrementListener);

		// set listeners
		findViewById(R.id.tv_cartons_showall).setOnClickListener(mShowAllListener);
		findViewById(R.id.tv_covers_showall).setOnClickListener(mShowAllListener);
		findViewById(R.id.tv_crates_showall).setOnClickListener(mShowAllListener);
		findViewById(R.id.tv_add_crate).setOnClickListener(mAddCrateButtonListener);
		mKeyboardView.setOnKeyboardActionListener(BasicOnKeyboardActionListener
				.sharedInstance(this));
		mItemQty.setOnTouchListener(mCustomKeyboardListener);
		mCubicMetres.setOnTouchListener(mCustomKeyboardListener);

		// set tag for show all
		findViewById(R.id.tv_cartons_showall).setTag(ItemInDTO.CARTON);
		findViewById(R.id.tv_covers_showall).setTag(ItemInDTO.COVER);
		findViewById(R.id.tv_crates_showall).setTag(ItemInDTO.CRATE);

		mRoomList.setOnChildClickListener(mRoomItemSelectedListener);
		mAutoComplete.setOnItemClickListener(mAutoCompleteListener);

		mAddedContentList.setAdapter(new PRVAddedContentAdapter(PRVItemCollectionActivity.this));
		mAddedContentList.getAdapter().registerDataSetObserver(mAddedContentObserver);

		mAddOnAdapter = new PRVAddOnAdapter(this);
		mAddOnAdapter.registerDataSetObserver(mShowAllObserver);
		
		// set visibility of D2D & D2S buttons
		if (ModelFactory.getPRVService().getCurrentForm().getDownload().size() > 1) {
			findViewById(R.id.prv_btn_d2d).setVisibility(View.VISIBLE);
			findViewById(R.id.prv_btn_d2s).setVisibility(View.VISIBLE);
			
			int targetId = R.id.prv_btn_d2d;
			if (!ModelFactory.getPRVService().getCurrentForm().isD2D()) {
				targetId = R.id.prv_btn_d2s;
			}
			findViewById(targetId).setEnabled(false);
			((Button)findViewById(targetId)).setTextAppearance(this, R.style.TollButton_Bold);
		} else {
			findViewById(R.id.prv_btn_d2d).setVisibility(View.GONE);
			findViewById(R.id.prv_btn_d2s).setVisibility(View.GONE);
		}

		// create dialogs
		createSelectRoomDialog();
		createAddCrateDialog();
		createAddCustomItemDialog();
		createShowAllDialog();

		mSelectRoomDialog.setOnDismissListener(mAddRoomDismissListener);

		if (ModelFactory.getPRVService().getCurrentRoom() == null) {
			mSelectRoomDialog.show();
		} else {
			refreshContent();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// http://stackoverflow.com/questions/4005728/hide-default-keyboard-on-click-in-android
		// hide keyboard if user touch anywhere else outside the keyboard
		if(getCurrentFocus() != null && getCurrentFocus() instanceof EditText){
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

			
			if (mKeyboardView.isShown()) {
				int[] location = new int[2];
				mKeyboardView.getLocationOnScreen(location);
				float x = ev.getRawX() + mKeyboardView.getLeft() - location[0];
		        float y = ev.getRawY() + mKeyboardView.getTop() - location[1];

				if (x < mKeyboardView.getLeft() ||
						x > mKeyboardView.getRight() ||
						y < mKeyboardView.getTop() ||
						y > mKeyboardView.getBottom()) {
					mKeyboardView.setVisibility(View.GONE);
					getCurrentFocus().clearFocus();					
				}
			}
		}

		return super.dispatchTouchEvent(ev);
	}
	
	/*
	@Override
	public boolean dispatchKeyEvent(KeyEvent ev) {
		boolean temp = super.dispatchKeyEvent(ev);
		if (mCubicMetres.isFocused()) {
			mCubicMetres.setSelection(mCubicMetres.getText().length());
			System.out.println("test");
		}
		return temp;
	}*/

	private View.OnTouchListener mCustomKeyboardListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			EditText et = (EditText)v;
			
			// select the whole text and show keyboard            
            if (mKeyboardView.getVisibility() != View.VISIBLE) {
    			mKeyboardView.setKeyboardLayout(v);
    			mKeyboardView.setVisibility(View.VISIBLE);
    			et.setText("");
    			//et.setSelection(0, et.getText().length());
            }
			
            // implement touch event
			et.onTouchEvent(event);

			// hide default keyboard
			InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }                
            
            return true;
		}
	};

	/**
	 * Pragma mark -
	 * Pragma Add Item List
	 */
	public void onAddItemListener(View v) {
		if (mLatestItemLabel.getTag() == null || mCubicMetres.getText().length() == 0 || mItemQty.getText().length() == 0) {
			return;
		}

		// init value
		ItemInDTO inDTO = new ItemInDTO();
		Item outDTO = (Item)mLatestItemLabel.getTag();
		inDTO.CubicMeterage = mCubicMetres.getText().toString();
		if (inDTO.CubicMeterage.compareTo(outDTO.CubicMeterage) != 0) {
			inDTO.IsCustom = ItemInDTO.IS_CUSTOM;
			inDTO.ID = --PRVForm.newItemId;
		} else {
			inDTO.IsCustom = ItemInDTO.NOT_CUSTOM;
			inDTO.ID = outDTO.ID;
		}

		inDTO.ItemType = outDTO.ItemType;
		inDTO.Name = outDTO.Name;
		inDTO.Length = outDTO.Length;
		inDTO.Width = outDTO.Width;
		inDTO.Depth = outDTO.Depth;
		inDTO.MaxNumber = outDTO.MaxNumber;
		inDTO.setQty(Integer.parseInt(mItemQty.getText().toString()));

		// save history
		ModelFactory.getPRVService().addItem(inDTO);

		((PRVAddedContentAdapter)mAddedContentList.getAdapter()).notifyDataSetChanged();
	}

	public void addCustomListener(View v) {
		mCustomItemDialog.show();
	}

	private TextWatcher mItemVolumeWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mCustomItemCubic.setText(ModelFactory.getUtilService().printFormat(ModelFactory.getPRVService().
					calculateVolume(mCustomItemLength.getText().toString(),
							mCustomItemWidth.getText().toString(),
							mCustomItemDepth.getText().toString())));
			/*
			mCustomItemCubic.setText(String.format(FORMAT, ModelFactory.getPRVService().
					calculateVolume(mCustomItemLength.getText().toString(),
							mCustomItemWidth.getText().toString(),
							mCustomItemDepth.getText().toString())));*/
		}

	};

	private DialogInterface.OnClickListener mAddCustomItemListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				if (mCustomItemName.getText().length() == 0 ||
				mCustomItemLength.getText().length() == 0 ||
				mCustomItemWidth.getText().length() == 0 ||
				mCustomItemDepth.getText().length() == 0) {
					return;
				}

				Item inDTO = new Item();
				inDTO.Length = mCustomItemLength.getText().toString();				
				inDTO.Width = mCustomItemWidth.getText().toString();
				inDTO.Depth = mCustomItemDepth.getText().toString();
				inDTO.Name = mCustomItemName.getText().toString();				
				inDTO.CubicMeterage = mCustomItemCubic.getText().toString();
				inDTO.IsCustom = ItemInDTO.IS_CUSTOM;
				inDTO.ItemType = ItemInDTO.STANDARD;
				inDTO.ID = --PRVForm.newItemId;

				onInventoryItemSelectedEvent(inDTO);

				mCustomItemLength.setText("");
				mCustomItemWidth.setText("");
				mCustomItemDepth.setText("");				
				mCustomItemName.setText("");

				break;
			}
		}
	};

	/**
	 * Pragma mark -
	 * Pragma Room Summary
	 */
	public void onRoomSummaryButtonClick(View v) {
		ModelFactory.getPRVService().saveFormData(PRVItemCollectionActivity.this);
		Intent i = new Intent(getApplication(), PRVRoomSummaryActivity.class);
		startActivity(i);
	}

	/**
	 * Pragma mark -
	 * Pragma Inventory Expandable List
	 */
	private OnGroupExpandListener mExpandOnlyOneListener = new OnGroupExpandListener() {

		@Override
		public void onGroupExpand(int groupPosition) {
			//collapse the old expanded group, if not the same
			//as new group to expand
			int lastExpandedGroupPosition = mRoomAdapter.getLastExpandedGroupPosition();
			if(groupPosition != lastExpandedGroupPosition){
				mRoomList.collapseGroup(lastExpandedGroupPosition);
			}

			mRoomAdapter.setLastExpandedGroupPosition(groupPosition);
		}

	};

	private View.OnTouchListener mAutoCompleteTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			mAutoComplete.setText("");
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mAutoComplete, 0);
			return false;
		}
	};

	private OnItemClickListener mAutoCompleteListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			onInventoryItemSelectedEvent((Item)arg0.getItemAtPosition(arg2));
		}

	};

	private OnChildClickListener mRoomItemSelectedListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, 
				int childPosition, long id) {
			onInventoryItemSelectedEvent((Item)v.findViewById(R.id.child_text).getTag());
			return false;
		}
	};

	// called either when user select an inventory item from expandable list
	// or from search list
	private void onInventoryItemSelectedEvent(Item param) {
		mItemQty.setText("1");
		mCubicMetres.setText(param.CubicMeterage);
		mLatestItemLabel.setText(param.Name);
		mLatestItemLabel.setTag(param);
	}

	/**
	 * Pragma mark -
	 * Pragma Crate List
	 */	
	private View.OnClickListener mShowAllListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = v.getTag().toString();

			int visibility = 0;		// set visible after show, otherwise button is null
			if (tag.compareTo(ItemInDTO.CARTON) == 0) {
				mShowAllDialog.setTitle(getResources().getString(R.string.tv_cartons));
				visibility = View.GONE;
			} else if (tag.compareTo(ItemInDTO.COVER) == 0) {
				mShowAllDialog.setTitle(getResources().getString(R.string.tv_covers));
				visibility = View.GONE;
			} else if (tag.compareTo(ItemInDTO.CRATE) == 0) {
				mShowAllDialog.setTitle(getResources().getString(R.string.tv_crates));
				visibility = View.VISIBLE;
			}	
			mAddOnAdapter.setItems(tag);
			mAddOnAdapter.notifyDataSetChanged();

			mShowAllDialog.show();
			mShowAllDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setVisibility(visibility);

		}
	};

	private DialogInterface.OnClickListener mShowAllDialogListener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			refreshRight();
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				break;
			case DialogInterface.BUTTON_NEGATIVE:
				mAddCrateDialog.show();
				break;
			}
		}

	};

	private TextWatcher mCrateVolumeWatcher = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mCustomCrateCubic.setText(ModelFactory.getUtilService().printFormat(ModelFactory.getPRVService().
					calculateVolume(mCustomCrateLength.getText().toString(),
							mCustomCrateWidth.getText().toString(),
							mCustomCrateDepth.getText().toString())));
			/*
			mCustomCrateCubic.setText(String.format(FORMAT, ModelFactory.getPRVService().
					calculateVolume(mCustomCrateLength.getText().toString(),
							mCustomCrateWidth.getText().toString(),
							mCustomCrateDepth.getText().toString())));*/
		}

	};

	// --------------- Add Crate dialog listeners
	private View.OnClickListener mAddCrateButtonListener = new View.OnClickListener() {
		public void onClick(View v) {
			mAddCrateDialog.show();
		}
	};

	private DialogInterface.OnClickListener mAddCrateListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				if (mCustomCrateName.getText().length() == 0 ||
				mCustomCrateLength.getText().length() == 0 ||
				mCustomCrateWidth.getText().length() == 0 ||
				mCustomCrateDepth.getText().length() == 0) {
					return;
				}

				ItemInDTO inDTO = new ItemInDTO();
				inDTO.Length = mCustomCrateLength.getText().toString();				
				inDTO.Width = mCustomCrateWidth.getText().toString();
				inDTO.Depth = mCustomCrateDepth.getText().toString();
				inDTO.Name = mCustomCrateName.getText().toString();				
				inDTO.CubicMeterage = mCustomCrateCubic.getText().toString();
				inDTO.IsCustom = ItemInDTO.IS_CUSTOM;
				inDTO.ItemType = ItemInDTO.CRATE;
				inDTO.ID = --PRVForm.newItemId;
				inDTO.setQty(1);

				ModelFactory.getPRVService().addCrate(inDTO);

				mCustomCrateLength.setText("");
				mCustomCrateWidth.setText("");
				mCustomCrateDepth.setText("");				
				mCustomCrateName.setText("");

				refreshRight();

				// show ShowAllDialog
				findViewById(R.id.tv_crates_showall).performClick();
				break;
			}
		}
	};

	public void incrementText(View v) {
		onIncrementDecrementText(v, PRVService.PLUS, 1);
	}

	public void decrementText(View v) {
		onIncrementDecrementText(v, PRVService.MINUS, 1);
	}

	private void onIncrementDecrementText(View v, int sign, int value) {
		EditText text = (EditText)v.getTag();
		int val = Integer.parseInt(text.getText().toString());

		if (sign == PRVService.PLUS) { val += value; } 
		else { val -= value; }

		if (val < 0) return;

		ItemInDTO inDTO = (ItemInDTO)text.getTag();
		if (!ModelFactory.getPRVService().validateLimit(inDTO, value, sign)) return;
		
		text.setText("" + val);

		//System.out.println(inDTO.Name + " " + inDTO.getQty());
		inDTO.setQty(val);
		ModelFactory.getPRVService().updateLimit(inDTO, value, sign);
		mRunningTotal.setText(ModelFactory.getUtilService().printFormat(
				ModelFactory.getPRVService().getRoomRunningTotal(
						ModelFactory.getPRVService().getCurrentRoom())));
		/*
		mRunningTotal.setText(String.format(FORMAT, 
				ModelFactory.getPRVService().getRoomRunningTotal(
						ModelFactory.getPRVService().getCurrentRoom())));*/
	}

	public View.OnLongClickListener onLongIncrementListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			onIncrementDecrementText(v, PRVService.PLUS, 10);
			return true;
		}
	};

	public View.OnLongClickListener onLongDecrementListener = new View.OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			onIncrementDecrementText(v, PRVService.MINUS, 10);
			return true;
		}
	};

	/**
	 * Pragma mark -
	 * Pragma Room List
	 */
	public void onAddRoomClickListener(View v) {
		mSelectRoomDialog.show();
	}

	private DialogInterface.OnClickListener mAddRoomListener = new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				if (ModelFactory.getPRVService().getCurrentRoom() == null) {
					ModelFactory.getPRVService().startTimeStamp(PRVItemCollectionActivity.this);
				}
				ModelFactory.getPRVService().initRoomSummaryInDTO(
						mSelectRoomTable.getCheckedRadioButtonValue());
				refreshContent();
				((PRVAddedContentAdapter)mAddedContentList.getAdapter()).notifyDataSetChanged();
				ModelFactory.getPRVService().saveFormData(PRVItemCollectionActivity.this);
				break;
				/*case DialogInterface.BUTTON_NEGATIVE:
				if (ModelFactory.getPRVService().getCurrentRoom() == null) {
					Intent i = new Intent(getApplicationContext(), PRVFragmentActivity.class);
					startActivity(i);
					return;
				}
				refreshContent();
				break;*/
			}
		}

	};

	private DialogInterface.OnDismissListener mAddRoomDismissListener = new DialogInterface.OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
			if (ModelFactory.getPRVService().getCurrentRoom() == null) {
				Intent i = new Intent(getApplicationContext(), PRVFragmentActivity.class);
				startActivity(i);
				return;
			}
		}
	};

	private AlertDialog dialogFromBuilder(int layoutId) {
		AlertDialog.Builder builder;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, null);

		switch (layoutId) {
		case R.layout.prv_dialog_select_room:
			mSelectRoomTable = (SelectRoomCustomTableLayout) 
			layout.findViewById(R.id.sr_radio_group);
			break;
		case R.layout.prv_dialog_custom_item:
			mCustomItemName = (EditText) layout.findViewById(R.id.aci_et_name);
			mCustomItemLength = (EditText) layout.findViewById(R.id.aci_et_length);
			mCustomItemWidth = (EditText) layout.findViewById(R.id.aci_et_width);
			mCustomItemDepth = (EditText) layout.findViewById(R.id.aci_et_depth);
			mCustomItemCubic = (EditText) layout.findViewById(R.id.aci_et_cubic);

			mCustomItemLength.addTextChangedListener(mItemVolumeWatcher);
			mCustomItemWidth.addTextChangedListener(mItemVolumeWatcher);
			mCustomItemDepth.addTextChangedListener(mItemVolumeWatcher);
			break;
		case R.layout.prv_dialog_custom_crate:
			mCustomCrateName = (EditText) layout.findViewById(R.id.ac_et_name);
			mCustomCrateLength = (EditText) layout.findViewById(R.id.ac_et_length);
			mCustomCrateWidth = (EditText) layout.findViewById(R.id.ac_et_width);
			mCustomCrateDepth = (EditText) layout.findViewById(R.id.ac_et_depth);
			mCustomCrateCubic = (EditText) layout.findViewById(R.id.ac_et_cubic);

			mCustomCrateLength.addTextChangedListener(mCrateVolumeWatcher);
			mCustomCrateWidth.addTextChangedListener(mCrateVolumeWatcher);
			mCustomCrateDepth.addTextChangedListener(mCrateVolumeWatcher);
			break;
		case R.layout.prv_dialog_show_all:
			mShowAllTable = (TableLayout)layout.findViewById(R.id.sa_layout_root);
			break;
		}

		builder = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
		builder.setView(layout);
		return builder.create();
	}

	private void createSelectRoomDialog() {
		mSelectRoomDialog = dialogFromBuilder(R.layout.prv_dialog_select_room);
		mSelectRoomDialog.setTitle(getResources().getString(R.string.sr_title));

		mSelectRoomDialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_ok), 
				mAddRoomListener);

		mSelectRoomDialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), 
				mAddRoomListener);
	}

	private void createAddCrateDialog() {
		mAddCrateDialog = dialogFromBuilder(R.layout.prv_dialog_custom_crate);
		mAddCrateDialog.setTitle(getResources().getString(R.string.ac_title));
		mAddCrateDialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_ok), 
				mAddCrateListener);
		mAddCrateDialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), 
				mAddCrateListener);
	}

	private void createAddCustomItemDialog() {
		mCustomItemDialog = dialogFromBuilder(R.layout.prv_dialog_custom_item);
		mCustomItemDialog.setTitle(getResources().getString(R.string.aci_title));
		mCustomItemDialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_ok), 
				mAddCustomItemListener);
		mCustomItemDialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				getResources().getString(R.string.btn_cancel), 
				mAddCustomItemListener);
	}

	private void createShowAllDialog() {		
		mShowAllDialog = dialogFromBuilder(R.layout.prv_dialog_show_all);
		mShowAllDialog.setButton(AlertDialog.BUTTON_POSITIVE, 
				getResources().getString(R.string.btn_ok), 
				mShowAllDialogListener);
		mShowAllDialog.setButton(AlertDialog.BUTTON_NEGATIVE, 
				getResources().getString(R.string.tv_add_crate),
				mShowAllDialogListener);
	}

	private void refreshLeft() {
		RoomOutDTO currentRoom = ModelFactory.getPRVService().getCurrentRoom();
		mRoomNameLabel.setText(currentRoom.Name);
		mRunningTotal.setText(ModelFactory.getUtilService().printFormat(
				ModelFactory.getPRVService().getRoomRunningTotal(currentRoom)));
		/*
		mRunningTotal.setText(String.format(FORMAT, 
				ModelFactory.getPRVService().getRoomRunningTotal(currentRoom)));*/
		mCubicMetres.setText("");
		mItemQty.setText("");
		mLatestItemLabel.setText("");
		mLatestItemLabel.setTag(null);

		// default expandable list expanded
		if (currentRoom.ID > 0) {
			int position = 0;
			while (currentRoom.ID != mRoomAdapter.getGroupId(position) && position < mRoomAdapter.getGroupCount()) {
				position++;
			}
			mRoomList.expandGroup(position);
		} else {
			mRoomList.expandGroup(0);
		}
	}

	private void refreshRight() {		
		RoomOutDTO currentRoom = ModelFactory.getPRVService().getCurrentRoom();
		ItemInDTO item = currentRoom.getCartonList().get(0);
		mCarton1.setText(item.Name);
		mCartonTxt1.setText("" + item.getQty());
		mCartonTxt1.setTag(item);

		item = currentRoom.getCartonList().get(1);
		mCarton2.setText(item.Name);
		mCartonTxt2.setText("" + item.getQty());
		mCartonTxt2.setTag(item);

		item = currentRoom.getCartonList().get(2);
		mCarton3.setText(item.Name);
		mCartonTxt3.setText("" + item.getQty());
		mCartonTxt3.setTag(item);

		item = currentRoom.getCoverList().get(0);
		mCover1.setText(item.Name);
		mCoverTxt1.setText("" + item.getQty());
		mCoverTxt1.setTag(item);

		item = currentRoom.getCoverList().get(1);
		mCover2.setText(item.Name);
		mCoverTxt2.setText("" + item.getQty());
		mCoverTxt2.setTag(item);

		item = currentRoom.getCoverList().get(2);
		mCover3.setText(item.Name);
		mCoverTxt3.setText("" + item.getQty());
		mCoverTxt3.setTag(item);

		if (currentRoom.getCrateList().size() == 0) {
			mCrateRow.setVisibility(View.GONE);
		} else {
			mCrateRow.setVisibility(View.VISIBLE);
			item = currentRoom.getCrateList().get(0);
			mCrate.setText(item.Name);
			mCrateTxt.setText("" + item.getQty());
			mCrateTxt.setTag(item);
		}
	}

	private void refreshContent() {
		refreshLeft();
		refreshRight();
	}
		
	public void isD2D(View v) {
		ModelFactory.getPRVService().setServiceModeD2D(true);
		ModelFactory.getPRVService().initPRVInDTO();
		
		v.setEnabled(false);
		((Button)v).setTextAppearance(this, R.style.TollButton_Bold);
		findViewById(R.id.prv_btn_d2s).setEnabled(true);
		((Button)findViewById(R.id.prv_btn_d2s)).setTextAppearance(this, R.style.TollButton);
		
		if (ModelFactory.getPRVService().getCurrentRoom() == null) {
			mSelectRoomDialog.show();
		} else {
			refreshContent();
		}
	}
	
	public void isD2S(View v) {
		ModelFactory.getPRVService().setServiceModeD2D(false);
		ModelFactory.getPRVService().initPRVInDTO();
		
		v.setEnabled(false);
		((Button)v).setTextAppearance(this, R.style.TollButton_Bold);
		findViewById(R.id.prv_btn_d2d).setEnabled(true);
		((Button)findViewById(R.id.prv_btn_d2d)).setTextAppearance(this, R.style.TollButton);
		
		if (ModelFactory.getPRVService().getCurrentRoom() == null) {
			mSelectRoomDialog.show();
		} else {
			refreshContent();
		}
	}
}
