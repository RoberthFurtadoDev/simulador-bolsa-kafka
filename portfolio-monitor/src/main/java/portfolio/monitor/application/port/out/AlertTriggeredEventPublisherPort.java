package portfolio.monitor.application.port.out;

import portfolio.monitor.dto.AlertTriggeredEvent;

public interface AlertTriggeredEventPublisherPort {
    void publish(AlertTriggeredEvent event);
}
