package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.Client;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {
}
