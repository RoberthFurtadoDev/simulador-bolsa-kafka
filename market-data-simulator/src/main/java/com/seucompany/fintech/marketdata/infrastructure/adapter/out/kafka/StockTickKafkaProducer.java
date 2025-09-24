package com.seucompany.fintech.marketdata.infrastructure.adapter.out.kafka;

import com.seucompany.fintech.marketdata.application.port.out.StockTickEventPublisher;
import com.seucompany.fintech.marketdata.domain.StockTick;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Adapter que implementa a port de publicação de eventos usando Apache Kafka.
 * Esta classe é a ponte entre nosso domínio e a tecnologia de mensageria.
 */
@Component // Anotado como um componente Spring para ser gerenciado pelo container
public class StockTickKafkaProducer implements StockTickEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(StockTickKafkaProducer.class);

    private final KafkaTemplate<String, StockTick> kafkaTemplate;
    private final String topicName;

    // Injeção de dependências via construtor (melhor prática)
    public StockTickKafkaProducer(KafkaTemplate<String, StockTick> kafkaTemplate,
                                  @Value("${app.kafka.topic.market-data}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    public void publish(StockTick stockTick) {
        // A chave da mensagem (stockTick.symbol()) é crucial!
        // O Kafka garante que todas as mensagens com a mesma chave irão para a mesma partição.
        // Isso garante a ORDEM de processamento para uma mesma ação.
        String key = stockTick.symbol();

        kafkaTemplate.send(topicName, key, stockTick)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        logger.info("Sent message=[{}] with offset=[{}]", stockTick, result.getRecordMetadata().offset());
                    } else {
                        logger.error("Unable to send message=[{}] due to : {}", stockTick, ex.getMessage());
                    }
                });
    }
}
