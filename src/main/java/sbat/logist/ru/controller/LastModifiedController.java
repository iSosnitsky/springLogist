package sbat.logist.ru.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sbat.logist.ru.dto.LastModifiedEntities;
import sbat.logist.ru.transport.domain.Driver;
import sbat.logist.ru.transport.domain.TransportCompany;
import sbat.logist.ru.transport.domain.Vehicle;
import sbat.logist.ru.transport.repository.DriverRepository;
import sbat.logist.ru.transport.repository.TransportCompanyRepository;
import sbat.logist.ru.transport.repository.VehicleRepository;

import java.util.List;

@RestController
@RequestMapping("/lastModified")
public class LastModifiedController {

    private final VehicleRepository vehicleRepository;
    private final TransportCompanyRepository transportCompanyRepository;
    private final DriverRepository driverRepository;

    public LastModifiedController(VehicleRepository vehicleRepository, TransportCompanyRepository transportCompanyRepository, DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.transportCompanyRepository = transportCompanyRepository;
        this.driverRepository = driverRepository;
    }

    @RequestMapping(value = "/vehicles",method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<Vehicle> getLastModifiedVehicles(){
        return vehicleRepository.getLastModified();
    }

    @RequestMapping(value = "/drivers", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<Driver> getLastModifiedDrivers(){
        return driverRepository.getLastModified();
    }

    @RequestMapping(value = "/transportCompanies", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<TransportCompany> getLastModifiedCompanies(){
        return transportCompanyRepository.getLastModified();
    }

    @RequestMapping(value="/all", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    LastModifiedEntities getAll(){
        LastModifiedEntities lastModifiedEntities = new LastModifiedEntities();
        lastModifiedEntities.setTransportCompanies(transportCompanyRepository.getLastModified());
        lastModifiedEntities.setDrivers(driverRepository.getLastModified());
        lastModifiedEntities.setVehicles(vehicleRepository.getLastModified());
        return lastModifiedEntities;
    }
}
