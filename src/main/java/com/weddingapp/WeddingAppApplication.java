package com.weddingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WeddingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeddingAppApplication.class, args);
	}

}
