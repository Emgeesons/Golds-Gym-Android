package com.emgeesons.goldsgym;

import com.facebook.SessionDefaultAudience;
import com.sromku.simple.fb.Permissions;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

import android.app.Application;

public class GoldGymApplication extends Application {
	private static final String APP_ID = "201215670038883";
	private static final String APP_NAMESPACE = "emgeesons_goldsgym";
	
	@Override
	public void onCreate()
	{
		super.onCreate();

		// initialize facebook configuration
		Permissions[] permissions = new Permissions[]
		{
			Permissions.EMAIL,
			Permissions.PUBLISH_ACTION,
			Permissions.PUBLISH_STREAM
		};

		SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
			.setAppId(APP_ID)
			.setNamespace(APP_NAMESPACE)
			.setPermissions(permissions)
			.setDefaultAudience(SessionDefaultAudience.FRIENDS)
			.build();

		SimpleFacebook.setConfiguration(configuration);
	}
}
