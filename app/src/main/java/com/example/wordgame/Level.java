package com.example.wordgame;

public class Level {

    private int questionNum;    // uniquely identify the question as each will be given different number (e.g. 1,2,3...)
    private String question;    // question to be asked; like if the answer is
    private String answer;      // the answer that user has to guess (e.g. passion, learn...)
    private String hint;        // some of the letters, as appropriate for each level, will be stored to give user when user want to use.
    private int levelNumber;    // this will help to identify which level the user is playing as this attribute will be stored in UserFile.
    private String givenWord;
    // Class CONSTRUCTOR


    public Level(int questionNum, String question, String answer, String hint, int levelNumber, String givenWord) {
        this.questionNum = questionNum;
        this.question = question;
        this.answer = answer;
        this.hint = hint;
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

    public String getHint() {
        return hint;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getGivenWord() {
        return givenWord;
    }
    /*
        Setter method for Class attributes for 'Write' access
     */

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void setGivenWord(String givenWord) {
        this.givenWord = givenWord;
    }
}
