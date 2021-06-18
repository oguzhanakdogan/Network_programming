package com.example.demo.entities;

import java.io.Serializable;

public class GameInformation implements Serializable {

    private String game_name;
    private int true_count;
    private int false_count;
    private int totalQuestion;


    public GameInformation(String game_name,
                           int true_count,
                           int false_count,
                           int totalQuestion) {
        this.game_name = game_name;
        this.true_count = true_count;
        this.false_count = false_count;
        this.totalQuestion = totalQuestion;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public int getTrue_count() {
        return true_count;
    }

    public void setTrue_count(int true_count) {
        this.true_count = true_count;
    }

    public int getFalse_count() {
        return false_count;
    }

    public void setFalse_count(int false_count) {
        this.false_count = false_count;
    }

    @Override
    public String toString() {

        String result ="{\"game_name\":"+ "\"" + game_name + "\"" +
                ",\"true_count\":" +"\"" +true_count+ "\""+
                ",\"false_count\":" + "\"" + false_count + "\""+
                ",\"total_question_count\":" + "\"" + totalQuestion + "\""+

                "}";


        return result;
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }
}
