package com.db.rbazadatak.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {

  @Bean
  public OpenAPI myOpenAPI() {
    Contact contact = new Contact();
    contact.setEmail("darij.b@gmail.com");

    Info info = new Info()
        .title("Person api")
        .version("1.0")
        .contact(contact)
        .description("Add, find or delete person");
    return new OpenAPI().info(info);
  }
}