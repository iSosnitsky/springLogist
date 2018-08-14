package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.ExchangeLog;

@PreAuthorize(value = "isAuthenticated()")
public interface ExchangeLogRepository extends PagingAndSortingRepository<ExchangeLog, Long> {
}
