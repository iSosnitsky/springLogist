package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Client;

import java.util.Optional;


@PreAuthorize(value = "isAuthenticated()")
public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
    Optional<Client> findByClientIDExternalAndDataSource(String clientIdExternal, DataSource dataSource);
    Optional<Client> findByClientIDExternal(String clientIdExternal);
}
