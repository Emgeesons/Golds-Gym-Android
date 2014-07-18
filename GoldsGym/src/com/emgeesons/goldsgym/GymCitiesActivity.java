package com.emgeesons.goldsgym;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class GymCitiesActivity extends Activity {
	
	
	 ArrayList<String> gymCities;
	 ArrayAdapter<String> citiesAdapter;
	 ListView citiesList;
	 CitiesAdapter citiesAdapter2;
	 Context context;
	 ProgressDialog pd;
	 Intent nextScreenIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gym_cities);
		
		//Back Button
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	    
	    context = this;
	    gymCities = new ArrayList<String>();
	    citiesList = (ListView) findViewById(R.id.gym_cities_list);
	    getCities();  
	   
     // to handle click event on listView item
        citiesList.setOnItemClickListener(new OnItemClickListener()
        {
                public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
                {
                    TextView citiesText=(TextView)v.findViewById(R.id.cities_text);
                    String city=citiesText.getText().toString();
                    nextScreenIntent = new Intent(GymCitiesActivity.this,GymsLocationActivity.class);
                    nextScreenIntent.putExtra("city", city);
       		     	startActivity(nextScreenIntent);
                   
                }
            });
	}
	
	
	private void initList(String[] cities){
		for(int i=1; i<cities.length;i++){
			gymCities.add(cities[i]);
		}
		citiesAdapter2 = new CitiesAdapter(this,gymCities);
		citiesList.setAdapter(citiesAdapter2); 
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
	
	public class CitiesAdapter  extends BaseAdapter
	{
	    
	    private Context mContext;
	    ArrayList<String> mList;
	    public CitiesAdapter(Context context,ArrayList<String> list) 
	    {
	            super();
	            mContext=context;
	            mList = list;
	           
	    }
	       
	    public int getCount() 
	    {
	        // return the number of records in cursor
	        return mList.size();
	    }

	    // getView method is called for each item of ListView
	    public View getView(int position,  View view, ViewGroup parent) {
	        // inflate the layout for each item of listView
	        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        view = inflater.inflate(R.layout.cities_list_item, null);
	                    
	        // fetch the sender number and sms body from cursor
	        String city = mList.get(position);
	                   
	        // get the reference of textViews
	        TextView citiesText=(TextView)view.findViewById(R.id.cities_text);
	        // Set the Sender number and smsBody to respective TextViews 
	        citiesText.setText(city);
	        if(position%2==1)
	        	citiesText.setBackgroundResource(R.color.light_gray);
	        
	        return view;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }
	}
	
	private void getCities(){
		final RequestQueue queue = Volley.newRequestQueue(this);
		String url = "http://goldsgym.emgeesonsdevelopment.in/mobile1.0/getGymCities.php";
		pd = ProgressDialog.show(this, "", "Loading...",true);
	    pd.setCancelable(true);
	    pd.setOnDismissListener(new OnDismissListener(){

			@Override
			public void onDismiss(DialogInterface dialog) {
				queue.cancelAll("cities");
				
			}
	    	
	    });
	    
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				String list= response.toString().replace("{", "");
				list = list.replace("}", "");
				list = list.replace("\"", "");
				list = list.replace(":", "");
				
				
				String[] cities = list.split(",");
				initList(cities);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
				Toast toast = Toast.makeText(context, "Something Went Wrong. Try Again Later", Toast.LENGTH_SHORT);
				toast.show();
			}
		});

		queue.add(jsObjRequest).setTag("cities");
	}//end of getCities
	

}//end of class


