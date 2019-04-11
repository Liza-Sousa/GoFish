package com.example.gofish;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View.OnClickListener;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class GoFishActivity extends AppCompatActivity {

    int[] cardArray = new int[52];
    int[] playerHand = new int[10];
    int[] opponentHand = new int[10];
    TextView playerPairTextView;
    TextView opponentPairTextView;
    TextView cardsRemainingTextView;
    ImageButton card[] = new ImageButton[10];
    ImageView cardWanted;
    private Handler handler; // used to delay
    int playerPair = 0;
    int opponentPair = 0;
    int cardsRemaining = 42;
    String TAG = "GoFish";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gofish);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        playerPairTextView = findViewById(R.id.playerPairs) ;
        opponentPairTextView = findViewById(R.id.opponentPairs) ;
        cardsRemainingTextView = findViewById(R.id.cardsLeft);

        handler = new Handler();
        card[0] = findViewById(R.id.card1);
        card[1] = findViewById(R.id.card2);
        card[2] = findViewById(R.id.card3);
        card[3] = findViewById(R.id.card4);
        card[4] = findViewById(R.id.card5);
        card[5] = findViewById(R.id.card6);
        card[6] = findViewById(R.id.card7);
        card[7] = findViewById(R.id.card8);
        card[8] = findViewById(R.id.card9);
        card[9] = findViewById(R.id.card10);
        cardWanted = findViewById(R.id.cardWanted);
        for (int i = 0; i<10; i++) {
            card[i].setClickable(false);
            setOnClick(card[i], i);
        }
        shuffleDeck();
        dealCards();
        //goFish(playerHand, 1);
        //opponentTurn();
    }

    private void setOnClick(final ImageButton btn, final int id){
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int getPair = 0;
                AssetManager assets = getAssets();
                for(int i = 0; i<10; i++) {
                    if(opponentHand[i] > 0 && playerHand[id] > 0 && opponentHand[i] % 13 == playerHand[id] % 13) {
                        // found a pair
                        Toast.makeText(getApplicationContext(), "You got a pair", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onclick: pos "+ Integer.toString(id) + " card " + Integer.toString(playerHand[id]) + " opponentcard " + Integer.toString(opponentHand[i]));

                        try (InputStream stream =
                                     assets.open( "cards/" + "empty" + ".png")) {
                            // load the asset as a Drawable and display on the flagImageView
                            Drawable currentCard = Drawable.createFromStream(stream, "empty");
                            card[id].setImageDrawable(currentCard);
                            card[id].setClickable(false);
                            playerHand[id] = -1;
                        } catch (IOException exception) {
                            Log.e(TAG,"Error loading " + "empty", exception);
                        }
                        // load the next flag after a 2-second delay
                        playerPair++;
                        playerPairTextView.setText(""+Integer.toString(playerPair));
                        getPair = 1;
                        handler.postDelayed(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                }, 2000); // 2000 milliseconds for 2-second delay
                        if(isEmpty(1) == 1)
                            goFish(playerHand, 1);
                    }
                }
                if(getPair == 0) {
                    Toast.makeText(getApplicationContext(), "You did not get a match", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onclick: pos "+ Integer.toString(id) + " card " + Integer.toString(playerHand[id]));
                    goFish(playerHand, 1);
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay

                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gofish, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch (item.getItemId()) {
            case R.id.switchGame:
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shuffleDeck(){
        Random rgen = new Random();  // Random number generator
     //initialize deck from 1-52
        for(int j = 0; j < cardArray.length; j++) {
            cardArray[j] = j +1;
        }
        //randomize order of cards
        for (int i=0; i<cardArray.length; i++) {
            int randomPosition = rgen.nextInt(cardArray.length);
            int temp = cardArray[i];
            cardArray[i] = cardArray[randomPosition];
            cardArray[randomPosition] = temp;
        }
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
            cardArray[i] = -1;
        }
        //set bottom row of cards to be -1 so they're invalid
        for (int i = 5; i < 10; i++) {
            playerHand[i] = -1;
            opponentHand[i] = -1;
            // card[i].setClickable(false);
        }

        // use AssetManager to load next image from assets folder
        AssetManager assets = getAssets();
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
                card[i].setImageDrawable(currentCard);
                card[i].setClickable(true);
            } catch (IOException exception) {
                Log.e(TAG, "Error loading " + nextCard, exception);
            }
        }
        for (int i = 5; i < 10; i++) {
            // get an InputStream to the asset representing the next flag
            // and try to use the InputStream
            try (InputStream stream =
                         assets.open(cards + "/" + "empty" + ".png")) {
                // load the asset as a Drawable and display on the flagImageView
                Drawable currentCard = Drawable.createFromStream(stream, "empty");
                card[i].setImageDrawable(currentCard);
                card[i].setClickable(false);
            } catch (IOException exception) {
                Log.e(TAG,"Error loading " + "empty", exception);
            }
        }
        checkPlayerInitialMatch(playerHand);
        checkOpponentInitialMatch(opponentHand);


    }
    public int checkPlayerInitialMatch(int[] array){
        int ret = 0;
        int empty = 0;
        for(int i = 0; i < array.length - 1; i++){
            for(int j = i+1; j<array.length; j++){
                if (array[i]!=-1 && array[j]!=-1 && array[i]%13 == array[j]%13){
                    Toast.makeText(getApplicationContext(), "You got a pair", Toast.LENGTH_SHORT).show();

                    playerPair++;
                    playerPairTextView.setText(""+Integer.toString(playerPair));
                    Log.d(TAG, "checkPlayerInitialMatch: card1 " + Integer.toString(playerHand[i]) + " card2 " + Integer.toString(playerHand[j]));
                    Log.d(TAG, "checkPlayerInitialMatch: pos1 " + Integer.toString(i) + " pos2 " + Integer.toString(j));

                    AssetManager assets = getAssets();
                    String cards = "cards";
                    String nextCard = "";
                    try (InputStream stream =
                                 assets.open(cards + "/" + "empty" + ".png")) {
                        // load the asset as a Drawable and display on the flagImageView
                        Drawable currentCard = Drawable.createFromStream(stream, "empty");
                        card[i].setImageDrawable(currentCard);
                        card[i].setClickable(false);
                        card[j].setImageDrawable(currentCard);
                        card[j].setClickable(false);

                    } catch (IOException exception) {
                        Log.e(TAG,"Error loading " + "empty", exception);
                    }
                    playerHand[j] = -1;
                    playerHand[i] = -1;
                    ret = 1;
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay

                }
            }

        }
        return ret;
    }

    public int checkOpponentInitialMatch(int[] array){
        int ret = 0;
        for(int i = 0; i < array.length - 1; i++){
            for(int j = i+1; j<array.length; j++){
                if (array[i]%13 == array[j]%13 && array[i]!=-1 && array[j]!=-1){
                    Toast.makeText(getApplicationContext(), "Opponent got a pair", Toast.LENGTH_SHORT).show();
                    opponentPair++;
                    opponentPairTextView.setText(""+Integer.toString(opponentPair));
                    Log.d(TAG, "checkOpponentInitialMatch: card1 " + Integer.toString(opponentHand[i]) + " card2 " + Integer.toString(opponentHand[j]));
                    Log.d(TAG, "checkOpponentInitialMatch: pos1 " + Integer.toString(i) + " pos2 " + Integer.toString(j));


                    opponentHand[j] = -1;
                    opponentHand[i] = -1;
                    ret = 1;
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay

                }
            }
        }
        return ret;
    }

    public void opponentTurn(){
        for(int i = 0; i<10; i++) {
            card[i].setClickable(false);
        }
        Random rgen = new Random();
        int cardPicked = rgen.nextInt(10);
        while(opponentHand[cardPicked]==-1){
            cardPicked = rgen.nextInt(10);
        }
        Log.d(TAG, "opponentTurn: cardPicked " + Integer.toString(cardPicked) );
        int baseValue = opponentHand[cardPicked]%13;
        Log.d(TAG, "opponentTurn: baseValue" + Integer.toString(baseValue + 1) );

        AssetManager assets = getAssets();
        String cards = "bigNum";
        String nextCard = Integer.toString(baseValue+1);

        // get an InputStream to the asset representing the next flag
        // and try to use the InputStream
        try (InputStream stream =
                     assets.open(cards + "/" + "big" + nextCard + ".jpg")) {
            // load the asset as a Drawable and display on the flagImageView
            Drawable currentCard = Drawable.createFromStream(stream, nextCard);
            cardWanted.setImageDrawable(currentCard);
        } catch (IOException exception) {
            Log.e(TAG, "Error loading " + nextCard, exception);
        }
        int getPair = 0;
        for(int i = 0; i<10; i++) {
            if(playerHand[i] > 0 && opponentHand[cardPicked] > 0  && playerHand[i] % 13 == opponentHand[cardPicked]%13) {
                // found a pair
                Toast.makeText(getApplicationContext(), "Opponent got a pair", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "opponentTurn: got a match pairs number" + Integer.toString(opponentPair + 1));
                Log.d(TAG, "opponentTurn: got a match pos "+ Integer.toString(cardPicked) + " opponentCard " + Integer.toString(opponentHand[cardPicked]) + " playercard " + Integer.toString(playerHand[i]));


                try (InputStream stream =
                             assets.open( "cards/" + "empty" + ".png")) {
                    // load the asset as a Drawable and display on the flagImageView
                    Drawable currentCard = Drawable.createFromStream(stream, "empty");
                    card[i].setImageDrawable(currentCard);
                    card[i].setClickable(false);
                    playerHand[i] = -1;
                    opponentHand[cardPicked] = -1;
                } catch (IOException exception) {
                    Log.e(TAG,"Error loading " + "empty", exception);
                }
                // load the next flag after a 2-second delay
                opponentPair++;
                opponentPairTextView.setText(""+Integer.toString(opponentPair));
                getPair = 1;
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 2000); // 2000 milliseconds for 2-second delay
                if(isEmpty(0) == 1)
                    goFish(opponentHand, 0);
                if(isEmpty(1) == 1)
                    goFish(playerHand, 1);

            }
        }
        if(getPair == 0) {
            Toast.makeText(getApplicationContext(), "Opponent did not get a match", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "opponentTurn: no match");

            handler.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                        }
                    }, 2000); // 2000 milliseconds for 2-second delay
            goFish(opponentHand, 0);
            for(int i = 0; i<10; i++) {
                if(playerHand[i] != -1)
                    card[i].setClickable(true);
            }
        } else {
            opponentTurn();
        }
    }

    public void goFish(int[] array, int player) {
        Random rgen = new Random();
        //no more cards left to get
        if (cardsRemaining == 0) {
            if (playerPair > opponentPair) {
                //print out You Win!
                Toast.makeText(getApplicationContext(), "You won! Congratulations!", Toast.LENGTH_SHORT).show();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 5000); // 2000 milliseconds for 2-second delay

            } else {
                //print out you lose :(
                Toast.makeText(getApplicationContext(), "You lost! Better luck next time.", Toast.LENGTH_SHORT).show();
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                            }
                        }, 5000); // 2000 milliseconds for 2-second delay
            }
        //cards still available to get
        } else {
            for (int j = 0; j < array.length; j++) {
                //if the random value of the cardArray has already been used, keep trying until a new value is found
                int ranNum = 0;
                if (array[j] == -1) {
                    while (array[j] == -1) {
                        ranNum = rgen.nextInt(cardArray.length);
                        array[j] = cardArray[ranNum];
                    }
                    cardsRemaining--;
                    Log.d(TAG, "picked card " + Integer.toString(cardArray[ranNum]) + " player " + Integer.toString(player));

                    cardArray[ranNum] = -1;
                    cardsRemainingTextView.setText(""+Integer.toString(cardsRemaining));

                    if(player==1) {

                        // use AssetManager to load next image from assets folder
                        AssetManager assets = getAssets();
                        String cards = "cards";
                        String nextCard = "";

                        nextCard = Integer.toString(array[j]);
                        // get an InputStream to the asset representing the next flag
                        // and try to use the InputStream
                        try (InputStream stream =
                                     assets.open(cards + "/" + nextCard + ".png")) {
                            // load the asset as a Drawable and display on the flagImageView
                            Drawable currentCard = Drawable.createFromStream(stream, nextCard);
                            card[j].setImageDrawable(currentCard);
                            card[j].setClickable(true);
                        } catch (IOException exception) {
                            Log.e(TAG, "Error loading " + nextCard, exception);
                        }
                    }
                    j = array.length;

                }

            }
            if (player == 1) {
                if (checkPlayerInitialMatch(playerHand) == 0) {
                    opponentTurn();
                } else ;{
                    if(isEmpty(player) == 1)
                        goFish(playerHand, player);
                }
            } else {
                if (checkOpponentInitialMatch(opponentHand) == 1) {
                    if(isEmpty(player) == 1)
                        goFish(opponentHand, player);
                    opponentTurn();
                }
            }

        }
    }

    private int isEmpty(int player) {
        int isEmpty = 1;
        if (player == 1) {
            for(int i = 0; i< 10; i++) {
                if (playerHand[i] >0)
                    isEmpty = 0;
            }
        } else {
            for(int i = 0; i< 10; i++) {
                if (opponentHand[i] >0)
                    isEmpty = 0;
            }
        }
        return isEmpty;
    }
}

