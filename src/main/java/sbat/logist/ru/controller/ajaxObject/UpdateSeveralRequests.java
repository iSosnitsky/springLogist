package sbat.logist.ru.controller.ajaxObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.Resource;
import sbat.logist.ru.constant.RequestStatus;
import sbat.logist.ru.transport.domain.*;
import sbat.logist.ru.transport.repository.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

//    requestStatusId: newStatusID,
//    requestDate: date,
//    comment: comment,
//    vehicleNumber: vehicleNumber,
//    palletsQty: palletsQty,
//    boxQty: boxQty,
//    requests: requests,
//    routeListId: dataTable.row($('#user-grid .selected')).data().routeListId,
//    hoursAmount: Number(hoursAmount),
//    goodCost: Number(goodCost),
//    company: companyId,
//    vehicle: vehicleId,
//    vehicle2: vehicle2Id,
//    vehicle3: vehicle3Id,
//    driver: driverID

@Data
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateSeveralRequests {
    private RequestStatus requestStatus;
//
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm")
    private Date requestDate;
    private String comment;
    private String vehicleNumber;
    private Integer boxQty;
    private Integer palletsQty;
    private Integer goodCost;

    @JsonProperty("requests")
    private List<Integer> requestsIds;

    private Integer routeListId;

    private Integer hoursAmount;

    @JsonProperty("transportCompany")
    private Long transportCompanyId;

    @JsonProperty("vehicles")
    private List<Long> vehicleIds;

    @JsonProperty("drivers")
    private List<Long> driverIds;

    @JsonIgnore
    private List<Driver> drivers = new ArrayList<>();

    @JsonIgnore
    private List<Vehicle> vehicles = new ArrayList<>();

    @JsonIgnore
    private List<Request> requests;

    @JsonIgnore
    private TransportCompany transportCompany;

    @JsonIgnore
    private RouteList routeList;


    public void processEntities(RequestRepository requestRepository, DriverRepository driverRepository, VehicleRepository vehicleRepository, RouteListRepository routeListRepository, TransportCompanyRepository transportCompanyRepository){
        this.routeList=routeListRepository.findById(this.routeListId).orElseThrow(() ->new IllegalArgumentException("Route List does not exist"));
        this.requests=routeList.getRequests().stream().filter(x->(this.requestsIds.contains(x.getId()))).map(x->{
            x.setHoursAmount(this.hoursAmount);
            x.setCommentForStatus(this.comment);
            x.setLastStatusUpdated(this.requestDate);
            x.setGoodsCost(BigDecimal.valueOf(this.goodCost));
            x.setBoxQuantity(this.boxQty);
            x.setRequestStatusId(this.requestStatus);
            return x;
        }).collect(Collectors.toList());




        if (this.transportCompanyId!=null){
            this.transportCompany=transportCompanyRepository.findById(this.transportCompanyId).orElseThrow(() -> new IllegalArgumentException("Transport company does not exist"));
            routeList.setTransportCompany(this.transportCompany);
            if(this.driverIds!=null) {
                driverRepository.findAllById(this.driverIds)
                        .forEach(x -> {
                            this.drivers.add(x);
                        });
                routeList.setDrivers(this.drivers);
            }
            if(this.vehicles!=null){
                vehicleRepository.findAllById(this.vehicleIds).forEach(this.vehicles::add);
                routeList.setVehicles(this.vehicles);
            }
        }
        routeList.setPalletsQty(this.palletsQty);
        requestRepository.saveAll(this.requests);
        routeListRepository.save(this.routeList);
    }

}
