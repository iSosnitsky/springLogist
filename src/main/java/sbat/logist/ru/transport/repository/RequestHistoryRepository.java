package sbat.logist.ru.transport.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.RequestsHistory;

public interface RequestHistoryRepository extends PagingAndSortingRepository<RequestsHistory, Long> {
}
