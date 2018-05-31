package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends DataTablesRepository<Client, Long> {
    Optional<Client> findByClientIDExternalAndDataSource(String clientIdExternal, DataSource dataSource);
    Optional<Client> findByClientIDExternal(@Param("clientIdExternal") String clientIdExternal);
    List<Client> findTop15ByClientNameContaining(@Param("name") String name);

    @Override
    DataTablesOutput<Client> findAll(DataTablesInput input);
}
