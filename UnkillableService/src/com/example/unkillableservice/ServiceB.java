package com.example.unkillableservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ServiceB extends Service {

	private BroadcastReceiver mBR;
	private IntentFilter mIF;
	private static final String TAG = "service";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(TAG, "Service B created");
		mBR = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Log.v(TAG, "Service A receive broadcast");
				
				Intent a = new Intent(ServiceB.this, ServiceA.class);
				// float x = intent.getFloatExtra("Xpos", 0);
				// float y = intent.getFloatExtra("Ypos", 0);
				// a.putExtra("Xpos", x);
				// a.putExtra("Ypos", y);
				startService(a);
			}
		};
		mIF = new IntentFilter();
		mIF.addAction("fxservice");
		registerReceiver(mBR, mIF);

	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(TAG, "Service B start command");
		return Service.START_STICKY;
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG, "Service B destroyed");
		
		if (!MainActivity.getFlag()) {
			Intent intent = new Intent();
			intent.setAction("listener");
			sendBroadcast(intent);
			Log.v(TAG, "Service B send broadcast");
		}
		unregisterReceiver(mBR);
	}

}
