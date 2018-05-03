package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.Driver;

public interface DriverRepository extends PagingAndSortingRepository<Driver, Long> {

}
