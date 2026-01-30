package org.example.ecommerce;

import org.example.ecommerce.repository.jpa.ProductJpaRepository;
import org.example.ecommerce.repository.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication implements CommandLineRunner {
	@Autowired
	private UserJpaRepository userRepo;
	@Autowired
	private ProductJpaRepository productRepo;
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
//		if (userRepo.count() == 0) {
//			// Initialize users
//			User admin = new User();
//			admin.setUsername("admin");
//			admin.setEmail("admin@example.com");
//			admin.setRole("ADMIN");
//			userRepo.save(admin);
//
//			User user1 = new User();
//			user1.setUsername("user1");
//			user1.setEmail("user1@example.com");
//			user1.setRole("USER");
//			userRepo.save(user1);
//
//			System.out.println("Initialized users");
//		}
//
//		if (productRepo.count() == 0) {
//			// Initialize products
//			productRepo.save(new Product(null, "Laptop", "High-performance laptop", 999.99));
//			productRepo.save(new Product(null, "Mouse", "Wireless mouse", 29.99));
//			productRepo.save(new Product(null, "Keyboard", "Mechanical keyboard", 79.99));
//			productRepo.save(new Product(null, "Monitor", "27-inch 4K monitor", 399.99));
//			productRepo.save(new Product(null, "Headphones", "Noise-cancelling headphones", 199.99));
//
//			System.out.println("Initialized products");
//		}

	}
}
