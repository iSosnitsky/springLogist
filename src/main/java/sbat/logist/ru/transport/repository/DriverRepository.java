package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.transport.domain.Driver;
import sbat.logist.ru.transport.domain.TransportCompany;

import javax.validation.Valid;
import java.util.List;

public interface DriverRepository extends DataTablesRepository<Driver, Long> {
    @Override
    DataTablesOutput<Driver> findAll(DataTablesInput input);

    List<Driver> findTop10ByTransportCompany(@Param("transportCompany") @Valid TransportCompany transportCompany);
}
