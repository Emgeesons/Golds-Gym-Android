package com.emgeesons.goldsgym;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.EditText;

public class AdvancedProfileActivity extends Activity {
	
	SharedPreferences ggPrefs;
	Context context;
	EditText smmText, pbfText;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_profile);
		context = getApplicationContext();
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		//Back Button
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
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
		instantiateVariables();
		
	}
	

	
	private void instantiateVariables(){
		smmText = (EditText) findViewById (R.id.smm_text);
		pbfText = (EditText) findViewById (R.id.pbf_text);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.adv_profile, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		 // Handle item selection
	    switch (item.getItemId()) 
	    {
	    	case R.id.action_body_stats:
	    			updateValues();
	    			onBackPressed();
	            return true;
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	private void updateValues(){

    	int decimalPart = (((LaunchAppActivity.currentDay%30) * 100)/30);
    	String dP = Integer.toString(decimalPart);
    	if(dP.length() == 1)
    		dP="0"+dP;
    	String monthValue = Integer.toString(LaunchAppActivity.currentMonth - 1)+"."+dP;
    	
		//SMM
		if(smmText.getText().toString().length() > 0){
			LaunchAppActivity.smmPrefs.edit().putFloat(monthValue, Float.parseFloat(smmText.getText().toString())).commit();
		}
		//PBF
		if(pbfText.getText().toString().length() > 0){
			LaunchAppActivity.pbfPrefs.edit().putFloat(monthValue, Float.parseFloat(pbfText.getText().toString())).commit();
		}
		ggPrefs.edit().putInt(LaunchAppActivity.ADVANCED_PROFILE, 10).commit();
	}

}
