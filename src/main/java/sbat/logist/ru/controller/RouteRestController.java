package sbat.logist.ru.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sbat.logist.ru.transport.domain.Route;
import sbat.logist.ru.transport.domain.RoutePoint;
import sbat.logist.ru.transport.domain.Tariff;
import sbat.logist.ru.transport.domain.specification.RoutePointsForRoute;
import sbat.logist.ru.transport.repository.RouteRepository;
import sbat.logist.ru.transport.repository.TariffRepoisitory;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class RouteRestController {
    @Autowired
    RouteRepository routeRepository;

    @Autowired
    TariffRepoisitory tariffRepoisitory;

//    @Autowired
//    RoutePointRepository routePointRepository;

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/routes", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Route> getRoutes(@Valid @RequestBody DataTablesInput input){
        return routeRepository.findAll(input);
    }

    @CrossOrigin(origins = "http://localhost:*")
    @JsonView(DataTablesOutput.View.class)
    @RequestMapping(value = "/data/tariffs", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
    public DataTablesOutput<Tariff> getTariffs(@Valid @RequestBody DataTablesInput input){
        return tariffRepoisitory.findAll(input);
    }

//    @CrossOrigin(origins = "http://localhost:*")
//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/routePointsForRoute", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE)
//    public DataTablesOutput<RoutePoint> postRoutePointsForRoute(@Param("route") Route route){
//        return routePointRepository.findAll(new DataTablesInput(), RoutePointsForRoute.routePointsForRoute(route));
//    }
//
//    @CrossOrigin(origins = "http://localhost:*")
//    @JsonView(DataTablesOutput.View.class)
//    @RequestMapping(value = "/data/routePointsForRoute", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE)
//    public DataTablesOutput<RoutePoint> getRoutePointsForRoute(@Param("route") Route route){
//        return routePointRepository.findAll(new DataTablesInput(), RoutePointsForRoute.routePointsForRoute(route));
//    }

}
