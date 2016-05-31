package com.qybrowser.fragment;

import com.tencent.smtt.sdk.QbSdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Process;

public class PreInitBackgroundActivity extends Activity{

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QbSdk.PreInitCallback initCallback = new QbSdk.PreInitCallback() {
			
			@Override
			public void onViewInitFinished() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(), X5FirstTimeActivity.class);
				getApplicationContext().startActivity(intent);
			}
			
			@Override
			public void onCoreInitFinished() {
				// TODO Auto-generated method stub
				
			}
		};
		
		QbSdk.preInit(this, initCallback);
	};
	
}
