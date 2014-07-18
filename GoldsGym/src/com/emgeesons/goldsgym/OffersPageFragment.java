package com.emgeesons.goldsgym;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;



public class OffersPageFragment extends Fragment {

	JSONObject passedObject;
	TextView offerTitle, offerDescription;
	ViewGroup rootView;
	ImageView offersImage;
	String offerUrls;
	String[] offerUrl;
	ImageLoader imageLoader;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
         rootView = (ViewGroup) inflater.inflate(R.layout.fragment_offers_page, container, false);
        Bundle bundleFragment = this.getArguments();
        try {
			passedObject = new JSONObject(bundleFragment.getString("json", "Undefined"));
		} catch (JSONException e) {
		}
        imageLoader = new ImageLoader(OffersActivity.queue, new BitmapCache(4));
        instantiateVariables();
        return rootView;
    }
	
	private void instantiateVariables(){
		offerTitle = (TextView) rootView.findViewById (R.id.offer_title);
		offerDescription = (TextView) rootView.findViewById (R.id.offer_description);
		offersImage = (ImageView) rootView.findViewById(R.id.offer_image);
		
		try {
			offerTitle.setText(passedObject.getString("title"));
			offerDescription.setText(Html.fromHtml(passedObject.getString("description")).toString());
			Linkify.addLinks(offerDescription, Linkify.ALL);
			
			//Offer Image Url
			offerUrls = passedObject.getString("image").toString();
			if(offerUrls.length()>2){
			//	System.out.println("Character is " + offerUrls.substring(0, offerUrls.length() - 2));
				offerUrls =offerUrls.substring(2, offerUrls.length()-3);
				offerUrl = offerUrls.split("\",\"");
				//for single images
				if(!offerUrl[0].endsWith("g"))
					offerUrl[0] = offerUrl[0]+"g";
				//Enter the default image over here
				imageLoader.get(offerUrl[0].replace("\\",""), ImageLoader.getImageListener(offersImage, R.drawable.loading, R.drawable.default_offers));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	

}
