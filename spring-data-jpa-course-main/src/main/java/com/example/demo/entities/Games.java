package com.example.demo.entities;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity(name = "games")
public class Games implements Serializable {

@SequenceGenerator(allocationSize = 1,
        name = "game_rowid_generator",
        sequenceName ="game_rowid_generator" )
    @GeneratedValue(generator = "game_rowid_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id", unique = true, nullable = false,updatable = false)
    @Id
    private int id;

    @Column(name = "user_id", unique = false, nullable = false,updatable = false)
    private int userId;

    @Column(name = "true_count", unique = false, nullable = false,updatable = true)
    private int trueCount;

    @Column(name = "false_count", unique = false, nullable = false,updatable = true)
    private int falseCount;

    @Column(name = "game_id", unique = false, nullable = false,updatable = true)
    private int gameID ;

    @Column(name = "total_question_count", unique = false, nullable = false,updatable = true)
    private int totalQuestionCount;




    public int getTrueCount() {
        return trueCount;
    }

    public void setTrueCount(int trueCount) {
        this.trueCount = trueCount;
    }

    public int getFalseCount() {
        return falseCount;
    }

    public void setFalseCount(int falseCount) {
        this.falseCount = falseCount;
    }



    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public String toString() {

        String result ="{\"id\":"+ "\"" + id + "\"" +
                ",\"userId\":" +"\"" +userId+ "\""+
                ",\"trueCount\":" + "\"" + trueCount + "\""+
                ",\"falseCount\":" + "\""+ falseCount +  "\""+
                ",\"gameID\":" +"\"" + gameID + "\""+
                ",\"totalQuestionCount\":" +"\"" + totalQuestionCount + "\""+

                "}";



        return result;
    }



    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getTotalQuestionCount() {
        return totalQuestionCount;
    }

    public void setTotalQuestionCount(int totalQuestionCount) {
        this.totalQuestionCount = totalQuestionCount;
    }
}
