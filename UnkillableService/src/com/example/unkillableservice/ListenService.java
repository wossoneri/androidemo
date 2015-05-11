package com.example.unkillableservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class ListenService extends Service {

	private BroadcastReceiver mBR;
	private IntentFilter mIF;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub

		mBR = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Intent a = new Intent(ListenService.this, FxService.class);
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
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (!MainActivity.getFlag()) {
			Intent intent = new Intent();
			intent.setAction("listener");
			sendBroadcast(intent);
		}
		unregisterReceiver(mBR);
	}

}
