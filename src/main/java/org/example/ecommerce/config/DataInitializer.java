//package org.example.ecommerce.config;
//
//import org.example.ecommerce.model.Product;
//import org.example.ecommerce.model.User;
//import org.example.ecommerce.repository.jpa.ProductJpaRepository;
//import org.example.ecommerce.repository.jpa.UserJpaRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class DataInitializer {
//
//	@Bean
//	CommandLineRunner initDatabase(UserJpaRepository userRepo, ProductJpaRepository productRepo,
//			PasswordEncoder passwordEncoder) {
//		return args -> {
//			if (userRepo.count() == 0) {
//				User admin = new User();
//				admin.setUsername("admin");
//				admin.setEmail("admin@example.com");
//				admin.setPassword(passwordEncoder.encode("password"));
//				admin.setRole("ROLE_ADMIN");
//				userRepo.save(admin);
//
//				User user1 = new User();
//				user1.setUsername("user1");
//				user1.setEmail("user1@example.com");
//				user1.setPassword(passwordEncoder.encode("password"));
//				user1.setRole("ROLE_USER");
//				userRepo.save(user1);
//			}
//
//			if (productRepo.count() == 0) {
//				Product p1 = new Product();
//				p1.setName("Modern Laptop");
//				p1.setDescription("High performance workspace laptop");
//				p1.setPrice(1200.00);
//				productRepo.save(p1);
//
//				Product p2 = new Product();
//				p2.setName("Wireless Mouse");
//				p2.setDescription("Ergonomic wireless mouse");
//				p2.setPrice(25.50);
//				productRepo.save(p2);
//			}
//		};
//	}
//}
