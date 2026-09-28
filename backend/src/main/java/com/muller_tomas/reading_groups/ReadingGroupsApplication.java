package com.muller_tomas.reading_groups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Reading Groups API", version = "1.0", description = "API for collaborative reading and annotation", contact = @Contact(name = "Tomas Muller", url = "https://github.com/T-o-m-a-s-M-u-l-l-e-r")))
@SpringBootApplication
public class ReadingGroupsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadingGroupsApplication.class, args);
	}

}
