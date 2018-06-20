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
import java.util.*;

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
    private String model = "";

    @Column(name="CARRYING_CAPACITY")
    @JsonView(DataTablesOutput.View.class)
    private String carryingCapacity;

    @Column(name="VOLUME")
    @JsonView(DataTablesOutput.View.class)
    private String volume;

    @Column(name="LOADING_TYPE")
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private VehicleLoadingType loadingType = VehicleLoadingType.BACK;

    @Column(name="PALLETS_QUANTITY")
    @JsonView(DataTablesOutput.View.class)
    private Integer palletsQuantity;

    @Column(name="TYPE")
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private VehicleType type = VehicleType.TENT;

    @Column(name="WIALON_ID")
    @JsonView(DataTablesOutput.View.class)
    private String wialonId;

    @Column(name="IS_RENTED")
    @JsonView(DataTablesOutput.View.class)
    private short isRented = 0;


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "route_lists_to_vehicles",
            joinColumns = { @JoinColumn(name = "vehicle_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_list_id") }
    )
    List<RouteList> routeLists = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return isRented == vehicle.isRented &&
                Objects.equals(id, vehicle.id) &&
                Objects.equals(vehicleIdExternal, vehicle.vehicleIdExternal) &&
                dataSource == vehicle.dataSource &&
                Objects.equals(licenseNumber, vehicle.licenseNumber) &&
                Objects.equals(model, vehicle.model) &&
                Objects.equals(carryingCapacity, vehicle.carryingCapacity) &&
                Objects.equals(volume, vehicle.volume) &&
                loadingType == vehicle.loadingType &&
                Objects.equals(palletsQuantity, vehicle.palletsQuantity) &&
                type == vehicle.type &&
                Objects.equals(wialonId, vehicle.wialonId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, vehicleIdExternal, dataSource, licenseNumber, model, carryingCapacity, volume, loadingType, palletsQuantity, type, wialonId, isRented);
    }
}
