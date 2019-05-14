package com.example.wordgame;

/**
 * Level Data is a class that contains question number, question, answer, level number, and the given word for one question
 * Objects of this class are created or loaded from raw file and populate in the Level class
 */
public class LevelData {

    private int questionNum;    // uniquely identify the question as each will be given different number (e.g. 1,2,3...)
    private String question;    // question to be asked; like if the answer is
    private String answer;      // the answer that user has to guess (e.g. passion, learn...)
    private int levelNumber;    // this will help to identify which level the user is playing as this attribute will be stored in UserFile.
    private String givenWord;

    // Class CONSTRUCTOR
    public LevelData(int questionNum, String question, String answer, int levelNumber, String givenWord) {
        this.questionNum = questionNum;
        this.question = question;
        this.answer = answer;
        this.levelNumber = levelNumber;
        this.givenWord = givenWord;
    }

    /*
            Getter method to get Class attributes as they are private
         */
    public int getQuestionNum() {
        return questionNum;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getGivenWord() {
        return givenWord;
    }

}
