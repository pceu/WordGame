package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class SettingActivity extends AppCompatActivity {

    public static int bkgdchecked = 1;

    // if timerSwitch and bkgdchecked set Off = 0 and set On = 1 (this will be the value assign to timerSet and Music to track)
    // this is set as static because we need to check this value from other activity and class thus must be available to the whole class in the project
    public static int timerSet = 0;
    //public static int bgMusicSet;

    // Switch buttons
    SwitchCompat timerSwitch;
    SwitchCompat musicSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // assign switch to it's id
        timerSwitch = findViewById(R.id.timer_switch);
        musicSwitch = findViewById(R.id.music_switch);

        // check the timerSet or bkgdchecked value whether it is 0 or 1, and set the timerSwitch or musicSwitch to On if timerSet = 1
        // we don't need to check for off as it is set to Off state by default
        if(timerSet == 1) { timerSwitch.setChecked(true); }
        if (bkgdchecked == 1) {musicSwitch.setChecked(true); }

        /*
           timerSwitch onClickListener change the value of timerSet either as 0 or 1 (0 = off, 1 = on)
           setting it as on means user wants to play the game with timer mode
         */
        timerSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerSwitch.isChecked()) {
                    timerSwitch.setChecked(true);
                    timerSet = 1;
                } else {
                    timerSet = 0;
                }
            }
        });

        /*
           musicSwitch onClickListener change the value of bkgdchecked either as 0 or 1 (0 = off, 1 = on)
           setting it as on means user wants music to be on while playing
         */
        musicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicSwitch.isChecked()) {
                    bkgdchecked = 1;
                } else {
                    bkgdchecked = 0;
                }
            }
        });

        //bkgdMusicoff();
    }

//    public  void bkgdMusicoff() {
//        CheckBox checkbox1 = (CheckBox) findViewById(R.id.checkBox);
//        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//                if (compoundButton.isChecked()){
//                    bkgdchecked = 1;
//                } else {
//                    bkgdchecked = 0;
//                }
//            }
//        });
//    }

    /*
        ======================== Credits  ==========================
        + using SwitchCompat is learned from:
            - https://www.youtube.com/watch?v=mT0ymLOaGhI by Android Coding
            - and from Android Developer website (https://developer.android.com/reference/androidx/appcompat/widget/SwitchCompat)
     */
}