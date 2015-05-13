package com.example.unkillableservice2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceB extends Service {

	private BroadcastReceiver mBR;
	private IntentFilter mIF;

	String tag = "service";

	public void showLog() {
		Log.i(tag, "serviceB-->showLog()");
	}

	public class MyBinderB extends Binder {

		public ServiceB getService() {
			return ServiceB.this;
		}
	}

	private MyBinderB myBinderB = new MyBinderB();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinderB;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(tag, "Service B created");

		mBR = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Intent a = new Intent(ServiceB.this, ServiceA.class);
				startService(a);
			}
		};
		mIF = new IntentFilter();
		mIF.addAction("ServiceA");
		registerReceiver(mBR, mIF);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service B start command");
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v(tag, "Service B destroyed");

		if (!MainActivity.getFlag()) {
			Intent intent = new Intent();
			intent.setAction("ServiceB");
			sendBroadcast(intent);
		}
		unregisterReceiver(mBR);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service B unbind");

		return true;
	}

}
