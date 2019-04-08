package com.example.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class LevelOneActivity extends AppCompatActivity {

    //Level object list for this level (Level 1)
    private List<Level> testQuestion = new ArrayList<Level>();

    // temporary method (will delete this method or add more comments if used
    /*
    temporarily comment as the level class has been modified
    private void addLevel() {
        testQuestion.add(new Level(1, "Something that people do or cause to happen", "Act", "C", 1));
        testQuestion.add(new Level(2, "Have Some ambitious plan", "Aim", "m", 1));
        testQuestion.add(new Level(3, "Make a request or demand something", "ask", "k", 1));
        testQuestion.add(new Level(1, "Not working properly", "Bad", "d", 1));
    }
    */

    /*
            HARD CODED QUESTIONS FOR LEVEL ONE (this will be modified when Level is stored in local drive)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_one);

        // create a list of Level object to be used in this activity (LevelOne)
        //addLevel();

    }
}
