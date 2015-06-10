package com.example.AndroidHelloWorld;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-26
 * Time: 下午12:28
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends ActivityGroup {
    private ScrollView container = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout2);

        LinearLayout layout = (LinearLayout) findViewById(R.id.main_lineer);//得到主页面的最外层布局控件
        layout.setBackgroundResource(R.drawable.main_bg);//设置背景图片，这里可以先判断当前是否为横屏，如果横屏则显示hpic
        initRadios();//初始化单选按钮
        container = (ScrollView) findViewById(R.id.containerBody);//得到用来显示不同Activity的容器
        View v = findViewById(R.id.tab);//得到下方tab
        v.getBackground().setAlpha(180);//设置其背景透明度
        ((RadioButton)findViewById(R.id.radio_button0)).setChecked(true);//初始化启动程序选中第一个
    }

    //给radiogroup的选项发生变化事件添加响应事件
    private void initRadios() {
        ((RadioButton) findViewById(R.id.radio_button0))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tabChange(compoundButton, b);
                    }
                });
        ((RadioButton) findViewById(R.id.radio_button1))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tabChange(compoundButton,b);
                    }
                });
        ((RadioButton) findViewById(R.id.radio_button2))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tabChange(compoundButton,b);
                    }
                });
        ((RadioButton) findViewById(R.id.radio_button3))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tabChange(compoundButton,b);
                    }
                });
        ((RadioButton) findViewById(R.id.radio_button4))
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        tabChange(compoundButton, b);
                    }
                });
    }

    //单选按钮change后执行
    public void tabChange(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){//是否为选中
            container.removeAllViews();//清空容器内容，必须
            Intent intent =null;//Intent是一个Activity到达另一个Activity的引路者，它描述了起点（当前Activity）和终点（目标Activity）
            //以下switch为判断选中的是哪个
            switch (buttonView.getId()) {
                case R.id.radio_button0:
                    intent = new Intent(MainActivity.this, View1Activity.class);
                    break;
                case R.id.radio_button1:
                    intent = new Intent(MainActivity.this, View2Activity.class);
                    break;
                case R.id.radio_button2:
                    intent = new Intent(MainActivity.this, View3Activity.class);
                    break;
                case R.id.radio_button3:
                    intent = new Intent(MainActivity.this, View4Activity.class);
                    break;
                case R.id.radio_button4:
                    intent = new Intent(MainActivity.this, View5Activity.class);
                    break;
            }
            /*
            * Intent的标志之一（目前我知道有4个），理解这个东西需要一点栈的知识，感兴趣的朋友去百度一下，不过建议先使用
            * */
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            /*
            * 代码解释：
            * getLocalActivityManager()得到一个LocalActivityManager，此方法来自父类ActivityGroup，
            * 通过LocalActivityManager通过startActivity(String id, Intent intent),可以与指定的Actiivty绑定，
            * 并且返回一个Window。LocalActivityManager可以同时管理多个Activity
            * */
            Window subActivity = getLocalActivityManager().startActivity(
                                    "subActivity", intent);
            //将intent传递给指定页面， 你可以理解为跳转到该页面
            container.addView(subActivity.getDecorView());
        }
    }

/*
*  从此处开始，以下代码为前面我菜单制作教程的代码，本章内容中没有用到，可以不用理会
*
* */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals("退出")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setIcon(R.drawable.menu_icons);
            builder.setTitle("你确定要离开吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "好吧，你要退出。.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "你太好了，欢迎继续使用.", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}