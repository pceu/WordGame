package com.example.wordgame;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * MainActivity is where user first see the app
 * user can go to setting, log or level page
 * it also create database with some hardcoded data for user if the database row is null
 */
public class MainActivity extends AppCompatActivity {

    // database is created in this page if it is the first time user plays the game
    // User database
    private UserDatabase userDb;

    /**
     * get database instance from here
     * populate sample data for user in database is no row is contained in it
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            // initialize the database instance
            userDb = UserDatabase.getInstance(this);
            // throw exception if the database is still null
            if (userDb == null) {
                throw new NullPointerException("Could not create database instance");
            }

            // empty row in the database means user has never played or delete the app and install the app again
            // in this situation, hardcoded data (some coin amount to use and other related User data) are created for ths user
            if(userDb.userDao().countRows() == 0) {
                if(generateUser() == null) {
                    throw new IllegalArgumentException();
                }

                userDb.userDao().insertAllUserData(generateUser());
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            addToLogList(String.valueOf(e.getMessage()));
            finish();
            startActivity(getIntent());
        }
    }

    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[Main Activity] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

    /**
     * create a temp User list variable to ad User object and add user data
     * generate user Data for level 1, 2 and 3 (we only call this if the row in database is empty
     * as this means it is the first time the user play or install the game
     * finally return the User list created
     * @return a list of User object
     */
    public List<User> generateUser() {
        // create a temp User list variable to ad User object and add user data
        List<User> userInitialData = new ArrayList<>();
        // generate user Data for level 1, 2 and 3 (we only call this if the row in database is empty
        // as this means it is the first time the user play or install the game
        userInitialData.add(new User(1, 0, 50));
        userInitialData.add(new User(2, 0, 0));
        userInitialData.add(new User(3, 0, 0));
        //return the hardcoded data
        return userInitialData;
    }

    @Override
    protected void onDestroy() {
        // release the reference to the database when activity is destroyed
        UserDatabase.destroyInstance();
        super.onDestroy();
    }

    public void GotoChooseLevel(View view) {
        Intent i = new Intent(this, ChooseLevelActivity.class);
        startActivity(i);
    }

    public void GoToLogActivity(View view) {
        Intent i = new Intent(this, LogActivity.class);
        startActivity(i);
    }

    public void GotoSetting(View view) {
        Intent i = new Intent(this, SettingActivity.class);
        startActivity(i);
    }

}
