package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.Vehicle;

import java.util.List;
import java.util.Optional;

@PreAuthorize(value = "isAuthenticated()")
public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleIdExternalAndDataSource(String vehicleIdExternal, DataSource dataSource);

    @PreAuthorize(value = "permitAll()")
    @Query(value = "SELECT * FROM vehicles WHERE (NOW() <= TIMESTAMPADD(HOUR, 4,last_time_modified))", nativeQuery = true)
    List<Vehicle> getLastModified();
}
