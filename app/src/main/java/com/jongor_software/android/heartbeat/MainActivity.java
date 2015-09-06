package com.jongor_software.android.heartbeat;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private Button mStartBeatButton;
    private Button mStopResetButton;

    private TextView mBeatsTextView;

    private boolean mCounting;
    private int mBeats;
    private int mSeconds;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views and assign to members
        mStartBeatButton = (Button) findViewById(R.id.buttonStartRepeat);
        mStopResetButton = (Button) findViewById(R.id.buttonStopReset);
        mBeatsTextView = (TextView) findViewById(R.id.textViewCount);

        // Default settings
        mStartBeatButton.setText(R.string.start_button);
        mStopResetButton.setText(R.string.stop_button);
        mStopResetButton.setEnabled(false);
        mCounting = false;
        mBeats = 0;
        updateBeats();

        mHandler = new Handler();
        mSeconds = 0;

        // Click events
        mStartBeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCounting) {
                    count();
                }
                else {
                    startCounting();
                }
            }
        });

        mStopResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCounting) {
                    stopCounting();
                }
                else {
                    resetCounting();
                }
            }
        });
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void startCounting() {
        mStartBeatButton.setText(R.string.beat_button);
        mStopResetButton.setEnabled(true);
        mCounting = true;
        count();
        mHandlerUpdate.run();
    }

    Runnable mHandlerUpdate = new Runnable() {
        @Override
        public void run() {
            updateBeats();
            mHandler.postDelayed(mHandlerUpdate, 1000);
        }
    };

    private void count() {
        ++mBeats;
    }

    private void stopCounting() {
        mStartBeatButton.setEnabled(false);
        mStartBeatButton.setText(R.string.start_button);
        mStopResetButton.setText(R.string.reset_button);
        mCounting = false;
        mHandler.removeCallbacks(mHandlerUpdate);
    }

    private void resetCounting() {
        mStartBeatButton.setText(R.string.start_button);
        mStartBeatButton.setEnabled(true);
        mStopResetButton.setText(R.string.stop_button);
        mStopResetButton.setEnabled(false);
        mBeats = 0;
        mSeconds = 0;
        updateBeats();
    }

    private void updateBeats() {
        ++mSeconds;
        double bpm = ((double)mBeats / (double)mSeconds) * 60.0;
        DecimalFormat df = new DecimalFormat("#");
        //df.setRoundingMode(RoundingMode.CEILING);
        mBeatsTextView.setText(df.format(bpm));
    }
}
