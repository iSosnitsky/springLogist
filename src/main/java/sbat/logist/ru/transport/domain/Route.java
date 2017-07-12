package sbat.logist.ru.transport.domain;

import lombok.Data;
import sbat.logist.ru.constant.DataSource;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@Table(name = "routes", indexes = {
        @Index(unique = true, columnList = "DIRECTIONIDEXTERNAL,DATASOURCEID"),
        @Index(unique = true, columnList = "ROUTENAME"),
        @Index(columnList = "TARIFFID"),
        @Index(columnList = "DATASOURCEID")
})
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTEID")
    private Integer id;

    @Column(name = "DIRECTIONIDEXTERNAL", nullable = false)
    private String externalId;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @Column(name = "ROUTENAME", nullable = false)
    private String routeName;

    @Column(name = "FIRSTPOINTARRIVALTIME", nullable = false)
    private Time time;

    @Column(name = "DAYSOFWEEK", nullable = false)
    private String daysOfWeek;

    @Column(name = "TARIFFID")
    private Integer tariffId;
}
