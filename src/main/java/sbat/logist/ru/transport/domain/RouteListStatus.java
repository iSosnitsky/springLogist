package sbat.logist.ru.transport.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="ROUTE_LIST_STATUSES")
public class RouteListStatus {
    @Id
    @Column(name="ROUTELISTSTATUSID")
    private String routeListStatusId;

    @Column(name="ROUTELISTSTATUSRUSNAME")
    private String routeListStatusRusName;
}
