package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.RouteListHistory;

import javax.persistence.Table;

public interface RouteListHistoryRepoistory extends PagingAndSortingRepository<RouteListHistory, Long> {

}
