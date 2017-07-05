package sbat.logist.ru.transport;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole {
    @Id
    @Column(name = "USERROLEID")
    @Enumerated(EnumType.STRING)
    private sbat.logist.ru.constant.UserRole userRoleId;

    @Column(name = "USERROLERUSNAME")
    private String userRoleRusname;
}
