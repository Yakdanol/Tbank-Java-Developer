package org.yakdanol.task2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yakdanol.task2.service.CityService;

@SpringBootApplication
public class Task2Application implements CommandLineRunner {
	private final CityService cityService;

	@Autowired
	public Task2Application(CityService cityService) {
		this.cityService = cityService;
	}

	public static void main(String[] args) {
		SpringApplication.run(Task2Application.class, args);
	}

	@Override
	public void run(String... args) {
		String jsonFilePath = "Homework/src/main/resources/city.json";
		cityService.processJson(jsonFilePath);
	}
}
