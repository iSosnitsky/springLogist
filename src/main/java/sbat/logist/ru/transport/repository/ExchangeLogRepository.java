package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import sbat.logist.ru.transport.domain.ExchangeLog;

import java.util.List;

@RepositoryRestResource
public interface ExchangeLogRepository extends DataTablesRepository<ExchangeLog, Long> {

    @Override
    DataTablesOutput<ExchangeLog> findAll(DataTablesInput input);
}
