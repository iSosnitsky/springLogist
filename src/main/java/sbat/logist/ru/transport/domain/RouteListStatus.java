package sbat.logist.ru.transport.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="route_list_statuses")
public class RouteListStatus {
    @Id
    @Column(name="ROUTELISTSTATUSID")
    private String routeListStatusId;

    @Column(name="ROUTELISTSTATUSRUSNAME")
    private String routeListStatusRusName;
}
