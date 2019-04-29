package com.example.wordgame;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LevelOneActivity extends AppCompatActivity implements View.OnClickListener, Level {

    // LevelData object list for a level - contains multiple questions to play for the level
    List<LevelData> levelData = new ArrayList<>();
    // User database
    private UserDatabase userDb;

    // create textView for timer
    TextView timer;
    CountDownTimer countDownTimer;

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    // use to track how many user has clicked given wordButtons
    // will increase whenever user clicks the wordButtons, and decrease whenever the answer is erased
    private int clickWordBtnCount;

    // hint button
    private Button hintButton;
    private int hintClickCount;     // stored how many hint button is clicked for a given question
    // the number user can click the hint button in a question
    static final int maxHintGiven = 1;
    // question board for a level
    private TextView questionBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    // boolean value at index 0 represent setAnswerBtn1, index 1 for setAnswerBtn2 and so on
    private Boolean [] setAnswerButtons = new Boolean[3];

    // Answer buttons - value will be assign when user click GivenWord buttons
    private Button[] answerButtons = new Button[3];

    // Coin Button
    int coinAmount;
    Button coinButton;

    // Skip Button
    Button skipButton;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button [] givenWordButtons = new Button[6];

    // Background Music
    MediaPlayer bkgrdmsc;
    private int lastbkgdchecked = SettingActivity.bkgdchecked;
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

        // initialize the database instance
        userDb = UserDatabase.getInstance(this);

        // assign timerTextView to it's id
        timer = findViewById(R.id.timer_level_one);
        // check if user has turned on timer mode in the setting, and decide whether to set timer or not
        if (isTimerOn()) {
            setTimer(60000);
        }

        // Background Music playing code
        if (lastbkgdchecked == 0) {
            bkgrdmsc = MediaPlayer.create(LevelOneActivity.this, R.raw.backgroundmusic);
            bkgrdmsc.setLooping(true);
            bkgrdmsc.start();
        }
        // read level one data from csv file (stored in raw directory) and instantiate LevelData object
        // add the LevelData object created from the file to levelQuestionOneData list
        readLevelOneData();

        // the LevelData object at useQuestionNumber will be pass to playLevel function to generate the game
        // the value for userQuestionNumber is get from the User Database
        userQuestionNumber = userDb.userDao().getQuestionNumber(1);
        hintClickCount = 0;

        // set pressAnswerCount to zero
        clickWordBtnCount = 0;
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
        coinAmount = userDb.userDao().getCoinAmount(1);
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
        SettingActivity.bkgdchecked = 0;
        super.onPause();
        bkgrdmsc.release();
        finish();
    }

    /*
        readLevelOneData
        Create an input stream to read files stored in this project
        create a bufferedReader for the input stream
        split the line in the csv file using comma as a token
        store each token at value (0..5) in a temp variable which will then be used to create a LevelData Object
        add the newly created LevelData object in a list (userLevelQuestion)
     */
    public void readLevelOneData() {
        InputStream myInputStream = getResources().openRawResource(R.raw.level_one_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myInputStream, Charset.forName("UTF-8")));

        String tempString = "";
        try {
            while ((tempString = bufferedReader.readLine()) != null) {
                String[] tokens = tempString.split(",");

                // Read the data and create a LevelData object, then add them in levelQuestionOneData list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                String hint = tokens[3];
                int levelNumber = Integer.parseInt(tokens[4]);
                String givenWord = tokens[5];
                //create a new LevelData object by passing the above variables in its constructor
                // add the new object to levelQuestionOneData list
                levelData.add(new LevelData(questionNumber, question, answer, hint, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("LevelData One Activity", "Error occur while reading on line" + tempString, e);
            e.printStackTrace();
        }
    }

    /*
        takes a level object as a parameter which will then be use to generate the game for user
        First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
        extract the question from the given object and present in the question board.
        assign each character to givenWordButtons
     */
    public void playLevel(LevelData levelDataObject)
    {
        // reset all the buttons to make sure no value associated with answer and word buttons are not assigned at the beginning of the game.
        resetButtons();

        // assign object's attribute 'question' to questionBoard TextView
        questionBoard.setText(levelDataObject.getQuestion());

        // assign given word value to each button
        // could use for loop as button will be stored in array
        for (int i = 0; i < givenWordButtons.length; i++) {
            givenWordButtons[i].setText(String.valueOf(levelDataObject.getGivenWord().charAt(i)));
        }
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
                    // will also use popup window (as custom popup window takes time to create)
                    // we are right now focusing on the basic thing first
                    Toast.makeText(this, "Not enough Coin to use hint or has used max hint allowance!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "Not enough Coin to skip the question!", Toast.LENGTH_LONG).show();
                    // will use popup window to let user know that user does not have sufficient amount of coin
                }
        }
    }

    /*
        clickGivenWord method
        - make disappear of the clicked button (the button given in the parameter)
        - increase the clickWordBtn count by 1
        - set the text from the button in the answer button (one that does not have a value yet)
        - do the answer validation if the clickWordBtn count gets to 3
     */
    public void clickGivenWord(Button wordButton) {
        disappearButton(wordButton);
        clickWordBtnCount++;
        setAnswer(String.valueOf(wordButton.getText()));
        if (clickWordBtnCount == 3) {
            validateAnswer(levelData.get(userQuestionNumber));
        }
    }

    /*
        clickAnswerButton method
        - do nothing and return if the setAnswerButtons at the given index is not false
        - otherwise, put back the button in the original place
        - set the answer button at index to an empty string
        - the false value for setAnswerButtons at index given
        - reduce the clickWordBtnCount again
     */
    public void clickAnswerButton(int arrayIndex) {
        if (!setAnswerButtons[arrayIndex]) {return; }
        else {
            putBackWordButton(answerButtons[arrayIndex]);
            answerButtons[arrayIndex].setText("");
            setAnswerButtons[arrayIndex] = false;
            clickWordBtnCount--;
        }
    }

    /*
        # accept a button as a parameter
        # compares the text from the given button with every word buttons; and put back (make appear and clickable) the button which has the same letter with the input button text
        # the input button is the answer button clicked by the user
     */
    public void putBackWordButton(Button answerButton) {
        String answerButtonText = String.valueOf(answerButton.getText());

        // use foreach loop to compare the given text with every single givenWord buttons
        // make reappear the button at index if the text stored are the same
        for (Button buttonAtIdx: givenWordButtons) {
            if(answerButtonText.equalsIgnoreCase(String.valueOf(buttonAtIdx.getText()))) {
                reappearButton(buttonAtIdx);
                return;
            }
        }
    }

    /*
        set answer accept a string input and checks:
        - if a button has been assigned with a value (an alphabet)
        - if yes, go to next button and if no, then assign the input text (which is the text of the clicked wordGivenButtons) to the answerButton.
        - set true to the setAnswerButton to know it has been assigned
     */
    public void setAnswer(String text) {
        for (int i = 0; i < answerButtons.length; i++) {
            if (!setAnswerButtons[i]) {
                answerButtons[i].setText(text);
                setAnswerButtons[i] = true;
                return;
            }
        }
    }

    /*
        - accepts a LevelData object as a parameter so that we know what object to pass when checking answer
        - compares the answer from user and answer within the LevelData object
        - If correct
            - check if the question is the last one in the level, and go to next level should the question be the last one for this level.
            - pass the next object in the LevelData Object list (next question in other words) for user to play
        - if incorrect
            - pass the same object for user to play again
        - also check if static variable for timer in Setting page set to as 1 or 'On' mode in other words
        - it yes, start the timer again
     */
    public void validateAnswer(LevelData levelData1Object) {
        // first, combine the letters assigned to the answer buttons from user pressing buttons
        String userAns = "";
        for (Button answerButton: answerButtons) {
            userAns = userAns + String.valueOf(answerButton.getText());
        }

        if (levelData1Object.getAnswer().equalsIgnoreCase(userAns)) {
            if (userQuestionNumber == levelData.size() - 1) {
                // reset the question number in database to zero as user has finished the level
                // when play again this level, will be started from question 1 again, which is zero index in the object and database
                userDb.userDao().updateQuestionNumber(0, 1);
                // go to next level page
                Intent intent = new Intent (this, LevelTwoActivity.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Answer is correct!", Toast.LENGTH_LONG).show();
            // increment coins amount by 10 for correct answer
            increaseCoin(10);
            clickWordBtnCount = 0;
            hintClickCount = 0;
            userQuestionNumber++;
            // update the question number in database with current question number
            userDb.userDao().updateQuestionNumber(userQuestionNumber, 1);
            // reset the timer
            countDownTimer.cancel();
            // check if timer is set as on to decide whether to set timer or not
            if (isTimerOn()) {
                setTimer(60000);
            }
            // pass the next question (object) to playLevel function
            playLevel(levelData.get(userQuestionNumber));
        } else {
            clickWordBtnCount = 0;
            // cancel the timer
            countDownTimer.cancel();
            // check if timer is set as on to decide whether to set timer or not
            if (isTimerOn()) {
                setTimer(60000);
            }
            playLevel(levelData.get(userQuestionNumber));
        }
    }

    /*
        set all wordGivenButtons to appear and set clickable to true
        set false for setAnswer variables
        set the answers buttons to empty string
     */
    public void resetButtons() {
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
    }

    /*
        this function set the alpha to full value and set button clickable to true
     */
    public void reappearButton(Button button) {
        button.animate().alpha(255).setDuration(50);
        button.setClickable(true);
    }

    /*
        set alpha value of a given button to zero and set false for setClickable
     */
    public void disappearButton(Button button) {
        button.animate().alpha(0).setDuration(50);
        button.setClickable(false);
    }

    /*
        # do nothing and return if all the setAnswer buttons are set as true
        # if the decided number of hints are already given, then return
        # if decreaseCoin does not return true, then exit the function
        # if the above stated conditions are not met, check every setAnswer buttons and do the following
            - if setAnswer bool value is false
            - increase clickWordBtnCount++ (as how many click of this helps the program know when to validate the answer)
            - store the letter at a button (if answer button 1 is not set yet) for the question in a temporary String variable
            - set the above temp variable as the given (in parameter) answer button text
            - set setAnswer button to true
            - validate the answer if clickWordBtnCount is 7
            - else return (exit the function)
     */
    public void giveHint() {
        // check if the hint has been given or not (one hint is only allowed for level 1)
        // the following method learned from - https://stackoverflow.com/questions/8260881/what-is-the-most-elegant-way-to-check-if-all-values-in-a-boolean-array-are-true
        if (Arrays.asList(setAnswerButtons).contains(false)) {
        } else { return; }

        if(hintClickCount == maxHintGiven) {
            return;
        }
        for (int i = 0; i < setAnswerButtons.length; i++) {
            if(!setAnswerButtons[i]) {
                clickWordBtnCount++;
                String hintLetter = String.valueOf(levelData.get(userQuestionNumber).getAnswer().charAt(i));
                answerButtons[i].setText(hintLetter);
                setAnswerButtons[i] = true;
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelData.get(userQuestionNumber));
                    return;
                } else { return;}
            }
        }
    }

    // Coin number Modification
    /*
        # check and return false if the user coin amount subtract amount passed in the parameter is less than zero
        # else - reduce the user coin amount by the given amount and update the text of coinButton1
        # also update the number of coin in database every time the coin is modified
     */
    public boolean decreaseCoin(int amount) {
        if (coinAmount - amount < 0) {
            return false;
        } else {
            coinAmount = coinAmount - amount;
            coinButton.setText(String.valueOf(coinAmount));
            userDb.userDao().updateCoin(coinAmount, 1);
            return true;
        }
    }

    /*
        takes the amount given in the parameter and update the coinAmount, coinButton text and coinAmount in the database
        does not need to return anything as we do not have to check the amount for increasing (increasing doesn't have any requirements to check)
     */
    public void increaseCoin(int amount) {
        coinAmount = coinAmount + amount;
        coinButton.setText(String.valueOf(coinAmount));
        userDb.userDao().updateCoin(coinAmount, 1);
    }

    public void setTimer(int timeInMilliSeconds) {
        countDownTimer = new CountDownTimer(timeInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                int leftMinutes = (int)(l / 60000);
                int leftSeconds = (int) (l % 60000 / 1000);
                StringBuilder remainTime = new StringBuilder("");
                remainTime.append(leftMinutes);
                remainTime.append(":");
                if (leftSeconds < 10) { remainTime.append(0);}
                remainTime.append(leftSeconds);
                timer.setText(remainTime);
            }

            @Override
            public void onFinish() {
                validateAnswer(levelData.get(userQuestionNumber));
                timer.setText("DONE!");
            }
        }.start();
    }

    public boolean isTimerOn() {
        if(SettingActivity.timerSet == 1) {
            return true;
        } else {
            return false;
        }
    }

}
