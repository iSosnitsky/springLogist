package sbat.logist.ru.transport.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.lang.Nullable;
import sbat.logist.ru.constant.DataSource;
import sbat.logist.ru.constant.UserRole;
import sbat.logist.ru.transport.repository.RequestRepository;
import sbat.logist.ru.transport.repository.UserRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
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
    private Integer userID;

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "USERIDEXTERNAL", nullable = false)
    private String userIDExternal = randomAlphaNumeric(10);

    @JsonView(DataTablesOutput.View.class)
    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource = DataSource.ADMIN_PAGE;

    @NotNull
    @JsonView(DataTablesOutput.View.class)
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @Column(name = "SALT")
    private String salt;

    @NotNull
    @Column(name = "PASSANDSALT")
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

    @Formula("CONCAT('','dummy')")
    @JsonView(DataTablesOutput.View.class)
    private String password = "dummy";

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSPORT_COMPANY_ID")
    @JsonView(DataTablesOutput.View.class)
    private TransportCompany transportCompany;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @PrePersist
    private void prePersist() {
        if (this.userRole.equals(UserRole.CLIENT_MANAGER) && this.client == null)
            throw new IllegalArgumentException("Client manager must have attached client");
        else if (this.userRole.equals(UserRole.DISPATCHER) && this.point == null)
            throw new IllegalArgumentException("Dispatcher must have attached point");
        else if (this.userRole.equals(UserRole.TRANSPORT_COMPANY) && this.transportCompany == null)
            throw new IllegalArgumentException("Transport company user must have attached transportCompany");
    }
}
