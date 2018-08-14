package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.RouteListStatus;

import java.util.Optional;

/**
 * Created by Roman on 10.07.17.
 */
@PreAuthorize(value = "isAuthenticated()")
public interface RouteListStatusRepository extends PagingAndSortingRepository<RouteListStatus, String> {
    Optional<RouteListStatus> findByRouteListStatusId(String routeListStatusId);
}
