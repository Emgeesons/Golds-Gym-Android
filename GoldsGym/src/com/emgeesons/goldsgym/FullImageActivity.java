package com.emgeesons.goldsgym;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class FullImageActivity extends Activity {
	
	Context context;
	ImageView photoView, deletePhoto; 
	int position, originalPosition;
	String path = Environment.getExternalStorageDirectory().toString();
    File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics");
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image);
		
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		//passedVariables
		Bundle b = new Bundle();
	    b = getIntent().getExtras();
	    position = b.getInt("id");
	    originalPosition = position;

		instantiateVariables();
		implementHandlers();
		gestureListeners();
		
		
	}
	
	private void gestureListeners(){
		photoView.setOnTouchListener(new OnSwipeTouchListener() {
		    public void onSwipeRight() {
		    	 if (dir.isDirectory()) {
		             String[] children = dir.list();
		             if(position > 0){
		            	 position--;
		            	 photoView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/GoldsGym/media/Golds Gym Body Pics/"+children[position]));
		             	 setTitle(children[position].substring(0, children[position].indexOf(".")));
		    	 	}else{
		    	 		Toast.makeText(FullImageActivity.this, "No More Images", Toast.LENGTH_SHORT).show();
		    	 	}
		         }
		    }
		    public void onSwipeLeft() {
		    	 if (dir.isDirectory()) {
		             String[] children = dir.list();
		             if(position < children.length-1){
		            	 position++;
		            	 photoView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/GoldsGym/media/Golds Gym Body Pics/"+children[position]));
		             	 setTitle(children[position].substring(0, children[position].indexOf(".")));
		    	 	}else{
		    	 		Toast.makeText(FullImageActivity.this, "No More Images", Toast.LENGTH_SHORT).show();
		    	 	}
		         }
		    }
		});
	}
	
	private void instantiateVariables(){
		photoView = (ImageView) findViewById(R.id.photo_view);
		deletePhoto = (ImageView) findViewById(R.id.delete_photo);

		
        if (dir.isDirectory()) {
            String[] children = dir.list();
            photoView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/GoldsGym/media/Golds Gym Body Pics/"+children[position]));
            setTitle(children[position].substring(0, children[position].indexOf(".")));
        }
		
		
	}
	
	private void implementHandlers(){
		deletePhoto.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	 String path = Environment.getExternalStorageDirectory().toString();
		         File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics");
		         if (dir.isDirectory()) {
		             String[] children = dir.list();
		                 new File(dir, children[position]).delete();
		         }
		    	onBackPressed();
		    }
		});
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

}
