package com.emgeesons.goldsgym;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DadActivity  extends Activity {
	
	Context context;
	SharedPreferences ggPrefs;
	LinearLayout pregnancyLayout, thyroidLayout, pcosLayout, diabetesLayout, cholesterolLayout, bpHypertensionLayout, bpHypotensionLayout, lowerBackLayout, spondylitisLayout,kneePainLayout,wristPainLayout;
	TextView hypertensionDos, hypertensionDonts, pregnancyDos, pregnancyDonts, diabetesDos, diabetesDonts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dad);
		
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//Shared Prefs
		context = this;
		ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		//Get Extra
		//Getting passed Variables
		Bundle b = new Bundle();
		b = getIntent().getExtras();
		if (b.getString("type").equalsIgnoreCase("Nutritionist")){
			//Each Card
			bp();
			cholesterol();
			diabetes();
			pcos();
			thyroid();
			pregnancy();
			removeTrainer();
		}else{
			hypertensionTrainer();
			diabetesTrainer();
			lowerBack();
			spondylitis();
			knee();
			wrist();
			pregnancyTrainer();
			removeNutritionist();
		}
	}
	
	private void hypertensionTrainer(){
		hypertensionDos = (TextView) findViewById(R.id.bp_hypertension_dos_text);
		hypertensionDonts = (TextView) findViewById(R.id.bp_hypertension_donts_text);
		bpHypertensionLayout = (LinearLayout) findViewById(R.id.bp_hypertension_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.HYPERTENSION,false)){
			bpHypertensionLayout.setVisibility(View.GONE);
		}else{
			hypertensionDos.setText(R.string.hypertension_trainer_dos);
			hypertensionDonts.setText(R.string.hypertension_trainer_donts);
		}
		
	}
	
	private void diabetesTrainer(){
		diabetesDos = (TextView) findViewById(R.id.diabetes_dos_text);
		diabetesDonts = (TextView) findViewById(R.id.diabetes_donts_text);
		diabetesLayout = (LinearLayout) findViewById(R.id.diabetes_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.DIABETES,false)){
			diabetesLayout.setVisibility(View.GONE);
		}else{
			diabetesDos.setText(R.string.diabetes_trainer_dos);
			diabetesDonts.setText(R.string.diabetes_trainer_donts);
		}
		
	}

	private void pregnancyTrainer(){
		pregnancyDos = (TextView) findViewById(R.id.pregnancy_dos_text);
		pregnancyDonts = (TextView) findViewById(R.id.pregnancy_donts_text);
		pregnancyLayout = (LinearLayout) findViewById(R.id.pregnancy_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.PREGNANCY,false)){
			pregnancyLayout.setVisibility(View.GONE);
		}else{
			pregnancyDos.setText(R.string.pregnancy_trainer_dos);
			pregnancyDonts.setText(R.string.pregnancy_trainer_donts);
		}
		
	}
	
	private void lowerBack(){
		lowerBackLayout = (LinearLayout) findViewById(R.id.lower_back_pain_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.LOWER_BACK,false)){
			lowerBackLayout.setVisibility(View.GONE);
		}
	}
	
	private void spondylitis(){
		spondylitisLayout = (LinearLayout) findViewById(R.id.spondylitis_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.SPONDILYTIS,false)){
			spondylitisLayout.setVisibility(View.GONE);
		}
	}
	
	private void knee(){
		kneePainLayout = (LinearLayout) findViewById(R.id.knee_pain_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.KNEE,false)){
			kneePainLayout.setVisibility(View.GONE);
		}
	}
	
	private void wrist(){
		wristPainLayout = (LinearLayout) findViewById(R.id.wrist_pain_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.WRIST,false)){
			wristPainLayout.setVisibility(View.GONE);
		}
	}
	
	
	private void bp(){
		bpHypertensionLayout = (LinearLayout) findViewById(R.id.bp_hypertension_layout);
		bpHypotensionLayout = (LinearLayout) findViewById(R.id.bp_hypotension_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.HYPERTENSION,false)){
			bpHypertensionLayout.setVisibility(View.GONE);
			bpHypotensionLayout.setVisibility(View.GONE);
		}
	}

	private void cholesterol(){
		cholesterolLayout = (LinearLayout) findViewById(R.id.cholesterol_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.CHOLESTEROL,false)){
			cholesterolLayout.setVisibility(View.GONE);
		}
	}
	
	private void diabetes(){
		diabetesLayout = (LinearLayout) findViewById(R.id.diabetes_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.DIABETES,false)){
			diabetesLayout.setVisibility(View.GONE);
		}
	}
	
	private void pcos(){
		pcosLayout = (LinearLayout) findViewById(R.id.pcos_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.PCOS,false)){
			pcosLayout.setVisibility(View.GONE);
		}
	}
	
	private void thyroid(){
		thyroidLayout = (LinearLayout) findViewById(R.id.thyroid_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.THYROID,false)){
			thyroidLayout.setVisibility(View.GONE);
		}
	}
	
	private void pregnancy(){
		pregnancyLayout = (LinearLayout) findViewById(R.id.pregnancy_layout);
		if(!ggPrefs.getBoolean(LaunchAppActivity.PREGNANCY,false)){
			pregnancyLayout.setVisibility(View.GONE);
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
	
	private void removeTrainer(){
		lowerBackLayout = (LinearLayout)findViewById(R.id.lower_back_pain_layout);
		kneePainLayout = (LinearLayout)findViewById(R.id.knee_pain_layout);
		wristPainLayout = (LinearLayout)findViewById(R.id.wrist_pain_layout);
		spondylitisLayout = (LinearLayout)findViewById(R.id.spondylitis_layout);
		
		lowerBackLayout.setVisibility(View.GONE);
		kneePainLayout.setVisibility(View.GONE);
		wristPainLayout.setVisibility(View.GONE);
		spondylitisLayout.setVisibility(View.GONE);
		
	}
	
	private void removeNutritionist(){
		thyroidLayout = (LinearLayout)findViewById(R.id.thyroid_layout);
		pcosLayout = (LinearLayout)findViewById(R.id.pcos_layout);
		cholesterolLayout = (LinearLayout)findViewById(R.id.cholesterol_layout);
		bpHypotensionLayout = (LinearLayout)findViewById(R.id.bp_hypotension_layout);
		
		thyroidLayout.setVisibility(View.GONE);
		pcosLayout.setVisibility(View.GONE);
		cholesterolLayout.setVisibility(View.GONE);
		bpHypotensionLayout.setVisibility(View.GONE);
	}
}
