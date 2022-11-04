package com.demo.person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PersonDetailApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonDetailApiApplication.class, args);
	}

}
