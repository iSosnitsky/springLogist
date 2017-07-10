package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Request;

import java.util.Optional;

public interface RequestRepository extends PagingAndSortingRepository<Request, Integer> {
    Optional<Request> findByExternalIdAndDataSource(String externalId, DataSource dataSource);
    Optional<Request> findByExternalId(String externalId);
    Optional<Request> findById(Integer id);
}
