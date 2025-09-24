package portfolio.monitor.infrastructure.adapter.out.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import portfolio.monitor.application.port.out.AlertTriggeredEventPublisherPort;
import portfolio.monitor.dto.AlertTriggeredEvent;

@Component
public class AlertTriggeredKafkaProducer implements AlertTriggeredEventPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(AlertTriggeredKafkaProducer.class);

    private final KafkaTemplate<String, AlertTriggeredEvent> kafkaTemplate;
    private final String topic;

    public AlertTriggeredKafkaProducer(
            KafkaTemplate<String, AlertTriggeredEvent> kafkaTemplate,
            @Value("${app.kafka.topic.output.alerts-triggered}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(AlertTriggeredEvent event) {
        try {
            // A chave do evento será o símbolo da ação
            String key = event.symbol();
            kafkaTemplate.send(topic, key, event);
            logger.info("Alert triggered event published for symbol {}: {}", key, event);
        } catch (Exception e) {
            logger.error("Failed to publish alert triggered event", e);
        }
    }
}
