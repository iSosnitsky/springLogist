package sbat.logist.ru.parser.json;

import lombok.Data;

import java.util.List;

@Data
public class PackageData {
    private List<JsonPoint> updatePoints;
    private List<JsonPoint> deletePoints;

    private List<Direction> updateDirections;
    private List<Direction> deleteDirections;

    private List<Trader> updateTrader;
    private List<Trader> deleteTrader;

    private List<Client> updateClients;
    private List<Client> deleteClients;

    private List<JsonAddress> updateAddress;
    private List<JsonAddress> deleteAddress;

    private List<Request> updateRequests;
    private List<Request> deleteRequests;

    private List<Status> updateStatus;
    private List<Status> deleteStatus;

    private List<JsonRouteList> updateRouteLists;
    private List<JsonRouteList> deleteRouteLists;
}
