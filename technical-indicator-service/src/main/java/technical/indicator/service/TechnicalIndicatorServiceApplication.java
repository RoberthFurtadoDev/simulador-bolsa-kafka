package technical.indicator.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class TechnicalIndicatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechnicalIndicatorServiceApplication.class, args);
	}

}
