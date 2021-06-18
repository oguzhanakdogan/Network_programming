package com.example.demo.entities;

import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class Patient implements Serializable {

    private String name;
    private String surname;
    private short age;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id='"  + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public short getAge() {
        return age;
    }

    public void setAge(short age) {
        this.age = age;
    }
}
