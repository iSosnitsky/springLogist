package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name="route_list_statuses")
public class RouteListStatus {
    @Id
    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTELISTSTATUSID")
    private String routeListStatusId;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="ROUTELISTSTATUSRUSNAME")
    private String routeListStatusRusName;
}
