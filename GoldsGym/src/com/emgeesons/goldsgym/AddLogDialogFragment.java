package com.emgeesons.goldsgym;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddLogDialogFragment extends DialogFragment {

	
	TextView okButton, cancelButton;
	EditText addWeightText, addPbfText, addSmmText;
	AlertDialog.Builder builder;
	AlertDialog dialog;
	Context context;
	View view;
	String  date;
	int year, month, day, dateNo;
	SharedPreferences ggPrefs;
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
       // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_fragment_add_log, null);
        builder.setView(view);
        builder.setTitle(R.string.add_log);
         instantiateVariables();
         implementHandlers();
        // Create the AlertDialog object and return it
        dialog = builder.create();
        return dialog;
        
    }
    
    private void instantiateVariables(){
    	
    	addWeightText = (EditText) view.findViewById(R.id.enter_weight);
    	addPbfText = (EditText) view.findViewById(R.id.enter_pbf);
    	addSmmText = (EditText) view.findViewById(R.id.enter_smm);
    	okButton = (TextView) view.findViewById(R.id.ok_button);
    	cancelButton = (TextView) view.findViewById(R.id.cancel_button);
    	context = view.getContext();
    	ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    private void implementHandlers(){
    	okButton.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if(verifyInfo()){
             	   //Getting the Values
					  updateGraph();
					  dialog.dismiss();
					  updateGoalStage();
                }else{
             	   Toast toast = Toast.makeText(context, "Hey. Enter Your Stats So We Can Track Your Progress", Toast.LENGTH_SHORT);
    			   toast.show();
                }
			  }
		});
  	cancelButton.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  dialog.dismiss();
			  }
		});
    }
    
    private boolean verifyInfo(){
    	if(addWeightText.getText()!=null &&  addWeightText.getText().toString()!= null && addWeightText.getText().toString().length() >=1){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    private void updateGoalStage(){//takes care of Goal Stage and Routine
    	float currentWeight = Float.parseFloat(addWeightText.getText().toString());
    	float beginWeight = ggPrefs.getFloat(LaunchAppActivity.WEIGHT_BEGIN, 0);
    	float weightAmount = ggPrefs.getFloat(LaunchAppActivity.WEIGHT_AMOUNT, 0);
    	boolean weightLoss = false;
    	if(ggPrefs.getString(LaunchAppActivity.TRAINING_PROGRAM, "Weight Loss").equalsIgnoreCase("Weight Loss")){
    		weightLoss = true;
    	}
    	//For begun
    	if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun")){
    		if(weightLoss){
    			//user reached goal
    			if(currentWeight <= (beginWeight - weightAmount)){
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Over").commit();
    			}else if (currentWeight > beginWeight){//change routine
    				if(!ggPrefs.getBoolean(LaunchAppActivity.CHANGE_ROUTINE, false)){
	    				ggPrefs.edit().putBoolean(LaunchAppActivity.CHANGE_ROUTINE, true).commit();
				    	 DialogFragment refer = new ChangeRoutineDialogFragment();
				    	 refer.show(getFragmentManager(),"Course Correction");
    				}
    			}
    		}else{//weight gain
    			if(currentWeight >= (beginWeight + weightAmount)){//user reached goal
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Over").commit();
    			}
    		}
    	}//end of begun
    	if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Indeterminate")){
    		if(weightLoss){
    			//user reached goal
    			if(currentWeight <= (beginWeight - weightAmount)){
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Over").commit();
    			}else{//user has not reached goal
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Incomplete").commit();
    			}
    		}else{//weight gain
    			if(currentWeight >= (beginWeight + weightAmount)){//user reached goal
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Over").commit();
    			}else{
    				ggPrefs.edit().putString(LaunchAppActivity.GOAL_STAGE, "Incomplete").commit();
    			}
    		}
    	}
    }
    
    private void updateGraph(){
    	int decimalPart = (((LaunchAppActivity.currentDay%30) * 100)/30);
    	String dP = Integer.toString(decimalPart);
    	if(dP.length() == 1)
    		dP="0"+dP;
    	String monthValue = Integer.toString(LaunchAppActivity.currentMonth - 1)+"."+dP;
    	//For Weight
    	ggPrefs.edit().putFloat(LaunchAppActivity.CURRENT_WEIGHT, Float.parseFloat(addWeightText.getText().toString())).commit();
    	ggPrefs.edit().putInt(LaunchAppActivity.CURRENT_WEIGHT_MONTH, LaunchAppActivity.currentMonth).commit();
    	LaunchAppActivity.weightPrefs.edit().putFloat(monthValue, Float.parseFloat(addWeightText.getText().toString())).commit();
    	if(addPbfText.getText().toString().length() >= 1)
    		LaunchAppActivity.pbfPrefs.edit().putFloat(monthValue, Float.parseFloat(addPbfText.getText().toString())).commit();
    	if(addSmmText.getText().toString().length() >= 1)
    		LaunchAppActivity.smmPrefs.edit().putFloat(monthValue, Float.parseFloat(addSmmText.getText().toString())).commit();
    	 int tabPosition = HomeScreenActivity.currentTab();
    	if(tabPosition == 2)
    		HomeScreenActivity.setTab(0);
    	else
    		HomeScreenActivity.setTab(2);

    }
}
