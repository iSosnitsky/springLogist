package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.MatViewBigSelect;

import java.util.List;

public interface MatViewBigSelectRepository extends DataTablesRepository<MatViewBigSelect, Long> {
    @Procedure(name = "refreshMaterializedView")
    void refresh();

//    @PreAuthorize("isFullyAuthenticated() && (#userName == principal.username)")
//    List<MatViewBigSelect> findByClientIt(DataTablesInput input, @Param("userName")String userName);

    @Override
    DataTablesOutput<MatViewBigSelect> findAll(DataTablesInput input, Specification<MatViewBigSelect> additionalSpecification);
}
