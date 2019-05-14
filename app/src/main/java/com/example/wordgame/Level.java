package com.example.wordgame;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public abstract class Level extends AppCompatActivity {

    // ================ CLASS ATTRIBUTES ==================================================================
    // LevelData object list for a level - contains multiple questions to play for the level
    protected List<LevelData> levelData = new ArrayList<>();
    // User database
    protected UserDatabase userDb;

    // create textView for timer
    protected TextView timer;
    protected CountDownTimer countDownTimer;
    protected final long timerDefaultDuration = 60000;
    protected long timerLeftDuration;    // differs for every level and the value will be count in milli seconds
    protected boolean isTimerRunning;    // this is used for tracking if timer is running or not

    protected int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    // use to track how many user has clicked given wordButtons
    // will increase whenever user clicks the wordButtons, and decrease whenever the answer is erased
    protected int clickWordBtnCount;
    protected int maxWordBtnClick;  // this indicates the time to validate the answer (user cannot click any button before the validation is done after clicking 3 different word buttons)

    // hint button
    protected Button hintButton;
    protected int hintClickCount;     // stored how many hint button is clicked for a given question
    // the number user can click the hint button in a question
    protected int maxHintGiven;  // differs for every level
    protected int levelNumber;  // indicate the current level number (this is needed when calling update function in Room database - we need to pass the level number)

    // question board for a level
    protected TextView questionBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    // boolean value at index 0 represent setAnswerBtn1, index 1 for setAnswerBtn2 and so on
    // will be override for level 2 and three as the array length differs for every level
    protected boolean[] setAnswerButtons;

    // Answer buttons - value will be assign when user click GivenWord buttons
    // will be override for level 2 and three as the array length differs for every level
    protected Button[] answerButtons;

    //----------------------------------------------------------------------------------------------
    // Coin Button
    protected int coinAmount;
    protected Button coinButton;

    // Skip Button
    protected Button skipButton;



    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    // will be override for level 2 and three as the array length differs for every level
    // Currently set the array length as 6 which is for level one
    protected Button [] givenWordButtons;

    // Background Music
    protected MediaPlayer bkgrdmsc;
    protected boolean isMusicPlaying; // use for tracking is music is playing or not
    protected int lastbkgdchecked = SettingActivity.bkgdchecked;

    // popup message
    Button posOkButton, negOkButton;
    Dialog popupDialog;
    TextView posMessageTv, negMessageTv, posTitleTv, negTitleTV;

    // ================ CLASS METHODS/FUNCTIONS ===========================================================

    /**
     * takes a level object as a parameter which will then be use to generate the game for user
     * First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
     * extract the question from the given object and present in the question board.
     * assign each character to givenWordButtons
     * @param levelDataObject an object of LevelData (contains question number, question, answer and so on)
     */
    public void playLevel(LevelData levelDataObject)
    {
        // reset all the buttons to make sure no value associated with answer and word buttons are not assigned at the beginning of the game.
        resetButtons();

        // handling bugs here
        /*
            if passed object is null, then pass next object
            condition: if the object is the last object in the class go to next level or appropriate page
         */
        if(levelDataObject == null || levelDataObject.getQuestion() == null) {
            if (userQuestionNumber == levelData.size() - 1) {
                // reset the question number in database to zero as user has finished the level
                // when play again this level, will be started from question 1 again, which is zero index in the object and database
                userDb.userDao().updateQuestionNumber(0, levelNumber);
                // go to next level page
                if(levelNumber == 1) {
                    goToActivity(LevelThreeActivity.class);
                } else if(levelNumber == 2) {
                    goToActivity(LevelThreeActivity.class);
                } else {
                    goToActivity(ChooseLevelActivity.class);
                }
                return;
            }
            Toast.makeText(this, "The question is empty! Try the next question", Toast.LENGTH_SHORT).show();
            userQuestionNumber++;
            // update the question number in database with current question number
            userDb.userDao().updateQuestionNumber(userQuestionNumber, levelNumber);
            // pass the next question (object) to playLevel function
            playLevel(levelData.get(userQuestionNumber));
        }
        // assign object's attribute 'question' to questionBoard TextView
        questionBoard.setText(levelDataObject.getQuestion());

        // assign given word value to each button
        // could use for loop as button will be stored in array
        for (int i = 0; i < givenWordButtons.length; i++) {
            givenWordButtons[i].setText(String.valueOf(levelDataObject.getGivenWord().charAt(i)));
        }
    }

    /**
     * extract current date time and hour
     * add the string combined with date time to the attribute of Log class - logList
     * @param exceptionMessage a string of message that will be added to Log page
     */
    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[level " + levelNumber +"] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

    /**
     * clickGivenWord method
     * make disappear of the clicked button (the button given in the parameter)
     * increase the clickWordBtn count by 1
     * set the text from the button in the answer button (one that does not have a value yet)
     * do the answer validation if the clickWordBtn count gets to maxClickCount
     * @param wordButton one of the wordButtons from the array
     */
    public void clickGivenWord(Button wordButton) {
        try {
            disappearButton(wordButton);
            clickWordBtnCount++;
            setAnswer(String.valueOf(wordButton.getText()));
            if (clickWordBtnCount == maxWordBtnClick) {
                validateAnswer(levelData.get(userQuestionNumber));
            }
        } catch (Exception e) {
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * clickAnswerButton method
     * do nothing and return if the setAnswerButtons at the given index is not false
     * otherwise, put back the button in the original place
     * set the answer button at index to an empty string
     * the false value for setAnswerButtons at index given
     * reduce the clickWordBtnCount again
     * @param arrayIndex the index of array
     */
    public void clickAnswerButton(int arrayIndex) {
        try {
            // exception is thrown when the bug is exceptional and solve it
            if(arrayIndex > answerButtons.length - 1) {
                throw new IndexOutOfBoundsException();
            }
            if(setAnswerButtons[arrayIndex]) {
                putBackWordButton(answerButtons[arrayIndex]);
                answerButtons[arrayIndex].setText("");
                setAnswerButtons[arrayIndex] = false;
                clickWordBtnCount--;
            }
        } catch (Exception e) {
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * accept a button as a parameter
     * compares the text from the given button with every word buttons; and put back (make appear and clickable) the button which has the same letter with the input button text
     * the input button is the answer button clicked by the user
     * exceptions if thrown any are added to the log page and load the current question again
     * @param answerButton one of the answer buttons in the array
     */
    public void putBackWordButton(Button answerButton) {
        try {
            if(answerButton.getText() == null || answerButton.getText() == "") {
                throw new MyException("Text button cannot be null or empty when trying to put back button.");
            }
            String answerButtonText = String.valueOf(answerButton.getText());

            // use foreach loop to compare the given text with every single givenWord buttons
            // make reappear the button at index if the text stored are the same
            for (Button buttonAtIdx: givenWordButtons) {
                if(answerButtonText.equalsIgnoreCase(String.valueOf(buttonAtIdx.getText())) && !buttonAtIdx.isClickable()) {
                    reappearButton(buttonAtIdx);
                    return;
                }
            }
        } catch (Exception e) {
            // resume the question in the game of bugs occur
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * set answer accept a string input and checks:
     *        - if a button has been assigned with a value (an alphabet)
     *        - if yes, go to next button and if no, then assign the input text (which is the text of the clicked wordGivenButtons) to the answerButton.
     *        - set true to the setAnswerButton to know it has been assigned
     * @param text a string to set at the button text
     */
    public void setAnswer(String text) {
        try {
            for (int i = 0; i < answerButtons.length; i++) {
                if (!setAnswerButtons[i]) {
                    answerButtons[i].setText(text);
                    setAnswerButtons[i] = true;
                    return;
                }
            }
        } catch (Exception e) {
            // resume the question and report the bugs to log page by adding it to the list
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * set all wordGivenButtons to appear and set clickable to true
     * set false for setAnswer variables
     * set the answers buttons to empty string
     */
    public void resetButtons() {
        try {
            // make re-appear wordButtons
            for (Button givenWordButton : givenWordButtons) {
                reappearButton(givenWordButton);
            }

            // set to false for setAnswers buttons as we need this value to check if the answerButton is filled with letter
            // learn the method(setting all boolean values in array to false or true) from this link - https://stackoverflow.com/questions/2364856/initializing-a-boolean-array-in-java
            Arrays.fill(setAnswerButtons, Boolean.FALSE);

            // Empty the any letter assign to answer buttons
            for (Button answerButton : answerButtons) {
                answerButton.setText("");
            }
        } catch (Exception e) {
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * this function set the alpha to full value and set button clickable to true
     * @param button a word button from the array
     */
    public void reappearButton(Button button) {
        button.animate().alpha(255).setDuration(50);
        button.setClickable(true);
    }

    /**
     * set alpha value of a given button to zero and set false for setClickable
     * @param button word button from givenWordButtons array
     */
    public void disappearButton(Button button) {

        button.animate().alpha(0).setDuration(50);
        button.setClickable(false);
    }

    /**
     * if the decided number of hints are already given, then return
     * if decreaseCoin does not return true, then exit the function
     * if the above stated conditions are not met, check every setAnswer buttons and do the following
     *             - if setAnswer bool value is false
     *             - increase clickWordBtnCount++ (as how many click of this helps the program know when to validate the answer)
     *             - store the letter at a button (if answer button 1 is not set yet) for the question in a temporary String variable
     *             - set the above temp variable as the given (in parameter) answer button text
     *             - set setAnswer button to true
     *             - validate the answer if clickWordBtnCount is 7
     *             - else return (exit the function)
     */
    public void giveHint() {
        try {
            // check if the hint has been given or not (one hint is only allowed for level 1)
            if(hintClickCount == maxHintGiven) {
                return;
            }
            for (int i = 0; i < setAnswerButtons.length; i++) {
                if(!setAnswerButtons[i]) {
                    clickWordBtnCount++;
                    String hintLetter = String.valueOf(levelData.get(userQuestionNumber).getAnswer().charAt(i));
                    answerButtons[i].setText(hintLetter);
                    setAnswerButtons[i] = true;
                    if (clickWordBtnCount == maxWordBtnClick) {
                        validateAnswer(levelData.get(userQuestionNumber));
                        return;
                    } else { return;}
                }
            }
        } catch (Exception e) {
            // resume the question in the game of bugs occur
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    // Coin number Modification

    /**
     * # check and return false if the user coin amount subtract amount passed in the parameter is less than zero
     *         # else - reduce the user coin amount by the given amount and update the text of coinButton1
     *         # also update the number of coin in database every time the coin is modified
     * @param amount number of coin to decrease
     * @return a boolean value
     */
    public boolean decreaseCoin(int amount) {
        try {
            if (coinAmount - amount < 0) {
                return false;
            } else {
                coinAmount = coinAmount - amount;
                coinButton.setText(String.valueOf(coinAmount));
                userDb.userDao().updateCoin(coinAmount, 1);
                return true;
            }
        } catch (Exception e) {
            // reload the question again
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
            return false;
        }
    }

    /**
     * takes the amount given in the parameter and update the coinAmount, coinButton text and coinAmount in the database
     * does not need to return anything as we do not have to check the amount for increasing (increasing doesn't have any requirements to check)
     * @param amount number of coin to increase the coin number
     */
    public void increaseCoin(int amount) {
        try {
            coinAmount = coinAmount + amount;
            coinButton.setText(String.valueOf(coinAmount));
            userDb.userDao().updateCoin(coinAmount, 1);
        } catch (Exception e) {
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * first set siTimerRunning to true to know that timer is running
     * initialize countDownTimer to a new one with passing timeInMilliSeconds
     * calculate minute and second for each tick (second in our case)
     * update the timer text with the calculated minutes and seconds
     * validate the answer when timer is finished
     * if timer is set as on start the timer again
     * open pop up message to let user know that he/she does not get the answer right
     * @param timeInMilliSeconds the duration of the timer in milli seconds
     */
    public void setTimer(long timeInMilliSeconds) {
        isTimerRunning = true;
        countDownTimer = new CountDownTimer(timeInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timerLeftDuration = l;
                // calculate minutes
                int leftMinutes = (int)(l / 60000);
                // calculate seconds
                int leftSeconds = (int) (l % 60000 / 1000);
                StringBuilder remainTime = new StringBuilder();
                remainTime.append(leftMinutes);
                remainTime.append(":");
                if (leftSeconds < 10) { remainTime.append(0);}
                remainTime.append(leftSeconds);
                // set the string (formatted as minute and second) to timer Text
                timer.setText(String.valueOf(remainTime));
            }

            @Override
            public void onFinish() {
                validateAnswer(levelData.get(userQuestionNumber));
                // check if timer is set as on first (if yes == set timer)
                // whether it the answer is right or wrong start the timer again for next question or repeating current question
                if (isTimerOn()) {
                    countDownTimer.cancel();
                    timerLeftDuration = timerDefaultDuration;
                    setTimer(timerLeftDuration);
                    isTimerRunning = true;
                }
                showNegativeMessage("That was close! Try again to answer correctly within the given time frame.");
            }
        }.start();
    }

    /**
     * true if timerSet == 1
     * else false
     * @return a boolean value (depending on which if statement is executed)
     */
    public boolean isTimerOn() {
        if(SettingActivity.timerSet == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return true if bkgdchecked is 1 or false
     */
    public boolean isMusicOn() {
        if(SettingActivity.bkgdchecked == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * accepts a LevelData object as a parameter so that we know what object to pass when checking answer
     * compares the answer from user and answer within the LevelData object
     * If correct
     *   - check if the question is the last one in the level, and go to next level should the question be the last one for this level.
     *   - modify some data related with question (see in the method body)
     *   - set timer again if isTimerOn returns true
     *   - pass the next object in the LevelData Object list (next question in other words) for user to play
     *   - if incorrect
     *   - pass the same object for user to play again
     * @param levelData1Object leveldata object
     */
    public void validateAnswer(LevelData levelData1Object) {
        try {
            // first, combine the letters assigned to the answer buttons from user pressing buttons
            StringBuilder userAns = new StringBuilder();
            for (Button answerButton: answerButtons) {
                userAns.append(String.valueOf(answerButton.getText()));
            }

            if (levelData1Object.getAnswer().equalsIgnoreCase(String.valueOf(userAns))) {
                if (userQuestionNumber == levelData.size() - 1) {
                    StringBuilder message = new StringBuilder();
                    message.append("You have completed Level ");
                    message.append(levelNumber);
                    message.append(". If you play this level again, you will be starting from question 1 again.");
                    showPositiveMessage(String.valueOf(message));
                    // reset the question number in database to zero as user has finished the level
                    // when play again this level, will be started from question 1 again, which is zero index in the object and database
                    userDb.userDao().updateQuestionNumber(0, levelNumber);
                    // go to next level page
                    if(levelNumber == 1) {
                        goToActivity(LevelThreeActivity.class);
                    } else if(levelNumber == 2) {
                        goToActivity(LevelThreeActivity.class);
                    } else {
                        goToActivity(ChooseLevelActivity.class);
                    }
                    return;
                }
                // increment coins amount by 10 for correct answer
                increaseCoin(10);
                clickWordBtnCount = 0;
                hintClickCount = 0;
                userQuestionNumber++;
                // update the question number in database with current question number
                userDb.userDao().updateQuestionNumber(userQuestionNumber, levelNumber);
                if (isTimerOn()) {
                    countDownTimer.cancel();
                    isTimerRunning = false;
                    timerLeftDuration = timerDefaultDuration;
                    setTimer(timerLeftDuration);
                }
                // pass the next question (object) to playLevel function
                playLevel(levelData.get(userQuestionNumber));
            } else {
                clickWordBtnCount = 0;
                // reload the question again by passing the same object to playLevel method
                playLevel(levelData.get(userQuestionNumber));
            }
        } catch (Exception e) {
            playLevel(levelData.get(userQuestionNumber));
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * go to the activity of the class being passed
     * @param activity class name/object
     */
    public void goToActivity(Class activity) {
        Intent intent = new Intent (this, activity);
        startActivity(intent);
    }

    /**
     * Create an input stream to read files stored in this project
     * create a bufferedReader for the input stream
     * split the line in the csv file using comma as a token
     * store each token at value (0..5) in a temp variable which will then be used to create a LevelData Object
     * add the newly created LevelData object in a list (userLevelQuestion)
     * @param inputStream ImputStream object
     */
    public void readLevelData(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

        String tempString = "";
        try {
            while ((tempString = bufferedReader.readLine()) != null) {
                String[] tokens = tempString.split(",");

                // Read the data and create a LevelData object, then add them in levelQuestionOneData list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                int levelNumber = Integer.parseInt(tokens[3]);
                String givenWord = tokens[4];
                //create a new LevelData object by passing the above variables in its constructor
                // add the new object to levelQuestionOneData list
                levelData.add(new LevelData(questionNumber, question, answer, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("Level " + levelNumber + " Activity", "Error occur while reading on line" + tempString, e);
            e.printStackTrace();
            addToLogList(String.valueOf(e.getMessage()));
            goToActivity(MainActivity.class);
        }
    }

    /**
     * prepare and assign content view to positive_message layout
     * assign textViews and buttons to it's id
     * set the message in the textView
     * populate the transparent background
     * @param message string message to show
     */
    public void showPositiveMessage(String message) {
        popupDialog.setContentView(R.layout.positive_message);
        posTitleTv = popupDialog.findViewById(R.id.posTitleTv);
        posOkButton = popupDialog.findViewById(R.id.posOkButton);
        posMessageTv = popupDialog.findViewById(R.id.posMessageTv);
        posMessageTv.setText(message);
        posOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });
        try {
            popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupDialog.show();
        } catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            popupDialog.dismiss();
            addToLogList(String.valueOf(e.getMessage()));
        }
    }

    /**
     * prepare and assign content view to negative_message layout
     * assign textViews and buttons to it's id
     * set the message in the textView
     * populate the transparent background
     * @param message string message to show
     */
    public void showNegativeMessage(String message) {
        popupDialog.setContentView(R.layout.negative_message);
        negTitleTV = popupDialog.findViewById(R.id.negTitleTv);
        negOkButton = popupDialog.findViewById(R.id.negOkButton);
        negMessageTv = popupDialog.findViewById(R.id.negMessageTv);
        negMessageTv.setText(String.valueOf(message));
        negOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });
        try {
            popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupDialog.show();
        } catch (NullPointerException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            popupDialog.dismiss();
            addToLogList(String.valueOf(e.getMessage()));
        }

    }

    /**
     * create intent and according levelNumber, assign appropriate class to the intent
     * store data such as hintClickCount, clickWordBtnCount, answerTexts, boolean for if wordButtons are clickable and current timer leftDuration
     * call finish activity as it usually go back to previous layout
     * and start the activity
     * @param newConfig the new config object
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Intent i;
        if(levelNumber == 1) {
            i = new Intent(this, LevelOneActivity.class);
        } else if(levelNumber == 2) {
            i = new Intent(this, LevelTwoActivity.class);
        } else {
            i = new Intent(this, LevelThreeActivity.class);
        }
        i.putExtra("hintClickCount", hintClickCount);
        i.putExtra("wordBtnCount", clickWordBtnCount);
        String[] answerTexts = new String[answerButtons.length];
        // store each answer button texts in array and pass them
        for(int index = 0; index < answerTexts.length; index++) {
            answerTexts[index] = String.valueOf(answerButtons[index].getText());
        }
        i.putExtra("answerTexts", answerTexts);
        i.putExtra("wordButtonsClickable", areWordBtnClickable());
        i.putExtra("currentLeftTimer", timerLeftDuration);
        finish();
        startActivity(i);
    }

    // ========= methods for passing data when configuration change

    /**
     * set boolean array at index to true if givenWordButtons at index is clickable
     * otherwise set false
     * @return array of boolean
     */
    public boolean[] areWordBtnClickable() {
        boolean[] result = new boolean[givenWordButtons.length];
        for(int i = 0; i <givenWordButtons.length; i++) {
            if(givenWordButtons[i].isClickable()) {
                result[i] = true;
            } else {
                result[i] = false;
            }
        }
        return result;
    }


    // ================== Activity resume, destroy and pause methods

    /**
     * calls super onDestroy method
     * destroy database instance
     * release music and set false for isMusicPlaying if music is playing
     * cancel timer and set false for isTimerRunning if timer is running
     * call finish method to end the activity
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserDatabase.destroyInstance();
        if(isMusicOn()) {
            // to save memory consumption we release the music and start again on onResume()
            bkgrdmsc.release();
            isMusicPlaying = false;
        }
        if (isTimerOn()) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
        finish();
    }

    // If user go out from game, the background music will turn off automatically.

    /**
     * pause music if music is playing and set false for isMusicPlaying
     * cancel timer and set false for isTimerRunning if timer is running
     */
    @Override
    protected void onPause(){
        super.onPause();
        if(isMusicOn()) {
            // to save memory consumption we release the music and start again on onResume()
            bkgrdmsc.release();
            isMusicPlaying = false;
        }
        if(isTimerOn()) {
            countDownTimer.cancel();
            isTimerRunning = false;
        }
        //finish();
    }


    /*
        ====== LEARNING SUMMARY
        - creating an interface - https://www.geeksforgeeks.org/interfaces-in-java/

     */


}
