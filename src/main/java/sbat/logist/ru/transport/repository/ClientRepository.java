package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Client;

import java.util.Optional;


public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
    Optional<Client> findByClientIDExternalAndDataSource(String clientIdExternal, DataSource dataSource);
    Optional<Client> findByClientIDExternal(String clientIdExternal);
}
