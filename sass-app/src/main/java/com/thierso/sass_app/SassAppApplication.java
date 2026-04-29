package com.thierso.sass_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SassAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SassAppApplication.class, args);
	}

}
