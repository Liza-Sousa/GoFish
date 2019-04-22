// HangManActivityFragment.java
// Contains the Flag Quiz logic
package com.example.gofish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HangManActivityFragment extends Fragment {
   // String used when logging error messages
   private static final String TAG = "FlagQuiz Activity";

   private static final int WORDS_IN_QUIZ = 10;

   private List<String> fileNameList; // hangman file names
   private List<String> wordsList = null; // words in current quiz

    private String correctAnswer; // correct country for the current word
   private int totalGuesses; // number of guesses made
   private int correctAnswers; // number of correct guesses
   private int guessRows; // number of rows displaying guess Buttons
   private SecureRandom random; // used to randomize the quiz
   private Handler handler; // used to delay loading next images
   private Animation shakeAnimation; // animation for incorrect guess
   private int hangIndex = 0;
   int score = 0;

   private LinearLayout quizLinearLayout; // layout that contains the quiz
   private TextView guessWordTextView; // shows current question #
   private ImageView manImageView; // displays a man
   private LinearLayout row1LinearLayouts; // rows of word textviews
   private LinearLayout row2LinearLayouts; // rows of answer textview and Buttons

   private TextView letter1;
   private TextView letter2;
   private TextView letter3;
   private TextView letter4;
   private TextView letter5;
   private EditText guessLetter;
   private Button answer;  // guess a letter
   // configures the HangManActivityFragment when its View is created
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
      super.onCreateView(inflater, container, savedInstanceState);
      View view =
         inflater.inflate(R.layout.fragment_hangman, container, false);

      fileNameList = new ArrayList<>();
      wordsList = new ArrayList<>(7);
       wordsList.add("hello");
       wordsList.add("world");
       wordsList.add("globe");
       wordsList.add("chair");
       wordsList.add("table");
       wordsList.add("light");
       wordsList.add("happy");

       random = new SecureRandom();

      handler = new Handler();
      // load the shake animation that's used for incorrect answers
      shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
         R.anim.incorrect_shake);
      shakeAnimation.setRepeatCount(3); // animation repeats 3 times

      // get references to GUI components
      quizLinearLayout =
         (LinearLayout) view.findViewById(R.id.quizLinearLayout);
      guessWordTextView =
         (TextView) view.findViewById(R.id.guessWordTextView);
      manImageView = (ImageView) view.findViewById(R.id.manImageView);
      row1LinearLayouts =
         (LinearLayout) view.findViewById(R.id.row1LinearLayout);
      row2LinearLayouts =
         (LinearLayout) view.findViewById(R.id.row2LinearLayout);
      letter1 = (TextView) view.findViewById(R.id.letter1);
      letter2 = (TextView) view.findViewById(R.id.letter2);
      letter3 = (TextView) view.findViewById(R.id.letter3);
      letter4 = (TextView) view.findViewById(R.id.letter4);
      letter5 = (TextView) view.findViewById(R.id.letter5);
      guessLetter = (EditText) view.findViewById(R.id.guessLetter);
      answer = (Button) view.findViewById(R.id.answer);
      answer.setOnClickListener(answerButtonListener);
      AssetManager assets = getActivity().getAssets();

       try {
           String[] paths = assets.list("Hangman");

           for (String path : paths)
               fileNameList.add(path.replace(".png", ""));
       }
       catch (IOException exception) {
           Log.e(TAG, "Error loading image file names", exception);
       }

       return view; // return the fragment's view for display
   }

   public int getScore(){
       return score;
   }

    public void setScore(int i){
        score = i;

    }


   // set up and start the next quiz
   public void resetQuiz(String s) {
      // use AssetManager to get image file names for enabled regions
      AssetManager assets = getActivity().getAssets();
      // wordsList.clear(); // empty list of image file names
       hangIndex = 0;

      correctAnswers = 0; // reset the number of correct answers made
      totalGuesses = 0; // reset the total number of guesses the user made
      wordsList.clear(); // clear prior list of quiz countries
       createList(s);
       /*wordsList.add("hello");
       wordsList.add("world");
       wordsList.add("globe");
       wordsList.add("chair");
       wordsList.add("table");
       wordsList.add("light");
       wordsList.add("happy");*/

       // get an InputStream to the asset representing the next flag
       // and try to use the InputStream
       try (InputStream stream =
                    assets.open("Hangman/" + fileNameList.get(hangIndex) + ".png")) {
           // load the asset as a Drawable and display on the flagImageView
           Drawable word = Drawable.createFromStream(stream, "hang man");
           manImageView.setImageDrawable(word);

           animate(false); // animate the flag onto the screen
       }
       catch (IOException exception) {
           Log.e(TAG, "Error loading Hangman image", exception);
       }

      loadNextWord(); // start the quiz by loading the first flag
   }
    public void createList(String s){
       wordsList.clear();
       switch(s){
           case "all":
               wordsList.add("hello");
               wordsList.add("world");
               wordsList.add("globe");
               wordsList.add("chair");
               wordsList.add("table");
               wordsList.add("light");
               wordsList.add("noooo");
               break;
           case "disney":
               wordsList.add("happy");
               wordsList.add("ariel");
               wordsList.add("dumbo");
               wordsList.add("goofy");
               wordsList.add("pluto");
               wordsList.add("daisy");
               wordsList.add("woody");
           break;
           case "pokemon":
               wordsList.add("eevee");
               wordsList.add("absol");
               wordsList.add("gible");
               wordsList.add("inkay");
               wordsList.add("pichu");
               wordsList.add("snivy");
               wordsList.add("zubat");
           break;
           case "animals":
               wordsList.add("horse");
               wordsList.add("tiger");
               wordsList.add("snake");
               wordsList.add("zebra");
               wordsList.add("mouse");
               wordsList.add("panda");
               wordsList.add("moose");
               break;

       }
    }
   // after the user guesses a correct flag, load the next flag
   private void loadNextWord() {
      // get file name of the next flag and remove it from the list
       Random rand = new Random();
       int ranNum = rand.nextInt(7);
      String nextWord = wordsList.get(ranNum);
      correctAnswer = nextWord; // update the correct answer
      letter1.setText("?");// clear answer
       letter2.setText("?");// clear answer
       letter3.setText("?");// clear answer
       letter4.setText("?");// clear answer
       letter5.setText("?");// clear answer
      // display current question number
      // use AssetManager to load next image from assets folder

   }

   // animates the entire quizLinearLayout on or off screen
   private void animate(boolean animateOut) {
      // prevent animation into the the UI for the first flag
      if (correctAnswers == 0)
         return;

      // calculate center x and center y
      int centerX = (quizLinearLayout.getLeft() +
         quizLinearLayout.getRight()) / 2; // calculate center x
      int centerY = (quizLinearLayout.getTop() +
         quizLinearLayout.getBottom()) / 2; // calculate center y

      // calculate animation radius
      int radius = Math.max(quizLinearLayout.getWidth(),
         quizLinearLayout.getHeight());

      Animator animator;

      // if the quizLinearLayout should animate out rather than in
      if (animateOut) {
         // create circular reveal animation
         animator = ViewAnimationUtils.createCircularReveal(
            quizLinearLayout, centerX, centerY, radius, 0);
         animator.addListener(
            new AnimatorListenerAdapter() {
               // called when the animation finishes
               @Override
               public void onAnimationEnd(Animator animation) {
                  loadNextWord();
               }
            }
         );
      }
      else { // if the quizLinearLayout should animate in
         animator = ViewAnimationUtils.createCircularReveal(
            quizLinearLayout, centerX, centerY, 0, radius);
      }

      animator.setDuration(500); // set animation duration to 500 ms
      animator.start(); // start the animation
   }

   // called when a guess Button is touched
   private OnClickListener answerButtonListener = new OnClickListener() {
      @Override
      public void onClick(View v) {
         char guess = guessLetter.getText().toString().toLowerCase().charAt(0);
         String answer = correctAnswer;
         ++totalGuesses; // increment number of guesses the user has made
          if (answer.indexOf(guess) >= 0) { // if the guess is correct

              // display correct answer in green text

             if (answer.indexOf(guess) == 0) {
                 int i = answer.indexOf(guess);
                 if (letter1.getText() == "?")
                     ++correctAnswers; // increment the number of correct answers
                 letter1.setText(""+guess);
                 letter1.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));

             }
             if (answer.indexOf(guess, 1) == 1) {
                 if (letter2.getText() == "?")
                     ++correctAnswers; // increment the number of correct answers
                 letter2.setText(""+guess);
                 letter2.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));
             }
             if (answer.indexOf(guess, 2) == 2) {
                 if (letter3.getText() == "?")
                     ++correctAnswers; // increment the number of correct answers
                 letter3.setText(""+guess);
                 letter3.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));
             }
             if (answer.indexOf(guess, 3) == 3) {
                 if (letter4.getText() == "?")
                     ++correctAnswers; // increment the number of correct answers
                 letter4.setText(""+guess);
                 letter4.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));
             }
             if (answer.indexOf(guess, 4) == 4) {
                 if (letter5.getText() == "?")
                     ++correctAnswers; // increment the number of correct answers
                 letter5.setText(""+guess);
                 letter5.setTextColor(
                         getResources().getColor(R.color.correct_answer,
                                 getContext().getTheme()));
             }

            // if the user has correctly identified FLAGS_IN_QUIZ flags
            if (correctAnswers == 5) {
                //score = score + (20 - totalGuesses);
                score = score+10;
                Toast.makeText(getContext(), "Winner Winner Chicken Dinner!", Toast.LENGTH_SHORT).show();
                // load the next flag after a 2-second delay
                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                animate(true); // animate the flag off the screen
                            }
                        }, 2000); // 2000 milliseconds for 2-second delay

                resetQuiz("all");
            }
         }
         else { // answer was incorrect
            manImageView.startAnimation(shakeAnimation); // play shake
             // use AssetManager to load next image from assets folder
             AssetManager assets = getActivity().getAssets();
             hangIndex++;
             // get an InputStream to the asset representing the next flag
             // and try to use the InputStream
             try (InputStream stream =
                          assets.open("Hangman/" + fileNameList.get(hangIndex) + ".png")) {
                 // load the asset as a Drawable and display on the flagImageView
                 Drawable man = Drawable.createFromStream(stream, "wrong guess");
                 manImageView.setImageDrawable(man);

                 animate(false); // animate the flag onto the screen
                 if (hangIndex == 6) {
                     score = score + correctAnswers;
                     Toast.makeText(getContext(), "You're dead! The word was " + answer, Toast.LENGTH_SHORT).show();
                     // load the next flag after a 2-second delay
                     handler.postDelayed(
                             new Runnable() {
                                 @Override
                                 public void run() {
                                     animate(true); // animate the flag off the screen
                                 }
                             }, 2000); // 2000 milliseconds for 2-second delay

                     resetQuiz("all");
                 }
             }
             catch (IOException exception) {
                 Log.e(TAG, "Error loading Hangman image", exception);
             }

         }
      }
   };

    // displays the fragment's menu items
    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.hangman_fragment_menu, menu);
    }
    */

}


