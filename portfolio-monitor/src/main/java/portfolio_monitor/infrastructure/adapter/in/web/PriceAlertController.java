package portfolio_monitor.infrastructure.adapter.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio_monitor.application.port.in.CreatePriceAlertUseCase;
import portfolio_monitor.domain.model.PriceAlert;

@RestController
@RequestMapping("/alerts")
public class PriceAlertController {

    private final CreatePriceAlertUseCase createPriceAlertUseCase;

    public PriceAlertController(CreatePriceAlertUseCase createPriceAlertUseCase) {
        this.createPriceAlertUseCase = createPriceAlertUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> createAlert(@RequestBody PriceAlert priceAlert) {
        createPriceAlertUseCase.createAlert(priceAlert);
        return ResponseEntity.accepted().build();
    }
}
