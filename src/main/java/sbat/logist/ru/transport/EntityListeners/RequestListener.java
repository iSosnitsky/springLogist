package sbat.logist.ru.transport.EntityListeners;


import org.springframework.beans.factory.annotation.Autowired;
import sbat.logist.ru.transport.domain.MatViewBigSelect;
import sbat.logist.ru.transport.domain.Request;
import sbat.logist.ru.transport.repository.MatViewBigSelectRepository;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.util.Optional;

public class RequestListener {
    @Autowired
    MatViewBigSelectRepository matViewBigSelectRepository;


    //spaghetti everywhere
    @PostPersist
    public void postPersist(Request request){
        MatViewBigSelect matViewBigSelect = MatViewBigSelect.builder()
                .requestID(request.getId())
                .requestIDExternal(request.getExternalId())
                .requestNumber(request.getRequestNumber())
                .requestDate(request.getRequestDate())
                .invoiceNumber(request.getInvoiceNumber())
                .invoiceDate(request.getInvoiceDate())
                .documentNumber(request.getDocumentNumber())
                .documentDate(request.getDocumentDate())
                .firma(request.getFirma())
                .storage(request.getStorage())
                .boxQty(request.getBoxQuantity())
                .requestStatusID(request.getRequestStatusId())
                .commentForStatus(request.getCommentForStatus())
                .requestStatusRusName(request.getRequestStatusId().getRusRequestStatusName())
                .clientID((Optional.ofNullable(request.getClientId()).isPresent()) ? request.getClientId().getClientID() : null)
                .clientIDExternal((Optional.ofNullable(request.getClientId()).isPresent()) ? request.getClientId().getClientIDExternal() : null)
                .INN((Optional.ofNullable(request.getClientId()).isPresent()) ? request.getClientId().getInn() : null)
                .clientName((Optional.ofNullable(request.getClientId()).isPresent()) ? request.getClientId().getClientName() : null)
                .marketAgentUserID((Optional.ofNullable(request.getMarketAgentUserId()).isPresent()) ? request.getMarketAgentUserId().getUserID() : null)
                .marketAgentUserName((Optional.ofNullable(request.getMarketAgentUserId()).isPresent()) ? request.getMarketAgentUserId().getUserName() : null)
                .driverUserID((Optional.ofNullable(request.getRouteListId()).isPresent()) ? (Optional.ofNullable(request.getRouteListId().getDriverId()).isPresent() ? request.getRouteListId().getDriverId().getUserID() : null) : null)
                .driverUserName((Optional.ofNullable(request.getRouteListId()).isPresent()) ? (Optional.ofNullable(request.getRouteListId().getDriverId()).isPresent() ? request.getRouteListId().getDriverId().getUserName() : null) : null)
                .routeListNumber(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getRouteListNumber() : null)
                .deliveryPointID(request.getDestinationPointId().getPointId())
                .deliveryPointName(request.getDestinationPointId().getPointName())
                .warehousePointID(request.getWarehousePoint().getPointId())
                .warehousePointName(request.getWarehousePoint().getPointName())
                .lastVisitedPointID(request.getLastVisitedRoutePointId().getPointId())
                .lastVisitedPointName(request.getLastVisitedRoutePointId().getPointName())
                .licensePlate(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getLicensePlate() : null)
                .palletsQty(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getPalletsQty() : null)
                .routeListID(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getRouteListId() : null)
                .routeID(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getRouteId().getId() : null)
                .routeName(Optional.ofNullable(request.getRouteListId()).isPresent() ? request.getRouteListId().getRouteId().getRouteName() : null)
                //Et cetera et cetera
                .build();
        matViewBigSelectRepository.save(matViewBigSelect);
    }

}
