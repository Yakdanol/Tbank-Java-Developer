package org.yakdanol.homework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yakdanol.homework.service.CityService;

@SpringBootApplication
public class HomeworkApplication implements CommandLineRunner {
	private final CityService cityService;

	@Autowired
	public HomeworkApplication(CityService cityService) {
		this.cityService = cityService;
	}

	public static void main(String[] args) {
		SpringApplication.run(HomeworkApplication.class, args);
	}

	@Override
	public void run(String... args) {
		String jsonFilePath = "Homework/src/main/resources/city.json";
		cityService.processJson(jsonFilePath);
	}
}
