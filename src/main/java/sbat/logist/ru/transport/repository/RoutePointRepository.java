package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import sbat.logist.ru.transport.domain.RoutePoint;

public interface RoutePointRepository extends DataTablesRepository<RoutePoint, Long> {
}
