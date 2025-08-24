package portfolio_monitor.application.port.out;

import portfolio_monitor.domain.model.PriceAlert;
import java.util.List;

public interface PriceAlertRepositoryPort {
    void save(PriceAlert priceAlert);
    List<PriceAlert> findBySymbol(String symbol);
}
