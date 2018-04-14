package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RouteList;
import sbat.logist.ru.transport.repository.MatViewBigSelectRepository;
import sbat.logist.ru.transport.repository.RequestDTReqpository;
import sbat.logist.ru.transport.repository.RouteListDTRepository;
import sbat.logist.ru.transport.repository.RouteListRepository;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class RequestRestController {

    @Autowired
    private MatViewBigSelectRepository matViewBigSelectRepository;

    @Autowired
    private RouteListDTRepository routeListDTRepository;


//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/requests", method = RequestMethod.GET)
//    public DataTablesOutput<Request> getRequests() {
//        return requestDTReqpository.findAll(new DataTablesInput());
//    }
//
//
//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/routeLists", method = RequestMethod.GET)
//    public DataTablesOutput<RouteList> getRouteLists() {
//        return routeListDTRepository.findAll(new DataTablesInput());
//    }

//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.POST)
//    public DataTablesOutput<MatViewBigSelect> postMatViewBigSelect(@Valid DataTablesInput input){
//        return matViewBigSelectRepository.findAll(input);
//    }

//    @CrossOrigin(origins = "http://localhost:*")
//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.GET)
//    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelect(@Valid DataTablesInput input){
//        return matViewBigSelectRepository.findAll(input);
//    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelectPost(@Valid @RequestBody DataTablesInput input){
        return matViewBigSelectRepository.findAll(input);
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/routeLists", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<RouteList> postRouteLists(@Valid @RequestBody DataTablesInput input){
        return routeListDTRepository.findAll(input);
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/routeLists", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<RouteList> getRouteLists(){
        return routeListDTRepository.findAll(new DataTablesInput());
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/testRouteLists", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<RouteList> testRouteLists(){
        return routeListDTRepository.findAll(new DataTablesInput());
    }
//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.GET)
//    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelect(@Valid DataTablesInput input, Specification<MatViewBigSelect> specification){
//        return matViewBigSelectRepository.findAll(input);
//    }
}
