package vn.ript.mediator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MediatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MediatorApplication.class, args);
	}

}
