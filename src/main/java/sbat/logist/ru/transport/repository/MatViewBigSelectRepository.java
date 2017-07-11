package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import sbat.logist.ru.transport.domain.MatViewBigSelect;

public interface MatViewBigSelectRepository extends JpaRepository<MatViewBigSelect, Long> {
    @Procedure(name = "refreshMaterializedView")
    void refresh();
}
