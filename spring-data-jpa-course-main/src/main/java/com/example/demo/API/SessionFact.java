package com.example.demo.API;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionFact {

    private static volatile SessionFactory sessionFact;

    private SessionFact(){

    }


    public static SessionFactory getInstance(){
        if(sessionFact == null){
            synchronized (SessionFact.class) {
                if(sessionFact == null) {
                    sessionFact = new Configuration().configure().buildSessionFactory();
                }
            }
        }
        return sessionFact;


    }
}
