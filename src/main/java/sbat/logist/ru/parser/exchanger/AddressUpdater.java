package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.PointType;
import sbat.logist.ru.parser.json.JsonAddress;
import sbat.logist.ru.transport.domain.Point;
import sbat.logist.ru.transport.repository.PointRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AddressUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;
    private final PointRepository pointRepository;

    @Autowired
    public AddressUpdater(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }


    public void execute(List<JsonAddress> jsonAddresses) {
        logger.info("START update points table from JSON objects: [updateAddresses]");
        final AtomicInteger counter = new AtomicInteger(0);
        jsonAddresses.forEach(jsonAddress -> {
            final Point foundPoint = pointRepository.findByPointIdExternalAndDataSource(jsonAddress.getAddressId(), DATA_SOURCE)
                    .map(point -> updatePoint(point,jsonAddress))
                    .orElseGet(() -> mapJsonAddress(jsonAddress));

            pointRepository.save(foundPoint);
            counter.incrementAndGet();
        });

        logger.info("INSERT OR UPDATE ON DUPLICATE INTO [points] completed, affected records size = [{}]", counter.get());
    }

    private Point mapJsonAddress(JsonAddress jsonAddress) {
        return Point.builder()
                .pointIdExternal(jsonAddress.getAddressId())
                .dataSource(DATA_SOURCE)
                .pointName(jsonAddress.getAddressShot())
                .address(jsonAddress.getAddressFull())
                .pointTypeId(PointType.AGENCY)
                .build();
    }

    private Point updatePoint(Point point, JsonAddress jsonAddress){
        point.setPointName(jsonAddress.getAddressShot());
        point.setAddress(jsonAddress.getAddressFull());
        return point;
    }
}
