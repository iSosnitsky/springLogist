package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.repository.query.Param;
import sbat.logist.ru.transport.domain.Tariff;

import java.util.List;

public interface TariffRepoisitory extends DataTablesRepository<Tariff, Long> {
    public List<Tariff> findTop10ByTariffNameContaining(@Param("tariffName") String tariffName);

    @Override
    DataTablesOutput<Tariff> findAll(DataTablesInput input);
}

