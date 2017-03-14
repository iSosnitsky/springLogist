package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
