package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonRouteList;
import sbat.logist.ru.transport.repository.RequestRepository;
import sbat.logist.ru.transport.repository.RouteListRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Component
public class ClearRequestsFromRouteList {
    private static final Logger logger = LoggerFactory.getLogger("main");

    private final RouteListRepository routeListRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public ClearRequestsFromRouteList(RouteListRepository routeListRepository, RequestRepository requestRepository) {
        this.routeListRepository = routeListRepository;
        this.requestRepository = requestRepository;
    }

    public void execute(List<JsonRouteList> routeLists) {
        logger.info("START UPDATE requests clear routeLists");

        final AtomicInteger counter = new AtomicInteger(0);


        routeLists.stream()
                .flatMap(routeList -> routeListRepository.findByRouteListIdExternalAndAndDataSourceId(routeList.getRouteListIdExternal(), DataSource.LOGIST_1C)
                        .map(Stream::of)
                        .orElseGet(() -> {
                            logger.warn("Can't find route list in database. routeListId external id: {}", routeList.getRouteListIdExternal());
                            return Stream.empty();
                        }))
                .flatMap(routeList -> requestRepository.findByRouteListId(routeList).stream())
                .forEach(request -> {
                    request.setRouteListId(null);
                    request.setWarehousePoint(null);
                    requestRepository.save(request);

                    counter.incrementAndGet();
                });

        logger.info("UPDATE requests clear routeLists completed, affected records size = [{}]", counter);
    }
}
