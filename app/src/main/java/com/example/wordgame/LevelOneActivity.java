package com.example.wordgame;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;
import java.io.InputStream;
import java.util.Arrays;


public class LevelOneActivity extends Level implements View.OnClickListener {

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
        setContentView(R.layout.activity_level_one);

        // read level one data from csv file (stored in raw directory) and instantiate LevelData object
        // add the LevelData object created from the file to levelQuestionOneData list
        InputStream myInputStream = getResources().openRawResource(R.raw.level_one_data); // create input stream for the  level
        readLevelData(myInputStream);

        // popup dialog
        popupDialog = new Dialog(this);

        // initialize the database instance
        userDb = UserDatabase.getInstance(this);

        maxHintGiven = 1;
        // Answer Button
        setAnswerButtons = new Boolean[3];
        answerButtons = new Button[3];
        //GIVEN WORDS BUTTONS
        givenWordButtons = new Button[6];


        // set pressAnswerCount to zero
        clickWordBtnCount = 0;
        maxWordBtnClick = 3;
        levelNumber = 1;
        timerDuration = 60000;  // timer duration in milli seconds
        // assign timerTextView to it's id
        timer = findViewById(R.id.timer_level_one);
        // check if user has turned on timer mode in the setting, and decide whether to set timer or not
        if (isTimerOn()) {
            setTimer(timerDuration);
        }

        // Background Music playing code
        if (lastbkgdchecked == 1) {
            bkgrdmsc = MediaPlayer.create(LevelOneActivity.this, R.raw.backgroundmusic);
            bkgrdmsc.setLooping(true);
            bkgrdmsc.start();
        } else {
            bkgrdmsc = MediaPlayer.create(LevelOneActivity.this, R.raw.backgroundmusic);
            bkgrdmsc.setLooping(false);
        }

        // the LevelData object at useQuestionNumber will be pass to playLevel function to generate the game
        // the value for userQuestionNumber is get from the User Database
        userQuestionNumber = userDb.userDao().getQuestionNumber(levelNumber);
        hintClickCount = 0;


        // assign buttons and textView from xml file
        questionBoard = findViewById(R.id.l1qBoard);

        // assign hint button to the hint button created in xml layout
        // assign the button to the onClick method for this view
        hintButton = findViewById(R.id.l1HintButton);
        hintButton.setOnClickListener(this);

        // assign buttons for answerButton
        answerButtons[0] = findViewById(R.id.l1AnswerBtn1);
        answerButtons[1] = findViewById(R.id.l1AnswerBtn2);
        answerButtons[2] = findViewById(R.id.l1AnswerBtn3);

        // ================ coin section =======================
        // get the coinAmount saved in the Database and set the coinAmount
        coinAmount = userDb.userDao().getCoinAmount(levelNumber);
        // assign button for coinButton
        coinButton = findViewById(R.id.coinButtonL1);
        coinButton.setText(String.valueOf(coinAmount));
        coinButton.setOnClickListener(this);

        // skip Question section==============================
        skipButton = findViewById(R.id.l1SkipButton);
        skipButton.setOnClickListener(this);

        // set answerButtons to false for all index
        Arrays.fill(setAnswerButtons, Boolean.FALSE);

        // assign buttons for givenWord buttons
        givenWordButtons[0] = findViewById(R.id.l1GivenWordBtn1);
        givenWordButtons[1] = findViewById(R.id.l1GivenWordBtn2);
        givenWordButtons[2] = findViewById(R.id.l1GivenWordBtn3);
        givenWordButtons[3] = findViewById(R.id.l1GivenWordBtn4);
        givenWordButtons[4] = findViewById(R.id.l1GivenWordBtn5);
        givenWordButtons[5] = findViewById(R.id.l1GivenWordBtn6);

        // assign all givenWord buttons to onClick method created for this view
        for (Button buttonAtIdx : givenWordButtons) {
            buttonAtIdx.setOnClickListener(this);
        }

        // assign all answerButtons to onCLick method created for this view
        for (Button answerButton : answerButtons) {
            answerButton.setOnClickListener(this);
        }

        // for clarity purpose, a playLevel method is created and call from here
        playLevel(levelData.get(userQuestionNumber));
    }

    @Override
    protected void onDestroy() {
        UserDatabase.destroyInstance();
        super.onDestroy();
    }

    // If user go out from game, the background music will turn off automatically.
    protected void onPause(){
        if (lastbkgdchecked == 0){
            bkgrdmsc.release();
        }
        //SettingActivity.bkgdchecked = 0;
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
        - Once it gets to 3 for clickWordBtnCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l1GivenWordBtn1:
                clickGivenWord(givenWordButtons[0]);
                break;
            case R.id.l1GivenWordBtn2:
                clickGivenWord(givenWordButtons[1]);
                break;
            case R.id.l1GivenWordBtn3:
                clickGivenWord(givenWordButtons[2]);
                break;
            case R.id.l1GivenWordBtn4:
                clickGivenWord(givenWordButtons[3]);
                break;
            case R.id.l1GivenWordBtn5:
                clickGivenWord(givenWordButtons[4]);
                break;
            case R.id.l1GivenWordBtn6:
                clickGivenWord(givenWordButtons[5]);
                break;
            case R.id.l1AnswerBtn1:
                clickAnswerButton(0);
                break;
            case R.id.l1AnswerBtn2:
                clickAnswerButton(1);
                break;
            case R.id.l1AnswerBtn3:
                clickAnswerButton(2);
                break;
            case R.id.l1HintButton:
                if(hintClickCount < maxHintGiven && decreaseCoin(10)) {
                    giveHint();
                    hintClickCount++;
                } else {
                    showNegativeMessage("Not enough coin for hint or user has reach maximum number of hint given for current level. A single use of Skip needs 10 coins.");
                }
            case R.id.coinButtonL1:
                break;
            case R.id.l1SkipButton:
                if (decreaseCoin(30)) {
                    userQuestionNumber++;
                    // update question number in database for level 1
                    userDb.userDao().updateQuestionNumber(userQuestionNumber, 1);
                    playLevel(levelData.get(userQuestionNumber));
                } else {
                    showNegativeMessage("Insufficient amount of coin to skip the question. Skip needs at least 30 coins.");
                }
        }
    }



}
