package com.example.du.like_button_animation;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;


public class MainActivity extends Activity implements View.OnClickListener {

    private Button button;
    private TextView textView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this, R.anim.like_anim);
        button = (Button) findViewById(R.id.bt);
        button.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.animation);
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            textView.setVisibility(View.VISIBLE);
            textView.startAnimation(animation);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    textView.setVisibility(View.GONE);
                }
            }, 1000);
        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

