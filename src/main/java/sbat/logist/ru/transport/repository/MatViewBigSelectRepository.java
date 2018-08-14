package sbat.logist.ru.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.MatViewBigSelect;

@PreAuthorize(value = "isAuthenticated()")
public interface MatViewBigSelectRepository extends JpaRepository<MatViewBigSelect, Long> {
    @Procedure(name = "refreshMaterializedView")
    void refresh();
}
