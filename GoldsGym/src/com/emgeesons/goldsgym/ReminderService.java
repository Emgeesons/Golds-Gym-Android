package com.emgeesons.goldsgym;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class ReminderService extends Service {

	  private NotificationManager mManager;
	  Context context;
	  static protected SharedPreferences servicePrefs;
	  Intent nextScreenIntent;
	  
	    @Override
	    public IBinder onBind(Intent arg0)
	    {
	       // TODO Auto-generated method stub
	        return null;
	    }
	 
	    @Override
	    public void onCreate() 
	    {
	       // TODO Auto-generated method stub  
	       super.onCreate();
	       //maybe you can put the below code over here
	    }
	 
	   
	   @Override
	   public void onStart(Intent intent, int startId)
	   {
	       super.onStart(intent, startId);
	       boolean remind= false;
	       context = getApplicationContext();
	       //get reference to tickPrefs and check the current Date;
	       servicePrefs = PreferenceManager.getDefaultSharedPreferences(context);
	       LaunchAppActivity.tickPrefs = context.getSharedPreferences("tickPrefs",Context.MODE_PRIVATE);
	       LaunchAppActivity.notifPrefs = context.getSharedPreferences("notifPrefs",Context.MODE_PRIVATE);

	       if(servicePrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || servicePrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance")){
	    	   //get the current day - verify if it works properly
	    	   Calendar startDate = HomeScreenActivity.dateFromString(servicePrefs.getString(LaunchAppActivity.START_DATE, "00-00-0000"));
	    	   Calendar currentDate = Calendar.getInstance(); 
	    	   long diffCurrentStart = currentDate.getTimeInMillis() - startDate.getTimeInMillis();		
	    	   LaunchAppActivity.currentDay = (int)( Math.floor(diffCurrentStart / (24 * 60 * 60 * 1000))) + 1;
	    	   //not if it has already happened today
	    	   if(!LaunchAppActivity.notifPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay),false) && LaunchAppActivity.currentDay > 1){
				       //checking for yesterdays workout
			    	   remind = !LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay - 1), false); 
			    	   if(remind){
			    		   createNotification("Gold's Gym","Log Yesterdays Workout");
			    		   LaunchAppActivity.notifPrefs.edit().putBoolean(Integer.toString(LaunchAppActivity.currentDay), true).commit();
			    	   }//end of yesterdays workout reminder
			    	   //checking if they have a workout today
			    	  calcStuff();
			    	  String workout = servicePrefs.getString(LaunchAppActivity.TODAYS_WORKOUT, "");
			    	   if(!workout.equalsIgnoreCase("") && !workout.equalsIgnoreCase("Rest") && !LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay), false)){
			    		   createNotification("Gold's Gym","Today's Workout - "+ workout);
			    		   LaunchAppActivity.notifPrefs.edit().putBoolean(Integer.toString(LaunchAppActivity.currentDay), true).commit();
			    	   }//end of todays workout reminder

					    createAlarm();
			       }//end of it has already happened today
	       }//end of goal defined
	    }
	   
	   private void calcStuff(){
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
			
			Calendar startDate = HomeScreenActivity.dateFromString(servicePrefs.getString(LaunchAppActivity.START_DATE, "00-00-0000"));
			Calendar endDate = HomeScreenActivity.dateFromString(servicePrefs.getString(LaunchAppActivity.END_DATE, "00-00-0000"));
			Calendar currentDate = Calendar.getInstance(); 
			long diffEndCurrent = endDate.getTimeInMillis() - currentDate.getTimeInMillis();
			//checking for vacation
			Calendar vacationEndDate = HomeScreenActivity.dateFromString(servicePrefs.getString(LaunchAppActivity.VACATION_END_DATE, "00-00-0000"));
			long diffVacCurrent = vacationEndDate.getTimeInMillis() - currentDate.getTimeInMillis();
			if(diffVacCurrent>0)
				LaunchAppActivity.vacation = true;
			else
				LaunchAppActivity.vacation = false;
			
			if(LaunchAppActivity.vacation){
				switch(LaunchAppActivity.currentDOW){
					case 1: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[0]).commit(); 
							break;
					case 2: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[1]).commit();
							break;
					case 3: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[2]).commit(); 
							break;
					case 4: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[3]).commit();
							break;
					case 5: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[4]).commit();
							break;
					case 6: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[5]).commit();
							break;
					case 7: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[6]).commit();
							break;
					default: servicePrefs.edit().putString(LaunchAppActivity.TODAYS_WORKOUT,Trainer.vacationFunctional1[0]).commit();
					
				}
			}else{// no vacation
				if(servicePrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Weight Loss").equalsIgnoreCase("Weight Loss")){
					//This is for weight Loss
					//You also have to keep track of current month and for intermediate 2 - current week
					if(servicePrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
						//if person is on maintenance
						TFragment.weightLossMaintenance(false);
					}else{
						//not on maintenance
						TFragment.weightLossNormal(false);
					}
				}else{
					//This is for weight gain
					//You also have to keep track of current month and for intermediate 2 - current week					
					if(servicePrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
						//if person is on maintenance
						TFragment.weightGainMaintenance(false);
					}else{
						//not on maintenance
						TFragment.weightGainNormal(false);
					}
				}
			}
		   
	   }
	 
	    @Override
	    public void onDestroy() 
	    {
	        // TODO Auto-generated method stub
	        super.onDestroy();
	    }
	    
		protected void createNotification(String title, String text){
			//use notification builder
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
			Notification notification = new Notification();
			notification.icon = R.drawable.notification_icon;
			notification.tickerText = text;
			notification.when = System.currentTimeMillis();
			notification.defaults = Notification.DEFAULT_ALL;
			notification.flags = notification.flags | notification.FLAG_AUTO_CANCEL;
			
			nextScreenIntent = new Intent(context,LaunchAppActivity.class);
			PendingIntent intent = PendingIntent.getActivity(context, 0, nextScreenIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		        		
			notification.setLatestEventInfo(context, title, text, intent);
			mNotificationManager.notify(1, notification);
			
			
			
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
		     
		    Intent myIntent = new Intent(context, ReminderReceiver.class);
		    pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent,0);
		     
		    AlarmManager alarmManager = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
		    alarmManager.set(AlarmManager.RTC, alarmDate.getTimeInMillis(), pendingIntent);
		}

}
