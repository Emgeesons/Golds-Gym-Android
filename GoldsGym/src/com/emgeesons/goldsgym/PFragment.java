package com.emgeesons.goldsgym;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PFragment extends Fragment{
	static View view;
	ImageView  profilePicture, bodyPics;
	String dob, endDate;
	TextView profileName, profileAge, profileHeight, noWorkouts, noWorkoutsMissed, noDaysLeft;
	SharedPreferences ggPrefs;
	static Context context;
	private static final String TEMP_PHOTO_FILE = "profile_pic.jpg";
	
	//For Chart
	private static GraphicalView mChartView;
	static LinearLayout chartLayout;
	static Map<String,?>  keys;
	Intent nextScreenIntent;
	LinearLayout advancedLayout, weightLayout;
	Animation animTranslate;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		view = inflater.inflate(R.layout.pfragment, container, false); 
		context = getActivity();
		//Animations
		animTranslate = AnimationUtils.loadAnimation(context, R.anim.anim_translate);
        return view;
    }
	
	@Override
	public void onResume() {
		super.onResume();
		HomeScreenActivity.trainingStatus();
		instantiateVariables();
		getImage();
		implementHandlers();
		logWeight();
		advancedProfile();
		drawChart();
		bodyPics();
		view.startAnimation(animTranslate);
	}
	
	private void logWeight(){
		//weight layout is only visible during normal training & maintenance and indeterminate state
		weightLayout = (LinearLayout)view.findViewById(R.id.profile_log_weight_layout);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Indeterminate")){
			weightLayout.setVisibility(View.VISIBLE);
			weightLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 DialogFragment refer = new AddLogDialogFragment();
			    	 refer.show(getFragmentManager(),"Add Log");
			    	 weightLayout.setVisibility(View.GONE);
			     }       
			});
		}else if (ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE,"Undefined").equalsIgnoreCase("Maintenance")){
			//it will come every month till the weight is logged
			int lastWeightMonth = ggPrefs.getInt(LaunchAppActivity.CURRENT_WEIGHT_MONTH, 0);
				if(LaunchAppActivity.currentMonth > lastWeightMonth){
				weightLayout.setVisibility(View.VISIBLE);
				weightLayout.setOnClickListener(new View.OnClickListener() {
				     @Override
				     public void onClick(View v) {
				    	 DialogFragment refer = new AddLogDialogFragment();
				         refer.show(getFragmentManager(),"Add Log");
				         weightLayout.setVisibility(View.GONE);
				     }       
				});
			}else{
				weightLayout.setVisibility(View.GONE);
			}
		}else{
			weightLayout.setVisibility(View.GONE);
		}
				
	}
	
	private void advancedProfile(){
		advancedLayout = (LinearLayout) view.findViewById(R.id.profile_advanced_profile);
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") && LaunchAppActivity.currentDay > 2 && ggPrefs.getInt(LaunchAppActivity.ADVANCED_PROFILE, 0)== 0 ){
		//This Card will only be shown four times
			//ggPrefs.edit().putInt(LaunchAppActivity.ADVANCED_PROFILE, ggPrefs.getInt(LaunchAppActivity.ADVANCED_PROFILE, 0) + 1).commit();
			advancedLayout.setVisibility(View.VISIBLE);
			advancedLayout.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
			    	 nextScreenIntent = new Intent(getActivity(),AdvancedProfileActivity.class);
			 	     startActivity(nextScreenIntent); 
			     }       
			});
		}else{
			advancedLayout.setVisibility(View.GONE);
		}
	}
	
	private void bodyPics(){
		if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Indeterminate") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Over") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Incomplete")){
			bodyPics.setVisibility(View.VISIBLE);
			bodyPics.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v) {
			    	 nextScreenIntent = new Intent(getActivity(),BodyPicsActivity.class);
			 	     startActivity(nextScreenIntent);
			 	    ((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			    }
			});
		}else{
			bodyPics.setVisibility(View.GONE);
		}
	}
	
	protected static void drawChart(){
		 chartLayout = (LinearLayout)view.findViewById(R.id.chart);
		chartLayout.removeAllViews();
		int weightSize = LaunchAppActivity.weightPrefs.getAll().size(), pbfSize = LaunchAppActivity.pbfPrefs.getAll().size(), smmSize = LaunchAppActivity.smmPrefs.getAll().size();
	    //For overView  
	    	  if( weightSize > 0){// true
		    	  //For all 3 graphs
	    		  String[] titles = new String[] { "Weight  ", "PBF  ", "SMM"};
		    	  int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.RED};
			      PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,PointStyle.SQUARE};
		    	  List<double[]> xValues = new ArrayList<double[]>();
		    	  List<double[]> yValues = new ArrayList<double[]>();
		    	  
		    	  //Now doing it for Weight
		    	  double[] xWeight = new double[weightSize];
				  double[] yWeight = new double[weightSize];
				  keys =LaunchAppActivity.weightPrefs.getAll();
				  int z=0;
					for(Entry<String, ?> entry : keys.entrySet()){
						xWeight[z] = Double.parseDouble(entry.getKey()); //month no
						yWeight[z] =  Double.parseDouble(entry.getValue().toString());// weight
					    z++;
					 }
				//Adding it to the main Arrays	
				xValues.add(xWeight);
				yValues.add(yWeight);
				
				//Now for PBF
				if(pbfSize > 0){
					double[] xPBF = new double[pbfSize];
					 double[] yPBF = new double[pbfSize];
					  keys =LaunchAppActivity.pbfPrefs.getAll();
					  z=0;
						for(Entry<String, ?> entry : keys.entrySet()){
							xPBF[z] = Double.parseDouble(entry.getKey()); //month no
							yPBF[z] =  Double.parseDouble(entry.getValue().toString());// pbf
						    z++;
						 }
					//Adding it to the main Arrays	
					xValues.add(xPBF);
					yValues.add(yPBF);
				}else{
					double[] xPBF = new double[1];
					 double[] yPBF = new double[1];
					  keys =LaunchAppActivity.pbfPrefs.getAll();
					 xPBF[0] = Double.parseDouble("0"); //month no
					 yPBF[0] =  Double.parseDouble("0");// weight
					//Adding it to the main Arrays	
					xValues.add(xPBF);
					yValues.add(yPBF);
				}
				
				//Now for SMM
				if(smmSize > 0){
					double[] xSMM = new double[smmSize];
					 double[] ySMM = new double[smmSize];
					  keys =LaunchAppActivity.smmPrefs.getAll();
					  z=0;
						for(Entry<String, ?> entry : keys.entrySet()){
							xSMM[z] = Double.parseDouble(entry.getKey()); //month no
							ySMM[z] =  Double.parseDouble(entry.getValue().toString());// smm
						    z++;
						 }
					//Adding it to the main Arrays	
					xValues.add(xSMM);
					yValues.add(ySMM);
				}else{
					double[] xSMM = new double[1];
					 double[] ySMM = new double[1];
					  keys =LaunchAppActivity.smmPrefs.getAll();
					  xSMM[0] = Double.parseDouble("0"); //month no
					  ySMM[0] =  Double.parseDouble("0");// weight
					//Adding it to the main Arrays	
					xValues.add(xSMM);
					yValues.add(ySMM);
				}
				
				
			      //Common Part
			      XYMultipleSeriesRenderer renderer1 = buildRenderer(colors, styles);
			      int length = renderer1.getSeriesRendererCount();
			      for (int i = 0; i < length; i++) {
		    	        ((XYSeriesRenderer) renderer1.getSeriesRendererAt(i)).setFillPoints(true);
			      }
			      setChartSettings(renderer1, "", "Month", "", 0,12, 0, 120,Color.LTGRAY, Color.LTGRAY);	   
		    	  renderer1.setXLabels(12);
		    	  renderer1.setYLabels(20);
		    	  renderer1.setShowGrid(true);
		    	  renderer1.setXAxisMin(0);
		    	  renderer1.setXLabelsAlign(Align.RIGHT);
		    	  renderer1.setYLabelsAlign(Align.RIGHT);
		    	  renderer1.setZoomButtonsVisible(true);
		    	  renderer1.setPanLimits(new double[] { 0, 12, 0, 120 });
		    	  renderer1.setZoomLimits(new double[] { 0, 20, 0, 200 });	
		    	  XYMultipleSeriesDataset dataset = buildDataset(titles, xValues, yValues);

	    	      mChartView = ChartFactory.getLineChartView(context, dataset, renderer1);
	    	      chartLayout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));    
	    	  }//end of weightSize > 0
	}
	
	protected static XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    setRenderer(renderer, colors, styles);
	    return renderer;
	  }
	
	protected static XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues,
		      List<double[]> yValues) {
		    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		    addXYSeries(dataset, titles, xValues, yValues, 0);
		    return dataset;
		  }
	
	public static void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,List<double[]> yValues, int scale) {
		    int length = titles.length;
		    for (int i = 0; i < length; i++) {
		      XYSeries series = new XYSeries(titles[i], scale);
		      double[] xV = xValues.get(i);
		      double[] yV = yValues.get(i);
		      int seriesLength = xV.length;
		      for (int k = 0; k < seriesLength; k++) {
		        series.add(xV[k], yV[k]);
		      }
		      dataset.addSeries(series);
		    }
		  }
	
	  
	  protected static void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
		    renderer.setAxisTitleTextSize(16);
		    renderer.setChartTitleTextSize(20);
		    renderer.setLabelsTextSize(15);
		    renderer.setLegendTextSize(15);
		    renderer.setPointSize(5f);
		    renderer.setMargins(new int[] { 20, 30, 15, 20 });
		    int length = colors.length;
		    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(colors[i]);
		      r.setPointStyle(styles[i]);
		      renderer.addSeriesRenderer(r);
		    }
		  }
	  
	  protected static void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
		      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
		      int labelsColor) {
		    renderer.setChartTitle(title);
		    renderer.setXTitle(xTitle);
		    renderer.setYTitle(yTitle);
		    renderer.setXAxisMin(xMin);
		    renderer.setXAxisMax(xMax);
		    renderer.setYAxisMin(yMin);
		    renderer.setYAxisMax(yMax);
		    renderer.setAxesColor(axesColor);
		    renderer.setLabelsColor(labelsColor);
		  }
	
	public void onActivityResult(int requestCode, int resultCode,Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {  
                    if (imageReturnedIntent!=null) {
                    	File tempFile = getTempFile();
                    	if(tempFile!=null){
	                        String filePath= Environment.getExternalStorageDirectory()+"/GoldsGym/media/"+TEMP_PHOTO_FILE;
	                        Bitmap selectedImage =  BitmapFactory.decodeFile(filePath);
	                        profilePicture.setImageBitmap(selectedImage );
	                     //   if (tempFile.exists()) tempFile.delete();
                    	}
                    }
                }
        }       
    }   
	
	private void getImage(){
		File tempFile = new File(Environment.getExternalStorageDirectory().toString() + "/GoldsGym/media",TEMP_PHOTO_FILE);
		if (tempFile.exists()){
			String filePath= Environment.getExternalStorageDirectory()
	                +"/GoldsGym/media/"+TEMP_PHOTO_FILE;
			Bitmap selectedImage =  BitmapFactory.decodeFile(filePath);
			profilePicture.setImageBitmap(selectedImage );
		}
	}
	
	private void instantiateVariables(){
		profilePicture = (ImageView) view.findViewById(R.id.profile_picture);
		profileName = (TextView) view.findViewById(R.id.profile_name);
		profileAge = (TextView) view.findViewById(R.id.profile_age);
		profileHeight = (TextView) view.findViewById(R.id.profile_height);
		noWorkouts = (TextView) view.findViewById(R.id.profile_no_workouts);
		noWorkoutsMissed = (TextView) view.findViewById(R.id.profile_no_workouts_missed);
		noDaysLeft = (TextView) view.findViewById(R.id.profile_no_days_left);
		bodyPics = (ImageView) view.findViewById(R.id.body_pics);
		assignInfo();
		
	}
	
	private void implementHandlers(){
		
		profilePicture.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	String path = Environment.getExternalStorageDirectory().toString();
                File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics/");
                if (!dir.isDirectory()) {
                        dir.mkdirs();
                }
		    	Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		    	photoPickerIntent.setType("image/*");
		    	photoPickerIntent.putExtra("crop", "true");
		    	photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		    	photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		    	startActivityForResult(photoPickerIntent, 1);
		    }
		});
	}

	
	private void assignInfo(){
		 ggPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		 String name = ggPrefs.getString(LaunchAppActivity.NAME, "No Name");
		 name = name.substring(0,1).toUpperCase() + name.substring(1);
		 profileName.setText(name);
		 dob = ggPrefs.getString(LaunchAppActivity.DOB, "00-00-0000");
		 if(dob.equalsIgnoreCase("00-00-0000")){
			 profileAge.setText("0 Yrs");
		 }else{
			 try { 
			 Long currentMS = new Date().getTime();
			 SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
			 Date d1;
			 d1 = format.parse(dob);
			 Long dobMS = d1.getTime();
			 Long ageMS = currentMS - dobMS;
			 int age = (int) (ageMS/31556952);
			 age = age/1000;
			 profileAge.setText(Integer.toString(age)+" Yrs");
			} catch (ParseException e) {
				profileAge.setText("0 Yrs");
			}
		 }
		 profileHeight.setText(ggPrefs.getString(LaunchAppActivity.HEIGHT, "0 Ft"));
		 
		 if(ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Begun") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Indeterminate") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Maintenance") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Over") || ggPrefs.getString(LaunchAppActivity.GOAL_STAGE, "Undefined").equalsIgnoreCase("Incomplete")){
			 noWorkouts.setText(Integer.toString(ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)));
			 if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay), false))
				 noWorkoutsMissed.setText(Integer.toString(LaunchAppActivity.currentDay - ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)));
			 else
				 noWorkoutsMissed.setText(Integer.toString(LaunchAppActivity.currentDay - 1 - ggPrefs.getInt(LaunchAppActivity.TOTAL_WORKOUTS, 0)));
			 if(Integer.parseInt(noWorkoutsMissed.getText().toString()) < 0)
				 noWorkoutsMissed.setText("0");
			 endDate = ggPrefs.getString(LaunchAppActivity.END_DATE, "00-00-0000");
			 if(endDate.equalsIgnoreCase("00-00-0000"))
					 noDaysLeft.setText("0");
			 else{
				 	if(LaunchAppActivity.daysLeft > 0){
				 		if(LaunchAppActivity.tickPrefs.getBoolean(Integer.toString(LaunchAppActivity.currentDay), false))
				 			noDaysLeft.setText(Integer.toString(LaunchAppActivity.daysLeft-1));
				 		else
				 			noDaysLeft.setText(Integer.toString(LaunchAppActivity.daysLeft));
				 	}
				 
			 }
		 }
	}
	
	private Uri getTempUri() {
	    return Uri.fromFile(getTempFile());
	}
	
	//This is for the photo
	private File getTempFile() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

	        File file = new File(Environment.getExternalStorageDirectory().toString()+"/GoldsGym/media",TEMP_PHOTO_FILE);
	        try {
	            file.createNewFile();
	        } catch (IOException e) {}

	        return file;
	    } else {

	        return null;
	    }
	}
}
