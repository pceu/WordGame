package com.example.wordgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    public static int bkgdchecked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        bkgdMusicoff();
    }

    public  void bkgdMusicoff() {
        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkBox);
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()){

                    bkgdchecked = 1;
                } else {
                    bkgdchecked = 0;
                }
            }
        });
    }
}