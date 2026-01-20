package org.example.ecommerce.service;

import org.example.ecommerce.dto.EmployeeDTO;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    public String registerEmployee(EmployeeDTO employee) {
        // Phase 2: Manual Validation Removed.
        // Validation is now handled by the Controller via @Valid.

        // "Saving" logic mock
        System.out.println("Saving employee: " + employee);
        return "success";
    }
}
