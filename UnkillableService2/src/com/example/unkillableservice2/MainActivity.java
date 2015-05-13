package com.example.unkillableservice2;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.unkillableservice2.ServiceA.MyBinderA;
import com.example.unkillableservice2.ServiceB.MyBinderB;

public class MainActivity extends Activity {

	private static boolean flag;

	Calendar cal;
	PendingIntent pi;
	AlarmManager mAlarmManagerA;
	AlarmManager mAlarmManagerB;
	
	String tag = "service";
	private ServiceConnection connA = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.v(tag, "Service A disconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.v(tag, "Service A connected");
			MyBinderA binder = (MyBinderA) service;
			ServiceA SA = binder.getService();
			SA.showLog();
		}
	};

	private ServiceConnection connB = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			Log.v(tag, "Service B disconnected");
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			Log.v(tag, "Service B connected");
			MyBinderB binder = (MyBinderB) service;
			ServiceB SB = binder.getService();
			SB.showLog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;

//				cal = Calendar.getInstance();
//				
//				mAlarmManagerA = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//				mAlarmManagerB = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				
				Intent a = new Intent(MainActivity.this, ServiceA.class);
				startService(a);
				bindService(a, connA, BIND_AUTO_CREATE);

//				pi = PendingIntent.getService(MainActivity.this, 0, a, 0);
//				mAlarmManagerA.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000, pi);
				
				a = new Intent(MainActivity.this, ServiceB.class);
				startService(a);
				bindService(a, connB, BIND_AUTO_CREATE);
				
//				pi = PendingIntent.getService(MainActivity.this, 0, a, 0);
//				mAlarmManagerB.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1000, pi);
				
				a = null;
			}
		});

		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;

				Intent i = new Intent(MainActivity.this, ServiceA.class);
				// unbindService(connA);
				stopService(i);
//				mAlarmManagerA.cancel(pi);

				i = new Intent(MainActivity.this, ServiceB.class);
				// unbindService(connB);
				stopService(i);
//				mAlarmManagerB.cancel(pi);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static boolean getFlag() {
		return flag;
	}
}
