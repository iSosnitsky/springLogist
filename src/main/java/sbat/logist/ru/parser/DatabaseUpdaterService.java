package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sbat.logist.ru.parser.exchanger.*;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.DataFrom1C;

@Service
public class DatabaseUpdaterService {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final ExchangeUpdater exchangeUpdater;
    private final PointUpdater pointUpdater;
    private final AddressUpdater addressUpdater;
    private final RouteUpdater routeUpdater;
    private final ClientUpdater clientUpdater;
    private final UserFromTraderUpdater userFromTraderUpdater;

    @Autowired
    public DatabaseUpdaterService(
            ExchangeUpdater exchangeUpdater,
            PointUpdater pointUpdater,
            AddressUpdater addressUpdater,
            RouteUpdater routeUpdater,
            ClientUpdater clientUpdater,
            UserFromTraderUpdater userFromTraderUpdater
    ) {
        this.exchangeUpdater = exchangeUpdater;
        this.pointUpdater = pointUpdater;
        this.addressUpdater = addressUpdater;
        this.routeUpdater = routeUpdater;
        this.clientUpdater = clientUpdater;
        this.userFromTraderUpdater = userFromTraderUpdater;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Data1c data1c) {
        final DataFrom1C dataFrom1C = data1c.getDataFrom1C();
        String server = dataFrom1C.getServer();
        logger.info("server = {}", server);
        Integer packageNumber = dataFrom1C.getPackageNumber();
        logger.info("packageNumber = {}", packageNumber);
        logger.info("dateCreated = {}", dataFrom1C.getCreated());

        exchangeUpdater.excecute(dataFrom1C);
        pointUpdater.execute(dataFrom1C.getPackageData().getUpdatePoints());
        addressUpdater.execute(dataFrom1C.getPackageData().getUpdateAddress());
        routeUpdater.execute(dataFrom1C.getPackageData().getUpdateDirections(), dataFrom1C.getPackageData().getUpdateRouteLists());
        clientUpdater.execute(dataFrom1C.getPackageData().getUpdateClients());
        userFromTraderUpdater.execute(dataFrom1C.getPackageData().getUpdateTrader());

//        transactionExecutor.put(11, new UpdateUsersFromTraders(packageData.getUpdateTraders()));
//
//        transactionExecutor.put(12, new UpdateUsersFromClients(packageData.getUpdateClients()));
//
//        transactionExecutor.put(13, new DeleteRouteLists(packageData.getDeleteRouteLists()));
//        transactionExecutor.put(14, new UpdateRouteLists(packageData.getUpdateRouteLists()));
//
//        transactionExecutor.put(15, new DeleteRequests(packageData.getDeleteRequests()));
//        transactionExecutor.put(16, new UpdateRequests(packageData.getUpdateRequests()));
//
//        transactionExecutor.put(17, new AssignStatusesInRequests(packageData.getUpdateStatuses()));
//        transactionExecutor.put(18, new ClearRouteListsInRequests(packageData.getUpdateRouteLists()));
//        transactionExecutor.put(19, new AssignRouteListsInRequests(packageData.getUpdateRouteLists()));
//        transactionExecutor.put(20, new RefreshMatView());
//
//        try {
//            transactionExecutor.executeAll();
//            connection.commit();
//        } finally {
//            transactionExecutor.closeAll();
//        }
    }
}
