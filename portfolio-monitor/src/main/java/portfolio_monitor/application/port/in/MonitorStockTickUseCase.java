package portfolio_monitor.application.port.in;

import portfolio_monitor.domain.model.StockTick;

public interface MonitorStockTickUseCase {
    void monitor(StockTick stockTick);
}