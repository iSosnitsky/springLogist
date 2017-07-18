package sbat.logist.ru.transport.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.PseudoMatView;

/**
 * Created by Roman on 17.07.17.
 */
public interface PseudoMatViewRepository extends PagingAndSortingRepository<PseudoMatView, Integer> {
}
