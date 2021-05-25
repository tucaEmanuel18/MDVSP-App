package com.labEleven.MDVSP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MDVSPApplication {

	public static void main(String[] args) {
		SpringApplication.run(MDVSPApplication.class, args);
		System.out.println("Server running at port 8001!");
	}

}
