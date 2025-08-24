package portfolio_monitor.application.port.in;

import portfolio_monitor.domain.model.PriceAlert;

public interface CreatePriceAlertUseCase {
    void createAlert(PriceAlert priceAlert);
}