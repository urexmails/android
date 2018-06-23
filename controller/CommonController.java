package com.contactpoint.controller;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.contactpoint.ci_prv_mm.DashboardActivity;
import com.contactpoint.ci_prv_mm.MainActivity;
import com.contactpoint.model.ModelFactory;
import com.contactpoint.model.service.AuthenticationService;

public class CommonController {

	public static void backToDashboard(Activity act) {
		Intent i = new Intent(act.getApplicationContext(), DashboardActivity.class);
		act.startActivity(i);
	}

	public static void logout(Activity act) {
		// save CI & PRV before logout
		ModelFactory.getCIService().saveFormData(act);
		ModelFactory.getPRVService().saveFormData(act);
		
		ModelFactory.getCIService().deallocateMemory();
		ModelFactory.getPRVService().deallocateMemory();
		
		System.gc();
		
		Intent i = new Intent(act.getApplicationContext(), MainActivity.class);
		i.putExtra(MainActivity.USERNAME, ModelFactory.getCIService()
				.getAuthenticationToken().username.replace(AuthenticationService.DOMAIN, ""));
		ModelFactory.getCIService().setAuthenticationToken(null);
		ModelFactory.getPRVService().setAuthenticationToken(null);
		ModelFactory.getPhotoService().setAuthenticationToken(null);
		act.startActivity(i);
	}

	public static void refreshTable(Context context, TableLayout table, 
			int header, BaseAdapter adapter) {
		table.removeAllViews();

		// add header
		try {
			table.addView((View)table.getTag());
		} catch (Exception e) {
			LayoutInflater inflater = LayoutInflater.from(context);
			TableRow row = (TableRow) inflater.inflate(header, null, false);
			table.addView(row);
			table.setTag(row);
		}

		for (int i = 0; i < adapter.getCount(); i++) {
			table.addView(adapter.getView(i, null, table));
		}
	}

	// updates the date in the edit text
	public static void updateDisplay(EditText target, int dayOfMonth, int monthOfYear, int year) {
		// Month is 0 based so add 1
		monthOfYear += 1;
		target.setText(
				new StringBuilder()
				.append(dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth).append("/")
				.append(monthOfYear < 10 ? "0" + monthOfYear : monthOfYear).append("/")
				.append(year)
				);
	}
	
	public static void updateDisplay(EditText target, int hourOfDay, int minute) {
		target.setText(new StringBuilder()
		.append(hourOfDay < 10 ? "0" + hourOfDay : hourOfDay).append(":")
		.append(minute < 10 ? "0" + minute : minute));
	}
	
	public static AlertDialog dialogFromBuilder(Context context, int layoutId, int themeId) {
		AlertDialog.Builder builder;

		LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(layoutId, null);

		layout.setOnTouchListener(OnDialogTouchListener);
		builder = new AlertDialog.Builder(new ContextThemeWrapper(context, themeId));
		builder.setView(layout);
		return builder.create();
	}
	
	public static View.OnTouchListener OnDialogTouchListener = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(!(v instanceof EditText)){
				InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
			}
			
			return false;
		}
	};
	
	public static <T> void setSpinnerAdapter(Context context, Spinner spinner, List<T> items) {
		ArrayAdapter <T> adapter = new ArrayAdapter<T>(context, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
			
}
