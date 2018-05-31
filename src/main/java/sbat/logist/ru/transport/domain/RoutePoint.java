package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@Table(name = "route_points")
public class RoutePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTEPOINTID")
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "SORTORDER")
    private int sortOrder;

    @JsonView(DataTablesOutput.View.class)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINTID")
    private Point point;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "ROUTEID")
    private int route;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "TIMEFORLOADINGOPERATIONS")
    private int timeForLoadingOperations;

}
