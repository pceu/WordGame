package com.example.wordgame;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelTwoActivity extends Level implements View.OnClickListener {

    /*
        The onCreate function
        set value for some variables such as pressCount
        assign buttons created in this class with buttons from xml file
        assign all givenWords buttons to onClick function
        call playLevel function which assign each object attributes to buttons, textView accordingly
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);

        try {
            // read level Two data from csv file (stored in raw directory) and instantiate LevelData object
            // add the LevelData object created from the file to levelQuestionTwoData list
            InputStream myInputStream = getResources().openRawResource(R.raw.level_two_data);
            readLevelData(myInputStream);

            // popup dialog
            popupDialog = new Dialog(this);

            // initialize the database instance
            userDb = UserDatabase.getInstance(this);

            maxHintGiven = 2;
            // Answer Button
            setAnswerButtons = new Boolean[5];
            answerButtons = new Button[5];
            //GIVEN WORDS BUTTONS
            givenWordButtons = new Button[8];

            // set pressAnswerCount to zero
            clickWordBtnCount = 0;
            maxWordBtnClick = 5;
            levelNumber = 2;
            timerDuration = 120000;  // timer duration in milli seconds
            // assign timerTextView to it's id
            timer = findViewById(R.id.timer_level_two);
            // check if user has turned on timer mode in the setting, and decide whether to set timer or not
            if (isTimerOn()) {
                setTimer(timerDuration);
            }

            // Background Music playing code
            if (lastbkgdchecked == 1) {
                bkgrdmsc = MediaPlayer.create(LevelTwoActivity.this, R.raw.backgroundmusic);
                bkgrdmsc.setLooping(true);
                bkgrdmsc.start();
            } else {
                bkgrdmsc = MediaPlayer.create(LevelTwoActivity.this, R.raw.backgroundmusic);
                bkgrdmsc.setLooping(false);
            }


            // the LevelData object at useQuestionNumber will be pass to playLevel function to generate the game
            // the value for userQuestionNumber is get from the User Database
            userQuestionNumber = userDb.userDao().getQuestionNumber(levelNumber);
            hintClickCount = 0;

            // assign buttons and textView from xml file
            questionBoard = findViewById(R.id.l2qBoard);

            // assign hint button to the hint button created in xml layout
            // assign the button to the onClick method for this view
            hintButton = findViewById(R.id.l2HintButton);
            hintButton.setOnClickListener(this);

            // assign buttons for answerButton
            answerButtons[0] = findViewById(R.id.l2AnswerBtn1);
            answerButtons[1] = findViewById(R.id.l2AnswerBtn2);
            answerButtons[2] = findViewById(R.id.l2AnswerBtn3);
            answerButtons[3] = findViewById(R.id.l2AnswerBtn4);
            answerButtons[4] = findViewById(R.id.l2AnswerBtn5);
            // set answerButtons to false for all index
            Arrays.fill(setAnswerButtons, Boolean.FALSE);

            // ================ coin section =======================
            // assign button for coinButton
            // get the coinAmount saved in the Database and set the coinAmount
            // note: we don't need to update coin amount for all levels as they are all the same so we will only be getting and updating for level 1
            coinAmount = userDb.userDao().getCoinAmount(1);
            coinButton = findViewById(R.id.coinButtonL2);
            coinButton.setText(String.valueOf(coinAmount));
            coinButton.setOnClickListener(this);
            // skip Question section==============================
            skipButton = findViewById(R.id.l2SkipButton);
            skipButton.setOnClickListener(this);

            // assign buttons for givenWord buttons
            givenWordButtons[0] = findViewById(R.id.l2GivenWordBtn1);
            givenWordButtons[1] = findViewById(R.id.l2GivenWordBtn2);
            givenWordButtons[2] = findViewById(R.id.l2GivenWordBtn3);
            givenWordButtons[3] = findViewById(R.id.l2GivenWordBtn4);
            givenWordButtons[4] = findViewById(R.id.l2GivenWordBtn5);
            givenWordButtons[5] = findViewById(R.id.l2GivenWordBtn6);
            givenWordButtons[6] = findViewById(R.id.l2GivenWordBtn7);
            givenWordButtons[7] = findViewById(R.id.l2GivenWordBtn8);

            // assign all givenWord buttons to onClick method created for this view
            for (Button buttonAtIndx : givenWordButtons) {
                buttonAtIndx.setOnClickListener(this);
            }

            // assign all answerWord buttons to onClick method created for this view
            // creating a method for each button will be a messy codes; thus, share all buttons in one method
            for (Button answerButton : answerButtons) {
                answerButton.setOnClickListener(this);
            }

            // for clarity purpose, a playLevel method is created and call from here
            playLevel(levelData.get(userQuestionNumber));
        } catch (Exception e) {
            goToActivity(MainActivity.class);
        }

    }

    @Override
    protected void onDestroy() {
        UserDatabase.destroyInstance();
        super.onDestroy();
    }

    // If user go out from game, the background music will turn off automatically.
    protected void onPause(){
        super.onPause();
        bkgrdmsc.release();
        finish();
    }


    /*
        - The onClick methods is shared by all the givenWord buttons as we use View.OnclickListener interface
        - switch statement is used to track which button is clicked
        - make the clicked button to disappear and set clickable to false
        - set the clicked button value to the unassigned answerButton
        - each time a button is pressed/clicked, clickWordBtnCount is increment to decide whether user has filled all answer buttons
        - Once it gets to 5 for clickWordBtnCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.l2GivenWordBtn1:
                    clickGivenWord(givenWordButtons[0]);
                    break;
                case R.id.l2GivenWordBtn2:
                    clickGivenWord(givenWordButtons[1]);
                    break;
                case R.id.l2GivenWordBtn3:
                    clickGivenWord(givenWordButtons[2]);
                    break;
                case R.id.l2GivenWordBtn4:
                    clickGivenWord(givenWordButtons[3]);
                    break;
                case R.id.l2GivenWordBtn5:
                    clickGivenWord(givenWordButtons[4]);
                    break;
                case R.id.l2GivenWordBtn6:
                    clickGivenWord(givenWordButtons[5]);
                    break;
                case R.id.l2GivenWordBtn7:
                    clickGivenWord(givenWordButtons[6]);
                    break;
                case R.id.l2GivenWordBtn8:
                    clickGivenWord(givenWordButtons[7]);
                    break;
                case R.id.l2AnswerBtn1:
                    clickAnswerButton(0);
                    break;
                case R.id.l2AnswerBtn2:
                    clickAnswerButton(1);
                    break;
                case R.id.l2AnswerBtn3:
                    clickAnswerButton(2);
                    break;
                case R.id.l2AnswerBtn4:
                    clickAnswerButton(3);
                    break;
                case R.id.l2AnswerBtn5:
                    clickAnswerButton(4);
                    break;
                case R.id.l2HintButton:
                    if(hintClickCount < maxHintGiven && decreaseCoin(10)) {
                        giveHint();
                        hintClickCount++;
                    } else {
                        showNegativeMessage("Not enough coin for hint or user has reach maximum number of hint given for current level. A single use of Skip needs 10 coins.");
                    }
                case R.id.coinButtonL2:
                    break;
                case R.id.l2SkipButton:
                    if (decreaseCoin(30)) {
                        userQuestionNumber++;
                        // update question number in database for level 2
                        userDb.userDao().updateQuestionNumber(userQuestionNumber, 2);
                        playLevel(levelData.get(userQuestionNumber));
                    } else {
                        showNegativeMessage("Insufficient amount of coin to skip the question. Skip needs at least 30 coins.");
                    }
            }
        } catch (Exception e) {
            fileList();
            startActivity(getIntent());
        }
    }



}
