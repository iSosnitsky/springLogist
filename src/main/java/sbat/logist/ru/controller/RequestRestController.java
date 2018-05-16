package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sbat.logist.ru.configuration.logist.user.LogistAuthToken;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.domain.RouteList;
import sbat.logist.ru.transport.domain.User;
import sbat.logist.ru.transport.domain.specification.RequestForUser;
import sbat.logist.ru.transport.repository.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class RequestRestController {



    @Autowired
    private MatViewBigSelectRepository matViewBigSelectRepository;

    @Autowired
    private RouteListDTRepository routeListDTRepository;

    @Autowired
    private RequestDTReqpository requestDTReqpository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDTRepository userDTRepository;


    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/requests", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Request> getRequests(){
        return requestDTReqpository.findAll(new DataTablesInput());
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    @Transactional(propagation = Propagation.REQUIRED)
    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelectPost(@Valid @RequestBody DataTablesInput input){
//        return matViewBigSelectRepository.findAll(input);
        LogistAuthToken authentication = (LogistAuthToken)SecurityContextHolder.getContext().getAuthentication();
        switch (authentication.getRole()){
            case "MARKET_AGENT": return matViewBigSelectRepository.findAll(input, RequestForUser.requestsForMarketAgent(authentication.getUser()));
            case "CLIENT_MANAGER": return matViewBigSelectRepository.findAll(input, RequestForUser.requestsForClient(authentication.getUser().getClient()));
            default: return matViewBigSelectRepository.findAll(input);

        }
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/matViewBigSelect", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<MatViewBigSelect> getMatViewBigSelect(){
        LogistAuthToken authentication = (LogistAuthToken)SecurityContextHolder.getContext().getAuthentication();
        System.out.println("fsgfsd");
        switch (authentication.getRole()){
            case "MARKET_AGENT": return matViewBigSelectRepository.findAll(new DataTablesInput(), RequestForUser.requestsForMarketAgent(authentication.getUser()));
            case "CLIENT_MANAGER": return matViewBigSelectRepository.findAll(new DataTablesInput(), RequestForUser.requestsForClient(authentication.getUser().getClient()));
            default: return matViewBigSelectRepository.findAll(new DataTablesInput());

        }


    }

    @RequestMapping(value = "/data/updateSeveralRequests", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<MatViewBigSelect> updateSeveralRequests(){

        LogistAuthToken authentication = (LogistAuthToken)SecurityContextHolder.getContext().getAuthentication();
        System.out.println("fsgfsd");
        switch (authentication.getRole()){
            case "MARKET_AGENT": return matViewBigSelectRepository.findAll(new DataTablesInput(), RequestForUser.requestsForMarketAgent(authentication.getUser()));
            case "CLIENT_MANAGER": return matViewBigSelectRepository.findAll(new DataTablesInput(), RequestForUser.requestsForClient(authentication.getUser().getClient()));
            default: return matViewBigSelectRepository.findAll(new DataTablesInput());

        }


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

}
