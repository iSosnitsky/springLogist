package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    @RestResource(path = "nameStartsWith", rel = "nameStartsWith")
    List<User> findUsersByUserName(@Param("kek") String userName);
    Optional<User> findUserByLogin(String login);
    Optional<User> findByUserIDExternalAndDataSource(String requestIdExternal, DataSource dataSource);
}
