package com.awesome.scottquach.proximitypush_upcounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.Touch;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;


public class StartMenu extends Activity {

    private Button upButton;
    private Button downButton;
    private TextView goalTextView;

    private int goalValue;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);


        goalTextView = (TextView) findViewById(R.id.goalTextView);

        sharedPref = getSharedPreferences("goalFile", MODE_PRIVATE);
        editor = sharedPref.edit();

        goalValue = sharedPref.getInt("goalValue", 0);
        goalTextView.setText(String.valueOf(goalValue));

        upButton = (Button) findViewById(R.id.upButton);
        downButton = (Button) findViewById(R.id.downButton);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1876787092384518~2446206781");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //When button is held down, incrase faster using handler
        upButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null){
                            return true;
                        }
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null){
                            return true;
                        }
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    goalValue++;
                    editor.putInt("goalValue", goalValue);
                    editor.apply();
                    goalTextView.setText(String.valueOf(goalValue));
                    mHandler.postDelayed(this, 100);
                }
            };

        });

        //When button is held down, decrease faster using handler
        downButton.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;
            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 500);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    if(goalValue >=1) {
                        goalValue--;
                    }
                    editor.putInt("goalValue", goalValue);
                    editor.apply();
                    goalTextView.setText(String.valueOf(goalValue));
                    mHandler.postDelayed(this, 500);
                }
            };

        });

    }

    /*button
    clicks
     */


    //go to main activity
    public void startPushUpStarted(View view) {
        Intent openMainActivity = new Intent(this,MainActivity.class);
        startActivity(openMainActivity);
    }

    //incrase goal value and save
    public void upButtonClicked(View view) {
        goalValue++;
        editor.putInt("goalValue", goalValue);
        editor.apply();
        goalTextView.setText(String.valueOf(goalValue));
    }

    //decrase goal value and save
    public void downButtonClicked(View view) {
        if(goalValue >=1) {
            goalValue--;
        }
        editor.putInt("goalValue", goalValue);
        editor.apply();
        goalTextView.setText(String.valueOf(goalValue));
    }

    //open settings page
    public void openSettingsPage(View view) {
        Intent openSettings = new Intent(StartMenu.this,SettingsActivity.class);
        startActivity(openSettings);
    }

    //open log of previous sessions page
    public void logButtonClicked(View view) {
        Intent openSaves = new Intent(StartMenu.this,Saves.class);
        startActivity(openSaves);
    }
}
