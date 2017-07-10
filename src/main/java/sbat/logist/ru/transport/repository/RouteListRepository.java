package sbat.logist.ru.transport.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.RouteList;

public interface RouteListRepository extends PagingAndSortingRepository<RouteList, Long> {
}
