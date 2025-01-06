package com.gestion.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gestion.tickets")
public class TicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketsApplication.class, args);
	}

}
