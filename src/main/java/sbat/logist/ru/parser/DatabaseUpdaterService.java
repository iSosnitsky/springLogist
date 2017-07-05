package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sbat.logist.ru.parser.exchanger.ExchangeUpdater;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.DataFrom1C;

@Service
public class DatabaseUpdaterService {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final ExchangeUpdater exchangeUpdater;

    @Autowired
    public DatabaseUpdaterService(ExchangeUpdater exchangeUpdater) {
        this.exchangeUpdater = exchangeUpdater;
    }

    public void update(Data1c data1c) {
        final DataFrom1C dataFrom1C = data1c.getDataFrom1C();
        String server = dataFrom1C.getServer();
        logger.info("server = {}", server);
        Integer packageNumber = dataFrom1C.getPackageNumber();
        logger.info("packageNumber = {}", packageNumber);
        logger.info("dateCreated = {}", dataFrom1C.getCreated());

        exchangeUpdater.excecute(dataFrom1C);
        pointUpdater.execute(dataFrom1C.getPackageData().getUpdatePoints());

//        transactionExecutor.put(2, new DeletePoints(packageData.getDeletePoints()));
//        transactionExecutor.put(3, new UpdatePoints(packageData.getUpdatePoints()));
//
//        transactionExecutor.put(4, new DeleteAddresses(packageData.getDeleteAddresses()));
//        transactionExecutor.put(5, new UpdatePointsFromAddresses(packageData.getUpdateAddresses()));
//
//        transactionExecutor.put(6, new DeleteRoutes(packageData.getDeleteDirections()));
//        transactionExecutor.put(7, new UpdateRoutes(packageData.getUpdateDirections(), packageData.getUpdateRouteLists()));
//
//        transactionExecutor.put(8, new DeleteClients(packageData.getDeleteClients()));
//        transactionExecutor.put(9, new UpdateClients(packageData.getUpdateClients()));
//
//        transactionExecutor.put(10, new DeleteTraders(packageData.getDeleteTraders()));
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
