package com.example.demo.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "doctor")
public class Doctor {

    @Id
    private int id;

//    @Column(name = "user_name", unique = true, updatable = false,nullable = false)
//    private String userName;

    @Column(name = "doctor_name", updatable = false,nullable = false)
    private String name;
    @Column(name = "doctor_surname", updatable = false,nullable = false)
    private String surname;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String eMail;


    @OneToMany
    private Collection<PatientInDB> patients = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }

    public Collection<PatientInDB> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientInDB> patients) {
        this.patients = patients;
    }
}
