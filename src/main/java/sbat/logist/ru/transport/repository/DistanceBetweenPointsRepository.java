package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.DistanceBetweenPoints;
import sbat.logist.ru.transport.domain.DistanceBetweenPointsId;

public interface DistanceBetweenPointsRepository extends PagingAndSortingRepository<DistanceBetweenPoints, DistanceBetweenPointsId> {
}
