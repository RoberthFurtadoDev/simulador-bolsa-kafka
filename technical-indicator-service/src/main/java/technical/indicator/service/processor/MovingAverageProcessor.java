package technical.indicator.service.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;
import technical.indicator.service.dto.MovingAverage; // Import corrigido
import technical.indicator.service.dto.StockTick;    // Import corrigido

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

@Component
public class MovingAverageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MovingAverageProcessor.class);
    private static final long WINDOW_SIZE_SECONDS = 30;

    @Value("${app.kafka.topic.input}")
    private String inputTopic;

    @Value("${app.kafka.topic.output}")
    private String outputTopic;

    private final Serde<StockTick> stockTickSerde;
    private final Serde<MovingAverage> movingAverageSerde;

    public MovingAverageProcessor(ObjectMapper objectMapper) {
        this.stockTickSerde = new JsonSerde<>(StockTick.class, objectMapper);
        this.movingAverageSerde = new JsonSerde<>(MovingAverage.class, objectMapper);
    }

    @Autowired
    public void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, StockTick> messageStream = streamsBuilder
                .stream(inputTopic, Consumed.with(Serdes.String(), stockTickSerde));

        messageStream
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(WINDOW_SIZE_SECONDS)))
                .aggregate(
                        MovingAverageAggregator::new,
                        this::aggregator,
                        Materialized.with(Serdes.String(), new JsonSerde<>(MovingAverageAggregator.class))
                )
                .toStream()
                .mapValues(this::calculateAverage)
                .peek((key, value) -> logger.info("Calculated Moving Average: {}", value))
                .to(outputTopic, Produced.with(WindowedSerdes.timeWindowedSerdeFrom(String.class), movingAverageSerde));
    }

    private MovingAverageAggregator aggregator(String key, StockTick value, MovingAverageAggregator aggregate) {
        aggregate.setSum(aggregate.getSum().add(value.price()));
        aggregate.setCount(aggregate.getCount() + 1);
        aggregate.setSymbol(key);
        return aggregate;
    }

    private MovingAverage calculateAverage(MovingAverageAggregator aggregator) {
        if (aggregator.getCount() == 0) {
            return new MovingAverage(aggregator.getSymbol(), BigDecimal.ZERO, Instant.now(), WINDOW_SIZE_SECONDS);
        }
        BigDecimal average = aggregator.getSum().divide(BigDecimal.valueOf(aggregator.getCount()), 2, RoundingMode.HALF_UP);
        return new MovingAverage(aggregator.getSymbol(), average, Instant.now(), WINDOW_SIZE_SECONDS);
    }

    public static class MovingAverageAggregator {
        private String symbol = "";
        private BigDecimal sum = BigDecimal.ZERO;
        private int count = 0;

        // Getters e Setters
        public String getSymbol() { return symbol; }
        public void setSymbol(String symbol) { this.symbol = symbol; }
        public BigDecimal getSum() { return sum; }
        public void setSum(BigDecimal sum) { this.sum = sum; }
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
