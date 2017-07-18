package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonStatus;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RequestStatus;
import sbat.logist.ru.transport.repository.RequestRepository;
import sbat.logist.ru.transport.repository.RequestStatusRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestStatusUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;

    private final RequestRepository requestRepository;
    private final RequestStatusRepository requestStatusRepository;

    @Autowired
    public RequestStatusUpdater(RequestRepository requestRepository, RequestStatusRepository requestStatusRepository) {
        this.requestRepository = requestRepository;
        this.requestStatusRepository = requestStatusRepository;
    }

    public void execute(List<JsonStatus> jsonStatuses){
        logger.info("START assign statuses in requests table from JSON object:[updateStatus]");
        AtomicInteger counter = new AtomicInteger(0);
        jsonStatuses.forEach(jsonStatus -> {
            try{
                final Request request = requestRepository.findByExternalIdAndDataSource(jsonStatus.getRequestId(),DATA_SOURCE).orElseThrow(IllegalStateException::new);
                final RequestStatus requestStatus = requestStatusRepository.findByRequestStatusId(jsonStatus.getStatus()).orElseThrow(IllegalStateException::new);
                request.setRequestStatusId(requestStatus);
                request.setCommentForStatus(jsonStatus.getComment());
                request.setLastStatusUpdated(jsonStatus.getTimeOutStatus());
                request.setBoxQuantity(jsonStatus.getNumBoxes());
                try{
                    requestRepository.save(request);
                    counter.incrementAndGet();
                } catch (Exception e){
                    logger.error("unable to save request[{}]",request.getExternalId());
                    logger.error(request.toString());
                }

            } catch (IllegalStateException e){
                logger.warn("Unable to update request status [{}], because it's not present in table [requests], or because there is no such status as [{}]",jsonStatus.getRequestId(),jsonStatus.getStatus());
            }
        });
        logger.info("ASSIGN statuses in requests completed, affected records size = [{}]", counter.get());
    }

}
