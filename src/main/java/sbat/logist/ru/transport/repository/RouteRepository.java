package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Route;

import java.util.Optional;


public interface RouteRepository extends PagingAndSortingRepository<Route, Integer> {
    Optional<Route> findByExternalIdAndRouteName(String externalId, String routeName);
    Optional<Route> findByExternalIdAndDataSource(String externalId, DataSource dataSource);
    Optional<Route> findByRouteName(String routeName);
}
