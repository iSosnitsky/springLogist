package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.UserRole;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "client_id_index", columnList = "CLIENTID"),
        @Index(name = "data_source_index", columnList = "DATASOURCEID"),
        @Index(name = "login_unique_index", columnList = "LOGIN", unique = true),
        @Index(name = "point_id_index", columnList = "POINTID"),
        @Index(name = "from_unique_index", columnList = "USERIDEXTERNAL,DATASOURCEID", unique = true),
        @Index(name = "role_index", columnList = "USERROLEID"),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "USERID")
    private Long userID;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "USERIDEXTERNAL")
    private String userIDExternal;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "SALT")
    private String salt;

    @NotNull
    @Column(name = "PASSANDSALT")
    @JsonView(DataTablesOutput.View.class)
    private String passAndSalt;

    @Column(name = "USERROLEID", nullable = false)
    @JsonView(DataTablesOutput.View.class)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "USERNAME")
    @JsonView(DataTablesOutput.View.class)
    private String userName;
    @Column(name = "PHONENUMBER")
    @JsonView(DataTablesOutput.View.class)
    private String phoneNumber;
    @Column(name = "EMAIL")
    @JsonView(DataTablesOutput.View.class)
    private String email;
    @Column(name = "POSITION")
    @JsonView(DataTablesOutput.View.class)
    private String position;


    //TODO: make lazy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINTID")
    @JsonView(DataTablesOutput.View.class)
    private Point point;

    //TODO: make lazy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTID")
    @JsonView(DataTablesOutput.View.class)
    private Client client;
}
