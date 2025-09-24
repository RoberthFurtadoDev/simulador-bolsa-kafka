package portfolio.monitor.application.port.out;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import portfolio.monitor.domain.model.PriceAlert;
import java.util.List;

@Repository
public interface PriceAlertRepositoryPort extends JpaRepository<PriceAlert, String> {

    List<PriceAlert> findBySymbol(String symbol);

}