package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "requests")
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUESTID")
    private Integer id;

    @Column(name = "REQUESTIDEXTERNAL")
    private String externalId;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @Column(name = "REQUESTNUMBER")
    private String requestNumber;

    @Column(name = "REQUESTDATE")
    private Date requestDate;

    //TODO: доделать джойны
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTID")
    private Client clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DESTINATIONPOINTID")
    private Point destinationPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARKETAGENTUSERID")
    private User marketAgentUserId;

    @Column(name = "INVOICENUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICEDATE")
    private Date invoiceDate;

    @Column(name = "DOCUMENTNUMBER")
    private String documentNumber;

    @Column(name = "DOCUMENTDATE")
    private Date documentDate;

    @Column(name = "FIRMA")
    private String firma;

    @Column(name = "STORAGE")
    private String storage;

    @Column(name = "CONTACTNAME")
    private String contactName;

    @Column(name = "CONTACTPHONE")
    private String contactPhone;

    @Column(name = "DELIVERYOPTION")
    private String deliveryOption;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELIVERYDATE")
    private Date deliveryDate;

    @Column(name = "BOXQTY")
    private Integer boxQuantity;

    @Column(name = "WEIGHT")
    private Integer weight;

    @Column(name = "VOLUME")
    private Integer volume;


    @Column(name = "GOODSCOST")
    private BigDecimal goodsCost;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTSTATUSUPDATED")
    private Date lastStatusUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTMODIFIEDBY")
    private User lastModifiedBy;

    @Enumerated(EnumType.STRING)
    @Column(name="REQUESTSTATUSID")
    private RequestStatus requestStatusId;

    @Column(name = "COMMENTFORSTATUS")
    private String commentForStatus;

    @JsonView(WebMvcProperties.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "ROUTELISTID")
    private RouteList routeListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WAREHOUSEPOINTID")
    private Point warehousePoint;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "TRANSPORTCOMPANYID")
//    private TransportCompany transportCompany;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="VEHICLEID")
//    private Vehicle vehicle;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="DRIVERID")
//    private Driver driver;
}