package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Client;

import java.util.List;
import java.util.Optional;


public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
    Optional<Client> findByClientIDExternalAndDataSource(String clientIdExternal, DataSource dataSource);
    Optional<Client> findByClientIDExternal(@Param("clientIdExternal") String clientIdExternal);
    List<Client> findByInnContainingOrClientNameContaining(@Param("inn") String inn, @Param("name") String name);

}
