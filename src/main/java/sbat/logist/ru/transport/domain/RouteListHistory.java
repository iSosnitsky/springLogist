package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.tools.javac.code.Attribute;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.jdo.annotations.*;
import javax.persistence.*;
import javax.persistence.Column;
import java.sql.Date;

@Data
@Entity
@Table(name="ROUTE_LIST_HISTORY")
public class RouteListHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROUTELISTHISTORYID")
    private Long routeListHistoryID;

    @Column(name="TIMEMARK")
    private Date TimeMark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTELISTID")
    private RouteList routeListID;

    @Column(name="ROUTELISTIDEXTERNAL")
    private String routeListIDExternal;

    @Column(name="DATASOURCEID")
    @Enumerated(EnumType.STRING)
    private DataSource dataSourceId;

    @Column(name="ROUTELISTNUMBER")
    private String routeListNumber;

    @Column(name="CREATIONDATE")
    private Date creationDate;

    @Column(name="DEPARTUREDATE")
    private Date departureDate;

    @Column(name="PALLETSQTY")
    private Integer palletsQty;

    @Column(name="FORWARDERID")
    private String forwarderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DRIVERID")
    private User driverId;

    @Column(name="DRIVERPHONENUMBER")
    private String driverPhoneNumber;

    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STATUS")
    @JsonView(DataTablesOutput.View.class)
    private RouteListStatus status;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="routeID")
    private Route routeId;
// аа
     @Column (name="DUTYSTATUS")   @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
}
