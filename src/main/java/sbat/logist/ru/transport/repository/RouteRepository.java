package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Route;

import java.util.List;
import java.util.Optional;


public interface RouteRepository extends DataTablesRepository<Route, Integer> {
    Optional<Route> findByExternalIdAndRouteName(String externalId, String routeName);
    Optional<Route> findByExternalIdAndDataSource(String externalId, DataSource dataSource);
    Optional<Route> findByRouteName(String routeName);
    List<Route> findByRouteNameContaining(@Param("routeName") String routeName);

    List<Route> findTop20ByRouteNameContaining(@Param("routeName") String routeName);

    @Override
    DataTablesOutput<Route> findAll(DataTablesInput input);
}
