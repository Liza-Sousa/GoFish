package com.example.gofish;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int[] cardArray = new int[52];
    int[] playerHand = new int[10];
    int[] opponentHand = new int[10];

    ImageButton card1;
    ImageButton card2;
    ImageButton card3;
    ImageButton card4;
    ImageButton card5;
    ImageButton card6;
    ImageButton card7;
    ImageButton card8;
    ImageButton card9;
    ImageButton card10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        shuffleDeck();
        dealCards();
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

    public void shuffleDeck(){
        Random rgen = new Random();  // Random number generator
     //initialize deck from 1-52
        for(int j = 1; j <= cardArray.length; j++) {
            cardArray[j] = j;
        }
        //randomize order of cards
        for (int i=0; i<cardArray.length; i++) {
            int randomPosition = rgen.nextInt(cardArray.length);
            int temp = cardArray[i];
            cardArray[i] = cardArray[randomPosition];
            cardArray[randomPosition] = temp;
        }
        return;
    }

    public void dealCards() {
        //deal even cards to player and odd cards to computer
        int j = 0;
        int k = 0;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                playerHand[j] = cardArray[i];
                j++;
            } else {
                opponentHand[k] = cardArray[i];
                k++;
            }
        }
        //set bottom row of cards to be -1 so they're invalid
        for (int i = 5; i < 10; i++) {
            playerHand[i] = -1;
            opponentHand[i] = -1;
        }


        // use AssetManager to load next image from assets folder
        AssetManager assets = getActivity().getAssets();
        String cards = "cards";
        String nextCard = "";
        for (int i = 0; i < 5; i++) {
            nextCard = Integer.toString(playerHand[i]);
            // get an InputStream to the asset representing the next flag
            // and try to use the InputStream
            try (InputStream stream =
                         assets.open(cards + "/" + nextCard + ".png")) {
                // load the asset as a Drawable and display on the flagImageView
                Drawable currentCard = Drawable.createFromStream(stream, nextCard);
                card1.setImageDrawable(currentCard);
            } catch (IOException exception) {
                //Log.e(TAG, "Error loading " + nextCard, exception);
            }
        }
    }
}
