package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
}
