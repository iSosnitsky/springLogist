package sbat.logist.ru.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@Table(name = "user_roles")
public class UserRole {
    @Id
    @Column(name = "USERROLEID")
    private String userRoleId;

    @Column(name = "USERROLERUSNAME")
    private String userRoleRusname;
}
