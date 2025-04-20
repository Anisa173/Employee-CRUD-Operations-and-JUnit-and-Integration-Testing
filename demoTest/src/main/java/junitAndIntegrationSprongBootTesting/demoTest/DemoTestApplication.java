package junitAndIntegrationSprongBootTesting.demoTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoTestApplication.class, args);
       Student student = new Student("Anisa",1);
        System.out.println("The student is called: " + student.getName());
        System.out.println("The StudentId is: " + student.getId());
    }





}
