package com.example.unkillableservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	// 定义浮动窗口布局
	// private LinearLayout mFloatLayout;
	private static boolean flag;

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
				intent = new Intent(MainActivity.this, ListenService.class);
				startService(intent);
				finish();
			}
		});

		remove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// uninstallApp("com.phicomm.hu");
				flag = true;
				Intent intent = new Intent(MainActivity.this, FxService.class);
				stopService(intent);
				intent = new Intent(MainActivity.this, ListenService.class);
				stopService(intent);
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
}