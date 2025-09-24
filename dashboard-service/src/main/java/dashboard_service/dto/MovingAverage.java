package dashboard_service.dto;
import java.math.BigDecimal;
import java.time.Instant;
public record MovingAverage(String symbol, BigDecimal average, Instant timestamp, long windowSizeInSeconds) {}
