package portfolio_monitor.domain.model;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Representa um alerta de preço configurado por um usuário.
 */
@Data // Lombok para gerar getters, setters, toString, etc.
public class PriceAlert {
    private String id = UUID.randomUUID().toString();
    private String symbol;
    private BigDecimal targetPrice;
    // Em um sistema real, teríamos userId, tipo de alerta (acima/abaixo), etc.
}
