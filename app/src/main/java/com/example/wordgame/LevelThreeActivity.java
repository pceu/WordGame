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

public class LevelThreeActivity extends AppCompatActivity implements View.OnClickListener {


    // Level object list for this level (Level 3) - contains multiple questions to play for the level
    private List<Level> levelThreeQuestion = new ArrayList<>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    private int pressAnsCount;

    TextView l3qBoard;

    //--------------- ANSWER BUTTONS SECTION-------------------------
    // Boolean variables for tracking whether the answer has been set or not (for answer buttons)
    Boolean setAnswerBtn1, setAnswerBtn2, setAnswerBtn3, setAnswerBtn4, setAnswerBtn5, setAnswerBtn6, setAnswerBtn7;

    // Answer buttons - value will be assign when user click GivenWord buttons
    Button l3AnswerBtn1, l3AnswerBtn2, l3AnswerBtn3, l3AnswerBtn4, l3AnswerBtn5, l3AnswerBtn6, l3AnswerBtn7;

    //----------------------------------------------------------------------------------------------

    //---------------- GIVEN WORDS BUTTONS SECTION ---------------
    // buttons given at the bottom for user to choose and combine to get the right answer
    Button l3GivenWordBtn1, l3GivenWordBtn2, l3GivenWordBtn3, l3GivenWordBtn4,
            l3GivenWordBtn5, l3GivenWordBtn6, l3GivenWordBtn7, l3GivenWordBtn8,
            l3GivenWordBtn9, l3GivenWordBtn10, l3GivenWordBtn11, l3GivenWordBtn12;
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
        setContentView(R.layout.activity_level_three);

        // read level Three data from csv file (stored in raw directory) and instantiate Level object
        // add the Level object created from the file to levelThreeQuestion list
        readLevelThreeData();

        // the Level object at useQuestionNumber will be pass to playLevelThree function to generate the game
        userQuestionNumber = 0; // temporarily set as 0, but modified when User class is created and loaded here

        // set pressAnswerCount to zero
        pressAnsCount = 0;
        // assign buttons and textView from xml file
        l3qBoard = findViewById(R.id.l3qBoard);

        // assign buttons for answerButton
        l3AnswerBtn1 = findViewById(R.id.l3AnswerBtn1);
        l3AnswerBtn2 = findViewById(R.id.l3AnswerBtn2);
        l3AnswerBtn3 = findViewById(R.id.l3AnswerBtn3);
        l3AnswerBtn4 = findViewById(R.id.l3AnswerBtn4);
        l3AnswerBtn5 = findViewById(R.id.l3AnswerBtn5);
        l3AnswerBtn6 = findViewById(R.id.l3AnswerBtn6);
        l3AnswerBtn7 = findViewById(R.id.l3AnswerBtn7);

        setAnswerBtn1 = false; setAnswerBtn2 = false; setAnswerBtn3 = false;
        setAnswerBtn4 = false; setAnswerBtn5 = false; setAnswerBtn6 = false; setAnswerBtn7 = false;

        // load the font for text in this level
        Typeface customFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/felaFromAssets.otf");


        // assign buttons for givenWord buttons
        l3GivenWordBtn1 = findViewById(R.id.l3GivenWordBtn1);
        l3GivenWordBtn1.setTypeface(customFont);
        l3GivenWordBtn2 = findViewById(R.id.l3GivenWordBtn2);
        l3GivenWordBtn3 = findViewById(R.id.l3GivenWordBtn3);
        l3GivenWordBtn4 = findViewById(R.id.l3GivenWordBtn4);
        l3GivenWordBtn5 = findViewById(R.id.l3GivenWordBtn5);
        l3GivenWordBtn6 = findViewById(R.id.l3GivenWordBtn6);
        l3GivenWordBtn7 = findViewById(R.id.l3GivenWordBtn7);
        l3GivenWordBtn8 = findViewById(R.id.l3GivenWordBtn8);
        l3GivenWordBtn9 = findViewById(R.id.l3GivenWordBtn9);
        l3GivenWordBtn10 = findViewById(R.id.l3GivenWordBtn10);
        l3GivenWordBtn11 = findViewById(R.id.l3GivenWordBtn11);
        l3GivenWordBtn12 = findViewById(R.id.l3GivenWordBtn12);

        // assign all givenWord buttons to onClick method created for this view
        l3GivenWordBtn1.setOnClickListener(this);
        l3GivenWordBtn2.setOnClickListener(this);
        l3GivenWordBtn3.setOnClickListener(this);
        l3GivenWordBtn4.setOnClickListener(this);
        l3GivenWordBtn5.setOnClickListener(this);
        l3GivenWordBtn6.setOnClickListener(this);
        l3GivenWordBtn7.setOnClickListener(this);
        l3GivenWordBtn8.setOnClickListener(this);
        l3GivenWordBtn9.setOnClickListener(this);
        l3GivenWordBtn10.setOnClickListener(this);
        l3GivenWordBtn11.setOnClickListener(this);
        l3GivenWordBtn12.setOnClickListener(this);

        // for clarity purpose, a playLevelThree method is created and call from here
        playLevelThree(levelThreeQuestion.get(userQuestionNumber));
    }

    /*
        readLevelThreeData
        Create an input stream to read files stored in this project
        create a bufferedReader for the input stream
        split the line in the csv file using comma as a token
        store each token at value (0..5) in a temp variable which will then be used to create a Level Object
        add the newly created Level object in a list (levelThreeQuestion)
        learned from Android Developer website and this (https://www.youtube.com/watch?v=i-TqNzUryn8)
     */
    public void readLevelThreeData() {
        InputStream myInputStream = getResources().openRawResource(R.raw.level_three_data);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myInputStream, Charset.forName("UTF-8")));

        String textLine = "";
        try {
            while ((textLine = bufferedReader.readLine()) != null) {
                String[] tokens = textLine.split(",");

                // Read the data and create a Level object, then add them in levelThreeQuestion list
                // first store the data in a temp variable first
                int questionNumber = Integer.parseInt(tokens[0]);
                String question = tokens[1];
                String answer = tokens[2];
                String hint = tokens[3];
                int levelNumber = Integer.parseInt(tokens[4]);
                String givenWord = tokens[5];
                //create a new Level object by passing the above variables in its constructor
                // add the new object to levelThreeQuestion list
                levelThreeQuestion.add(new Level(questionNumber, question, answer, hint, levelNumber, givenWord));
            }
        } catch (IOException e) {
            Log.wtf("Level Three Activity", "Error occur while reading on line" + textLine, e);
            e.printStackTrace();
        }
    }

    /*
        takes a level object as a parameter which will then be use to generate the game for user
        First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
        extract the question from the given object and present in the question board.
        assign each character to givenWordButtons
     */
    public void playLevelThree(Level level3Object)
    {
        // reset all the buttons to make sure no value associated with answer and word buttons are not assigned at the beginning of the game.
        resetButtons();

        // assign object question attribute to questionBoard TextView
        l3qBoard.setText(level3Object.getQuestion());

        // assign given word value to each button
        l3GivenWordBtn1.setText(String.valueOf(level3Object.getGivenWord().charAt(0)));
        l3GivenWordBtn2.setText(String.valueOf(level3Object.getGivenWord().charAt(1)));
        l3GivenWordBtn3.setText(String.valueOf(level3Object.getGivenWord().charAt(2)));
        l3GivenWordBtn4.setText(String.valueOf(level3Object.getGivenWord().charAt(3)));
        l3GivenWordBtn5.setText(String.valueOf(level3Object.getGivenWord().charAt(4)));
        l3GivenWordBtn6.setText(String.valueOf(level3Object.getGivenWord().charAt(5)));
        l3GivenWordBtn7.setText(String.valueOf(level3Object.getGivenWord().charAt(6)));
        l3GivenWordBtn8.setText(String.valueOf(level3Object.getGivenWord().charAt(7)));
        l3GivenWordBtn9.setText(String.valueOf(level3Object.getGivenWord().charAt(8)));
        l3GivenWordBtn10.setText(String.valueOf(level3Object.getGivenWord().charAt(9)));
        l3GivenWordBtn11.setText(String.valueOf(level3Object.getGivenWord().charAt(10)));
        l3GivenWordBtn12.setText(String.valueOf(level3Object.getGivenWord().charAt(11)));
    }

    /*
        - The onClick methods is shared by all the givenWord buttons as we use View.OnclickListener interface
        - switch statement is used to track which button is clicked
        - make the clicked button to disappear and set clickable to false
        - set the clicked button value to the unassigned answerButton
        - each time a button is pressed/clicked, pressAnsCount is increment to decide whether user has filled all answer buttons
        - Once it gets to 7 for pressAnsCount, call validateAnswer() which will validate the answer and pass appropriate object
          depending on user getting the answer right or wrong
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l3GivenWordBtn1:
                l3GivenWordBtn1.animate().alpha(0).setDuration(50);
                l3GivenWordBtn1.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn1.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn2:
                l3GivenWordBtn2.animate().alpha(0).setDuration(50);
                l3GivenWordBtn2.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn2.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn3:
                l3GivenWordBtn3.animate().alpha(0).setDuration(50);
                l3GivenWordBtn3.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn3.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn4:
                l3GivenWordBtn4.animate().alpha(0).setDuration(50);
                l3GivenWordBtn4.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn4.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn5:
                l3GivenWordBtn5.animate().alpha(0).setDuration(50);
                l3GivenWordBtn5.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn5.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn6:
                l3GivenWordBtn6.animate().alpha(0).setDuration(50);
                l3GivenWordBtn6.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn6.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn7:
                l3GivenWordBtn7.animate().alpha(0).setDuration(50);
                l3GivenWordBtn7.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn7.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn8:
                l3GivenWordBtn8.animate().alpha(0).setDuration(50);
                l3GivenWordBtn8.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn8.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn9:
                l3GivenWordBtn9.animate().alpha(0).setDuration(50);
                l3GivenWordBtn9.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn9.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn10:
                l3GivenWordBtn10.animate().alpha(0).setDuration(50);
                l3GivenWordBtn10.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn10.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn11:
                l3GivenWordBtn11.animate().alpha(0).setDuration(50);
                l3GivenWordBtn11.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn11.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
                }
                break;
            case R.id.l3GivenWordBtn12:
                l3GivenWordBtn12.animate().alpha(0).setDuration(50);
                l3GivenWordBtn12.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l3GivenWordBtn12.getText()));
                if (pressAnsCount == 7) {
                    validateAnswer(levelThreeQuestion.get(userQuestionNumber));
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
            l3AnswerBtn1.setText(text);
            setAnswerBtn1 = true;
            return;
        }
        if (!setAnswerBtn2) {
            l3AnswerBtn2.setText(text);
            setAnswerBtn2 = true;
            return;
        }
        if (!setAnswerBtn3) {
            l3AnswerBtn3.setText(text);
            setAnswerBtn3 = true;
            return;
        }
        if (!setAnswerBtn4) {
            l3AnswerBtn4.setText(text);
            setAnswerBtn4 = true;
            return;
        }
        if (!setAnswerBtn5) {
            l3AnswerBtn5.setText(text);
            setAnswerBtn5 = true;
            return;
        }
        if (!setAnswerBtn6) {
            l3AnswerBtn6.setText(text);
            setAnswerBtn6 = true;
            return;
        }
        if (!setAnswerBtn7) {
            l3AnswerBtn7.setText(text);
            setAnswerBtn7 = true;
        }
    }

    /*
    - accepts a Level object as a parameter so that we know what object to pass when checking answer
    - compares the answer from user and answer within the Level object
    - If correct
        - check if the question is the last one in the level, and go to Level page as this is the last level at the moment
        - pass the next object in the Level Object list (next question in other words) for user to play
    - if incorrect
        - pass the same object for user to play again
 */
    public void validateAnswer(Level level2Object) {
        // first, combine the letters assigned to the answer buttons from user pressing buttons
        String userAns = String.valueOf(l3AnswerBtn1.getText())
                + String.valueOf(l3AnswerBtn2.getText())
                + String.valueOf(l3AnswerBtn3.getText())
                + String.valueOf(l3AnswerBtn4.getText())
                + String.valueOf(l3AnswerBtn5.getText())
                + String.valueOf(l3AnswerBtn6.getText())
                + String.valueOf(l3AnswerBtn7.getText());

        if (level2Object.getAnswer().equalsIgnoreCase(userAns)) {
            if (userQuestionNumber == (levelThreeQuestion.size() - 1)) {
                // save coins and questionNumber to drive for future and other levels
                // go to back to level page
                Intent intent = new Intent (this, ChooseLevelActivity.class);
                startActivity(intent);
                return;
            }
            Toast.makeText(this, "Answer is correct!", Toast.LENGTH_LONG).show();
            pressAnsCount = 0;
            userQuestionNumber++;
            playLevelThree(levelThreeQuestion.get(userQuestionNumber));
        } else {
            pressAnsCount = 0;
            playLevelThree(levelThreeQuestion.get(userQuestionNumber));
        }
    }

    /*
    set all wordGivenButtons to appear and set clickable to true
    set false for setAnswer variables
    set the answers buttons to empty string
 */
    public void resetButtons() {
        // make re-appear wordButtons
        l3GivenWordBtn1.animate().alpha(255).setDuration(50);
        l3GivenWordBtn1.setClickable(true);
        l3GivenWordBtn2.animate().alpha(255).setDuration(50);
        l3GivenWordBtn2.setClickable(true);
        l3GivenWordBtn3.animate().alpha(255).setDuration(50);
        l3GivenWordBtn3.setClickable(true);
        l3GivenWordBtn4.animate().alpha(255).setDuration(50);
        l3GivenWordBtn4.setClickable(true);
        l3GivenWordBtn5.animate().alpha(255).setDuration(50);
        l3GivenWordBtn5.setClickable(true);
        l3GivenWordBtn6.animate().alpha(255).setDuration(50);
        l3GivenWordBtn6.setClickable(true);
        l3GivenWordBtn7.animate().alpha(255).setDuration(50);
        l3GivenWordBtn7.setClickable(true);
        l3GivenWordBtn8.animate().alpha(255).setDuration(50);
        l3GivenWordBtn8.setClickable(true);
        l3GivenWordBtn9.animate().alpha(255).setDuration(50);
        l3GivenWordBtn9.setClickable(true);
        l3GivenWordBtn10.animate().alpha(255).setDuration(50);
        l3GivenWordBtn10.setClickable(true);
        l3GivenWordBtn11.animate().alpha(255).setDuration(50);
        l3GivenWordBtn11.setClickable(true);
        l3GivenWordBtn12.animate().alpha(255).setDuration(50);
        l3GivenWordBtn12.setClickable(true);

        // set to false for setAnswers buttons as we need this value to check if the answerButton is filled with letter
        setAnswerBtn1 = false;
        setAnswerBtn2 = false;
        setAnswerBtn3 = false;
        setAnswerBtn4 = false;
        setAnswerBtn5 = false;
        setAnswerBtn6 = false;
        setAnswerBtn7 = false;

        // Empty the any letter assign to answer buttons
        l3AnswerBtn1.setText("");
        l3AnswerBtn2.setText("");
        l3AnswerBtn3.setText("");
        l3AnswerBtn4.setText("");
        l3AnswerBtn5.setText("");
        l3AnswerBtn6.setText("");
        l3AnswerBtn7.setText("");
    }


}
