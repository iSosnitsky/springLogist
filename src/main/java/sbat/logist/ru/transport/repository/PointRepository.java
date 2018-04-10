package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.PointType;
import sbat.logist.ru.transport.domain.Point;

import java.util.List;
import java.util.Optional;


public interface PointRepository extends PagingAndSortingRepository<Point, Long> {
    Optional<Point> findByPointIdExternalAndDataSource(String pointIdExternal, DataSource dataSource);
    List<Point> findTop10ByPointNameContainingAndPointTypeId(@Param("pointName") String pointName, @Param("pointType")PointType pointType);
}
