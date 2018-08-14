package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import sbat.logist.ru.transport.domain.PseudoMatView;

/**
 * Created by Roman on 17.07.17.
 */
@PreAuthorize(value = "isAuthenticated()")
public interface PseudoMatViewRepository extends PagingAndSortingRepository<PseudoMatView, Integer> {
}
