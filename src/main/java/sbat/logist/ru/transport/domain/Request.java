package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sbat.logist.ru.constant.DataSource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue
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
    @Column(name = "CLIENTID")
    private Integer clientId;

    @Column(name = "DESTINATIONPOINTID")
    private Integer destinationPointId;

    @Column(name = "MARKETAGENTUSERID")
    private Integer marketAgentUserId;

    @Column(name = "INVOICENUMBER")
    private String invoiceNumber;

    @Column(name = "INVOICEDATE")
    private String invoiceDate;

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

    @Column(name = "LASTMODIFIEDBY")
    private Integer lastModifiedBy;

    @Column(name = "REQUESTSTATUSID")
    private String requestStatusId;

    @Column(name = "COMMENTFORSTATUS")
    private String commentForStatus;


 }
