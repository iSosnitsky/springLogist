package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import sbat.logist.ru.transport.domain.RouteList;

public interface RouteListDTRepository extends DataTablesRepository<RouteList, Integer> {
}
