package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.transport.domain.TransportCompany;

import java.util.List;

public interface TransportCompanyRepository extends PagingAndSortingRepository<TransportCompany, Long> {
    List<TransportCompany> findTop10ByNameContaining(@Param("companyName") String companyName);
}
