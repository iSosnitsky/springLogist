package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.Freight;

public interface FreightRepository extends PagingAndSortingRepository<Freight, Long> {
}
