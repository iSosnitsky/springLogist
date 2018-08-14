package sbat.logist.ru.transport.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.RouteList;

import java.util.Optional;


@PreAuthorize(value = "isAuthenticated()")
public interface RouteListRepository extends PagingAndSortingRepository<RouteList, Long> {
    Optional<RouteList> findByRouteListIdExternalAndAndDataSourceId(String routeListIdExternal, DataSource dataSource);
    Optional<RouteList> findByRouteListIdExternal(String routeListIdExternal);
    Optional<RouteList> findByRouteListId(Long routeListId);
}
