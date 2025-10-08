package br.feevale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class App {

	public static void main(final String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(UTC));
		SpringApplication.run(App.class, args);
	}

}
