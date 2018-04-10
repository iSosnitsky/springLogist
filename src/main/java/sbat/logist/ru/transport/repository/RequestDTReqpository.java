package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import sbat.logist.ru.transport.domain.Request;

public interface RequestDTReqpository extends DataTablesRepository<Request, Integer> {
}
