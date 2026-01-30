package org.example.ecommerce.repository.jpa;

import org.example.ecommerce.model.Employee;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface EmployeeJpaRepository extends JpaRepository<Employee, Long> {
	Employee findByEmail(String email);
}
