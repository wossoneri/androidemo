package com.phicomm.hu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class FloatWindowTest extends Activity 
{
    /** Called when the activity is first created. */
	
	private static final String TAG = "FloatWindowTest";
	WindowManager mWindowManager;
	WindowManager.LayoutParams wmParams;
	LinearLayout mFloatLayout;
	Button mFloatView;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //createFloatView();
        setContentView(R.layout.main);
        
        Button start = (Button)findViewById(R.id.start);
        Button stop = (Button)findViewById(R.id.stop);
        
        start.setOnClickListener(new OnClickListener() 
        {
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				createFloatView();
				//finish();
				//handle.post(r);
			}
		});
        
        stop.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(mFloatLayout != null)
				{
					mWindowManager.removeView(mFloatLayout);
					finish();
				}	
		}
		});
        
        
    }
    
    private void createFloatView()
    {
    	//获取LayoutParams对象
        wmParams = new WindowManager.LayoutParams();
        
        //获取的是LocalWindowManager对象
        mWindowManager = this.getWindowManager();
        Log.i(TAG, "mWindowManager1--->" + this.getWindowManager());
        //mWindowManager = getWindow().getWindowManager();
        Log.i(TAG, "mWindowManager2--->" + getWindow().getWindowManager());
     
        //获取的是CompatModeWrapper对象
        //mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager3--->" + mWindowManager);
        wmParams.type = LayoutParams.TYPE_PHONE;
        wmParams.format = PixelFormat.RGBA_8888;;
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        
        LayoutInflater inflater = this.getLayoutInflater();//LayoutInflater.from(getApplication());
        
        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.float_layout, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        //setContentView(R.layout.main);
        mFloatView = (Button)mFloatLayout.findViewById(R.id.float_id);
        
        Log.i(TAG, "mFloatView" + mFloatView);
        Log.i(TAG, "mFloatView--parent-->" + mFloatView.getParent());
        Log.i(TAG, "mFloatView--parent--parent-->" + mFloatView.getParent().getParent());
        //绑定触摸移动监听
        mFloatView.setOnTouchListener(new OnTouchListener() 
        {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				// TODO Auto-generated method stub
				wmParams.x = (int)event.getRawX() - mFloatLayout.getWidth()/2;
				//25为状态栏高度
				wmParams.y = (int)event.getRawY() - mFloatLayout.getHeight()/2 - 40;
				mWindowManager.updateViewLayout(mFloatLayout, wmParams);
				return false;
			}
		});
        
        //绑定点击监听
        mFloatView.setOnClickListener(new OnClickListener()
        {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(FloatWindowTest.this, ResultActivity.class);
				startActivity(intent);
			}
		});
        
    }
}