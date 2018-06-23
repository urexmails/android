package com.contactpoint.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.contactpoint.ci_prv_mm.R;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.client.PRVForm;
import com.contactpoint.model.client.prv.Room;

/**
 * @author diego
 *
 */
public class SelectRoomCustomTableLayout extends TableLayout implements OnClickListener {

//    private static final String TAG = "SelectRoomCustomTableLayout";
    private RadioButton activeRadioButton;
    private EditText otherEditText;

    /** 
     * @param context
     */
    public SelectRoomCustomTableLayout(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public SelectRoomCustomTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        int index = 0;
        TableRow row = new TableRow(context);
        RadioButton rd = null;
        boolean isMM = ModelFactory.getPRVService().getCurrentForm().isMM();
        for (Room outDTO : ModelFactory.getPRVService().getReferenceDataOutDTO(isMM).rooms) {
        	rd = new RadioButton(context);
        	rd.setTextColor(context.getResources().getColor(R.color.black));
        	rd.setText(outDTO.Name);
        	rd.setTag(outDTO);
        	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
        		rd.setPadding(15, 10, 0, 10);
        	}
        	rd.setOnClickListener(this);
        	
        	row.addView(rd);
        	
        	if (++index % 2 == 0) {
        		addView(row);
        		row = new TableRow(context);
        	}
        	
        	if (activeRadioButton == null) {
        		activeRadioButton = rd;
        		activeRadioButton.setChecked(true);
        	}
        }
        
        // 'other' check box
        LinearLayout container = new LinearLayout(context);
        rd = new RadioButton(context);
    	rd.setTextColor(context.getResources().getColor(R.color.black));
    	rd.setText(context.getResources().getString(R.string.sr_other));
        rd.setOnClickListener(this);
    	if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
    		rd.setPadding(15, 10, 0, 10);
    		setPadding(50, 10, 0, 10);
    	}
        otherEditText = new EditText(context);
        otherEditText.setEms(10);
        otherEditText.setBackgroundResource(R.drawable.toll_textbox_black);
        otherEditText.setEnabled(false);
        container.addView(rd);
        container.addView(otherEditText);
        row.addView(container);
		addView(row);
    }

    public void onClick(View v) {
        final RadioButton rb = (RadioButton) v;
        if ( activeRadioButton != null ) {
            activeRadioButton.setChecked(false);
        }
        rb.setChecked(true);
        activeRadioButton = rb;
        if (activeRadioButton.getText().toString()
        		.compareTo(getContext().getResources().getString(R.string.sr_other)) == 0) {
        	otherEditText.setEnabled(true);
        } else {
        	otherEditText.setEnabled(false);
        }
    }
    
    public Room getCheckedRadioButtonValue() {
    	if (activeRadioButton.getTag() != null) {
    		return (Room) activeRadioButton.getTag();
    	} else {
    		Room room = new Room();
    		room.ID = --PRVForm.newRoomId;
    		room.Name = otherEditText.getText().toString();
    		return room;
    	}
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, int index,
            android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
    }


    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
    }

}