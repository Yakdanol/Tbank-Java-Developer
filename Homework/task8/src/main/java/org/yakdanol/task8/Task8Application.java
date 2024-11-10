package org.yakdanol.task8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.yakdanol.task8", "org.yakdanol.task7", "org.yakdanol.task5_6"})
public class Task8Application {

	public static void main(String[] args) {
		SpringApplication.run(Task8Application.class, args);
	}

}
