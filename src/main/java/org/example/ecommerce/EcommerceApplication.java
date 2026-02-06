package org.example.ecommerce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class EcommerceApplication implements CommandLineRunner {
	@Autowired
	PasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
		System.out.println("Ecommerce Application Started...");
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("run");
		log.info(encoder.encode("test"));

	}
}
