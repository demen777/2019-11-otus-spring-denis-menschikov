package ru.otus.demen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.otus.demen.service.TestingRunner;

@SpringBootApplication
public class Hw3TestingApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Hw3TestingApplication.class, args);
		TestingRunner testingRunner = context.getBean(TestingRunner.class);
		testingRunner.run();
	}

}
