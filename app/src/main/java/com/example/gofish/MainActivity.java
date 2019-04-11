package com.example.gofish;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton goFishButton;
    ImageButton hangmanButton;
    ImageButton tictactoeButton;
    String TAG = "main";
    private boolean phoneDevice = true; // used to force portrait mode

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // determine screen size
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        // if device is a tablet, set phoneDevice to false
        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
                screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE)
            phoneDevice = false; // not a phone-sized device

        // if running on phone-sized device, allow only portrait orientation
        if (phoneDevice)
            setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        goFishButton = findViewById(R.id.goFish);
        hangmanButton = findViewById(R.id.hangman);
        tictactoeButton = findViewById(R.id.tictactoe);

        goFishButton.setOnClickListener(goFishListen);
        hangmanButton.setOnClickListener(hangmanListen);
        tictactoeButton.setOnClickListener(tictactoeListen);
        Log.d(TAG, "onCreate: main started");

    }
    OnClickListener goFishListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to go fish
            Intent myIntent = new Intent(MainActivity.this, GoFishActivity.class);
            startActivity(myIntent);

        }
    };
    OnClickListener hangmanListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to hangman
        }
    };
    OnClickListener tictactoeListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to tictac
        }
    };
}
