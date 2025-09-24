package portfolio.monitor.application.port.in;

import portfolio.monitor.domain.model.PriceAlert;

public interface CreatePriceAlertUseCase {

    void createAlert(PriceAlert priceAlert);

}
