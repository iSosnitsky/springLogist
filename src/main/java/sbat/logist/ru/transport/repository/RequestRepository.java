package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RouteList;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends PagingAndSortingRepository<Request, Integer> {
    Optional<Request> findByExternalIdAndDataSource(String externalId, DataSource dataSource);
    Optional<Request> findByExternalId(String externalId);
    List<Request> findByRouteListId(RouteList routeList);
}
