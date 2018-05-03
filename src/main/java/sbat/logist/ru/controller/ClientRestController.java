package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.repository.ClientRepository;

import javax.validation.Valid;

@RestController
public class ClientRestController {
    @Autowired
    ClientRepository clientRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/clients", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Client> getUsers(){
        return clientRepository.findAll(new DataTablesInput());
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/clients", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Client> getUsers(@Valid @RequestBody DataTablesInput input){
        return clientRepository.findAll(input);
    }
}
