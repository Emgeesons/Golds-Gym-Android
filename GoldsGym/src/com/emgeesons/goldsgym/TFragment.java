package com.emgeesons.goldsgym;


import java.util.Calendar;
import java.util.Random;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnLogoutListener;
import com.sromku.simple.fb.entities.Feed;



@SuppressLint("DefaultLocale")
public class TFragment extends Fragment {
	
	SharedPreferences ggPrefs;
	ImageView getStartedButton,getStartedImage, achievementImage, facebookImage,motivationImage, resetStatsButton;
	TextView getStartedText, motivationalTitle, achievementTitle, achievementSubtitle;
	static TextView mondayExercise, tuesdayExercise, wednesdayExercise, thursdayExercise, fridayExercise, saturdayExercise, sundayExercise, yesterdayExercise;
	ImageView mondayImage, tuesdayImage, wednesdayImage, thursdayImage, fridayImage, saturdayImage, sundayImage, yesterdayImage;
	View v;
	Intent nextScreenIntent;
	Context context;
	LinearLayout welcomeCardLayout, dadLayout, guidelinesLayout, trainerLayout, mondayLayout, tuesdayLayout, wednesdayLayout, thursdayLayout, fridayLayout, saturdayLayout, sundayLayout, achievementLayout, advancedLayout;
	LinearLayout weightLayout, yesterdayLayout;
	static String program;
	static int mondayDaily, tuesdayDaily, wednesdayDaily, thursdayDaily, fridayDaily, saturdayDaily, sundayDaily;
	static boolean achievement = false;
	SharedPreferences.Editor editor;
	int day;
	static String picture, message, name, caption, description;
	
	RelativeLayout motivationLayout;
	Animation animTranslate;
	Animation animAlpha;
	Animation animScale;
	Animation animRotate;

	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		v =  inflater.inflate(R.layout.tfragment, container, false);
		context = getActivity();
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Facebook
	/*	try {
	        PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.emgeesons.goldsgym",PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            String s = Base64.encodeToString(md.digest(), Base64.DEFAULT);
	            Log.d("Your Tag", s);
	            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
	            }
	    } catch (NameNotFoundException e) {

	    } catch (NoSuchAlgorithmException e) {

	    }*/
		//Animations
		animTranslate = AnimationUtils.loadAnimation(context, R.anim.anim_translate);
	    animAlpha = AnimationUtils.loadAnimation(context, R.anim.anim_alpha);
	    animScale = AnimationUtils.loadAnimation(context, R.anim.anim_scale);
	    animRotate = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
		return v;
		
    }
	
	@Override
	public void onResume() {
		super.onResume();
		HomeScreenActivity.trainingStatus();
		welcomeGetStarted();
		motivationalQuotes();
		achievements();
		logWeight();
		//advancedProfile();
		logYesterday();
		trainer();		
		guidelines();
		dadLayout();
		
		v.startAnimation(animTranslate);
	}
	
	private void logYesterday(){
		yesterdayLayout = (LinearLayout)v.findViewById(R.id.trainer_log_workout_layout);
		yesterdayImage = (ImageView) v.findViewById(R.id.log_yesterday_image);
		yesterdayExercise = (TextView) v.findViewById(R.id.log_workout_title);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance")){
			if(LaunchAppActivity.currentDay > 1 && !LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 1), false)){
				yesterdayLayout.setVisibility(View.VISIBLE);
				yesterdayImage.setTag("Grey");
				yesterdayImage.setOnClickListener(new View.OnClickListener(){
		 		    public void onClick(View v) {
		 		    	if(((String) (yesterdayImage.getTag())).equalsIgnoreCase("Green")){ //Already Ticked
		 		    		//removing the entry - -not really required though
		 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString(LaunchAppActivity.currentDay - 1)).commit();
		 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
		 		    		yesterdayImage.setImageResource(R.drawable.grey_tickmark);
		 		    		yesterdayImage.startAnimation(animScale);
		 		    		yesterdayImage.setTag("Grey");
		 		    		yesterdayExercise.setTextColor(getResources().getColor(R.color.red));
		 		    		//depending on DOW
		 		    		switch(LaunchAppActivity.currentDOW){
		 		    			case 1://Monday
		 		    					//Do Nothing
		 		    					break;
		 		    			case 2://Tuesday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.MONDAY_TICK, false).commit();
		 		    					mondayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					mondayImage.setTag("Grey");
		 		    					mondayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 3: //Wednesday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.TUESDAY_TICK, false).commit();
		 		    					tuesdayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					tuesdayImage.setTag("Grey");
		 		    					tuesdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 4: //Thursday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.WEDNESDAY_TICK, false).commit();
		 		    					wednesdayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					wednesdayImage.setTag("Grey");
		 		    					wednesdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 5: //Friday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.THURSDAY_TICK, false).commit();
		 		    					thursdayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					thursdayImage.setTag("Grey");
		 		    					thursdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 6: // Saturday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.FRIDAY_TICK, false).commit();
		 		    					fridayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					fridayImage.setTag("Grey");
		 		    					fridayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 7: // Sunday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.SATURDAY_TICK, false).commit();
		 		    					saturdayImage.setImageResource(R.drawable.grey_tickmark);
		 		    					saturdayImage.setTag("Grey");
		 		    					saturdayImage.startAnimation(animScale);
		 		    					break;
		 		    			default: //Monday
		 		    					 //Do Nothing
		 		    					break;
		 		    		}
		 		    		achievements();
		 		    	}else{ // For Grey //Not Ticked
		 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString(LaunchAppActivity.currentDay - 1), true).commit();
		 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
		 		    		yesterdayImage.setImageResource(R.drawable.green_tickmark);
		 		    		yesterdayImage.setTag("Green");
		 		    		yesterdayImage.startAnimation(animScale);
		 		    		yesterdayExercise.setTextColor(getResources().getColor(R.color.green));
		 		    		//depending on DOW
		 		    		switch(LaunchAppActivity.currentDOW){
		 		    			case 1://Monday
		 		    					//Do Nothing
		 		    					break;
		 		    			case 2://Tuesday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.MONDAY_TICK, true).commit();
		 		    					mondayImage.setImageResource(R.drawable.green_tickmark);
		 		    					mondayImage.setTag("Green");
		 		    					mondayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 3: //Wednesday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.TUESDAY_TICK, true).commit();
		 		    					tuesdayImage.setImageResource(R.drawable.green_tickmark);
		 		    					tuesdayImage.setTag("Green");
		 		    					tuesdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 4: //Thursday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.WEDNESDAY_TICK, true).commit();
		 		    					wednesdayImage.setImageResource(R.drawable.green_tickmark);
		 		    					wednesdayImage.setTag("Green");
		 		    					wednesdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 5: //Friday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.THURSDAY_TICK, true).commit();
		 		    					thursdayImage.setImageResource(R.drawable.green_tickmark);
		 		    					thursdayImage.setTag("Green");
		 		    					thursdayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 6: // Saturday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.FRIDAY_TICK, true).commit();
		 		    					fridayImage.setImageResource(R.drawable.green_tickmark);
		 		    					fridayImage.setTag("Green");
		 		    					fridayImage.startAnimation(animScale);
		 		    					break;
		 		    			case 7: // Sunday
		 		    					ggPrefs.edit().putBoolean(LaunchAppActivity.SATURDAY_TICK, true).commit();
		 		    					saturdayImage.setImageResource(R.drawable.green_tickmark);
		 		    					saturdayImage.setTag("Green");
		 		    					saturdayImage.startAnimation(animScale);
		 		    					break;
		 		    			default: //Monday
		 		    					 //Do Nothing
		 		    					break;
		 		    		}
		 		    		achievements();
		 		    	}
		 		    	yesterdayLayout.setVisibility(View.GONE);
		 		    }
		 		});
			}else{
				yesterdayLayout.setVisibility(View.GONE);
			}
		}else{
			yesterdayLayout.setVisibility(View.GONE);
		}
		
	}
	
/*	private void advancedProfile(){
		advancedLayout = (LinearLayout) v.findViewById(R.id.trainer_advanced_profile);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") && LaunchAppActivity.currentDay > 2 && ggPrefs.getInt(LaunchAppActivity.ADVANCED_PROFILE, 0)>= 0 && ggPrefs.getInt(LaunchAppActivity.ADVANCED_PROFILE, 0) <  4 ){
		//This Card will only be shown four times
			ggPrefs.edit().putInt(LaunchAppActivity.ADVANCED_PROFILE, ggPrefs.getInt(LaunchAppActivity.ADVANCED_PROFILE, 0) + 1).commit();
			advancedLayout.setVisibility(View.VISIBLE);
			advancedLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(getActivity(),AdvancedProfileActivity.class);
			 	     startActivity(nextScreenIntent); 
			     }       
			});
		}else{
			advancedLayout.setVisibility(View.GONE);
		}
	} */
	
	private void welcomeGetStarted(){
		//Instantiating Variables
		welcomeCardLayout = (LinearLayout) v.findViewById(R.id.welcome_card_layout);
		getStartedButton = (ImageView)v.findViewById(R.id.get_started_button);
		getStartedImage = (ImageView)v.findViewById(R.id.get_started_image);
		resetStatsButton = (ImageView)v.findViewById(R.id.reset_stats_button);
		getStartedText = (TextView)v.findViewById(R.id.get_started_text);
		//First Check if the Goal has been set or not
		//Goal Stages - Undefined  //set  //begin  //maintenance  //vacation  //over //indeterminate
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Undefined")){ //Goal has not been set
			//Implementing Handlers
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
			    	HomeScreenActivity.reset();
			    	 nextScreenIntent = new Intent(getActivity(),MedicalConditionActivity.class);
				     startActivity(nextScreenIntent); 
					}
			});
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Set")){ //Goal has been set
				resetStatsButton.setVisibility(View.GONE);
				getStartedButton.setImageResource(R.drawable.lets_begin);
				getStartedImage.setImageResource(R.drawable.ur_set_icon); //this will change
				String loseGain;
				if(ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Weight Loss").equalsIgnoreCase("Weight Loss"))
					loseGain = "lose";
				else
					loseGain = "gain";
				getStartedText.setText("You need to " + loseGain + " " + ggPrefs.getFloat(LaunchAppActivity.WEIGHT_AMOUNT, 0) + " Kg(s) in " + ggPrefs.getInt(LaunchAppActivity.NO_MONTHS, 0) + "Month(s)" );
				//Implementing Handlers
				getStartedButton.setOnClickListener(new View.OnClickListener(){
				    public void onClick(View v) {
				    		createAlarm();
				    		HomeScreenActivity.setDates();
				    		ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Begun").commit();
				    		HomeScreenActivity.setTab(1);
				    }
				});
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Indeterminate")){//When the Goal is over. And user hasn't input weight
			welcomeCardLayout.setVisibility(View.GONE);
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Over")){//When the Goal is over. And the User has achieved the Goal
			welcomeCardLayout.setVisibility(View.VISIBLE);
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setImageResource(R.drawable.maintain_weight);
			getStartedImage.setImageResource(R.drawable.over_icon); //this will remain the same
			getStartedText.setText("Superb!! Way to go! You have achieved your Goal.");
			getStartedButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
		    		ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Maintenance").commit();
		    		HomeScreenActivity.trainingStatus();
		    		welcomeCardLayout.setVisibility(View.GONE);
		    		HomeScreenActivity.setTab(1);
				}
			});
			/*resetStatsButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
		        	DialogFragment resetFragment = new ResetGoalDialogFragment();
		        	resetFragment.show(getFragmentManager(),"ResetGoalDialogFragment");
				}
			});*/
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Incomplete")){//When the Goal is over. And the User has NOT achieved the Goal
			welcomeCardLayout.setVisibility(View.VISIBLE);
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setImageResource(R.drawable.reset_stats);
			getStartedImage.setImageResource(R.drawable.try_harder);
			getStartedText.setText("We missed our Target!! Let's try harder this time and make sure we get in to perfect shape");
			getStartedButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
			    	ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Undefined").commit();
			    	welcomeCardLayout.setVisibility(View.GONE);
			    	 HomeScreenActivity.reset();
			    	 nextScreenIntent = new Intent(getActivity(),MedicalConditionActivity.class);
				     startActivity(nextScreenIntent);
					}
			});
		}else{ //for begun //maintenance // vacation //indeterminate
			welcomeCardLayout.setVisibility(View.GONE);
		}
			
			
	}
	
	private void motivationalQuotes(){
		motivationLayout = (RelativeLayout) v.findViewById(R.id.motivation_layout);
		motivationImage = (ImageView) v.findViewById(R.id.motivation_image);
		motivationalTitle = (TextView) v.findViewById(R.id.motivational_title);	
		if((ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance"))){
				if(LaunchAppActivity.vacation){
					motivationalTitle.setText(getResources().getString(R.string.vacation_quote));
					int quoteLength = getResources().getString(R.string.vacation_quote).length();
					if(quoteLength < 60)
						motivationImage.setImageResource(R.drawable.t_m_1);
					else if (quoteLength < 120)
						motivationImage.setImageResource(R.drawable.t_m_2);
					else if (quoteLength < 160)
						motivationImage.setImageResource(R.drawable.t_m_3);
					else
						motivationImage.setImageResource(R.drawable.t_m_4);
				}else{
					String[] trainerQuotes = getResources().getStringArray(R.array.trainer_quotes);
					Random r = new Random();
					int index=r.nextInt(92);
					int quoteLength = trainerQuotes[index].length();
					if(quoteLength < 60)
						motivationImage.setImageResource(R.drawable.t_m_1);
					else if (quoteLength < 120)
						motivationImage.setImageResource(R.drawable.t_m_2);
					else if (quoteLength < 160)
						motivationImage.setImageResource(R.drawable.t_m_3);
					else
						motivationImage.setImageResource(R.drawable.t_m_4);
					motivationalTitle.setText(trainerQuotes[index]);
				}

		}else{
			motivationLayout.setVisibility(View.GONE);
		}
	}
	
	//check for achievements - to be shown on begun, vacation, goal reached
	private void achievements(){
		//once an achievement is seen. It will go away
		achievementLayout = (LinearLayout) v.findViewById(R.id.achievement_layout);
		if(!ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Undefined") && !ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Set")){
			LaunchAppActivity.achievement = checkForAchievements(); 
			if(LaunchAppActivity.achievement){
				achievementLayout.setVisibility(View.VISIBLE);
				motivationLayout.setVisibility(View.GONE);
				
			}else{
				achievementLayout.setVisibility(View.GONE);
			}
		}else{
			achievementLayout.setVisibility(View.GONE);
		}
	}
	
	private void achievmentInstialisation(){
		achievementImage = (ImageView) v.findViewById(R.id.achievement_image);
		achievementTitle = (TextView) v.findViewById(R.id.achievement_title);
		achievementSubtitle = (TextView) v.findViewById(R.id.achievement_sub_title);
		facebookImage = (ImageView) v.findViewById(R.id.facebook_image);
		facebookImage.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	shareOnFacebook();
		    	
			}
		});
	}
	
	private boolean checkForAchievements(){
		boolean returnValue = false;
		//checking for week. there should be only 1 perfect week
		if((!ggPrefs.getBoolean(LaunchAppActivity.PERFECT_WEEK, false))){
			if((LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay), false) && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 1), false) &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 2), false)  && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 3), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 4), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 5), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 6), false)) ||
					(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 1), false) && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 2), false) &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 3), false)  && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 4), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 5), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 6), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 7), false)) ||
					(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 2), false) && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 3), false) &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 4), false)  && 
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 5), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 6), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 7), false)  &&
					LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 8), false))){
				returnValue = true;

				if(ggPrefs.getInt(LaunchAppActivity.PERFECT_WEEK_ELAPSED, 0) == 0)
					ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_WEEK_ELAPSED, LaunchAppActivity.currentDay).commit();
				if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.PERFECT_WEEK_ELAPSED, 0) >= 2)
					ggPrefs.edit().putBoolean(LaunchAppActivity.PERFECT_WEEK, true).commit();	
				achievmentInstialisation();
				//achievementImage - - set image resource
				message = "Woohoo!! Worked out 7 Days on a Trot and it Feels Great!.";
				caption="";
				picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/perfect_week.png";
				name="Had A Perfect Week at The Gym with The Gold's Gym Android App";
				description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
				achievementTitle.setText("Congratulations. You've had a Perfect Week.");
				achievementSubtitle.setText("Now go get a perfect Month");
				achievementImage.setImageResource(R.drawable.a_icon5);
			}// no need to check for day before's and all as it it updated instantly
			//END OF CHECKING FOR WEEK
		}else if((ggPrefs.getInt(LaunchAppActivity.PERFECT_MONTH, 0) < LaunchAppActivity.currentMonth - 1)){//checking for the previous month 
			// can happen on a monthly basis .  A perfect week must take place for a perfect month to take place
			//check this month wise ( 1 - 30, 31 - 60, etc).
			boolean monthValue = true;
			int startDate = (LaunchAppActivity.currentMonth - 2)*30 + 1;
			int endDate = (LaunchAppActivity.currentMonth - 1)*30;
			for (int i = startDate; i<=endDate; i++){
				if(!LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(i), false)){
					monthValue = false;
				}
			}
			if(monthValue){
				returnValue = true;
				achievmentInstialisation();
				if(ggPrefs.getInt(LaunchAppActivity.PERFECT_MONTH_ELAPSED, 0) == 0)
					ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_MONTH_ELAPSED, LaunchAppActivity.currentDay).commit();
				if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.PERFECT_MONTH_ELAPSED, 0) >= 3)
					ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_MONTH, LaunchAppActivity.currentMonth - 1).commit();
				message = "Ain't Nothing Gonna Stop Me Now. Going for the Kill!!";
				caption="";
				picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/perfect_month.png";
				name="Had a Perfect Month at The Gym with The Gold's Gym Android App";
				description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
				achievementTitle.setText("Congratulations. You've had a Perfect Month.");
				achievementSubtitle.setText("Share this on Facebook");
				achievementImage.setImageResource(R.drawable.a_icon6);
			}
		}
		//NOW CHECKING FOR WEIGHT
		if(!returnValue){
			float currentWeight = ggPrefs.getFloat(LaunchAppActivity.CURRENT_WEIGHT, 0);
			float beginWeight = ggPrefs.getFloat(LaunchAppActivity.WEIGHT_BEGIN, 0);
			float weightAmount =  ggPrefs.getFloat(LaunchAppActivity.WEIGHT_AMOUNT, 0);
			float targetWeight, weightChanged, percentWeight;
			//weight loss
			if(ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Undefined").equalsIgnoreCase("Weight Loss") && currentWeight >0){
				targetWeight = beginWeight - weightAmount;
				weightChanged = beginWeight - currentWeight;
				percentWeight = (weightChanged/weightAmount)*100;
				if(percentWeight >= 25 && percentWeight < 50 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_25, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Starting is the most difficult part. And I'm off to a great one.";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/25_percent.png";
					name="Just Reached 25% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText("Congratulations. You've reached 25% of your goal.");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon1);
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_25_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_25_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_25_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_25, true).commit();
				}else if (percentWeight >= 50 && percentWeight < 75 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_50, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Dedication gives Results. I'm Halfway Through!.";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/50_percent.png";
					name="Just Completed 50% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached 50% of your goal");
					achievementSubtitle.setText("Lets share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon2);
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_50_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_50_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_50_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_50, true).commit();
				}else if (percentWeight >= 75 && percentWeight < 100 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_75, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Nothings Gonna Stop Me Now. Almost Reached My Ideal Body Weight";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/75_percent.png";
					name="Just Completed 75% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached 75% of your goal");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon3);
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_75_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_75_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_75_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_75, true).commit();
				}else if (/*percentWeight >= 100  && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_100, false)*/ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Over")){ // GOAL ACHIEVED -- COMPLETED MID WAY... FLAG SOMETHING HERE
					returnValue = true;
					achievmentInstialisation();
					message = "I Feel Awesome. Just Reached My Ideal Body Weight";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/goal_achieved.png";
					name="Completed My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached your goal");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon4);
					ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_100, true).commit();
				}
			}else if (ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Undefined").equalsIgnoreCase("Weight Gain")  && currentWeight > 0){
				//weight gain
				targetWeight = beginWeight + weightAmount;
				weightChanged = currentWeight - beginWeight;
				percentWeight = (weightChanged/weightAmount)*100;
				if(percentWeight >= 25 && percentWeight < 50 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_25, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Starting is the most difficult part. And I'm off to a great one.";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/25_percent.png";
					name="Just Reached 25% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached 25% of your goal.");
					achievementImage.setImageResource(R.drawable.a_icon1);
					achievementSubtitle.setText("Share this on Facebook");
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_25_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_25_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_25_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_25, true).commit();
				}else if (percentWeight >= 50 && percentWeight < 75 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_50, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Dedication gives Results. I'm Halfway Through!.";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/50_percent.png";
					name="Just Completed 50% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached 50% of your goal");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon2);
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_50_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_50_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_50_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_50, true).commit();
				}else if (percentWeight >= 75 && percentWeight < 100 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_75, false)){
					returnValue = true;
					achievmentInstialisation();
					message = "Nothings Gonna Stop Me Now. Almost Reached My Ideal Body Weight";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/75_percent.png";
					name="Just Completed 75% of My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached 75% of your goal");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon3);
					if(ggPrefs.getInt(LaunchAppActivity.WEIGHT_75_ELAPSED, 0) == 0)
						ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_75_ELAPSED, LaunchAppActivity.currentDay).commit();
					if(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.WEIGHT_75_ELAPSED, 0) >= 3)
						ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_75, true).commit();
				}else if (ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Over")/*percentWeight >= 100 && !ggPrefs.getBoolean(LaunchAppActivity.WEIGHT_100, false)*/){ // GOAL ACHIEVED -- COMPLETED MID WAY... FLAG SOMETHING HERE
					returnValue = true;
					achievmentInstialisation();
					message = "I Feel Awesome. Just Reached My Ideal Body Weight";
					caption="";
					picture="http://www.emgeesonsdevelopment.in/goldsgym/mobile1.0/achievementImages/goal_achieved.png";
					name="Completed My Goal with The Gold's Gym Android App";
					description="My Daily Fitness Guide - The All New Android Gold's Gym App acts as your personal trainer and nutritionist. Download it today.";
					achievementTitle.setText(" Congratulations. You've reached your goal");
					achievementSubtitle.setText("Share this on Facebook");
					achievementImage.setImageResource(R.drawable.a_icon4);
					ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_100, true).commit();
				}
			}
		}
		return returnValue;
	}
	
	private void logWeight(){
		//weight layout is only visible during normal training & maintenance and indeterminate state
		weightLayout = (LinearLayout)v.findViewById(R.id.trainer_log_weight_layout);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Indeterminate")){
			weightLayout.setVisibility(View.VISIBLE);
			weightLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 DialogFragment refer = new AddLogDialogFragment();
			    	 refer.show(getFragmentManager(),"Add Log");
			    	 weightLayout.setVisibility(View.GONE);
			     }       
			});
		}/*else if (ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
			//it will come every month till the weight is logged
			int lastWeightMonth = ggPrefs.getInt(LaunchAppActivity.CURRENT_WEIGHT_MONTH, 0);
				if(LaunchAppActivity.currentMonth > lastWeightMonth){
				weightLayout.setVisibility(View.VISIBLE);
				weightLayout.setOnClickListener(new View.OnClickListener() {
				     @Override
				     public void onClick(View v) {
				    	 DialogFragment refer = new AddLogDialogFragment();
				         refer.show(getFragmentManager(),"Add Log");
				         weightLayout.setVisibility(View.GONE);
				     }       
				});
			}else{
				weightLayout.setVisibility(View.GONE);
			}
		}*/else{
			weightLayout.setVisibility(View.GONE);
		}
				
	}
	
	private void trainer(){//show for maintenance, begun and vacation
		//vacation schedule is show even if the person is on maintenance
		trainerLayout = (LinearLayout) v.findViewById(R.id.trainer_weekly_workout_layout);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance")){
			initializeTrainerParameters();
			updateDates(); // used to make ticks green and grey and update tickPrefs
			dailyHandlers();
			initializeListeners(); // this is for green to grey tick and vice versa
			updateTicks();
			//Find out if vacation is going on -- display functional training schedule
			if(LaunchAppActivity.vacation){
				Trainer.vacationFunctional1();
				mondayExercise.setText(Trainer.vacationFunctional1[0]);
				tuesdayExercise.setText(Trainer.vacationFunctional1[1]);
				wednesdayExercise.setText(Trainer.vacationFunctional1[2]);
				thursdayExercise.setText(Trainer.vacationFunctional1[3]);
				fridayExercise.setText(Trainer.vacationFunctional1[4]);
				saturdayExercise.setText(Trainer.vacationFunctional1[5]);
				sundayExercise.setText(Trainer.vacationFunctional1[6]);
				//This will require some changing - because u need to show both - functional 1 & 2 
				mondayDaily = R.array.functional1_workout; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.functional1_workout;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.functional1_workout;
				saturdayDaily = R.array.functional_cardio;
				sundayDaily=R.array.rest;
				
			}else{//no vacation
				if(ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Weight Loss").equalsIgnoreCase("Weight Loss")){
					//This is for weight Loss
					//You also have to keep track of current month and for intermediate 2 - current week
					if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
						//if person is on maintenance
						weightLossMaintenance(true);
					}else{
						//not on maintenance
						weightLossNormal(true);
					}
				}else{
					//This is for weight gain
					//You also have to keep track of current month and for intermediate 2 - current week					
					if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
						//if person is on maintenance
						weightGainMaintenance(true);
					}else{
						//not on maintenance
						weightGainNormal(true);
					}
				}
			}//end of  no vacation
		//	setWorkouts();
			hideTicks();
		}else{
			trainerLayout.setVisibility(View.GONE);
		}
	}
	
	private void hideTicks(){
		//starting program mid week
		int oneToSix = LaunchAppActivity.currentDay%30;
		if(oneToSix > 0 && oneToSix < 7 && LaunchAppActivity.currentDOW > 1){
			//hide some ticks
			switch(LaunchAppActivity.currentDOW){
				case 1: //Monday
						break;
				case 2:	//Tuesday
						if (oneToSix == 1)
							mondayImage.setVisibility(View.GONE);
						break;
				case 3: //Wednesday
						if(oneToSix <=2)
							mondayImage.setVisibility(View.GONE);
						if(oneToSix == 1)
							tuesdayImage.setVisibility(View.GONE);
						break;
				case 4: // Thursday
						if(oneToSix <=3)
							mondayImage.setVisibility(View.GONE);
						if(oneToSix <=2)
							tuesdayImage.setVisibility(View.GONE);
						if(oneToSix == 1)
							wednesdayImage.setVisibility(View.GONE);
						break;
				case 5: // Friday
						if(oneToSix <=4)
							mondayImage.setVisibility(View.GONE);
						if(oneToSix <=3)
							tuesdayImage.setVisibility(View.GONE);
						if(oneToSix <= 2)
							wednesdayImage.setVisibility(View.GONE);
						if(oneToSix == 1)
							thursdayImage.setVisibility(View.GONE);
						break;		
				case 6: // Saturday
					if(oneToSix <=5)
						mondayImage.setVisibility(View.GONE);
					if(oneToSix <=4)
						tuesdayImage.setVisibility(View.GONE);
					if(oneToSix <= 3)
						wednesdayImage.setVisibility(View.GONE);
					if(oneToSix <= 2)
						thursdayImage.setVisibility(View.GONE);
					if(oneToSix == 1)
						fridayImage.setVisibility(View.GONE);
					break;	
				case 7: // Sunday
					if(oneToSix <=6)
						mondayImage.setVisibility(View.GONE);
					if(oneToSix <=5)
						tuesdayImage.setVisibility(View.GONE);
					if(oneToSix <= 4)
						wednesdayImage.setVisibility(View.GONE);
					if(oneToSix <= 3)
						thursdayImage.setVisibility(View.GONE);
					if(oneToSix <= 2)
						fridayImage.setVisibility(View.GONE);
					if(oneToSix == 1)
						saturdayImage.setVisibility(View.GONE);
					break;
				default: break;	
			}
		}
	}

	
	protected static void weightLossMaintenance(boolean value){
		Trainer.weightLossMMonthlyMap();
		int remainder = LaunchAppActivity.currentMonth%8;
		program = Trainer.weightLossMMonthlyMap.get(remainder);
		getExercises(program, value);
	}
	
	protected static void weightLossNormal(boolean value){
		Trainer.weightLossMonthlyMap();
		program = Trainer.weightLossMonthlyMap.get(LaunchAppActivity.currentMonth);
		getExercises(program, value);
		
	}
	
	protected static void weightGainMaintenance(boolean value){
		Trainer.weightGainMMonthlyMap();
		int remainder = LaunchAppActivity.currentMonth%8;
		program = Trainer.weightGainMMonthlyMap.get(remainder);
		getExercises(program, value);
	}
	
	protected static void weightGainNormal(boolean value){
		Trainer.weightGainMonthlyMap();
		program = Trainer.weightGainMonthlyMap.get(LaunchAppActivity.currentMonth);
		getExercises(program, value);
	}
	

	private static void getExercises(String program, boolean value){
		if(program.equalsIgnoreCase("Basic 1")){
			Trainer.weeklyBasic1();
			if(value){
				mondayExercise.setText(Trainer.weeklyBasic1[0]);
				tuesdayExercise.setText(Trainer.weeklyBasic1[1]);
				wednesdayExercise.setText(Trainer.weeklyBasic1[2]);
				thursdayExercise.setText(Trainer.weeklyBasic1[3]);
				fridayExercise.setText(Trainer.weeklyBasic1[4]);
				saturdayExercise.setText(Trainer.weeklyBasic1[5]);
				sundayExercise.setText(Trainer.weeklyBasic1[6]);
				
				mondayDaily = R.array.basic_1_cardio; 
				tuesdayDaily = R.array.basic_1_whole_body;
				wednesdayDaily = R.array.rest;
				thursdayDaily = R.array.basic_1_cardio;
				fridayDaily = R.array.basic_1_whole_body;
				saturdayDaily = R.array.basic_1_cardio;
				sundayDaily=R.array.basic_1_whole_body;
			}else{
				switch(LaunchAppActivity.currentDOW){
				case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[0]).commit(); 
						break;
				case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[1]).commit();
						break;
				case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[2]).commit(); 
						break;
				case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[3]).commit();
						break;
				case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[4]).commit();
						break;
				case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[5]).commit();
						break;
				case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[6]).commit();
						break;
				default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic1[0]).commit();
				
				}
			}
			
		}else if(program.equalsIgnoreCase("Basic 2")){
			Trainer.weeklyBasic2();
			if(value){
				mondayExercise.setText(Trainer.weeklyBasic2[0]);
				tuesdayExercise.setText(Trainer.weeklyBasic2[1]);
				wednesdayExercise.setText(Trainer.weeklyBasic2[2]);
				thursdayExercise.setText(Trainer.weeklyBasic2[3]);
				fridayExercise.setText(Trainer.weeklyBasic2[4]);
				saturdayExercise.setText(Trainer.weeklyBasic2[5]);
				sundayExercise.setText(Trainer.weeklyBasic2[6]);
				
				mondayDaily = R.array.basic_2_lower_body; 
				tuesdayDaily = R.array.basic_2_upper_body;
				wednesdayDaily = R.array.basic_2_wed_cardio;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.basic_2_lower_body;
				saturdayDaily = R.array.basic_2_upper_body;
				sundayDaily=R.array.basic_2_sun_cardio;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2[0]).commit();
					
				}
				
			}
		}else if(program.equalsIgnoreCase("Intermediate 1")){
			Trainer.weeklyIntermediate1();
			if(value){
				mondayExercise.setText(Trainer.weeklyIntermediate1[0]);
				tuesdayExercise.setText(Trainer.weeklyIntermediate1[1]);
				wednesdayExercise.setText(Trainer.weeklyIntermediate1[2]);
				thursdayExercise.setText(Trainer.weeklyIntermediate1[3]);
				fridayExercise.setText(Trainer.weeklyIntermediate1[4]);
				saturdayExercise.setText(Trainer.weeklyIntermediate1[5]);
				sundayExercise.setText(Trainer.weeklyIntermediate1[6]);
				
				mondayDaily = R.array.int1_lower_body; 
				tuesdayDaily = R.array.int1_upper_1;
				wednesdayDaily = R.array.int1_cardio_abs;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.int1_upper_2;
				saturdayDaily = R.array.int1_cardio_abs;
				sundayDaily=R.array.rest;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate1[0]).commit();
				
			}
				
			}
		}else if(program.equalsIgnoreCase("Intermediate 2")){   
			Trainer.weeklyIntermediate2();
			if(value){
				if(LaunchAppActivity.currentWeek%2 == 0){
					mondayExercise.setText(Trainer.weeklyIntermediate2[0]);
					tuesdayExercise.setText(Trainer.weeklyIntermediate2[1]);
					wednesdayExercise.setText(Trainer.weeklyIntermediate2[2]);
					thursdayExercise.setText(Trainer.weeklyIntermediate2[3]);
					fridayExercise.setText(Trainer.weeklyIntermediate2[4]);
					saturdayExercise.setText(Trainer.weeklyIntermediate2[5]);
					sundayExercise.setText(Trainer.weeklyIntermediate2[6]);
					
					mondayDaily = R.array.int2_chest_etc; 
					tuesdayDaily = R.array.rest;
					wednesdayDaily = R.array.int2_back_etc;
					thursdayDaily = R.array.rest;
					fridayDaily = R.array.int2_chest_etc;
					saturdayDaily = R.array.rest;
					sundayDaily=R.array.rest;
				}else{
					mondayExercise.setText(Trainer.weeklyIntermediate2[7]);
					tuesdayExercise.setText(Trainer.weeklyIntermediate2[8]);
					wednesdayExercise.setText(Trainer.weeklyIntermediate2[9]);
					thursdayExercise.setText(Trainer.weeklyIntermediate2[10]);
					fridayExercise.setText(Trainer.weeklyIntermediate2[11]);
					saturdayExercise.setText(Trainer.weeklyIntermediate2[12]);
					sundayExercise.setText(Trainer.weeklyIntermediate2[13]);
					
					mondayDaily = R.array.int2_back_etc; 
					tuesdayDaily = R.array.rest;
					wednesdayDaily = R.array.int2_chest_etc;
					thursdayDaily = R.array.rest;
					fridayDaily = R.array.int2_back_etc;
					saturdayDaily = R.array.rest;
					sundayDaily=R.array.rest;
				}
			}else{
				if(LaunchAppActivity.currentWeek%2 == 0){
					switch(LaunchAppActivity.currentDOW){
						case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[0]).commit(); 
								break;
						case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[1]).commit();
								break;
						case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[2]).commit(); 
								break;
						case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[3]).commit();
								break;
						case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[4]).commit();
								break;
						case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[5]).commit();
								break;
						case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[6]).commit();
								break;
						default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[0]).commit();
					
					}
				}else{
					switch(LaunchAppActivity.currentDOW){
						case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[7]).commit(); 
								break;
						case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[8]).commit();
								break;
						case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[9]).commit(); 
								break;
						case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[10]).commit();
								break;
						case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[11]).commit();
								break;
						case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[12]).commit();
								break;
						case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[13]).commit();
								break;
						default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyIntermediate2[7]).commit();
					
				}
				}
			}
			
			
		}else if(program.equalsIgnoreCase("Advance 1")){
			Trainer.weeklyAdvance1();
			if(value){
				mondayExercise.setText(Trainer.weeklyAdvance1[0]);
				tuesdayExercise.setText(Trainer.weeklyAdvance1[1]);
				wednesdayExercise.setText(Trainer.weeklyAdvance1[2]);
				thursdayExercise.setText(Trainer.weeklyAdvance1[3]);
				fridayExercise.setText(Trainer.weeklyAdvance1[4]);
				saturdayExercise.setText(Trainer.weeklyAdvance1[5]);
				sundayExercise.setText(Trainer.weeklyAdvance1[6]);
				
				mondayDaily = R.array.adv1_legs; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.adv1_back;
				thursdayDaily = R.array.adv1_cardio_abs;
				fridayDaily = R.array.adv1_chest_b;
				saturdayDaily = R.array.rest;
				sundayDaily=R.array.adv1_shoulder_t;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1[0]).commit();
				
				}
			}
			
		}else if(program.equalsIgnoreCase("Advance 2")){
			Trainer.weeklyAdvance2();
			if(value){
				mondayExercise.setText(Trainer.weeklyAdvance2[0]);
				tuesdayExercise.setText(Trainer.weeklyAdvance2[1]);
				wednesdayExercise.setText(Trainer.weeklyAdvance2[2]);
				thursdayExercise.setText(Trainer.weeklyAdvance2[3]);
				fridayExercise.setText(Trainer.weeklyAdvance2[4]);
				saturdayExercise.setText(Trainer.weeklyAdvance2[5]);
				sundayExercise.setText(Trainer.weeklyAdvance2[6]);
				
				mondayDaily = R.array.adv2_legs; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.adv2_back;
				thursdayDaily = R.array.adv2_cardio_abs;
				fridayDaily = R.array.adv2_chest_b;
				saturdayDaily = R.array.rest;
				sundayDaily=R.array.adv2_shoulder_t;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance2[0]).commit();
			
				}
			}
			
		}else if(program.equalsIgnoreCase("Functional Training 1")){ //this will change
			Trainer.functional1();
			if(value){
				mondayExercise.setText(Trainer.functional1[0]);
				tuesdayExercise.setText(Trainer.functional1[1]);
				wednesdayExercise.setText(Trainer.functional1[2]);
				thursdayExercise.setText(Trainer.functional1[3]);
				fridayExercise.setText(Trainer.functional1[4]);
				saturdayExercise.setText(Trainer.functional1[5]);
				sundayExercise.setText(Trainer.functional1[6]);
				
				mondayDaily = R.array.functional1_workout; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.functional1_workout;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.functional1_workout;
				saturdayDaily = R.array.functional_cardio;
				sundayDaily=R.array.rest;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional1[0]).commit();
		
				}
			}
			
		}else if(program.equalsIgnoreCase("Functional Training 2")){ 
			Trainer.functional2();
			if(value){
				mondayExercise.setText(Trainer.functional2[0]);
				tuesdayExercise.setText(Trainer.functional2[1]);
				wednesdayExercise.setText(Trainer.functional2[2]);
				thursdayExercise.setText(Trainer.functional2[3]);
				fridayExercise.setText(Trainer.functional2[4]);
				saturdayExercise.setText(Trainer.functional2[5]);
				sundayExercise.setText(Trainer.functional2[6]);
				
				mondayDaily = R.array.functional2_workout; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.functional2_workout;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.functional2_workout;
				saturdayDaily = R.array.functional_cardio;
				sundayDaily=R.array.rest;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.functional2[0]).commit();
		
				}
			}
			
		}else if(program.equalsIgnoreCase("Basic 2 (NC)")){
			Trainer.weeklyBasic2NC();
			if(value){
				mondayExercise.setText(Trainer.weeklyBasic2NC[0]);
				tuesdayExercise.setText(Trainer.weeklyBasic2NC[1]);
				wednesdayExercise.setText(Trainer.weeklyBasic2NC[2]);
				thursdayExercise.setText(Trainer.weeklyBasic2NC[3]);
				fridayExercise.setText(Trainer.weeklyBasic2NC[4]);
				saturdayExercise.setText(Trainer.weeklyBasic2NC[5]);
				sundayExercise.setText(Trainer.weeklyBasic2NC[6]);
				
				mondayDaily = R.array.basic_2_lower_body; 
				tuesdayDaily = R.array.basic_2_upper_body;
				wednesdayDaily = R.array.basic_2_nc_abs;
				thursdayDaily = R.array.rest;
				fridayDaily = R.array.basic_2_lower_body;
				saturdayDaily = R.array.basic_2_upper_body;
				sundayDaily=R.array.rest;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyBasic2NC[0]).commit();
	
				}
				
			}
			
		}else if(program.equalsIgnoreCase("Advance 1 (NC)")){
			Trainer.weeklyAdvance1NC();
			if(value){
				mondayExercise.setText(Trainer.weeklyAdvance1NC[0]);
				tuesdayExercise.setText(Trainer.weeklyAdvance1NC[1]);
				wednesdayExercise.setText(Trainer.weeklyAdvance1NC[2]);
				thursdayExercise.setText(Trainer.weeklyAdvance1NC[3]);
				fridayExercise.setText(Trainer.weeklyAdvance1NC[4]);
				saturdayExercise.setText(Trainer.weeklyAdvance1NC[5]);
				sundayExercise.setText(Trainer.weeklyAdvance1NC[6]);
				
				mondayDaily = R.array.adv1_legs; 
				tuesdayDaily = R.array.rest;
				wednesdayDaily = R.array.adv1_back;
				thursdayDaily = R.array.adv1_abs;
				fridayDaily = R.array.adv1_chest_b;
				saturdayDaily = R.array.rest;
				sundayDaily=R.array.adv1_shoulder_t;
			}else{
				switch(LaunchAppActivity.currentDOW){
					case 1: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[0]).commit(); 
							break;
					case 2: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[1]).commit();
							break;
					case 3: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[2]).commit(); 
							break;
					case 4: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[3]).commit();
							break;
					case 5: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[4]).commit();
							break;
					case 6: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[5]).commit();
							break;
					case 7: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[6]).commit();
							break;
					default: ReminderService.servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.weeklyAdvance1NC[0]).commit();
	
				}
				
			}
		}
	}
	

	//TAKES YOU TO THE DAILY SCHEDULE - - when it is rest day. it should not go to daily schedule
	private void dailyHandlers(){
		mondayLayout = (LinearLayout) v.findViewById(R.id.monday_layout);
		mondayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!((String) mondayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
		        	nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Monday");
		        	nextScreenIntent.putExtra("title",mondayExercise.getText());
		        	nextScreenIntent.putExtra("description",mondayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		tuesdayLayout = (LinearLayout) v.findViewById(R.id.tuesday_layout);
		tuesdayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!((String) tuesdayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Tuesday");
		        	nextScreenIntent.putExtra("title",tuesdayExercise.getText());
		        	nextScreenIntent.putExtra("description",tuesdayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		wednesdayLayout = (LinearLayout) v.findViewById(R.id.wednesday_layout);
		wednesdayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!((String) wednesdayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Wednesday");
		        	nextScreenIntent.putExtra("title",wednesdayExercise.getText());
		        	nextScreenIntent.putExtra("description",wednesdayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		thursdayLayout = (LinearLayout) v.findViewById(R.id.thursday_layout);
		thursdayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!((String) thursdayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Thursday");
		        	nextScreenIntent.putExtra("title",thursdayExercise.getText());
		        	nextScreenIntent.putExtra("description",thursdayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		fridayLayout = (LinearLayout) v.findViewById(R.id.friday_layout);
		fridayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!((String) fridayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Friday");
		        	nextScreenIntent.putExtra("title",fridayExercise.getText());
		        	nextScreenIntent.putExtra("description",fridayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		saturdayLayout = (LinearLayout) v.findViewById(R.id.saturday_layout);
		saturdayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(!((String) saturdayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Saturday");
		        	nextScreenIntent.putExtra("title",saturdayExercise.getText());
		        	nextScreenIntent.putExtra("description",saturdayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
		
		sundayLayout = (LinearLayout) v.findViewById(R.id.sunday_layout);
		sundayLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!((String) sundayExercise.getText()).equalsIgnoreCase("Rest")){
					// Rest Should not be clickable
					nextScreenIntent = new Intent(context,DailyActivity.class);
		        	nextScreenIntent.putExtra("heading","Sunday");
		        	nextScreenIntent.putExtra("title",sundayExercise.getText());
		        	nextScreenIntent.putExtra("description",sundayDaily);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			}
		});
	}
	
	private void initializeTrainerParameters(){
		mondayExercise = (TextView) v.findViewById(R.id.monday_exercise);
		tuesdayExercise = (TextView) v.findViewById(R.id.tuesday_exercise);
		wednesdayExercise = (TextView) v.findViewById(R.id.wednesday_exercise);
		thursdayExercise = (TextView) v.findViewById(R.id.thursday_exercise);
		fridayExercise = (TextView) v.findViewById(R.id.friday_exercise);
		saturdayExercise = (TextView) v.findViewById(R.id.saturday_exercise);
		sundayExercise = (TextView) v.findViewById(R.id.sunday_exercise);

		mondayImage = (ImageView) v.findViewById(R.id.monday_image);
		tuesdayImage = (ImageView) v.findViewById(R.id.tuesday_image);
		wednesdayImage = (ImageView) v.findViewById(R.id.wednesday_image);
		thursdayImage = (ImageView) v.findViewById(R.id.thursday_image);
		fridayImage = (ImageView) v.findViewById(R.id.friday_image);
		saturdayImage = (ImageView) v.findViewById(R.id.saturday_image);
		sundayImage = (ImageView) v.findViewById(R.id.sunday_image);
	}
	
	private void initializeListeners(){
		
		mondayImage.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	if(((String) (mondayImage.getTag())).equalsIgnoreCase("Green")){
	 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)mondayExercise.getTag())).commit();
	 		    		//Yesterday - Do the same
	 		    		if(LaunchAppActivity.currentDOW == 2){
	 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
	 		    			yesterdayImage.startAnimation(animScale);
	 		    			yesterdayImage.setTag("Grey");
	 		    		}
	 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
	 		    		mondayImage.setImageResource(R.drawable.grey_tickmark);
	 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.MONDAY_TICK, false).commit();
	 		    		mondayImage.startAnimation(animScale);
	 		    		mondayImage.setTag("Grey");
	 		    		achievements();
	 		    	}else{
	 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)mondayExercise.getTag()), true).commit();
	 		    		//Yesterday - Do the same
	 		    		if(LaunchAppActivity.currentDOW == 2){
	 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
	 		    			yesterdayImage.startAnimation(animScale);
	 		    			yesterdayImage.setTag("Green");
	 		    		}
	 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
	 		    		mondayImage.setImageResource(R.drawable.green_tickmark);
	 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.MONDAY_TICK, true).commit();
	 		    		mondayImage.setTag("Green");
	 		    		mondayImage.startAnimation(animScale);
	 		    		achievements();
	 		    	}
	 		    }
	 		});
		
		tuesdayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	if(((String) (tuesdayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)tuesdayExercise.getTag())).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 3){
 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Grey");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		tuesdayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.TUESDAY_TICK, false).commit();
 		    		tuesdayImage.setTag("Grey");
 		    		tuesdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)tuesdayExercise.getTag()), true).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 3){
 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Green");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		tuesdayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.TUESDAY_TICK, true).commit();
 		    		tuesdayImage.setTag("Green");
 		    		tuesdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
		
		wednesdayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	if(((String) (wednesdayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)wednesdayExercise.getTag())).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 4){
 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Grey");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		wednesdayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.WEDNESDAY_TICK, false).commit();
 		    		wednesdayImage.setTag("Grey");
 		    		wednesdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)wednesdayExercise.getTag()), true).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 4){
 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Green");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		wednesdayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.WEDNESDAY_TICK, true).commit();
 		    		wednesdayImage.setTag("Green");
 		    		wednesdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
		
		thursdayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	if(((String) (thursdayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)thursdayExercise.getTag())).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 5){
 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Grey");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		thursdayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.THURSDAY_TICK, false).commit();
 		    		thursdayImage.setTag("Grey");
 		    		thursdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)thursdayExercise.getTag()), true).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 5){
 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Green");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		thursdayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.THURSDAY_TICK, true).commit();
 		    		thursdayImage.setTag("Green");
 		    		thursdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
		
		fridayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	
 		    	if(((String) (fridayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)fridayExercise.getTag())).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 6){
 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Grey");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		fridayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.FRIDAY_TICK, false).commit();
 		    		fridayImage.setTag("Grey");
 		    		fridayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)fridayExercise.getTag()), true).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 6){
 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Green");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		fridayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.FRIDAY_TICK, true).commit();
 		    		fridayImage.setTag("Green");
 		    		fridayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
		
		saturdayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	if(((String) (saturdayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)saturdayExercise.getTag())).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 7){
 		    			yesterdayImage.setImageResource(R.drawable.grey_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Grey");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		saturdayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.SATURDAY_TICK, false).commit();
 		    		saturdayImage.setTag("Grey");
 		    		saturdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)saturdayExercise.getTag()), true).commit();
 		    		//Yesterday - Do the same
 		    		if(LaunchAppActivity.currentDOW == 7){
 		    			yesterdayImage.setImageResource(R.drawable.green_tickmark);
 		    			yesterdayImage.startAnimation(animScale);
 		    			yesterdayImage.setTag("Green");
 		    		}
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		saturdayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.SATURDAY_TICK, true).commit();
 		    		saturdayImage.setTag("Green");
 		    		saturdayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
		
		sundayImage.setOnClickListener(new View.OnClickListener(){
 		    public void onClick(View v) {
 		    	if(((String) (sundayImage.getTag())).equalsIgnoreCase("Green")){
 		    		LaunchAppActivity.tickPrefs.edit().remove(Integer.toString((Integer)sundayExercise.getTag())).commit();
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)-1).commit();
 		    		sundayImage.setImageResource(R.drawable.grey_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.SUNDAY_TICK, false).commit();
 		    		sundayImage.setTag("Grey");
 		    		sundayImage.startAnimation(animScale);
 		    		achievements();
 		    	}else{
 		    		LaunchAppActivity.tickPrefs.edit().putBoolean(Integer.toString((Integer)sundayExercise.getTag()), true).commit();
 		    		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)+1).commit();
 		    		sundayImage.setImageResource(R.drawable.green_tickmark);
 		    		ggPrefs.edit().putBoolean(LaunchAppActivity.SUNDAY_TICK, true).commit();
 		    		sundayImage.setTag("Green");
 		    		sundayImage.startAnimation(animScale);
 		    		achievements();
 		    	}
 		    }
 		});
	}
	
	private void updateTicks(){ // for the very first time
		
		//MONDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) mondayExercise.getTag()), false)){
			mondayImage.setImageResource(R.drawable.green_tickmark);
			mondayImage.setTag("Green");
		}else{
			mondayImage.setImageResource(R.drawable.grey_tickmark); //DO YOU REALLY NEED THE ELSE PARTS ??
			mondayImage.setTag("Grey");
		}
		
		//TUESDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) tuesdayExercise.getTag()), false)){
			tuesdayImage.setImageResource(R.drawable.green_tickmark);
			tuesdayImage.setTag("Green");
		}else{
			tuesdayImage.setImageResource(R.drawable.grey_tickmark);
			tuesdayImage.setTag("Grey");
		}
		
		//WEDNESDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) wednesdayExercise.getTag()), false)){
			wednesdayImage.setImageResource(R.drawable.green_tickmark);
			wednesdayImage.setTag("Green");
		}else{
			wednesdayImage.setImageResource(R.drawable.grey_tickmark);
			wednesdayImage.setTag("Grey");
		}
		
		//THURSDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) thursdayExercise.getTag()), false)){
			thursdayImage.setImageResource(R.drawable.green_tickmark);
			thursdayImage.setTag("Green");
		}else{
			thursdayImage.setImageResource(R.drawable.grey_tickmark);
			thursdayImage.setTag("Grey");
		}
		
		//FRIDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) fridayExercise.getTag()), false)){
			fridayImage.setImageResource(R.drawable.green_tickmark);
			fridayImage.setTag("Green");
		}else{
			fridayImage.setImageResource(R.drawable.grey_tickmark);
			fridayImage.setTag("Grey");
		}
		
		//SATURDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) saturdayExercise.getTag()), false)){
			saturdayImage.setImageResource(R.drawable.green_tickmark);
			saturdayImage.setTag("Green");
		}else{
			saturdayImage.setImageResource(R.drawable.grey_tickmark);
			saturdayImage.setTag("Grey");
		}
		
		
		//SUNDAY
		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString((Integer) sundayExercise.getTag()), false)){
			sundayImage.setImageResource(R.drawable.green_tickmark);
			sundayImage.setTag("Green");
		}else{
			sundayImage.setImageResource(R.drawable.grey_tickmark);
			sundayImage.setTag("Grey");
		}
	}
	
	private void guidelines(){
		guidelinesLayout = (LinearLayout) v.findViewById(R.id.trainer_guidelines_layout);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
			guidelinesLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(context,GuidelinesActivity.class);
			    	//You are only sending the id of the resource. not the string itself
			    	 nextScreenIntent.putExtra("text", guidelinesTextId());
	       		     startActivity(nextScreenIntent);
	       		     ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			     }       
			});
		}else{
			guidelinesLayout.setVisibility(View.GONE);
		}
	}
	
	//You are only sending the id of the resource. not the string itself
	@SuppressLint("DefaultLocale")
	private int guidelinesTextId(){
		int textId=0;
		if(LaunchAppActivity.vacation){
			textId=R.string.guidelines_vacation;
		}else if(ggPrefs.getString(LaunchAppActivity.TRAINING_LEVEL, "Basic").toLowerCase().contains("basic")){
			textId=R.string.guidelines_basic;
		}else if(ggPrefs.getString(LaunchAppActivity.TRAINING_LEVEL, "Basic").toLowerCase().contains("intermediate")) {
			textId=R.string.guidelines_intermediate;
		}else if(ggPrefs.getString(LaunchAppActivity.TRAINING_LEVEL, "Basic").toLowerCase().contains("advance")) {
			textId=R.string.guidelines_advanced;
		}else if (ggPrefs.getString(LaunchAppActivity.TRAINING_LEVEL, "Basic").toLowerCase().contains("functional")) {
			textId=R.string.guidelines_functional;
		}else{
			textId=R.string.guidelines_basic;
		}
		return textId;
	}
	
	
	private void dadLayout(){
		dadLayout = (LinearLayout) v.findViewById(R.id.trainer_dads_layout);
		if((ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")) && (ggPrefs.getBoolean(LaunchAppActivity.HYPERTENSION,false) || ggPrefs.getBoolean(LaunchAppActivity.DIABETES,false) || ggPrefs.getBoolean(LaunchAppActivity.LOWER_BACK,false) || ggPrefs.getBoolean(LaunchAppActivity.SPONDILYTIS,false) || ggPrefs.getBoolean(LaunchAppActivity.KNEE,false) || ggPrefs.getBoolean(LaunchAppActivity.WRIST,false) || ggPrefs.getBoolean(LaunchAppActivity.PREGNANCY,false))){
			dadLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(context,DadActivity.class);
			    	 nextScreenIntent.putExtra("type", "Trainer");
	       		     startActivity(nextScreenIntent);
	       		  ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			     }       
			});
		}else{
			dadLayout.setVisibility(View.GONE);
		}
	}
	
	protected void updateDates(){
		switch(LaunchAppActivity.currentDOW){
			case 1: mondayExercise.setTag(LaunchAppActivity.currentDay);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay + 1);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay + 2);
					thursdayExercise.setTag(LaunchAppActivity.currentDay + 3);
					fridayExercise.setTag(LaunchAppActivity.currentDay + 4);
					saturdayExercise.setTag(LaunchAppActivity.currentDay + 5);
					sundayExercise.setTag(LaunchAppActivity.currentDay + 6);
					break;

			case 2: mondayExercise.setTag(LaunchAppActivity.currentDay - 1);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay + 1);
					thursdayExercise.setTag(LaunchAppActivity.currentDay + 2);
					fridayExercise.setTag(LaunchAppActivity.currentDay + 3);
					saturdayExercise.setTag(LaunchAppActivity.currentDay + 4);
					sundayExercise.setTag(LaunchAppActivity.currentDay + 5);
					break;

			case 3: mondayExercise.setTag(LaunchAppActivity.currentDay - 2);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay - 1);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay);
					thursdayExercise.setTag(LaunchAppActivity.currentDay + 1);
					fridayExercise.setTag(LaunchAppActivity.currentDay + 2);
					saturdayExercise.setTag(LaunchAppActivity.currentDay + 3);
					sundayExercise.setTag(LaunchAppActivity.currentDay + 4);
					break;

			case 4: mondayExercise.setTag(LaunchAppActivity.currentDay - 3);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay - 2);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay - 1);
					thursdayExercise.setTag(LaunchAppActivity.currentDay);
					fridayExercise.setTag(LaunchAppActivity.currentDay + 1);
					saturdayExercise.setTag(LaunchAppActivity.currentDay + 2);
					sundayExercise.setTag(LaunchAppActivity.currentDay + 3);
					break;
					
			case 5: mondayExercise.setTag(LaunchAppActivity.currentDay - 4);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay - 3);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay - 2);
					thursdayExercise.setTag(LaunchAppActivity.currentDay - 1);
					fridayExercise.setTag(LaunchAppActivity.currentDay);
					saturdayExercise.setTag(LaunchAppActivity.currentDay + 1);
					sundayExercise.setTag(LaunchAppActivity.currentDay + 2);
					break;
					
			case 6: mondayExercise.setTag(LaunchAppActivity.currentDay - 5);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay - 4);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay - 3);
					thursdayExercise.setTag(LaunchAppActivity.currentDay - 2);
					fridayExercise.setTag(LaunchAppActivity.currentDay - 1);
					saturdayExercise.setTag(LaunchAppActivity.currentDay);
					sundayExercise.setTag(LaunchAppActivity.currentDay  + 1);
					break;
					
			case 7: mondayExercise.setTag(LaunchAppActivity.currentDay - 6);
					tuesdayExercise.setTag(LaunchAppActivity.currentDay  - 5);
					wednesdayExercise.setTag(LaunchAppActivity.currentDay - 4);
					thursdayExercise.setTag(LaunchAppActivity.currentDay - 3);
					fridayExercise.setTag(LaunchAppActivity.currentDay - 2);
					saturdayExercise.setTag(LaunchAppActivity.currentDay - 1);
					sundayExercise.setTag(LaunchAppActivity.currentDay);
					break;
		}
	}
	
	protected void createAlarm(){
		PendingIntent pendingIntent;
		//You can make this a recurring event if you want
		Calendar alarmDate = Calendar.getInstance();
		alarmDate.add(Calendar.DATE,1);
		alarmDate.set(Calendar.HOUR_OF_DAY,8);
		alarmDate.set(Calendar.MINUTE, 00);
		alarmDate.set(Calendar.SECOND, 0);
		alarmDate.set(Calendar.AM_PM,Calendar.AM);
	     
	    Intent myIntent = new Intent(getActivity(), ReminderReceiver.class);
	    pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,0);
	     
	    AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC, alarmDate.getTimeInMillis(), pendingIntent);
	}
	
/*	protected void setWorkouts(){
		//get day of week
		switch(LaunchAppActivity.currentDOW){
			case 1: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,sundayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,mondayExercise.getText().toString()).commit();
					break;
			case 2: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,mondayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,tuesdayExercise.getText().toString()).commit();
					break;
			case 3: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,tuesdayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,wednesdayExercise.getText().toString()).commit();
					break;
			case 4: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,wednesdayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,thursdayExercise.getText().toString()).commit();
					break;
			case 5: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,thursdayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,fridayExercise.getText().toString()).commit();
					break;
			case 6: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,fridayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,saturdayExercise.getText().toString()).commit();
					break;
			case 7: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,saturdayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,sundayExercise.getText().toString()).commit();
					break;
			default: ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,sundayExercise.getText().toString()).commit();
					ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,mondayExercise.getText().toString()).commit();
					break;
		}
	}*/
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
	    super.onActivityResult(requestCode, resultCode, data);
	} 
	
	
	
	protected void shareOnFacebook(){
		HomeScreenActivity.fbLogin();
	}
	
}
