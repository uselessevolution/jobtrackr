package com.jobtrackr.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI jobTrackrOpenApi() {

        Contact contact = new Contact()
                .name("JobTrackr Development Team");

        Info info = new Info()
                .title("JobTrackr API")
                .version("1.0.0")
                .description(
                        "REST API for managing job applications, "
                        + "interviews, reminders and notifications."
                )
                .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}