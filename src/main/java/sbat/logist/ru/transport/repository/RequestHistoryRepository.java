package sbat.logist.ru.transport.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.RequestsHistory;

@PreAuthorize(value = "isAuthenticated()")
public interface RequestHistoryRepository extends PagingAndSortingRepository<RequestsHistory, Long> {
}
