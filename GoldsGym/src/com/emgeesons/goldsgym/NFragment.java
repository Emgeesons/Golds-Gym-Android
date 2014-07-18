package com.emgeesons.goldsgym;

import java.util.Calendar;
import java.util.Random;

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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NFragment extends Fragment {

	View view;
	LinearLayout welcomeLayout,guidelinesLayout, dadLayout, logWeightLayout;
	SharedPreferences ggPrefs;
	Context context;
	RelativeLayout motivationalLayout ;
	TextView motivationalTitle,getStartedText;
	Intent nextScreenIntent;
	WebView dietWebView;
	ImageView getStartedButton,getStartedImage,motivationImage, resetStatsButton;
	
	Animation animTranslate;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		view = inflater.inflate(R.layout.nfragment, container, false); 
		context=view.getContext();
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		//Animations
		animTranslate = AnimationUtils.loadAnimation(context, R.anim.anim_translate);
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		HomeScreenActivity.trainingStatus();
		welcomeLayout();
		motivationalTip();
		diet();
		guidelines();
		dads();
		logWeight();
		view.startAnimation(animTranslate);
	}
	
	private void welcomeLayout(){
		//Instantiating Variables
		welcomeLayout = (LinearLayout) view.findViewById(R.id.nutrionist_welcome_layout);
		getStartedButton = (ImageView)view.findViewById(R.id.get_started_button);
		resetStatsButton = (ImageView)view.findViewById(R.id.reset_stats_button);
		getStartedImage = (ImageView)view.findViewById(R.id.get_started_image);
		getStartedText = (TextView)view.findViewById(R.id.get_started_text);
		
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Undefined")){//undefined
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
			    	 nextScreenIntent = new Intent(getActivity(),MedicalConditionActivity.class);
				     startActivity(nextScreenIntent);
					}
			});
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Set")){//set
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
			    		welcomeLayout.setVisibility(View.GONE);
			    		HomeScreenActivity.setTab(0);
			    }
			});
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Indeterminate")){//When the Goal is over. And user hasn't input weight
			welcomeLayout.setVisibility(View.GONE);
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Over")){//When the Goal is over. And the User has achieved the Goal
			welcomeLayout.setVisibility(View.VISIBLE);
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setImageResource(R.drawable.maintain_weight);
			getStartedImage.setImageResource(R.drawable.over_icon); //this will remain the same
			getStartedText.setText("Superb!! Way to go! You have achieved your Goal.");
			getStartedButton.setOnClickListener(new View.OnClickListener(){ //maintain weight
			    public void onClick(View v) {
		    		ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Maintenance").commit();
		    		HomeScreenActivity.trainingStatus();
		    		welcomeLayout.setVisibility(View.GONE);
		    		HomeScreenActivity.setTab(0);
				}
			});
		/*	resetStatsButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
		        	DialogFragment resetFragment = new ResetGoalDialogFragment();
		        	resetFragment.show(getFragmentManager(),"ResetGoalDialogFragment");
				}
			});*/
		}else if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Incomplete")){//When the Goal is over. And the User has NOT achieved the Goal
			welcomeLayout.setVisibility(View.VISIBLE);
			resetStatsButton.setVisibility(View.GONE);
			getStartedButton.setImageResource(R.drawable.reset_stats);
			getStartedImage.setImageResource(R.drawable.try_harder);
			getStartedText.setText("We missed our Target!! Let's try harder next time and get in to perfect shape");
			getStartedButton.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
			    	ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Undefined").commit();
			    	 HomeScreenActivity.reset();
			    	 nextScreenIntent = new Intent(getActivity(),MedicalConditionActivity.class);
				     startActivity(nextScreenIntent);
					}
			});
		}
		
		else{//for begun //maintenance // vacation - For Vacation - Diet doesnt change
			welcomeLayout.setVisibility(View.GONE);
		}
		
	}
	
	private void motivationalTip(){
		motivationalLayout = (RelativeLayout) view.findViewById(R.id.motivation_layout);
		motivationalTitle = (TextView) view.findViewById(R.id.motivational_title);
		motivationImage = (ImageView) view.findViewById(R.id.motivation_image);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
			String[] nutrionistQuotes = getResources().getStringArray(R.array.nutrionist_quotes);
			Random r = new Random();
			int index=r.nextInt(26);
			int quoteLength = nutrionistQuotes[index].length();
			if(quoteLength < 60)
				motivationImage.setImageResource(R.drawable.n_m_1);
			else if (quoteLength < 120)
				motivationImage.setImageResource(R.drawable.n_m_2);
			else if (quoteLength < 160)
				motivationImage.setImageResource(R.drawable.n_m_3);
			else
				motivationImage.setImageResource(R.drawable.n_m_4);
			motivationalTitle.setText(nutrionistQuotes[index]);
		}else{
			motivationalLayout.setVisibility(View.GONE);
		}
	}
	
	private void diet(){
		//current week needs to work properly
		dietWebView = (WebView) view.findViewById (R.id.diet_web_view);
		//decide whether diet should be hidden or seen
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
			String diet;
			if(ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM,"Undefined").equalsIgnoreCase("Weight Gain")){
				// if Goal Stage is Maintenance.
				if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
					//Veg or Non Veg
					if(ggPrefs.getString(LaunchAppActivity.VEG_NONVEG,"Undefined").equalsIgnoreCase("Veg")){
						diet = caseWGMVeg(LaunchAppActivity.currentWeek);
					}else{//Non Veg
						diet = caseWGMNonVeg(LaunchAppActivity.currentWeek);
					}
				}else{
					//Veg or Non Veg
					if(ggPrefs.getString(LaunchAppActivity.VEG_NONVEG,"Undefined").equalsIgnoreCase("Veg")){
						diet = caseWGVeg(LaunchAppActivity.currentWeek);
					}else{//Non Veg
						diet = caseWGNonVeg(LaunchAppActivity.currentWeek);
					}
				}
			}else{ // If not weight Gain. It is weight loss..
				// if Goal Stage is Maintenance.				
				if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
					//Veg or Non Veg
					if(ggPrefs.getString(LaunchAppActivity.VEG_NONVEG,"Undefined").equalsIgnoreCase("Veg")){
						diet = caseWLMVeg(LaunchAppActivity.currentWeek);
					}else{//Non Veg
						diet = caseWLMNonVeg(LaunchAppActivity.currentWeek);
					}
				}else{
					//Veg or Non Veg
					if(ggPrefs.getString(LaunchAppActivity.VEG_NONVEG,"Undefined").equalsIgnoreCase("Veg")){
						diet = caseWLVeg(LaunchAppActivity.currentWeek);
					}else{//Non Veg
						diet = caseWLNonVeg(LaunchAppActivity.currentWeek);
					}
				}	
			}
			dietWebView.setBackgroundColor(0);
			dietWebView.setBackgroundResource(R.drawable.welcome_bg);
			dietWebView.loadDataWithBaseURL("", diet, "text/html", "UTF-8", "");
		}else{
			dietWebView.setVisibility(View.GONE);
		}
	}
	
	private void guidelines(){
		guidelinesLayout = (LinearLayout) view.findViewById(R.id.nutritionist_guidelines_layout);
		if((ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")) && ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM,"Undefined").equalsIgnoreCase("Weight Loss")){
			guidelinesLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(context,GuidelinesActivity.class);
			    	 nextScreenIntent.putExtra("text", R.string.weight_loss_guidelines);
	       		     startActivity(nextScreenIntent);
	       		  ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			     }       
			});
		}else if ((ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")) && ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM,"Undefined").equalsIgnoreCase("Weight Gain")){
			guidelinesLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(context,GuidelinesActivity.class);
			    	 nextScreenIntent.putExtra("text", R.string.weight_gain_guidelines);
	       		     startActivity(nextScreenIntent);
	       		  ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			     }       
			});
		}else{
			guidelinesLayout.setVisibility(View.GONE);
		}
	}
	
	private void dads(){
		dadLayout = (LinearLayout) view.findViewById(R.id.nutritionist_dads_layout);
		if((ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")) && (ggPrefs.getBoolean(LaunchAppActivity.HYPERTENSION,false) || ggPrefs.getBoolean(LaunchAppActivity.CHOLESTEROL,false) || ggPrefs.getBoolean(LaunchAppActivity.DIABETES,false) || ggPrefs.getBoolean(LaunchAppActivity.PCOS,false) || ggPrefs.getBoolean(LaunchAppActivity.THYROID,false) || ggPrefs.getBoolean(LaunchAppActivity.PREGNANCY,false))){
			dadLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(context,DadActivity.class);
			    	 nextScreenIntent.putExtra("type", "Nutritionist");
	       		     startActivity(nextScreenIntent);
	       		  ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			     }       
			});
		}else{
			dadLayout.setVisibility(View.GONE);
		}
	}
	
	private void logWeight(){
		logWeightLayout = (LinearLayout) view.findViewById(R.id.nutritionist_log_weight_layout);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Indeterminate")){
			logWeightLayout.setVisibility(View.VISIBLE);
			logWeightLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 DialogFragment refer = new AddLogDialogFragment();
			    	 refer.show(getFragmentManager(),"Add Log");
			    	 logWeightLayout.setVisibility(View.GONE);
			     }       
			});
		}else{
			logWeightLayout.setVisibility(View.GONE);
		}
	}
	
	private String caseWLVeg(int week){
        switch (week) {
            case 1:  return Diets.wlVeg1Diet();
            case 2:  return Diets.wlVeg1Diet();
            case 3:  return Diets.wlVeg2Diet();
            case 4:  return Diets.wlVeg2Diet();
            case 5:  return Diets.wlVeg4Diet();
            case 6:  return Diets.wlVeg4Diet();
            case 7:  return Diets.wlVeg5Diet();
            case 8:  return Diets.wlVeg5Diet();
            case 9:  return Diets.wlVeg6Diet();
            case 10:  return Diets.wlVeg6Diet();
            case 11:  return Diets.wlVeg7Diet();
            case 12:  return Diets.wlVeg7Diet();
            case 13:  return Diets.wlVeg8Diet();
            case 14:  return Diets.wlVeg8Diet();
            case 15:  return Diets.wlVeg9Diet();
            case 16:  return Diets.wlVeg9Diet();
            case 17:  return Diets.wlVeg10Diet();
            case 18:  return Diets.wlVeg10Diet();
            case 19:  return Diets.wlVeg11Diet();
            case 20:  return Diets.wlVeg11Diet();
            case 21:  return Diets.wlVeg12Diet();
            case 22:  return Diets.wlVeg12Diet();
            case 23:  return Diets.wlVeg13Diet();
            case 24:  return Diets.wlVeg13Diet();
            case 25:  return Diets.wlVeg14Diet();
            case 26:  return Diets.wlVeg14Diet();
            case 27:  return Diets.wlVeg15Diet();
            case 28:  return Diets.wlVeg15Diet();
            case 29:  return Diets.wlVeg3Diet();//extra diet
            case 30:  return Diets.wlVeg3Diet();//extra diet
            default: return Diets.wlVeg1Diet();         
        }
	}
	
	private String caseWLNonVeg(int week){
		 switch (week) {
         case 1:  return Diets.wlNonVeg1Diet();
         case 2:  return Diets.wlNonVeg1Diet();
         case 3:  return Diets.wlNonVeg2Diet();
         case 4:  return Diets.wlNonVeg2Diet();
         case 5:  return Diets.wlNonVeg4Diet();
         case 6:  return Diets.wlNonVeg4Diet();
         case 7:  return Diets.wlNonVeg5Diet();
         case 8:  return Diets.wlNonVeg5Diet();
         case 9:  return Diets.wlNonVeg6Diet();
         case 10:  return Diets.wlNonVeg6Diet();
         case 11:  return Diets.wlNonVeg7Diet();
         case 12:  return Diets.wlNonVeg7Diet();
         case 13:  return Diets.wlNonVeg8Diet();
         case 14:  return Diets.wlNonVeg8Diet();
         case 15:  return Diets.wlNonVeg9Diet();
         case 16:  return Diets.wlNonVeg9Diet();
         case 17:  return Diets.wlNonVeg10Diet();
         case 18:  return Diets.wlNonVeg10Diet();
         case 19:  return Diets.wlNonVeg11Diet();
         case 20:  return Diets.wlNonVeg11Diet();
         case 21:  return Diets.wlNonVeg12Diet();
         case 22:  return Diets.wlNonVeg12Diet();
         case 23:  return Diets.wlNonVeg13Diet();
         case 24:  return Diets.wlNonVeg13Diet();
         case 25:  return Diets.wlNonVeg14Diet();
         case 26:  return Diets.wlNonVeg14Diet();
         case 27:  return Diets.wlNonVeg15Diet();
         case 28:  return Diets.wlNonVeg15Diet();
         case 29:  return Diets.wlNonVeg3Diet();//extra diet
         case 30:  return Diets.wlNonVeg3Diet();//extra diet
         default: return Diets.wlNonVeg1Diet();         
     }
	}
	
	private String caseWGVeg(int week){
		switch (week) {
	         case 1:  return Diets.wgVeg1Diet();
	         case 2:  return Diets.wgVeg1Diet();
	         case 3:  return Diets.wgVeg2Diet();
	         case 4:  return Diets.wgVeg2Diet();
	         case 5:  return Diets.wgVeg4Diet();
	         case 6:  return Diets.wgVeg4Diet();
	         case 7:  return Diets.wgVeg5Diet();
	         case 8:  return Diets.wgVeg5Diet();
	         case 9:  return Diets.wgVeg6Diet();
	         case 10:  return Diets.wgVeg6Diet();
	         case 11:  return Diets.wgVeg7Diet();
	         case 12:  return Diets.wgVeg7Diet();
	         case 13:  return Diets.wgVeg8Diet();
	         case 14:  return Diets.wgVeg8Diet();
	         case 15:  return Diets.wgVeg9Diet();
	         case 16:  return Diets.wgVeg9Diet();
	         case 17:  return Diets.wgVeg10Diet();
	         case 18:  return Diets.wgVeg10Diet();
	         case 19:  return Diets.wgVeg11Diet();
	         case 20:  return Diets.wgVeg11Diet();
	         case 21:  return Diets.wgVeg12Diet();
	         case 22:  return Diets.wgVeg12Diet();
	         case 23:  return Diets.wgVeg13Diet();
	         case 24:  return Diets.wgVeg13Diet();
	         case 25:  return Diets.wgVeg14Diet();
	         case 26:  return Diets.wgVeg14Diet();
	         case 27:  return Diets.wgVeg15Diet();
	         case 28:  return Diets.wgVeg15Diet();
	         case 29:  return Diets.wgVeg3Diet();//extra diet
	         case 30:  return Diets.wgVeg3Diet();//extra diet
	         case 31:  return Diets.wgVeg3Diet();//extra diet
	         default: return Diets.wgVeg1Diet();         
     }
	}

	private String caseWGNonVeg(int week){
		switch (week) {
	        case 1:  return Diets.wgNonVeg1Diet();
	        case 2:  return Diets.wgNonVeg1Diet();
	        case 3:  return Diets.wgNonVeg2Diet();
	        case 4:  return Diets.wgNonVeg2Diet();
	        case 5:  return Diets.wgNonVeg4Diet();
	        case 6:  return Diets.wgNonVeg4Diet();
	        case 7:  return Diets.wgNonVeg5Diet();
	        case 8:  return Diets.wgNonVeg5Diet();
	        case 9:  return Diets.wgNonVeg6Diet();
	        case 10:  return Diets.wgNonVeg6Diet();
	        case 11:  return Diets.wgNonVeg7Diet();
	        case 12:  return Diets.wgNonVeg7Diet();
	        case 13:  return Diets.wgNonVeg8Diet();
	        case 14:  return Diets.wgNonVeg8Diet();
	        case 15:  return Diets.wgNonVeg9Diet();
	        case 16:  return Diets.wgNonVeg9Diet();
	        case 17:  return Diets.wgNonVeg10Diet();
	        case 18:  return Diets.wgNonVeg10Diet();
	        case 19:  return Diets.wgNonVeg11Diet();
	        case 20:  return Diets.wgNonVeg11Diet();
	        case 21:  return Diets.wgNonVeg12Diet();
	        case 22:  return Diets.wgNonVeg12Diet();
	        case 23:  return Diets.wgNonVeg13Diet();
	        case 24:  return Diets.wgNonVeg13Diet();
	        case 25:  return Diets.wgNonVeg14Diet();
	        case 26:  return Diets.wgNonVeg14Diet();
	        case 27:  return Diets.wgNonVeg15Diet();
	        case 28:  return Diets.wgNonVeg15Diet();
	        case 29:  return Diets.wgNonVeg3Diet();//extra diet
	        case 30:  return Diets.wgNonVeg3Diet();//extra diet
	        case 31:  return Diets.wgNonVeg3Diet();//extra diet
	        default: return Diets.wgNonVeg1Diet();         
		}
	}
	
	private String caseWLMVeg(int week){
		int remainder = week %8;
	       switch (remainder) {
	       	   case 0:  return Diets.wlVeg2Diet();
	           case 1:  return Diets.wlVeg1Diet();
	           case 2:  return Diets.wlVeg1Diet();
	           case 3:  return Diets.wlVeg1Diet();
	           case 4:  return Diets.wlVeg1Diet();
	           case 5:  return Diets.wlVeg2Diet();
	           case 6:  return Diets.wlVeg2Diet();
	           case 7:  return Diets.wlVeg2Diet();
	           default: return Diets.wlVeg2Diet();         
       }
	}

	private String caseWLMNonVeg(int week){
		int remainder = week %8;
	       switch (remainder) {
	       	   case 0:  return Diets.wlNonVeg2Diet();
	           case 1:  return Diets.wlNonVeg1Diet();
	           case 2:  return Diets.wlNonVeg1Diet();
	           case 3:  return Diets.wlNonVeg1Diet();
	           case 4:  return Diets.wlNonVeg1Diet();
	           case 5:  return Diets.wlNonVeg2Diet();
	           case 6:  return Diets.wlNonVeg2Diet();
	           case 7:  return Diets.wlNonVeg2Diet();
	           default: return Diets.wlNonVeg2Diet();         
	       }
	}
	
	private String caseWGMVeg(int week){
		int remainder = week %8;
	       switch (remainder) {
	       	   case 0:  return Diets.wgVeg2Diet();
	           case 1:  return Diets.wgVeg1Diet();
	           case 2:  return Diets.wgVeg1Diet();
	           case 3:  return Diets.wgVeg1Diet();
	           case 4:  return Diets.wgVeg1Diet();
	           case 5:  return Diets.wgVeg2Diet();
	           case 6:  return Diets.wgVeg2Diet();
	           case 7:  return Diets.wgVeg2Diet();
	           default: return Diets.wgVeg2Diet();         
	       }
	}

	private String caseWGMNonVeg(int week){
		int remainder = week %8;
	       switch (remainder) {
	       	   case 0:  return Diets.wgNonVeg2Diet();
	           case 1:  return Diets.wgNonVeg1Diet();
	           case 2:  return Diets.wgNonVeg1Diet();
	           case 3:  return Diets.wgNonVeg1Diet();
	           case 4:  return Diets.wgNonVeg1Diet();
	           case 5:  return Diets.wgNonVeg2Diet();
	           case 6:  return Diets.wgNonVeg2Diet();
	           case 7:  return Diets.wgNonVeg2Diet();
	           default: return Diets.wgNonVeg2Diet();         
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
}

				
