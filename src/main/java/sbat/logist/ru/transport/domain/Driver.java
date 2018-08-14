package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="DRIVERS")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="TRANSPORT_COMPANY_ID")
    private Integer transportCompanyId;

    @Column(name="FULL_NAME")
    private String fullName;

    @Column(name="PASSPORT")
    private String passport;

    @Column(name="PHONE")
    private String phone;

    @Column(name="LICENSE")
    private String license;

    @JsonIgnore
    @Column(name="LAST_TIME_MODIFIED")
    private Date lastTimeModified;
}
