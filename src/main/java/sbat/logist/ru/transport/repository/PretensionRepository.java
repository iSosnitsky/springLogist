package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.transport.domain.Pretension;

import java.util.List;
import java.util.Optional;

public interface PretensionRepository extends PagingAndSortingRepository<Pretension, Integer> {
    List<Pretension> findByRequestIdExternal(@Param("requestIdExternal") String requestIdExternal);
}
