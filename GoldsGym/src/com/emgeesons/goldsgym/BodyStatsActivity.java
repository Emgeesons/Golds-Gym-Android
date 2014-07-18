package com.emgeesons.goldsgym;

import java.lang.reflect.Field;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BodyStatsActivity extends Activity {

	private Intent nextScreenIntent;
	TextView bodyNote, bodySubNote;
	EditText feetText, inchesText, weightText;
	SeekBar bmiBar;
	double bmiResult, weightResult, metres, ibwResult;
	float weight;
	int feet, inches, bmiBarProgress;
	SharedPreferences ggPrefs;
	Context context;
	String overUnderWeight, heightString="";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body_stats);
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
		implementHandlers();
		
	}
	
	private void instantiateVariables(){
		bodyNote = (TextView) findViewById(R.id.body_note);
		bodySubNote = (TextView) findViewById(R.id.body_sub_note);
		feetText = (EditText) findViewById(R.id.feet_text);
		inchesText = (EditText) findViewById(R.id.inches_text);
		weightText = (EditText) findViewById(R.id.weight_text);
		bmiBar = (SeekBar) findViewById (R.id.bmi_bar);
		bmiBar.setEnabled(false);
	}
	
	

	
	private void implementHandlers(){//Even inches needs to be entered
		 weightText.setOnFocusChangeListener(new OnFocusChangeListener() {          
		        public void onFocusChange(View v, boolean hasFocus) {
		            if(feetText.getText().toString().length()>=1 && inchesText.getText().toString().length() >=1 && weightText.getText().toString().length() >=1){
		            	updateBmi();
		            }//end of hasFocus condition
		            else{
		            	bodySubNote.setText("");
		            	bodyNote.setText(getResources().getString(R.string.body_note));
		            }
		        }//end of onFocusChanged function
		    });//end of FocusChangeListener Definition
		 feetText.setOnFocusChangeListener(new OnFocusChangeListener() {          
		        public void onFocusChange(View v, boolean hasFocus) {
		            if(feetText.getText().toString().length()>=1 && inchesText.getText().toString().length() >=1 && weightText.getText().toString().length() >=1){
		            	updateBmi();
		            }//end of hasFocus condition  
		            else{
		            	bodySubNote.setText("");
		            	bodyNote.setText(getResources().getString(R.string.body_note));
		            }
		        }//end of onFocusChanged function
		    });//end of FocusChangeListener Definition
		 inchesText.setOnFocusChangeListener(new OnFocusChangeListener() {          
		        public void onFocusChange(View v, boolean hasFocus) {
		            if(feetText.getText().toString().length()>=1 && inchesText.getText().toString().length() >=1 && weightText.getText().toString().length() >=1){
		            	updateBmi();
		            }//end of hasFocus condition  
		            else{
		            	bodySubNote.setText("");
		            	bodyNote.setText(getResources().getString(R.string.body_note));
		            }
		        }//end of onFocusChanged function
		    });//end of FocusChangeListener Definition
		 
		 weightText.addTextChangedListener(new TextWatcher(){
		        public void afterTextChanged(Editable s) {
		        }
		        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
		        public void onTextChanged(CharSequence s, int start, int before, int count){
		        	if(s.length()>=2 && feetText.getText().toString().length()>=1 && inchesText.getText().toString().length() >=1)
		        		updateBmi();
		        }
		    });
		
	}
	
	private void setHeightWeight(){
		if(feetText.getText().toString().length()>=1)
			heightString= feetText.getText().toString()+" Ft";
		if(inchesText.getText().toString().length()>=1)
			heightString += "  "+ inchesText.getText().toString()+" In";
		ggPrefs.edit().putString(LaunchAppActivity.HEIGHT, heightString).commit();
		ggPrefs.edit().putFloat(LaunchAppActivity.WEIGHT_BEGIN, Float.parseFloat(weightText.getText().toString())).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.CURRENT_WEIGHT_MONTH, 1).commit();
		ggPrefs.edit().putFloat(LaunchAppActivity.CURRENT_WEIGHT, Float.parseFloat(weightText.getText().toString())).commit();
		
		//Put current Weight in Chart
		LaunchAppActivity.weightPrefs.edit().putFloat(Double.toString(1.0 - 1.0 + 0.1), Float.parseFloat(weightText.getText().toString())).commit();
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.body_stats, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		 // Handle item selection
	    switch (item.getItemId()) 
	    {
	    	case R.id.action_body_stats:
	    		if(verifyInfo()){ 
	    			//The Below line is as good as not there as we are not commiting it. but we have done it before in set height weight
	    			LaunchAppActivity.weightPrefs.edit().putFloat("0"/*Integer.toString(LaunchAppActivity.currentDay)*/, Float.parseFloat(weightText.getText().toString()));
					 nextScreenIntent = new Intent(BodyStatsActivity.this,DietaryRecallActivity.class);
				     startActivity(nextScreenIntent);
	    		}else{
	    			Toast toast = Toast.makeText(context, "Don't Feel Shy. Enter your Information", Toast.LENGTH_LONG);
 					toast.show();
	    		}
	            return true;
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void updateBmi(){
		feet = Integer.parseInt(feetText.getText().toString());
    	inches = Integer.parseInt(inchesText.getText().toString());
    	metres = (feet*0.3048) + (inches*0.0254);
    	weight = Float.parseFloat(weightText.getText().toString());
    	bmiResult = weight/(metres*metres);
    	//IBW for MEN
    	if(ggPrefs.getString(LaunchAppActivity.GENDER, "Undefined").equalsIgnoreCase("Male")){
    		double additionLeft = metres - 1.524;
    		if(additionLeft > 0){//Man is more than 5 feet
    			ibwResult = 50 + ((additionLeft/0.0254)*2.3);
    		}else{
    			ibwResult = 50;
    		}
    	}else{//IBW for WOMEN
    		double additionLeft = metres - 1.524;
    		if(additionLeft > 0){//Man is more than 5 feet
    			ibwResult = 45.5 + ((additionLeft/0.0254)*2.3);
    		}else{
    			ibwResult = 45.5;
    		}
    	}
    	weightResult = weight - ibwResult;
    	overUnderWeight = weightResult >  0 ? "Overweight" : "Underweight";
    	//Limiting to 2 decimal places
    	bmiResult = bmiResult*100;
    	bmiResult = Math.round(bmiResult);
    	bmiResult = bmiResult / 100;
    	weightResult = Math.round(weightResult*100);
    	weightResult = weightResult/100;
    	//making sure its positive
    	weightResult = weightResult < 0 ? weightResult * (-1) : weightResult; 
    	
    	//controlling the text		
    	ggPrefs.edit().putFloat(LaunchAppActivity.WEIGHT_AMOUNT, (float) weightResult).commit();
    	ggPrefs.edit().putString(LaunchAppActivity.TRAINING_PROGRAM, overUnderWeight.equalsIgnoreCase("Overweight") ? "Weight Loss" : "Weight Gain").commit();
    	bodyNote.setText("Your BMI is "+bmiResult);
    	bodySubNote.setText("You are "+weightResult+" Kgs "+overUnderWeight);
    	
    	//controlling the seekbar
    	if(bmiResult<=18.5){
    		bmiBarProgress = (int)(16.65*bmiResult/18.5);
    	}else if(bmiResult <=25){
    		bmiBarProgress = (int)(33.32*bmiResult/25);
    	}else if(bmiResult <=30){
    		bmiBarProgress = (int)(49.99*bmiResult/30);
    	}else if(bmiResult <=35){
    		bmiBarProgress = (int)(66.66*bmiResult/35);
    	}else if(bmiResult <=40){
    		bmiBarProgress = (int)(83.33*bmiResult/40);
    	}else{
    		bmiBarProgress = (int)(100*bmiResult/50); //change this later. SET AN UPPER LIMIT FOR BMI DENOMINATOR
    	}
    	if(bmiBarProgress >= 100)
    		bmiBarProgress = 99;
    	else if(bmiBarProgress == 0)
    		bmiBarProgress = 1;
    	bmiBar.setProgress(bmiBarProgress);
    	
	}
	

	
	private boolean verifyInfo(){
		// All are Compulsory
		if(weightText.getText().toString().length() >=1 && feetText.getText().toString().length()>=1 && inchesText.getText().toString().length()>=1 ){
			//store currentWeight, weightAmount, start Date and end date
			updateBmi();
			//setDates();
			setHeightWeight();
			return true;
		}else{
			return false;
		}
	}
	
	

	
}
