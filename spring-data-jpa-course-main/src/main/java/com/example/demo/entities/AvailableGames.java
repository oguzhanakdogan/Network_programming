package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "availablegames")
public class AvailableGames {

    @Id
    private int id;

    @Column(name = "game_name", updatable = false,nullable = false,unique = true,length = 30)
    private String gameName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
