package com.emgeesons.goldsgym;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ReminderReceiver extends BroadcastReceiver {

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
			Intent nextScreenIntent = new Intent(context, ReminderService.class);
		    context.startService(nextScreenIntent);
	}

}
