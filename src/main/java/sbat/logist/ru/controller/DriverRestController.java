package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.Driver;
import sbat.logist.ru.transport.repository.DriverRepository;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class DriverRestController {

    @Autowired
    DriverRepository driverRepository;

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/drivers", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Driver> getRoutes(@Valid @RequestBody DataTablesInput input){
        return driverRepository.findAll(input);
    }
}
