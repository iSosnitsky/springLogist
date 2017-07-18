package sbat.logist.ru.parser.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.sql.Date;
import java.util.Set;

@Data
@ToString
public class JsonRouteList {
    @JsonProperty("routerSheetId")
    private String routeListIdExternal;
    @JsonProperty("routerSheetNumber")
    private String routeListNumber;
    @JsonProperty("routerSheetDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date routeListDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    private Date departureDate;
    private String forwarderId;
    private String driverId;
    private String pointDepartureId;
    private String pointArrivalId;
    private String directId;
    private String status;
    private Set<String> invoices;

    private static final String NULL = "NULL";
    private static final String DELIMITER_FOR_GENERATED_ROUTE_ID = "MAG";

    /**
     * @return state of this object in terms of route scope. it can be intrasite route, trunk route
     * or error.
     */
    private RouteScopeType getRouteState() {
        if (directId.equals(NULL) && !pointArrivalId.isEmpty())
            return RouteScopeType.TRUNK_ROUTE;
        else if (!directId.isEmpty() && !directId.equals(NULL) && pointArrivalId.equals(NULL))
            return RouteScopeType.INTRASITE_ROUTE;
        else
            return RouteScopeType.ERROR;
    }

    /**
     * directId is not NULL, pointArrivalId is NULL
     * @return true if route is intrasite, false in other case.
     */
    public boolean isIntrasiteRoute() {
        return getRouteState().equals(RouteScopeType.INTRASITE_ROUTE);
    }

    /**
     * directId is NULL, pointArrivalId is not NULL If a route is the trunk route then it has no
     * direction, so method get directId makes no sense. We must generate route for such case, with
     * routeName like 52MAG607 where 52 is point departure id and 607 is point arrival id.
     *
     * @return true if route is trunk route, false in other case.
     */
    public boolean isTrunkRoute() {
        return getRouteState() == RouteScopeType.TRUNK_ROUTE;
    }

    @JsonIgnore
    public String getGeneratedRouteId() {
        if (!isTrunkRoute())
            throw new UnsupportedOperationException("Generated route id is valuable only for trunk routes");
        return getPointDepartureId() + DELIMITER_FOR_GENERATED_ROUTE_ID + getPointArrivalId();
    }

    // внутриузловой или магистральный маршрут
    private enum RouteScopeType {
        INTRASITE_ROUTE, TRUNK_ROUTE, ERROR
    }
}
