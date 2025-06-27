package com.sumte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SumteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SumteApplication.class, args);
	}

}
