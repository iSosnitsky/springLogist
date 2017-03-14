package sbat.logist.ru.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USERID")
    private Long userID;
    @Column(name = "USERIDEXTERNAL")
    private String userIDExternal;
    @Column(name = "DATASOURCEID")
    private String dataSourceID;
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "SALT")
    private String salt;
    @Column(name = "PASSANDSALT")
    private String passAndSalt;
    @Column(name = "USERROLEID")
    private String userRoleID;
    @Column(name = "USERNAME")
    private String userName;
    @Column(name = "PHONENUMBER")
    private String phoneNumber;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "POSITION")
    private String position;
    @Column(name = "POINTID")
    private Long pointID;
    @Column(name = "CLIENTID")
    private Long clientID;
}
