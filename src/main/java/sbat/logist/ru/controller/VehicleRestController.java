package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.Vehicle;
import sbat.logist.ru.transport.repository.VehicleRepository;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class VehicleRestController {
    @Autowired
    VehicleRepository vehicleRepository;

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/vehicles", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Vehicle> getVehicles(@Valid @RequestBody DataTablesInput input){
        return vehicleRepository.findAll(input);
    }
}
