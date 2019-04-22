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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ImageButton goFishButton;
    ImageButton hangmanButton;
    ImageButton tictactoeButton;
    TextView goFishLabel;
    TextView hangmanLabel;
    TextView tictactoeLabel;
    int hangmanScore = 15;
    int tictacScore = 30;

    String TAG = "main";
    int score = 0;
    private boolean phoneDevice = true; // used to force portrait mode

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent mIntent = getIntent();
        score = mIntent.getIntExtra("score", 0);


        Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_SHORT).show();

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

        goFishLabel = findViewById(R.id.goFishLabel);
        hangmanLabel = findViewById(R.id.hangmanLabel);
        tictactoeLabel = findViewById(R.id.tictactoeLabel);

        goFishButton.setOnClickListener(goFishListen);
        hangmanButton.setOnClickListener(hangmanListen);
        tictactoeButton.setOnClickListener(tictactoeListen);
        Log.d(TAG, "onCreate: main started");

        if(score<hangmanScore) {
            hangmanButton.setClickable(false);
            hangmanButton.setVisibility(View.INVISIBLE);
            hangmanLabel.setText("???");
        }
        else{
            hangmanButton.setClickable(true);
            hangmanButton.setVisibility(View.VISIBLE);
            hangmanLabel.setText("Hangman");
        }

        if(score<tictacScore) {
            tictactoeButton.setClickable(false);
            tictactoeButton.setVisibility(View.INVISIBLE);
            tictactoeLabel.setText("???");
        }
        else{
            tictactoeButton.setClickable(true);
            tictactoeButton.setVisibility(View.VISIBLE);
            tictactoeLabel.setText("Tic Tac Toe");
        }

    }
    OnClickListener goFishListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to go fish
            Intent myIntent = new Intent(MainActivity.this, GoFishActivity.class);
            myIntent.putExtra("score", score);
            startActivity(myIntent);

        }
    };
    OnClickListener hangmanListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to hangman
            Intent myIntent = new Intent(MainActivity.this, HangManActivity.class);
            myIntent.putExtra("score", score);
            startActivity(myIntent);

        }
    };
    OnClickListener tictactoeListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to tictac
            Intent myIntent = new Intent(MainActivity.this, DoodleActivity.class);
            myIntent.putExtra("score", score);
            startActivity(myIntent);

        }
    };
}
