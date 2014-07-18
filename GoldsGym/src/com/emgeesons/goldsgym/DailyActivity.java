package com.emgeesons.goldsgym;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DailyActivity extends Activity {

	Context context;
	SharedPreferences ggPrefs;
	TextView titleText, noteText, titleText2, noteText2;
	TextView exerciseText, exerciseText2;
	RelativeLayout dailyLayout;
	ImageView  infoImage;
	View horizontalLine;
	String heading, title, passedExercise;
	String[] description, description2;
	float scale;
	int sizeInDp, linePadding, rightSide;
	RelativeLayout.LayoutParams  textParams, imageParams, dividerParams;
	RelativeLayout dailyLayout2;
	Intent nextScreenIntent;
	int j=0;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		//Shared Prefs
		context = this;
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Bundle Extras
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    heading = b.getString("heading");
	    title = b.getString("title");
	    description=getResources().getStringArray(b.getInt("description"));
	    
	    //Set Heading
	    this.setTitle(" " +heading);
	    
	    //Set Title
	    titleText = (TextView) findViewById(R.id.title);
	    titleText.setText(title);
	    
	    //Set Description
	    noteText=(TextView)findViewById(R.id.note);
	    if(description!=null && description[0].length()>3){
	    	noteText.setText(description[0]);
	    }else{
	    	noteText.setVisibility(View.GONE);
	    }
	    
	    dailyLayout = (RelativeLayout) findViewById(R.id.daily_layout);
	    getExercises();
		
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		dailyLayout2();
	}
	
	private void dailyLayout2(){ //this is used for vacation to show functional training 2
		//Making this very specific. Not generalized
		dailyLayout2 = (RelativeLayout) findViewById(R.id.daily_layout_2);
		//it is vacation mode and there are exercises to be done
		if(LaunchAppActivity.vacation && (heading.equalsIgnoreCase("Monday") || heading.equalsIgnoreCase("Wednesday") || heading.equalsIgnoreCase("Friday"))){
			dailyLayout2.setVisibility(View.VISIBLE);
			titleText2 = (TextView) findViewById(R.id.title_2);
			noteText2 = (TextView) findViewById(R.id.note_2);
			titleText2.setText("Alternate Exercise Program");
			noteText2.setText("Involves using your own Body Weight for Resistance and Dumbbells, Barbells & Kettlebells if available");
			getExercises2();
			
		}else{
			dailyLayout2.setVisibility(View.GONE);
		}
		
	}
	
	@SuppressWarnings("deprecation")
	private void getExercises2(){
		//Setting Padding
		scale = getResources().getDisplayMetrics().density;
		sizeInDp = (int) (20*scale + 0.5f);
		linePadding = (int) (5*scale + 0.5f);
		rightSide=(int)(230*scale + 0.5f);
		description2 =  getResources().getStringArray(R.array.functional2_workout);
		for(int i=1;i<description2.length;i=i+2){
			exerciseText2 = new TextView(this);
			exerciseText2.setId(i*3);
			exerciseText2.setText(description2[i]+" "+description2[i+1]);
			exerciseText2.setTag(description2[i]);
			textParams = new RelativeLayout.LayoutParams(rightSide, RelativeLayout.LayoutParams.WRAP_CONTENT);
			if(i==1)
				textParams.addRule(RelativeLayout.BELOW,R.id.note_2);
			else
				textParams.addRule(RelativeLayout.BELOW,(i-2)*3);
			exerciseText2.setClickable(true);
			exerciseText2.setPadding(0,sizeInDp,0,0);
			dailyLayout2.addView(exerciseText2,textParams);
			
			infoImage = new ImageView(this);
			infoImage.setId(i*30);
			infoImage.setImageResource(R.drawable.about);
			imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			if(i==1)
				imageParams.addRule(RelativeLayout.BELOW, R.id.note_2);
			else
				imageParams.addRule(RelativeLayout.BELOW, (i-2)*3);
			//imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imageParams.addRule(RelativeLayout.RIGHT_OF, exerciseText2.getId());
			infoImage.setPadding(0,sizeInDp,0,0);
			infoImage.setClickable(true);
			dailyLayout2.addView(infoImage,imageParams);
			
			horizontalLine= new View(this);
			dividerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 2);
			dividerParams.addRule(RelativeLayout.BELOW, exerciseText2.getId());
			horizontalLine.setPadding(0, linePadding, 0, 0);
			horizontalLine.setBackgroundColor(getResources().getColor(R.color.gray_daily));
			dailyLayout2.addView(horizontalLine,dividerParams);
			
			
			//IMPLEMENT LISTENERS
			exerciseText2.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	nextScreenIntent = new Intent(DailyActivity.this,ExerciseActivity.class);
			    	nextScreenIntent.putExtra("exercise", description2[v.getId()/3]);
				     startActivity(nextScreenIntent);
			    }
			});
			infoImage.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	nextScreenIntent = new Intent(DailyActivity.this,ExerciseActivity.class);
			    	nextScreenIntent.putExtra("exercise", description2[v.getId()/30]);
				     startActivity(nextScreenIntent);
			    }
			});
			j++;
		}
	}
	

	
	@SuppressWarnings("deprecation")
	private void getExercises(){
		//Setting Padding
		scale = getResources().getDisplayMetrics().density;
		sizeInDp = (int) (20*scale + 0.5f);
		linePadding = (int) (5*scale + 0.5f);
		rightSide=(int)(230*scale + 0.5f);
		for(int i=1;i<description.length;i=i+2){
			exerciseText = new TextView(this);
			exerciseText.setId(i*3);
			exerciseText.setText(description[i]+" "+description[i+1]);
			exerciseText.setTag(description[i]);
			textParams = new RelativeLayout.LayoutParams(rightSide, RelativeLayout.LayoutParams.WRAP_CONTENT);
			if(i==1)
				textParams.addRule(RelativeLayout.BELOW,R.id.note);
			else
				textParams.addRule(RelativeLayout.BELOW,(i-2)*3);
			exerciseText.setClickable(true);
			exerciseText.setPadding(0,sizeInDp,0,0);
			dailyLayout.addView(exerciseText,textParams);
			
			infoImage = new ImageView(this);
			infoImage.setId(i*30);
			infoImage.setImageResource(R.drawable.about);
			imageParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			if(i==1)
				imageParams.addRule(RelativeLayout.BELOW, R.id.note);
			else
				imageParams.addRule(RelativeLayout.BELOW, (i-2)*3);
			//imageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			imageParams.addRule(RelativeLayout.RIGHT_OF, exerciseText.getId());
			infoImage.setPadding(0,sizeInDp,0,0);
			infoImage.setClickable(true);
			dailyLayout.addView(infoImage,imageParams);
			
			horizontalLine= new View(this);
			dividerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, 2);
			dividerParams.addRule(RelativeLayout.BELOW, exerciseText.getId());
			horizontalLine.setPadding(0, linePadding, 0, 0);
			horizontalLine.setBackgroundColor(getResources().getColor(R.color.gray_daily));
			dailyLayout.addView(horizontalLine,dividerParams);
			
			
			//IMPLEMENT LISTENERS
			exerciseText.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	nextScreenIntent = new Intent(DailyActivity.this,ExerciseActivity.class);
			    	nextScreenIntent.putExtra("exercise", description[v.getId()/3]);
				     startActivity(nextScreenIntent);
			    }
			});
			infoImage.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	nextScreenIntent = new Intent(DailyActivity.this,ExerciseActivity.class);
			    	nextScreenIntent.putExtra("exercise", description[v.getId()/30]);
				     startActivity(nextScreenIntent);
			    }
			});
			j++;
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		 // Handle item selection
	    switch (item.getItemId()) 
	    {
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
