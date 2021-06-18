package com.example.demo.API;

import com.example.demo.entities.*;
import com.example.demo.services.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
public class FireBaseController {
    @Autowired
    FirebaseInitializer fireBaseController;


    @GetMapping("/getpatient")
    public String getPatient() throws ExecutionException, InterruptedException {
        List<PatientInDB> patientList = new ArrayList<>();


        DocumentReference docRef = fireBaseController.
                getFirebae().collection("patients")
                .document("2LwEXUJ5DHynUHm1LQBt");
// asynchronously retrieve the document
        ApiFuture<DocumentSnapshot> future = docRef.get();
// ...
// future.get() blocks on response
        DocumentSnapshot document = future.get();
        if (document.exists()) {
            System.out.println("Document data: " + document.getData());
            return String.valueOf(document.getData());
        } else {
            System.out.println("No such document!");
            return "no such doc";
        }


    }

    @PostMapping(value = "/savePatient",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String generatePatient(int id, String name, String surname, int age){

        //parametrelerde girilen id'ye göre Doctor'u database'de arar
        Doctor doctor ;

        SessionFactory sessionFactory = SessionFact.getInstance();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        doctor = session.get(Doctor.class, id);
        session.getTransaction().commit();

        //Parametre'de verilen id ile eşleşen bir doktor bulunamazsa hasta yaratılmaz
        if (doctor == null){
            System.out.println("We do not do it here");
            return "We do not do it here";
        }

        System.out.println("yaradildi");


        //Hasta yaratılır ve firebase'e gönderilir
        Firestore db = fireBaseController.getFirebae();
        PatientInDB patient = new PatientInDB();
        patient.setUuid((int) System.currentTimeMillis());
        patient.setName(name );
        patient.setSurname(surname);
        patient.setAge(age);
        patient.setDateofJoin(new Date(age));
        db.collection("patients")
                .document()
                .set(patient);

        //creation orientation game at first
        Games game = new Games();
        game.setGameID(8);
        game.setUserId(patient.getUuid());
        game.setTrueCount(0);
        game.setFalseCount(0);
        game.setTotalQuestionCount(8);

        //Doktor'un hastası veritabanına kaydedilir
        session.beginTransaction();
        doctor = session.get(Doctor.class, id);
        session.save(patient);
        session.save(game);
        doctor.getPatients().add(patient);
        session.save(doctor);

        session.getTransaction().commit();
        session.close();

        //return "[{\"No\":\"1\",\"Name\":\"Adithya\"},{\"No\":\"2\",\"Name\":\"Jai\"}, {\"No\":\"3\",\"Name\":\"Raja\"}]";
        return patient.toString();

    }

    @PostMapping(value = "/makecangesofgame",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String addGame(int user_id, int gameID, int trueCount, int falseCount){
        // System.out.println("uuid si => "+ patient.getUuid());

        System.out.println(user_id);
        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE game_id = :game_id"
                + " and user_id = :user_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("game_id", gameID);
        query.setParameter("user_id", user_id);
        List<Games> results = query.list();
        for (Games r:
                results) {
            System.out.println("her bir row => "+  r.toString());
        }

        if(results.size() != 0){

            int gameAY = results.get(0).getId();
            Games game = session.get(Games.class,gameAY);

            game.setTrueCount(trueCount);
            game.setFalseCount(falseCount);



            session.beginTransaction();

            session.update(game);

            session.getTransaction().commit();

        }

        else {


            Games game = new Games();
            game.setTrueCount(trueCount);
            game.setFalseCount(falseCount);
            game.setUserId(user_id);
            game.setGameID(gameID);


            session.beginTransaction();

            session.save(game);

            session.getTransaction().commit();
        }
        session.close();
        return "";

    }


    @PostMapping(value = "/registerAsDoctor",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String doctorRegistiration(
            String username,
            String name,
            String surname,
            String password,
            String email){
        System.out.println("username => " + username + " name " + name + " surname " + surname
                + "password => " + password + " e mail => "  + email);
        SessionFactory sessionFactory = SessionFact.getInstance();

        Doctor doctor = new Doctor();
        doctor.setId((int) System.currentTimeMillis());
        doctor.seteMail(email);
        doctor.setName(name);
        doctor.setPassword(password);
        doctor.setSurname(surname);

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(doctor);
        session.getTransaction().commit();
        session.close();

        return "";
    }



    @PostMapping(value = "/mypatients",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody
    String  myPatients(int doctor_id){



        SessionFactory sessionFactory = SessionFact.getInstance();
        Session session = sessionFactory.openSession();
        String sql =
                "select patients_uuid from doctor_patients where doctor_id= :doctor_id" ;
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("doctor_id", doctor_id);
        List<Integer> mantaradam = query.list();

        //System.out.println("sonuçlarrrI" + mantaradam.toString());


        List<PatientInDB> patientInDBList = new ArrayList<>();

        session.beginTransaction();
        for(int i = 0; i < mantaradam.size();i++){
            patientInDBList.add(session.get(PatientInDB.class, mantaradam.get(i)));
        }
        session.getTransaction().commit();
        session.close();




        return patientInDBList.toString();

    }

    @PostMapping(value = "/signIn",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String  signIN(String email, String password){
//        System.out.println("doctorun idsi => " + doctor_id);


        SessionFactory sessionFactory = SessionFact.getInstance();
        Session session = sessionFactory.openSession();
        String sql = "select id from doctor where email= :doctor_email and password=:password" ;
        SQLQuery query = session.createSQLQuery(sql);
        query.setParameter("doctor_email", email);
        query.setParameter("password", password);
        List<Integer> mantaradam = query.list();


        if(mantaradam.size() == 0){
            return "0";
        }

        return mantaradam.get(0).toString();

    }

    @PostMapping(value = "/getGameInformation",consumes =
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String getGameInfo(int patient_id){

        List<GameInformation> informationList = new ArrayList<>();

        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String availableSQL = "SELECT * FROM availablegames";
        SQLQuery queryforAvailable = session.createSQLQuery(availableSQL);
        queryforAvailable.addEntity(AvailableGames.class);
        List<AvailableGames> resultsAvailable = queryforAvailable.list();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patient_id);
        List<Games> results = query.list();

        List<Games> gamesArrayList = new ArrayList<>();

        session.beginTransaction();
        for(int i = 0; i < results.size();i++){
            gamesArrayList.add(session.get(Games.class, results.get(i).getId()));
            int game_id = results.get(i).getGameID();
            AvailableGames availableGame = session.get(AvailableGames.class, game_id);
            informationList.add(
                    new GameInformation(
                            availableGame.getGameName(),
                            gamesArrayList.get(i).getTrueCount(),
                            gamesArrayList.get(i).getFalseCount(),
                            gamesArrayList.get(i).getTotalQuestionCount()
                    )
            );
        }

        session.getTransaction().commit();
        session.close();
        if(informationList.size() == 0){
            return "0";
        }
        return informationList.toString();
    }



    @PostMapping(value = "/wordsandorder",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody String  WordsAndOrder(String pairs, String patientid){
        //  Map<String, Map<String, Map<String, Object>>> docData = new HashMap<>();
        // Map<String, Map<String , Object>> QuestionCount = new HashMap<>();
        int questionTH =0;
        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", Integer.parseInt(patientid));
        query.setParameter("game_id", 7);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(7);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(Integer.parseInt(patientid));
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            //Size of result = 1, so we will get the 0th element of array all the time.
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();





        Map<String, Object> Questions = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(pairs);

            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                Questions.put(object.getString( "word"), object.get("order"));

            }

//            docData.put("name", "Los Angeles");
//            docData.put("state", "CA");
//            docData.put("country", "USA");
//            docData.put("regions", Arrays.asList("west_coast", "socal"));
// Add a new document (asynchronously) in collection "cities" with id "LA"


            //   QuestionCount.put("Question1", Questions);
            //  docData.put("WordOrder",QuestionCount);
            //document olarak => patient_id + test_türü(12346_facedetection
            fireBaseController
                    .getFirebae()
                    .collection("patients")
                    .document(patientid)
                    .collection("WordsAndOrder")
                    .document(String.valueOf(questionTH))
                    .set(Questions);

// ...
// future.get() blocks on response
            //System.out.println("Update time : " + future.get().getUpdateTime());

        }catch (Exception e) {
            return "WAAAAAH! Veri yanlış girilmiş.";
        }
        return  pairs.toString();

    }


    @GetMapping(value = "/deneme123",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void dummytsp() {
        System.out.println(": ThreadId " + Thread.currentThread().getId());

        System.out.println("aktif thread sayısı => "+  Thread.activeCount());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Doktor, hastaya soru hazırlarken, fotoğraf gerektiren işlemler için kullanır
    //if answer = 1, çamaşır makınası if 2 , bulaşık makinası
    //gameID is gonna be 5
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST ,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,"multipart/form-data"},
            produces ={MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientID") int patientID,
            @RequestParam("answer") int answer) {
        int questionTH = 0;


        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patientID);
        query.setParameter("game_id", 5);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(5);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(patientID);
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();



        System.out.println("hasta number => " + patientID);
        final Path root = Paths.get("dosya/" +"dishesandwashes-"+
                patientID +"-"+ questionTH + ".png");
        System.out.println(file);
        try {
            // retrieve image
            Files.copy(file.getInputStream(),
                    root.resolve(Objects.requireNonNull(file.getOriginalFilename())));

            Map<String, Object> Question = new HashMap<>();
            Question.put(root.toString(),answer);


            fireBaseController.getFirebae().
                    collection("patients").
                    document(String.valueOf(patientID)).
                    collection("DishesAndWashes").
                    document(String.valueOf(questionTH)).set(Question);

            System.out.println("kaydedildi");
        } catch (Exception e) {
            System.out.println("kaydedilemedi => " );
            e.printStackTrace();
        }
        try {


        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }



    //Doktor, hastaya soru hazırlarken, fotoğraf gerektiren işlemler için kullanır
    //if answer = 1, çamaşır makınası if 2 , bulaşık makinası
    //gameID is gonna be 5
    @RequestMapping(value = "/emotiondetection", method = RequestMethod.POST ,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,"multipart/form-data"},
            produces ={MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<?> emotionDetection(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientID") int patientID,
            @RequestParam("answer") int answer) {
        int questionTH = 0;


        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patientID);
        query.setParameter("game_id", 6);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(6);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(patientID);
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();



        System.out.println("hasta number => " + patientID);
        final Path root = Paths.get("dosya/" +"emotiondetection-"+
                patientID+ "-" + questionTH + ".png");
        System.out.println(file);
        try {
            // retrieve image
            Files.copy(file.getInputStream(),
                    root.resolve(Objects.requireNonNull(file.getOriginalFilename())));


            Map<String, Object> Question = new HashMap<>();
            Question.put(root.toString(),answer);


            fireBaseController.getFirebae().
                    collection("patients").
                    document(String.valueOf(patientID)).
                    collection("EmotionDetection").
                    document(String.valueOf(questionTH)).set(Question);



        } catch (Exception e) {
            System.out.println("kaydedilemedi => " );
            e.printStackTrace();
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }







    @RequestMapping(value = "/animaldetection", method = RequestMethod.POST ,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,"multipart/form-data"},
            produces ={MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<?> animalDetection(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientID") int patientID,
            @RequestParam("answer") String answer) {
        int questionTH = 0;


        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patientID);
        query.setParameter("game_id", 2);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(2);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(patientID);
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();



        System.out.println("hasta number => " + patientID);
        final Path root = Paths.get("dosya/" +
                "animaltexting-"+ patientID + questionTH + ".png");
        System.out.println(file);
        try {
            // retrieve image
            Files.copy(file.getInputStream(),
                    root.resolve(Objects.requireNonNull(file.getOriginalFilename())));





            Map<String, Object> Question = new HashMap<>();
            Question.put(root.toString(),answer);


            fireBaseController.getFirebae().
                    collection("patients").
                    document(String.valueOf(patientID)).
                    collection("AnimalDetection").
                    document(String.valueOf(questionTH)).set(Question);

            System.out.println("kaydedildi");
        } catch (Exception e) {
            System.out.println("kaydedilemedi => " );
            e.printStackTrace();
        }
        try {


        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }






    @RequestMapping(value = "/vegetablesandfruits", method = RequestMethod.POST ,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,"multipart/form-data"},
            produces ={MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<?> vegandfru(
            @RequestParam("file") MultipartFile file,
            @RequestParam("patientID") int patientID,
            @RequestParam("answer") String answer) {
        int questionTH = 0;


        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patientID);
        query.setParameter("game_id", 4);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(4);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(patientID);
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();



        System.out.println("hasta number => " + patientID);
        final Path root = Paths.get("dosya/" +"VegAndFru-"+ patientID + questionTH + ".png");
        System.out.println(file);
        try {
            // retrieve image
            Files.copy(file.getInputStream(),
                    root.resolve(Objects.requireNonNull(file.getOriginalFilename())));





            Map<String, Object> Question = new HashMap<>();
            Question.put(root.toString(),answer);


            fireBaseController.getFirebae().
                    collection("patients").
                    document(String.valueOf(patientID)).
                    collection("VegAndFru").
                    document(String.valueOf(questionTH)).set(Question);

            System.out.println("kaydedildi");
        } catch (Exception e) {
            System.out.println("kaydedilemedi => " );
            e.printStackTrace();
        }
        try {


        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }





    @RequestMapping(value = "/facedetection", method = RequestMethod.POST ,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,"multipart/form-data"},
            produces ={MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    @ResponseBody
    public ResponseEntity<?> facedetection(
            @RequestParam("file") MultipartFile file,
            @RequestParam("secondfile") MultipartFile secondfile,
            @RequestParam("thirdfile") MultipartFile thirdfile,
            @RequestParam("patientID") int patientID,
            @RequestParam("answer") String answer) {
        int questionTH = 0;


        SessionFactory sessionFactory = SessionFact.getInstance();

        Session session = sessionFactory.openSession();

        String sql = "SELECT * FROM games WHERE "
                + "  user_id = :user_id and game_id = :game_id";
        SQLQuery query = session.createSQLQuery(sql);
        query.addEntity(Games.class);
        query.setParameter("user_id", patientID);
        query.setParameter("game_id", 1);
        List<Games> results = query.list();

        session.beginTransaction();
        if(results.size() == 0){
            Games game = new Games();
            game.setGameID(1);
            game.setTrueCount(0);
            game.setFalseCount(0);
            game.setUserId(patientID);
            game.setTotalQuestionCount(1);
            session.save(game);
            questionTH = 1;
        }else{
            Games game = results.get(0);

            game.setTotalQuestionCount(game.getTotalQuestionCount()+ 1);
            questionTH = game.getTotalQuestionCount();
            session.update(game);
        }
        session.getTransaction().commit();
        session.close();



        System.out.println("hasta number => " + patientID);
        final Path root = Paths.get("dosya/" +"facedetection-sidephoto-"+
                patientID + questionTH + ".png");
        final Path firstPersonRoot = Paths.get("dosya/" +"facedetection-firstperson-"+
                patientID + questionTH + ".png");
        final Path secondPersonRoot = Paths.get("dosya/" +"facedetection-secondperson-"+
                patientID + questionTH + ".png");
        // System.out.println(file);
        try {
            // retrieve image
            Files.copy(file.getInputStream(),
                    root.resolve(Objects.requireNonNull(file.getOriginalFilename())));

            Files.copy(secondfile.getInputStream(),
                    firstPersonRoot.resolve(Objects.requireNonNull(secondfile.getOriginalFilename())));
            Files.copy(thirdfile.getInputStream(),
                    secondPersonRoot.resolve(Objects.requireNonNull(thirdfile.getOriginalFilename())));


            Map<String, Object> Question = new HashMap<>();

            if(answer.equals("1")){
                Question.put("answer","1");
                Question.put(firstPersonRoot.toString(),"true");
                Question.put(secondPersonRoot.toString(),"false");

            }else if(answer.equals("2")){
                Question.put("answer","2");
                Question.put(firstPersonRoot.toString(),"false");
                Question.put(secondPersonRoot.toString(),"true");

            }
            Question.put(root.toString(),"Person");
            fireBaseController.getFirebae().
                    collection("patients").
                    document(String.valueOf(patientID)).
                    collection("facedetection").
                    document(String.valueOf(questionTH)).set(Question);

            System.out.println("kaydedildi");
        } catch (Exception e) {
            System.out.println("kaydedilemedi => " );
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }














}
