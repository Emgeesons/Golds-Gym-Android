package com.emgeesons.goldsgym;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.CheckBox;
import android.widget.CompoundButton;
public class MedicalConditionActivity extends Activity {
	
	private Intent nextScreenIntent;
	SharedPreferences ggPrefs;
	CheckBox checkHyper, checkDiabetese, checkBack, checkSpondilytis, checkKnee, checkWrist, checkPregnancy, checkThyroid, checkCholesterol, checkPcos, checkOther, checkNone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_medical_condition);
		
		//Shared Preferences
		 Context context = getApplicationContext();
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
	  			//Ignore
	  		}
	  		
	  		instantiateVariables();
	}
	
	
	private void instantiateVariables(){
		checkHyper = (CheckBox) findViewById (R.id.check_hyper);
		checkDiabetese = (CheckBox) findViewById (R.id.check_diabetese);
		checkBack = (CheckBox) findViewById (R.id.check_back);
		checkSpondilytis = (CheckBox) findViewById (R.id.check_spondilytis);
		checkKnee = (CheckBox) findViewById (R.id.check_knee);
		checkWrist = (CheckBox) findViewById (R.id.check_wrist);
		checkPregnancy = (CheckBox) findViewById (R.id.check_pregnancy);
		checkThyroid = (CheckBox) findViewById (R.id.check_thyroid);
		checkCholesterol = (CheckBox) findViewById (R.id.check_cholesterol);
		checkPcos = (CheckBox) findViewById (R.id.check_pcos);
		checkOther = (CheckBox) findViewById (R.id.check_other);
		checkNone = (CheckBox) findViewById (R.id.check_none);
		implementListeners();
		
	}
	

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.medical_condition, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
		    case R.id.action_medical_condition:
		    	//Store the Medical Conditions
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.HYPERTENSION,checkHyper.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.DIABETES,checkDiabetese.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.LOWER_BACK,checkBack.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.SPONDILYTIS,checkSpondilytis.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.KNEE,checkKnee.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.WRIST,checkWrist.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.PREGNANCY,checkPregnancy.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.THYROID,checkThyroid.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.CHOLESTEROL,checkCholesterol.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.PCOS,checkPcos.isChecked()).commit();
		    	ggPrefs.edit().putBoolean(LaunchAppActivity.OTHER_MEDICAL,checkOther.isChecked()).commit();
		    	if(proceed()){
			    	//Go to the next screen
			    	nextScreenIntent = new Intent(MedicalConditionActivity.this,BodyStatsActivity.class);
				    startActivity(nextScreenIntent);
		    	}else{
		    		//show a pop up
		    		showDialog();
		    	}
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private boolean proceed(){
		if(checkOther.isChecked())
			return false;
		else if(checkBack.isChecked() && checkSpondilytis.isChecked() && checkHyper.isChecked())
			return false;
		else
			return true;
	}
	
	private void showDialog(){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MedicalConditionActivity.this);
		 
		// Setting Dialog Title
		//alertDialog.setTitle("Consult a Doctor");
		alertDialog.setTitle("Sorry, Please Consult Your Doctor!");
		 
		 
		// Setting Positive "Yes" Btn
		alertDialog.setPositiveButton("OK",
		        new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		                // Write your code here to execute after dialog
		            	onBackPressed();
		            }
		        });
		// Showing Alert Dialog
		alertDialog.show();
	}
	
	private void implementListeners(){
		checkNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkHyper.setChecked(false);
				   		checkDiabetese.setChecked(false);
				   		checkBack.setChecked(false);
				   		checkSpondilytis.setChecked(false);
				   		checkKnee.setChecked(false);
				   		checkWrist.setChecked(false);
				   		checkPregnancy.setChecked(false);
				   		checkThyroid.setChecked(false);
				   		checkCholesterol.setChecked(false);
				   		checkPcos.setChecked(false);
				   		checkOther.setChecked(false);
				   	}
			   }
			});
		checkHyper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkDiabetese.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkSpondilytis.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkKnee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkWrist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkPregnancy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkThyroid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkCholesterol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkPcos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
		checkOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   	if(isChecked){
				   		checkNone.setChecked(false);
				   	}
			   }
			});
	}

}
