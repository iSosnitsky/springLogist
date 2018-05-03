package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.transport.domain.converter.DataSourceConverter;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Table(name="route_lists", indexes = {
        @Index(unique = true, columnList = "ROUTELISTIDEXTERNAL,DATASOURCEID"),
        @Index(columnList = "ROUTEID"),
        @Index(columnList = "STATUS"),
        @Index(columnList = "DRIVERID")
})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RouteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROUTELISTID")
    @JsonView(DataTablesOutput.View.class)
    private Integer routeListId;

    @Column(name="ROUTELISTIDEXTERNAL")
    @JsonView(DataTablesOutput.View.class)
    private String routeListIdExternal;

    @Enumerated(EnumType.STRING)
    @Column(name="DATASOURCEID", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    private DataSource dataSourceId;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTELISTNUMBER", nullable = false)
    private String routeListNumber;
    @JsonView(DataTablesOutput.View.class)
    @Column(name="CREATIONDATE")
    private Date creationDate;
    @JsonView(DataTablesOutput.View.class)
    @Column(name="DEPARTUREDATE")
    private Date departureDate;
    @JsonView(DataTablesOutput.View.class)
    @Column(name="PALLETSQTY")
    private Integer palletsQty;
    @JsonView(DataTablesOutput.View.class)
    @Column(name="FORWARDERID")
    private String forwarderId;


    //TODO: Переписать после водителей
    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DRIVERID")
    private User driverId;

    @Column(name="DRIVERPHONENUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String driverPhoneNumber;

    @Column(name="LICENSEPLATE")
    @JsonView(DataTablesOutput.View.class)
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STATUS")
    @JsonView(DataTablesOutput.View.class)
    private RouteListStatus status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="routeID")
    @JsonView(DataTablesOutput.View.class)
    private Route routeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="transport_company_id")
    @JsonView(DataTablesOutput.View.class)
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="driver_id_internal")
    @JsonView(DataTablesOutput.View.class)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_id")
//    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_2_id")
//    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vehicle_3_id")
//    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle3;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FREIGHT_ID")
    @JsonView(DataTablesOutput.View.class)
    private Freight freight;



    public Route getRouteId() {
        return routeId;
    }

    public TransportCompany getTransportCompany() {
        return transportCompany;
    }

    public Driver getDriver() {
        return driver;
    }

    public Vehicle getVehicle1() {
        return vehicle1;
    }

    public Vehicle getVehicle2() {
        return vehicle2;
    }

    public Vehicle getVehicle3() {
        return vehicle3;
    }

    public Freight getFreight() {
        return freight;
    }

    @OneToMany(mappedBy = "routeListId", fetch = FetchType.LAZY)
    private List<Request> requests = new ArrayList<>(0);

}
