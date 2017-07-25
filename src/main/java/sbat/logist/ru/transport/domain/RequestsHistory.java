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

    @ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "CLIENTID")
    private Client clientId;

    @ManyToOne
    @JoinColumn(name = "DESTINATIONPOINTID")
    private Point destinationPointId;

    @ManyToOne
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

    @ManyToOne
    @JoinColumn(name = "LASTMODIFIEDBY")
    private User lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "REQUESTSTATUSID")
    private RequestStatus requestStatusId;

    @Column(name="COMMENTFORSTATUS")
    private String commentForStatus;

    @ManyToOne
    @JoinColumn(name = "WAREHOUSEPOINTID")
    private Point warehousePointId;

    @ManyToOne
    @JoinColumn(name = "ROUTELISTID")
    private RouteList routeListId;

    @ManyToOne
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

}
