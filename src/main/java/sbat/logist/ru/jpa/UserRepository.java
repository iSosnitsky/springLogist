package sbat.logist.ru.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findUsersByUserName(String userName);
    Optional<User> findUserByLogin(String login);
}
