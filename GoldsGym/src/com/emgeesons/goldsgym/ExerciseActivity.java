package com.emgeesons.goldsgym;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ExerciseActivity extends Activity {
	
	ImageView exerciseImage;
	TextView exerciseName, exerciseDescription;
	String title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise);
		

		//Getting passed Variables
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    title = b.getString("exercise");
	    setTitle(" "+title);
		
		//Back Button
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    updateInfo();
	}
	
	private void updateInfo(){
		//Istantiate Variables
		exerciseImage = (ImageView)findViewById(R.id.exercise_image);
		exerciseName = (TextView)findViewById(R.id.exercise_name);
		exerciseDescription = (TextView)findViewById(R.id.exercise_description);
		
		if(Exercises.exercises==null)//saves you memory. otherwise this is a potential memory leak. do this for the others as well
			Exercises.initializeExercises();
		
		exerciseName.setText(title);
		exerciseImage.setImageResource(Exercises.exercises.get(title)[0]);
		exerciseDescription.setText(Exercises.exercises.get(title)[1]);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.exercise, menu);
	    return super.onCreateOptionsMenu(menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		 // Handle item selection
	    switch (item.getItemId()) 
	    {
	    	case R.id.action_exercise:
	    		onBackPressed();
	            return true;
	        case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
