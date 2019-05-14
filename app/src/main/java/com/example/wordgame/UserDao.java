package com.example.wordgame;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * UserDao handles all the queries and commands that should be executed to the database
 * It is responsible for defining some functions for to access the database
 * Using Room also enhance detecting runtime errors
 * what each method does is explained with a brief comment
 */
@Dao
public interface UserDao {

    @Insert
    public void insertAllUserData(List<User> users);

    // query and returns how many rows in the database
    @Query("SELECT COUNT(*) FROM User")
    int countRows();

    // select the coin_amount where level number is equal to the levelNumber pass in the method
    @Query("SELECT coin_amount FROM User WHERE levelNumber = :levelNumber")
    int getCoinAmount(int levelNumber);

    // select the question number for table where levelNumber is equal to the levelNumber given in the parameter
    @Query("SELECT question_number FROM User WHERE levelNumber = :levelNumber")
    int getQuestionNumber(int levelNumber);

    // update the coinAmount for users
    @Query("UPDATE User SET coin_amount = :amount WHERE levelNumber = :levelNumber")
    void updateCoin(int amount, int levelNumber);

    // update the user question number for future play
    @Query("UPDATE User SET question_number = :questionNumber WHERE levelNumber = :levelNumber")
    void updateQuestionNumber(int questionNumber, int levelNumber);



}