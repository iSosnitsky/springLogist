package sbat.logist.ru.parser.exchanger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.backup.Exchange;
import sbat.logist.ru.backup.ExchangeId;
import sbat.logist.ru.backup.ExchangeRepository;
import sbat.logist.ru.parser.json.DataFrom1C;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ExchangeUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final ExchangeRepository exchangeRepository;

    @Autowired
    public ExchangeUpdater(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    public void excecute(DataFrom1C dataFrom1C) {
        logger.info("START update exchange table from JSON object:[dataFrom1C]");
        final List<ExchangeId> list = exchangeRepository.findMaxExchangedIdGroupedByServerName();
        final Map<String, Integer> map = list.stream()
                .collect(Collectors.toMap(ExchangeId::getServerName, ExchangeId::getPackageNumber));

        final String server = dataFrom1C.getServer();
        final Integer value = map.get(server);
        final Integer packageNumber = dataFrom1C.getPackageNumber();
        if (value != null && packageNumber <= value) {
            final String format = String.format("package number %d for %s server must be greater then %d", packageNumber, server, value);
            logger.error(format);
            throw new IllegalStateException(format);
        }

        ObjectMapper mapper = new ObjectMapper();
        String objectToJson;
        try {
            objectToJson = mapper.writeValueAsString(dataFrom1C);
        } catch (JsonProcessingException e) {
            logger.error("can't map object to json: {}", dataFrom1C);
            throw new IllegalStateException("can't map object to json");
        }
        ExchangeId id = new ExchangeId();
        id.setPackageNumber(packageNumber);
        id.setServerName(server);
        Exchange exchange = new Exchange();
        exchange.setExchangeId(id);
        exchange.setPackageCreated(dataFrom1C.getCreated());
        exchange.setPackageData(objectToJson);

        exchangeRepository.save(exchange);

        logger.info("INSERT INTO exchange table completed server = [{}], packageNumber = [{}]", server, packageNumber);
    }
}