package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.Driver;

public interface DriverRepository extends DataTablesRepository<Driver, Long> {
    @Override
    DataTablesOutput<Driver> findAll(DataTablesInput input);
}
