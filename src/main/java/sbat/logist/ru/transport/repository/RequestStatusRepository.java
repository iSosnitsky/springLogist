package sbat.logist.ru.transport.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import sbat.logist.ru.transport.domain.RequestStatus;

import java.util.Optional;


public interface RequestStatusRepository extends PagingAndSortingRepository<RequestStatus,String> {
    Optional<RequestStatus> findByRequestStatusId(String statusId);
}
