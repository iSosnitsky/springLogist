package sbat.logist.ru.transport.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sbat.logist.ru.constant.DataSource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
@SecondaryTables(value = {
        @SecondaryTable(name="clients", pkJoinColumns={@PrimaryKeyJoinColumn(name="CLIENTID")}),
        @SecondaryTable(name="users", pkJoinColumns={@PrimaryKeyJoinColumn(name = "USERID")})}
)
public class PseudoMatView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUESTID")
    private Integer id;

    @NotNull
    @Column(table = "clients", name = "CLIENTIDEXTERNAL")
    private String clientIDExternal;

    @Column(table = "clients", name = "DATASOURCEID", nullable = false)
    @Enumerated(EnumType.STRING)
    private DataSource dataSource;

    @Column(table = "users", name = "USERNAME")
    private String userName;
    @Column(table = "users", name = "PHONENUMBER")
    private String phoneNumber;
    @Column(table = "users", name = "EMAIL")
    private String email;
}
