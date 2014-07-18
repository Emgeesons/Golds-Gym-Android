package com.emgeesons.goldsgym;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

@SuppressLint("SimpleDateFormat")
public class AboutUserActivity extends Activity {
	
	//Variable Declaration
	SharedPreferences ggPrefs;
	private Intent nextScreenIntent;
	EditText nameText, numberText, emailText, areaText;
	ImageView maleImage, femaleImage;
	TextView yrsLabel;
	ProgressDialog pd;
	TextView nameLabel, emailLabel, numberLabel, areaLabel, ageLabel, genderLabel;
	
	// date and time
	 private int mYear;
	 private int mMonth;
	 private int mDay;
	 Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_user);
		
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
		context = getApplicationContext();
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		instantiateVariables();
		implementHandlers();
		
	}
	
	private void instantiateVariables(){
		yrsLabel = (TextView)findViewById(R.id.yrs_label);
		nameText = (EditText)findViewById(R.id.name_text);
		areaText = (EditText)findViewById(R.id.area_text);
		numberText =(EditText)findViewById(R.id.number_text);
		emailText = (EditText)findViewById(R.id.email_text1);
		maleImage = (ImageView)findViewById(R.id.male_image);
		femaleImage = (ImageView)findViewById(R.id.female_image);
		
		nameLabel = (TextView)findViewById(R.id.name_label);
		emailLabel = (TextView)findViewById(R.id.email_label);
		numberLabel = (TextView)findViewById(R.id.number_label);
		areaLabel = (TextView)findViewById(R.id.area_label);
		ageLabel = (TextView)findViewById(R.id.age_label);
		genderLabel = (TextView)findViewById(R.id.gender_label);
		
		//Setting up Date
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
	
	}

	private void updateDisplay() throws ParseException{
		Long currentMS = new Date().getTime();
		mMonth++;
		String dob = mMonth+"-"+mDay+"-"+mYear; 
		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		Date d1   = format.parse(dob);
		Long dobMS = d1.getTime();
		Long ageMS = currentMS - dobMS;
		int age = (int) (ageMS/31556952);
		age = age/1000;
		yrsLabel.setText(Integer.toString(age) + "  Yrs");
		yrsLabel.setTextColor(getResources().getColor(R.color.black_text));
		ggPrefs.edit().putString(LaunchAppActivity.AGE, Integer.toString(age)).commit();
		ggPrefs.edit().putString(LaunchAppActivity.DOB, dob).commit(); // put in mm-dd-yyyy format

	}
	
	private void implementHandlers(){
		yrsLabel.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  DatePickerDialog DPD = new DatePickerDialog(AboutUserActivity.this, mDateSetListener, mYear, mMonth,mDay);
				  DPD.getDatePicker().setMaxDate(new Date().getTime());
				  DPD.show();
			  }
		});
		
		femaleImage.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	femaleImage.setImageResource(R.drawable.f_black);
		    	maleImage.setImageResource(R.drawable.m_grey);
		    	ggPrefs.edit().putString(LaunchAppActivity.GENDER, "Female").commit();
		    }
		});
		
		maleImage.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	femaleImage.setImageResource(R.drawable.f_grey);
		    	maleImage.setImageResource(R.drawable.m_black);
		    	ggPrefs.edit().putString(LaunchAppActivity.GENDER, "Male").commit();
		    }
		});
		
	}
	
	 @Override
	 @Deprecated
	 protected void onPrepareDialog(int id, Dialog dialog) {
	  // TODO Auto-generated method stub
	  super.onPrepareDialog(id, dialog);

	  ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);

	 }

	 private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

	  public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
		   mYear = year;
		   mMonth = monthOfYear;
		   mDay = dayOfMonth;
		   try {
			updateDisplay();
		   } catch (ParseException e) {
		   }
	  }
	 };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.about_user, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_about_user:
	        	verifyUser();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void verifyUser(){
		//setting all to black
		nameLabel.setTextColor(Color.BLACK);
		ageLabel.setTextColor(Color.BLACK);
		emailLabel.setTextColor(Color.BLACK);
		numberLabel.setTextColor(Color.BLACK);
		genderLabel.setTextColor(Color.BLACK);
		areaLabel.setTextColor(Color.BLACK);
		
		boolean verified = true;
		
		if(nameText.getText().toString().length()<2){//condition is false
			verified = false;
			nameLabel.setTextColor(Color.RED);
		}
		if(areaText.getText().toString().length()<2){//condition is false
			verified = false;
			areaLabel.setTextColor(Color.RED);
		}
		if(numberText.getText().toString().length()<6){//condition is false
			verified = false;
			numberLabel.setTextColor(Color.RED);
		}
		if(emailText.getText().toString().length()<8){//condition is false
			verified = false;
			emailLabel.setTextColor(Color.RED);
		}
		if(ggPrefs.getString(LaunchAppActivity.GENDER, "Undefined").equalsIgnoreCase("Undefined")){
			verified = false;
			genderLabel.setTextColor(Color.RED);
		}
		if(ggPrefs.getString(LaunchAppActivity.AGE, "Undefined").equalsIgnoreCase("Undefined")){
			verified = false;
			ageLabel.setTextColor(Color.RED);
		}
			
		
		if(verified){
			//save it in preferences
			ggPrefs.edit().putString(LaunchAppActivity.NAME, nameText.getText().toString()+"  ").commit();
			ggPrefs.edit().putString(LaunchAppActivity.NUMBER, numberText.getText().toString()).commit();
			ggPrefs.edit().putString(LaunchAppActivity.EMAIL, emailText.getText().toString()).commit();
			ggPrefs.edit().putString(LaunchAppActivity.AREA, areaText.getText().toString()).commit();
			// register user and go to the welcome screen
			 registerUser();
		     
		}else{
			//if the information is not entered correctly
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, "Don't be shy! Fill In Your Details Correctly", duration);
			toast.show();
			
		}
	}
	
	private void registerUser(){
		final RequestQueue queue = Volley.newRequestQueue(this);
		String url="http://goldsgym.emgeesonsdevelopment.in/mobile1.0/userRegistration.php?name="+nameText.getText().toString().replace(" ", "%20")+"&number="+numberText.getText().toString()+"&email="+emailText.getText().toString()+"&gender="+ggPrefs.getString(LaunchAppActivity.GENDER, "Undefined")+"&dob="+ggPrefs.getString(LaunchAppActivity.DOB, "Undefined")+"&area="+ggPrefs.getString(LaunchAppActivity.AREA, "Undefined").replace(" ", "%20");
		pd = ProgressDialog.show(this, "", "Loading...",true);
	    pd.setCancelable(true);
	    pd.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				queue.cancelAll("register");
				
			}
	    	
	    });
	    JsonObjectRequest jar = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				
				try{
					//Store the user Id
					JSONObject getString = new JSONObject(response.toString());
					JSONArray jsonProductJSONArray = new JSONArray();
					jsonProductJSONArray = getString .getJSONArray("response");
					JSONObject c = jsonProductJSONArray.getJSONObject(0);
					ggPrefs.edit().putString(LaunchAppActivity.USER_ID, c.getString("userId")).commit();
					if(pd.isShowing()&&pd!=null){
			             pd.dismiss();
			        }
	    			Toast toast = Toast.makeText(context, "Account Activated!", Toast.LENGTH_SHORT);
 					toast.show();
					nextScreenIntent = new Intent(AboutUserActivity.this,HomeScreenActivity.class);
					startActivity(nextScreenIntent);
				}catch(Exception e){
					if(pd.isShowing()&&pd!=null){
			             pd.dismiss();
			        }
					int duration = Toast.LENGTH_LONG;
					Toast toast2 = Toast.makeText(context, "Something went wrong. Try again later", duration);
					toast2.show();
				}
			}
			
		}, new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				int duration = Toast.LENGTH_LONG;
				Toast toast = Toast.makeText(context, "Something went wrong. Try again later", duration);
				toast.show();
			}
			
		}) ;
		queue.add(jar).setTag("register");
	}
	



}


