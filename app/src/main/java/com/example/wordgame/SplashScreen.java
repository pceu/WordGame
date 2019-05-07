package com.example.wordgame;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;
import java.util.Date;

public class SplashScreen extends AppCompatActivity {
    ImageView splashScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splashScreen = findViewById(R.id.splashScreen);
        splashScreen.setScaleType(ImageView.ScaleType.FIT_XY);

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    // Wait 2 Second
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    addToLogList(String.valueOf(e.getMessage()));
                }
            }
        };
        myThread.start();

    }

    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[Splash Screen Activity] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

}