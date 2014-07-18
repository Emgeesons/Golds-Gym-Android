package com.emgeesons.goldsgym;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BodyPicsActivity extends Activity {
	
	Context context;
	ImageView addPhoto;
	private GridView gridView;
    private GridViewAdapter customGridAdapter;
    int childrenLength=0;
    RelativeLayout relativeLayout;
    Intent nextScreenIntent;
    String[] children;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_body_pics);
		context= this;
		//Back Button
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		instantiateVariables();
		implementHandlers();
		
		
	}
	
	public void onResume() {
		super.onResume();
		getPics();
	}
	
	private void instantiateVariables(){
		addPhoto = (ImageView) findViewById(R.id.add_photo);
		gridView = (GridView) findViewById(R.id.gallery_grid);
		relativeLayout = (RelativeLayout) findViewById(R.id.relative_layout);

	}
	
	private void getPics(){
		String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics");
        if (dir.isDirectory()) {
            children = dir.list();
             childrenLength = children.length;
        }
        if(childrenLength > 0){
        	gridView.setVisibility(View.VISIBLE);
        	relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
	        customGridAdapter = new GridViewAdapter(this, R.layout.row_grid, getData());
	        gridView.setAdapter(customGridAdapter);
	        gridView.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
		        	nextScreenIntent = new Intent(context,FullImageActivity.class);
		        	nextScreenIntent.putExtra("id",position);
		        	startActivity(nextScreenIntent);
		        	((Activity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	            }
	     
	        });
        }else{
        	gridView.setVisibility(View.GONE);
        	relativeLayout.setBackgroundResource(R.drawable.empty_pics);
        }
	}
	
    private ArrayList getData() {
    	final ArrayList imageItems = new ArrayList();
        // retrieve String drawable array
    	String path = Environment.getExternalStorageDirectory().toString();
        File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
            	imageItems.add(new ImageItem(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+"/GoldsGym/media/Golds Gym Body Pics/"+children[i]),children[i].substring(0, children[i].indexOf("."))));
            	
            }
        }
 
        return imageItems;
 
    }
	
	private void implementHandlers(){
		addPhoto.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	selectSource();
		    }
		});
	}
	
	private void selectSource(){
		 final CharSequence[] options = { "From Camera", "Choose From Gallery","Cancel" };
		 
	        AlertDialog.Builder builder = new AlertDialog.Builder(BodyPicsActivity.this);
	        builder.setTitle("Add Photo");
	        builder.setItems(options, new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int item) {
	                if (options[item].equals("From Camera"))
	                {
	                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	                    String path = Environment.getExternalStorageDirectory().toString();
	                    File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics/");
	                    if (!dir.isDirectory()) {
	                            dir.mkdirs();
	                    }
	                    //Give the image a name
	                    String currentDateTimeString = new SimpleDateFormat("MMM dd, HH-mm-ss").format(new Date());
	                    File file = new File(dir, currentDateTimeString + ".jpg");
	                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
	                    intent.putExtra("crop", "true");
	                    galleryAddPic(file.getAbsolutePath());
	                    startActivityForResult(intent, 1);
	                }
	                else if (options[item].equals("Choose From Gallery"))
	                {
	                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                    String path = Environment.getExternalStorageDirectory().toString();
	                    File dir = new File(path, "/GoldsGym/media/Golds Gym Body Pics/");
	                    if (!dir.isDirectory()) {
	                            dir.mkdirs();
	                    }
	                    //Give the image a name

	                    String currentDateTimeString = new SimpleDateFormat("MMM dd, HH-mm-ss").format(new Date());
	                    File file = new File(dir, currentDateTimeString + ".jpg");
	                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
	                    intent.putExtra("crop", "true");
	                    galleryAddPic(file.getAbsolutePath());
	                    startActivityForResult(intent, 2);
	 
	                }
	                else if (options[item].equals("Cancel")) {
	                    dialog.dismiss();
	                }
	            }
	        });
	        builder.show();
	}
	
	private void galleryAddPic(String path) {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(path);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
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
