package com.dalia.ProjetoDalia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class 	     ProjetoDaliaApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjetoDaliaApplication.class, args);
	}
}
