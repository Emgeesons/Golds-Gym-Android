package com.emgeesons.goldsgym;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ResetGoalDialogFragment extends DialogFragment{

	Intent nextScreenIntent;
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are You Sure?")
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       HomeScreenActivity.reset();
                       HomeScreenActivity.trainingStatus();
	            	   //Resume the current fragment tab
                       int tab = HomeScreenActivity.currentTab();
                       if(tab == 0)
                    	   HomeScreenActivity.setTab(1);
                       else 
                    	   HomeScreenActivity.setTab(0);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // User cancelled the dialog
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
    
}