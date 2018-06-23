package com.contactpoint.view;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

import com.contactpoint.ci_prv_mm.R;

public class TollCustomKeyboard extends KeyboardView  {
		
	private Keyboard mKeyboard;
	
	public TollCustomKeyboard(Context context, AttributeSet attrs) {
		super(context, attrs);
		mKeyboard = new Keyboard(context, R.xml.toll_custom_numeric_keypad);
		setKeyboard(mKeyboard);
	}
	
	public void setKeyboardLayout(View v) {
		RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams)getLayoutParams();
		param.addRule(RelativeLayout.BELOW, v.getId());
		param.addRule(RelativeLayout.ALIGN_LEFT, v.getId());
	}
	
	public void showWithAnimation(Animation animation) {
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				setVisibility(View.VISIBLE);
			}
		});

		setAnimation(animation);
	}	
	
}
