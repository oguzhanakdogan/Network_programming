package com.example.demo.entities;

import javax.persistence.*;

@Entity(name = "timedata")
public class TimeData {
    @Id
    @SequenceGenerator(name = "patients_sequence",
            sequenceName = "patients_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patients_sequence")
    private Long id;

    @Column(name = "patient_id", nullable = false, unique = false, updatable = true)
    private int patient_id;

    @Column(name = "game_name", nullable = false, unique = false, updatable = true)
    private String game_name;

    @Column(name = "timeinsecond", nullable = false, unique = false, updatable = true)
    private int time;

    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MySequenceGenerator")
    @SequenceGenerator(allocationSize=1, schema="myschema",  name="MySequenceGenerator", sequenceName = "mysequence")
    @Column(name = "part", nullable = false, unique = false, updatable = true)
    private int part;

    @Column(name = "date", nullable = false)
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Override
    public String toString() {
        String str = "{\"date\":"+ "\"" + date + "\"" +
                ",\"game_name\":" +"\"" +game_name+ "\""+
                ",\"time\":" + "\"" + time + "\""+
                ",\"part\":" + "\""+ part +  "\""+
                "}";

        return str;
    }

}
