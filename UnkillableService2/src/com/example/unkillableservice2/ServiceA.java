package com.example.unkillableservice2;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class ServiceA extends Service {
	private BroadcastReceiver mBR;
	private IntentFilter mIF;
	String tag = "service";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return myBinderA;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(tag, "Service A created");

		mBR = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				Intent a = new Intent(ServiceA.this, ServiceB.class);
				startService(a);
			}
		};
		mIF = new IntentFilter();
		mIF.addAction("ServiceB");
		registerReceiver(mBR, mIF);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service A start command");
		createFloatView();
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(tag, "Service A destroyed");

		unregisterReceiver(mBR);
		if (mFloatLayout != null) {
			mWindowManager.removeView(mFloatLayout);
		}
		if (!MainActivity.getFlag()) {
			Intent intent = new Intent();
			intent.setAction("ServiceA");
			sendBroadcast(intent);
		}

	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(tag, "Service A unbind");

		return true;
	}

	public void showLog() {
		Log.i(tag, "serviceA-->showLog()");
	}

	public class MyBinderA extends Binder {

		public ServiceA getService() {
			return ServiceA.this;
		}
	}

	private MyBinderA myBinderA = new MyBinderA();

	LinearLayout mFloatLayout;
	WindowManager.LayoutParams wmParams;
	// 创建浮动窗口设置布局参数的对象
	WindowManager mWindowManager;
	Button mFloatView;
	private float lastX = 0;
	private float lastY = 0;

	private void createFloatView() {
		wmParams = new WindowManager.LayoutParams();
		// 获取WindowManagerImpl.CompatModeWrapper 其实就是window manager对象
		mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
		// 设置window type
		wmParams.type = LayoutParams.TYPE_PHONE;
		// 设置图片格式，效果为背景透明
		wmParams.format = PixelFormat.RGBA_8888;
		// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
		wmParams.flags =
		// LayoutParams.FLAG_NOT_TOUCH_MODAL |
		LayoutParams.FLAG_NOT_FOCUSABLE
		// LayoutParams.FLAG_NOT_TOUCHABLE
		;

		// 调整悬浮窗显示的停靠位置为左侧置顶
		wmParams.gravity = Gravity.START | Gravity.TOP;

		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = (int) lastX;
		wmParams.y = (int) lastY;

		/*
		 * // 设置悬浮窗口长宽数据 wmParams.width = 200; wmParams.height = 80;
		 */

		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

		LayoutInflater inflater = LayoutInflater.from(getApplication());
		// 获取浮动窗口视图所在布局
		mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
		// 添加mFloatLayout
		mWindowManager.addView(mFloatLayout, wmParams);

		// 浮动窗口按钮
		mFloatView = (Button) mFloatLayout.findViewById(R.id.float_id);

		mFloatLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
				View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		// 设置监听浮动窗口的触摸移动
		mFloatView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				// getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
				lastX = event.getRawX() - mFloatView.getMeasuredWidth() / 2;
				wmParams.x = (int) lastX;
				// Log.i(tag, "Width/2--->" + mFloatView.getMeasuredWidth()/2);
				// 25为状态栏的高度
				lastY = event.getRawY() - mFloatView.getMeasuredHeight() / 2 - 25;
				wmParams.y = (int) lastY;
				// Log.i(tag, "Width/2--->" + mFloatView.getMeasuredHeight()/2);
				// 刷新
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});

	}
}
