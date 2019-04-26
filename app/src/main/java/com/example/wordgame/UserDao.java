package com.example.wordgame;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    public void insertAllUserData(List<User> users);

    // query and returns how many rows in the database
    @Query("SELECT COUNT(*) FROM User")
    int countRows();

    @Query("SELECT coin_amount FROM User WHERE levelNumber = :levelNumber")
    int getCoinAmount(int levelNumber);

    @Query("SELECT question_number FROM User WHERE levelNumber = :levelNumber")
    int getQuestionNumber(int levelNumber);

    // update the coinAmount for users
    @Query("UPDATE User SET coin_amount = :amount WHERE levelNumber = :levelNumber")
    void updateCoin(int amount, int levelNumber);

    // update the user question number for future play
    @Query("UPDATE User SET question_number = :questionNumber WHERE levelNumber = :levelNumber")
    void updateQuestionNumber(int questionNumber, int levelNumber);



}