package sbat.logist.ru.parser.json;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class JsonRouteList {
    private String routeListIdExternal;
    private String routeListNumber;
    private Date routeListDate;
    private Date departureDate;
    private String forwarderId;
    private String driverId;
    private String pointDepartureId;
    private String pointArrivalId;
    private String directId;
    private String status;
    private Set<String> invoices;
}
