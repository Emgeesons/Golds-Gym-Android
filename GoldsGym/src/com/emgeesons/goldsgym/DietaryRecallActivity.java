package com.emgeesons.goldsgym;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DietaryRecallActivity  extends Activity {
	
	Context context;
	SharedPreferences ggPrefs;
	Spinner breakfastSpinner_1_1, breakfastSpinner_1_2, breakfastTimingSpinner;
	Spinner lunchSpinner_1_1, lunchSpinner_1_2, lunchTimingSpinner;
	Spinner snacksSpinner_1_1, snacksSpinner_1_2, snacksTimingSpinner;
	Spinner dinnerSpinner_1_1, dinnerSpinner_1_2, dinnerTimingSpinner;
	Spinner bedtimeSpinner_1_1, bedtimeSpinner_1_2, bedtimeTimingSpinner;
	List<Spinner> breakfastSpinners =  new ArrayList<Spinner>();
	List<Spinner> lunchSpinners =  new ArrayList<Spinner>();
	List<Spinner> snacksSpinners =  new ArrayList<Spinner>();
	List<Spinner> dinnerSpinners =  new ArrayList<Spinner>();
	List<Spinner> bedtimeSpinners =  new ArrayList<Spinner>();
	ArrayAdapter<CharSequence> breakfastAdapter_1_1, breakfastAdapter_1_2, breakfastTimingsAdapter;
	ArrayAdapter<CharSequence> lunchAdapter_1_1, lunchAdapter_1_2, lunchTimingsAdapter;
	ArrayAdapter<CharSequence> snacksAdapter_1_1, snacksAdapter_1_2, snacksTimingsAdapter;
	ArrayAdapter<CharSequence> dinnerAdapter_1_1, dinnerAdapter_1_2, dinnerTimingsAdapter;
	ArrayAdapter<CharSequence> bedtimeAdapter_1_1, bedtimeAdapter_1_2, bedtimeTimingsAdapter;
	RadioButton radioVeg, radioNonveg;
	ImageView breakfastPlus, lunchPlus, snacksPlus, dinnerPlus, bedtimePlus;
	RelativeLayout relativeLayout;
	RelativeLayout.LayoutParams  layoutParams;
	int breakfastId1, breakfastId2, lunchId1, lunchId2, snacksId1, snackId2, dinnerId1, dinnerId2, bedtimeId1, bedtimeId2;
	String id, id2;
	boolean mealSelected=false;
	RadioGroup mealType;
	double energy = 0, carbs = 0, protein = 0, fat = 0, fibre = 0;
	int breakfastSize, lunchSize, snacksSize, dinnerSize, bedtimeSize;
	String breakfast, lunch, dinner, snacks, bedtime;
	int breakfastServing, lunchServing, dinnerServing, snacksServing, bedtimeServing;
	Map<String, double[]> foodMap;
	String alertMessage, timingsMessage;
	RelativeLayout breakfastRelativeLayout, lunchRelativeLayout, snacksRelativeLayout, dinnerRelativeLayout, bedtimeRelativeLayout;
	TextView breakfastSkip, lunchSkip, snacksSkip, dinnerSkip, bedtimeSkip;
	String toastMessage;
	int viewCount = 5;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dietary_recall);
		
		//Shared Preferences
		 context = DietaryRecallActivity.this;
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
  		vegNonveg();
  		breakfast();
  		initializeLayouts();
  		lunch();
  		snacks();
  		dinner();
  		bedtime();
	}
	
	private void vegNonveg(){
		 //Radio Buttons
		 radioVeg = (RadioButton) findViewById(R.id.radio_veg);
		 radioNonveg = (RadioButton) findViewById(R.id.radio_nonveg);
		 mealType=(RadioGroup)findViewById(R.id.veg_nonveg_radio_group);
		 mealType.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener(){
			 public void onCheckedChanged(RadioGroup arg0, int arg1) {
			    RadioButton rb=(RadioButton)findViewById(arg1);
			    ggPrefs.edit().putString(LaunchAppActivity.VEG_NONVEG, rb.getText().toString()).commit();
			    mealSelected=true;
			 }
	         
	     });
	}
	
	private void initializeLayouts(){
		breakfastRelativeLayout = (RelativeLayout) findViewById (R.id.breakfast_relative_layout);
		lunchRelativeLayout = (RelativeLayout) findViewById(R.id.lunch_relative_layout);
		snacksRelativeLayout = (RelativeLayout) findViewById(R.id.snacks_relative_layout);
		dinnerRelativeLayout = (RelativeLayout) findViewById(R.id.dinner_relative_layout);
		bedtimeRelativeLayout = (RelativeLayout) findViewById(R.id.bedtime_relative_layout);
		
		lunchRelativeLayout.setVisibility(View.GONE);
		snacksRelativeLayout.setVisibility(View.GONE);
		dinnerRelativeLayout.setVisibility(View.GONE);
		bedtimeRelativeLayout.setVisibility(View.GONE);
		
		//skipping meals
		breakfastSkip = (TextView) findViewById(R.id.breakfast_skip);
		lunchSkip = (TextView) findViewById(R.id.lunch_skip);
		snacksSkip = (TextView) findViewById(R.id.snacks_skip);
		dinnerSkip = (TextView) findViewById(R.id.dinner_skip);
		bedtimeSkip = (TextView) findViewById(R.id.bedtime_skip);
		
		//Listeners
		breakfastSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	breakfastRelativeLayout.setVisibility(View.GONE);
            	lunchRelativeLayout.setVisibility(View.VISIBLE);
            	viewCount --;
            }
        });
		lunchSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	lunchRelativeLayout.setVisibility(View.GONE);
            	snacksRelativeLayout.setVisibility(View.VISIBLE);
            	viewCount--;
            }
        });
		snacksSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	snacksRelativeLayout.setVisibility(View.GONE);
            	dinnerRelativeLayout.setVisibility(View.VISIBLE);
            	viewCount --;
            }
        });
		dinnerSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	dinnerRelativeLayout.setVisibility(View.GONE);
            	bedtimeRelativeLayout.setVisibility(View.VISIBLE);
            	viewCount --;
            }
        });
		bedtimeSkip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	bedtimeRelativeLayout.setVisibility(View.GONE);
            	viewCount --;
            }
        });
	}
	
	
	private void breakfast(){
		//Timings Spinner
		breakfastTimingSpinner = (Spinner) findViewById(R.id.breakfast_timing_spinner);
		breakfastTimingsAdapter = ArrayAdapter.createFromResource(this,R.array.breakfast_timings_array, android.R.layout.simple_spinner_item);
		breakfastTimingsAdapter.setDropDownViewResource(R.layout.spinner_item);
		breakfastTimingSpinner.setAdapter(breakfastTimingsAdapter);
		
		//ImageViews
		breakfastPlus = (ImageView) findViewById(R.id.breakfast_plus);
		
		//Spinner
		//Breakfast section
		breakfastSpinner_1_1 = (Spinner) findViewById(R.id.breakfast_spinner_1_1);
		 breakfastAdapter_1_1 = ArrayAdapter.createFromResource(this,R.array.breakfast_array, android.R.layout.simple_spinner_item);
		 breakfastAdapter_1_1.setDropDownViewResource(R.layout.spinner_item);
		 breakfastSpinner_1_1.setAdapter(breakfastAdapter_1_1);
		 breakfastSpinners.add(breakfastSpinner_1_1);
		 
		 breakfastSpinner_1_2 = (Spinner) findViewById(R.id.breakfast_spinner_1_2);
		 breakfastAdapter_1_2 = ArrayAdapter.createFromResource(this,R.array.servings_array, android.R.layout.simple_spinner_item);
		 breakfastAdapter_1_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 breakfastSpinner_1_2.setAdapter(breakfastAdapter_1_2);
		 breakfastSpinners.add(breakfastSpinner_1_2);

		 //Assigning the Id
		 breakfastId1 = R.id.breakfast_spinner_1_1;
		 
		 //Spinner Listener
		 breakfastSpinner_1_2.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	if (lunchRelativeLayout.getVisibility() == View.GONE) {
	            	    lunchRelativeLayout.setVisibility(View.VISIBLE);
	            	}  
	                return false;
	                
	            }
	        });
		 
			//Breakfast Plus Button
			breakfastPlus.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	//First Spinner
	 		    	breakfastSpinner_1_1 = new Spinner(context);
	 		    	id = "1"+Integer.toString(breakfastSpinners.size());
	 		    	breakfastSpinner_1_1.setId(Integer.parseInt(id));
	 		    	breakfastAdapter_1_1 = ArrayAdapter.createFromResource(DietaryRecallActivity.this,R.array.breakfast_array, android.R.layout.simple_spinner_item);
	 		    	breakfastAdapter_1_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 		    	breakfastSpinner_1_1.setAdapter(breakfastAdapter_1_1);
	 		    	breakfastSpinners.add(breakfastSpinner_1_1);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,breakfastId1);
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.breakfast_relative_layout);
	 		    	relativeLayout.addView(breakfastSpinner_1_1,layoutParams);
	 		    	
	 		    	//Second Spinner
	 		    	breakfastSpinner_1_2 = new Spinner(context);
	 		    	id2 = "1"+Integer.toString(breakfastSpinners.size());
	 		    	breakfastSpinner_1_2.setId(Integer.parseInt(id2));
	 		    	breakfastSpinner_1_2.setAdapter(breakfastAdapter_1_2);
	 		    	breakfastSpinners.add(breakfastSpinner_1_2);
//	 		    	breakfastSpinner_1_2.setPadding(0, 0, 0, 0);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,breakfastId1);
	 		    	layoutParams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(id));
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.breakfast_relative_layout);
	 		    	relativeLayout.addView(breakfastSpinner_1_2,layoutParams);
	 		    	breakfastId1 = Integer.parseInt(id);
	 		    	
	 		    	
	 		    }
	 		});
		 
	}
	
	private void lunch(){
		//Timings Spinner
		lunchTimingSpinner = (Spinner) findViewById(R.id.lunch_timing_spinner);
		lunchTimingsAdapter = ArrayAdapter.createFromResource(this,R.array.lunch_timings_array, android.R.layout.simple_spinner_item);
		lunchTimingsAdapter.setDropDownViewResource(R.layout.spinner_item);
		lunchTimingSpinner.setAdapter(lunchTimingsAdapter);
		
		//ImageViews
		lunchPlus = (ImageView) findViewById(R.id.lunch_plus);
		
		//Spinner
		//Lunch section
		lunchSpinner_1_1 = (Spinner) findViewById(R.id.lunch_spinner_1_1);
		 lunchAdapter_1_1 = ArrayAdapter.createFromResource(this,R.array.main_course_array, android.R.layout.simple_spinner_item);
		 lunchAdapter_1_1.setDropDownViewResource(R.layout.spinner_item);
		 lunchSpinner_1_1.setAdapter(lunchAdapter_1_1);
		 lunchSpinners.add(lunchSpinner_1_1);
		 
		 lunchSpinner_1_2 = (Spinner) findViewById(R.id.lunch_spinner_1_2);
		 lunchAdapter_1_2 = ArrayAdapter.createFromResource(this,R.array.servings_array, android.R.layout.simple_spinner_item);
		 lunchAdapter_1_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 lunchSpinner_1_2.setAdapter(lunchAdapter_1_2);
		 lunchSpinners.add(lunchSpinner_1_2);

		 //Assigning the Id
		 lunchId1 = R.id.lunch_spinner_1_1;
		 
		//Spinner Listener
		 lunchSpinner_1_2.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	if (snacksRelativeLayout.getVisibility() == View.GONE) {
	            	    snacksRelativeLayout.setVisibility(View.VISIBLE);
	            	} 
	                return false;
	                
	            }
	        });
		 
			//Lunch Plus Button
			lunchPlus.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	//First Spinner
	 		    	lunchSpinner_1_1 = new Spinner(context);
	 		    	id = "1"+Integer.toString(lunchSpinners.size());
	 		    	lunchSpinner_1_1.setId(Integer.parseInt(id));
	 		    	lunchAdapter_1_1 = ArrayAdapter.createFromResource(DietaryRecallActivity.this,R.array.main_course_array, android.R.layout.simple_spinner_item);
	 		    	lunchAdapter_1_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 		    	lunchSpinner_1_1.setAdapter(lunchAdapter_1_1);
	 		    	lunchSpinners.add(lunchSpinner_1_1);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,lunchId1);
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.lunch_relative_layout);
	 		    	relativeLayout.addView(lunchSpinner_1_1,layoutParams);
	 		    	
	 		    	//Second Spinner
	 		    	lunchSpinner_1_2 = new Spinner(context);
	 		    	id2 = "1"+Integer.toString(lunchSpinners.size());
	 		    	lunchSpinner_1_2.setId(Integer.parseInt(id2));
	 		    	lunchSpinner_1_2.setAdapter(lunchAdapter_1_2);
	 		    	lunchSpinners.add(lunchSpinner_1_2);
//	 		    	lunchSpinner_1_2.setPadding(0, 0, 0, 0);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,lunchId1);
	 		    	layoutParams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(id));
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.lunch_relative_layout);
	 		    	relativeLayout.addView(lunchSpinner_1_2,layoutParams);
	 		    	lunchId1 = Integer.parseInt(id);
	 		    	
	 		    	
	 		    }
	 		});
		 
		 
	}
	
	private void snacks(){
		//Timings Spinner
		snacksTimingSpinner = (Spinner) findViewById(R.id.snacks_timing_spinner);
		snacksTimingsAdapter = ArrayAdapter.createFromResource(this,R.array.snacks_timings_array, android.R.layout.simple_spinner_item);
		snacksTimingsAdapter.setDropDownViewResource(R.layout.spinner_item);
		snacksTimingSpinner.setAdapter(snacksTimingsAdapter);
		
		//ImageViews
		snacksPlus = (ImageView) findViewById(R.id.snacks_plus);
		
		//Spinner
		//Breakfast section
		snacksSpinner_1_1 = (Spinner) findViewById(R.id.snacks_spinner_1_1);
		 snacksAdapter_1_1 = ArrayAdapter.createFromResource(this,R.array.snacks_array, android.R.layout.simple_spinner_item);
		 snacksAdapter_1_1.setDropDownViewResource(R.layout.spinner_item);
		 snacksSpinner_1_1.setAdapter(snacksAdapter_1_1);
		 snacksSpinners.add(snacksSpinner_1_1);
		 
		 snacksSpinner_1_2 = (Spinner) findViewById(R.id.snacks_spinner_1_2);
		 snacksAdapter_1_2 = ArrayAdapter.createFromResource(this,R.array.servings_array, android.R.layout.simple_spinner_item);
		 snacksAdapter_1_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 snacksSpinner_1_2.setAdapter(snacksAdapter_1_2);
		 snacksSpinners.add(snacksSpinner_1_2);

		 //Assigning the Id
		 snacksId1 = R.id.snacks_spinner_1_1;
		 
		//Spinner Listener
		 snacksSpinner_1_2.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	if (dinnerRelativeLayout.getVisibility() == View.GONE) {
	            		dinnerRelativeLayout.setVisibility(View.VISIBLE);
	            	} 
	                return false;
	                
	            }
	        });
		 
			//snacks Plus Button
			snacksPlus.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	//First Spinner
	 		    	snacksSpinner_1_1 = new Spinner(context);
	 		    	id = "1"+Integer.toString(snacksSpinners.size());
	 		    	snacksSpinner_1_1.setId(Integer.parseInt(id));
	 		    	snacksAdapter_1_1 = ArrayAdapter.createFromResource(DietaryRecallActivity.this,R.array.snacks_array, android.R.layout.simple_spinner_item);
	 		    	snacksAdapter_1_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 		    	snacksSpinner_1_1.setAdapter(snacksAdapter_1_1);
	 		    	snacksSpinners.add(snacksSpinner_1_1);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,snacksId1);
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.snacks_relative_layout);
	 		    	relativeLayout.addView(snacksSpinner_1_1,layoutParams);
	 		    	
	 		    	//Second Spinner
	 		    	snacksSpinner_1_2 = new Spinner(context);
	 		    	id2 = "1"+Integer.toString(snacksSpinners.size());
	 		    	snacksSpinner_1_2.setId(Integer.parseInt(id2));
	 		    	snacksSpinner_1_2.setAdapter(snacksAdapter_1_2);
	 		    	snacksSpinners.add(snacksSpinner_1_2);
//	 		    	snacksSpinner_1_2.setPadding(0, 0, 0, 0);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,snacksId1);
	 		    	layoutParams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(id));
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.snacks_relative_layout);
	 		    	relativeLayout.addView(snacksSpinner_1_2,layoutParams);
	 		    	snacksId1 = Integer.parseInt(id);
	 		    	
	 		    	
	 		    }
	 		});
		 
	}
	
	private void dinner(){
		//Timings Spinner
		dinnerTimingSpinner = (Spinner) findViewById(R.id.dinner_timing_spinner);
		dinnerTimingsAdapter = ArrayAdapter.createFromResource(this,R.array.dinner_timings_array, android.R.layout.simple_spinner_item);
		dinnerTimingsAdapter.setDropDownViewResource(R.layout.spinner_item);
		dinnerTimingSpinner.setAdapter(dinnerTimingsAdapter);
		
		//ImageViews
		dinnerPlus = (ImageView) findViewById(R.id.dinner_plus);
		
		//Spinner
		//Breakfast section
		dinnerSpinner_1_1 = (Spinner) findViewById(R.id.dinner_spinner_1_1);
		 dinnerAdapter_1_1 = ArrayAdapter.createFromResource(this,R.array.main_course_array, android.R.layout.simple_spinner_item);
		 dinnerAdapter_1_1.setDropDownViewResource(R.layout.spinner_item);
		 dinnerSpinner_1_1.setAdapter(dinnerAdapter_1_1);
		 dinnerSpinners.add(dinnerSpinner_1_1);
		 
		 dinnerSpinner_1_2 = (Spinner) findViewById(R.id.dinner_spinner_1_2);
		 dinnerAdapter_1_2 = ArrayAdapter.createFromResource(this,R.array.servings_array, android.R.layout.simple_spinner_item);
		 dinnerAdapter_1_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 dinnerSpinner_1_2.setAdapter(dinnerAdapter_1_2);
		 dinnerSpinners.add(dinnerSpinner_1_2);

		 //Assigning the Id
		 dinnerId1 = R.id.dinner_spinner_1_1;
		 
		//Spinner Listener
		 dinnerSpinner_1_2.setOnTouchListener(new View.OnTouchListener() {
	            @Override
	            public boolean onTouch(View v, MotionEvent event) {
	            	if (bedtimeRelativeLayout.getVisibility() == View.GONE) {
	            		bedtimeRelativeLayout.setVisibility(View.VISIBLE);
	            	} 
	                return false;
	                
	            }
	        });
		 
			//dinner Plus Button
			dinnerPlus.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	//First Spinner
	 		    	dinnerSpinner_1_1 = new Spinner(context);
	 		    	id = "1"+Integer.toString(dinnerSpinners.size());
	 		    	dinnerSpinner_1_1.setId(Integer.parseInt(id));
	 		    	dinnerAdapter_1_1 = ArrayAdapter.createFromResource(DietaryRecallActivity.this,R.array.main_course_array, android.R.layout.simple_spinner_item);
	 		    	dinnerAdapter_1_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 		    	dinnerSpinner_1_1.setAdapter(dinnerAdapter_1_1);
	 		    	dinnerSpinners.add(dinnerSpinner_1_1);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,dinnerId1);
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.dinner_relative_layout);
	 		    	relativeLayout.addView(dinnerSpinner_1_1,layoutParams);
	 		    	
	 		    	//Second Spinner
	 		    	dinnerSpinner_1_2 = new Spinner(context);
	 		    	id2 = "1"+Integer.toString(dinnerSpinners.size());
	 		    	dinnerSpinner_1_2.setId(Integer.parseInt(id2));
	 		    	dinnerSpinner_1_2.setAdapter(dinnerAdapter_1_2);
	 		    	dinnerSpinners.add(dinnerSpinner_1_2);
//	 		    	dinnerSpinner_1_2.setPadding(0, 0, 0, 0);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,dinnerId1);
	 		    	layoutParams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(id));
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.dinner_relative_layout);
	 		    	relativeLayout.addView(dinnerSpinner_1_2,layoutParams);
	 		    	dinnerId1 = Integer.parseInt(id);
	 		    	
	 		    	
	 		    }
	 		});
		 
		 
	}
	
	private void bedtime(){
		//Timings Spinner
		bedtimeTimingSpinner = (Spinner) findViewById(R.id.bedtime_timing_spinner);
		bedtimeTimingsAdapter = ArrayAdapter.createFromResource(this,R.array.bedtime_timings_array, android.R.layout.simple_spinner_item);
		bedtimeTimingsAdapter.setDropDownViewResource(R.layout.spinner_item);
		bedtimeTimingSpinner.setAdapter(bedtimeTimingsAdapter);
		
		//ImageViews
		bedtimePlus = (ImageView) findViewById(R.id.bedtime_plus);
		
		//Spinner
		//Breakfast section
		bedtimeSpinner_1_1 = (Spinner) findViewById(R.id.bedtime_spinner_1_1);
		 bedtimeAdapter_1_1 = ArrayAdapter.createFromResource(this,R.array.bedtime_array, android.R.layout.simple_spinner_item);
		 bedtimeAdapter_1_1.setDropDownViewResource(R.layout.spinner_item);
		 bedtimeSpinner_1_1.setAdapter(bedtimeAdapter_1_1);
		 bedtimeSpinners.add(bedtimeSpinner_1_1);
		 
		 bedtimeSpinner_1_2 = (Spinner) findViewById(R.id.bedtime_spinner_1_2);
		 bedtimeAdapter_1_2 = ArrayAdapter.createFromResource(this,R.array.servings_array, android.R.layout.simple_spinner_item);
		 bedtimeAdapter_1_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 bedtimeSpinner_1_2.setAdapter(bedtimeAdapter_1_2);
		 bedtimeSpinners.add(bedtimeSpinner_1_2);

		 //Assigning the Id
		 bedtimeId1 = R.id.bedtime_spinner_1_1;
		 
			//bedtime Plus Button
			bedtimePlus.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	//First Spinner
	 		    	bedtimeSpinner_1_1 = new Spinner(context);
	 		    	id = "1"+Integer.toString(bedtimeSpinners.size());
	 		    	bedtimeSpinner_1_1.setId(Integer.parseInt(id));
	 		    	bedtimeAdapter_1_1 = ArrayAdapter.createFromResource(DietaryRecallActivity.this,R.array.bedtime_array, android.R.layout.simple_spinner_item);
	 		    	bedtimeAdapter_1_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 		    	bedtimeSpinner_1_1.setAdapter(bedtimeAdapter_1_1);
	 		    	bedtimeSpinners.add(bedtimeSpinner_1_1);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,bedtimeId1);
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.bedtime_relative_layout);
	 		    	relativeLayout.addView(bedtimeSpinner_1_1,layoutParams);
	 		    	
	 		    	//Second Spinner
	 		    	bedtimeSpinner_1_2 = new Spinner(context);
	 		    	id2 = "1"+Integer.toString(bedtimeSpinners.size());
	 		    	bedtimeSpinner_1_2.setId(Integer.parseInt(id2));
	 		    	bedtimeSpinner_1_2.setAdapter(bedtimeAdapter_1_2);
	 		    	bedtimeSpinners.add(bedtimeSpinner_1_2);
//	 		    	bedtimeSpinner_1_2.setPadding(0, 0, 0, 0);
	 		    	//Adding it to the layout
	 		    	layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	 		    	layoutParams.addRule(RelativeLayout.BELOW,bedtimeId1);
	 		    	layoutParams.addRule(RelativeLayout.RIGHT_OF, Integer.parseInt(id));
	 		    	relativeLayout= (RelativeLayout) findViewById (R.id.bedtime_relative_layout);
	 		    	relativeLayout.addView(bedtimeSpinner_1_2,layoutParams);
	 		    	bedtimeId1 = Integer.parseInt(id);
	 		    	
	 		    	
	 		    }
	 		});
		 
		 
	}
	

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.dietary_recall, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
		    case R.id.action_dietary_recall:
		    	if(!checkMeal()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else if(!checkBreakfast()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else if(!checkLunch()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else if(!checkSnacks()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else if(!checkDinner()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else if(!checkBedtime()){
		    		Toast toast = Toast.makeText(context, toastMessage, Toast.LENGTH_LONG);
					toast.show();
		    	}else{
			    	//Set the Goal - Algorithm - PENDING
		    		calculateStats();
		    		alertMessage();
		    	}
				 return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void alertMessage(){
		int percentage;
		alertMessage = "<html><title></title>" +
				"<body><b><font size='3'>Your Diet has </font></b>" +
				"<br />"+
				"<ul>";
		double totalCals = (fat*9) + (carbs*4) + (protein*4);
		
		//For Carbs
		percentage = (int)(carbs*4*100/totalCals);
		if (percentage > 60){
			alertMessage += "<li><font size ='2'> High Intake of Carbohydrates \n</font></li>";
		}else if (percentage < 60){
			alertMessage += "<li><font size ='2'> Low Intake of Carbohydrates \n</font></li>";
		}else{
			alertMessage += "<li><font size ='2'> Normal Intake of Carbohydrates \n</font></li>";
		}
		
		//For Proteins
		percentage = (int)(protein*4*100/totalCals);
		if (percentage > 30){
			alertMessage += "<li><font size ='2'> High Intake of Proteins \n</font></li>";
		}else if (percentage < 25){
			alertMessage += "<li><font size ='2'> Low Intake of Proteins \n</font></li>";
		}else{
			alertMessage += "<li><font size ='2'> Normal Intake of Proteins \n</font></li>";
		}
		
		//For Fat
		percentage = (int)(carbs*9*100/totalCals);
		if (percentage > 15){
			alertMessage += "<li><font size ='2'> High Intake of Fat \n</font></li>";
		}else if (percentage < 10){
			alertMessage += "<li><font size ='2'> Low Intake of Fat \n</font></li>";
		}else{
			alertMessage += "<li><font size ='2'> Normal Intake of Fat \n</font></li>";
		}
		
		//Fibre is left
		percentage = (int)(14*totalCals/1000);
		if (percentage < fibre){
			alertMessage += "<li><font size ='2'> Good Intake of Fibre \n</font></li>";
		}else if (percentage > fibre){
			alertMessage += "<li><font size ='2'> Poor Intake of Fibre \n</font></li>";
		}else{
			alertMessage += "<li><font size ='2'> Normal Intake of Fibre \n</font></li>";
		}
		
		alertMessage +="</ul>";
		
		//For Timings
		if(calculateTimings()){ //timing is less than or greater than 3 hours
			alertMessage +="<br />";
			alertMessage += "<i><font size='2'><center>Do not keep a gap of more than 3-4 hours between meals as it could affect your metabolism rate.</center></font></i><br />";
		}else{
			alertMessage +="<br />";
			alertMessage +="<font size='2'><center>Great! You're space between meals is right.</center></font><br />";
			alertMessage +="<br />";
			alertMessage += "<i><font size='2'><center>Tip:Do not keep a gap of more than 3-4 hours between meals as it could affect your metabolism rate.</center></font></i><br />";
		}
		alertMessage+="</body></html>";
		Bundle args = new Bundle();
        args.putString("message", alertMessage);
        DialogFragment dietNotice = new DietNoticeFragment();
        dietNotice.setArguments(args);
        dietNotice.show(getFragmentManager(),"NoticeDialogFragment");
	}
	
	
	private boolean checkMeal(){
		if(ggPrefs.getString(LaunchAppActivity.VEG_NONVEG, "Undefined").equalsIgnoreCase("Undefined") || !mealSelected){
			toastMessage = "Are you Veg or Non Veg ?";
			return false;
		}else{
			return true;
		}
	}
	
	private boolean checkBreakfast(){
		if(breakfastRelativeLayout.getVisibility() == View.VISIBLE){
			if(breakfastSpinner_1_1.getSelectedItem().toString().equalsIgnoreCase("Select item")){
				((TextView)breakfastSpinner_1_1.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Breakfast";
				return false;
			}else if(breakfastSpinner_1_2.getSelectedItem().toString().equalsIgnoreCase("Qty")){
				((TextView)breakfastSpinner_1_2.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Breakfast";
				return false;
			}else if(breakfastTimingSpinner.getSelectedItem().toString().equalsIgnoreCase("Timing")){
				((TextView)breakfastTimingSpinner.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "What Time Do You Eat Your Breakfast";
				return false;
			}
			((TextView)breakfastSpinner_1_1.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)breakfastSpinner_1_2.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)breakfastTimingSpinner.getChildAt(0)).setTextColor(Color.BLACK);
			return true;
		}else{
			return true;
		}
	}
	
	private boolean checkLunch(){
		if(lunchRelativeLayout.getVisibility() == View.VISIBLE){
			if(lunchSpinner_1_1.getSelectedItem().toString().equalsIgnoreCase("Select item")){
				((TextView)lunchSpinner_1_1.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Lunch";
				return false;
			}else if(lunchSpinner_1_2.getSelectedItem().toString().equalsIgnoreCase("Qty")){
				((TextView)lunchSpinner_1_2.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Lunch";
				return false;
			}else if(lunchTimingSpinner.getSelectedItem().toString().equalsIgnoreCase("Timing")){
				((TextView)lunchTimingSpinner.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "What Time Do You Eat Your Lunch";
				return false;
			}
			((TextView)lunchSpinner_1_1.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)lunchSpinner_1_2.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)lunchTimingSpinner.getChildAt(0)).setTextColor(Color.BLACK);
			return true;
		}else{
			return true;
		}
	}
	
	private boolean checkSnacks(){
		if(snacksRelativeLayout.getVisibility() == View.VISIBLE){
			if(snacksSpinner_1_1.getSelectedItem().toString().equalsIgnoreCase("Select item")){
				((TextView)snacksSpinner_1_1.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Snacks";
				return false;
			}else if(snacksSpinner_1_2.getSelectedItem().toString().equalsIgnoreCase("Qty")){
				((TextView)snacksSpinner_1_2.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Snacks";
				return false;
			}else if(snacksTimingSpinner.getSelectedItem().toString().equalsIgnoreCase("Timing")){
				((TextView)snacksTimingSpinner.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "What Time Do You Eat Your Snacks";
				return false;
			}
			((TextView)snacksSpinner_1_1.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)snacksSpinner_1_2.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)snacksTimingSpinner.getChildAt(0)).setTextColor(Color.BLACK);
			return true;
		}else{
			return true;
		}
	}
	
	private boolean checkDinner(){
		if(dinnerRelativeLayout.getVisibility() == View.VISIBLE){
			if(dinnerSpinner_1_1.getSelectedItem().toString().equalsIgnoreCase("Select item")){
				((TextView)dinnerSpinner_1_1.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Dinner";
				return false;
			}else if(dinnerSpinner_1_2.getSelectedItem().toString().equalsIgnoreCase("Qty")){
				((TextView)dinnerSpinner_1_2.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Dinner";
				return false;
			}else if(dinnerTimingSpinner.getSelectedItem().toString().equalsIgnoreCase("Timing")){
				((TextView)dinnerTimingSpinner.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "What Time Do You Eat Your Dinner";
				return false;
			}
			((TextView)dinnerSpinner_1_1.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)dinnerSpinner_1_2.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)dinnerTimingSpinner.getChildAt(0)).setTextColor(Color.BLACK);
			return true;
		}else{
			return true;
		}
	}

	private boolean checkBedtime(){
		if(bedtimeRelativeLayout.getVisibility() == View.VISIBLE){
			if(bedtimeSpinner_1_1.getSelectedItem().toString().equalsIgnoreCase("Select item")){
				((TextView)bedtimeSpinner_1_1.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Bedtime";
				return false;
			}else if(bedtimeSpinner_1_2.getSelectedItem().toString().equalsIgnoreCase("Qty")){
				((TextView)bedtimeSpinner_1_2.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "Looks Like You Forgot To Fill in Your Bedtime";
				return false;
			}else if(bedtimeTimingSpinner.getSelectedItem().toString().equalsIgnoreCase("Timing")){
				((TextView)bedtimeTimingSpinner.getChildAt(0)).setTextColor(Color.RED);
				toastMessage = "What Time Do You Eat Your Bedtime";
				return false;
			}
			((TextView)bedtimeSpinner_1_1.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)bedtimeSpinner_1_2.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView)bedtimeTimingSpinner.getChildAt(0)).setTextColor(Color.BLACK);
			return true;
		}else{
			return true;
		}
	}

	
	private boolean calculateTimings(){
		int breakfastTiming , lunchTiming , snacksTiming, dinnerTiming , bedtimeTiming;
		/*if(breakfastRelativeLayout.getVisibility() == View.VISIBLE)
			breakfastTiming  = Integer.parseInt(breakfastTimingSpinner.getSelectedItem().toString().substring(0, breakfastTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(breakfastTimingSpinner.getSelectedItem().toString().substring(breakfastTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
		if(lunchRelativeLayout.getVisibility() == View.VISIBLE)
			lunchTiming = Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(0, lunchTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(lunchTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
		if(snacksRelativeLayout.getVisibility() == View.VISIBLE)
			snacksTiming = Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(0, snacksTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(snacksTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
		if(dinnerRelativeLayout.getVisibility() == View.VISIBLE)
			dinnerTiming = Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(0, dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
		if(bedtimeRelativeLayout.getVisibility() == View.VISIBLE)
			bedtimeTiming = Integer.parseInt(bedtimeTimingSpinner.getSelectedItem().toString().substring(0, bedtimeTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(bedtimeTimingSpinner.getSelectedItem().toString().substring(bedtimeTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
		if(viewCount < 2)
			return true;
		//breakfast
		if(breakfastTiming > -1 && lunchTiming > -1 && (lunchTiming - breakfastTiming !=180))
			return true;
		else if(breakfastTiming > -1 && snacksTiming > -1 && (snacksTiming - breakfastTiming !=180))
			return true;
		else if (breakfastTiming > - 1 && dinnerTiming)
		*/
		
		if(viewCount!=5)
			return true;
		
		if(breakfastRelativeLayout.getVisibility() == View.VISIBLE && lunchRelativeLayout.getVisibility() == View.VISIBLE){
			breakfastTiming = Integer.parseInt(breakfastTimingSpinner.getSelectedItem().toString().substring(0, breakfastTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(breakfastTimingSpinner.getSelectedItem().toString().substring(breakfastTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			lunchTiming = Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(0, lunchTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(lunchTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			if(lunchTiming - breakfastTiming != 180){
				return true;
			}
		}
		if(snacksRelativeLayout.getVisibility() == View.VISIBLE && lunchRelativeLayout.getVisibility() == View.VISIBLE){
			snacksTiming = Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(0, snacksTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(snacksTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			lunchTiming = Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(0, lunchTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(lunchTimingSpinner.getSelectedItem().toString().substring(lunchTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			if(snacksTiming - lunchTiming != 180){
				return true;
			}
		}

		if(snacksRelativeLayout.getVisibility() == View.VISIBLE && dinnerRelativeLayout.getVisibility() == View.VISIBLE){
			snacksTiming = Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(0, snacksTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(snacksTimingSpinner.getSelectedItem().toString().substring(snacksTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			dinnerTiming = Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(0, dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			if(dinnerTiming == 30)
				dinnerTiming = 24*60 + 30;
			else if (dinnerTiming == 60)
				dinnerTiming = 24*60 + 60;
			else if (dinnerTiming == 90)
				dinnerTiming = 24*60 + 90;
			else if (dinnerTiming == 180)
				dinnerTiming = 24*60 + 180;
			if(dinnerTiming - snacksTiming != 180){
				return true;
			}
		}
		if(bedtimeRelativeLayout.getVisibility() == View.VISIBLE && dinnerRelativeLayout.getVisibility() == View.VISIBLE){
			bedtimeTiming = Integer.parseInt(bedtimeTimingSpinner.getSelectedItem().toString().substring(0, bedtimeTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(bedtimeTimingSpinner.getSelectedItem().toString().substring(bedtimeTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			dinnerTiming = Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(0, dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')))*60 + Integer.parseInt(dinnerTimingSpinner.getSelectedItem().toString().substring(dinnerTimingSpinner.getSelectedItem().toString().indexOf('.')+1));
			if(dinnerTiming == 30)
				dinnerTiming = 24*60 + 30;
			else if (dinnerTiming == 60)
				dinnerTiming = 24*60 + 60;
			else if (dinnerTiming == 90)
				dinnerTiming = 24*60 + 90;
			else if (dinnerTiming == 180)
				dinnerTiming = 24*60 + 180;
			if(bedtimeTiming == 30)
				bedtimeTiming = 24*60 + 30;
			else if (bedtimeTiming == 60)
				bedtimeTiming = 24*60 + 60;
			else if (bedtimeTiming == 90)
				bedtimeTiming = 24*60 + 90;
			else if (bedtimeTiming == 180)
				bedtimeTiming = 24*60 + 180;
			if(bedtimeTiming - dinnerTiming != 180){
				return true;
			}
		}
		
		return false; // false means gap is exact 3 hours or some meals are skipped
				
	}
	
	public void onRadioButtonClicked(View view) {
	    // Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.radio_veg:
	            if (checked)
	            	ggPrefs.edit().putString(LaunchAppActivity.VEG_NONVEG,"Veg").commit();
	            break;
	        case R.id.radio_nonveg:
	            if (checked)
	            	ggPrefs.edit().putString(LaunchAppActivity.VEG_NONVEG,"Non Veg").commit();
	            break;
	    }
	}

	private void calculateStats(){
		instantiateMap();
		//get the size of the breakfast array
		int breakfastSize = breakfastSpinners.size();
		//for breakfast items
		for(int i=0; i <breakfastSize; i=i+2){
			breakfast = breakfastSpinners.get(i).getSelectedItem().toString();
			try{
				breakfastServing = Integer.parseInt(breakfastSpinners.get(i+1).getSelectedItem().toString());
			}catch(Exception e){
				breakfastServing=0;
			}
			energy += ((double[])foodMap.get(breakfast))[0]*breakfastServing;
			carbs += ((double[])foodMap.get(breakfast))[1]*breakfastServing;
			protein += ((double[])foodMap.get(breakfast))[2]*breakfastServing;
			fat += ((double[])foodMap.get(breakfast))[3]*breakfastServing;
			fibre += ((double[])foodMap.get(breakfast))[4]*breakfastServing;
		}

		//get the size of the lunch array
		int lunchSize = lunchSpinners.size();
		//for lunch items
		for(int i=0; i <lunchSize; i=i+2){
			lunch = lunchSpinners.get(i).getSelectedItem().toString();
			try{
				lunchServing = Integer.parseInt(lunchSpinners.get(i+1).getSelectedItem().toString());
			}catch(Exception e){
				lunchServing=0;
			}
			energy += ((double[])foodMap.get(lunch))[0]*lunchServing;
			carbs += ((double[])foodMap.get(lunch))[1]*lunchServing;
			protein += ((double[])foodMap.get(lunch))[2]*lunchServing;
			fat += ((double[])foodMap.get(lunch))[3]*lunchServing;
			fibre += ((double[])foodMap.get(lunch))[4]*lunchServing;
		}
		

		//get the size of the snacks array
		int snacksSize = snacksSpinners.size();
		//for snacks items
		for(int i=0; i <snacksSize; i=i+2){
			snacks = snacksSpinners.get(i).getSelectedItem().toString();
			try{
				snacksServing = Integer.parseInt(snacksSpinners.get(i+1).getSelectedItem().toString());
			}catch(Exception e){
				snacksServing=0;
			}
			energy += ((double[])foodMap.get(snacks))[0]*snacksServing;
			carbs += ((double[])foodMap.get(snacks))[1]*snacksServing;
			protein += ((double[])foodMap.get(snacks))[2]*snacksServing;
			fat += ((double[])foodMap.get(snacks))[3]*snacksServing;
			fibre += ((double[])foodMap.get(snacks))[4]*snacksServing;
		}
		

		//get the size of the dinner array
		int dinnerSize = dinnerSpinners.size();
		//for dinner items
		for(int i=0; i <dinnerSize; i=i+2){
			dinner = dinnerSpinners.get(i).getSelectedItem().toString();
			try{
				dinnerServing = Integer.parseInt(dinnerSpinners.get(i+1).getSelectedItem().toString());
			}catch(Exception e){
				dinnerServing=0;
			}
			energy += ((double[])foodMap.get(dinner))[0]*dinnerServing;
			carbs += ((double[])foodMap.get(dinner))[1]*dinnerServing;
			protein += ((double[])foodMap.get(dinner))[2]*dinnerServing;
			fat += ((double[])foodMap.get(dinner))[3]*dinnerServing;
			fibre += ((double[])foodMap.get(dinner))[4]*dinnerServing;
		}
		

		//get the size of the bedtime array
		int bedtimeSize = bedtimeSpinners.size();
		//for bedtime items
		for(int i=0; i <bedtimeSize; i=i+2){
			bedtime = bedtimeSpinners.get(i).getSelectedItem().toString();
			try{
				bedtimeServing = Integer.parseInt(bedtimeSpinners.get(i+1).getSelectedItem().toString());
			}catch(Exception e){
				bedtimeServing=0;
			}
			energy += ((double[])foodMap.get(bedtime))[0]*bedtimeServing;
			carbs += ((double[])foodMap.get(bedtime))[1]*bedtimeServing;
			protein += ((double[])foodMap.get(bedtime))[2]*bedtimeServing;
			fat += ((double[])foodMap.get(bedtime))[3]*bedtimeServing;
			fibre += ((double[])foodMap.get(bedtime))[4]*bedtimeServing;
		}
		
	}
	
	private void instantiateMap(){
		foodMap = new DefaultHashMap<String, double[]>(new double[]{0,0,0,0,0});
		foodMap.put("", new double[] {0,0,0,0,0});
		//Breakfast
		foodMap.put("", new double[]{0,0,0,0,0});
		foodMap.put("Select item", new double[]{0,0,0,0,0});
		foodMap.put("Biscuits Ð Cream", new double[]{130,18,1,5,0});
		foodMap.put("Biscuits Ð Khari", new double[]{161,16.7,2.6,9.6,0.4});
		foodMap.put("Biscuits Ð Salty", new double[]{124,17,2,6,0});
		foodMap.put("Biscuits Ð Sweet", new double[]{316,53,1,10,2});
		foodMap.put("Boiled Egg - W/o Yolk", new double[]{16,0,4,0,0});
		foodMap.put("Boiled Egg - With Yolk", new double[]{87,0,6.65,6.65,0});
		foodMap.put("Chiwda", new double[]{167,10,2.9,12.9,0.5});
		foodMap.put("Corn Flakes", new double[]{112,2.5,2.4,0.3,0.75});
		foodMap.put("Daliya Porridge", new double[]{269,86.1,5.7,8.5,0.5});
		foodMap.put("Daliya Upma (Plain)", new double[]{180,29.16,2.46,5.48,2.31});
		foodMap.put("Dosa", new double[]{80,17,2.3,0.17,1.32});
		foodMap.put("Idli", new double[]{106,8.3,4.4,0.1,0});
		foodMap.put("Kurkure (Tomato)", new double[]{164,16.9,1.8,10,0});
		foodMap.put("Muesli (Dried Nuts)", new double[]{109,23.4,2.4,1.5,2.16});
		foodMap.put("Nankhatai/Shrewsberry", new double[]{251,26,2,16,0});
		foodMap.put("Nuts", new double[]{125,19,2,4,0});
		foodMap.put("Oat Flakes", new double[]{106,20.3,3,2.7,3.33});
		foodMap.put("Omelette (Plain)", new double[]{130,0,6.5,8,0});
		foodMap.put("Puffed Rice", new double[]{162,36.5,3.75,0.05,0.15});
		foodMap.put("Sandwich-Whole Wheat ", new double[]{273,41.2,6.32,8.9,3.3});
		foodMap.put("Scrambled Egg", new double[]{130,0,6.5,8,0});
		foodMap.put("Sev, Ghatiya", new double[]{84,8.4,2.6,4.6,0});
		foodMap.put("Smoothies - Plain", new double[]{169,30.5,4.65,6,0});
		foodMap.put("Smoothies Ð Fruit", new double[]{213,40.5,5.35,6.2,1.1});
		foodMap.put("Upma", new double[]{104,39.4,4.6,5.59,3.5});
		foodMap.put("Vermicelli Upma", new double[]{175,28.4,2.6,5,1.8});
		foodMap.put("Wafers (Potato)", new double[]{160,15,2,10,1});
		foodMap.put("Wheat Flakes", new double[]{106,25,3,0.5,3});
		foodMap.put("White Bread", new double[]{273,42.45,6,8.65,2.8});
		//Snacks
		foodMap.put("Bhajya/Pakoda", new double[]{177,16,4.1,10.2,0.52});
		foodMap.put("Batata Wada", new double[]{229,27.8,4.9,10.2,2.22});
		foodMap.put("Chat Items", new double[]{203,25.4,2.7,10.2,48});
		foodMap.put("Dabeli", new double[]{311,37.7,4.7,0.4,0.86});
		foodMap.put("Franky", new double[]{213,31.6,4.7,7.3,0.25});
		foodMap.put("Popcorn", new double[]{444,91,3.6,35,1.2});
		foodMap.put("Samosa", new double[]{147,20,3.3,1,0.8});
		//Bedtime
		foodMap.put("Double Tonned Milk", new double[]{70.5,7.5,4.5,3,0});
		foodMap.put("Flavoured Milk", new double[]{220,24,7,9,0});
		foodMap.put("Full Cream Milk", new double[]{172,7.5,6.45,13.2,0});
		foodMap.put("Milk", new double[]{158,12,8,8,0});
		foodMap.put("Skimmed Milk", new double[]{44,7.5,3.75,0.15,0});
		foodMap.put("Tonned Milk", new double[]{138,11.2,7.4,7.1,0});
		//Other
		foodMap.put("Cereals Ð Wheat Flour ,Whole", new double[]{100,21,3,0.5,4});
		foodMap.put("Carrot Halwa", new double[]{200,53,7,10,4});
		foodMap.put("Cereals Ð Bajra", new double[]{100,20,3.5,1.5,3.8});
		foodMap.put("Cereals Ð Rice,Raw,Milled", new double[]{100,21,2,0.5,1.3});
		foodMap.put("Cheese", new double[]{100,2,7,8,0});
		foodMap.put("Chicken", new double[]{100,0,26,0,0});
		foodMap.put("Chikki", new double[]{142,20,6,4,4});
		foodMap.put("Coconut (Dry)", new double[]{100,2.5,1,10,0});
		foodMap.put("Coconut (Fresh)", new double[]{100,3,1,10,4.4});
		foodMap.put("Egg (Whole)", new double[]{85,0,6.5,6.5,0});
		foodMap.put("Eggwhites", new double[]{15,0,3,0,0});
		foodMap.put("Fish", new double[]{100,0,20,0,0});
		foodMap.put("Fruit Custard", new double[]{250,55,7,6,1.7});
		foodMap.put("Fruits", new double[]{50,10,0,0,3});
		foodMap.put("Kheer", new double[]{193,23,5.5,6,0});
		foodMap.put("Lassi", new double[]{145,11,2.5,6,0});
		foodMap.put("Milk (Buffalo)", new double[]{117,5,4.3,6.5,0});
		foodMap.put("Milk (Cow)", new double[]{70,4.4,3.2,4.1,0});
		foodMap.put("Mutton", new double[]{200,0,18.5,13.3,0});
		foodMap.put("Nuts", new double[]{100,8.5,3.5,6,3});
		foodMap.put("Oil /Ghee", new double[]{45,0,0,5,0});
		foodMap.put("Paneer", new double[]{100,2,7,8,0});
		foodMap.put("Pudding", new double[]{266,30,14,9,0});
		foodMap.put("Pulses Ð Bengal Gram", new double[]{100,17,6.3,1.7,5.1});
		foodMap.put("Pulses - Dhal(Chana Dal)", new double[]{100,17,6.3,1.7,5.1});
		foodMap.put("Pulses Ð Green Gram", new double[]{100,17,7,0.5,2.7});
		foodMap.put("Pulses - Whole Moong", new double[]{100,17,7,0.5,2.7});
		foodMap.put("Pulses Ð Lentils(Masoor)", new double[]{100,17,7.5,0.25,3.2});
		foodMap.put("Pulses Ð Red Gram (Tur Dal)", new double[]{100,17,7,0.5,3});
		foodMap.put("Pulses Ð Soyabean", new double[]{90,4,9,4,7.6});
		foodMap.put("Roots And Tubers", new double[]{100,23,1.5,0,1});
		foodMap.put("Sago", new double[]{100,26,0,0,0});
		foodMap.put("Sheera", new double[]{230,47,3.3,11,2.5});
		foodMap.put("Soft Drinks", new double[]{140,36,0.26,0.07,0});
		foodMap.put("Sugar", new double[]{20,5,0,0,0});
		//Main Course
		foodMap.put("Aloo Parathas", new double[]{172,26.6,3.9,0.4,0.46});
		foodMap.put("Bhakari", new double[]{352,47.6,10.2,15.4,6});
		foodMap.put("Biryani", new double[]{693,84.4,21.5,30,0});
		foodMap.put("Buttermilk", new double[]{18,0.9,0.9,1.2,0});
		foodMap.put("Chapati", new double[]{120,21,3.5,2.4,0.36});
		foodMap.put("Cheese Parathas", new double[]{247,22,10.7,7.9,0.36});
		foodMap.put("Chicken Ð Coconut Gravy", new double[]{270,5.9,27,16.06,0.8});
		foodMap.put("Chicken Ð Curry", new double[]{214,3.5,26.4,10.66,0.8});
		foodMap.put("Chicken Ð Fried", new double[]{382,12,33.35,22.25,0.08});
		foodMap.put("Chicken Ð Roasted", new double[]{154,0,25.9,5.6,0});
		foodMap.put("Chicken Fried Rice", new double[]{332,58.5,26.6,10.6,0.5});
		foodMap.put("Chicken Vegetable Biryani", new double[]{449,63,22,23.5,0.5});
		foodMap.put("Curd Rice", new double[]{205,27,5,9,0});
		foodMap.put("Dal Dhokali", new double[]{600,100,30,15,6});
		foodMap.put("Dal/ Sambar", new double[]{138,20.5,6,5.4,0.6});
		foodMap.put("Daliya Khichadi", new double[]{286,36,6,15,8});
		foodMap.put("Egg Bhurji", new double[]{195,15,9,12,2});
		foodMap.put("Egg Curry", new double[]{390,17,16,30,0});
		foodMap.put("Egg Fried Rice", new double[]{317,58.5,9.3,16.7,0.5});
		foodMap.put("Egg Pulao", new double[]{317,58.5,9.3,17.2,0.5});
		foodMap.put("Fish Ð Coconut Gravy", new double[]{282,8.7,20.1,18.46,0.8});
		foodMap.put("Fish Ð Curry", new double[]{225,6.3,19.5,13.06,0.8});
		foodMap.put("Fish Ð Fried", new double[]{290,10.2,20.04,18.08,1.2});
		foodMap.put("Fish Ð Roasted", new double[]{200,10.2,20.04,8.08,1.2});
		foodMap.put("Fruit Raita", new double[]{122.5,12,4.5,6,0.12});
		foodMap.put("Kadhi", new double[]{160,15.7,5.7,8.2,0.12});
		foodMap.put("Khichadi", new double[]{232,55,5.1,10.2,0.62});
		foodMap.put("Leafy Vegetable", new double[]{137,18.15,3.88,5.2,1.43});
		foodMap.put("Masala Rice", new double[]{362,61.2,10.1,9.5,4.1});
		foodMap.put("Moong Dal Chilla", new double[]{212,40,16,20,12});
		foodMap.put("Mutton Vegetable Biryani", new double[]{513,63,17.9,33,0.5});
		foodMap.put("Paneer Bhurji", new double[]{423,21,21,30,4});
		foodMap.put("Paneer Parathas", new double[]{247,22,10.7,7.9,0.36});
		foodMap.put("Paneer Pulao", new double[]{332,60.4,9.8,18,0.5});
		foodMap.put("Phodni Bhaat", new double[]{350,44,6,15,0});
		foodMap.put("Phulka", new double[]{69,11.5,1.7,2.2,18});
		foodMap.put("Plain Paratha", new double[]{147,21,3.5,0.4,0.36});
		foodMap.put("Plain Rice", new double[]{115,28,2.3,0,0});
		foodMap.put("Prawns Pulao", new double[]{258,58.7,8.2,10.7,0.5});
		foodMap.put("Pulao", new double[]{293,44,3.5,10.8,3});
		foodMap.put("Pulses", new double[]{233,24.65,7.78,12,1.71});
		foodMap.put("Red Pumpkin Puri", new double[]{321,50.46,8,10.17,5});
		foodMap.put("Sambhar", new double[]{303,39.4,12.1,12.1,3});
		foodMap.put("Sprouts Raita", new double[]{160,18,8,6.2,0.12});
		foodMap.put("Thalipeeth", new double[]{135,23,4.5,5,2.7});
		foodMap.put("Thepla", new double[]{149,21,3.5,0.4,0.46});
		foodMap.put("Usal/ Chudani", new double[]{416,58,9,12,11});
		foodMap.put("Vegetable Biryani", new double[]{367,63,3.9,23,0.5});
		foodMap.put("Vegetable Fried Rice", new double[]{232,58.5,2.6,10,0.5});
		foodMap.put("Vegetable Parathas", new double[]{159,23,3.7,0.4,0.51});
		foodMap.put("Vegetable Pulao", new double[]{232,58.5,2.6,10.5,0.5});
		foodMap.put("Vegetable Raita", new double[]{122,12.25,4.7,6,0.15});
		foodMap.put("Vegetable", new double[]{162,22.15,3.88,5.2,1.03});
		
	}
}
