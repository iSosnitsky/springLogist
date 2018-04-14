package sbat.logist.ru.transport.repository;



import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.RouteList;
import java.util.Optional;


public interface RouteListRepository extends DataTablesRepository<RouteList, Long> {
    Optional<RouteList> findByRouteListIdExternalAndAndDataSourceId(String routeListIdExternal, DataSource dataSource);
    Optional<RouteList> findByRouteListIdExternal(String routeListIdExternal);
    Optional<RouteList> findByRouteListId(Long routeListId);
}
