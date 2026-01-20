package org.example.ecommerce.controller;

import org.example.ecommerce.dto.EmployeeDTO;
import org.example.ecommerce.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeAPIController {

    private final EmployeeService employeeService;

    public EmployeeAPIController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
	public ResponseEntity<String> registerEmployee(@Valid @RequestBody EmployeeDTO employee) {
        String result = employeeService.registerEmployee(employee);

        if (!"success".equals(result)) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok("Employee registered successfully");
    }
}
