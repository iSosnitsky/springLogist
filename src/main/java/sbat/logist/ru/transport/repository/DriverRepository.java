package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.Driver;

import java.util.List;

@PreAuthorize(value = "isAuthenticated()")
public interface DriverRepository extends PagingAndSortingRepository<Driver, Long> {
    @PreAuthorize(value = "permitAll()")
    @Query(value = "SELECT * FROM drivers WHERE (NOW() <= TIMESTAMPADD(HOUR, 4,last_time_modified))", nativeQuery = true)
    List<Driver> getLastModified();
}
