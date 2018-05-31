package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sbat.logist.ru.transport.domain.Client;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.ClientRepository;
import sbat.logist.ru.transport.repository.UserDTRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import javax.validation.Valid;

@RestController
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDTRepository userDTRepository;

    @Autowired
    ClientRepository clientRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/users", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<User> getUsers(){
        return userDTRepository.findAll(new DataTablesInput());
    }


    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/clients", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Client> getClients(@Valid @RequestBody DataTablesInput input){
        return clientRepository.findAll(input);
    }


    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/users", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<User> getUsers(@Valid @RequestBody DataTablesInput input){
        return userDTRepository.findAll(input);
    }






}
