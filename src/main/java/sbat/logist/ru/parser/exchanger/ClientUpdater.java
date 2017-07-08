package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonClient;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.repository.ClientRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class ClientUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private final ClientRepository clientRepository;


    public ClientUpdater(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(List<JsonClient> jsonClients) {
        final AtomicInteger counter = new AtomicInteger(0);
        logger.info("START update jsonClients table from JSON object:[updateClientsArray]");
        jsonClients.forEach(jsonClient -> {
            final Client client = clientRepository.findByClientIDExternalAndDataSource(jsonClient.getClientId(), DATA_SOURCE)
                    .map(foundClient -> mapJson(jsonClient))
                    .orElseGet(() -> mapJson(jsonClient));
            clientRepository.save(client);
            counter.incrementAndGet();
        });

    }

    private Client mapJson(JsonClient jsonClient) {
        return Client.builder()
                .clientIDExternal(jsonClient.getClientId())
                .dataSource(DATA_SOURCE)
                .clientName(jsonClient.getClientName())
                .inn(jsonClient.getClientInn())
                .build();
    }
}
