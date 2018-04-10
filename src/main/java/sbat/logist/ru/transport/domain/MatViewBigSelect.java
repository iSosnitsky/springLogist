package sbat.logist.ru.transport.domain;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

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
    @JsonView(DataTablesOutput.View.class)
    @Id
    @Column(name = "REQUESTID")
    private Long requestID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="REQUESTIDEXTERNAL")
    private String requestIDExternal;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="REQUESTNUMBER")
    private String requestNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="REQUESTDATE")
    private Date requestDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="INVOICENUMBER")
    private String invoiceNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="INVOICEDATE")
    private Date invoiceDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="requestStatusRusName")
    private String requestStatusRusName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DOCUMENTNUMBER")
    private String documentNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DOCUMENTDATE")
    private Date documentDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="FIRMA")
    private String firma;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="STORAGE")
    private String storage;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="BOXQTY")
    private Integer boxQty;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="REQUESTSTATUSID")
    private String requestStatusID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="COMMENTFORSTATUS")
    private String commentForStatus;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="CLIENTID")
    private Integer clientID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="CLIENTIDEXTERNAL")
    private String clientIDExternal;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="INN")
    private String INN;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="CLIENTNAME")
    private String clientName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="MARKETAGENTUSERID")
    private Integer marketAgentUserID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="MARKETAGENTUSERNAME")
    private String marketAgentUserName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DRIVERUSERID")
    private Integer driverUserID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DRIVERUSERNAME")
    private String driverUserName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DELIVERYPOINTID")
    private Integer deliveryPointID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="DELIVERYPOINTNAME")
    private String deliveryPointName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "WAREHOUSEPOINTID")
    private Integer warehousePointID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="WAREHOUSEPOINTNAME")
    private String warehousePointName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="LASTVISITEDPOINTID")
    private Integer lastVisitedPointID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="LASTVISITEDPOINTNAME")
    private String lastVisitedPointName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="NEXTPOINTID")
    private Integer nextPointID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="NEXTPOINTNAME")
    private String nextPointName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTELISTNUMBER")
    private String routeListNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="PALLETSQTY")
    private Integer palletsQty;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTELISTID")
    private Integer routeListID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTEID")
    private Integer routeID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTENAME")
    private String routeName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ARRIVALTIMETONEXTROUTEPOINT")
    private Date arrivalTimeToNextRoutePoint;
}

