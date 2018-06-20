package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
@Table(name="DRIVERS")
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
@JsonDeserialize()
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="VEHICLE_ID")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="TRANSPORT_COMPANY_ID")
    private TransportCompany transportCompany;

    @Column(name="FULL_NAME")
    private String fullName;

    @Column(name="PASSPORT")
    private String passport;

    @Column(name="PHONE")
    private String phone;

    @Column(name="LICENSE")
    private String license;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "route_lists_to_drivers",
            joinColumns = { @JoinColumn(name = "driver_id") },
            inverseJoinColumns = { @JoinColumn(name = "route_list_id") }
    )
    Set<RouteList> routeLists = new HashSet<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return Objects.equals(id, driver.id) &&
                Objects.equals(fullName, driver.fullName) &&
                Objects.equals(passport, driver.passport) &&
                Objects.equals(phone, driver.phone) &&
                Objects.equals(license, driver.license);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, fullName, passport, phone, license, routeLists);
    }
}
