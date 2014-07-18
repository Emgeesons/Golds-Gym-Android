package com.emgeesons.goldsgym;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class DisclaimerActivity extends Activity {

	private Intent nextScreenIntent;
	SharedPreferences ggPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Shared Preferences
		 Context context = getApplicationContext();
		 ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
			 setContentView(R.layout.activity_disclaimer);
			 
				//Forcing of the menu button to action overflow
				try {
					ViewConfiguration config = ViewConfiguration.get(this);
					Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
					if(menuKeyField != null) {
					    menuKeyField.setAccessible(true);
					    menuKeyField.setBoolean(config, false);
					}
				} catch (Exception ex) {
					// Ignore
				}
				 
		 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.disclaimer, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_disclaimer:
	        	ggPrefs.edit().putBoolean(LaunchAppActivity.DISCLAIMER, true).commit();
	        	nextScreenIntent = new Intent(this,AboutUserActivity.class);
		    	startActivity(nextScreenIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
}
