package org.yakdanol.task15;

import org.openjdk.jmh.runner.RunnerException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yakdanol.task15.utils.BenchmarkMessageBrokers;

@SpringBootApplication
public class Task15Application {

	public static void main(String[] args) throws RunnerException {
		SpringApplication.run(Task15Application.class, args);
			BenchmarkMessageBrokers.run(args);
	}
}
