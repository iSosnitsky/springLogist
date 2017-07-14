package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebMvcProperties;
import sbat.logist.ru.constant.DataSource;

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
    @ManyToOne
    @JoinColumn(name = "CLIENTID")
    private Client clientId;

    @ManyToOne
    @JoinColumn(name="DESTINATIONPOINTID")
    private Point destinationPointId;

    @ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "LASTMODIFIEDBY")
    private User lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "REQUESTSTATUSID")
    private RequestStatus requestStatusId;

    @Column(name = "COMMENTFORSTATUS")
    private String commentForStatus;

    @JsonView(WebMvcProperties.View.class)
    @ManyToOne
    @JoinColumn(name= "ROUTELISTID")
    private RouteList routeListId;

    @ManyToOne
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

    @ManyToOne
    @JoinColumn(name = "WAREHOUSEPOINTID")
    private Point warehousePoint;

    @ManyToOne
    @JoinColumn(name = "TRANSPORTCOMPANYID")
    private TransportCompany transportCompany;

    @ManyToOne
    @JoinColumn(name="VEHICLEID")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name="DRIVERID")
    private Driver driver;
}