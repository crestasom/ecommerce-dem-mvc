package org.example.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Demo API")
                        .version("1.0")
                        .description(
                                "API documentation for the E-commerce Demo application, including the Employee registration and management API.")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@example.com")));
    }
}
