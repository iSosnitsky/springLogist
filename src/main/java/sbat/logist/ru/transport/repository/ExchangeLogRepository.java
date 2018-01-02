package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.ExchangeLog;

public interface ExchangeLogRepository extends PagingAndSortingRepository<ExchangeLog, Long> {
}
