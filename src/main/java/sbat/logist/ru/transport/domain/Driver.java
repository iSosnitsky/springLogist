package sbat.logist.ru.transport.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="DRIVERS")
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
}
