package com.example.wordgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class LevelTwoActivity extends AppCompatActivity {

    private List<Level> levelTwoQuestion = new ArrayList<Level>();

    private void loadLevel2() {
        levelTwoQuestion.add(new Level(1, "When you know your mistake, you tend do this", "admit", "it", 2));
        levelTwoQuestion.add(new Level(2, "Pain cause by involuntary contraction", "Cramp", "ap", 2));
        levelTwoQuestion.add(new Level(2, "To ___ something on the surface", "apply", "py", 2));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_two);

        // instantiate level 2 object - contains question num, question, answer, hint and level number
        loadLevel2();
    }
}
