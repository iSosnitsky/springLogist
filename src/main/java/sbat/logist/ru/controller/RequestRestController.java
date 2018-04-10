package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RouteList;
import sbat.logist.ru.transport.repository.MatViewBigSelectRepository;
import sbat.logist.ru.transport.repository.RequestDTReqpository;
import sbat.logist.ru.transport.repository.RouteListDTRepository;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class RequestRestController {
    @Autowired
    private RequestDTReqpository requestDTReqpository;
    @Autowired
    private RouteListDTRepository routeListDTRepository;

    @Autowired
    private MatViewBigSelectRepository matViewBigSelectRepository;

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/requests", method = RequestMethod.GET)
    public DataTablesOutput<Request> getRequests() {
        return requestDTReqpository.findAll(new DataTablesInput());
    }


    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/routeLists", method = RequestMethod.GET)
    public DataTablesOutput<RouteList> getRouteLists() {
        return routeListDTRepository.findAll(new DataTablesInput());
    }

    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.POST)
    public DataTablesOutput<MatViewBigSelect> postMatViewBigSelect(@Valid DataTablesInput input){
        return matViewBigSelectRepository.findAll(input);
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.GET)
    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelect(@Valid DataTablesInput input){
        return matViewBigSelectRepository.findAll(input);
    }

//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.GET)
//    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelect(@Valid DataTablesInput input, Specification<MatViewBigSelect> specification){
//        return matViewBigSelectRepository.findAll(input);
//    }
}
