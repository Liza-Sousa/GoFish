package com.example.gofish;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    int score = 0;
    int cardValue;
    int cardPicked;
    int count = 10;
//    AssetManager assets = getAssets();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gofish);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent mIntent = getIntent();
        score = mIntent.getIntExtra("score", 0);
        //Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_LONG).show();
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
            playerHand[i] = -1;
            opponentHand[i] = -1;
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
                        matchMessage();
                        //Toast.makeText(getApplicationContext(), "You got a pair", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onclick: pos "+ Integer.toString(id) + " card " + Integer.toString(playerHand[id]) + " opponentcard " + Integer.toString(opponentHand[i]));

                        try (InputStream stream =
                                     assets.open( "cards/" + "empty" + ".png")) {
                            // load the asset as a Drawable and display on the flagImageView
                            Drawable currentCard = Drawable.createFromStream(stream, "empty");
                            card[id].setImageDrawable(currentCard);
                            card[id].setClickable(false);
                            playerHand[id] = -1;
                            opponentHand[i]=-1;
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
                        //if user doesn't have cards and there are still cards remaining, goFish
                        if(isEmpty(1) == 1 && cardsRemaining!=0)
                            goFish(playerHand, 1);
                       //if the user doesn't have any cards and the pile is empty, game over
                        if(isEmpty(1) == 0 && cardsRemaining==0){
                            /*score = score + playerPair;
                            Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_SHORT).show();*/
                            if (playerPair > opponentPair) {
                                //print out You Win!
                                endMessage("You win! Congratulations! Play Again?");
                            } else {
                                //print out you lose :(
                                endMessage("You lose, but you'll get it next time! PLay Again?");
                            }
                        }
                    }
                }
                if(getPair == 0) {
                    //goFish dialog
                    goFishMessage();
                    //Toast.makeText(getApplicationContext(), "You did not get a match!!! :(", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onclick: pos "+ Integer.toString(id) + " card " + Integer.toString(playerHand[id]));
                    //goFish(playerHand, 1);
                    handler.postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                }
                            }, 2000); // 2000 milliseconds for 2-second delay
                    //opponentTurn();

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
                switchGame();
                return true;
            case R.id.restartGame:
                restartGame();
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartGame() {
        for (int i = 0; i<10; i++) {
            card[i].setClickable(false);
            playerHand[i] = -1;
            opponentHand[i] = -1;
            setOnClick(card[i], i);
        }
        score = score + playerPair;
        playerPair = 0;
        playerPairTextView.setText("" + Integer.toString(playerPair));
        opponentPair = 0;
        opponentPairTextView.setText("" + Integer.toString(opponentPair));

        cardsRemaining = 42;
        cardsRemainingTextView.setText("" + Integer.toString(cardsRemaining));

        count = 10;
        // empty players' hand
        shuffleDeck();
        dealCards();
    }
    public void shuffleDeck(){
        Random rgen = new Random();  // Random number generator
     //initialize deck from 1-52
        for(int j = 0; j < cardArray.length; j++) {
            cardArray[j] = j +1;
        }

/*        //randomize order of cards
        for (int i=0; i<cardArray.length; i++) {
            int randomPosition = rgen.nextInt(cardArray.length);
            int temp = cardArray[i];
            cardArray[i] = cardArray[randomPosition];
            cardArray[randomPosition] = temp;
        }*/
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
            //change total cards here
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
//        int cardValue = 0;
        String cardString = "";
        boolean matchCheck = false;
        for(int i = 0; i<10; i++) {
            card[i].setClickable(false);
        }
        //if opponent has no cards and there aren't any cards to draw, end game
        if(cardsRemaining==0 && isEmpty(0)==1){
            /*score = score + playerPair;
            Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_SHORT).show();*/
            if (playerPair > opponentPair) {
                //print out You Win!
                endMessage("You win! Congratulations! Play Again?");
            } else {
                //print out you lose :(
                endMessage("You lose, but you'll get it next time! PLay Again?");
            }
        }

        Random rgen = new Random();
        cardPicked = rgen.nextInt(10);
        while(opponentHand[cardPicked]==-1){
            cardPicked = rgen.nextInt(10);
        }
        cardValue = (opponentHand[cardPicked] % 13);
        switch(cardValue){
            case 1:
                cardString = "A";
                break;
            case 11:
                cardString = "J";
                break;
            case 12:
                cardString= "Q";
                break;
            case 0:
                cardString = "K";
                break;
            default:
                cardString = Integer.toString(cardValue);

        }
        Log.d(TAG, "opponentTurn: cardPicked " + Integer.toString(cardPicked) );
        int baseValue = opponentHand[cardPicked]%13;
        Log.d(TAG, "opponentTurn: baseValue" + Integer.toString(baseValue + 1) );

       AssetManager assets = getAssets();
        String cards = "bigNum";
        String nextCard = Integer.toString(baseValue);

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
        /////////////////////////////////////
        // create a new AlertDialog and set its message
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage("Do you have a " + cardString);

        // configure the negative (CANCEL) Button
        confirmBuilder.setNegativeButton("Nope, go fish!",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean matchCheck = false;
                        AssetManager assets = getAssets();
                        for (int i = 0; i < 10; i++) {
                            if ((playerHand[i] % 13)  == cardValue) {
                                // found a pair
                                matchCheck= true;
                                Toast.makeText(getApplicationContext(), "Liar! You have that card!", Toast.LENGTH_LONG).show();
                                setBlankCard(i);
                                playerHand[i] = -1;
                                opponentHand[cardPicked] = -1;
                                opponentPair++;
                                opponentPairTextView.setText(""+Integer.toString(opponentPair));
                                if(isEmpty(0) == 1)
                                    goFish(opponentHand, 0);
                                if(isEmpty(1) == 1)
                                    goFish(playerHand, 1);
                                Toast.makeText(getApplicationContext(), "Opponent got a pair even though you tried to lie about it", Toast.LENGTH_SHORT).show();
                                if(isEmpty(0)==0 && cardsRemaining!=0)
                                    opponentTurn();
                                if(isEmpty(0)==1 && cardsRemaining==0){
                                  /*  score = score + playerPair;
                                    Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_SHORT).show();*/
                                    if (playerPair > opponentPair) {
                                        //print out You Win!
                                        endMessage("You win! Congratulations! Play Again?");
                                    } else {
                                        //print out you lose :(
                                        endMessage("You lose, but you'll get it next time! Play Again?");
                                    }
                                }
                            }
                        }
                        if(matchCheck==false) {
                            for (int i = 0; i < 10; i++) {
                                if(playerHand[i] > 0) {
                                    card[i].setClickable(true);
                                }
                            }
                            goFish(opponentHand, 0);
                            //return;
                        }


                    }
                });

        // configure the positive (DELETE) Button
        confirmBuilder.setPositiveButton("Yes!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean matchCheck = false;
                        for (int i = 0; i < 10; i++) {
                            if ((playerHand[i] % 13)  == cardValue) {
                                // found a pair
                                matchCheck = true;
                                setBlankCard(i);
                                playerHand[i] = -1;
                                opponentHand[cardPicked] = -1;
                                opponentPair++;
                                opponentPairTextView.setText("" + Integer.toString(opponentPair));
                                /*if (isEmpty(0) == 1)
                                    //comes back here
                                    goFish(opponentHand, 0);
                                if (isEmpty(1) == 1)
                                    goFish(playerHand, 1);
                                    */
                                Toast.makeText(getApplicationContext(), "Opponent got a pair!!! :(", Toast.LENGTH_LONG).show();
                               /* if(isEmpty(1)==0 && cardsRemaining!=0)
                                    goFish(playerHand, 1);*/
                                if(isEmpty(0)==0 && cardsRemaining!=0)
                                    opponentTurn();
                                else {
                                    //comes back here
                                    Toast.makeText(getApplicationContext(), "Going fishing since hand is empty", Toast.LENGTH_LONG).show();
                                    goFish(opponentHand, 0);
                                    //opponentTurn();
                                }

                                // test1 opponentTurn();
                            }
                        }
                        if (matchCheck == false) {
                            Toast.makeText(getApplicationContext(), "Oops! You actually don't have that card!", Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < 10; i++) {
                                if(playerHand[i] > 0) {
                                    card[i].setClickable(true);
                                }
                            }
                            goFish(opponentHand, 0);
                            return;
                        }

                    }
                }
        );

        confirmBuilder.create().show(); // display AlertDialog
    }

    public void goFish(int[] array, int player) {
        Random rgen = new Random();
        int emptyCheck = 0;
        int cardGoFished = 0;
        //no more cards left to get
        emptyCheck = isEmpty(player);
        if (cardsRemaining == 0) {
            /*score = score + playerPair;
            Toast.makeText(getApplicationContext(), "Score is now " + score, Toast.LENGTH_SHORT).show();*/
            if (playerPair > opponentPair) {
                //print out You Win!
                endMessage("You win! Congratulations! Play Again?");
            } else {
                //print out you lose :(
                endMessage("You lose, but you'll get it next time! Play Again?");
            }
        //cards still available to get
        } else {
            for (int j = 0; j < array.length; j++) {
                //if the random value of the cardArray has already been used, keep trying until a new value is found
                int ranNum = 0;
                if (array[j] == -1) {
                    while (array[j] == -1) {
                        //ranNum = rgen.nextInt(cardArray.length);
                        array[j] = cardArray[count];
                       count++;
                    }
                    cardsRemaining--;
                    cardGoFished=array[j];
                    Log.d(TAG, "picked card " + Integer.toString(cardArray[ranNum]) + " player " + Integer.toString(player));

                    //change available cards here
                    cardArray[ranNum] = -1;
                    cardsRemainingTextView.setText(""+Integer.toString(cardsRemaining));

                    if(player==1) {

                        // use AssetManager to load next image from assets folder
                        AssetManager assets = getAssets();
                        String cards = "cards";
                        String nextCard = "";

                        nextCard = Integer.toString(array[j]);
                        Toast.makeText(getApplicationContext(), "You drew a " + (array[j]%13), Toast.LENGTH_LONG).show();
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
            //player is real person
            if (player == 1) {
                if (checkPlayerInitialMatch(playerHand) == 0) {
                    opponentTurn();
                } else {
                    Toast.makeText(getApplicationContext(), "You got a pair of  " + cardGoFished%13 , Toast.LENGTH_SHORT).show();
                    if(isEmpty(player) == 1)
                        goFish(playerHand, player);
                }
            }
            //player is computer
            else {
                if (checkOpponentInitialMatch(opponentHand) == 0) {
                    for (int j = 0; j < playerHand.length; j++) {
                        if (playerHand[j] > 0) {
                            card[j].setClickable(true);
                        }
                    }
                }

                else {
                    Toast.makeText(getApplicationContext(), "Opponent got a pair of  " + cardGoFished%13 , Toast.LENGTH_SHORT).show();
                    if(isEmpty(player) == 1)
                        goFish(opponentHand, player);
                    else{
                        opponentTurn();
                    }
                }
/*                if(emptyCheck== 1){
                    opponentTurn();
                }*/
                /*if (checkOpponentInitialMatch(opponentHand) == 1) {
                    if(isEmpty(player) == 1)
                        goFish(opponentHand, player);
                    opponentTurn();
                }
                return;*/
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

    private void setBlankCard(int i){
       AssetManager assets = getAssets();
        try (InputStream stream =
                     assets.open( "cards/" + "empty" + ".png")) {
            // load the asset as a Drawable and display on the flagImageView
            Drawable currentCard = Drawable.createFromStream(stream, "empty");
            card[i].setImageDrawable(currentCard);
            card[i].setClickable(false);
        } catch (IOException exception) {
            Log.e(TAG,"Error loading " + "empty", exception);
        }
    }
    private void goFishMessage(){
        //Toast.makeText(getApplicationContext(), "Entered message function", Toast.LENGTH_LONG).show();
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage("Go Fish!");
        // configure the negative (CANCEL) Button
        confirmBuilder.setNegativeButton("okay :(",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        goFish(playerHand, 1);
                    }
                });
        confirmBuilder.create().show(); // display AlertDialog
    }

    private void matchMessage(){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage("You got a match! Go again!");
        // configure the negative (CANCEL) Button
        confirmBuilder.setNegativeButton("hooray :)", null);
        confirmBuilder.create().show(); // display AlertDialog
    }

    private void endMessage(String s){
        AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(this);
        confirmBuilder.setMessage(s);
        // configure the negative (CANCEL) Button
        confirmBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("gofish", "onClick: switch");
                        //switch back to main
                        switchGame();
                    }
                });

        // configure the positive (DELETE) Button
        confirmBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.d("gofish", "onClick: restart");
                        restartGame();
                    }
                });
        confirmBuilder.create().show(); // display AlertDialog
    }

    private void switchGame(){
        score = score + playerPair;
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("score", score);
        startActivity(myIntent);
    }
}

