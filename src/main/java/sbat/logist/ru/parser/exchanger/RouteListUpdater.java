package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonRouteList;
import sbat.logist.ru.transport.domain.Route;
import sbat.logist.ru.transport.domain.RouteList;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.RouteListRepository;
import sbat.logist.ru.transport.repository.RouteListStatusRepository;
import sbat.logist.ru.transport.repository.RouteRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RouteListUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private final RouteRepository routeRepository;
    private final RouteListRepository routeListRepository;
    private final UserRepository userRepository;
    private final RouteListStatusRepository routeListStatusRepository;

    @Autowired
    public RouteListUpdater(
            RouteListRepository routeListRepository,
            RouteRepository routeRepository,
            UserRepository userRepository,
            RouteListStatusRepository routeListStatusRepository
    ) {
        this.routeListRepository = routeListRepository;
        this.routeRepository = routeRepository;
        this.userRepository = userRepository;
        this.routeListStatusRepository = routeListStatusRepository;
    }

    public void execute(List<JsonRouteList> jsonRouteLists) {
        final AtomicInteger counter = new AtomicInteger(0);
        logger.info("START update route_lists table from JSON object:[updateRouteLists]");

        jsonRouteLists.forEach(jsonRouteList -> {
            String routeIdExternal = null;
            if (jsonRouteList.isIntrasiteRoute()) {
                routeIdExternal = jsonRouteList.getDirectId();
            } else if (jsonRouteList.isTrunkRoute()) {
                routeIdExternal = jsonRouteList.getGeneratedRouteId();
            }
            if (routeIdExternal != null) {
                try {
                    User driver = userRepository.findByUserIDExternalAndDataSource(jsonRouteList.getDriverId(), DATA_SOURCE).orElse(null);
                    Route route = routeRepository.findByExternalIdAndDataSource(routeIdExternal, DATA_SOURCE).orElseThrow(IllegalStateException::new);

                    RouteList routeList = routeListRepository.findByRouteListIdExternalAndAndDataSourceId(jsonRouteList.getRouteListIdExternal(), DATA_SOURCE)
                            .map(rl -> fillRouteList(rl, jsonRouteList, route, driver))
                            .orElseGet(() -> {
                                RouteList rl = RouteList.builder()
                                        .routeListIdExternal(jsonRouteList.getRouteListIdExternal())
                                        .creationDate(jsonRouteList.getRouteListDate())
                                        .dataSourceId(DataSource.LOGIST_1C)
                                        .build();
                                return fillRouteList(rl, jsonRouteList, route, driver);
                            });
                    routeListRepository.save(routeList);
                    counter.incrementAndGet();
                } catch (IllegalStateException e) {
                    logger.error("Error: {}", e);
                    logger.error("Unable to insert RouteList [{}] because route[{}] wasn't found in routes table", jsonRouteList.getRouteListIdExternal(), routeIdExternal);
                }


            } else {
                logger.warn("Unable to insert RouteList[{}] into RouteLists, route doesn't exist in Routes table or route isn't pointed out", jsonRouteList.getRouteListIdExternal());
            }
        });

        logger.info("INSERT OR UPDATE INTO route_lists completed, affected records size = [{}]", counter.get());
    }

    private RouteList fillRouteList(RouteList rl, JsonRouteList jsonRouteList, Route route, User driver) {
        rl.setDepartureDate(jsonRouteList.getDepartureDate());
        rl.setForwarderId(jsonRouteList.getForwarderId());
        rl.setRouteListNumber(jsonRouteList.getRouteListNumber());
        rl.setRouteId(route);
        rl.setDriverId(driver);
        rl.setStatus((routeListStatusRepository.findByRouteListStatusId(jsonRouteList.getStatus())
                .orElseThrow(() -> new IllegalStateException("Can't find route list status in repo " + jsonRouteList.getStatus()))));
        return rl;
    }
}
