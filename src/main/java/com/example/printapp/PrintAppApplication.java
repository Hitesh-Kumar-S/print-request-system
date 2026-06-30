package com.example.printapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PrintAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrintAppApplication.class, args);
	}

}
