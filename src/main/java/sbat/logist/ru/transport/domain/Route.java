package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.util.generator.RandomStringGenerator;
import org.hibernate.type.SetType;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @JsonView(DataTablesOutput.View.class)
    private String externalId = "LSS-"+RandomStringGenerator.randomAlphaNumeric(10);

    @Column(name = "DATASOURCEID", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource = DataSource.ADMIN_PAGE;

    @Column(name = "ROUTENAME", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    private String routeName;

    @Column(name = "FIRSTPOINTARRIVALTIME", nullable = false)
    private Time firstPointArrivalTime;

    @Column(name = "DAYSOFWEEK")
    private String daysOfWeek;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "route")
    List<RoutePoint> routePoints;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonView(DataTablesOutput.View.class)
    @JoinColumn(name = "TARIFFID")
    private Tariff tariff;
}
