package com.example.wordgame;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
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

public class LevelThreeActivity extends Level implements View.OnClickListener {

    /**
     * The onCreate function
     * set value for some variables such as pressCount
     *         - assign buttons created in this class with buttons from xml file
     *         - assign all givenWords buttons to onClick function
     *         - call playLevel function which assign each object attributes to buttons, textView accordingly
     *         - as attributes are inherited from Level class all the attributes are initialize in this onCreate method
     *         - class related attributes are:
     *             - music file
     *             - timer
     *             - buttons
     *             - textView
     *             - database and so on
     *         - when assigning and initializing them, comments are also included
     * @param savedInstanceState state being passed when activity is loaded
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_three);

        try {
            // read level Three data from csv file (stored in raw directory) and instantiate LevelData object
            // add the LevelData object created from the file to levelQuestionThreeData list
            InputStream myInputStream = getResources().openRawResource(R.raw.level_three_data);
            readLevelData(myInputStream);

            // popup dialog
            popupDialog = new Dialog(this);

            // initialize the database instance
            userDb = UserDatabase.getInstance(this);

            maxHintGiven = 3;
            // Answer Button
            setAnswerButtons = new boolean[7];
            answerButtons = new Button[7];
            //GIVEN WORDS BUTTONS
            givenWordButtons = new Button[12];

            // set pressAnswerCount to zero
            clickWordBtnCount = 0;
            maxWordBtnClick = 7;
            levelNumber = 3;


            // Background Music playing code
            if (lastbkgdchecked == 1) {
                bkgrdmsc = MediaPlayer.create(LevelThreeActivity.this, R.raw.backgroundmusic);
                bkgrdmsc.setLooping(true);
                bkgrdmsc.start();
                isMusicPlaying = true;
            } else {
                isMusicPlaying = false;
            }


            // the LevelData object at useQuestionNumber will be pass to playLevel function to generate the game
            // the value for userQuestionNumber is get from the User Database
            userQuestionNumber = userDb.userDao().getQuestionNumber(levelNumber);

            //==================== Hint section================
            hintClickCount = 0;
            // assign hint button to the hint button created in xml layout
            // assign the button to the onClick method for this view
            hintButton = findViewById(R.id.l3HintButton);
            hintButton.setOnClickListener(this);

            //-------------------------------------------------------------------

            // set pressAnswerCount to zero
            clickWordBtnCount = 0;
            // assign buttons and textView from xml file
            questionBoard = findViewById(R.id.l3qBoard);

            // assign buttons for answerButton
            answerButtons[0] = findViewById(R.id.l3AnswerBtn1);
            answerButtons[1] = findViewById(R.id.l3AnswerBtn2);
            answerButtons[2] = findViewById(R.id.l3AnswerBtn3);
            answerButtons[3] = findViewById(R.id.l3AnswerBtn4);
            answerButtons[4] = findViewById(R.id.l3AnswerBtn5);
            answerButtons[5] = findViewById(R.id.l3AnswerBtn6);
            answerButtons[6] = findViewById(R.id.l3AnswerBtn7);
            // set answerButtons to false for all index
            Arrays.fill(setAnswerButtons, Boolean.FALSE);

            // load the font for text in this level
            Typeface customFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/felaFromAssets.otf");

            // ================ coin section =======================
            // assign button for coinButton
            // get the coinAmount saved in the Database and set the coinAmount
            // note: we don't need to update coin amount for all levels as they are all the same so we will only be getting and updating for level 1
            coinAmount = userDb.userDao().getCoinAmount(1); // we don't pass level number here as we only update and retrieve coin amount in level 1
            // assign button for coinButton
            coinButton = findViewById(R.id.coinButtonL3);
            coinButton.setText(String.valueOf(coinAmount));
            coinButton.setOnClickListener(this);

            // skip Question section==============================
            skipButton = findViewById(R.id.l3SkipButton);
            skipButton.setOnClickListener(this);

            // assign buttons for givenWord buttons
            givenWordButtons[0] = findViewById(R.id.l3GivenWordBtn1);
            givenWordButtons[0].setTypeface(customFont);
            givenWordButtons[1] = findViewById(R.id.l3GivenWordBtn2);
            givenWordButtons[2] = findViewById(R.id.l3GivenWordBtn3);
            givenWordButtons[3] = findViewById(R.id.l3GivenWordBtn4);
            givenWordButtons[4] = findViewById(R.id.l3GivenWordBtn5);
            givenWordButtons[5] = findViewById(R.id.l3GivenWordBtn6);
            givenWordButtons[6] = findViewById(R.id.l3GivenWordBtn7);
            givenWordButtons[7] = findViewById(R.id.l3GivenWordBtn8);
            givenWordButtons[8] = findViewById(R.id.l3GivenWordBtn9);
            givenWordButtons[9] = findViewById(R.id.l3GivenWordBtn10);
            givenWordButtons[10] = findViewById(R.id.l3GivenWordBtn11);
            givenWordButtons[11] = findViewById(R.id.l3GivenWordBtn12);

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

            // put before checking bundle as this variable will be changed if bundle is not null
            timerLeftDuration = timerDefaultDuration;  // timer duration in milli seconds

            Bundle bundle = getIntent().getExtras();
            if(bundle != null) {
                hintClickCount = bundle.getInt("hintClickCount");
                clickWordBtnCount = bundle.getInt("wordBtnCount");
                timerLeftDuration = bundle.getLong("currentLeftTimer");
                boolean[] wordButtonsClickableValue = bundle.getBooleanArray("wordButtonsClickable");
                for(int i = 0; i <wordButtonsClickableValue.length; i++) {
                    if(!wordButtonsClickableValue[i]) {
                        disappearButton(givenWordButtons[i]);
                    }
                }
                String[] tempString = bundle.getStringArray("answerTexts");
                for(int a = 0; a < tempString.length; a++) {
                    answerButtons[a].setText(tempString[a]);
                    if(tempString[a].equalsIgnoreCase("")) {
                        setAnswerButtons[a] = false;
                    } else {
                        setAnswerButtons[a] = true;
                    }
                }
            }

            // assign timerTextView to it's id
            timer = findViewById(R.id.timer_level_three);
            // check if user has turned on timer mode in the setting, and decide whether to set timer or not
            if (isTimerOn()) {
                setTimer(timerLeftDuration);
                timer.setVisibility(TextView.VISIBLE);
            } else {
                isTimerRunning = false;
                timer.setVisibility(TextView.INVISIBLE);
            }
        } catch (Exception e) {
            addToLogList(String.valueOf(e.getMessage()));
            goToActivity(MainActivity.class);
        }
    }

    /**
     * onResume()
     * Music and timer are released and canceled when activity is on pause (to save memory consumption)
     * when the activity is resumed, we then check again whether to resume timer or play music by checking some value
     * related to them
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(isMusicOn() && !isMusicPlaying) {
            bkgrdmsc = MediaPlayer.create(LevelThreeActivity.this, R.raw.backgroundmusic);
            bkgrdmsc.setLooping(true);
            bkgrdmsc.start();
        }
        if(isTimerOn() && !isTimerRunning) {
            setTimer(timerLeftDuration);
        }
    }

    /**
     * - The onClick methods is shared by all the givenWord buttons as we use View.OnclickListener interface
     *         - switch statement is used to track which button is clicked
     *         - make the clicked button to disappear and set clickable to false
     *         - set the clicked button value to the unassigned answerButton
     *         - each time a button is pressed/clicked, clickWordBtnCount is increment to decide whether user has filled all answer buttons
     *         - Once it gets to 7 for clickWordBtnCount, call validateAnswer() which will validate the answer and pass appropriate object
     *           depending on user getting the answer right or wrong
     * @param view view on this activity/page
     */
    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.l3GivenWordBtn1:
                    clickGivenWord(givenWordButtons[0]);
                    break;
                case R.id.l3GivenWordBtn2:
                    clickGivenWord(givenWordButtons[1]);
                    break;
                case R.id.l3GivenWordBtn3:
                    clickGivenWord(givenWordButtons[2]);
                    break;
                case R.id.l3GivenWordBtn4:
                    clickGivenWord(givenWordButtons[3]);
                    break;
                case R.id.l3GivenWordBtn5:
                    clickGivenWord(givenWordButtons[4]);
                    break;
                case R.id.l3GivenWordBtn6:
                    clickGivenWord(givenWordButtons[5]);
                    break;
                case R.id.l3GivenWordBtn7:
                    clickGivenWord(givenWordButtons[6]);
                    break;
                case R.id.l3GivenWordBtn8:
                    clickGivenWord(givenWordButtons[7]);
                    break;
                case R.id.l3GivenWordBtn9:
                    clickGivenWord(givenWordButtons[8]);
                    break;
                case R.id.l3GivenWordBtn10:
                    clickGivenWord(givenWordButtons[9]);
                    break;
                case R.id.l3GivenWordBtn11:
                    clickGivenWord(givenWordButtons[10]);
                    break;
                case R.id.l3GivenWordBtn12:
                    clickGivenWord(givenWordButtons[11]);
                    break;
                case R.id.l3AnswerBtn1:
                    clickAnswerButton(0);
                    break;
                case R.id.l3AnswerBtn2:
                    clickAnswerButton(1);
                    break;
                case R.id.l3AnswerBtn3:
                    clickAnswerButton(2);
                    break;
                case R.id.l3AnswerBtn4:
                    clickAnswerButton(3);
                    break;
                case R.id.l3AnswerBtn5:
                    clickAnswerButton(4);
                    break;
                case R.id.l3AnswerBtn6:
                    clickAnswerButton(5);
                    break;
                case R.id.l3AnswerBtn7:
                    clickAnswerButton(6);
                    break;
                case R.id.l3HintButton:
                    if(hintClickCount < maxHintGiven && decreaseCoin(10)) {
                        giveHint();
                        hintClickCount++;
                    } else {
                        showNegativeMessage("Not enough coin for hint or user has reach maximum number of hint given for current level. A single use of Skip needs 10 coins.");
                    }
                case R.id.coinButtonL3:
                    break;
                case R.id.l3SkipButton:
                    if (decreaseCoin(30)) {
                        userQuestionNumber++;
                        // update question number in database for level 3
                        userDb.userDao().updateQuestionNumber(userQuestionNumber, 3);
                        playLevel(levelData.get(userQuestionNumber));
                    } else {
                        showNegativeMessage("Insufficient amount of coin to skip the question. Skip needs at least 30 coins.");
                    }
            }
        } catch (Exception e) {
            addToLogList(String.valueOf(e.getMessage()));
            finish();
            startActivity(getIntent());
        }
    }


}
