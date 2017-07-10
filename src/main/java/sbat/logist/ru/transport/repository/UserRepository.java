package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findUsersByUserName(String userName);
    Optional<User> findUserByLogin(String login);
    Optional<User> findByUserIDExternalAndDataSource(String requestIdExternal, DataSource dataSource);
}
