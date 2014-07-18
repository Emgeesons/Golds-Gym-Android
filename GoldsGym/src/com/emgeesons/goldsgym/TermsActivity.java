package com.emgeesons.goldsgym;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TermsActivity extends Activity {
	//WebView webView;
	//ProgressDialog pd;
	
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_terms);
		setTitle(" Terms & Conditions");
		/*
		webView = (WebView) findViewById(R.id.terms_web_view);
	    pd = ProgressDialog.show(this, "", "Loading...",true);
	    pd.setCancelable(true);
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setSupportZoom(true);  
	    webView.getSettings().setBuiltInZoomControls(true);
	    webView.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onPageFinished(WebView view, String url) {
		        if(pd.isShowing()&&pd!=null){
		             pd.dismiss();
		        }
	        }
	    });
	    webView.loadUrl(getString(R.string.terms_link));*/
	    
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
