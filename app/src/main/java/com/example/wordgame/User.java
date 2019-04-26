package com.example.wordgame;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {

    @PrimaryKey
    @NonNull
    private int levelNumber;

    @ColumnInfo(name = "question_number")
    private int questionNumber;

    @ColumnInfo(name = "coin_amount")
    private int coinAmount;

    // Constructor
    // Room use constructor with no argument
    public User() {

    }

    // constructor with arguments (will be ignore by Room as it always takes the one with no constructor
    // that is the reason why this constructor is marked as ignore so that Room will always ignore this constructor
    @Ignore
    public User(int levelNumber, int questionNumber, int coinAmount) {
        this.levelNumber = levelNumber;
        this.questionNumber = questionNumber;
        this.coinAmount = coinAmount;
    }

    // Get and Set method for the class attributes

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(int coinAmount) {
        this.coinAmount = coinAmount;
    }
}
