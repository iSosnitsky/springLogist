package sbat.logist.ru.parser.json;

import lombok.Data;

import java.util.List;

@Data
public class PackageData {
    private List<Point> updatePoints;
    private List<Point> deletePoints;

    private List<Direction> updateDirections;
    private List<Direction> deleteDirections;

    private List<Trader> updateTrader;
    private List<Trader> deleteTrader;

    private List<Client> updateClients;
    private List<Client> deleteClients;

    private List<Address> updateAddress;
    private List<Address> deleteAddress;

    private List<Request> updateRequests;
    private List<Request> deleteRequests;

    private List<Status> updateStatus;
    private List<Status> deleteStatus;

    private List<RouteList> updateRouteLists;
    private List<RouteList> deleteRouteLists;
}
