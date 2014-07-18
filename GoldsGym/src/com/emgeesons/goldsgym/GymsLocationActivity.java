package com.emgeesons.goldsgym;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class GymsLocationActivity  extends Activity  {
	
	Context context;
	ProgressDialog pd;
	ArrayList<String> gyms;
	ListView gymsList;
	LocationsAdapter locationsAdapter;
	String passedCity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gyms_location);
		context = getApplicationContext();
		//Getting passed Variables
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    passedCity = b.getString("city");
	    this.setTitle(" " +passedCity);
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		gyms = new ArrayList<String>();
		gymsList = (ListView) findViewById(R.id.gym_locations_list);
		getGyms();
	}
	
	private void getGyms(){
		final RequestQueue queue = Volley.newRequestQueue(this);
		String url="http://goldsgym.emgeesonsdevelopment.in/mobile1.0/getGymLocations.php?city="+passedCity;
		pd = ProgressDialog.show(this, "", "Loading...",true);
	    pd.setCancelable(true);
	    pd.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				queue.cancelAll("gyms");
				
			}
	    	
	    });
	    JsonObjectRequest jar = new JsonObjectRequest( Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject response) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				initList(response);
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
		queue.add(jar).setTag("gyms");
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
		    case android.R.id.home:
	            onBackPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void initList(JSONObject response){
		try {
			JSONObject getString = new JSONObject(response.toString());
			JSONArray jsonProductJSONArray = new JSONArray();
			jsonProductJSONArray = getString .getJSONArray("response");
			JSONObject c;
			 // Receive the JSON object from server -  YOU CAN REMOVE THIS
	        for (int i = 0; i < jsonProductJSONArray.length(); i++) {
	            c = jsonProductJSONArray.getJSONObject(i);
	            gyms.add(c.getString("name"));
	        }
	        locationsAdapter = new LocationsAdapter(this,jsonProductJSONArray);
			gymsList.setAdapter(locationsAdapter); 
			
		} catch (JSONException e) {

		}
		
		
	}
	
	public class LocationsAdapter  extends BaseAdapter
	{
	    
	    private Context mContext;
	    JSONArray mJSONProductJSONArray;
	    TextView gymTitle, gymAddress, gymTel, gymTimings, gymEmail;
	    String gymName, gymAdd, gymNo,gymTime, gymMail;
	    Double latitude, longitude;
	    ImageView mapImage;
	    JSONObject c;
	    Intent nextScreenIntent;
	    
	    public LocationsAdapter(Context context,JSONArray jsonProductJSONArray) 
	    {
	            super();
	            mContext=context;
	            mJSONProductJSONArray = jsonProductJSONArray;
	           
	    }
	       
	    public int getCount() 
	    {
	        // return the number of records in cursor
	        return mJSONProductJSONArray.length();
	    }

	    // getView method is called for each item of ListView
	    public View getView(int position,  View view, ViewGroup parent) {
	        // inflate the layout for each item of listView
	        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        view = inflater.inflate(R.layout.gyms_list_item, null);
	        
	        
	     // get the reference of textViews
	         gymTitle=(TextView)view.findViewById(R.id.gym_title);
	         gymAddress = (TextView) view.findViewById(R.id.gym_address);
	         gymTel= (TextView) view.findViewById(R.id.gym_tel);
	         gymTimings= (TextView) view.findViewById(R.id.gym_timings);
	         gymEmail= (TextView) view.findViewById(R.id.gym_email);
	         mapImage = (ImageView) view.findViewById(R.id.map_image);
	         mapImage.setTag(position);
	         
	     
	         
	     //fetch the data from json array 
	         try {
	         c = mJSONProductJSONArray.getJSONObject(position);
	         gymName = c.getString("name");
	         gymAdd = c.getString("address");
	         gymNo = c.getString("telephone");
	         gymMail = c.getString("email");
	         gymTime = c.getString("timings");
			} catch (JSONException e) {
			}
	                   
	        // Set the value in the list 
	        gymTitle.setText(gymName);
	        gymAddress.setText(gymAdd);
	        gymTel.setText(gymNo);
	        gymTimings.setText(gymTime);
	        gymEmail.setText(gymMail);
	        //Linkifying Text
	        Linkify.addLinks(gymEmail, Linkify.ALL);
	        Linkify.addLinks(gymTel, Linkify.ALL);
	        
	        if(position%2==1){
	        	view.setBackgroundResource(R.color.light_gray);
	        	view.setPadding(0, 0, 0, 0);	
	        }
	        
	      //defining action listener for map icon
	         mapImage.setOnClickListener(new View.OnClickListener(){
	 		    public void onClick(View v) {
	 		    	try {
						c = mJSONProductJSONArray.getJSONObject((Integer) v.getTag());
						gymName = c.getString("name");
					    if(c.getString("latitude")!=null && !c.getString("latitude").equalsIgnoreCase("null") && c.getString("latitude").length() >1)
					        latitude= Double.parseDouble(c.getString("latitude"));
					    else
					       latitude= 0.0;
					    if(c.getString("longitude")!=null && !c.getString("longitude").equalsIgnoreCase("null") && c.getString("longitude").length() >1)
					       longitude= Double.parseDouble(c.getString("longitude"));
					    else
					       longitude = 0.0;
						}catch (JSONException e) {
						}
	 		    		if(latitude!=null && longitude!=null && latitude > 0 && longitude > 0 ){
		 		    		//Take them to maps
		 		    		String uriBegin = "geo:" + latitude + "," + longitude;
		 		    		String query = latitude + "," + longitude + "(Gold's Gym - " + gymName + ")";
		 		    		String encodedQuery = Uri.encode(query);
		 		    		String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
		 		    		Uri uri = Uri.parse(uriString);
		 		    		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
		 		    		startActivity(intent);
		 		    	}else{
		 		    		//show a toast alert
		 					Toast toast = Toast.makeText(context, "Directions not available for this Gym", Toast.LENGTH_SHORT);
		 					toast.show();
		 		    	}
	 		    }
	 		});
	        
	        return view;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	}
	

}
