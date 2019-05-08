package com.example.wordgame;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogActivity extends AppCompatActivity {

    // this is set to static so that we can add a list to it from any class
    // runtime error will be added to the list
    static ArrayList<String> logList = new ArrayList<>();

    private ListView listView;
    private ArrayAdapter myAdapter;

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

    public void addToLogList(String exceptionMessage) {
        Date currentTime = Calendar.getInstance().getTime();
        LogActivity.logList.add("[Log Activity] - " + exceptionMessage + " [" + String.valueOf(currentTime) + "]");
    }

}
