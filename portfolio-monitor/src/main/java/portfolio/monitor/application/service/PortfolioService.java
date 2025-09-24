package portfolio.monitor.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import portfolio.monitor.application.port.in.CreatePriceAlertUseCase;
import portfolio.monitor.application.port.in.MonitorStockTickUseCase;
import portfolio.monitor.application.port.out.AlertTriggeredEventPublisherPort;
import portfolio.monitor.application.port.out.PriceAlertRepositoryPort;
import portfolio.monitor.domain.model.PriceAlert;
import portfolio.monitor.domain.model.StockTick;
import portfolio.monitor.dto.AlertTriggeredEvent;

import java.time.Instant;
import java.util.List;

@Service
public class PortfolioService implements CreatePriceAlertUseCase, MonitorStockTickUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);
    private final PriceAlertRepositoryPort priceAlertRepository;
    private final AlertTriggeredEventPublisherPort alertTriggeredEventPublisher;

    public PortfolioService(PriceAlertRepositoryPort priceAlertRepository,
                            AlertTriggeredEventPublisherPort alertTriggeredEventPublisher) {
        this.priceAlertRepository = priceAlertRepository;
        this.alertTriggeredEventPublisher = alertTriggeredEventPublisher;
    }

    @Override
    @Transactional
    public void createAlert(PriceAlert priceAlert) {
        logger.info("--> Criando e salvando novo alerta: {}", priceAlert);
        priceAlertRepository.save(priceAlert);
    }

    @Override
    @Transactional
    public void monitor(StockTick stockTick) {
        List<PriceAlert> alerts = priceAlertRepository.findBySymbol(stockTick.symbol());

        for (PriceAlert alert : alerts) {
            if (stockTick.price().compareTo(alert.getTargetPrice()) >= 0) {
                logger.info("!!! ALERTA DISPARADO !!! Para o símbolo {}", alert.getSymbol());

                AlertTriggeredEvent event = new AlertTriggeredEvent(
                        alert.getId(),
                        alert.getSymbol(),
                        alert.getTargetPrice(),
                        stockTick.price(),
                        Instant.now()
                );

                logger.info("--> Publicando evento de alerta disparado: {}", event);
                alertTriggeredEventPublisher.publish(event);

                priceAlertRepository.delete(alert);
                logger.info("--> Alerta com ID {} removido do banco de dados.", alert.getId());
            }
        }
    }
}

