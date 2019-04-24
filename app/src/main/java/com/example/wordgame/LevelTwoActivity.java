package com.example.wordgame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Arrays;

public class LevelTwoActivity extends AppCompatActivity implements View.OnClickListener, Level {

    // load the font for text in this level
    //Typeface customFont = Typeface.createFromAsset(getAssets(), "assets/fonts/felaFromAssets.otf");

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    // use to track how many user has clicked given wordButtons
    // will increase whenever user clicks the wordButtons, and decrease whenever the answer is erased
    private int clickWordBtnCount;

    // hint button
    private Button hintButton;
    private int hintClickCount;     // stored how many hint button is clicked for a given question
    // the number user can click the hint button in a question
    static final int maxHintGiven = 2;
    // question board for a level
    private TextView questionBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    // boolean value at index 0 represent setAnswerBtn1, index 1 for setAnswerBtn2 and so on
    // Level 2 has 5 answer related buttons
    private Boolean [] setAnswerButtons = new Boolean[5];

    // Answer buttons - value will be assign when user click GivenWord buttons
    private Button[] answerButtons = new Button[5];

    // Coin Button
    int coinAmount;
    Button coinButton;

    // Skip Button
    Button skipButton;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button [] givenWordButtons = new Button[8];
    // Background Music
    MediaPlayer bkgrdmsc;

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

        // Background Music playing code
        bkgrdmsc = MediaPlayer.create(LevelTwoActivity.this, R.raw.backgroundmusic);
        bkgrdmsc.setLooping(true);
        bkgrdmsc.start();

        // read level Two data from csv file (stored in raw directory) and instantiate LevelData object
        // add the LevelData object created from the file to levelQuestionTwoData list
        readLevelTwoData();

        // the LevelData object at useQuestionNumber will be pass to playLevel function to generate the game
        userQuestionNumber = 0; // temporarily set as 0, but modified when User class is created and loaded here

        hintClickCount = 0;

        // set pressAnswerCount to zero
        clickWordBtnCount = 0;
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
        coinAmount = 50;
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

    }

    // If user go out from game, the background music will turn off automatically.
    protected void onPause(){
        super.onPause();
        bkgrdmsc.release();
        finish();
    }

    /*
        readLevelTwoData
        Create an input stream to read files stored in this project
        create a bufferedReader for the input stream
        split the line in the csv file using comma as a token
        store each token at value (0..5) in a temp variable which will then be used to create a LevelData Object
        add the newly created LevelData object in a list (userLevelQuestion)
        learned from Android Developer website and this (https://www.youtube.com/watch?v=i-TqNzUryn8)
     */
    public void readLevelTwoData() {
        InputStream myInputStream = getResources().openRawResource(R.raw.level_two_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myInputStream, Charset.forName("UTF-8")));

        String textLine = "";
        try {
            while ((textLine = bufferedReader.readLine()) != null) {
                String[] tokens = textLine.split(",");

                // Read the data and create a LevelData object, then add them in levelOneQuestion list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                String hint = tokens[3];
                int levelNumber = Integer.parseInt(tokens[4]);
                String givenWord = tokens[5];
                //create a new LevelData object by passing the above variables in its constructor
                // add the new object to levelQuestionTwoData list
                levelData.add(new LevelData(questionNumber, question, answer, hint, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("LevelData Two Activity", "Error occur while reading on line" + textLine, e);
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
        - Once it gets to 5 for clickWordBtnCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
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
                }
            case R.id.coinButtonL2:
                break;
            case R.id.l2SkipButton:
                if (decreaseCoin(30)) {
                    userQuestionNumber++;
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
        if (clickWordBtnCount == 5) {
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
     */
    public void validateAnswer(LevelData levelData2Object) {
        // first, combine the letters assigned to the answer buttons from user pressing buttons
        String userAns = "";
        for (Button answerButton: answerButtons) {
            userAns = userAns + String.valueOf(answerButton.getText());
        }

        if (levelData2Object.getAnswer().equalsIgnoreCase(userAns)) {
            coinAmount = coinAmount + 10;
            coinButton.setText(String.valueOf(coinAmount));
            clickWordBtnCount = 0;
            hintClickCount = 0;
            if (userQuestionNumber == levelData.size() - 1) {
                // save coins and questionNumber to drive for future and other levels
                // go to back to level page
                Intent intent = new Intent (this, LevelThreeActivity.class);
                startActivity(intent);
                return;
            }
            userQuestionNumber++;
            playLevel(levelData.get(userQuestionNumber));
        } else {
            clickWordBtnCount = 0;
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
    public  void disappearButton(Button button) {
        button.animate().alpha(0).setDuration(50);
        button.setClickable(false);
    }

    /*
        # do nothing and return if all the setAnswer buttons are set as true
        # if the decided number of hints are already given, then return
        # if the above stated conditions are not met, check every setAnswer buttons and do the following
            - if setAnswer bool value is false
            - increase clickWordBtnCount++ (as how many click of this helps the program know when to validate the answer)
            - store the letter at a button (if answer button 1 is not set yet) for the question in a temporary String variable
            - set the above temp variable as the given (in parameter) answer button text
            - set setAnswer button to true
            - validate the answer if clickWordBtnCount is 5
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
                if (clickWordBtnCount == 5) {
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
     */
    public boolean decreaseCoin(int amount) {
        if (coinAmount - amount < 0) {
            return false;
        } else {
            coinAmount = coinAmount - amount;
            coinButton.setText(String.valueOf(coinAmount));
            return true;
        }
    }

}
