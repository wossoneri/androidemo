package com.example.unkillableservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	// 定义浮动窗口布局
	// private LinearLayout mFloatLayout;
	private static boolean flag;

	private ServiceConnection mSC1 = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub

		}
	};
	private ServiceConnection mSC2 = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub

		}
	};

	// ** Called when the activity is first created.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// FloatingWindowActivity的布局视图按钮
		Button start = (Button) findViewById(R.id.start_id);

		Button remove = (Button) findViewById(R.id.remove_id);

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				Intent intent = new Intent(MainActivity.this, FxService.class);
				startService(intent);
				// bindService(intent, mSC1, BIND_AUTO_CREATE);
				intent = new Intent(MainActivity.this, ListenService.class);
				startService(intent);
				// bindService(intent, mSC2, BIND_AUTO_CREATE);
				// finish();
			}
		});

		remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// uninstallApp("com.phicomm.hu");
				flag = true;
				Intent i = new Intent(MainActivity.this, FxService.class);
				stopService(i);
				i = new Intent(MainActivity.this, ListenService.class);
				stopService(i);
				// unbindService(mSC1);
				// unbindService(mSC2);
			}
		});

	}

	// private void uninstallApp(String packageName) {
	// Uri packageURI = Uri.parse("package:" + packageName);
	// Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
	// startActivity(uninstallIntent);
	// // setIntentAndFinish(true, true);
	// }

	public static boolean getFlag() {
		return flag;
	}
	/*
	 * private void forceStopApp(String packageName) { ActivityManager am =
	 * (ActivityManager)getSystemService( Context.ACTIVITY_SERVICE);
	 * am.forceStopPackage(packageName);
	 * 
	 * Class c =
	 * Class.forName("com.android.settings.applications.ApplicationsState");
	 * Method m = c.getDeclaredMethod("getInstance", Application.class);
	 * 
	 * //mState = ApplicationsState.getInstance(this.getApplication()); }
	 */

	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// flag = true;
	// unbindService(mSC1);
	// unbindService(mSC2);
	// }

}