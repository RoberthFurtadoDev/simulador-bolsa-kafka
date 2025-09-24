package br.com.service.notification_service.consumer;

import br.com.service.notification_service.dto.AlertTriggeredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(NotificationConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic.input}")
    public void handleNotification(AlertTriggeredEvent event) {
        logger.info("================ NOTIFICAÇÃO RECEBIDA ================");
        logger.info("Iniciando processo de notificação para o alerta ID: {}", event.alertId());

        // Simulação de lógica de notificação
        try {
            // Aqui você poderia ter a lógica para escolher o canal (E-mail, SMS, Push)
            // e chamar um serviço externo.
            Thread.sleep(1000); // Simula o tempo de envio
            logger.info("E-mail simulado enviado para o usuário sobre o ativo {}", event.symbol());
            logger.info("Detalhes: Preço Alvo R${}, Preço Atingido R${}", event.targetPrice(), event.triggeredPrice());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Erro ao simular envio de notificação", e);
        }

        logger.info("================ FIM DA NOTIFICAÇÃO ================");
    }
}
