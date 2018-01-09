package sbat.logist.ru.parser.exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.parser.json.JsonCar;
import sbat.logist.ru.transport.domain.Vehicle;
import sbat.logist.ru.transport.repository.VehicleRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class VehicleUpdater {
    private static final Logger logger = LoggerFactory.getLogger("main");
    private static final DataSource DATA_SOURCE = DataSource.LOGIST_1C;

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleUpdater(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public void execute(List<JsonCar> jsonCars){
        logger.info("START update cars in vehicles table from JSON object:[cars]");
        AtomicInteger counter = new AtomicInteger(0);
        jsonCars.forEach(jsonCar -> {
            try{
                final Vehicle vehicle = vehicleRepository.findByVehicleIdExternalAndDataSource(jsonCar.getCarId(),DATA_SOURCE).orElseGet(Vehicle::new);
                vehicle.setVehicleIdExternal(jsonCar.getCarId());
                vehicle.setDataSource(DATA_SOURCE);
                vehicle.setLicenseNumber(jsonCar.getRegionNumber()+" "+jsonCar.getCarNumber());
                try{
                    vehicleRepository.save(vehicle);
                    counter.incrementAndGet();
                } catch (Exception e){
                    logger.error("unable to save Car:\n{}",jsonCar.toString());
//                    logger.error(request.toString());
                }

            } catch (IllegalStateException e){
                logger.warn("Unable to update car status [{}]",jsonCar.getCarId());
            }
        });
        logger.info("UPDATE cars in vehicles table completed, affected records size = [{}]", counter.get());
    }

}
