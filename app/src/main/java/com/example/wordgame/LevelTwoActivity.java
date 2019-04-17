package com.example.wordgame;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

public class LevelTwoActivity extends AppCompatActivity implements View.OnClickListener {

    // load the font for text in this level
    //Typeface customFont = Typeface.createFromAsset(getAssets(), "assets/fonts/felaFromAssets.otf");

    // Level object list for this level (Level 2) - contains multiple questions to play for the level
    private List<Level> levelTwoQuestion = new ArrayList<>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    private int clickWordBtnCount;

    private int hintClickCount;     // stored how many hint button is clicked for a given question
    private static final int maxHintGiven = 2;  // the number user can click the hint button in a question
    // hint button
    Button l2HintButton;

    TextView l2qBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    Boolean setAnswerBtn1, setAnswerBtn2, setAnswerBtn3, setAnswerBtn4, setAnswerBtn5;

    // Answer buttons - value will be assign when user click GivenWord buttons
    Button l2AnswerBtn1, l2AnswerBtn2, l2AnswerBtn3, l2AnswerBtn4, l2AnswerBtn5;

    // Coin Button
    private int coinAmount;
    Button coinButtonL2;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button l2GivenWordBtn1, l2GivenWordBtn2, l2GivenWordBtn3, l2GivenWordBtn4,
            l2GivenWordBtn5, l2GivenWordBtn6, l2GivenWordBtn7, l2GivenWordBtn8;
    //----------------------------------------------------------------------------------------------

    /*
        The onCreate function
        set value for some variables such as pressCount
        assign buttons created in this class with buttons from xml file
        assign all givenWords buttons to onClick function
        call playLevelTwo function which assign each object attributes to buttons, textView accordingly
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);

        // read level Two data from csv file (stored in raw directory) and instantiate Level object
        // add the Level object created from the file to levelTwoQuestion list
        readLevelTwoData();

        // the Level object at useQuestionNumber will be pass to playLevelTwo function to generate the game
        userQuestionNumber = 0; // temporarily set as 0, but modified when User class is created and loaded here

        hintClickCount = 0;

        // set pressAnswerCount to zero
        clickWordBtnCount = 0;
        // assign buttons and textView from xml file
        l2qBoard = findViewById(R.id.l2qBoard);

        // assign hint button to the hint button created in xml layout
        // assign the button to the onClick method for this view
        l2HintButton = findViewById(R.id.l2HintButton);
        l2HintButton.setOnClickListener(this);

        // assign buttons for answerButton
        l2AnswerBtn1 = findViewById(R.id.l2AnswerBtn1);
        l2AnswerBtn2 = findViewById(R.id.l2AnswerBtn2);
        l2AnswerBtn3 = findViewById(R.id.l2AnswerBtn3);
        l2AnswerBtn4 = findViewById(R.id.l2AnswerBtn4);
        l2AnswerBtn5 = findViewById(R.id.l2AnswerBtn5);
        setAnswerBtn1 = false; setAnswerBtn2 = false; setAnswerBtn3 = false;
        setAnswerBtn4 = false; setAnswerBtn5 = false;

        // ================ coin section =======================
        // assign button for coinButton
        coinAmount = 50;
        coinButtonL2 = findViewById(R.id.coinButtonL2);
        coinButtonL2.setText(String.valueOf(coinAmount));
        coinButtonL2.setOnClickListener(this);

        // assign buttons for givenWord buttons
        l2GivenWordBtn1 = findViewById(R.id.l2GivenWordBtn1);
        l2GivenWordBtn2 = findViewById(R.id.l2GivenWordBtn2);
        l2GivenWordBtn3 = findViewById(R.id.l2GivenWordBtn3);
        l2GivenWordBtn4 = findViewById(R.id.l2GivenWordBtn4);
        l2GivenWordBtn5 = findViewById(R.id.l2GivenWordBtn5);
        l2GivenWordBtn6 = findViewById(R.id.l2GivenWordBtn6);
        l2GivenWordBtn7 = findViewById(R.id.l2GivenWordBtn7);
        l2GivenWordBtn8 = findViewById(R.id.l2GivenWordBtn8);

        // assign all givenWord buttons to onClick method created for this view
        l2GivenWordBtn1.setOnClickListener(this);
        l2GivenWordBtn2.setOnClickListener(this);
        l2GivenWordBtn3.setOnClickListener(this);
        l2GivenWordBtn4.setOnClickListener(this);
        l2GivenWordBtn5.setOnClickListener(this);
        l2GivenWordBtn6.setOnClickListener(this);
        l2GivenWordBtn7.setOnClickListener(this);
        l2GivenWordBtn8.setOnClickListener(this);

        // assign all answerWord buttons to onClick method created for this view
        // creating a method for each button will be a messy codes; thus, share all buttons in one method
        l2AnswerBtn1.setOnClickListener(this);
        l2AnswerBtn2.setOnClickListener(this);
        l2AnswerBtn3.setOnClickListener(this);
        l2AnswerBtn4.setOnClickListener(this);
        l2AnswerBtn5.setOnClickListener(this);

        // for clarity purpose, a playLevelOne method is created and call from here
        playLevelTwo(levelTwoQuestion.get(userQuestionNumber));

    }

    /*
        readLevelTwoData
        Create an input stream to read files stored in this project
        create a bufferedReader for the input stream
        split the line in the csv file using comma as a token
        store each token at value (0..5) in a temp variable which will then be used to create a Level Object
        add the newly created Level object in a list (userLevelQuestion)
        learned from Android Developer website and this (https://www.youtube.com/watch?v=i-TqNzUryn8)
     */
    public void readLevelTwoData() {
        InputStream myInputStream = getResources().openRawResource(R.raw.level_two_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myInputStream, Charset.forName("UTF-8")));

        String textLine = "";
        try {
            while ((textLine = bufferedReader.readLine()) != null) {
                String[] tokens = textLine.split(",");

                // Read the data and create a Level object, then add them in levelOneQuestion list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                String hint = tokens[3];
                int levelNumber = Integer.parseInt(tokens[4]);
                String givenWord = tokens[5];
                //create a new Level object by passing the above variables in its constructor
                // add the new object to levelTwoQuestion list
                levelTwoQuestion.add(new Level(questionNumber, question, answer, hint, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("Level Two Activity", "Error occur while reading on line" + textLine, e);
            e.printStackTrace();
        }
    }

    /*
        takes a level object as a parameter which will then be use to generate the game for user
        First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
        extract the question from the given object and present in the question board.
        assign each character to givenWordButtons
     */
    public void playLevelTwo(Level level2Object)
    {
        // reset all the buttons to make sure no value associated with answer and word buttons are not assigned at the beginning of the game.
        resetButtons();

        // assign object question attribute to questionBoard TextView
        l2qBoard.setText(level2Object.getQuestion());

        // assign given word value to each button
        l2GivenWordBtn1.setText(String.valueOf(level2Object.getGivenWord().charAt(0)));
        l2GivenWordBtn2.setText(String.valueOf(level2Object.getGivenWord().charAt(1)));
        l2GivenWordBtn3.setText(String.valueOf(level2Object.getGivenWord().charAt(2)));
        l2GivenWordBtn4.setText(String.valueOf(level2Object.getGivenWord().charAt(3)));
        l2GivenWordBtn5.setText(String.valueOf(level2Object.getGivenWord().charAt(4)));
        l2GivenWordBtn6.setText(String.valueOf(level2Object.getGivenWord().charAt(5)));
        l2GivenWordBtn7.setText(String.valueOf(level2Object.getGivenWord().charAt(6)));
        l2GivenWordBtn8.setText(String.valueOf(level2Object.getGivenWord().charAt(7)));
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
                disappearButton(l2GivenWordBtn1);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn1.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn2:
                disappearButton(l2GivenWordBtn2);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn2.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn3:
                disappearButton(l2GivenWordBtn3);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn3.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn4:
                disappearButton(l2GivenWordBtn4);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn4.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn5:
                disappearButton(l2GivenWordBtn5);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn5.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                 break;
            case R.id.l2GivenWordBtn6:
                disappearButton(l2GivenWordBtn6);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn6.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn7:
                disappearButton(l2GivenWordBtn7);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn7.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn8:
                disappearButton(l2GivenWordBtn8);
                clickWordBtnCount++;
                setAnswer(String.valueOf(l2GivenWordBtn8.getText()));
                if (clickWordBtnCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2AnswerBtn1:
                if (!setAnswerBtn1) {return; }
                else {
                    putBackWordButton(l2AnswerBtn1);
                    l2AnswerBtn1.setText("");
                    setAnswerBtn1 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l2AnswerBtn2:
                if (!setAnswerBtn2) {return; }
                else {
                    putBackWordButton(l2AnswerBtn2);
                    l2AnswerBtn2.setText("");
                    setAnswerBtn2 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l2AnswerBtn3:
                if (!setAnswerBtn3) {return; }
                else {
                    putBackWordButton(l2AnswerBtn3);
                    l2AnswerBtn3.setText("");
                    setAnswerBtn3 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l2AnswerBtn4:
                if (!setAnswerBtn4) {return; }
                else {
                    putBackWordButton(l2AnswerBtn4);
                    l2AnswerBtn4.setText("");
                    setAnswerBtn4 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l2AnswerBtn5:
                if (!setAnswerBtn5) {return; }
                else {
                    putBackWordButton(l2AnswerBtn5);
                    l2AnswerBtn5.setText("");
                    setAnswerBtn5 = false;
                    clickWordBtnCount--;
                }
                break;
            case R.id.l2HintButton:
                if(hintClickCount < maxHintGiven) {
                    giveHint();
                    hintClickCount++;
                }
            case R.id.coinButtonL2:
                break;
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
            l2AnswerBtn1.setText(text);
            setAnswerBtn1 = true;
            return;
        }
        if (!setAnswerBtn2) {
            l2AnswerBtn2.setText(text);
            setAnswerBtn2 = true;
            return;
        }
        if (!setAnswerBtn3) {
            l2AnswerBtn3.setText(text);
            setAnswerBtn3 = true;
            return;
        }
        if (!setAnswerBtn4) {
            l2AnswerBtn4.setText(text);
            setAnswerBtn4 = true;
            return;
        }
        if (!setAnswerBtn5) {
            l2AnswerBtn5.setText(text);
            setAnswerBtn5 = true;
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
    public void validateAnswer(Level level2Object) {
        // first, combine the letters assigned to the answer buttons from user pressing buttons
        String userAns = String.valueOf(l2AnswerBtn1.getText())
                + String.valueOf(l2AnswerBtn2.getText())
                + String.valueOf(l2AnswerBtn3.getText())
                + String.valueOf(l2AnswerBtn4.getText())
                + String.valueOf(l2AnswerBtn5.getText());

        if (level2Object.getAnswer().equalsIgnoreCase(userAns)) {
            if (userQuestionNumber == 2) {
                // save coins and questionNumber to drive for future and other levels
                // go to back to level page
                Intent intent = new Intent (this, LevelThreeActivity.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Answer is correct!", Toast.LENGTH_LONG).show();
            coinAmount = coinAmount + 10;
            coinButtonL2.setText(String.valueOf(coinAmount));
            clickWordBtnCount = 0;
            hintClickCount = 0;
            userQuestionNumber++;
            playLevelTwo(levelTwoQuestion.get(userQuestionNumber));
        } else {
            clickWordBtnCount = 0;
            playLevelTwo(levelTwoQuestion.get(userQuestionNumber));
        }


    }

    /*
        set all wordGivenButtons to appear and set clickable to true
        set false for setAnswer variables
        set the answers buttons to empty string
     */
    public void resetButtons() {
        // make re-appear wordButtons
        reappearButton(l2GivenWordBtn1);
        reappearButton(l2GivenWordBtn2);
        reappearButton(l2GivenWordBtn3);
        reappearButton(l2GivenWordBtn4);
        reappearButton(l2GivenWordBtn5);
        reappearButton(l2GivenWordBtn6);
        reappearButton(l2GivenWordBtn7);
        reappearButton(l2GivenWordBtn8);

        // set to false for setAnswers buttons as we need this value to check if the answerButton is filled with letter
        setAnswerBtn1 = false;
        setAnswerBtn2 = false;
        setAnswerBtn3 = false;
        setAnswerBtn4 = false;
        setAnswerBtn5 = false;

        // Empty the any letter assign to answer buttons
        l2AnswerBtn1.setText("");
        l2AnswerBtn2.setText("");
        l2AnswerBtn3.setText("");
        l2AnswerBtn4.setText("");
        l2AnswerBtn5.setText("");
    }

    /*
        # accept a button as a parameter
        # compares the text from the given button with every word buttons; and put back (make appear and clickable) the button which has the same letter with the input button text
        # the input button is the answer button clicked by the user
     */
    public void putBackWordButton(Button answerButton) {
        String answerButtonText = String.valueOf(answerButton.getText());
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn1.getText())) && !l2GivenWordBtn1.isClickable()) {
            reappearButton(l2GivenWordBtn1);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn2.getText())) && !l2GivenWordBtn2.isClickable()) {
            answerButton.setText("");
            reappearButton(l2GivenWordBtn2);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn3.getText())) && !l2GivenWordBtn3.isClickable()) {
            reappearButton(l2GivenWordBtn3);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn4.getText())) && !l2GivenWordBtn4.isClickable()) {
            reappearButton(l2GivenWordBtn4);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn5.getText())) && !l2GivenWordBtn5.isClickable()) {
            reappearButton(l2GivenWordBtn5);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn6.getText())) && !l2GivenWordBtn6.isClickable()) {
            reappearButton(l2GivenWordBtn6);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn7.getText())) && !l2GivenWordBtn7.isClickable()) {
            reappearButton(l2GivenWordBtn7);
            return;
        }
        if(answerButtonText.equalsIgnoreCase(String.valueOf(l2GivenWordBtn8.getText())) && !l2GivenWordBtn8.isClickable()) {
            reappearButton(l2GivenWordBtn8);
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
            - validate the answer if clickWordBtnCount is 7
            - else return (exit the function)
     */
    public void giveHint() {
        // check if the hint has been given or not (one hint is only allowed for level 1)
        if(setAnswerBtn1 && setAnswerBtn2 && setAnswerBtn3 && setAnswerBtn4 && setAnswerBtn5) {
            return;
        }
        if(hintClickCount == maxHintGiven) {
            return;
        }
        if(!setAnswerBtn1) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelTwoQuestion.get(userQuestionNumber).getAnswer().charAt(0));
            l2AnswerBtn1.setText(hintLetter);
            setAnswerBtn1 = true;
            if (clickWordBtnCount == 5) {
                validateAnswer(levelTwoQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn2) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelTwoQuestion.get(userQuestionNumber).getAnswer().charAt(1));
            l2AnswerBtn2.setText(hintLetter);
            setAnswerBtn2 = true;
            if (clickWordBtnCount == 5) {
                validateAnswer(levelTwoQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn3) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelTwoQuestion.get(userQuestionNumber).getAnswer().charAt(2));
            l2AnswerBtn3.setText(hintLetter);
            setAnswerBtn3 = true;
            if (clickWordBtnCount == 5) {
                validateAnswer(levelTwoQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn4) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelTwoQuestion.get(userQuestionNumber).getAnswer().charAt(3));
            l2AnswerBtn4.setText(hintLetter);
            setAnswerBtn4 = true;
            if (clickWordBtnCount == 5) {
                validateAnswer(levelTwoQuestion.get(userQuestionNumber));
            } else { return;}
        }
        if(!setAnswerBtn5) {
            clickWordBtnCount++;
            String hintLetter = String.valueOf(levelTwoQuestion.get(userQuestionNumber).getAnswer().charAt(4));
            l2AnswerBtn5.setText(hintLetter);
            setAnswerBtn5 = true;
            if (clickWordBtnCount == 5) {
                validateAnswer(levelTwoQuestion.get(userQuestionNumber));
            }
        }
    }

}
