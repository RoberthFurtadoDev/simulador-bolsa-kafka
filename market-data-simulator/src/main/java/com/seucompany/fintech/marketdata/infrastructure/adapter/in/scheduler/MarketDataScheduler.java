package com.seucompany.fintech.marketdata.infrastructure.adapter.in.scheduler;

import com.seucompany.fintech.marketdata.application.port.out.StockTickEventPublisher;
import com.seucompany.fintech.marketdata.domain.StockTick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Random;

/**
 * Adapter de "entrada" que simula a geração de dados do mercado.
 * Utiliza uma tarefa agendada (@Scheduled) do Spring para rodar periodicamente.
 */
@Component
public class MarketDataScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MarketDataScheduler.class);
    private static final List<String> SYMBOLS = List.of("PETR4", "VALE3", "MGLU3", "ITUB4", "BBDC4");
    private final Random random = new Random();

    private final StockTickEventPublisher eventPublisher;

    public MarketDataScheduler(StockTickEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    // Roda a cada 2 segundos.
    @Scheduled(fixedRate = 2000)
    public void generateMarketData() {
        // Para cada símbolo, gera um preço e publica.
        SYMBOLS.forEach(symbol -> {
            StockTick tick = new StockTick(symbol, generateRandomPrice(), Instant.now());
            logger.info("Generating tick: {}", tick);
            eventPublisher.publish(tick);
        });
    }

    private BigDecimal generateRandomPrice() {
        // Gera um preço aleatório entre 10.00 e 100.00
        double price = 10 + (90 * random.nextDouble());
        return new BigDecimal(price).setScale(2, RoundingMode.HALF_UP);
    }
}
