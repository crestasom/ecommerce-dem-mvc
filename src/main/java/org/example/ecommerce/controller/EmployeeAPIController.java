package org.example.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.ecommerce.dto.EmployeeDTO;
import org.example.ecommerce.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
@Tag(name = "Employee API", description = "Operations related to employee registration and retrieval")
public class EmployeeAPIController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Register a new employee", description = "Saves employee details and returns the saved object.")
    @ApiResponse(responseCode = "200", description = "Employee registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<EmployeeDTO> registerEmployee(@Valid @RequestBody EmployeeDTO employee) {
        EmployeeDTO savedEmployee = employeeService.registerEmployee(employee);
        return ResponseEntity.ok(savedEmployee);
    }

    @GetMapping
    @Operation(summary = "List all employees", description = "Retrieves a list of all registered employees.")
    public ResponseEntity<java.util.List<EmployeeDTO>> listEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{email}")
    @Operation(summary = "Get employee by email", description = "Retrieves details of a specific employee using their email.")
    @ApiResponse(responseCode = "200", description = "Employee found")
    @ApiResponse(responseCode = "404", description = "Employee not found")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable String email) {
        return employeeService.getEmployeeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
