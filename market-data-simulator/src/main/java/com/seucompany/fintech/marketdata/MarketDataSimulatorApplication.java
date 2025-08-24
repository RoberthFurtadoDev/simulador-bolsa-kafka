package com.seucompany.fintech.marketdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Habilita o suporte a tarefas agendadas (@Scheduled)
public class MarketDataSimulatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketDataSimulatorApplication.class, args);
	}

}
