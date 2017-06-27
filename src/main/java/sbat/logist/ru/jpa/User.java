package sbat.logist.ru.jpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    @GeneratedValue
    @Column(name = "USERID")
    private Long userID;


    @NotNull
    @Column(name = "USERIDEXTERNAL")
    private String userIDExternal;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "DATASOURCEID")
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "USERROLEID")
    private UserRole userRole;

    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PHONENUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "POSITION")
    private String position;

    @ManyToOne
    @JoinColumn(name = "POINTID")
    private Point point;

    @ManyToOne
    @JoinColumn(name = "CLIENTID")
    private Client client;
}
