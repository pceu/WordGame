package com.example.wordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseLevelActivity extends AppCompatActivity {

    Button levelOneButton;
    Button levelTwoButton;
    Button levelThreeButton;

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
