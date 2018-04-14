package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.repository.UserDTRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import javax.validation.Valid;

@RestController
@CrossOrigin
public class UserRestContoller {
    @Autowired
    UserDTRepository userDTRepository;

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/users", method = RequestMethod.GET)
    public DataTablesOutput<User> getMatViewBigSelect(){

        return userDTRepository.findAll(new DataTablesInput());
    }
}
