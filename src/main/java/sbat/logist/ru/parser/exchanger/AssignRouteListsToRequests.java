package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonRouteList;
import sbat.logist.ru.transport.domain.Point;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RouteList;
import sbat.logist.ru.transport.repository.PointRepository;
import sbat.logist.ru.transport.repository.RequestRepository;
import sbat.logist.ru.transport.repository.RouteListRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AssignRouteListsToRequests {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private final PointRepository pointRepository;
    private final RequestRepository requestRepository;
    private final RouteListRepository routeListRepository;

    @Autowired
    public AssignRouteListsToRequests(
            PointRepository pointRepository,
            RequestRepository requestRepository,
            RouteListRepository routeListRepository
    ) {
        this.pointRepository = pointRepository;
        this.requestRepository = requestRepository;
        this.routeListRepository = routeListRepository;
    }


    public void execute(List<JsonRouteList> jsonRouteLists) {
        logger.info("START UPDATE requests assign routeLists");

        AtomicInteger counter = new AtomicInteger(0);
        jsonRouteLists.forEach((jsonRouteList) -> {
            try {
                final RouteList routeList = routeListRepository.findByRouteListIdExternalAndAndDataSourceId(jsonRouteList.getRouteListIdExternal(), DATA_SOURCE).orElseThrow(IllegalStateException::new);
                final Point warehousePoint = pointRepository.findByPointIdExternalAndDataSource(jsonRouteList.getPointDepartureId(), DATA_SOURCE).orElseThrow(IllegalStateException::new);
                jsonRouteList.getInvoices().forEach(invoice -> {
                    final Request request = requestRepository.findByExternalIdAndDataSource(invoice, DATA_SOURCE).orElseThrow(IllegalStateException::new);

                    request.setRouteListId(routeList);
                    request.setWarehousePoint(warehousePoint);
                    requestRepository.save(request);
                    counter.incrementAndGet();
                });
            } catch (IllegalStateException e) {
                logger.warn("something real bad happened when assigning RouteLists to Requests. I's just sleep deprived as hell writing this. Just in case, routeListId was [{}]", jsonRouteList.getRouteListIdExternal());
            }
        });
        logger.info("UPDATE requests assign routeLists completed, affected records size = [{}]", counter.get());
    }
}
