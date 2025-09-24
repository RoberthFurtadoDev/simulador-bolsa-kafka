package com.seucompany.fintech.marketdata.application.port.out;

import com.seucompany.fintech.marketdata.domain.StockTick;

/**
 * Port (Interface) que define o contrato para publicar eventos de cotação de ações.
 * A camada de aplicação depende desta abstração, não da implementação concreta (Kafka).
 * Isso segue o Princípio da Inversão de Dependência (SOLID).
 */
public interface StockTickEventPublisher {
    void publish(StockTick stockTick);
}
