package com.example.wordgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Date;

/**
 * In setting page or activity user can either turn on or off for music and timer
 * Other control are planned to be added, should this be further developed later
 */
public class SettingActivity extends AppCompatActivity {

    public static int bkgdchecked = 1;

    // if timerSwitch and bkgdchecked set Off = 0 and set On = 1 (this will be the value assign to timerSet and Music to track)
    // this is set as static because we need to check this value from other activity and class thus must be available to the whole class in the project
    public static int timerSet = 0;
    //public static int bgMusicSet;

    // Switch buttons
    SwitchCompat timerSwitch;
    SwitchCompat musicSwitch;

    /**
     * assign some attributes with id from layout
     * handles timerSwitch and music when click which is either to set 0 or 1
     * @param savedInstanceState bundle object
     */

    // Legal button
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        try {
            // assign switch to it's id
            timerSwitch = findViewById(R.id.timer_switch);
            musicSwitch = findViewById(R.id.music_switch);

            // these - timerSet and bkgdchecked - value cannot be other than 0 or 1 so assign default value if it has been changed to other value from other class (this variables are accessible from any class)
            if(timerSet != 1 && timerSet != 0) {
                timerSet = 0;
            }
            if(bkgdchecked != 1 && bkgdchecked != 0) {
                bkgdchecked = 1;
            }
            // check the timerSet or bkgdchecked value whether it is 0 or 1, and set the timerSwitch or musicSwitch to On if timerSet = 1
            // we don't need to check for off as it is set to Off state by default, but for error clean we also set false value
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
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            addToLogList(String.valueOf(e.getMessage()));
            // finish and start the activity again
            finish();
            startActivity(getIntent());
        }

        // Legal button OnClickListener
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    // Show legal dialog on setting page
    public  void openDialog() {
        LegalDialog legalDialog = new LegalDialog();
        legalDialog.show(getSupportFragmentManager(), "Legal Dialog");
    }

    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[Setting Activity] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

    /*
        ======================== Credits  ==========================
        + using SwitchCompat is learned from:
            - https://www.youtube.com/watch?v=mT0ymLOaGhI by Android Coding
            - and from Android Developer website (https://developer.android.com/reference/androidx/appcompat/widget/SwitchCompat)
     */
}