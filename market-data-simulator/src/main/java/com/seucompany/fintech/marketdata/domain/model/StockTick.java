package com.seucompany.fintech.marketdata.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Representa um "tick" do mercado, ou seja, a cotação de uma ação em um dado momento.
 * Este é um Record do Java para imutabilidade, uma boa prática para objetos de domínio.
 *
 * @param symbol O código da ação (ex: PETR4, VALE3).
 * @param price O preço da ação no momento do tick.
 * @param timestamp O momento exato em que a cotação foi gerada.
 */
public record StockTick(
        String symbol,
        BigDecimal price,
        Instant timestamp
) {
}
