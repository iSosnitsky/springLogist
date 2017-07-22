package sbat.logist.ru.transport.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@Table(name = "mat_view_big_select")
@NoArgsConstructor
@AllArgsConstructor
@NamedStoredProcedureQueries({
        @NamedStoredProcedureQuery(name = "refreshMaterializedView", procedureName = "refreshMaterializedView")
})
public class MatViewBigSelect {
    @Id
    @Column(name = "REQUESTID")
    private Long requestId;

    @Column(name="REQUESTIDEXTERNAL")
    private String requestIdExternal;

    @Column(name="REQUESTNUMBER")
    private String requestNumber;

    @Column(name="REQUESTDATE")
    private Date requestDate;

    @Column(name="INVOICENUMBER")
    private String invoiceNumber;

    @Column(name="DOCUMENTNUMBER")
    private String documentNumber;

    @Column(name="DOCUMENTDATE")
    private Date documentDate;

    @Column(name="FIRMA")
    private String firma;

    @Column(name="STORAGE")
    private String storage;

    @Column(name="BOXQTY")
    private Integer boxQty;

    @Column(name="REQUESTSTATUSID")
    private String requestStatusId;

    @Column(name="COMMENTFORSTATUS")
    private String commentForStatus;

    @Column(name="CLIENTID")
    private Integer clientId;

    @Column(name="CLIENTIDEXTERNAL")
    private String clientIdExternal;

    @Column(name="INN")
    private String inn;

    @Column(name="CLIENTNAME")
    private String clientName;

    @Column(name="MARKETAGENTUSERID")
    private Integer marketAgentUserId;

    @Column(name="MARKETAGENTUSERNAME")
    private String marketAgentUserName;

    @Column(name="DRIVERUSERID")
    private Integer driverUserId;

    @Column(name="DRIVERUSERNAME")
    private String driverUserName;

    @Column(name="DELIVERYPOINTID")
    private Integer deliveryPointId;

    @Column(name="DELIVERYPOINTNAME")
    private String deliveryPointName;

    @Column(name = "WAREHOUSEPOINTID")
    private Integer warehousePointId;

    @Column(name="WAREHOUSEPOINTNAME")
    private String warehousePointName;

    @Column(name="LASTVISITEDPOINTID")
    private Integer lastVisitedPointId;

    @Column(name="LASTVISITEDPOINTNAME")
    private String lastVisitedPointName;

    @Column(name="NEXTPOINTID")
    private Integer nextPointId;

    @Column(name="NEXTPOINTNAME")
    private String nextPointName;

    @Column(name="ROUTELISTNUMBER")
    private String routeListNumber;

    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @Column(name="PALLETSQTY")
    private Integer palletsQty;

    @Column(name="ROUTELISTID")
    private Integer routeListId;

    @Column(name="ROUTEID")
    private Integer routeId;

    @Column(name="ROUTENAME")
    private String routeName;

    @Column(name="ARRIVALTIMETONEXTROUTEPOINT")
    private Date arrivalTimeToNextRoutePoint;
}
