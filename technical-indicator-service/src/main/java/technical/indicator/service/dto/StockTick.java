package technical.indicator.service.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record StockTick(
        String symbol,
        BigDecimal price,
        Instant timestamp
) {
}
