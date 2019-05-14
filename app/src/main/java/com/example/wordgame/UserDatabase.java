package com.example.wordgame;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * UserDatabase is responsible for handling and connecting Room database with SQLite database
 * get the database instance
 * create instance of database
 */
@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    private static UserDatabase instance;

    /**
     * create database instance if it's null and return the instance
     * @param context context object
     * @return UserDatabase instance
     */
    public static UserDatabase getInstance(Context context) {
        if (instance == null) {
            // the name given here is the database name not the table name
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user_database")
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    /**
     * set the database instance to null
     * This method will be used to release the reference to the database object; this will avoid the memory leads
     */
    public static void destroyInstance() {
        instance = null;
    }
}
