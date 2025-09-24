package dashboard_service.consumer;

import dashboard_service.dto.AlertTriggeredEvent;
import dashboard_service.dto.MovingAverage;
import dashboard_service.dto.StockTick;
import dashboard_service.dto.WebSocketMessage;
import dashboard_service.websocket.DashboardWebSocketHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final DashboardWebSocketHandler webSocketHandler;

    public KafkaConsumer(DashboardWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @KafkaListener(topics = "${app.kafka.topics.ticks}")
    public void listenTicks(StockTick tick) {
        webSocketHandler.broadcast(new WebSocketMessage("TICK", tick));
    }

    @KafkaListener(topics = "${app.kafka.topics.indicators}")
    public void listenIndicators(MovingAverage indicator) {
        webSocketHandler.broadcast(new WebSocketMessage("INDICATOR", indicator));
    }

    @KafkaListener(topics = "${app.kafka.topics.alerts}")
    public void listenAlerts(AlertTriggeredEvent alert) {
        webSocketHandler.broadcast(new WebSocketMessage("ALERT", alert));
    }
}
