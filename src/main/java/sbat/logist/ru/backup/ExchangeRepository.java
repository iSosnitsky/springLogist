package sbat.logist.ru.backup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExchangeRepository extends JpaRepository<Exchange, ExchangeId> {
    @Query("SELECT new sbat.logist.ru.backup.ExchangeId(e.exchangeId.serverName, max(e.exchangeId.packageNumber)) FROM Exchange e group by e.exchangeId.serverName")
    List<ExchangeId> findMaxExchangedIdGroupedByServerName();
}
