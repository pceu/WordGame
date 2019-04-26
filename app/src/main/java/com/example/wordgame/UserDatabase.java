package com.example.wordgame;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static UserDatabase instance;
    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            // the name given here is the database name not the table name
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user_database")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    /*
        This method will be used to release the reference to the database object; this will avoid the memory leads
     */
    public static void destroyInstance() {
        instance = null;
    }
}
