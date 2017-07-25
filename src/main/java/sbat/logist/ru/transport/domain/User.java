package sbat.logist.ru.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Column(name = "USERID")
    private Long userID;

    @NotNull
    @Column(name = "USERIDEXTERNAL")
    private String userIDExternal;

    @Column(name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @NotNull
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @Column(name = "SALT")
    private String salt;

    @NotNull
    @Column(name = "PASSANDSALT")
    private String passAndSalt;

    @Column(name = "USERROLEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PHONENUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "POSITION")
    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINTID")
    private Point point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTID")
    private Client client;
}
