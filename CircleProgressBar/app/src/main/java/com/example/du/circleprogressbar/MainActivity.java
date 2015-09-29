package com.example.du.circleprogressbar;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    CircleProgressBar barStroke;
    CircleProgressBar barStrokeText;
    CircleProgressBar barFill;

    Timer updateTimer;
    TimerTask updateTimerTask;
    int currentValue = 0;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barStroke = (CircleProgressBar) findViewById(R.id.barStroke);
        barStroke.setMax(100);

        barStrokeText = (CircleProgressBar) findViewById(R.id.barStrokeText);
        barStrokeText.setMax(100);

        barFill = (CircleProgressBar) findViewById(R.id.barFill);
        barFill.setMax(100);

        updateTimer = new Timer();

        updateTimerTask = new TimerTask() {
            @Override
            public void run() {
                currentValue += 1;
                barStroke.setValue(currentValue);
                barStrokeText.setValue(currentValue);
                barFill.setValue(currentValue);

                if (currentValue >= 100)
                    currentValue = 0;
            }
        };
        updateTimer.schedule(updateTimerTask, 0, 50);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
