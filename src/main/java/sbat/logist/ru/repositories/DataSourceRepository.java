package sbat.logist.ru.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.model.DataSource;

public interface DataSourceRepository extends PagingAndSortingRepository<DataSource, String> {
}
