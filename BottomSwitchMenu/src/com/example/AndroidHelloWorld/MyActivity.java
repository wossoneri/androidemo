package com.example.AndroidHelloWorld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

public class MyActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不显示当前界面标题栏
        setContentView(R.layout.main);//指定当前Activity的布局文件为res/layout/main.xml

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        layout.setBackgroundResource(R.drawable.vpic);//设置背景图片，这里可以先判断当前是否为横屏，如果横屏则显示hpic

        //从MyActivity跳转到MainActivity
        final Intent intent = new Intent(MyActivity.this, MainActivity.class);

        //如果之前启动过这个Activity，并还没有被destroy的话，而是无论是否存在，都重新启动新的activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //创建一个新的线程来显示欢迎动画，指定时间后结束，跳转至指定界面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//用线程暂停3秒来模拟做了一个耗时3秒的检测操作,为了省时间，改为1秒

                    //获取应用的上下文，生命周期是整个应用，应用结束才会结束
                    getApplicationContext().startActivity(intent);//跳转
                    finish();//结束本欢迎画面Activity
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //背景自动适应
        //AutoBackground(this, layout, R.drawable.vpic, R.drawable.hpic);
    }

//    //获取屏幕方向
//    public static int ScreenOrient(Activity activity) {
//        int orient = activity.getRequestedOrientation();
//        if (orient != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE && orient != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            //宽>高为横屏,反正为竖屏
//            WindowManager windowManager = activity.getWindowManager();
//            Display display = windowManager.getDefaultDisplay();
//            int screenWidth = display.getWidth();
//            int screenHeight = display.getHeight();
//            orient = screenWidth < screenHeight ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
//        }
//        return orient;
//    }
//
//    public static void AutoBackground(Activity activity, View view, int Background_v, int Background_h) {
//        int orient = ScreenOrient(activity);
//        if (orient == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) { //纵向
//            view.setBackgroundResource(Background_v);
//        } else { //横向
//            view.setBackgroundResource(Background_h);
//        }
//    }


}
