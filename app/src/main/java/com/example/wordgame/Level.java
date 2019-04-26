package com.example.wordgame;

import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public interface Level {

//    // LevelData object list for a level - contains multiple questions to play for the level
//    List<LevelData> levelData = new ArrayList<>();


    // ================ CLASS METHODS/FUNCTIONS ===========================================================

    /*
        readLevelOneData
        Create an input stream to read files stored in this project
        create a bufferedReader for the input stream
        split the line in the csv file using comma as a token
        store each token at value (0..5) in a temp variable which will then be used to create a LevelData Object
        add the newly created LevelData object in a list (userLevelQuestion)
     */

    /*
        takes a level object as a parameter which will then be use to generate the game for user
        First, reset answerButtons and WordGiven buttons (explained more about the function at the top of resetButtons method).
        extract the question from the given object and present in the question board.
        assign each character to givenWordButtons
     */
    void playLevel(LevelData levelDataObject);

    /*
        set all wordGivenButtons to appear and set clickable to true
        set false for setAnswer variables
        set the answers buttons to empty string
     */
    void resetButtons();

    /*
        this function set the alpha to full value and set button clickable to true
     */
    void reappearButton(Button button);

    /*
        set alpha value of a given button to zero and set false for setClickable
     */
    void disappearButton(Button button);

    /*
        # accept a button as a parameter
        # compares the text from the given button with every word buttons; and put back (make appear and clickable) the button which has the same letter with the input button text
        # the input button is the answer button clicked by the user
     */
    void putBackWordButton(Button answerButton);

    /*
        set answer accept a string input and checks:
        - if a button has been assigned with a value (an alphabet)
        - if yes, go to next button and if no, then assign the input text (which is the text of the clicked wordGivenButtons) to the answerButton.
        - set true to the setAnswerButton to know it has been assigned
     */
    void setAnswer(String text);

    // Coin number Modification
    /*
        # check and return false if the user coin amount subtract amount passed in the parameter is less than zero
        # else - reduce the user coin amount by the given amount and update the text of coinButton1
     */
    boolean decreaseCoin(int amount);


    /*
        ====== LEARNING SUMMARY
        - creating an interface - https://www.geeksforgeeks.org/interfaces-in-java/

     */


}
