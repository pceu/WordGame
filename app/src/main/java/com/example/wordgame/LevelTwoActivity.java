package com.example.wordgame;

import android.content.Intent;
import android.graphics.Typeface;
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
    private int pressAnsCount;

    TextView l2qBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    Boolean setAnswerBtn1, setAnswerBtn2, setAnswerBtn3, setAnswerBtn4, setAnswerBtn5;

    // Answer buttons - value will be assign when user click GivenWord buttons
    Button l2AnswerBtn1, l2AnswerBtn2, l2AnswerBtn3, l2AnswerBtn4, l2AnswerBtn5;

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

        // set pressAnswerCount to zero
        pressAnsCount = 0;
        // assign buttons and textView from xml file
        l2qBoard = findViewById(R.id.l2qBoard);

        // assign buttons for answerButton
        l2AnswerBtn1 = findViewById(R.id.l2AnswerBtn1);
        l2AnswerBtn2 = findViewById(R.id.l2AnswerBtn2);
        l2AnswerBtn3 = findViewById(R.id.l2AnswerBtn3);
        l2AnswerBtn4 = findViewById(R.id.l2AnswerBtn4);
        l2AnswerBtn5 = findViewById(R.id.l2AnswerBtn5);
        setAnswerBtn1 = false; setAnswerBtn2 = false; setAnswerBtn3 = false;
        setAnswerBtn4 = false; setAnswerBtn5 = false;

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
        - each time a button is pressed/clicked, pressAnsCount is increment to decide whether user has filled all answer buttons
        - Once it gets to 5 for pressAnsCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l2GivenWordBtn1:
                l2GivenWordBtn1.animate().alpha(0).setDuration(50);
                l2GivenWordBtn1.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn1.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn2:
                l2GivenWordBtn2.animate().alpha(0).setDuration(50);
                l2GivenWordBtn2.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn2.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn3:
                l2GivenWordBtn3.animate().alpha(0).setDuration(50);
                l2GivenWordBtn3.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn3.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn4:
                l2GivenWordBtn4.animate().alpha(0).setDuration(50);
                l2GivenWordBtn4.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn4.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn5:
                l2GivenWordBtn5.animate().alpha(0).setDuration(50);
                l2GivenWordBtn5.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn5.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                 break;
            case R.id.l2GivenWordBtn6:
                l2GivenWordBtn6.animate().alpha(0).setDuration(50);
                l2GivenWordBtn6.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn6.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn7:
                l2GivenWordBtn7.animate().alpha(0).setDuration(50);
                l2GivenWordBtn7.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn7.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l2GivenWordBtn8:
                l2GivenWordBtn8.animate().alpha(0).setDuration(50);
                l2GivenWordBtn8.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn8.getText()));
                if (pressAnsCount == 5) {
                    validateAnswer(levelTwoQuestion.get(userQuestionNumber));
                }
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
            pressAnsCount = 0;
            userQuestionNumber++;
            playLevelTwo(levelTwoQuestion.get(userQuestionNumber));
        } else {
            pressAnsCount = 0;
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
        l2GivenWordBtn1.animate().alpha(255).setDuration(50);
        l2GivenWordBtn1.setClickable(true);
        l2GivenWordBtn2.animate().alpha(255).setDuration(50);
        l2GivenWordBtn2.setClickable(true);
        l2GivenWordBtn3.animate().alpha(255).setDuration(50);
        l2GivenWordBtn3.setClickable(true);
        l2GivenWordBtn4.animate().alpha(255).setDuration(50);
        l2GivenWordBtn4.setClickable(true);
        l2GivenWordBtn5.animate().alpha(255).setDuration(50);
        l2GivenWordBtn5.setClickable(true);
        l2GivenWordBtn6.animate().alpha(255).setDuration(50);
        l2GivenWordBtn6.setClickable(true);
        l2GivenWordBtn7.animate().alpha(255).setDuration(50);
        l2GivenWordBtn7.setClickable(true);
        l2GivenWordBtn8.animate().alpha(255).setDuration(50);
        l2GivenWordBtn8.setClickable(true);

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
}
