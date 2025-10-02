package br.feevale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
//@EntityScan(basePackages = {"br.feevale.security.domain"})
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
