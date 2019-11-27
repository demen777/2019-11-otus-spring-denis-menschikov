package ru.otus.demen;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.demen.service.TestingRunner;


public class App {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        TestingRunner testingRunner = context.getBean(TestingRunner.class);
        testingRunner.run();
    }
}
