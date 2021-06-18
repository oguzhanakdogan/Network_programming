package com.example.demo.API;

import com.example.demo.entities.AvailableGames;
import com.example.demo.entities.Games;
import com.example.demo.entities.PatientInDB;
import com.example.demo.entities.TimeData;
import com.example.demo.services.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.activation.FileTypeMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    FirebaseInitializer firebaseInitializer;

    @GetMapping(path = "/mypat")
    public String deneme(
            int patient_id,
            int game_id
    ) throws ExecutionException, InterruptedException {

        AvailableGames game = null;
        SessionFactory sessionFactory = SessionFact.getInstance();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        game = session.get(AvailableGames.class, game_id);
        session.getTransaction().commit();
        session.close();

        //get the collection of question type
        CollectionReference collectionReference = firebaseInitializer.
                getFirebae().
                collection("patients")
                .document(String.valueOf(patient_id))
                .collection(game.getGameName());

        ApiFuture<QuerySnapshot> future1 = collectionReference.get();
        QuerySnapshot q = future1.get();

        final JSONObject jsonObject = new JSONObject();
        var ref = new Object() {
            Integer questionIndex = 0;
        };
        //get list of collections
        List<QueryDocumentSnapshot> snapshots = q.getDocuments();

        snapshots.forEach(queryDocumentSnapshot ->
                {
                    try {
                        jsonObject.
                                put(String.valueOf(ref.questionIndex),
                                        queryDocumentSnapshot.getData());
                        ref.questionIndex++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        );

        if (jsonObject.length() != 0) {
            System.out.println("Bunu döndürüyorum => " +
                    jsonObject.toString());
            return jsonObject.toString();

        }

        return "No such document!";

    }

    @GetMapping(path = "/downloadimage")
    public ResponseEntity<byte[]> downloadImage(
            String fileName
    ) throws IOException {

        File img = new File(fileName);
        return ResponseEntity.ok().
                contentType(MediaType.
                        valueOf(FileTypeMap.
                                getDefaultFileTypeMap().
                                getContentType(img))).
                body(Files.readAllBytes(img.toPath()));
    }

    @GetMapping(path = "/recordanswers")
    public String recordAnswers(
            int trueCount,
            int falseCount,
            int gameID,
            int patient_id,
            int duration
    ) {
        System.out.println("Doğru sayısı => " + trueCount);
        System.out.println("Yanlış sayısı => " + falseCount);
        SessionFactory factory = SessionFact.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();


        String mySQL = "SELECT * FROM availablegames WHERE id = :id";

        SQLQuery myQuery = session.createSQLQuery(mySQL);
        myQuery.addEntity(AvailableGames.class);
        myQuery.setParameter("id", gameID);

        List<AvailableGames> myResults = myQuery.list();


        TimeData data  = new TimeData();
        data.setPatient_id(patient_id);
        data.setDate((String.valueOf( new Date().getDay()) +new Date().getMonth() + new Date().getYear()));
        data.setGame_name(myResults.get(0).getGameName());
        data.setTime(duration);











        String sql = "SELECT * FROM games WHERE game_id = :game_id"
                + " and user_id = :user_id";

        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("game_id", gameID);
        query.setParameter("user_id", patient_id);
        List<Games> results = query.list();
        if (results.size() == 0) {
            session.close();
            return "Not OK";
        }
        Games game = results.get(0);



        game.setTrueCount(trueCount);
        game.setFalseCount(falseCount);
        session.update(game);
        session.save(data);
        session.getTransaction().commit();


        session.close();
        System.out.println("ok");
        return "OK";
    }

    @GetMapping(path = "/getpersonalinfo")
    public String getNameAndSurname(int patient_id) {
        System.out.println("idmiz" + patient_id);
        String patient = "";
        SessionFactory factory = SessionFact.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        String sql = "SELECT * FROM patients WHERE "
                + "uuid = :user_id";

        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(PatientInDB.class);
        query.setParameter("user_id", patient_id);
        List<PatientInDB> results = query.list();
        if (results.size() == 0) {
            session.close();
            return "NOT OK!";
        }
        patient = results.get(0).toString();
        session.close();

        return patient;


    }

    @GetMapping(path = "/setorientation")
    public String orientation(
            int patient_id,
            boolean ad_soyad,
            boolean gun,
            boolean tarih,
            boolean ay,
            boolean year,
            boolean mevsim,
            boolean gun_saati,
            boolean yer,
            int duration

    ) {
        int trueCount = 0;
        int falseCount = 0;
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(ad_soyad);
        list.add(gun);
        list.add(tarih);
        list.add(ay);
        list.add(year);
        list.add(mevsim);
        list.add(gun_saati);
        list.add(yer);


        String patient = "";
        SessionFactory factory = SessionFact.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        String sql = "SELECT * FROM patients WHERE "
                + "uuid = :user_id";

        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(PatientInDB.class);
        query.setParameter("user_id", patient_id);
        List<PatientInDB> results = query.list();
        if (results.size() == 0) {
            session.close();
            return "NOT OK!";
        }


        Map<String,Boolean> orientationJson = new HashMap<>();

        try {
            orientationJson.put("Ad, Soyad", ad_soyad);
            orientationJson.put("Gün", gun);
            orientationJson.put("Tarih", tarih);
            orientationJson.put("Ay", ay);
            orientationJson.put("yıl", year);
            orientationJson.put("Mevsim", mevsim);
            orientationJson.put("Günün Saati", gun_saati);
            orientationJson.put("Yer", yer);
        } catch (Exception e) {
            e.printStackTrace();
            return "Not OK!";
        }

        for (boolean answer :
                list) {
            if (answer) {
                trueCount++;
            } else {
                falseCount++;
            }
        }

        String stringQuery = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";

        SQLQuery queryResult = session.createSQLQuery(stringQuery);
        queryResult.addEntity(Games.class);
        queryResult.setParameter("user_id", patient_id);
        queryResult.setParameter("game_id", 8);
        List<Games> myResults = queryResult.list();
        Games game = myResults.get(0);
        game.setFalseCount(falseCount);
        game.setTrueCount(trueCount);


        String theQuery = "SELECT * FROM timedata WHERE "
                + "  patient_id = :patient_id and game_name =:Oryantasyon";

        SQLQuery theResult = session.createSQLQuery(theQuery);
        theResult.addEntity(TimeData.class);
        theResult.setParameter("patient_id", patient_id);
        theResult.setParameter("Oryantasyon", "Oryantasyon");
        List<TimeData> theResults = theResult.list();

        Optional<TimeData> i =theResults.stream().max(Comparator.comparingInt(TimeData::getPart));
        System.out.println("imiz ise =>" + i);


        TimeData data = new TimeData();
        data.setPatient_id(patient_id);
        data.setTime(duration);
        data.setDate("10/10/10");
        data.setGame_name("Oryantasyon");

        if(i.isPresent()){
            data.setPart(i.get().getPart() + 1);
        }else{
            data.setPart(1);
        }

        session.save(data);
        session.update(game);
        session.getTransaction().commit();


        firebaseInitializer
                .getFirebae()
                .collection("patients")
                .document(String.valueOf(patient_id))
                .collection("oryantasyon")
                .document(String.valueOf(1))
                .set(orientationJson);


        session.close();
        return patient;


    }

    @RequestMapping(path = "/getoriantation")
    public String getOriantationAnswers(
            int patient_id) throws ExecutionException,
            InterruptedException {

// asynchronously retrieve the document
// ...
// future.get() blocks on response

        JSONObject answer = new JSONObject();
        DocumentReference documentReference =
                firebaseInitializer.getFirebae()
                .collection("patients")
                .document(String.valueOf(patient_id))
                .collection("oryantasyon")
                .document("1");
        //get the collection of question type

        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        if(document.exists()){
            System.out.println("bunu döndürüyorum" +
                    document.getData().toString());
            System.out.println(document.getData().get("Ad, Soyad"));
            try {
                answer.put("Ad, Soyad",document.getData().get("Ad, Soyad"));
                answer.put("Gün",document.getData().get("Gün"));
                answer.put("Tarih",document.getData().get("Tarih"));
                answer.put("Ay",document.getData().get("Ay"));
                answer.put("yıl",document.getData().get("yıl"));
                answer.put("Mevsim",document.getData().get("Mevsim"));
                answer.put("Günün Saati",document.getData().get("Günün Saati"));
                answer.put("Yer",document.getData().get("Yer"));


            }catch (Exception e){
                return "NOTOK!";
            }



            return answer.toString();
        }
        return "NOTOK";
    }

    @RequestMapping(path = "/gettimedata")
    public String getTimeData(int patient_id){
        System.out.println("istenilen patient id => " + patient_id);
        SessionFactory factory = SessionFact.getInstance();
        Session session = factory.openSession();
        session.beginTransaction();
        String sql = "SELECT * FROM timedata WHERE "
                + "patient_id = :patient_id";

        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(TimeData.class);
        query.setParameter("patient_id", patient_id);
        List<TimeData> results = query.list();
        System.out.println("query size + "   + results.size());
        return results.toString();
    }
}