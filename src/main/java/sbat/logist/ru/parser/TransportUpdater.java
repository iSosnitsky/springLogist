package sbat.logist.ru.parser;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sbat.logist.ru.parser.exchanger.*;
import sbat.logist.ru.parser.json.Data1c;
import sbat.logist.ru.parser.json.PackageData;

@Component
public class TransportUpdater {
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

    public TransportUpdater(
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


    @Transactional(propagation = Propagation.REQUIRED, transactionManager = "mainTransactionManager")
    public void updateMain(Data1c data1c) {
        PackageData packageData = data1c.getDataFrom1C().getPackageData();
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
