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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ChangeRoutineDialogFragment extends DialogFragment {

	
	TextView okButton, questionText, resultText;
	RadioGroup optionRadios;
	RadioButton fatButton, muscleButton, dontKnowButton;
	AlertDialog.Builder builder;
	AlertDialog dialog;
	Context context;
	View view;
	SharedPreferences ggPrefs;
	String selection;
	
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
       // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        view = inflater.inflate(R.layout.dialog_fragment_change_routine, null);
        builder.setView(view);
        builder.setTitle(R.string.course_correction);
         instantiateVariables();
         implementHandlers();
        // Create the AlertDialog object and return it
        dialog = builder.create();
        return dialog;
        
    }
    
    private void instantiateVariables(){
    	questionText = (TextView) view.findViewById(R.id.question_text);
    	resultText = (TextView) view.findViewById(R.id.result_text);
    	optionRadios = (RadioGroup) view.findViewById(R.id.options_radio);
    	fatButton = (RadioButton) view.findViewById(R.id.radio_fat);
    	muscleButton = (RadioButton) view.findViewById(R.id.radio_muscle);
    	dontKnowButton = (RadioButton) view.findViewById(R.id.radio_dont_know);
    	okButton = (TextView) view.findViewById(R.id.ok_button);
    	context = view.getContext();
    	ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    	resultText.setVisibility(View.GONE);
    }
    

    
    private void implementHandlers(){
    	okButton.setOnClickListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View v) {
				  if (resultText.getVisibility() == View.VISIBLE) {
					  	dialog.dismiss();
					  	HomeScreenActivity.resultTextStringId =0;
					} else if(HomeScreenActivity.resultTextStringId !=0) {
					    questionText.setVisibility(View.GONE);
					    optionRadios.setVisibility(View.GONE);
					    resultText.setVisibility(View.VISIBLE);
					    resultText.setText(HomeScreenActivity.resultTextStringId);
					    okButton.setText(" OK ");
					}
			  }
		});
    }
   
}
