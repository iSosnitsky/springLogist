package sbat.logist.ru.transport.domain;

import lombok.Data;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@Table(name="REQUESTS_HISTORY")
public class RequestsHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REQUSTHISTORYID")
    private Long requstHistoryId;

    @Column(name="AUTOTIMEMARK")
    private Date autoTimeMark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REQUESTID")
    private Request requestId;

    @Column(name="REQUESTIDEXTERNAL")
    private String requestIdExternal;

    @Column(name="DATASOURSEID")
    private DataSource dataSourceId;

    @Column(name="REQUESTNUMBER")
    private String requestNumber;

    @Column(name="REQUESTDATE")
    private Date requestDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTID")
    private Client clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DESTINATIONPOINTID")
    private Point destinationPointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MARKETAGENTUSERID")
    private User marketAgentUserId;

    @Column(name="INVOICENUMBER")
    private String invoiceNumber;

    @Column(name="INVOICEDATE")
    private Date invoiceDate;

    @Column(name="DOCUMENTNUMBER")
    private String documentNumber;

    @Column(name="DOCUMENTDATE")
    private Date documentDate;

    @Column(name="FIRMA")
    private String firma;

    @Column(name="STORAGE")
    private String storage;

    @Column(name="CONTACTNAME")
    private String contactName;

    @Column(name="CONTACTPHONE")
    private String contactPhone;

    @Column(name="DELIVERYOPTION")
    private String deliveryOption;

    @Column(name="DELIVERYDATE")
    private Date deliveryDate;

    @Column(name="BOXQTY")
    private Integer boxQty;

    @Column(name="WEIGHT")
    private Integer weight;

    @Column(name="VOLUME")
    private Integer volume;

    @Column(name = "GOODSCOST")
    private BigDecimal goodsCost;

    @Column(name="LASTSTATUSUPDATED")
    private Date lastStatusUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTMODIFIEDBY")
    private User lastModifiedBy;

    @Column(name="REQUESTSTATUSID")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatusId;

    @Column(name="COMMENTFORSTATUS")
    private String commentForStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WAREHOUSEPOINTID")
    private Point warehousePointId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTELISTID")
    private RouteList routeListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

}
