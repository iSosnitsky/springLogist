package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.TransportCompany;
import sbat.logist.ru.transport.repository.TransportCompanyRepository;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class TransportCompanyRestController {
    @Autowired
    TransportCompanyRepository transportCompanyRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/transportCompanies", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<TransportCompany> getTransportCompanies(@Valid @RequestBody DataTablesInput input){
        return transportCompanyRepository.findAll(input);
    }
}
