package sbat.logist.ru.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.constant.DataSource;

import javax.persistence.*;
import java.sql.Date;

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
public class RouteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ROUTELISTID")
    private Long routeListId;

    @Column(name="ROUTELISTIDEXTERNAL")
    private String routeListIdExternal;

    @Enumerated(EnumType.STRING)
    @Column(name="DATASOURCEID", nullable = false)
    private DataSource dataSourceId;


    @Column(name="ROUTELISTNUMBER", nullable = false)
    private String routeListNumber;

    @Column(name="CREATIONDATE")
    private Date creationDate;

    @Column(name="DEPARTUREDATE")
    private Date departureDate;

    @Column(name="PALLETSQTY")
    private Integer palletsQty;

    @Column(name="FORWARDERID")
    private String forwarderId;


    //TODO: Переписать после водителей
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DRIVERID")
    private User driverId;

    @Column(name="DRIVERPHONENUMBER")
    private String driverPhoneNumber;

    @Column(name="LICENSEPLATE")
    private String licensePlate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STATUS")
    private RouteListStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ROUTEID")
    private Route routeId;
}
