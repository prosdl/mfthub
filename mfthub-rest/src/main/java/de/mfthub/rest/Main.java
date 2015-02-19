package de.mfthub.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import de.mfthub.model.ConfigurationSpringJPA;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {
      org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class})
@Import(ConfigurationSpringJPA.class)
@ImportResource("classpath:/spring-oauth2.xml")
public class Main {
   public static void main(String[] args) {
      SpringApplication.run(Main.class, args);
   }
}
