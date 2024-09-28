package com.springbootproject.seasidehotel.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Example API", version = "v1", description = "Example API documentation"))
public class OpenApiConfig {
}
