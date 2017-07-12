package sbat.logist.ru.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sbat.logist.ru.parser.exchanger.*;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.DataFrom1C;
import sbat.logist.ru.parser.json.PackageData;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DatabaseUpdaterService {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final ExchangeUpdater exchangeUpdater;
    private final PointUpdater pointUpdater;
    private final AddressUpdater addressUpdater;
    private final RouteUpdater routeUpdater;
    private final ClientUpdater clientUpdater;
    private final UserFromTraderUpdater userFromTraderUpdater;
    private final UserFromClientUpdater userFromClientUpdater;
    private final RouteListUpdater routeListUpdater;
    private final AssignRouteListsToRequests assignRouteListsToRequests;
    private final RequestStatusUpdater requestStatusUpdater;
    private final RequestUpdater requestUpdater;
    private final ClearRequestsFromRouteList clearRequestsFromRouteList;
    private final MatViewUpdater matViewUpdater;

    @Autowired
    public DatabaseUpdaterService(
            ExchangeUpdater exchangeUpdater,
            PointUpdater pointUpdater,
            AddressUpdater addressUpdater,
            RouteUpdater routeUpdater,
            ClientUpdater clientUpdater,
            UserFromTraderUpdater userFromTraderUpdater,
            UserFromClientUpdater userFromClientUpdater,
            RouteListUpdater routeListUpdater,
            AssignRouteListsToRequests assignRouteListsToRequests,
            RequestStatusUpdater requestStatusUpdater,
            RequestUpdater requestUpdater,
            ClearRequestsFromRouteList clearRequestsFromRouteList,
            MatViewUpdater matViewUpdater
    ) {
        this.exchangeUpdater = exchangeUpdater;
        this.pointUpdater = pointUpdater;
        this.addressUpdater = addressUpdater;
        this.routeUpdater = routeUpdater;
        this.clientUpdater = clientUpdater;
        this.userFromTraderUpdater = userFromTraderUpdater;
        this.userFromClientUpdater = userFromClientUpdater;
        this.routeListUpdater = routeListUpdater;
        this.assignRouteListsToRequests = assignRouteListsToRequests;
        this.requestStatusUpdater = requestStatusUpdater;
        this.requestUpdater = requestUpdater;
        this.clearRequestsFromRouteList = clearRequestsFromRouteList;
        this.matViewUpdater = matViewUpdater;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Data1c data1c) {
        final DataFrom1C dataFrom1C = data1c.getDataFrom1C();
        String server = dataFrom1C.getServer();
        logger.info("server = {}", server);
        Integer packageNumber = dataFrom1C.getPackageNumber();
        logger.info("packageNumber = {}", packageNumber);
        logger.info("dateCreated = {}", dataFrom1C.getCreated());

        exchangeUpdater.excecute(data1c);
//        if(true) throw new RuntimeException();
        PackageData packageData = dataFrom1C.getPackageData();
        pointUpdater.execute(packageData.getUpdatePoints());
        addressUpdater.execute(packageData.getUpdateAddress());
        routeUpdater.execute(packageData.getUpdateDirections(), packageData.getUpdateRouteLists());
        clientUpdater.execute(packageData.getUpdateClients());
        userFromTraderUpdater.execute(packageData.getUpdateTrader());
        userFromClientUpdater.execute(packageData.getUpdateClients());
        routeListUpdater.execute(packageData.getUpdateRouteLists());
        requestUpdater.execute(packageData.getUpdateRequests());
        requestStatusUpdater.execute(packageData.getUpdateStatus());
        clearRequestsFromRouteList.execute(packageData.getUpdateRouteLists());
        assignRouteListsToRequests.execute(packageData.getUpdateRouteLists());
        matViewUpdater.execute();

    }
}
