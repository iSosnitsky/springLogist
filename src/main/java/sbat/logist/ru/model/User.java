package sbat.logist.ru.model;

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

@Data
@Entity
@NoArgsConstructor
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

    @Column(name = "USERIDEXTERNAL")
    private String userIDExternal;

    @ManyToOne
    @JoinColumn(name = "DATASOURCEID")
    private DataSource dataSource;

    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SALT")
    private String salt;
    @Column(name = "PASSANDSALT")
    private String passAndSalt;

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
