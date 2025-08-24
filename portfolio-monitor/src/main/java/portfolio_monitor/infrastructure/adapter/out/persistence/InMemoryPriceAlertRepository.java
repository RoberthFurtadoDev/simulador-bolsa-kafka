package portfolio_monitor.infrastructure.adapter.out.persistence;

import org.springframework.stereotype.Repository;
import portfolio_monitor.application.port.out.PriceAlertRepositoryPort;
import portfolio_monitor.domain.model.PriceAlert;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryPriceAlertRepository implements PriceAlertRepositoryPort {

    private final ConcurrentHashMap<String, List<PriceAlert>> alertsBySymbol = new ConcurrentHashMap<>();

    @Override
    public void save(PriceAlert priceAlert) {
        alertsBySymbol.computeIfAbsent(priceAlert.getSymbol(), k -> new CopyOnWriteArrayList<>()).add(priceAlert);
    }

    @Override
    public List<PriceAlert> findBySymbol(String symbol) {
        return alertsBySymbol.getOrDefault(symbol, List.of());
    }
}
