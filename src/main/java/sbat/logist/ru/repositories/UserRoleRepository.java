package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.UserRole;

public interface UserRoleRepository extends PagingAndSortingRepository<UserRole, String> {
}
