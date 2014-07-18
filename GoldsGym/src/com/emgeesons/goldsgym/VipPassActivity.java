package com.emgeesons.goldsgym;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class VipPassActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_vip_pass);
		setTitle(" VIP Pass");
		
		//Step 1: User has to provide 5 references to get the VIP Pass
		//Step 2: After user enters the 5 references, the GET VIP Pass button should be visible (before that it is hidden)
		// Step 3: When user clicks on Get VIP Pass, we need to call web service and send these references
		//Web service to call: http://emgeesonsdevelopment.in/goldsgym/mobile2.0/getVipPass.php
		//Parameters to Pass - Source - ANDROID, UID, FN, LN, Email, Mobile
		// for references - FN1, LN1, M1 ..... FN5, LN5, M5
		//SharedPreferences ggPrefs;
		//ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		//ggPrefs.getString(LaunchAppActivity.NAME, "No Name"); //LaunchAppActivity.USER_ID // LaunchAppActivity.NUMBER // LaunchAppActivity.EMAIL
		// Step 4: If response is success, show success message
	    
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		 // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
