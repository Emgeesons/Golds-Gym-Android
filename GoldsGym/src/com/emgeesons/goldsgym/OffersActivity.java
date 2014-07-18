package com.emgeesons.goldsgym;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;



public class OffersActivity extends FragmentActivity {
	
	
	ProgressDialog pd;
	Context context;
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	OffersPageFragment offersPageFragment;
	static RequestQueue queue;
	String type, requestType;
	ImageView coachmarkImage;
	SharedPreferences ggPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offers);
		
		//Getting passed Variables
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    type = b.getString("type");
	    this.setTitle(type);
	    requestType = b.getString("requestType");
	    
		context = OffersActivity.this;
		
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		getOffers();
	}

	private void instantiateVariables(JSONArray jsonProductJSONArray){
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new OffersActivityAdapter(getSupportFragmentManager(), jsonProductJSONArray);
        mPager.setAdapter(mPagerAdapter);
      //  mPager.setPageTransformer(true, new DepthPageTransformer());
        coachmarkImage = (ImageView) findViewById(R.id.coachmark_image);
        ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        coachmarkImage.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	coachmarkImage.setVisibility(View.GONE);
	 		    	ggPrefs.edit().putBoolean(LaunchAppActivity.OFFERS_COACHMARK, true).commit();
	 		    }
        	});
        if(!ggPrefs.getBoolean(LaunchAppActivity.OFFERS_COACHMARK, false))
        	coachmarkImage.setVisibility(View.VISIBLE);
        
        
	}
	
	
	
	 @Override
	    public void onBackPressed() {
	            super.onBackPressed();
	       
	    }
	 
	    /**
	     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
	     * sequence.
	     */
	    private class OffersActivityAdapter extends FragmentStatePagerAdapter {
	    	JSONArray mJSONProductJSONArray;
	    	JSONObject c;
	        public OffersActivityAdapter(FragmentManager fm, JSONArray jsonProductJSONArray) {
	            super(fm);
	            mJSONProductJSONArray = jsonProductJSONArray;
	        }

	        @Override
	        public Fragment getItem(int position) {
	        	try {
					c = mJSONProductJSONArray.getJSONObject(position);
				} catch (JSONException e) {

					Toast toast = Toast.makeText(context, "Something went wrong. Try again later", Toast.LENGTH_SHORT);
					toast.show();
				}
	        	Bundle fragmentBundle = new Bundle();
	        	fragmentBundle.putString("json", c.toString());
	        	
	        	offersPageFragment = new OffersPageFragment();
	        	offersPageFragment.setArguments(fragmentBundle);
	            return offersPageFragment;
	        }

	        @Override
	        public int getCount() {
	            return mJSONProductJSONArray.length();
	        }
	    }
	
	private void getOffers(){
		queue = Volley.newRequestQueue(this);
		String url="http://www.goldsgymindia.com/ws/ws.asmx/"+type;
		pd = ProgressDialog.show(this, "", "Loading...",true);
	    pd.setCancelable(true);
	    pd.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				queue.cancelAll(type);
				
			}
	    	
	    });
	    JsonObjectRequest jar = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				//what should you do with the response
				try {
					JSONObject getString = new JSONObject(response.toString());
					JSONArray jsonProductJSONArray = new JSONArray();
					if(getString.getJSONArray(requestType)!=null){//event
						jsonProductJSONArray = getString .getJSONArray(requestType);
						instantiateVariables(jsonProductJSONArray);
					}
				}catch(Exception e){
					//do something
				}
				
			}
			
		}, new Response.ErrorListener(){

			@Override
			public void onErrorResponse(VolleyError error) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				Toast toast = Toast.makeText(context, "Something went wrong. Try again later", Toast.LENGTH_SHORT);
				toast.show();
			}
			
		}) ;
		queue.add(jar).setTag(type);

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
	
	//Animation
	public class DepthPageTransformer implements PageTransformer {
	    private static final float MIN_SCALE = 0.75f;

	    public void transformPage(View view, float position) {
	        int pageWidth = view.getWidth();

	        if (position < -1) { // [-Infinity,-1)
	            // This page is way off-screen to the left.
	            view.setAlpha(0);

	        } else if (position <= 0) { // [-1,0]
	            // Use the default slide transition when moving to the left page
	            view.setAlpha(1);
	            view.setTranslationX(0);
	            view.setScaleX(1);
	            view.setScaleY(1);

	        } else if (position <= 1) { // (0,1]
	            // Fade the page out.
	            view.setAlpha(1 - position);

	            // Counteract the default slide transition
	            view.setTranslationX(pageWidth * -position);

	            // Scale the page down (between MIN_SCALE and 1)
	            float scaleFactor = MIN_SCALE
	                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);

	        } else { // (1,+Infinity]
	            // This page is way off-screen to the right.
	            view.setAlpha(0);
	        }
	    }
	}
	
}
