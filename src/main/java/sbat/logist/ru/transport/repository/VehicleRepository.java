package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.domain.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends PagingAndSortingRepository<Vehicle, Long> {
    Optional<Vehicle> findByVehicleIdExternalAndDataSource(String vehicleIdExternal, DataSource dataSource);
}
