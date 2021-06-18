package com.example.demo.services;

import com.example.demo.entities.PatientInDB;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class PatientGenerator {

    public PatientInDB generatePatient(String name, String surname, short age){
        System.out.println("request geldi" + name + " " + surname + " " + age);
        PatientInDB patient = new PatientInDB();
        patient.setUuid((int) System.currentTimeMillis());
        patient.setAge(age);
        patient.setName(name);
        patient.setSurname(surname);
        patient.setDateofJoin((java.sql.Date) new Date());
        return patient;
    }
}
