package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Data
@Entity
@Table(name="REQUESTS_HISTORY")
public class RequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REQUSTHISTORYID")
    private Long requstHistoryId;

    @Column(name="AUTOTIMEMARK")
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private Date autoTimeMark;

//    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "REQUESTID")
    private Long requestId;

    @Column(name="REQUESTIDEXTERNAL")
    private String requestIdExternal;

    @Column(name="DATASOURCEID")
    @JsonView(DataTablesOutput.View.class)
    @Enumerated(EnumType.STRING)
    private DataSource dataSourceId;

    @Column(name="REQUESTNUMBER")
    private String requestNumber;

    @Column(name="REQUESTDATE")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date requestDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CLIENTID")
//    private Client clientId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "DESTINATIONPOINTID")
//    private Point destinationPointId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "MARKETAGENTUSERID")
//    private User marketAgentUserId;

    @Column(name="INVOICENUMBER")
    private String invoiceNumber;

    @Column(name="INVOICEDATE")
    @JsonFormat(pattern="dd/MM/yyyy")
    private Date invoiceDate;

    @Column(name="DOCUMENTNUMBER")
    private String documentNumber;

    @Column(name="DOCUMENTDATE")
    @JsonFormat(pattern="dd/MM/yyyy")
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
    @JsonFormat(pattern="dd/MM/yyyy")
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
    @JsonFormat(pattern="dd/MM/yyyy HH:mm")
    private Date lastStatusUpdated;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "LASTMODIFIEDBY")
//    private User lastModifiedBy;

    @Column(name="REQUESTSTATUSID")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatusId;

    @Column(name="COMMENTFORSTATUS")
    private String commentForStatus;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "WAREHOUSEPOINTID")
//    private Point warehousePointId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ROUTELISTID")
//    private RouteList routeListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

}
