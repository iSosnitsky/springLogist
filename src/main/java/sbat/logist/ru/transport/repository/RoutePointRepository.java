package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import sbat.logist.ru.transport.domain.RoutePoint;

public interface RoutePointRepository extends DataTablesRepository<RoutePoint, Long> {

    @Override
    DataTablesOutput<RoutePoint> findAll(DataTablesInput input);
}
