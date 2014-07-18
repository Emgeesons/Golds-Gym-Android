package com.emgeesons.goldsgym;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Calendar;

import com.facebook.SessionDefaultAudience;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.SimpleFacebook.OnLoginListener;
import com.sromku.simple.fb.SimpleFacebook.OnLogoutListener;
import com.sromku.simple.fb.SimpleFacebook.OnPublishListener;
import com.sromku.simple.fb.entities.Feed;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RadioButton;
import android.widget.Toast;

public class HomeScreenActivity extends Activity {
	
	Intent nextScreenIntent;
	static SharedPreferences ggPrefs;
	 Context context;
	static Fragment profileFragment, trainerFragment, nutritionistFragment;
	static ActionBar actionbar;
	static SharedPreferences.Editor editor;
	PendingIntent pendingIntent;
	static int resultTextStringId=0;
	
    //Facebook
    protected static final String TAG = FullImageActivity.class.getName();
	protected static SimpleFacebook mSimpleFacebook;
	
	protected static void fbLogout(){
		mSimpleFacebook.logout(mOnLogoutListener);
	}
	
	protected static void fbLogin(){
		mSimpleFacebook.login(mOnLoginListener);
	}
	
	// Login listener
	private static OnLoginListener mOnLoginListener = new OnLoginListener()
	{

		@Override
		public void onFail(String reason)
		{
		//	Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable)
		{
			//Log.e(TAG, "Bad thing happened" + throwable.getMessage(), throwable);
		}

		@Override
		public void onThinking()
		{
			//pd = ProgressDialog.show(context, "", "Posting to Facebook ...",true);
			//pd.setCancelable(true);
			//Log.w(TAG, "Thinking");
		}

		@Override
		public void onLogin()
		{
			// change the state of the button or do whatever you want
		//	if(pd.isShowing()&&pd!=null){
	      //      pd.dismiss();
	       //}
			//Log.w(TAG, "Logged In");
			// feed builder
			final Feed feed = new Feed.Builder()
				.setMessage(TFragment.message)
				.setName(TFragment.name)
				.setCaption(TFragment.caption)
				.setDescription(TFragment.description)
				.setPicture(TFragment.picture)
				.setLink("https://play.google.com/store/apps/details?id=com.emgeesons.goldsgym")
				.addAction("Get the App", "https://play.google.com/store/apps/details?id=com.emgeesons.goldsgym")
				.build();
	    	HomeScreenActivity.mSimpleFacebook.publish(feed, HomeScreenActivity.onPublishListener);
			
		}

		@Override
		public void onNotAcceptingPermissions()
		{
			//Log.w(TAG, "Not accepted permission");
		}
	};
	
	// Logout listener
	private static OnLogoutListener mOnLogoutListener = new OnLogoutListener()
	{

		@Override
		public void onFail(String reason)
		{
		//	Log.w(TAG, "Failed to login");
		}

		@Override
		public void onException(Throwable throwable)
		{
			//Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking()
		{
			// show progress bar or something to the user while login is happening
		//	Log.w(TAG, "FThinking");
		}

		@Override
		public void onLogout()
		{
			// change the state of the button or do whatever you want
			//Log.w(TAG, "Logout");
		}

	};
	
	// listener for publishing action
	static OnPublishListener onPublishListener = new SimpleFacebook.OnPublishListener()
	{

		@Override
		public void onFail(String reason)
		{
			// insure that you are logged in before publishing
		//	Log.w(TAG, "Failed to publish");
			HomeScreenActivity.fbLogout();
		}

		@Override
		public void onException(Throwable throwable)
		{
		//	Log.e(TAG, "Bad thing happened", throwable);
		}

		@Override
		public void onThinking()
		{
			// show progress bar or something to the user while publishing
			
		}

		@Override
		public void onComplete(String postId){
		//	Log.w(TAG, "Published");
			HomeScreenActivity.fbLogout();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);
		
		context = getApplicationContext();
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String name = ggPrefs.getString(LaunchAppActivity.NAME, "").substring(0, ggPrefs.getString(LaunchAppActivity.NAME, "").indexOf(' '));
		name = name.substring(0,1).toUpperCase() + name.substring(1);
		this.setTitle(" Howdy " + name +",");
		
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
		
		trainingStatus();
		
		//ActionBar gets initiated
        actionbar = getActionBar();
        
        //Tell the ActionBar we want to use Tabs.
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
      //initiating tabs and set text to it.
        ActionBar.Tab TrainerTab = actionbar.newTab().setText("Trainer");
        ActionBar.Tab NutritionistTab = actionbar.newTab().setText("Nutritionist");
        ActionBar.Tab ProfileTab = actionbar.newTab().setText("Profile");
        
      //create the fragments we want to use for display content
         trainerFragment = new TFragment();
         nutritionistFragment = new NFragment();
         profileFragment = new PFragment();
        
      //set the Tab listener. Now we can listen for clicks.
        TrainerTab.setTabListener(new MyTabsListener(trainerFragment));
        NutritionistTab.setTabListener(new MyTabsListener(nutritionistFragment));
        ProfileTab.setTabListener(new MyTabsListener(profileFragment));
		
      //add the tabs to the actionbar
        actionbar.addTab(TrainerTab);
        actionbar.addTab(NutritionistTab);
        actionbar.addTab(ProfileTab);
        
	}
	

	
	@Override
	protected void onResume() {
		super.onResume();
		if(LaunchAppActivity.weightPrefs == null){
			Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		}
		else{	
			trainingStatus();
		}
		mSimpleFacebook = SimpleFacebook.getInstance(this);
	}
	
	
	protected static void trainingStatus(){
		//if training program is undefined. set training status and start - end date to be undefined as well
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Undefined")){
			reset();
		}
		// if training status is not undefined then do the following
		//begun, maintenance, over
		else {
			Calendar startDate = dateFromString(ggPrefs.getString(LaunchAppActivity.START_DATE, "00-00-0000"));
			Calendar endDate = dateFromString(ggPrefs.getString(LaunchAppActivity.END_DATE, "00-00-0000"));
			Calendar currentDate = Calendar.getInstance(); 
			long diffEndCurrent = endDate.getTimeInMillis() - currentDate.getTimeInMillis();
			//checking for vacation
			Calendar vacationEndDate = dateFromString(ggPrefs.getString(LaunchAppActivity.VACATION_END_DATE, "00-00-0000"));
			long diffVacCurrent = vacationEndDate.getTimeInMillis() + 24*60*60*1000 - currentDate.getTimeInMillis(); //Adding the 1 day
			if(diffVacCurrent>0)
				LaunchAppActivity.vacation = true;
			else
				LaunchAppActivity.vacation = false;
			//checking for end date & indeterminate state
			if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") && diffEndCurrent < 0){//end date is over - they should either go to Maintenance or Over (Reset)
				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE,"Indeterminate").commit();
			}
			//calculate the week or month going on for the training schedule - make this a static variable and not persistent
				long diffCurrentStart = currentDate.getTimeInMillis() - startDate.getTimeInMillis();	
				LaunchAppActivity.daysLeft = (int)( Math.floor(diffEndCurrent / (24 * 60 * 60 * 1000))); // Math.ceil always rounds down
				LaunchAppActivity.currentDay = (int)( Math.floor(diffCurrentStart / (24 * 60 * 60 * 1000))) + 1;
				int rem = LaunchAppActivity.currentDay %30;
				if(rem == 0)
					LaunchAppActivity.currentMonth = (int) Math.floor(LaunchAppActivity.currentDay / 30);
				else
					LaunchAppActivity.currentMonth = (int) Math.floor(LaunchAppActivity.currentDay / 30) + 1;
				int rem2 = LaunchAppActivity.currentDay%7;
				if(rem2 == 0 )
					LaunchAppActivity.currentWeek = (int) Math.floor(LaunchAppActivity.currentDay / 7);
				else
					LaunchAppActivity.currentWeek = (int) Math.floor(LaunchAppActivity.currentDay / 7) + 1;
				
				Calendar calendar = Calendar.getInstance();
				int day = calendar.get(Calendar.DAY_OF_WEEK); 
				LaunchAppActivity.currentDOW = day;
				if(LaunchAppActivity.currentDOW == 1)
					LaunchAppActivity.currentDOW = 8;
				LaunchAppActivity.currentDOW -=1;
			
		}

		
	}
	
	protected static void setTab(int position){
		actionbar.setSelectedNavigationItem(position);
	}
	
	protected static int currentTab(){
		return actionbar.getSelectedNavigationIndex();
	}
	
	
	

	
	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;
		 
		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}
		 
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
		 
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.fragment_container, fragment);
		}
		 
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(fragment);
		}
		 
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_screen, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_vip_pass:
	    		nextScreenIntent = new Intent(HomeScreenActivity.this,VipPassActivity.class);
		     	startActivity(nextScreenIntent);
		     	return true;
	    	case R.id.action_gyms:
				 nextScreenIntent = new Intent(HomeScreenActivity.this,GymCitiesActivity.class);
			     startActivity(nextScreenIntent);
	            return true;
		    case R.id.action_offers:
				 nextScreenIntent = new Intent(HomeScreenActivity.this,OffersActivity.class);
				 nextScreenIntent.putExtra("type", "Offers");
				 nextScreenIntent.putExtra("requestType", "offers");
			     startActivity(nextScreenIntent);
	            return true; 
		    case R.id.action_events:
				 nextScreenIntent = new Intent(HomeScreenActivity.this,OffersActivity.class);
				 nextScreenIntent.putExtra("type", "Events");
				 nextScreenIntent.putExtra("requestType", "event");
			     startActivity(nextScreenIntent);
	            return true; 
		    case R.id.action_vacation:
		    	if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance")){
		        	DialogFragment vacationFragment = new VacationDialogFragment();
		        	vacationFragment.show(getFragmentManager(),"VacationDialogFragment");
		    	}else{
					int duration = Toast.LENGTH_SHORT;
					Toast toast = Toast.makeText(context, "Feature Available Once You've Set Your Goal", duration);
					toast.show();
		    	}
	            return true; 
		    case R.id.action_reset:
	        	DialogFragment resetFragment = new ResetGoalDialogFragment();
	        	resetFragment.show(getFragmentManager(),"ResetGoalDialogFragment");
	            return true; 
	        case R.id.action_share:
	        	nextScreenIntent = new Intent();
	        	nextScreenIntent.setAction(Intent.ACTION_SEND);
	        	nextScreenIntent.putExtra(Intent.EXTRA_TEXT, "Hi, I'm using the Gold's Gym Android App. Download it now from - http://tinyurl.com/goldsgymindia-android");
	        	nextScreenIntent.setType("text/plain");
	        	startActivity(nextScreenIntent);
	            return true;
	        case R.id.action_rate_us:
	        	String url = getString(R.string.rating_link);
	        	nextScreenIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	        	startActivity(nextScreenIntent);
	            return true;
	        case R.id.action_terms:
				 nextScreenIntent = new Intent(HomeScreenActivity.this,TermsActivity.class);
			     startActivity(nextScreenIntent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	protected void onActivityResult(int requestCode, int resultCode,Intent imageReturnedIntent) {
		mSimpleFacebook.onActivityResult(this, requestCode, resultCode, imageReturnedIntent);
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
	}
	
	protected static Calendar dateFromString(String date){
		String[] dateSeperated = date.split("-");
		  Calendar day = Calendar.getInstance();
		  day.set(Integer.parseInt(dateSeperated[2]), Integer.parseInt(dateSeperated[1]), Integer.parseInt(dateSeperated[0]));
		  return day;
	}
	
	protected int monthsLeft(Calendar startDate, Calendar endDate){
		long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
		int days = (int)( Math.ceil(diff / (24 * 60 * 60 * 1000))); // Math.ceil always rounds up
		return days;
	}
	
	protected static void setNoMonths(){
		double d = 0;
		int noMonths=0;
		d=ggPrefs.getFloat(LaunchAppActivity.WEIGHT_AMOUNT,0);
		if(d<4.5){
			noMonths=1;
		}else if(d<8.5){
			noMonths=2;
		}else if(d<10.5){
			noMonths=3;
		}else if(d<13.5){
			noMonths=4;
		}else if(d<15.5){
			noMonths=5;
		}else if(d<18.5){
			noMonths=6;
		}else{
			noMonths=7;
		}
		ggPrefs.edit().putInt(LaunchAppActivity.NO_MONTHS, noMonths).commit();
	}
	
	//carry forward to dietary recall
	protected static void setDates(){
		Calendar startDate = Calendar.getInstance();
		//Month is -1 of the actual month(starts from 0 goes to 11)
		String startDateString = Integer.toString(startDate.get(Calendar.DAY_OF_MONTH))+"-"+Integer.toString(startDate.get(Calendar.MONTH))+"-"+Integer.toString(startDate.get(Calendar.YEAR));
		//End Date
		double d = 0;
		int noMonths=0;
		d=ggPrefs.getFloat(LaunchAppActivity.WEIGHT_AMOUNT,0);
		if(d<4.5){
			noMonths=1;
		}else if(d<8.5){
			noMonths=2;
		}else if(d<10.5){
			noMonths=3;
		}else if(d<13.5){
			noMonths=4;
		}else if(d<15.5){
			noMonths=5;
		}else if(d<18.5){
			noMonths=6;
		}else{
			noMonths=7;
		}
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DATE,noMonths*30);
		String endDateString = Integer.toString(endDate.get(Calendar.DAY_OF_MONTH))+"-"+Integer.toString(endDate.get(Calendar.MONTH))+"-"+Integer.toString(endDate.get(Calendar.YEAR));
		//Setting the values
		ggPrefs.edit().putString(LaunchAppActivity.START_DATE, startDateString).commit();
		ggPrefs.edit().putString(LaunchAppActivity.END_DATE, endDateString).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.NO_MONTHS, noMonths).commit();
		
		LaunchAppActivity.currentWeek=1;
		LaunchAppActivity.currentDay =1;
		LaunchAppActivity.currentMonth=1;
		LaunchAppActivity.daysLeft=daysLeft(startDate, endDate);
	}
	
	protected static int daysLeft(Calendar startDate, Calendar endDate){
		long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();
		int days = (int)( Math.floor(diff / (24 * 60 * 60 * 1000))); // Math.ceil always rounds up
		return days;
	}
	
	protected static void reset(){
		ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Undefined").commit();
		//Dietary Recall
		ggPrefs.edit().putString(LaunchAppActivity.VEG_NONVEG, "Undefined").commit();
		//Dates
		ggPrefs.edit().putString(LaunchAppActivity.START_DATE, "00-00-0000").commit();
		ggPrefs.edit().putString(LaunchAppActivity.END_DATE, "00-00-0000").commit();
		ggPrefs.edit().putInt(LaunchAppActivity.NO_MONTHS, 0).commit();
		//Weight Amount & Program
		ggPrefs.edit().putFloat(LaunchAppActivity.WEIGHT_AMOUNT, 0).commit();
		ggPrefs.edit().putFloat(LaunchAppActivity.WEIGHT_BEGIN, 0).commit();
		ggPrefs.edit().putString(LaunchAppActivity.TRAINING_PROGRAM, "Undefined").commit();
		ggPrefs.edit().putString(LaunchAppActivity.TRAINING_LEVEL, "Undefined").commit();
		ggPrefs.edit().putInt(LaunchAppActivity.TOTAL_WORKOUTS, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.WORKOUTS_MISSED, 0).commit();
		//Medical Conditions
		ggPrefs.edit().putBoolean(LaunchAppActivity.PCOS, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.PREGNANCY, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.THYROID, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WRIST, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.KNEE, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.LOWER_BACK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.HYPERTENSION, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.DIABETES, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.SPONDILYTIS, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.CHOLESTEROL, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.OTHER_MEDICAL, false).commit();
		//Vacation
		ggPrefs.edit().putString(LaunchAppActivity.VACATION_END_DATE, "00-00-0000").commit();
		//workout log
		if(LaunchAppActivity.tickPrefs !=null){
			editor = LaunchAppActivity.tickPrefs.edit();
			editor.clear().commit();
		}
		//achievements
		ggPrefs.edit().putBoolean(LaunchAppActivity.PERFECT_WEEK, false).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_MONTH, 0).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_25, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_50, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_75, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WEIGHT_100, false).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_25_ELAPSED, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_50_ELAPSED, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.WEIGHT_75_ELAPSED, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_MONTH_ELAPSED, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.PERFECT_WEEK_ELAPSED, 0).commit();
		ggPrefs.edit().putFloat(LaunchAppActivity.CURRENT_WEIGHT, 0).commit();
		ggPrefs.edit().putInt(LaunchAppActivity.CURRENT_WEIGHT_MONTH, 0).commit();
		//advanced profile
		ggPrefs.edit().putInt(LaunchAppActivity.ADVANCED_PROFILE, 0).commit();
		//reminder
		ggPrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT, "").commit();
		ggPrefs.edit().putString(LaunchAppActivity.YESTERDAYS_WORKOUT,"").commit();
		//static variables
		LaunchAppActivity.currentDay = 0;
		LaunchAppActivity.currentMonth = 0;
		LaunchAppActivity.currentWeek = 0;
		LaunchAppActivity.achievement = false;
		LaunchAppActivity.vacation = false;
		LaunchAppActivity.daysLeft = 0;
		LaunchAppActivity.currentDOW = 0;
		
		//ticks
		ggPrefs.edit().putBoolean(LaunchAppActivity.MONDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.TUESDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.WEDNESDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.THURSDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.FRIDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.SATURDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.SUNDAY_TICK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.OFFERS_COACHMARK, false).commit();
		ggPrefs.edit().putBoolean(LaunchAppActivity.CHANGE_ROUTINE, false).commit();
		
		//delete everything from the chart preferences
		if(LaunchAppActivity.weightPrefs!=null && LaunchAppActivity.smmPrefs!=null && LaunchAppActivity.pbfPrefs!=null  && LaunchAppActivity.notifPrefs!=null){
			editor = LaunchAppActivity.weightPrefs.edit();
			editor.clear().commit();
			editor = LaunchAppActivity.smmPrefs.edit();
			editor.clear().commit();
			editor = LaunchAppActivity.pbfPrefs.edit();
			editor.clear().commit();
			editor = LaunchAppActivity.notifPrefs.edit();
			editor.clear().commit();
		}
		
		//deleting images

        String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
			
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    switch(view.getId()) {
	        case R.id.radio_dont_know:
	            if (checked)
	            	resultTextStringId = R.string.dont_know_result;
	            break;
	        case R.id.radio_muscle:
	            if (checked)
	            	resultTextStringId = R.string.muscle_result;
	            break;
	        case R.id.radio_fat:
	        	if(checked){
	        		float currentWeight = ggPrefs.getFloat(LaunchAppActivity.CURRENT_WEIGHT, 0);
			    	float beginWeight = ggPrefs.getFloat(LaunchAppActivity.WEIGHT_BEGIN, 0);
			    	if((currentWeight - beginWeight) < 1.49)
			    		resultTextStringId = R.string.fat_result_1;
			    	else if ((currentWeight - beginWeight) < 2.49)
			    		resultTextStringId = R.string.fat_result_2;
			    	else
			    		resultTextStringId = R.string.fat_result_3;
	        	}
	            break;
	    }
	}





	
	
}
