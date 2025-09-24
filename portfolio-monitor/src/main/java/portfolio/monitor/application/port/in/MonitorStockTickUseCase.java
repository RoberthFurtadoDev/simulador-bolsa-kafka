package portfolio.monitor.application.port.in;

import portfolio.monitor.domain.model.StockTick;

public interface MonitorStockTickUseCase {

    void monitor(StockTick stockTick);

}
