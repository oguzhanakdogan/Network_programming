package com.example.demo;

import com.example.demo.API.SessionFact;
import com.example.demo.entities.AvailableGames;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        SessionFactory sessionFactory = SessionFact.getInstance();
        Session session = sessionFactory.openSession();

        AvailableGames facedetection = new AvailableGames();
        facedetection.setId(1);
        facedetection.setGameName("facedetection");

        AvailableGames animaldetection = new AvailableGames();
        animaldetection.setId(2);
        animaldetection.setGameName("animal_detection");

        AvailableGames makesentence = new AvailableGames();
        makesentence.setId(3);
        makesentence.setGameName("make_sentence");

        AvailableGames vegAndFruit = new AvailableGames();
        vegAndFruit.setId(4);
        vegAndFruit.setGameName("VegAndFru");


        AvailableGames machines = new AvailableGames();
        machines.setId(5);
        machines.setGameName("DishesAndWashes");


        AvailableGames emotions = new AvailableGames();
        emotions.setId(6);
        emotions.setGameName("EmotionDetection");

        AvailableGames WordsAndOrder = new AvailableGames();
        WordsAndOrder.setId(7);
        WordsAndOrder.setGameName("WordsAndOrder");

        AvailableGames Orientation = new AvailableGames();
        WordsAndOrder.setId(8);
        WordsAndOrder.setGameName("Orientation");

//        AvailableGames DishesAndWashes = new AvailableGames();
//        WordsAndOrder.setId(8);
//        WordsAndOrder.setGameName("PersonDetection");

        session.beginTransaction();

        session.save(facedetection);
        session.save(animaldetection);
        session.save(makesentence);
        session.save(vegAndFruit);
        session.save(machines);
        session.save(emotions);
        session.save(WordsAndOrder);

        session.getTransaction().commit();
        session.close();

//
//        Car car = new Car();
//       car.setVehicheName("Volvo");
//
//        Car car1 = new Car();
//       car1.setVehicheName("BMW");
////
//        Courses java = new Courses();
//        java.setCourseName("Java");
//        Courses perl = new Courses();
//        perl.setCourseName("perl");
//
//        Student oguzhan =new Student("maria", "horison" , "oguzhanakdogan06@gmail.com", 21);
//        oguzhan.getCourses().add(java);
//        oguzhan.getCourses().add(perl);
//        oguzhan.setAddress(addr);
//        oguzhan.getCars().add(car);
//        oguzhan.getCars().add(car1);
//
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        session.save(oguzhan);
//        session.save(car);
//        session.save(car1);
//        session.save(java);
//        session.save(perl);
//        session.getTransaction().commit();
//
//        oguzhan = null;
//
//        //Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        //Since we are using ORM(Hibernate), we are not gonna be dealing with table, but classes.
//        //second argument should be primary key
//        oguzhan=(Student) session.get(Student.class,1);
//        System.out.println("yaziyor" + oguzhan.toString());



//        SessionFactory sessionFactory = SessionFact.getInstance();
//
//        Doctor doctor = new Doctor();
//        doctor.setId((int) System.currentTimeMillis());
//        doctor.seteMail("email@hotmail.com");
//        doctor.setName("name");
//        doctor.setPassword("password");
//        doctor.setUserName("username");
//        doctor.setSurname("surname");
//
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        session.save(doctor);
//        session.getTransaction().commit();
    }

//    @Bean
//   public CommandLineRunner commandLineRunner(StudentRepository studentRepository){
//        return args -> {
//            Student maria =new Student("maria", "horison" , "oguzhanakdogan06@gmail.com", 21);
//            studentRepository.save(maria);
//        };
//    }
}
