package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;
import sbat.logist.ru.parser.json.JsonRequest;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.repository.ClientRepository;
import sbat.logist.ru.transport.repository.PointRepository;
import sbat.logist.ru.transport.repository.RequestRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RequestUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PointRepository pointRepository;

    @Autowired
    public RequestUpdater(RequestRepository requestRepository, UserRepository userRepository, ClientRepository clientRepository, PointRepository pointRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.pointRepository = pointRepository;
    }

    public void execute(List<JsonRequest> requests) {
        logger.info("START update requests table from JSON object:[updateRequestsTable]");
        AtomicInteger counter = new AtomicInteger(0);
        requests.forEach(jsonRequest -> {
            try {
                Request foundRequest = requestRepository.findByExternalIdAndDataSource(jsonRequest.getRequestId(), DATA_SOURCE)
                        .map(request -> updateRequest(request, jsonRequest))
                        .orElseGet(() -> mapJsonToRequest(jsonRequest));
                requestRepository.save(foundRequest);
                counter.incrementAndGet();
            } catch (IllegalStateException e) {
                logger.warn("failed to update/insert request {}", e.getMessage());
            }
        });
        logger.info("INSERT OR UPDATE INTO requests completed, affected records size = [{}]", counter.get());
    }

    private Request updateRequest(Request request, JsonRequest jsonRequest) {

        request.setClientId(clientRepository.findByClientIDExternalAndDataSource(jsonRequest.getClientId(), DATA_SOURCE)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to update request : %s has %s = %s that is not contained in %s table.", jsonRequest.getRequestId(), "clientId", jsonRequest.getClientId(), "users"))));
        request.setRequestNumber(jsonRequest.getRequestNumber());
        request.setRequestDate(jsonRequest.getRequestDate());
        request.setInvoiceNumber(jsonRequest.getInvoiceNumber());
        request.setInvoiceDate(jsonRequest.getInvoiceDate());
        request.setDocumentNumber(jsonRequest.getDocumentNumber());
        request.setDocumentDate(jsonRequest.getDocumentDate());
        request.setFirma(jsonRequest.getFirma());
        request.setStorage(jsonRequest.getStorage());
        request.setDestinationPointId(pointRepository.findByPointIdExternalAndDataSource(jsonRequest.getAddressId(), DATA_SOURCE).orElse(null));
        request.setContactName(jsonRequest.getContactName());
        request.setContactPhone(jsonRequest.getContactPhone());
        request.setDeliveryOption(jsonRequest.getDeliveryOption());
        request.setMarketAgentUserId(userRepository.findByUserIDExternalAndDataSource(jsonRequest.getTraderId(), DATA_SOURCE)
                .orElseThrow(() -> new IllegalStateException(String.format("Failed to update request : %s has %s = %s that is not contained in %s table.", jsonRequest.getRequestId(), "marketAgentUserId", jsonRequest.getTraderId(), "users"))));
        request.setDeliveryDate(jsonRequest.getDeliveryDate());
        return request;
    }

    private Request mapJsonToRequest(JsonRequest jsonRequest) {
        return Request.builder()
                .dataSource(DATA_SOURCE)
                .externalId(jsonRequest.getRequestId())
                .clientId(clientRepository.findByClientIDExternalAndDataSource(jsonRequest.getClientId(), DATA_SOURCE)
                        .orElseThrow(() -> new IllegalStateException(String.format("Failed to create request : %s has %s = %s that is not contained in %s table.", jsonRequest.getRequestId(), "clientId", jsonRequest.getClientId(), "users"))))
                .requestNumber(jsonRequest.getRequestNumber())
                .requestDate(jsonRequest.getRequestDate())
                .invoiceNumber(jsonRequest.getInvoiceNumber())
                .invoiceDate(jsonRequest.getInvoiceDate())
                .documentNumber(jsonRequest.getDocumentNumber())
                .documentDate(jsonRequest.getDocumentDate())
                .firma(jsonRequest.getFirma())
                .storage(jsonRequest.getStorage())
                .destinationPointId(pointRepository.findByPointIdExternalAndDataSource(jsonRequest.getAddressId(), DATA_SOURCE)
                        .orElse(null))
                .contactName(jsonRequest.getContactName())
                .contactPhone(jsonRequest.getContactPhone())
                .deliveryOption(jsonRequest.getDeliveryOption())
                .marketAgentUserId(userRepository.findByUserIDExternalAndDataSource(jsonRequest.getTraderId(), DATA_SOURCE)
                        .orElseThrow(() -> new IllegalStateException(String.format("Failed to create request : %s has %s = %s that is not contained in %s table.", jsonRequest.getRequestId(), "marketAgentUserId", jsonRequest.getTraderId(), "users"))))
                .deliveryDate(jsonRequest.getDeliveryDate())
                .lastStatusUpdated(new java.sql.Date(Calendar.getInstance().getTime().getTime()))
                .requestStatusId(RequestStatus.CREATED)
                .commentForStatus("Заявка добавлена из 1С")
                .lastModifiedBy(userRepository.findOne(Long.parseLong("1")))
                .build();
    }

}
