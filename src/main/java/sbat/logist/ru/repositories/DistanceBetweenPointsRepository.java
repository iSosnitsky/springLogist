package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.DistanceBetweenPoints;
import sbat.logist.ru.model.DistanceBetweenPointsId;

public interface DistanceBetweenPointsRepository extends PagingAndSortingRepository<DistanceBetweenPoints, DistanceBetweenPointsId> {
}
