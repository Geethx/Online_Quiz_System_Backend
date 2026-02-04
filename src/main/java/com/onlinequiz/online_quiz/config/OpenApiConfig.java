package com.onlinequiz.online_quiz.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development Server");

        Contact contact = new Contact();
        contact.setName("Online Quiz System Team");
        contact.setEmail("support@onlinequiz.com");

        License license = new License()
                .name("Academic Project")
                .url("https://github.com/Geethx/Online_Quiz_System_Backend");

        Info info = new Info()
                .title("Online Quiz and Assignment System API")
                .version("1.0.0")
                .description(
                        "RESTful API for managing questions, assignments, and quiz attempts with timed auto-submission functionality.")
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer));
    }
}
