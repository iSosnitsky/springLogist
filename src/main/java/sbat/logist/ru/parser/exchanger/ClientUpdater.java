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
                    .map(foundClient -> fillClient(foundClient, jsonClient))
                    .orElseGet(() -> {
                        Client c = Client.builder()
                                .clientIDExternal(jsonClient.getClientId())
                                .dataSource(DATA_SOURCE)
                                .build();
                        return fillClient(c, jsonClient);
                    });
            try{
            clientRepository.save(client);
            counter.incrementAndGet();
            //SQLException
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        });

        logger.info("INSERT OR UPDATE for clients completed, affected records size = [{}]", counter.get());
    }

    private Client fillClient(Client client, JsonClient jsonClient) {
        client.setClientName(jsonClient.getClientName());
        client.setInn(jsonClient.getClientInn());
        return client;
    }
}
