package com.emgeesons.goldsgym;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class DietNoticeFragment extends DialogFragment{

	
	AlertDialog.Builder builder;
	AlertDialog dialog;
	Context context;
	View view;
	ProgressDialog pd;
	SharedPreferences ggPrefs;
	WebView webview;
	String message;
	Intent nextScreenIntent;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the Builder class for convenient dialog construction
	    builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	  //Getting passed Variables
	  	message = getArguments().getString("message");
	  	    
	   // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    view = inflater.inflate(R.layout.dialog_diet_notice, null);
	    builder.setView(view);
	    builder.setTitle("Diet Results");
	    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   //Setting Goal Value
	   			    	ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Set").commit();
	   			    	HomeScreenActivity.setNoMonths();
	   			    	//Go to the next screen
	   			    	nextScreenIntent = new Intent(context,HomeScreenActivity.class);
	   			    	//closing the other screens
	   			    	nextScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	   				     startActivity(nextScreenIntent);
	               }
	           });
	     instantiateVariables();
	    // Create the AlertDialog object and return it
	    dialog = builder.create();
	    return dialog;
	}
	
    private void instantiateVariables(){
    	context = view.getContext();
    	ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    	webview = (WebView)view.findViewById(R.id.diet_notice_web_view);
    	webview.loadDataWithBaseURL("", message, "text/html", "UTF-8", "");
    }
    

    


}
