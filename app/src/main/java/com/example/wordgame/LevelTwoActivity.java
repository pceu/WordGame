package com.example.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class LevelTwoActivity extends AppCompatActivity {

    // Level object list for this level (Level 2) - contains multiple questions to play for the level
    private List<Level> levelTwoQuestion = new ArrayList<Level>();

    private int userQuestionNumber;     // when exit the game the question user up to will be saved for user and allow to resume for future playing

    private void loadLevel2() {
        levelTwoQuestion.add(new Level(1, "When you know your mistake, you tend do this", "admit", "it", 2));
        levelTwoQuestion.add(new Level(2, "Pain cause by involuntary contraction", "Cramp", "ap", 2));
        levelTwoQuestion.add(new Level(2, "To ___ something on the surface", "apply", "py", 2));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);

        userQuestionNumber = 1; // temporarily set as 1, but modified when User class is created and loaded here
        // instantiate level 2 object - contains question num, question, answer, hint and level number
        loadLevel2();

        // for clarity purpose, a playLevelTwo method is created and call from here
        playLevelTwo();

    }

    public void playLevelTwo() {
        for (int i = userQuestionNumber; i <= levelTwoQuestion.size(); i++) {
            // will add some codes later
            return;
        }

    }
}
