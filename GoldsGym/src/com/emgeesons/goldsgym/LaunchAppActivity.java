package com.emgeesons.goldsgym;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LaunchAppActivity extends Activity {

	private Intent nextScreenIntent;
	SharedPreferences ggPrefs;

	public static final String DISCLAIMER= "DISCLAIMER";
	
	//Signup
	public static final String USER_ID = "USER_ID";
	public static final String NAME = "NAME";
	public static final String AREA = "AREA";
	public static final String NUMBER = "NUMBER";
	public static final String AGE = "AGE";
	public static final String DOB = "DOB";
	public static final String GENDER = "GENDER";
	public static final String HEIGHT = "HEIGHT";
	public static final String EMAIL = "EMAIL";
	public static final String GOAL_STAGE = "GOAL_STAGE";
	
	//Medical Conditions
	public static final String HYPERTENSION = "HYPERTENSION";
	public static final String DIABETES = "DIABETES";
	public static final String LOWER_BACK = "LOWER_BACK";
	public static final String SPONDILYTIS = "SPONDILYTIS";
	public static final String KNEE = "KNEE";
	public static final String WRIST = "WRIST";
	public static final String PREGNANCY = "PREGNANCY";
	public static final String THYROID = "THYROID";
	public static final String CHOLESTEROL = "CHOLESTEROL";
	public static final String PCOS = "PCOS";
	public static final String OTHER_MEDICAL = "OTHER_MEDICAL";
	
	//Other
	public static final String WEIGHT_AMOUNT = "WEIGHT_AMOUNT";
	public static final String WEIGHT_BEGIN = "WEIGHT_BEGIN";
	public static final String WEIGHT_25 = "WEIGHT_25";
	public static final String WEIGHT_50 = "WEIGHT_50";
	public static final String WEIGHT_75 = "WEIGHT_75";
	public static final String WEIGHT_100 = "WEIGHT_100";

	public static final String WEIGHT_25_ELAPSED = "WEIGHT_25_ELAPSED";
	public static final String WEIGHT_50_ELAPSED = "WEIGHT_50_ELAPSED";
	public static final String WEIGHT_75_ELAPSED = "WEIGHT_75_ELAPSED";
	public static final String PERFECT_MONTH_ELAPSED = "PERFECT_MONTH_ELAPSED";
	public static final String PERFECT_WEEK_ELAPSED = "PERFECT_WEEK_ELAPSED";
	public static final String CURRENT_WEIGHT = "CURRENT_WEIGHT";
	public static final String CURRENT_WEIGHT_MONTH = "CURRENT_WEIGHT_MONTH";
	public static final String TRAINING_PROGRAM = "TRAINING_PROGRAM";
	public static final String TRAINING_LEVEL = "TRAINING_LEVEL"; 
	public static final String VEG_NONVEG = "VEG_NONVEG";
	public static final String TOTAL_WORKOUTS = "TOTAL_WORKOUTS";
	public static final String WORKOUTS_MISSED = "WORKOUTS_MISSED";
	public static final String END_DATE = "END_DATE";
	public static final String START_DATE = "START_DATE";
	public static final String NO_MONTHS = "NO_MONTHS";
	public static final String VACATION_END_DATE = "VACATION_END_DATE";
	public static final String MAINTENANCE = "MAINTENANCE";
	
	//Ticks
	public static final String MONDAY_TICK = "MONDAY_TICK";
	public static final String TUESDAY_TICK = "TUESDAY_TICK";
	public static final String WEDNESDAY_TICK = "WEDNESDAY_TICK";
	public static final String THURSDAY_TICK = "THURSDAY_TICK";
	public static final String FRIDAY_TICK = "FRIDAY_TICK";
	public static final String SATURDAY_TICK = "SATURDAY_TICK";
	public static final String SUNDAY_TICK = "SUNDAY_TICK";
	public static final String ADVANCED_PROFILE = "ADVANCED_PROFILE";
	public static final String PERFECT_WEEK = "PERFECT_WEEK";
	public static final String PERFECT_MONTH = "PERFECT_MONTH";


	public static final String YESTERDAYS_WORKOUT = "YESTERDAYS_WORKOUT";
	public static final String TODAYS_WORKOUT = "TODAYS_WORKOUT";
	public static final String CHANGE_ROUTINE = "CHANGE_ROUTINE";
	
	protected static final String OFFERS_COACHMARK = "OFFERS_COACHMARK";
	
	protected static int currentWeek, currentMonth, daysLeft, currentDay, currentDOW;
	protected static boolean vacation=false, achievement = false;
	
	protected static SharedPreferences weightPrefs, smmPrefs, pbfPrefs;
	protected static SharedPreferences notifPrefs, tickPrefs;
	
	// Splash screen timer
    private static int SPLASH_TIME_OUT = 1700;
    ImageView logoImage;
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		  setContentView(R.layout.activity_launch_app);
		  logoImage = (ImageView) findViewById(R.id.gg_logo);
		  logoImage.startAnimation(animRotate);
		  
	        new Handler().postDelayed(new Runnable() {
	 
	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */
	 
	            @Override
	            public void run() {
	                // This method will be executed once the timer is over
	                // Start your app main activity
	            	//if user has not agreed to the disclaimer 
	       		 if(!ggPrefs.getBoolean(LaunchAppActivity.DISCLAIMER, false)){
	                Intent i = new Intent(LaunchAppActivity.this, DisclaimerActivity.class);
	                startActivity(i);
	       		 }else{ //user has agreed to the disclaimer
	    			 //check if the user has registered - Not Registered
	    			 if(ggPrefs.getString(LaunchAppActivity.USER_ID, "-1").equalsIgnoreCase("-1")){
	    				 nextScreenIntent = new Intent(LaunchAppActivity.this,AboutUserActivity.class);
	    			     startActivity(nextScreenIntent);
	    			 }else{//registered
	    				 nextScreenIntent = new Intent(LaunchAppActivity.this,HomeScreenActivity.class);
	    			     startActivity(nextScreenIntent);
	    			 }
	    			 
	    			 
	    		 }
	                // close this activity
	                finish();
	            }
	        }, SPLASH_TIME_OUT);
	        
		//Shared Preferences
		 Context context = getApplicationContext();
		 ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		 
		 //Other Prefs
		 weightPrefs = context.getSharedPreferences("weightPrefs",Context.MODE_PRIVATE);
		 smmPrefs = context.getSharedPreferences("smmPrefs",Context.MODE_PRIVATE);
		 pbfPrefs = context.getSharedPreferences("pbfPrefs",Context.MODE_PRIVATE);
		 tickPrefs = context.getSharedPreferences("tickPrefs",Context.MODE_PRIVATE);
		 notifPrefs = context.getSharedPreferences("notifPrefs",Context.MODE_PRIVATE);
		 
		 
	}
}
