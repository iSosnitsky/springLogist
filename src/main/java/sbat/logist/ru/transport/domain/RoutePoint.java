package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.transport.EntityListeners.RoutePointListener;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@NoArgsConstructor
@EntityListeners(RoutePointListener.class)
@Table(name = "route_points")
public class RoutePoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROUTEPOINTID")
    private Long id;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "SORTORDER")
    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINTID")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTEID")
    private Route route;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "TIMEFORLOADINGOPERATIONS")
    private int timeForLoadingOperations;

    @Transient
    @Getter
    @JsonView(DataTablesOutput.View.class)
    private String pointName = "Fgsfds";

    @PostLoad
    private void postLoad(){
        Hibernate.initialize(this.point);
        this.pointName = this.point.getPointName();
    }
}
