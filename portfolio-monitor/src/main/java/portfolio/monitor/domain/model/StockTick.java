package portfolio.monitor.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

// Exatamente o mesmo Record do outro serviço.
public record StockTick(
        String symbol,
        BigDecimal price,
        Instant timestamp
) {
}
