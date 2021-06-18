package com.example.demo.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;


@Entity(name = "patients")
public class PatientInDB  implements Serializable {

    @Id
    private int uuid;

    @Column(name = "name", nullable = false, unique = false, updatable = true)
    private String name;

    @Column(name = "surname", nullable = false, unique = false, updatable = true)
    private String surname;

    @Column(name = "age", nullable = false, unique = false,updatable = true)
    private int age;

    @Column(name = "date_of_join", nullable = false, unique = false, updatable = false)
    private Date dateofJoin;






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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDateofJoin() {
        return dateofJoin;
    }

    public void setDateofJoin(Date dateofJoin) {
        this.dateofJoin = dateofJoin;
    }

    @Override
    public String toString() {
        String str = "{\"uuid\":"+ "\"" + uuid + "\"" +
                ",\"name\":" +"\"" +name+ "\""+
                ",\"surname\":" + "\"" + surname + "\""+
                ",\"age\":" + "\""+ age +  "\""+
                ",\"dateofJoin\":" +"\"" + dateofJoin + "\""+ "}";

        return str;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
