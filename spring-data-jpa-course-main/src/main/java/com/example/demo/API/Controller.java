package com.example.demo.API;

import com.example.demo.TextWriter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    TextWriter t;

    @RequestMapping("/")
    public String index(){

        SessionFactory sessionFactory = SessionFact.getInstance();

//        Address addr = new Address();
//        addr.setCity("Ankara");
//        addr.setPincode("06380");
//        addr.setState("iç anadolu");
//        addr.setStreet("polatlı");
//
//        Car car = new Car();
//        car.setVehicheName("Volvo");
//
//        Car car1 = new Car();
//        car1.setVehicheName("BMW");
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



        //oguzhan = null;

        //Session session = sessionFactory.openSession();

        //Since we are using ORM(Hibernate), we are not gonna be dealing with table, but classes.
        //second argument should be primary key
     //   oguzhan=(Student) session.get(Student.class,1);















        return "t.writeText(oguzhan.toString())";



    }
}
