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

        levelOneButton = (Button) findViewById(R.id.levelOneButton);
        levelTwoButton = (Button) findViewById(R.id.levelTwoButton);
        levelThreeButton = (Button) findViewById(R.id.levelThreeButton);
    }

    public void ToLevelOnePage(View view) {
        Intent intent = new Intent (this, LevelOneActivity.class);
        startActivity(intent);
    }

    public void ClickLevelOne(View view) {
        ToLevelOnePage(view);
    }


}
