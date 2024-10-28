package org.yakdanol.task5_6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.yakdanol.task5_6.model.repository")
public class Task56Application {

	public static void main(String[] args) {
		SpringApplication.run(Task56Application.class, args);
	}

}
