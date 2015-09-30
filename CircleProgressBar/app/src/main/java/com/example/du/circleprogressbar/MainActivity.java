package com.example.du.circleprogressbar;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    CircleProgressBar mBarStroke;
    CircleProgressBar mBarText;
    CircleProgressBar mBarFill;

    Timer mTimer;
    TimerTask mTimerTask;
    int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBarStroke = (CircleProgressBar)findViewById(R.id.barStroke);
        mBarStroke.setMax(100);

        mBarText = (CircleProgressBar)findViewById(R.id.barText);
        mBarText.setMax(100);

        mBarFill = (CircleProgressBar)findViewById(R.id.barFill);
        mBarFill.setMax(100);

        mTimer = new Timer();

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                value += 1;
                mBarStroke.setValue(value);
                mBarText.setValue(value);
                mBarFill.setValue(value);

                if (value >= 100)
                    value = 0;
            }
        };

        mTimer.schedule(mTimerTask, 0, 50);


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
