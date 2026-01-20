package org.example.ecommerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    @GetMapping("/register")
    public String showRegisterForm() {
        return "employees/register";
    }

    @GetMapping("/summary")
    public String showSummary() {
        return "employees/summary";
    }
}
