package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonRouteList;
import sbat.logist.ru.transport.domain.Route;
import sbat.logist.ru.transport.repository.RouteRepository;

import java.util.*;

public class RouteUpdater {
    public static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private static final Logger logger = LoggerFactory.getLogger("main");
    private final RouteRepository routeRepository;

    @Autowired
    public RouteUpdater(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Autowired

    public void execute(List<JsonRouteList> routeLists) {
        logger.info("START update routes table from JSON object:[updateDirections]");

        Map<String, String> map = new HashMap<>();
        routeLists.forEach(routeList -> {
            final Route route = new Route();
            route.setExternalId(routeList.getDirectId());
            route.setDataSource(DATA_SOURCE);
            route.setRouteName(getUniqueDirectionName(generateNewDirectionNameIfDuplicate(routeList.getDirectId(), routeList.getDirectName)));
        });
//        PreparedStatement preparedStatement = connection.prepareStatement(
//                "INSERT INTO routes (directionIDExternal, dataSourceID, routeName) VALUE (?, ?, ?)\n" +
//                        "ON DUPLICATE KEY UPDATE\n" +
//                        "  routeName = VALUES(routeName);"
//        );

//
//        BidiMap<String, String> allRoutes = Selects.getInstance().selectAllRoutesAsExtKeyAndName(DBManager.LOGIST_1C);
//        for (DirectionsData updateRoute : updateRoutesArray) {
//            String directionIDExternal = updateRoute.getDirectId();
//            String directionName = updateRoute.getDirectName();
//            String uniqueDirectionName = generateNewDirectionNameIfDuplicate(allRoutes, directionIDExternal, directionName);
//            preparedStatement.setString(1, directionIDExternal);
//            preparedStatement.setString(2, DBManager.LOGIST_1C);
//            preparedStatement.setString(3, uniqueDirectionName);
//            preparedStatement.addBatch();
//        }
//
//        Map<String, Integer> allPoints = Selects.getInstance().allPointsAsKeyPairs();
//        Map<Integer, String> allPointNamesById = Selects.getInstance().allPointNamesById(DBManager.LOGIST_1C);
//        for (RouteListsData updateRouteList : updateRouteLists) {
//
//            // create new route only for trunk routes
//            if (updateRouteList.isTrunkRoute()) {
//                // insert or update route
//                preparedStatement.setString(1, updateRouteList.getGeneratedRouteId());
//                preparedStatement.setString(2, DBManager.LOGIST_1C);
//                try{
//                    String generatedRouteName = getGeneratedRouteName(allPoints, allPointNamesById, updateRouteList.getPointArrivalId(), updateRouteList.getPointDepartureId());
//                    preparedStatement.setString(3, generatedRouteName);
//                } catch (DBCohesionException e) {
//                    logger.warn(e);
//                    continue;
//                }
//                preparedStatement.addBatch();
//            }
//        }
//
//        int[] affectedRecords = preparedStatement.executeBatch();
//        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [routes] completed, affected records size = [{}]", affectedRecords.length);
//
//        return preparedStatement;
    }

    private String generateNewDirectionNameIfDuplicate(String directionIDExternal, String directionName) {
        // если у нас новое направление(directionIDExternal нет в БД) и routeName - дублируется => нужно присвоить новый routeName
        return routeRepository.findByExternalIdAndRouteName(directionIDExternal, directionName)
                .map(route -> {
                    if (!route.getExternalId().equals(directionIDExternal) && route.getRouteName().equals(directionName)) {
                        return getUniqueDirectionName(directionName);
                    } else {
                        // запрет на переименование, т.к. такое имя уже есть
                        logger.warn("directionName [{}] already exist in database ", directionName);
                        return directionName;
                    }
                })
                .orElseGet(() -> directionName);
    }

    /**
     * if allRouteNames contains routeNameCandidate then direction name changed
     *
     * @param duplicatedRouteName
     */
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
