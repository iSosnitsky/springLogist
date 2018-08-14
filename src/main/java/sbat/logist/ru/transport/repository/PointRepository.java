package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Point;

import java.util.Optional;


@PreAuthorize(value = "isAuthenticated()")
public interface PointRepository extends PagingAndSortingRepository<Point, Long> {
    Optional<Point> findByPointIdExternalAndDataSource(String pointIdExternal, DataSource dataSource);
}
