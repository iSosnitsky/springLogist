package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.RequestStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "requests")
@ToString
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@DynamicInsert(true)
@DynamicUpdate(true)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUESTID")
    @JsonView(DataTablesOutput.View.class)
    private Integer id;

    @Column(name = "REQUESTIDEXTERNAL")
    @JsonView(DataTablesOutput.View.class)
    private String externalId;

    @Column(name = "DATASOURCEID", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "REQUESTNUMBER")
    private String requestNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "REQUESTDATE")
    private Date requestDate;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "CLIENTID")
    private Client clientId;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DESTINATIONPOINTID")
    private Point destinationPointId;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "MARKETAGENTUSERID")
    private User marketAgentUserId;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "INVOICENUMBER")
    private String invoiceNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "INVOICEDATE")
    private Date invoiceDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "DOCUMENTNUMBER")
    private String documentNumber;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "DOCUMENTDATE")
    private Date documentDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "FIRMA")
    private String firma;


    @JsonView(DataTablesOutput.View.class)
    @Column(name = "STORAGE")
    private String storage;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "CONTACTNAME")
    private String contactName;



    @JsonView(DataTablesOutput.View.class)
    @Column(name = "CONTACTPHONE")
    private String contactPhone;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "DELIVERYOPTION")
    private String deliveryOption;

    @JsonView(DataTablesOutput.View.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DELIVERYDATE")
    private Date deliveryDate;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "BOXQTY")
    private Integer boxQuantity;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "WEIGHT")
    private Integer weight;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "VOLUME")
    private Integer volume;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "GOODSCOST")
    private BigDecimal goodsCost;

    @JsonView(DataTablesOutput.View.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LASTSTATUSUPDATED")
    private Date lastStatusUpdated;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTMODIFIEDBY")
    private User lastModifiedBy;

    @JsonView(DataTablesOutput.View.class)
    @Enumerated(EnumType.STRING)
    @Column(name="REQUESTSTATUSID")
    private RequestStatus requestStatusId;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "COMMENTFORSTATUS")
    private String commentForStatus;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "ROUTELISTID")
    private RouteList routeListId;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LASTVISITEDROUTEPOINTID")
    private Point lastVisitedRoutePointId;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WAREHOUSEPOINTID")
    private Point warehousePoint;

    @OneToMany(mappedBy = "requestId")
    private List<RequestHistory> requestHistory = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSPORTCOMPANYID")
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="VEHICLEID")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="DRIVERID")
    private Driver driver;

    @PostPersist
    private void postPersist(){
        //Здесь можно сделать что-нибудь с этой сущностью после её сохранения (INSERT) в бд
        //Например засунуть её копию и все связанные поля в matViewBigSelect
        sbat.logist.ru.constant.UserRole.class.getDeclaringClass().getEnumConstants();

    }

    @PrePersist
    private void prePersist(){
        //Здесь можно сделать что-нибудь с этой сущностью перед её сохранением (INSERT) в бд
    }
}