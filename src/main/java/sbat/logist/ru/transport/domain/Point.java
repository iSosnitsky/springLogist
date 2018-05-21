package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.PointType;

import javax.persistence.*;
import java.sql.Time;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "points")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINTID")
    @JsonView(DataTablesOutput.View.class)
    private Long pointId;

    @Column(name = "POINTIDEXTERNAL")
    @JsonView(DataTablesOutput.View.class)
    private String pointIdExternal;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private DataSource dataSource;

    @Column(name = "POINTNAME")
    @JsonView(DataTablesOutput.View.class)
    private String pointName;

    @JsonView(DataTablesOutput.View.class)
    @Column(name="REGION")
    private String region;

    @Column(name = "TIMEZONE")
    @JsonView(DataTablesOutput.View.class)
    private String timeZone;

    @Column(name="DOCS")
    @JsonView(DataTablesOutput.View.class)
    private Integer docs;

    @Column(name="COMMENTS")
    @JsonView(DataTablesOutput.View.class)
    private String comments;

    @Column(name = "OPENTIME")
    @JsonView(DataTablesOutput.View.class)
    private Time openTime;

    @Column(name = "CLOSETIME")
    @JsonView(DataTablesOutput.View.class)
    private Time closeTime;

    @JsonView(DataTablesOutput.View.class)
    private String district;
    @JsonView(DataTablesOutput.View.class)
    private String locality;

    @Column(name = "MAILINDEX")
    @JsonView(DataTablesOutput.View.class)
    private String mailIndex;

    @JsonView(DataTablesOutput.View.class)
    private String address;
    @JsonView(DataTablesOutput.View.class)
    private String email;

    @Column(name = "PHONENUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String phoneNumber;

    @Column(name = "RESPONSIBLEPERSONID")
    @JsonView(DataTablesOutput.View.class)
    private String responsiblePersonId;

    @Column(name = "POINTTYPEID", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonView(DataTablesOutput.View.class)
    private PointType pointTypeId;

}
