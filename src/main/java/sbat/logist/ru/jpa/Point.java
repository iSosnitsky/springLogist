package sbat.logist.ru.jpa;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@NoArgsConstructor
@Table(name = "points")
public class Point {
    @Id
    @GeneratedValue
    @Column(name = "POINTID")
    private Long pointId;

    @Column(name = "POINTIDEXTERNAL")
    private String pointIdExternal;

    @ManyToOne
    @JoinColumn(name = "DATASOURCEID")
    private DataSource dataSource;

    @Column(name = "POINTNAME")
    private String pointName;

    private String region;

    @Column(name = "TIMEZONE")
    private String timeZone;

    private Integer docs;
    private String comments;

    @Column(name = "OPENTIME")
    private Time openTime;

    @Column(name = "CLOSETIME")
    private Time closeTime;

    private String district;
    private String locality;

    @Column(name = "MAILINDEX")
    private String mailIndex;

    private String address;
    private String email;

    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Column(name = "RESPONSIBLEPERSONID")
    private String responsiblePersonId;

    @Column(name = "POINTTYPEID")
    private String pointTypeId;

}
