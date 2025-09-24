package portfolio.monitor.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AlertTriggeredEvent(
        String alertId,
        String symbol,
        BigDecimal targetPrice,
        BigDecimal triggeredPrice,
        Instant timestamp
) {
}
