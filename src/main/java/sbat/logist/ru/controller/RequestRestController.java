package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.Hibernate;
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
import sbat.logist.ru.transport.domain.*;
import sbat.logist.ru.transport.domain.specification.RequestForUser;
import sbat.logist.ru.transport.repository.*;

import javax.validation.Valid;
import java.util.Optional;

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
    private RequestRepository requestReqpository;

    @Autowired
    private ExchangeLogRepository exchangeLogRepository;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientRepository clientRepository;

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
    @RequestMapping(value = "/data/exchangeLogs", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<ExchangeLog> getExchangeLogs(@Valid @RequestBody DataTablesInput input){
        return exchangeLogRepository.findAll(input);
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
    @RequestMapping(value="/api/requests/findFirstByClientIdExternalAndInvoiceNumber", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Request findFirstByClientIdExternalAndInvoiceNumber(@Param("clientIdExternal") String clientIdExternal, @Param("invoiceNumber") String invoiceNumber){
        Optional<Client> client =clientRepository.findByClientIDExternal(clientIdExternal);
        if (client.isPresent()){
            Optional<Request> request = requestReqpository.findFirstByClientIdAndInvoiceNumber(client.get(), invoiceNumber);
            if (request.isPresent()) {
                Request request1 = request.get();
                Hibernate.initialize(request1.getMarketAgentUserId());
                Hibernate.initialize(request1.getClientId());
                Hibernate.initialize(request1.getDestinationPointId());
                Hibernate.initialize(request1.getRouteListId());
                Hibernate.initialize(request1.getWarehousePoint());
                Hibernate.initialize(request1.getRequestHistory());
                return request1;
            } else return null;
        } else {
            return null;
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


    @RequestMapping(value = "/data/updateRequest", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<MatViewBigSelect> updateRequest(){

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


    @CrossOrigin(origins = "http://localhost:*")
    @RequestMapping(value="/commands/updateRequest/{requestIdExternal}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public Request updateRequest(@PathVariable String requestIdExternal){
        Optional<Request> request =requestReqpository.findFirstByExternalId(requestIdExternal);
        if (request.isPresent()){
            return null;
        } else {
            return null;
        }
    }
}
