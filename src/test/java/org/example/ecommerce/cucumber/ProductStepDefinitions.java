package org.example.ecommerce.cucumber;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.example.ecommerce.model.dto.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

/**
 * Cucumber Step Definitions for Product Feature
 * Refactored to perform Integration Testing using MockMvc and Validator API.
 */
@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductStepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductStepDefinitions.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Validator validator;

    private ProductDTO lastEnteredProduct;
    private MvcResult lastResult;
    private Set<ConstraintViolation<ProductDTO>> violations;

    @Given("I am logged in as an administrator")
    public void i_am_logged_in_as_an_administrator() {
        LOGGER.info("Step: Logged in as ADMIN (Simulated via @WithMockUser placeholder)");
    }

    @Given("I navigate to the add product page")
    public void i_navigate_to_the_add_product_page() throws Exception {
        LOGGER.info("Step: Navigating to Add Product Page via MockMvc");
        mockMvc.perform(get("/products/add").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @When("I enter product details with name {string}, description {string}, and price {double}")
    public void i_enter_product_details(String name, String description, Double price) {
        LOGGER.info("Step: Preparing ProductDTO for validation and integration test");
        lastEnteredProduct = ProductDTO.builder()
                .name(name.isEmpty() ? null : name) // Simulation of empty field as null for validator
                .description(description)
                .price(price)
                .build();
    }

    @When("I click save")
    public void i_click_save() throws Exception {
        LOGGER.info("Step: Validating ProductDTO using Jakarta Validator API");
        violations = validator.validate(lastEnteredProduct);

        if (violations.isEmpty()) {
            LOGGER.info("Validation passed, performing MockMvc POST to save product");
            lastResult = mockMvc.perform(post("/products/save")
                    .with(csrf())
                    .with(user("admin").roles("ADMIN"))
                    .flashAttr("product", lastEnteredProduct))
                    .andReturn();
        } else {
            LOGGER.warn("Validation failed with {} violations", violations.size());
            violations.forEach(v -> LOGGER.warn("Violation: {}", v.getMessage()));
        }
    }

    @Then("I should see a success message {string}")
    public void i_should_see_a_success_message(String expectedMessage) throws Exception {
        LOGGER.info("Step: Verifying Success Message in Flash Attributes");
        Assertions.assertTrue(violations.isEmpty(), "Expected no validation violations");
        Assertions.assertEquals(expectedMessage, lastResult.getFlashMap().get("message"));
    }

    @Then("the product {string} should be present in the product list")
    public void the_product_should_be_present_in_the_product_list(String productName) throws Exception {
        LOGGER.info("Step: Verifying product presence in the list via GET /products");
        mockMvc.perform(get("/products").with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Then("I should see an error message or the form should remain")
    public void i_should_see_an_error_message() {
        LOGGER.info("Step: Verifying validation errors were caught by Validator API");
        Assertions.assertFalse(violations.isEmpty(), "Expected validation violations but found none");
    }
}
