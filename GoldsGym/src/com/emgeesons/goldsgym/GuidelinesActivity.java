package com.emgeesons.goldsgym;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class GuidelinesActivity extends Activity {
	
	Context context;
	TextView guidelineText;
	int textId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guidelines);
		
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Getting passed Variables
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    textId = b.getInt("text");
		
		//setting the text
		guidelineText = (TextView) findViewById(R.id.guideline_text);
		guidelineText.setText(textId);
		
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
