package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonDirection;
import sbat.logist.ru.parser.json.JsonRouteList;
import sbat.logist.ru.transport.domain.Point;
import sbat.logist.ru.transport.domain.Route;
import sbat.logist.ru.transport.repository.PointRepository;
import sbat.logist.ru.transport.repository.RouteRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Component
public class RouteUpdater {
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final RouteRepository routeRepository;
    private final PointRepository pointRepository;

    @Autowired
    public RouteUpdater(
            RouteRepository routeRepository,
            PointRepository pointRepository
    ) {
        this.routeRepository = routeRepository;
        this.pointRepository = pointRepository;
    }

    public void execute(List<JsonDirection> directions, List<JsonRouteList> routeLists) {
        logger.info("START update routes table from JSON object:[updateDirections]");

        final AtomicInteger counter = new AtomicInteger(0);
        directions.forEach(direction -> {
            final String externalId = direction.getDirectId();
            final String directionName = direction.getDirectName();
            final Route r = getRouteToInsert(externalId, directionName, () -> generateNewDirectionNameIfDuplicate(externalId, directionName));

            routeRepository.save(r);
            counter.incrementAndGet();
        });

        routeLists.forEach(r -> {
            if (r.isTrunkRoute()) {
                try {
                    final String generatedRouteId = r.getGeneratedRouteId();
                    final String generatedRouteName = generateRouteName(r.getPointArrivalId(), r.getPointDepartureId());
                    final Route routeToInsert = getRouteToInsert(generatedRouteId, generatedRouteName, () -> generateNewDirectionNameIfDuplicate(generatedRouteId, generatedRouteName));

                    routeRepository.save(routeToInsert);
                    counter.incrementAndGet();
                } catch (Exception e) {
                    logger.error("can't insert route:", e);
                }
            }
        });

        logger.info("INSERT OR UPDATE INTO [routes] completed, affected records size = [{}]", counter);
    }

    /**
     * emulates on duplicates key
     */
    private Route getRouteToInsert(String externalId, String name, Supplier<String> nameSupplier) {
        return routeRepository.findByExternalIdAndDataSource(externalId, DATA_SOURCE)
                .map(r -> {
                    if (!r.getRouteName().equals(name)) {
                        r.setRouteName(nameSupplier.get());
                    }
                    return r;
                })
                .orElseGet(() -> {
                    final Route route = new Route();
                    route.setExternalId(externalId);
                    route.setDataSource(DATA_SOURCE);
                    route.setRouteName(nameSupplier.get());
                    return route;
                });
    }

    private String generateRouteName(String pointArrivalIdExternal, String pointDepartureIdExternal) {
        // check points that they are exist.
        final Point pointArrival = pointRepository.findByPointIdExternalAndDataSource(pointArrivalIdExternal, DATA_SOURCE)
                .orElseThrow(() -> {
                    logger.error("arrival point doesn't exist in database. pointIdExternal: {}", pointArrivalIdExternal);
                    return new IllegalStateException("database doesn't have point with id " + pointArrivalIdExternal);
                });
        final Point pointDeparture = pointRepository.findByPointIdExternalAndDataSource(pointDepartureIdExternal, DATA_SOURCE)
                .orElseThrow(() -> {
                    logger.error("departure point doesn't exist in database. pointIdExternal: {}", pointDepartureIdExternal);
                    return new IllegalStateException("database doesn't have point with id " + pointDepartureIdExternal);
                });
        return pointDeparture.getPointName() + '-' + pointArrival.getPointName();
    }

    private String generateNewDirectionNameIfDuplicate(String directionIDExternal, String directionName) {
        // если у нас новое направление(directionIDExternal нет в БД) и routeName - дублируется => нужно присвоить новый routeName
        return routeRepository.findByRouteName(directionName)
                .map(route -> {
                    if (!route.getExternalId().equals(directionIDExternal) && route.getRouteName().equals(directionName)) {
                        return getUniqueDirectionName(directionName);
                    } else {
                        // запрет на переименование, т.к. такое имя уже есть
                        logger.warn("directionName [{}] already exist in database ", directionName);
                        return directionName;
                    }
                })
                .orElse(directionName);
    }

    /**
     * if allRouteNames contains routeNameCandidate then direction name changed
     *
     * @param duplicatedRouteName
     */
    //TODO: Doesn't work. Logger shits with errors regarding duplicate routeNames on production server
    private String getUniqueDirectionName(final String duplicatedRouteName) {
        int count = 0;
        do {
            count++;
        } while (routeRepository.findByRouteName(duplicatedRouteName + count + "").isPresent());
        final String generatedDirectionName = duplicatedRouteName + count + "";
        logger.warn("direction name [{}] was duplicated, generated direction name = [{}]", duplicatedRouteName, generatedDirectionName);
        return generatedDirectionName;
    }
}
