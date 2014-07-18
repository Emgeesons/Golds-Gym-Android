package com.emgeesons.goldsgym;

import java.util.Calendar;

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
import android.widget.DatePicker;

public class VacationDialogFragment  extends DialogFragment {

	AlertDialog.Builder builder;
	AlertDialog dialog;
	Context context;
	View view;
	ProgressDialog pd;
	SharedPreferences ggPrefs;
	String message;
	Intent nextScreenIntent;
	DatePicker vacationDatePicker;
	int year, month, day;
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    // Use the Builder class for convenient dialog construction
	    builder = new AlertDialog.Builder(getActivity());
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	   // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    view = inflater.inflate(R.layout.dialog_fragment_vacation, null);
	    builder.setView(view);
	    builder.setTitle(getResources().getString(R.string.vacation_heading));
	    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	            	   LaunchAppActivity.vacation = true;
	         		   year = vacationDatePicker.getYear();
	         		   month = vacationDatePicker.getMonth(); 
	         		   day = vacationDatePicker.getDayOfMonth();
	         		   ggPrefs.edit().putString(LaunchAppActivity.VACATION_END_DATE,Integer.toString(day)+"-" +Integer.toString(month)+"-"+Integer.toString(year)).commit();
	         		   HomeScreenActivity.trainingStatus();
	            	   //Resume the current fragment tab
	            	   HomeScreenActivity.setTab(1);
	               }
	           });
	    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
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
    	vacationDatePicker = (DatePicker)view.findViewById(R.id.vacation_end_date);
    	Calendar maxEndDate = Calendar.getInstance();
    	maxEndDate.add(Calendar.YEAR,1);
    	vacationDatePicker.setMaxDate(maxEndDate.getTimeInMillis());
    	vacationDatePicker.setMinDate(System.currentTimeMillis() - 1000);
    	
    }
}
