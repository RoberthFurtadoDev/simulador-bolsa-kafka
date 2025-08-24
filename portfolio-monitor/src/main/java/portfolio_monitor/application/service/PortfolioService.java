package portfolio_monitor.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import portfolio_monitor.application.port.in.CreatePriceAlertUseCase;
import portfolio_monitor.application.port.in.MonitorStockTickUseCase;
import portfolio_monitor.application.port.out.PriceAlertRepositoryPort;
import portfolio_monitor.domain.model.PriceAlert;
import portfolio_monitor.domain.model.StockTick;

import java.util.List;

@Service
public class PortfolioService implements CreatePriceAlertUseCase, MonitorStockTickUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PortfolioService.class);
    private final PriceAlertRepositoryPort priceAlertRepository;

    public PortfolioService(PriceAlertRepositoryPort priceAlertRepository) {
        this.priceAlertRepository = priceAlertRepository;
    }

    @Override
    public void createAlert(PriceAlert priceAlert) {
        logger.info("Creating new alert: {}", priceAlert);
        priceAlertRepository.save(priceAlert);
    }

    @Override
    public void monitor(StockTick stockTick) {
        List<PriceAlert> alerts = priceAlertRepository.findBySymbol(stockTick.symbol());
        if (alerts.isEmpty()) {
            return;
        }

        logger.info("Checking {} alert(s) for symbol {}", alerts.size(), stockTick.symbol());

        alerts.forEach(alert -> {
            if (stockTick.price().compareTo(alert.getTargetPrice()) >= 0) {
                logger.warn("!!! ALERT TRIGGERED !!! Symbol: {}, Current Price: {}, Target Price: {}",
                        stockTick.symbol(), stockTick.price(), alert.getTargetPrice());
            }
        });
    }
}
