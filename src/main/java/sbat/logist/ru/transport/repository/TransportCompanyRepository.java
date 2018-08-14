package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.TransportCompany;

import java.util.List;

@PreAuthorize(value = "isAuthenticated()")
public interface TransportCompanyRepository extends PagingAndSortingRepository<TransportCompany, Long> {
    @PreAuthorize(value = "permitAll()")
    @Query(value = "SELECT * FROM transport_companies WHERE (NOW() <= TIMESTAMPADD(HOUR, 4,last_time_modified))", nativeQuery = true)
    List<TransportCompany> getLastModified();
}
