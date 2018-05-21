package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;

@Entity
@Table(name = "FREIGHT")
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@Builder
public class Freight {
    @Id
    @Column(name="FREIGHT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSPORT_COMPANY_ID")
    @JsonView(DataTablesOutput.View.class)
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DRIVER_ID")
    @JsonView(DataTablesOutput.View.class)
    private Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_ID")
    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_2_ID")
    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VEHICLE_3_ID")
    @JsonView(DataTablesOutput.View.class)
    private Vehicle vehicle3;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    @JsonView(DataTablesOutput.View.class)
    private Route route;

    @Column(name = "DISTANCE")
    @JsonView(DataTablesOutput.View.class)
    private Integer distance;

    @Column(name = "CONTINUANCE")
    @JsonView(DataTablesOutput.View.class)
    private Integer continuance;

    @Column(name = "STALL_HOURS")
    @JsonView(DataTablesOutput.View.class)
    private Integer stallHours;


    @Column(name = "UNIQUE_ADDRESSES")
    @JsonView(DataTablesOutput.View.class)
    private Integer uniqueAdresses;

    @Column(name = "TOTAL_BOX_AMOUNT")
    @JsonView(DataTablesOutput.View.class)
    private Integer totalBoxAmount;

    @Column(name = "SPEED_READINGS_END")
    @JsonView(DataTablesOutput.View.class)
    private Integer speedReadingsEnd;

    @Column(name = "SPEED_READINGS_BEGIN")
    @JsonView(DataTablesOutput.View.class)
    private Integer speedReadingsBegin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="STATUS_ID")
    @JsonView(DataTablesOutput.View.class)
    private RouteListStatus status;

    @Column(name="FREIGHT_NUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String freightNumber;

    @Column(name="FUEL_CONSUMPTION")
    @JsonView(DataTablesOutput.View.class)
    private Integer fuelConsumption;

}
