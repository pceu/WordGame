package com.example.wordgame;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * ChooseLevelActivity is simple as it only navigate to level 1, 2 and 3
 * comments are not added for methods as they are too simple and clear from just seeing them
 */
public class ChooseLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);

    }

    public void ClickLevelOne(View view) {
        Intent i = new Intent(this, LevelOneActivity.class);
        startActivity(i);
    }

    public void ClickLevelTwo(View view) {
        Intent i = new Intent(this, LevelTwoActivity.class);
        startActivity(i);
    }

    public void ClickLevelThree(View view) {
        Intent i = new Intent(this, LevelThreeActivity.class);
        startActivity(i);
    }
}
