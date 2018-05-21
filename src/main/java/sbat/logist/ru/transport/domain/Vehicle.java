package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.VehicleLoadingType;
import sbat.logist.ru.constant.VehicleType;
import sbat.logist.ru.transport.domain.converter.VehicleTypeConverter;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@Entity
@Data
@Table(name="VEHICLES")
public class Vehicle {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DataTablesOutput.View.class)
    private Long id;

    @Column(name="VEHICLE_ID_EXTERNAL")
    @JsonView(DataTablesOutput.View.class)
    private String vehicleIdExternal;

    @Column(name="DATA_SOURCE_ID")
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TRANSPORT_COMPANY_ID")
    private TransportCompany transportCompany;

    @Column(name="LICENSE_NUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String licenseNumber;

    @Column(name="MODEL")
    @JsonView(DataTablesOutput.View.class)
    private String model;

    @Column(name="CARRYING_CAPACITY")
    @JsonView(DataTablesOutput.View.class)
    private String carryingCapacity;

    @Column(name="VOLUME")
    @JsonView(DataTablesOutput.View.class)
    private String volume;

    @Column(name="LOADING_TYPE")
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private VehicleLoadingType loadingType;

    @Column(name="PALLETS_QUANTITY")
    @JsonView(DataTablesOutput.View.class)
    private Integer palletsQuantity;

    @Column(name="TYPE")
    @Convert(converter = VehicleTypeConverter.class)
    @JsonView(DataTablesOutput.View.class)
    private VehicleType type;

    @Column(name="WIALON_ID")
    @JsonView(DataTablesOutput.View.class)
    private String wialonId;
}
