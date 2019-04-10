package com.example.gofish;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {
    ImageButton goFishButton;
    ImageButton hangmanButton;
    ImageButton tictactoeButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goFishButton = findViewById(R.id.goFish);
        hangmanButton = findViewById(R.id.hangman);
        tictactoeButton = findViewById(R.id.tictactoe);

        goFishButton.setOnClickListener(goFishListen);
        hangmanButton.setOnClickListener(hangmanListen);
        tictactoeButton.setOnClickListener(tictactoeListen);

    }
    OnClickListener goFishListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //switch to go fish
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
