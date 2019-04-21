package com.example.wordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

public class LevelOneActivity extends AppCompatActivity implements View.OnClickListener {


    // Level object list for this level (Level 1) - contains multiple questions to play for the level
    private List<Level> levelOneQuestion = new ArrayList<>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    // use to track how many user has clicked given wordButtons
    // will increase whenever user clicks the wordButtons, and decrease whenever the answer is erased
    private int clickWordBtnCount;

    private int hintClickCount;     // stored how many hint button is clicked for a given question
    private static final int maxHintGiven = 1;  // the number user can click the hint button in a question

    TextView l1qBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    Boolean setAnswerBtn1, setAnswerBtn2, setAnswerBtn3;

    // Answer buttons - value will be assign when user click GivenWord buttons
    Button l1AnswerBtn1, l1AnswerBtn2, l1AnswerBtn3;

    // Coin Button
    private int coinAmount;
    Button coinButtonL1;

    // Skip Button
    Button l1SkipButton;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button l1GivenWordBtn1, l1GivenWordBtn2, l1GivenWordBtn3, l1GivenWordBtn4,
            l1GivenWordBtn5, l1GivenWordBtn6;
    //----------------------------------------------------------------------------------------------

    // hint button
    Button l1HintButton;

    // Background Music
    MediaPlayer bkgrdmsc;
    private int lastbkgdchecked = SettingActivity.bkgdchecked;
    /*
        The onCreate function
        set value for some variables such as pressCount
        assign buttons created in this class with buttons from xml file
        assign all givenWords buttons to onClick function
        call playLevelOne function which assign each object attributes to buttons, textView accordingly
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        // Background Music playing code
        if (lastbkgdchecked == 0) {
            bkgrdmsc = MediaPlayer.create(LevelOneActivity.this, R.raw.backgroundmusic);
            bkgrdmsc.setLooping(true);
            bkgrdmsc.start();
        }

        // read level one data from csv file (stored in raw directory) and instantiate Level object
        // add the Level object created from the file to levelOneQuestion list
        readLevelOneData();

        // the Level object at useQuestionNumber will be pass to playLevelOne function to generate the game
        userQuestionNumber = 0; // temporarily set as 0, but modified when User class is created and loaded here
        hintClickCount = 0;

        // set pressAnswerCount to zero
        clickWordBtnCount = 0;
        Toast.makeText(this, String.valueOf(clickWordBtnCount), Toast.LENGTH_LONG).show();
        // assign buttons and textView from xml file
        l1qBoard = findViewById(R.id.l1qBoard);

        // assign hint button to the hint button created in xml layout
        // assign the button to the onClick method for this view
        l1HintButton = findViewById(R.id.l1HintButton);
        l1HintButton.setOnClickListener(this);

        // assign buttons for answerButton
        l1AnswerBtn1 = findViewById(R.id.l1AnswerBtn1);
        l1AnswerBtn2 = findViewById(R.id.l1AnswerBtn2);
        l1AnswerBtn3 = findViewById(R.id.l1AnswerBtn3);

        // ================ coin section =======================
        // assign button for coinButton
        coinAmount = 50;
        coinButtonL1 = findViewById(R.id.coinButtonL1);
        coinButtonL1.setText(String.valueOf(coinAmount));
        coinButtonL1.setOnClickListener(this);

        // skip Question section==============================
        l1SkipButton = findViewById(R.id.l1SkipButton);
        l1SkipButton.setOnClickListener(this);

        setAnswerBtn1 = false; setAnswerBtn2 = false; setAnswerBtn3 = false;

        // assign buttons for givenWord buttons
        l1GivenWordBtn1 = findViewById(R.id.l1GivenWordBtn1);
        l1GivenWordBtn2 = findViewById(R.id.l1GivenWordBtn2);
        l1GivenWordBtn3 = findViewById(R.id.l1GivenWordBtn3);
        l1GivenWordBtn4 = findViewById(R.id.l1GivenWordBtn4);
        l1GivenWordBtn5 = findViewById(R.id.l1GivenWordBtn5);
        l1GivenWordBtn6 = findViewById(R.id.l1GivenWordBtn6);

        // assign all givenWord buttons to onClick method created for this view
        l1GivenWordBtn1.setOnClickListener(this);
        l1GivenWordBtn2.setOnClickListener(this);
        l1GivenWordBtn3.setOnClickListener(this);
        l1GivenWordBtn4.setOnClickListener(this);
        l1GivenWordBtn5.setOnClickListener(this);
        l1GivenWordBtn6.setOnClickListener(this);

        // assign all answerButtons to onCLick method created for this view
        l1AnswerBtn1.setOnClickListener(this);
        l1AnswerBtn2.setOnClickListener(this);
        l1AnswerBtn3.setOnClickListener(this);

        // for clarity purpose, a playLevelOne method is created and call from here
        playLevelOne(levelOneQuestion.get(userQuestionNumber));

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
        store each token at value (0..5) in a temp variable which will then be used to create a Level Object
        add the newly created Level object in a list (userLevelQuestion)
     */
    public void readLevelOneData() {
        InputStream myInputStream = getResources().openRawResource(R.raw.level_one_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myInputStream, Charset.forName("UTF-8")));

        String tempString = "";
        try {
            while ((tempString = bufferedReader.readLine()) != null) {
                String[] tokens = tempString.split(",");

                // Read the data and create a Level object, then add them in levelOneQuestion list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                String hint = tokens[3];
                int levelNumber = Integer.parseInt(tokens[4]);
                String givenWord = tokens[5];
                //create a new Level object by passing the above variables in its constructor
                // add the new object to levelOneQuestion list
                levelOneQuestion.add(new Level(questionNumber, question, answer, hint, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("Level One Activity", "Error occur while reading on line" + tempString, e);
            e.printStackTrace();
        }
    }

    /*
        takes a level object as a parameter which will then be use to generate the game for user
        First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
        extract the question from the given object and present in the question board.
        assign each character to givenWordButtons
     */
    public void playLevelOne(Level level1Object)
    {
        // reset all the buttons to make sure no value associated with answer and word buttons are not assigned at the beginning of the game.
        resetButtons();

        // assign object's attribute 'question' to questionBoard TextView
        l1qBoard.setText(level1Object.getQuestion());

        // assign given word value to each button
        l1GivenWordBtn1.setText(String.valueOf(level1Object.getGivenWord().charAt(0)));
        l1GivenWordBtn2.setText(String.valueOf(level1Object.getGivenWord().charAt(1)));
        l1GivenWordBtn3.setText(String.valueOf(level1Object.getGivenWord().charAt(2)));
        l1GivenWordBtn4.setText(String.valueOf(level1Object.getGivenWord().charAt(3)));
        l1GivenWordBtn5.setText(String.valueOf(level1Object.getGivenWord().charAt(4)));
        l1GivenWordBtn6.setText(String.valueOf(level1Object.getGivenWord().charAt(5)));
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
                disappearButton(l1GivenWordBtn1);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn1.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn2:
                disappearButton(l1GivenWordBtn2);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn2.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn3:
                disappearButton(l1GivenWordBtn3);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn3.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn4:
                disappearButton(l1GivenWordBtn4);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn4.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn5:
                disappearButton(l1GivenWordBtn5);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn5.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn6:
                disappearButton(l1GivenWordBtn6);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l1GivenWordBtn6.getText()));
                if (clickWordBtnCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1AnswerBtn1:
                if (!setAnswerBtn1) {return; }
                else {
                    putBackWordButton(l1AnswerBtn1);
                    l1AnswerBtn1.setText("");
                    setAnswerBtn1 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l1AnswerBtn2:
                if (!setAnswerBtn2) {return; }
                else {
                    putBackWordButton(l1AnswerBtn2);
                    l1AnswerBtn2.setText("");
                    setAnswerBtn2 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l1AnswerBtn3:
                if (!setAnswerBtn3) {return; }
                else {
                    putBackWordButton(l1AnswerBtn3);
                    l1AnswerBtn3.setText("");
                    setAnswerBtn3 = false;
                    clickWordBtnCount--;
                }
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
                    playLevelOne(levelOneQuestion.get(userQuestionNumber));
                } else {
                    Toast.makeText(this, "Not enough Coin to skip the question!", Toast.LENGTH_LONG).show();
                    // will use popup window to let user know that user does not have sufficient amount of coin
                }
        }
    }

    /*
        # accept a button as a parameter
        # compares the text from the given button with every word buttons; and put back (make appear and clickable) the button which has the same letter with the input button text
        # the input button is the answer button clicked by the user
     */
    public void putBackWordButton(Button answerButton) {
        String answerButtonText = String.valueOf(answerButton.getText());
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn1.getText()))) {
            reappearButton(l1GivenWordBtn1);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn2.getText()))) {
            answerButton.setText("");
            reappearButton(l1GivenWordBtn2);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn3.getText()))) {
            reappearButton(l1GivenWordBtn3);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn4.getText()))) {
            reappearButton(l1GivenWordBtn4);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn5.getText()))) {
            reappearButton(l1GivenWordBtn5);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l1GivenWordBtn6.getText()))) {
            reappearButton(l1GivenWordBtn6);
        }
    }

    /*
        set answer accept a string input and checks:
        - if a button has been assigned with a value (an alphabet)
        - if yes, go to next button and if no, then assign the input text (which is the text of the clicked wordGivenButtons) to the answerButton.
        - set true to the setAnswerButton to know it has been assigned
     */
    public void setAnswer(String text) {
        if (!setAnswerBtn1) {
            l1AnswerBtn1.setText(text);
            setAnswerBtn1 = true;
            return;
        }
        if (!setAnswerBtn2) {
            l1AnswerBtn2.setText(text);
            setAnswerBtn2 = true;
            return;
        }
        if (!setAnswerBtn3) {
            l1AnswerBtn3.setText(text);
            setAnswerBtn3 = true;
        }
    }

    /*
        - accepts a Level object as a parameter so that we know what object to pass when checking answer
        - compares the answer from user and answer within the Level object
        - If correct
            - check if the question is the last one in the level, and go to next level should the question be the last one for this level.
            - pass the next object in the Level Object list (next question in other words) for user to play
        - if incorrect
            - pass the same object for user to play again
     */
    public void validateAnswer(Level level1Object) {
        // first, combine the letters assigned to the answer buttons from user pressing buttons
        String userAns = String.valueOf(l1AnswerBtn1.getText())
                        + String.valueOf(l1AnswerBtn2.getText())
                        + String.valueOf(l1AnswerBtn3.getText());

        if (level1Object.getAnswer().equalsIgnoreCase(userAns)) {
            if (userQuestionNumber == levelOneQuestion.size() - 1) {
                // save coins and questionNumber to drive for future and other levels
                // go to next level page
                Intent intent = new Intent (this, LevelTwoActivity.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Answer is correct!", Toast.LENGTH_LONG).show();
            coinAmount = coinAmount + 10;
            coinButtonL1.setText(String.valueOf(coinAmount));
            clickWordBtnCount = 0;
            hintClickCount = 0;
            userQuestionNumber++;
            playLevelOne(levelOneQuestion.get(userQuestionNumber));
        } else {
            clickWordBtnCount = 0;
            playLevelOne(levelOneQuestion.get(userQuestionNumber));
        }
    }

    /*
        set all wordGivenButtons to appear and set clickable to true
        set false for setAnswer variables
        set the answers buttons to empty string
     */
    public void resetButtons() {
        // make re-appear wordButtons
        reappearButton(l1GivenWordBtn1);
        reappearButton(l1GivenWordBtn2);
        reappearButton(l1GivenWordBtn3);
        reappearButton(l1GivenWordBtn4);
        reappearButton(l1GivenWordBtn5);
        reappearButton(l1GivenWordBtn6);

        // set to false for setAnswers buttons as we need this value to check if the answerButton is filled with letter
        setAnswerBtn1 = false;
        setAnswerBtn2 = false;
        setAnswerBtn3 = false;

        // Empty the any letter assign to answer buttons
        l1AnswerBtn1.setText("");
        l1AnswerBtn2.setText("");
        l1AnswerBtn3.setText("");
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
        if(setAnswerBtn1 && setAnswerBtn2 && setAnswerBtn3) {
            return;
        }
        if(hintClickCount == maxHintGiven) {
            return;
        }
        if(!setAnswerBtn1) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelOneQuestion.get(userQuestionNumber).getAnswer().charAt(0));
            l1AnswerBtn1.setText(hintLetter);
            setAnswerBtn1 = true;
            if (clickWordBtnCount == 3) {
                validateAnswer(levelOneQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn2) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelOneQuestion.get(userQuestionNumber).getAnswer().charAt(1));
            l1AnswerBtn2.setText(hintLetter);
            setAnswerBtn2 = true;
            if (clickWordBtnCount == 3) {
                validateAnswer(levelOneQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn3) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelOneQuestion.get(userQuestionNumber).getAnswer().charAt(2));
            l1AnswerBtn3.setText(hintLetter);
            setAnswerBtn3 = true;
            if (clickWordBtnCount == 3) {
                validateAnswer(levelOneQuestion.get(userQuestionNumber));
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
            coinButtonL1.setText(String.valueOf(coinAmount));
            return true;
        }
    }

}
