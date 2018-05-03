package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import sbat.logist.ru.transport.domain.User;

public interface UserDTRepository extends DataTablesRepository<User, Long> {
    DataTablesOutput<User> findAll(DataTablesInput input);
    DataTablesOutput<User> findAll(DataTablesInput input, Specification<User> specification);
}
