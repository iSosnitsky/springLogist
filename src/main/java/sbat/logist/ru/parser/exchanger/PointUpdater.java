package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.PointType;
import sbat.logist.ru.parser.json.JsonPoint;
import sbat.logist.ru.transport.domain.Point;
import sbat.logist.ru.transport.repository.PointRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PointUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private final PointRepository pointRepository;

    public PointUpdater(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public void execute(List<JsonPoint> jsonPoints) {
        final AtomicInteger counter = new AtomicInteger(0);
        logger.info("START update jsonPoints table from JSON object:[updatePointsArray]");
        jsonPoints.forEach(jsonPoint -> {
            final Point dbPoint = pointRepository.findByPointIdExternalAndDataSource(jsonPoint.getPointId(), DATA_SOURCE)
                    .map(foundPoint -> fillPoint(foundPoint, jsonPoint))
                    .orElseGet(() -> {
                        Point p = Point.builder()
                                .pointIdExternal(jsonPoint.getPointId())
                                .dataSource(DATA_SOURCE)
                                .build();
                        return fillPoint(p, jsonPoint);
                    });
            pointRepository.save(dbPoint);
            counter.incrementAndGet();
        });

        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", counter.get());
    }

    private Point fillPoint(Point point, JsonPoint jsonPoint) {
        point.setPointName(jsonPoint.getPointName());
        point.setAddress(jsonPoint.getPointAddress());
        point.setEmail(jsonPoint.getPointEmails());
        point.setPointTypeId(PointType.valueOf(jsonPoint.getPointType()));
        point.setResponsiblePersonId(jsonPoint.getResponsiblePersonId());

        return point;
    }
}
