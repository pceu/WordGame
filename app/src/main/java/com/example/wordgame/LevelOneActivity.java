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

public class LevelOneActivity extends AppCompatActivity implements View.OnClickListener {

    // Level object list for this level (Level 1) - contains multiple questions to play for the level
    private List<Level> levelOneQuestion = new ArrayList<>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    private int pressAnsCount;

    TextView l1qBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    Boolean setAnswerBtn1, setAnswerBtn2, setAnswerBtn3;

    // Answer buttons - value will be assign when user click GivenWord buttons
    Button l1AnswerBtn1, l1AnswerBtn2, l1AnswerBtn3;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button l1GivenWordBtn1, l1GivenWordBtn2, l1GivenWordBtn3, l1GivenWordBtn4,
            l1GivenWordBtn5, l1GivenWordBtn6;
    //----------------------------------------------------------------------------------------------


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

        // read level one data from csv file (stored in raw directory) and instantiate Level object
        // add the Level object created from the file to levelOneQuestion list
        readLevelOneData();

        // the Level object at useQuestionNumber will be pass to playLevelOne function to generate the game
        userQuestionNumber = 0; // temporarily set as 0, but modified when User class is created and loaded here

        // set pressAnswerCount to zero
        pressAnsCount = 0;
        // assign buttons and textView from xml file
        l1qBoard = findViewById(R.id.l1qBoard);

        // assign buttons for answerButton
        l1AnswerBtn1 = findViewById(R.id.l1AnswerBtn1);
        l1AnswerBtn2 = findViewById(R.id.l1AnswerBtn2);
        l1AnswerBtn3 = findViewById(R.id.l1AnswerBtn3);

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

        // for clarity purpose, a playLevelOne method is created and call from here
        playLevelOne(levelOneQuestion.get(userQuestionNumber));

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
        - each time a button is pressed/clicked, pressAnsCount is increment to decide whether user has filled all answer buttons
        - Once it gets to 3 for pressAnsCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l1GivenWordBtn1:
                l1GivenWordBtn1.animate().alpha(0).setDuration(50);
                l1GivenWordBtn1.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn1.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn2:
                l1GivenWordBtn2.animate().alpha(0).setDuration(50);
                l1GivenWordBtn2.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn2.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn3:
                l1GivenWordBtn3.animate().alpha(0).setDuration(50);
                l1GivenWordBtn3.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn3.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn4:
                l1GivenWordBtn4.animate().alpha(0).setDuration(50);
                l1GivenWordBtn4.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn4.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn5:
                l1GivenWordBtn5.animate().alpha(0).setDuration(50);
                l1GivenWordBtn5.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn5.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l1GivenWordBtn6:
                l1GivenWordBtn6.animate().alpha(0).setDuration(50);
                l1GivenWordBtn6.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l1GivenWordBtn6.getText()));
                if (pressAnsCount == 3) {
                    validateAnswer(levelOneQuestion.get(userQuestionNumber));
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
            pressAnsCount = 0;
            userQuestionNumber++;
            playLevelOne(levelOneQuestion.get(userQuestionNumber));
        } else {
            pressAnsCount = 0;
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
        l1GivenWordBtn1.animate().alpha(255).setDuration(50);
        l1GivenWordBtn1.setClickable(true);
        l1GivenWordBtn2.animate().alpha(255).setDuration(50);
        l1GivenWordBtn2.setClickable(true);
        l1GivenWordBtn3.animate().alpha(255).setDuration(50);
        l1GivenWordBtn3.setClickable(true);
        l1GivenWordBtn4.animate().alpha(255).setDuration(50);
        l1GivenWordBtn4.setClickable(true);
        l1GivenWordBtn5.animate().alpha(255).setDuration(50);
        l1GivenWordBtn5.setClickable(true);
        l1GivenWordBtn6.animate().alpha(255).setDuration(50);
        l1GivenWordBtn6.setClickable(true);

        // set to false for setAnswers buttons as we need this value to check if the answerButton is filled with letter
        setAnswerBtn1 = false;
        setAnswerBtn2 = false;
        setAnswerBtn3 = false;

        // Empty the any letter assign to answer buttons
        l1AnswerBtn1.setText("");
        l1AnswerBtn2.setText("");
        l1AnswerBtn3.setText("");
    }

}
