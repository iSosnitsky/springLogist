package sbat.logist.ru.transport.repository;



import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.RouteList;

import java.util.List;
import java.util.Optional;


public interface RouteListRepository extends DataTablesRepository<RouteList, Integer> {
    List<RouteList> findTop5ByRouteListNumberContaining(@Param("routeListNumber") String routeListNumber);
    Optional<RouteList> findByRouteListIdExternalAndAndDataSourceId(String routeListIdExternal, DataSource dataSource);
    Optional<RouteList> findByRouteListIdExternal(String routeListIdExternal);
    Optional<RouteList> findByRouteListId(Integer routeListId);
}
