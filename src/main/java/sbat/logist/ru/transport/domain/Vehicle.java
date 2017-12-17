package sbat.logist.ru.transport.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.VehicleLoadingType;
import sbat.logist.ru.constant.VehicleType;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Data
@Table(name="VEHICLES")
public class Vehicle {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="VEHICLE_ID_EXTERNAL")
    private String vehicleIdExternal;

    @Column(name="DATA_SOURCE_ID")
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TRANSPORT_COMPANY_ID")
    private TransportCompany transportCompany;

    @Column(name="LICENSE_NUMBER")
    private String licenseNumber;

    @Column(name="MODEL")
    private String model;

    @Column(name="CARRYING_CAPACITY")
    private String carryingCapacity;

    @Column(name="VOLUME")
    private String volume;

    @Column(name="LOADING_TYPE")
    private VehicleLoadingType loadingType;

    @Column(name="PALLETS_QUANTITY")
    private Integer palletsQuantity;

    @Column(name="TYPE")
    private VehicleType type;

    @Column(name="WIALON_ID")
    private String wialonId;
}
