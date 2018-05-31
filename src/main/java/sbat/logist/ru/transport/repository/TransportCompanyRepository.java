package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import sbat.logist.ru.transport.domain.TransportCompany;

import java.util.List;

public interface TransportCompanyRepository extends DataTablesRepository<TransportCompany, Long> {
    List<TransportCompany> findTop10ByNameContaining(@Param("companyName") String companyName);


    @Override
    DataTablesOutput<TransportCompany> findAll(DataTablesInput input);
}
