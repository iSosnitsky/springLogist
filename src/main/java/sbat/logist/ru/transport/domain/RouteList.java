package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
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
    @JsonView(DataTablesOutput.View.class)
    private Long routeListId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ROUTEID")
    @JsonView(DataTablesOutput.View.class)
    private Route routeId;
}
