package com.example.demo.services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Service
public class FirebaseInitializer {

    @PostConstruct
    public void intializer(){
        try {
            InputStream serviceAccount = this.getClass().getClassLoader().getResourceAsStream("./seniorprojectalzheimer-firebase-adminsdk-1tw4n-b8cf6be5a7.json");


            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("burasi calisti");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error calisti" );

        }

    }

    public Firestore getFirebae(){

        FirebaseDatabase.getInstance("https://seniorprojectalzheimer.firebaseio.com/").setPersistenceEnabled(true);

        return FirestoreClient.getFirestore();
    }
}
