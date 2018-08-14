package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.DistanceBetweenPoints;
import sbat.logist.ru.transport.domain.DistanceBetweenPointsId;

@PreAuthorize(value = "isAuthenticated()")
public interface DistanceBetweenPointsRepository extends PagingAndSortingRepository<DistanceBetweenPoints, DistanceBetweenPointsId> {
}
