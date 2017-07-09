package sbat.logist.ru.parser.json;

import lombok.Data;

import java.util.List;

@Data
public class PackageData {
    private List<JsonPoint> updatePoints;
    private List<JsonPoint> deletePoints;

    private List<JsonDirection> updateDirections;
    private List<JsonDirection> deleteDirections;

    private List<JsonTrader> updateTrader;
    private List<JsonTrader> deleteTrader;

    private List<JsonClient> updateClients;
    private List<JsonClient> deleteClients;

    private List<JsonAddress> updateAddress;
    private List<JsonAddress> deleteAddress;

    private List<JsonRequest> updateRequests;
    private List<JsonRequest> deleteRequests;

    private List<JsonStatus> updateStatus;
    private List<JsonStatus> deleteStatus;

    private List<JsonRouteList> updateRouteLists;
    private List<JsonRouteList> deleteRouteLists;
}
