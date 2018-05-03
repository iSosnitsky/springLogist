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
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.UserDTRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import javax.validation.Valid;

@RestController
public class UserRestController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDTRepository userDTRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/users", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<User> getUsers(){
        return userDTRepository.findAll(new DataTablesInput());
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/users", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<User> getUsers(@Valid @RequestBody DataTablesInput input){
        return userDTRepository.findAll(input);
    }




}
