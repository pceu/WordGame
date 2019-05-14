package com.example.wordgame;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

/**
 * LogActivity records errors, bugs and exceptions thrown from other class in the project
 * it contains a static string list which is accessible from any class\
 * keep adding the message added to the list and present them in a listView
 * user can also delete all the message listed on the screen with delete button
 *
 */
public class LogActivity extends AppCompatActivity {

    // this is set to static so that we can add a list to it from any class
    // runtime error will be added to the list
    static ArrayList<String> logList = new ArrayList<>();

    private ListView listView;
    private ArrayAdapter myAdapter;

    /**
     * assign variable with id from the layout
     * populate the stringList to a table view on the screen
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        try {
            listView = findViewById(R.id.listView);
            final Date currentTime = Calendar.getInstance().getTime();
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(currentTime + " - ");
            stringBuilder.append("This is test data.");
            logList.add(String.valueOf(stringBuilder));

            myAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, logList);
            listView.setAdapter(myAdapter);


            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logList.clear();
                    myAdapter = null;
                    listView.setAdapter(myAdapter);
                    Snackbar.make(view, "Log history deleted permanently!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } catch (Exception e) {
            addToLogList(String.valueOf(e.getMessage()));
            finish();
            startActivity(getIntent());
        }

    }

    /**
     * get current time and date
     * combine with message being passed and add to the logList
     * @param exceptionMessage string of message
     */
    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[Log Activity] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

}
