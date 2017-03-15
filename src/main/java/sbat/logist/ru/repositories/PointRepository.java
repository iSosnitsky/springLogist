package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.Point;

public interface PointRepository extends PagingAndSortingRepository<Point, Long> {
}
