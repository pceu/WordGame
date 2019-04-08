package com.example.wordgame;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LevelTwoActivity extends AppCompatActivity implements View.OnClickListener {

    // load the font for text in this level
    //Typeface customFont = Typeface.createFromAsset(getAssets(), "assets/fonts/felaFromAssets.otf");

    // Level object list for this level (Level 2) - contains multiple questions to play for the level
    private List<Level> levelTwoQuestion = new ArrayList<>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing
    private int pressAnsCount = 0;

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
        this is a temporary method for instantiating Level objects and will extract Level data from local drive and
        create a list of Level objects .
        New methods/functions will be implemented in the future
     */
    private void loadLevel2() {
        levelTwoQuestion.add(new Level(1, "When you know your mistake, you tend do this", "admit", "it", 2, "TDMAESIO"));
        levelTwoQuestion.add(new Level(2, "Pain cause by involuntary contraction", "Cramp", "ap", 2, "MCALPAGR"));
        levelTwoQuestion.add(new Level(2, "To ___ something on the surface", "apply", "py", 2, "EUPAYJLP"));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);

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

        userQuestionNumber = 1; // temporarily set as 1, but modified when User class is created and loaded here
        // instantiate level 2 object - contains question num, question, answer, hint and level number
        loadLevel2();

        // for clarity purpose, a playLevelTwo method is created and call from here
        playLevelTwo();

    }

    /*
        playLevelTwo method will also call other method for playing this level
        more comments will be added when completed with this function/method
     */
    public void playLevelTwo() {
        for (int i = userQuestionNumber; i <= levelTwoQuestion.size(); i++) {

            // temporarily assign a list at i to a level object to use in here. It will be a messy code if we just use levelQuestion.at(i)
            Level tempL2Question = levelTwoQuestion.get(i);

            // assign object question attribute to questionBoard TextView
            l2qBoard.setText(tempL2Question.getQuestion());

            // assign given word value to each button
            l2GivenWordBtn1.setText(String.valueOf(tempL2Question.getGivenWord().charAt(0)));
            l2GivenWordBtn2.setText(String.valueOf(tempL2Question.getGivenWord().charAt(1)));
            l2GivenWordBtn3.setText(String.valueOf(tempL2Question.getGivenWord().charAt(2)));
            l2GivenWordBtn4.setText(String.valueOf(tempL2Question.getGivenWord().charAt(3)));
            l2GivenWordBtn5.setText(String.valueOf(tempL2Question.getGivenWord().charAt(4)));
            l2GivenWordBtn6.setText(String.valueOf(tempL2Question.getGivenWord().charAt(5)));
            l2GivenWordBtn7.setText(String.valueOf(tempL2Question.getGivenWord().charAt(6)));
            l2GivenWordBtn8.setText(String.valueOf(tempL2Question.getGivenWord().charAt(7)));

            Boolean ansIsRight = false;
            break;
            // temporarily commented as the emulator takes ages to run this code snippets (need to solve this)
//            while (!ansIsRight) {
//                if (pressAnsCount == 5) {
//
//                    // temporary var which combines all the letters form answer buttons
//                    // this will be compare with the answer stored in the Level object
//                    String userAns = String.valueOf(l2AnswerBtn1.getText())
//                                    + String.valueOf(l2AnswerBtn2.getText())
//                                    + String.valueOf(l2AnswerBtn3.getText())
//                                    + String.valueOf(l2AnswerBtn4.getText())
//                                    + String.valueOf(l2AnswerBtn5.getText());
//                    if (tempL2Question.getAnswer().equalsIgnoreCase(userAns)) {
//                        ansIsRight = true;
//                    } else {ansIsRight = false;}
//                }
//            }
        }

    }

    /*
        The onClick methods is shared by all the givenWord buttons as we use View.OnclickListener interface
        switch statement is used to track which button is clicked
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.l2GivenWordBtn1:
                l2GivenWordBtn1.animate().alpha(0).setDuration(50);
                l2GivenWordBtn1.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn1.getText()));
                break;
            case R.id.l2GivenWordBtn2:
                l2GivenWordBtn2.animate().alpha(0).setDuration(50);
                l2GivenWordBtn2.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn2.getText()));
                break;
            case R.id.l2GivenWordBtn3:
                l2GivenWordBtn3.animate().alpha(0).setDuration(50);
                l2GivenWordBtn3.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn3.getText()));
                break;
            case R.id.l2GivenWordBtn4:
                l2GivenWordBtn4.animate().alpha(0).setDuration(50);
                l2GivenWordBtn4.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn4.getText()));
                break;
            case R.id.l2GivenWordBtn5:
                l2GivenWordBtn5.animate().alpha(0).setDuration(50);
                l2GivenWordBtn5.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn5.getText()));
                 break;
            case R.id.l2GivenWordBtn6:
                l2GivenWordBtn6.animate().alpha(0).setDuration(50);
                l2GivenWordBtn6.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn6.getText()));
                break;
            case R.id.l2GivenWordBtn7:
                l2GivenWordBtn7.animate().alpha(0).setDuration(50);
                l2GivenWordBtn7.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn7.getText()));
                break;
            case R.id.l2GivenWordBtn8:
                l2GivenWordBtn8.animate().alpha(0).setDuration(50);
                l2GivenWordBtn8.setClickable(false);
                pressAnsCount++;
                setAnswer(String.valueOf(l2GivenWordBtn8.getText()));
                break;
        }
    }

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
        }
        if (!setAnswerBtn5) {
            l2AnswerBtn5.setText(text);
            setAnswerBtn5 = true;
        }
    }
}
