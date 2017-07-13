package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sbat.logist.ru.parser.exchanger.ExchangeUpdater;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.DataFrom1C;

@Service
public class DatabaseUpdaterService {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final ExchangeUpdater exchangeUpdater;
    private final TransportUpdater transportUpdater;

    @Autowired
    public DatabaseUpdaterService(
            ExchangeUpdater exchangeUpdater,
            TransportUpdater transportUpdater
    ) {
        this.exchangeUpdater = exchangeUpdater;
        this.transportUpdater = transportUpdater;
    }

    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "backupTransactionManager")
    public void updateBackup(Data1c data1c) {
        final DataFrom1C dataFrom1C = data1c.getDataFrom1C();
        String server = dataFrom1C.getServer();
        logger.info("server = {}", server);
        Integer packageNumber = dataFrom1C.getPackageNumber();
        logger.info("packageNumber = {}", packageNumber);
        logger.info("dateCreated = {}", dataFrom1C.getCreated());
        exchangeUpdater.excecute(data1c);
        transportUpdater.updateMain(data1c);
    }
}
