package portfolio_monitor.infrastructure.adapter.in.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import portfolio_monitor.application.port.in.MonitorStockTickUseCase;
import portfolio_monitor.domain.model.StockTick;

@Component
public class StockTickKafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(StockTickKafkaConsumer.class);
    private final MonitorStockTickUseCase monitorStockTickUseCase;

    public StockTickKafkaConsumer(MonitorStockTickUseCase monitorStockTickUseCase) {
        this.monitorStockTickUseCase = monitorStockTickUseCase;
    }

    @KafkaListener(topics = "${app.kafka.topic.market-data}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(StockTick stockTick) {
        logger.info("Received stock tick: {}", stockTick);
        monitorStockTickUseCase.monitor(stockTick);
    }
}
