package com.contactpoint.model.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.contactpoint.ci_prv_mm.MainActivity;

public class AutoLogoutReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(MainActivity.DAILY_EXPIRE, true);
		context.startActivity(i);
	}

}
